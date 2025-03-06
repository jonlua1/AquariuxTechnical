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
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
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

        log.info("Executing trade - User: {}, Symbol: {}, Quantity: {}, Type: {}", userId, symbol, quantity, type);

        if (!type.equals("BUY") && !type.equals("SELL")){
            log.error("Invalid trade type: {}", type);
            throw new IllegalArgumentException("Invalid trade type. Allowed values: BUY / SELL");
        }

        CryptoPrice latestPrice = priceRepository.findBySymbol(symbol)
                                    .orElseThrow(() -> {
                                        log.error("No price found for symbol: {}", symbol);
                                        return new PriceNotFoundException(symbol);
                                    });

        double tradePrice = request.getType().equalsIgnoreCase("BUY")
                            ? latestPrice.getAskPrice() : latestPrice.getBidPrice();

        double totalValue = tradePrice * quantity;

        UserWallet userWallet = walletRepository.findByUserIdAndSymbol(userId, symbol)
                .orElse(new UserWallet(null, userId, symbol, 0));

        UserWallet usdtWallet = walletRepository.findByUserIdAndSymbol(userId, "USDT")
                                            .orElseThrow(() -> {
                                                log.warn("USDT wallet not found for userId: {}", userId);
                                                return new RuntimeException("USDT wallet not found");
                                            });

        if (type.equals("BUY")) {
            if (usdtWallet.getBalance() < totalValue) {
                log.warn("Insufficient USDT balance for userId: {} current balance: {}, buying: {}", userId, usdtWallet.getBalance(), totalValue);
                throw new RuntimeException("Insufficient USDT balance");
            }
            usdtWallet.setBalance(usdtWallet.getBalance() - totalValue);
            userWallet.setBalance(userWallet.getBalance() + quantity);
        } else {
            if (userWallet.getBalance() < quantity) {
                log.warn("Insufficient {} balance for userId: {} current balance: {}, selling: {}", symbol, userId, userWallet.getBalance(), quantity);
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

        log.info("Trade executed successfully: {}", transaction);
        return transactionRepository.save(transaction);
    }

    public List<TradeTransaction> getUserTradingHistory(Long userId) {
        log.info("Retrieving trading history for userId: {}", userId);
        return transactionRepository.findByUserId(userId, Sort.by(Sort.Direction.DESC, "timestamp"));
    }
}
