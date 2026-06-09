package com.balaji.distributor.DTO;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class CheckoutSessionResponse {

    private Long sessionId;

    private double totalAmount;

    private String paymentMethod;

    private String paymentStatus;

    private String transactionId;

    private String paymentUrl;

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime paidAt;

    private Long orderId;


}