package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.service.PortfolioService;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/portfolio")
public class PortfolioController {

    @Autowired
    private PortfolioService portfolioService;

    @GetMapping("/holdings")
    public ResponseEntity<List<Holding>> getHoldings() {
        return ResponseEntity.ok(portfolioService.getHoldings());
    }

    @GetMapping("/positions")
    public ResponseEntity<Map<String, List<Position>>> getPositions() {
        return ResponseEntity.ok(portfolioService.getPositions());
    }

    @GetMapping("/margins")
    public ResponseEntity<Map<String, Margin>> getMargins() {
        return ResponseEntity.ok(portfolioService.getMargins());
    }

    @GetMapping("/profile")
    public ResponseEntity<Profile> getProfile() {
        return ResponseEntity.ok(portfolioService.getProfile());
    }
}
