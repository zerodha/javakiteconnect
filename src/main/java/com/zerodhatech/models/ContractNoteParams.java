package com.zerodhatech.models;

/**
 * A wrapper for virtual contract note API's params to be sent while sending request.
 */
public class ContractNoteParams {
    /**
     * order ID that is received in the orderbook.
     */
    public String orderID;
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

    /** Variety (regular, co, amo, iceberg)*/
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
     * Order quantity.
     */
    public int quantity;
    /**
     * Average price of the order.
     */
    public double averagePrice;
}
