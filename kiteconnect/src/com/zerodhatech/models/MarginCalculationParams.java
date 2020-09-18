package com.zerodhatech.models;

/**
 * A wrapper for margin calculation API's params to be sent while sending request.
 */
public class MarginCalculationParams {
    /**
     * Tradingsymbol of the instrument  (ex. RELIANCE, INFY).
     */
    public String tradingSymbol;
    /**
     * Exchange in which instrument is listed (NSE, BSE, NFO, BFO, CDS, MCX).
     */
    public String exchange;
    /**
     * Transaction type (BUY or SELL).
     */
    public String transactionType;
    /** Variety (regular, co, amo)*/
    public String variety;
    /**
     * Product code (NRML, MIS, CNC).
     */
    public String product;
    /**
     * Order type (LIMIT, SL, SL-M, MARKET).
     */
    public String orderType;
    /**
     * Order quantity
     */
    public int quantity;
    /**
     * Order Price
     */
    public double price;
    /**
     * Trigger price
     */
    public double triggerPrice;
}
