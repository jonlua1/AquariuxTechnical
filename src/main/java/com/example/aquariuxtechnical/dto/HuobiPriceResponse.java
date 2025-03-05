package com.example.aquariuxtechnical.dto;

import lombok.Data;

import java.util.List;

//{
//  "data":[{...}],
//  "status": "ok",
//  "ts": 1741168418770
//}

@Data
public class HuobiPriceResponse {
    private List<HuobiTicker> data;
}
