package com.example.aquariuxtechnical.scheduler;

import com.example.aquariuxtechnical.service.PriceFetcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PriceScheduler {
    private static final Logger logger = LoggerFactory.getLogger(PriceScheduler.class);

    @Autowired
    PriceFetcher priceFetcher;

    @Scheduled(fixedRate = 10000)
    public void updatePrices(){
        logger.info("Scheduler triggered");
        priceFetcher.fetchPrices("ETHUSDT");
        priceFetcher.fetchPrices("BTCUSDT");
    }

}
