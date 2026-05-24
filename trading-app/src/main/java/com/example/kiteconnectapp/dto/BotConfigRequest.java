package com.example.kiteconnectapp.dto;

import lombok.Data;

@Data
public class BotConfigRequest {
    private String botId;
    private String name;
    private String exchange;
    private String tradingSymbol;
    private String transactionType;
    private String product;
    private String orderType;
    private Integer quantity;
    private Integer intervalSeconds;
    private Boolean enabled;
    private Boolean dryRun;
    private Integer maxOrdersPerDay;
    private Integer maxPositionSize;
    private String startTime;
    private String endTime;
}
