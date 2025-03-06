package com.example.aquariuxtechnical.dto;

import lombok.Data;

@Data
public class TradeRequestDTO {
    private String symbol;

    private double quantity;

    private String type;        // BUY or SELL
}
