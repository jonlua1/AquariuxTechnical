package com.example.aquariuxtechnical.service;

import com.example.aquariuxtechnical.dto.TradeRequestDTO;
import com.example.aquariuxtechnical.entity.CryptoPrice;
import com.example.aquariuxtechnical.entity.TradeTransaction;
import com.example.aquariuxtechnical.entity.UserWallet;
import com.example.aquariuxtechnical.exception.PriceNotFoundException;
import com.example.aquariuxtechnical.repository.CryptoPriceRepository;
import com.example.aquariuxtechnical.repository.TradeTransactionRepository;
import com.example.aquariuxtechnical.repository.UserWalletRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class TradeService {
    @Autowired
    private CryptoPriceRepository priceRepository;

    @Autowired
    private UserWalletRepository walletRepository;

    @Autowired
    private TradeTransactionRepository transactionRepository;

    @Transactional
    public TradeTransaction executeTrade(Long userId, TradeRequestDTO request){
        String symbol = request.getSymbol();
        double quantity = request.getQuantity();
        String type = request.getType().toUpperCase();

        if (!type.equals("BUY") && !type.equals("SELL")){
            throw new IllegalArgumentException("Invalid trade type. Allowed values: BUY / SELL");
        }

        CryptoPrice latestPrice = priceRepository.findBySymbol(symbol)
                                    .orElseThrow(() -> new PriceNotFoundException(symbol));

        double tradePrice = request.getType().equalsIgnoreCase("BUY")
                            ? latestPrice.getAskPrice() : latestPrice.getBidPrice();

        double totalValue = tradePrice * quantity;

        UserWallet userWallet = walletRepository.findByUserIdAndSymbol(userId, symbol)
                .orElse(new UserWallet(null, userId, symbol, 0));

        UserWallet usdtWallet = walletRepository.findByUserIdAndSymbol(userId, "USDT")
                                            .orElseThrow(() -> new RuntimeException("USDT wallet not found"));

        if (type.equals("BUY")) {
            if (usdtWallet.getBalance() < totalValue) {
                throw new RuntimeException("Insufficient USDT balance");
            }
            usdtWallet.setBalance(usdtWallet.getBalance() - totalValue);
            userWallet.setBalance(userWallet.getBalance() + quantity);
        } else {
            if (userWallet.getBalance() < quantity) {
                throw new RuntimeException("Insufficient " + symbol + " balance");
            }

            usdtWallet.setBalance(usdtWallet.getBalance() + totalValue);
            userWallet.setBalance(userWallet.getBalance() - quantity);
        }

        walletRepository.save(usdtWallet);
        walletRepository.save(userWallet);

        TradeTransaction transaction = new TradeTransaction();
        transaction.setUserId(userId);
        transaction.setSymbol(symbol);
        transaction.setPrice(tradePrice);
        transaction.setTimestamp(LocalDateTime.now());
        transaction.setQuantity(quantity);
        transaction.setType(type);
        transaction.setTotalCost(totalValue);

        return transactionRepository.save(transaction);
    }
}
