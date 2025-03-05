package com.example.aquariuxtechnical.controller;

import com.example.aquariuxtechnical.service.WalletService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/wallets")
public class WalletController {
    @Autowired
    private WalletService walletService;

    @GetMapping
    public ResponseEntity<?> getUserWallets(@RequestParam long userId) {
        return ResponseEntity.ok(walletService.getUserWallets(userId));
    }
}
