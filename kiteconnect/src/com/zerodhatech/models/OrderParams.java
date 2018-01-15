package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

public class OrderParams {
    /**
     * Exchange in which instrument is listed (NSE, BSE, NFO, BFO, CDS, MCX).
     */
    public String exchange;

    /**
     * Tradingsymbol of the instrument  (ex. RELIANCE, INFY).
     */
    public String tradingsymbol;

    /**
     * Transaction type (BUY or SELL).
     */
    public String transactionType;

    /**
     * Order quantity
     */
    public Integer quantity;

    /**
     * Order Price
     */
    public Double price;

    /**
     * Product code (NRML, MIS, CNC).
     */
    public String product;

    /**
     * Order type (NRML, SL, SL-M, MARKET).
     */
    public String orderType;

    /**
     * Order validity (DAY, IOC).
     */
    public String validity;

    /**
     * Disclosed quantity
     */
    public Integer disclosedQuantity;

    /**
     * Trigger price
     */
    public Double triggerPrice;

    /**
     * Square off value (only for bracket orders)
     */
    public Double squareoffValue;

    /**
     * Stoploss value (only for bracket orders)
     */
    public Double stoplossValue;

    /**
     * Trailing stoploss value (only for bracket orders)
     */
    public Double trailingStoploss;
}