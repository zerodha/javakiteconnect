package com.zerodhatech.models;

import java.util.List;

/**
 * Created by sujith on 9/23/19.
 */
public class GTTParams {
    /**
     * Tradingsymbol of the instrument  (ex. RELIANCE, INFY).
     */
    public String tradingsymbol;
    /**
     * Exchange in which instrument is listed (NSE, BSE, NFO, BFO, CDS, MCX).
     */
    public String exchange;
    /** A unique instrument token which is assigned to each instrument, can be found in the instrument master dump.*/
    public int instrumentToken;

    public String triggerType;

    public double lastPrice;

    /** List of target or stop-loss orders*/
    public List<GTTOrderParams> orders;

    public List<Double> triggerPrices;

    public class GTTOrderParams{
        /**
         * Order quantity
         */
        public int quantity;
        /**
         * Order Price
         */
        public double price;
        /**
         * Order type (LIMIT, SL, SL-M, MARKET).
         */
        public String orderType;
        /**
         * Product code (NRML, MIS, CNC).
         */
        public String product;

        /** Transaction type (BUY, SELL)
         * */
        public String transactionType;
    }
}
