package com.example.aquariuxtechnical.controller;

import com.example.aquariuxtechnical.dto.ErrorResponseDTO;
import com.example.aquariuxtechnical.entity.CryptoPrice;
import com.example.aquariuxtechnical.exception.PriceNotFoundException;
import com.example.aquariuxtechnical.service.CryptoPriceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/api/price")
public class PriceController {
    @Autowired
    private CryptoPriceService priceService;

    @GetMapping("/{symbol}")
    public ResponseEntity<?> getLatestPrice(@PathVariable String symbol) {
        try {
            log.info("Fetching latest price for {}", symbol);
            CryptoPrice price = priceService.findBySymbol(symbol.toUpperCase());
            return ResponseEntity.ok(price);
        } catch (PriceNotFoundException e) {
            log.warn("No price found for {}", symbol);
            ErrorResponseDTO errorResponse = ErrorResponseDTO.create(
                    "Price Not Available",
                    e.getMessage(),
                    HttpStatus.NOT_FOUND);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorResponse);
        }
    }
}
