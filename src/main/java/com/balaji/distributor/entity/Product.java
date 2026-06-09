package com.balaji.distributor.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String category;

    private double price;

    private double discount;

    private double stock;

    private String unit;


    @Column(length = 5000)
    private String imageUrl;

    private String description;


}