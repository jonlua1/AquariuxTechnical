package com.example.aquariuxtechnical.dto;

import lombok.Data;

//{
//    "symbol": "ETHUSDT",
//    "bidPrice": "2238.09000000",
//    "bidQty": "92.44670000",
//    "askPrice": "2238.10000000",
//    "askQty": "12.98830000"
//}

@Data
public class BinancePriceResponse {
    private String symbol;
    private double bidPrice;
    private double askPrice;
}
