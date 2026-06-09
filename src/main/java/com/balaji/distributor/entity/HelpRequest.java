package com.balaji.distributor.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class HelpRequest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String mobileno;

    private String email;

    private String topic;

    @Column(length = 2000)
    private String message;

    private String status = "PENDING";

    private LocalDateTime timestamp = LocalDateTime.now();

    // USER RELATION

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
