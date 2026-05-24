package com.example.kiteconnectapp.dto;

import lombok.Data;

@Data
public class MarginLegRequest {
    private String exchange;
    private String tradingSymbol;
    private String transactionType;
    private String variety;
    private String product;
    private String orderType;
    private Integer quantity;
    private Double price;
    private Double triggerPrice;
}
