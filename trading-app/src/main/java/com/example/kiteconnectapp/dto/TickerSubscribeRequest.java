package com.example.kiteconnectapp.dto;

import lombok.Data;

import java.util.List;

@Data
public class TickerSubscribeRequest {
    private List<Long> tokens;
    private String mode;
}
