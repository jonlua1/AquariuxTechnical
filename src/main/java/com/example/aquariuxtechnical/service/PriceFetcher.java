package com.example.aquariuxtechnical.service;

import com.example.aquariuxtechnical.dto.BinancePriceResponse;
import com.example.aquariuxtechnical.dto.HuobiPriceResponse;
import com.example.aquariuxtechnical.dto.HuobiTicker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PriceFetcher {
    private static final Logger logger = LoggerFactory.getLogger(PriceFetcher.class);

    private static final String BINANCE_URL = "https://api.binance.com/api/v3/ticker/bookTicker";
    private static final String HUOBI_URL = "https://api.huobi.pro/market/tickers";

    @Autowired
    private RestTemplate template;

    public void fetchPrices(String pair) {
        logger.info("Fetching price for {}", pair);

        double bestBid = 0.0;
        double bestAsk = Double.MAX_VALUE;

        try {
            // Fetch prices from Binance
            ResponseEntity<BinancePriceResponse[]> binanceResponse = template.getForEntity(BINANCE_URL, BinancePriceResponse[].class);

            if (binanceResponse.getBody() != null) {
                for (BinancePriceResponse price: binanceResponse.getBody()) {
                    if (price.getSymbol().equalsIgnoreCase(pair)) {
                        logger.debug("Binance {} pair bid price: {}, ask price: {}", pair, price.getBidPrice(), price.getAskPrice());
                        bestBid = Math.max(bestBid, price.getBidPrice());
                        bestAsk = Math.min(bestAsk, price.getAskPrice());
                    }
                }
            }

            // Fetch prices from Huobi
            ResponseEntity<HuobiPriceResponse> huobiResponse = template.getForEntity(HUOBI_URL, HuobiPriceResponse.class);
            if (huobiResponse.getBody() != null && huobiResponse.getBody().getData() != null) {
                for (HuobiTicker ticker: huobiResponse.getBody().getData()) {
                    if (ticker.getSymbol().equalsIgnoreCase(pair)) {
                        logger.debug("Huobi {} pair bid price: {}, ask price: {}", pair, ticker.getBid(), ticker.getAsk());
                        bestBid = Math.max(bestBid, ticker.getBid());
                        bestAsk = Math.min(bestAsk, ticker.getAsk());
                    }
                }
            }
            logger.info("Best {} pair bid price: {}, ask price: {}", pair, bestBid, bestAsk);
        }
        catch (Exception e) {
            logger.error("Error fetching price for {}: {}", pair, e.getMessage());
        }
    }
}
