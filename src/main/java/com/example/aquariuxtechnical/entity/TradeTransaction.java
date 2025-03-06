package com.example.aquariuxtechnical.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Table(name = "trade_transaction")
@Data
public class TradeTransaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(nullable = false)
    private String symbol;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private double quantity;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private double totalCost;

    @Column(nullable = false)
    private LocalDateTime timestamp;
}
