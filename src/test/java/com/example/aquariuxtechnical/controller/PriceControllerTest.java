package com.example.aquariuxtechnical.controller;

import com.example.aquariuxtechnical.entity.CryptoPrice;
import com.example.aquariuxtechnical.exception.PriceNotFoundException;
import com.example.aquariuxtechnical.service.CryptoPriceService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.time.LocalDateTime;

import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PriceController.class)
public class PriceControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private CryptoPriceService priceService;

    @Test
    public void testGetLatestPrice_Success() throws Exception {
        String symbol = "BTCUSDT";

        CryptoPrice mockPrice = new CryptoPrice(symbol, 90000.00, 91000.00, LocalDateTime.now());
        Mockito.when(priceService.findBySymbol(symbol)).thenReturn(mockPrice);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/price/{symbol}", symbol))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.symbol").value(symbol))
                .andExpect(jsonPath("$.bidPrice").value(90000))
                .andExpect(jsonPath("$.askPrice").value(91000))
                .andDo(print());
    }

    @Test
    public void testGetLatestPrice_PriceNotFound() throws Exception {
        String symbol = "INVALID";

        Mockito.when(priceService.findBySymbol(symbol)).thenThrow(new PriceNotFoundException(symbol));

        mockMvc.perform(MockMvcRequestBuilders.get("/api/price/{symbol}", symbol))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.details").value("No price found for symbol: INVALID"))
                .andExpect(jsonPath("$.message").value("Price Not Available"))
                .andDo(print());
    }
}
