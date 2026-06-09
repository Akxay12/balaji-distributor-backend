package com.balaji.distributor.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Basket {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Integer basketNumber;


    private String status; // ACTIVE / SAVED

    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(
            mappedBy = "basket",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    private List<BasketItem> items =
            new ArrayList<>();

}