package com.balaji.distributor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CheckoutSession {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    // USER

    @ManyToOne
    private User user;

    // BASKET

    @ManyToOne
    private Basket basket;

    // TOTALS

    private double subtotal;

    private double gstPercentage;

    private double gstAmount;

    private double shippingCharges;

    private double totalAmount;

    // PAYMENT

    private String paymentMethod;

    // PENDING
    // PAID
    // FAILED
    // EXPIRED

    private String paymentStatus;

    // TRANSACTION

    private String transactionId;

    // SHIPPING SNAPSHOT

    private String fullName;

    private String mobile;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    // PAYMENT URL

    private String paymentUrl;

    // TIME

    private LocalDateTime createdAt;

    private LocalDateTime expiresAt;

    private LocalDateTime paidAt;

    private Long orderId;


}