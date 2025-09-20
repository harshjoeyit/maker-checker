package com.example.testproject.repo;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.testproject.entity.TransferRequest;
import com.example.testproject.entity.TransferStatus;

@Repository
public interface TransferRequestRepository extends JpaRepository<TransferRequest, Long> {
    List<TransferRequest> findByStatus(TransferStatus status);
    List<TransferRequest> findByMakerId(String makerId);
    List<TransferRequest> findByCheckerId(String checkerId);

    @Query("SELECT tr FROM TransferRequest tr WHERE tr.status = 'PENDING' AND tr.expiresAt < :currentTime")
    List<TransferRequest> findExpiredPendingRequests(LocalDateTime currentTime);
}