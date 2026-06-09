package com.balaji.distributor.entity;


import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
public class Banner {

    @Id
    private Long id = 1L;

    private String offerText;

    private String title;

    @Column(length = 1000)
    private String description;

    @Column(length = 1000)
    private String subtitle;

    @Column(length = 2000)
    private String imageUrl;

    private Boolean isActive = false;
}
