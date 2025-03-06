package com.example.aquariuxtechnical.repository;

import com.example.aquariuxtechnical.entity.TradeTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
}
