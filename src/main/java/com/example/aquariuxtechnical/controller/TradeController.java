package com.example.aquariuxtechnical.controller;

import com.example.aquariuxtechnical.dto.TradeRequestDTO;
import com.example.aquariuxtechnical.entity.TradeTransaction;
import com.example.aquariuxtechnical.service.TradeService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/trade")
public class TradeController {

    @Autowired
    private TradeService tradeService;

    @PostMapping("/execute")
    public ResponseEntity<?> executeTrade(@RequestParam Long userId, @RequestBody TradeRequestDTO tradeRequest) {
        log.info("Trading request received for userId: {}", userId);
        try {
            TradeTransaction transaction = tradeService.executeTrade(userId, tradeRequest);
            return ResponseEntity.ok(transaction);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/history")
    public List<TradeTransaction> getUserTradingHistory(@RequestParam Long userId) {
        log.info("Trading history request received for userId: {}", userId);
        return tradeService.getUserTradingHistory(userId);
    }
}
