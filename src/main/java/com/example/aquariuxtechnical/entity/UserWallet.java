package com.example.aquariuxtechnical.entity;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "user_wallet")
@Data
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user_id", nullable = false)
    private long userId;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private double balance;
}
