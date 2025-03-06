package com.example.aquariuxtechnical.repository;

import com.example.aquariuxtechnical.entity.TradeTransaction;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TradeTransactionRepository extends JpaRepository<TradeTransaction, Long> {
    List<TradeTransaction> findByUserId(Long userId, Sort sort);
}
