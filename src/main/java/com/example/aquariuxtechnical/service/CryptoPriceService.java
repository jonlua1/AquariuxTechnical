package com.example.aquariuxtechnical.service;

import com.example.aquariuxtechnical.entity.CryptoPrice;
import com.example.aquariuxtechnical.exception.PriceNotFoundException;
import com.example.aquariuxtechnical.repository.CryptoPriceRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CryptoPriceService {
    @Autowired
    private CryptoPriceRepository priceRepository;

    @Transactional
    public CryptoPrice findBySymbol(String symbol){
        return priceRepository.findBySymbol(symbol)
                .orElseThrow(() -> new PriceNotFoundException(symbol));
    }
}
