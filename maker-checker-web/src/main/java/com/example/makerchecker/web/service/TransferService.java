package com.example.makerchecker.web.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.makerchecker.app.entity.AuditAction;
import com.example.makerchecker.app.entity.Role;
import com.example.makerchecker.app.entity.TransferRequest;
import com.example.makerchecker.app.entity.TransferStatus;
import com.example.makerchecker.app.entity.User;
import com.example.makerchecker.app.exception.MakerCheckerException;
import com.example.makerchecker.app.repo.TransferRequestRepository;
import com.example.makerchecker.app.repo.UserRepository;

@Service
@Transactional
public class TransferService {

    @Autowired
    private TransferRequestRepository transferRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AuditService auditService;

    // Maker operations
    public TransferRequest createTransferRequest(String fromAccount, String toAccount,
            BigDecimal amount, String description, String makerId) {

        // Validate maker
        validateMakerOperation(makerId);

        // Validations
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new MakerCheckerException("Amount must be greater than zero");
        }

        TransferRequest request = new TransferRequest(fromAccount, toAccount, amount, description, makerId);
        TransferRequest saved = transferRepository.save(request);

        auditService.logAction(saved.getId(), makerId, AuditAction.CREATED, null,
                toAccount, description);

        return saved;
    }

    public TransferRequest updateTransferRequest(Long requestId, String newFromAccount,
            String newToAccount, BigDecimal newAmount, String newDescription, String makerId) {

        TransferRequest request = transferRepository.findById(requestId)
                .orElseThrow(() -> new MakerCheckerException("Transfer request not found"));

        // Only maker can modify and only if pending
        if (!request.getMakerId().equals(makerId)) {
            throw new MakerCheckerException("Only the original maker can modify this request");
        }

        if (request.getStatus() != TransferStatus.PENDING) {
            throw new MakerCheckerException("Only pending requests can be modified");
        }

        String oldValue = request.toString();

        // Update request details
        request.setFromAccount(newFromAccount);
        request.setToAccount(newToAccount);
        request.setAmount(newAmount);
        request.setDescription(newDescription);

        TransferRequest updated = transferRepository.save(request);

        // Audit log
        auditService.logAction(updated.getId(), makerId, AuditAction.MODIFIED,
                oldValue, updated.toString(), "Transfer request modified");

        return updated;
    }

    // Checker operations
    public TransferRequest approveTransferRequest(Long requestId, String checkerId) {

        TransferRequest request = transferRepository.findById(requestId)
                .orElseThrow(() -> new MakerCheckerException("Transfer request not found"));

        // Validate checker role and four-eyes principle
        validateCheckerOperation(request, checkerId);

        request.setStatus(TransferStatus.APPROVED);
        request.setCheckerId(checkerId);
        request.setCheckedAt(LocalDateTime.now());

        TransferRequest saved = transferRepository.save(request);

        // Audit log
        auditService.logAction(saved.getId(), checkerId, AuditAction.APPROVED,
                TransferStatus.PENDING.toString(), TransferStatus.APPROVED.toString(),
                "Transfer request approved");

        return saved;
    }

    public TransferRequest rejectTransferRequest(Long requestId, String checkerId, String reason) {
        TransferRequest request = transferRepository.findById(requestId)
                .orElseThrow(() -> new MakerCheckerException("Transfer request not found"));

        // Validate checker role and four-eyes principle
        validateCheckerOperation(request, checkerId);

        request.setStatus(TransferStatus.REJECTED);
        request.setCheckerId(checkerId);
        request.setCheckedAt(LocalDateTime.now());
        request.setRejectionReason(reason);

        TransferRequest saved = transferRepository.save(request);

        // Audit log
        auditService.logAction(saved.getId(), checkerId, AuditAction.REJECTED,
                TransferStatus.PENDING.toString(), TransferStatus.REJECTED.toString(),
                "Transfer request rejected: " + reason);

        return saved;
    }

    // Query operations
    public List<TransferRequest> getPendingRequests() {
        return transferRepository.findByStatus(TransferStatus.PENDING);
    }

    public List<TransferRequest> getRequestsByMaker(String makerId) {
        return transferRepository.findByMakerId(makerId);
    }

    public List<TransferRequest> getRequestsByChecker(String checkerId) {
        return transferRepository.findByCheckerId(checkerId);
    }

    public TransferRequest getRequestById(Long requestId) {
        return transferRepository.findById(requestId)
                .orElseThrow(() -> new MakerCheckerException("Transfer request not found"));
    }

    public List<TransferRequest> getAllRequests() {
        return transferRepository.findAll();
    }

    /*
     * Validates maker operations including role
     */
    private void validateMakerOperation(String makerId) {
        validateUserRole(makerId, Role.MAKER);
    }

    /*
     * Validates checker operations including role, four-eyes principle, status, and
     * expiry
     */
    private void validateCheckerOperation(TransferRequest request, String checkerId) {
        // Validate checker role
        validateUserRole(checkerId, Role.CHECKER);

        // Four-eyes principle: checker cannot be the maker
        if (request.getMakerId().equals(checkerId)) {
            throw new MakerCheckerException("Checker cannot be the same as maker (four-eyes principle)");
        }

        // Only pending requests can be checked
        if (request.getStatus() != TransferStatus.PENDING) {
            throw new MakerCheckerException("Only pending requests can be approved/rejected");
        }

        // Check if request has expired
        if (request.getExpiresAt().isBefore(LocalDateTime.now())) {
            expirePendingRequest(request);
            throw new MakerCheckerException("Request has expired");
        }
    }

    /*
     * Validates that the user exists and has the expected role
     */
    private void validateUserRole(String userId, Role expectedRole) {
        User user = userRepository.findByUsername(userId)
                .orElseThrow(() -> new MakerCheckerException("User not found: " + userId));

        if (!user.getRoles().contains(expectedRole)) {
            throw new MakerCheckerException("User " + userId + " does not have role " + expectedRole);
        }
    }

    /*
     * Auto-expiry handling
     */
    public void expirePendingRequests() {
        List<TransferRequest> expiredRequests = transferRepository
                .findExpiredPendingRequests(LocalDateTime.now());

        for (TransferRequest request : expiredRequests) {
            expirePendingRequest(request);
        }
    }

    private void expirePendingRequest(TransferRequest request) {
        request.setStatus(TransferStatus.EXPIRED);
        transferRepository.save(request);

        // Audit log
        auditService.logAction(request.getId(), "SYSTEM", AuditAction.EXPIRED,
                TransferStatus.PENDING.toString(), TransferStatus.EXPIRED.toString(),
                "Request expired automatically");
    }
}