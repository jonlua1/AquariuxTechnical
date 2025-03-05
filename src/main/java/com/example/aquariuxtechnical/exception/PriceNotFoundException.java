package com.example.aquariuxtechnical.exception;

public class PriceNotFoundException extends RuntimeException {
    public PriceNotFoundException(String symbol) {
        super("No price found for symbol: " + symbol);
    }
}