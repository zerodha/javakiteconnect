package com.example.kiteconnectapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class MarginCalculationRequest {
    private List<MarginLegRequest> legs;
    private Boolean considerPositions;
    private Boolean mode;
}
