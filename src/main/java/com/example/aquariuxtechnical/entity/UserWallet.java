package com.example.aquariuxtechnical.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_wallet")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserWallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private double balance;
}
