package com.zerodhatech.models;

/** A wrapper for order params to be sent while placing an order.*/
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
     * Order type (LIMIT, SL, SL-M, MARKET).
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
    public Double squareoff;

    /**
     * Stoploss value (only for bracket orders)
     */
    public Double stoploss;

    /**
     * Trailing stoploss value (only for bracket orders)
     */
    public Double trailingStoploss;

    /**
     * Tag: field for users to tag orders. It accepts alphanumeric 20 character String values.
     */
     public String tag;

     /**
      * Parent order id is used to send order modify request.
      */
     public String parentOrderId;

     /**
      * Custom validity user can enter which denotes time to live in minutes.
      */
     public int validityTTL;

     /**
      * Split quantity for each iceberg leg order.
      */
     public int icebergQuantity;

     /**
      * Total number of legs for iceberg order type. (number of legs per Iceberg should be between 2 and 10)
      */
     public  int icebergLegs;

     /**
      * Auction number
      * */
     public String auctionNumber;
}