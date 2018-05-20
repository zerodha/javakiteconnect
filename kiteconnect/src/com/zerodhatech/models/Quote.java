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
    @SerializedName("day_high_oi")
    public double oiDayHigh;
    @SerializedName("day_low_oi")
    public double oiDayLow;
    @SerializedName("depth")
    public MarketDepth depth;

}
