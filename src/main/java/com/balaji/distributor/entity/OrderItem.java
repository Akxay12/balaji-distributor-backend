package com.balaji.distributor.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_item")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(
            strategy = GenerationType.IDENTITY
    )
    private Long id;

    // ORDER

    @ManyToOne
    @JoinColumn(name = "order_id")
    @JsonBackReference
    private Order order;

    // PRODUCT SNAPSHOT

    private Long productId;

    private String productName;

    private String imageUrl;

    private double productPrice;

    private double discountAtPurchase;

    private String unit;

    private int quantity;

    // FINAL TOTAL OF THIS ITEM

    private double finalPrice;
}