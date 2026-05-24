package com.example.kiteconnectapp.dto;

import lombok.Data;

@Data
public class OrderRequest {
    private String exchange;
    private String tradingSymbol;
    private String transactionType;
    private Integer quantity;
    private Double price;
    private String product;
    private String orderType;
    private String validity;
    private Integer disclosedQuantity;
    private Double triggerPrice;
    private String variety;
    private String tag;
}
