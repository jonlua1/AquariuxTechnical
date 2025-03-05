package com.example.aquariuxtechnical.service;

import com.example.aquariuxtechnical.dto.BinancePriceResponseDTO;
import com.example.aquariuxtechnical.dto.HuobiPriceResponseDTO;
import com.example.aquariuxtechnical.dto.HuobiTickerDTO;
import com.example.aquariuxtechnical.entity.CryptoPrice;
import com.example.aquariuxtechnical.repository.CryptoPriceRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PriceFetcher {
    private static final Logger logger = LoggerFactory.getLogger(PriceFetcher.class);

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String HUOBI_URL = "https://api.huobi.pro/market/tickers";

    @Autowired
    private RestTemplate template;

    @Autowired
    private CryptoPriceRepository priceRepository;

    public void fetchAndStoreBestPrices(String symbol) {
        logger.info("Fetching price for {}", symbol);

        double bestBid = 0.0;
        double bestAsk = Double.MAX_VALUE;

        try {
            // Fetch prices from Binance
            ResponseEntity<BinancePriceResponseDTO[]> binanceResponse = template.getForEntity(BINANCE_URL, BinancePriceResponseDTO[].class);

            if (binanceResponse.getBody() != null) {
                for (BinancePriceResponseDTO price: binanceResponse.getBody()) {
                    if (price.getSymbol().equalsIgnoreCase(symbol)) {
                        logger.debug("Binance {} bid price: {}, ask price: {}", symbol, price.getBidPrice(), price.getAskPrice());
                        bestBid = Math.max(bestBid, price.getBidPrice());
                        bestAsk = Math.min(bestAsk, price.getAskPrice());
                    }
                }
            }

            // Fetch prices from Huobi
            ResponseEntity<HuobiPriceResponseDTO> huobiResponse = template.getForEntity(HUOBI_URL, HuobiPriceResponseDTO.class);
            if (huobiResponse.getBody() != null && huobiResponse.getBody().getData() != null) {
                for (HuobiTickerDTO ticker: huobiResponse.getBody().getData()) {
                    if (ticker.getSymbol().equalsIgnoreCase(symbol)) {
                        logger.debug("Huobi {} bid price: {}, ask price: {}", symbol, ticker.getBid(), ticker.getAsk());
                        bestBid = Math.max(bestBid, ticker.getBid());
                        bestAsk = Math.min(bestAsk, ticker.getAsk());
                    }
                }
            }

            Optional<CryptoPrice> existingPrice = priceRepository.findBySymbol(symbol);
            CryptoPrice price = existingPrice.orElse(new CryptoPrice());
            price.setSymbol(symbol);
            price.setBidPrice(bestBid);
            price.setAskPrice(bestAsk);
            price.setUpdatedAt(LocalDateTime.now());

            priceRepository.save(price);
            logger.info("Updated price for {}: Bid: {}, Ask: {}", symbol, bestBid, bestAsk);
        }
        catch (Exception e) {
            logger.error("Error fetching price for {}: {}", symbol, e.getMessage());
        }
    }
}
