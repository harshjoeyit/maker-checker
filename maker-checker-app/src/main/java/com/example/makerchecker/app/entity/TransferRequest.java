package com.example.makerchecker.app.entity;

import java.math.BigDecimal;
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
@Table(name = "transfer_requests")
public class TransferRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "from_account", nullable = false)
    private String fromAccount;
    
    @Column(name = "to_account", nullable = false)
    private String toAccount;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal amount;
    
    @Column(length = 500)
    private String description;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransferStatus status = TransferStatus.PENDING;

     @Column(name = "maker_id", nullable = false)
    private String makerId;
    
    @Column(name = "checker_id")
    private String checkerId;
    
    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
    
    @Column(name = "checked_at")
    private LocalDateTime checkedAt;
    
    @Column(name = "expires_at")
    private LocalDateTime expiresAt;
    
    @Column(name = "rejection_reason")
    private String rejectionReason;
    
    public TransferRequest(String fromAccount, String toAccount, BigDecimal amount, 
                          String description, String makerId) {
        this.fromAccount = fromAccount;
        this.toAccount = toAccount;
        this.amount = amount;
        this.description = description;
        this.makerId = makerId;
        this.expiresAt = LocalDateTime.now().plusHours(24); // 24-hour expiry
    }

    public String toString() {
        return "TransferRequest{" +
                "id=" + id +
                ", fromAccount='" + fromAccount + '\'' +
                ", toAccount='" + toAccount + '\'' +
                ", amount=" + amount +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", makerId='" + makerId + '\'' +
                ", checkerId='" + checkerId + '\'' +
                ", createdAt=" + createdAt +
                ", checkedAt=" + checkedAt +
                ", expiresAt=" + expiresAt +
                ", rejectionReason='" + rejectionReason + '\'' +
                '}';
    }
}
