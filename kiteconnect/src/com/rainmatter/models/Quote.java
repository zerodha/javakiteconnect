package com.rainmatter.models;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

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
    @SerializedName("open_interest")
    public double openInterest;
    @SerializedName("sell_quantity")
    public double sellQuantity;
    @SerializedName("last_price")
    public double lastTradedPrice;
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
    public double dayHighOI;
    @SerializedName("day_low_oi")
    public double dayLowOI;
    @SerializedName("depth")
    public MarketDepth depth;

}
