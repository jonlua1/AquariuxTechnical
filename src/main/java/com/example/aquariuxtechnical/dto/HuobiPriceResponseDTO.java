package com.example.aquariuxtechnical.dto;

import lombok.Data;

import java.util.List;

//{
//  "data":[{...}],
//  "status": "ok",
//  "ts": 1741168418770
//}

@Data
public class HuobiPriceResponseDTO {
    private List<HuobiTickerDTO> data;
}
