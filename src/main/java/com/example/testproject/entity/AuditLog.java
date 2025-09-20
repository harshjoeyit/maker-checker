package com.example.testproject.entity;

import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "audit_logs")
public class AuditLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "transfer_request_id")
    private Long transferRequestId;
    
    @Column(name = "user_id", nullable = false)
    private String userId;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuditAction action;

    @Column(name = "old_value", length = 1000)
    private String oldValue;
    
    @Column(name = "new_value", length = 1000)
    private String newValue;
    
    @Column(name = "timestamp")
    private LocalDateTime timestamp = LocalDateTime.now();

    @Column(length = 500)
    private String comments;
    
    // Constructor
    public AuditLog(Long transferRequestId, String userId, AuditAction action, 
                   String oldValue, String newValue, String comments) {
        this.transferRequestId = transferRequestId;
        this.userId = userId;
        this.action = action;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.comments = comments;
    }
}
