package com.example.makerchecker.web.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.makerchecker.web.service.TransferService;

@Component
public class ExpiryScheduler {
    @Autowired
    private TransferService transferService;

    @Scheduled(fixedRate = 600000) // Run every 10 minutes (600000 ms)
    public void expirePendingRequests() {
        System.out.println("Running expiry scheduler...");
        transferService.expirePendingRequests();
    }
}
