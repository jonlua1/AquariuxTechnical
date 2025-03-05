package com.example.aquariuxtechnical.repository;

import com.example.aquariuxtechnical.entity.CryptoPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CryptoPriceRepository extends JpaRepository<CryptoPrice, String> {
    Optional<CryptoPrice> findBySymbol(String symbol);
}
