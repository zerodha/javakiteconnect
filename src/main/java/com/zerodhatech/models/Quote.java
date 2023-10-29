package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * A wrapper for quote.
 */
public class Quote {

    @SerializedName("volume")
    public double volumeTradedToday;
    @SerializedName("last_quantity")
    public double lastTradedQuantity;
    @SerializedName("last_trade_time")
    public Date lastTradedTime;
    @SerializedName("net_change")
    public double change;
    @SerializedName("oi")
    public double oi;
    @SerializedName("sell_quantity")
    public double sellQuantity;
    @SerializedName("last_price")
    public double lastPrice;
    @SerializedName("buy_quantity")
    public double buyQuantity;
    @SerializedName("ohlc")
    public OHLC ohlc;
    @SerializedName("instrument_token")
    public long instrumentToken;
    @SerializedName("timestamp")
    public Date timestamp;
    @SerializedName("average_price")
    public double averagePrice;
    @SerializedName("oi_day_high")
    public double oiDayHigh;
    @SerializedName("oi_day_low")
    public double oiDayLow;
    @SerializedName("depth")
    public MarketDepth depth;
    @SerializedName("lower_circuit_limit")
    public double lowerCircuitLimit;
    @SerializedName("upper_circuit_limit")
    public double upperCircuitLimit;
}
