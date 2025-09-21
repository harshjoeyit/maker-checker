package com.example.makerchecker.web.controller;

import java.util.List;

import org.aspectj.lang.annotation.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.makerchecker.app.entity.AuditLog;
import com.example.makerchecker.app.entity.TransferRequest;
import com.example.makerchecker.web.dto.TransferRequestDto;
import com.example.makerchecker.web.service.AuditService;
import com.example.makerchecker.web.service.TransferService;

@RestController
@RequestMapping("/api/transfers")
public class TransferController {

    @Autowired
    private TransferService transferService;

    @Autowired
    private AuditService auditService;

    @GetMapping("")
    @Before("Get all transfer requests API")
    public ResponseEntity<List<TransferRequest>> getAllRequests() {
        return ResponseEntity.ok(transferService.getAllRequests());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TransferRequest> getRequestById(@PathVariable Long id) {
        return ResponseEntity.ok(transferService.getRequestById(id));
    }

    // Maker endpoints
    @PostMapping("")
    public ResponseEntity<TransferRequest> createTransferRequest(@RequestBody TransferRequestDto dto) {
        TransferRequest request = transferService.createTransferRequest(
                dto.getFromAccount(), dto.getToAccount(), dto.getAmount(),
                dto.getDescription(), dto.getMakerId());

        return ResponseEntity.ok(request);
    }

    @PutMapping("/{id}")
    public ResponseEntity<TransferRequest> updateTransfer(@PathVariable Long id,
            @RequestBody TransferRequestDto dto) {
        TransferRequest request = transferService.updateTransferRequest(
                id, dto.getFromAccount(), dto.getToAccount(), dto.getAmount(),
                dto.getDescription(), dto.getMakerId());
        return ResponseEntity.ok(request);
    }

    @GetMapping("/maker/{makerId}")
    public ResponseEntity<List<TransferRequest>> getRequestsByMaker(@PathVariable String makerId) {
        return ResponseEntity.ok(transferService.getRequestsByMaker(makerId));
    }

    // Checker endpoints
    @PostMapping("/approve/{transferRequestId}")
    public ResponseEntity<TransferRequest> approveTransfer(@PathVariable Long transferRequestId,
            @RequestParam String checkerId) {
        TransferRequest request = transferService.approveTransferRequest(transferRequestId, checkerId);
        return ResponseEntity.ok(request);
    }

    @PostMapping("/reject/{transferRequestId}")
    public ResponseEntity<TransferRequest> rejectTransfer(@PathVariable Long transferRequestId,
            @RequestParam String checkerId,
            @RequestParam String reason) {
        TransferRequest request = transferService.rejectTransferRequest(transferRequestId, checkerId, reason);
        return ResponseEntity.ok(request);
    }

    @GetMapping("/pending")
    public ResponseEntity<List<TransferRequest>> getPendingRequests() {
        return ResponseEntity.ok(transferService.getPendingRequests());
    }

    @GetMapping("/checker/{checkerId}")
    public ResponseEntity<List<TransferRequest>> getRequestsByChecker(@PathVariable String checkerId) {
        return ResponseEntity.ok(transferService.getRequestsByChecker(checkerId));
    }

    @GetMapping("/audit/{transferRequestId}")
    public ResponseEntity<List<AuditLog>> getAuditTrail(@PathVariable Long transferRequestId) {
        return ResponseEntity.ok(auditService.getAuditTrailForRequest(transferRequestId));
    }

    @GetMapping("/audit/user/{userId}")
    public ResponseEntity<List<AuditLog>> getUserAuditTrail(@PathVariable String userId) {
        return ResponseEntity.ok(auditService.getAuditTrailForUser(userId));
    }
}
