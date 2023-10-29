package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for position.
 */
public class Position {

    @SerializedName("product")
    public String product;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("sell_value")
    public Double sellValue;
    @SerializedName("last_price")
    public Double lastPrice;
    @SerializedName("unrealised")
    public Double unrealised;
    @SerializedName("buy_price")
    public Double buyPrice;
    @SerializedName("sell_price")
    public Double sellPrice;
    @SerializedName("m2m")
    public Double m2m;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("quantity")
    public int netQuantity;
    @SerializedName("sell_quantity")
    public int sellQuantity;
    @SerializedName("realised")
    public Double realised;
    @SerializedName("buy_quantity")
    public int buyQuantity;
    @SerializedName("net_value")
    public Double netValue;
    @SerializedName("buy_value")
    public Double buyValue;
    @SerializedName("multiplier")
    public Double multiplier;
    @SerializedName("instrument_token")
    public String instrumentToken;
    @SerializedName("close_price")
    public Double closePrice;
    @SerializedName("pnl")
    public Double pnl;
    @SerializedName("overnight_quantity")
    public int overnightQuantity;
    @SerializedName("buy_m2m")
    public double buym2m;
    @SerializedName("sell_m2m")
    public double sellm2m;
    @SerializedName("day_buy_quantity")
    public double dayBuyQuantity;
    @SerializedName("day_sell_quantity")
    public double daySellQuantity;
    @SerializedName("day_buy_price")
    public double dayBuyPrice;
    @SerializedName("day_sell_price")
    public double daySellPrice;
    @SerializedName("day_buy_value")
    public double dayBuyValue;
    @SerializedName("day_sell_value")
    public double daySellValue;
    @SerializedName("value")
    public double value;
    @SerializedName("average_price")
    public double averagePrice;
}

