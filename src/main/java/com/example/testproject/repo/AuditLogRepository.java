package com.example.testproject.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.testproject.entity.AuditLog;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, Long> {
    List<AuditLog> findByTransferRequestIdOrderByTimestampDesc(Long transferRequestId);
    List<AuditLog> findByUserIdOrderByTimestampDesc(String userId);
}
