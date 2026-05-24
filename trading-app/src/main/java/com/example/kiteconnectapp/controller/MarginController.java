package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.dto.MarginCalculationRequest;
import com.example.kiteconnectapp.service.MarginService;
import com.zerodhatech.models.CombinedMarginData;
import com.zerodhatech.models.MarginCalculationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/margin")
public class MarginController {

    @Autowired
    private MarginService marginService;

    @PostMapping("/calculate")
    public ResponseEntity<List<MarginCalculationData>> calculate(@RequestBody MarginCalculationRequest request) {
        return ResponseEntity.ok(marginService.calculateMargins(request));
    }

    @PostMapping("/calculate/combined")
    public ResponseEntity<CombinedMarginData> calculateCombined(@RequestBody MarginCalculationRequest request) {
        return ResponseEntity.ok(marginService.calculateCombinedMargins(request));
    }
}
