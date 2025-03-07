package com.example.aquariuxtechnical.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Table(name = "crypto_price")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CryptoPrice {
    @Id
    private String symbol;              //"ETHUSDT" or "BTCUSDT"

    private double bidPrice;            //Highest bid price (Sell orders)

    private double askPrice;            //Lowest ask price (Buy orders)

    private LocalDateTime updatedAt;    //Latest price update timestamp
}
