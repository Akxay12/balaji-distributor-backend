package com.balaji.distributor.repository;


import com.balaji.distributor.entity.CheckoutSession;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;

public interface CheckoutSessionRepository
        extends JpaRepository<
        CheckoutSession,
        Long
        > {


    @Modifying
    @Query("""
DELETE FROM CheckoutSession s
""")
    void deleteAllSessions();
}