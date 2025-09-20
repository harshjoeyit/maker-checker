package com.example.testproject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.testproject.entity.AuditAction;
import com.example.testproject.entity.AuditLog;
import com.example.testproject.repo.AuditLogRepository;

@Service
public class AuditService {
    
    @Autowired
    private AuditLogRepository auditRepository;

    public void logAction(Long transferRequestId, String userId, AuditAction action,
                         String oldValue, String newValue, String comments) {
        
        AuditLog log = new AuditLog(transferRequestId, userId, action, oldValue, newValue, comments);
        auditRepository.save(log);
    }

    public List<AuditLog> getAuditTrailForRequest(Long transferRequestId) {
        return auditRepository.findByTransferRequestIdOrderByTimestampDesc(transferRequestId);
    }
    
    public List<AuditLog> getAuditTrailForUser(String userId) {
        return auditRepository.findByUserIdOrderByTimestampDesc(userId);
    }

    public List<AuditLog> getAllAuditLogs() {
        return auditRepository.findAll();
    }
}