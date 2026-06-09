package com.balaji.distributor.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "orders")
@Data
@NoArgsConstructor

public class Order {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    // USER

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // ORDER ITEMS

    @OneToMany(
            mappedBy = "order",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @JsonManagedReference
    private List<OrderItem> items =
            new ArrayList<>();

    // ORDER SUMMARY

    private double subtotal;

    private double gstPercentage;

    private double gstAmount;

    private double shippingCharges;

    private double totalAmount;

    // PAYMENT

    private String paymentMethod;

    // STATUS

    private String status;

    // SHIPPING SNAPSHOT

    private String fullName;

    private String mobile;

    private String addressLine1;

    private String addressLine2;

    private String city;

    private String state;

    private String pincode;

    // CREATED

    private LocalDateTime createdAt;
}
