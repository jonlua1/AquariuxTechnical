package com.example.aquariuxtechnical.dto;

import lombok.Data;

//{
//  "symbol": "ethusdt",
//  "open": 2073.66,
//  "high": 2262.1,
//  "low": 1991.66,
//  "close": 2236.85,
//  "amount": 56571.3877493527,
//  "vol": 120083696.144891,
//  "count": 88521,
//  "bid": 2236.83,
//  "bidSize": 3.2468,
//  "ask": 2236.84,
//  "askSize": 0.0057
//}

@Data
public class HuobiTicker {
    private String symbol;
    private double bid;
    private double ask;
}
