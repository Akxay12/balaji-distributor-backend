package com.balaji.distributor.service;

import com.balaji.distributor.repository.CheckoutSessionRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CheckoutSessionCleanupService {

    private final CheckoutSessionRepository
            sessionRepo;


    @Scheduled(
            cron = "0 0 0 * * *"
    )
    @Transactional
    public void cleanupOldSessions() {

        sessionRepo.deleteAllSessions();

        System.out.println(
                "All checkout sessions deleted"
        );
    }
}