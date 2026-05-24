package com.example.kiteconnectapp.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BotOrderLog {
    private String botId;
    private String action;
    private String status;
    private String detail;
    private Instant timestamp;
}
