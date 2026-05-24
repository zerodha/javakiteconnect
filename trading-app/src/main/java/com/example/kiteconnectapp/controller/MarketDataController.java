package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.service.MarketDataService;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/market")
public class MarketDataController {

    @Autowired
    private MarketDataService marketDataService;

    @GetMapping("/quotes")
    public ResponseEntity<Map<String, Quote>> getQuotes(@RequestParam String symbols) {
        String[] instruments = symbols.split(",");
        return ResponseEntity.ok(marketDataService.getQuotes(instruments));
    }

    @GetMapping("/instruments")
    public ResponseEntity<List<Instrument>> getInstruments() {
        return ResponseEntity.ok(marketDataService.getInstruments());
    }

    @GetMapping("/historical")
    public ResponseEntity<HistoricalData> getHistoricalData(
            @RequestParam String instrumentToken,
            @RequestParam String interval,
            @RequestParam String from,
            @RequestParam String to) {
        return ResponseEntity.ok(marketDataService.getHistoricalData(instrumentToken, interval, from, to));
    }

    @GetMapping("/tokens")
    public ResponseEntity<Map<String, Long>> getInstrumentTokens(@RequestParam String symbols) {
        List<String> items = Arrays.asList(symbols.split(","));
        return ResponseEntity.ok(marketDataService.getInstrumentTokensBySymbols(items));
    }
}
