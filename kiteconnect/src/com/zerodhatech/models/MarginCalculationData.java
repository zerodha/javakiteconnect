package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for margin calculation response data.
 */
public class MarginCalculationData {
    @SerializedName("type")
    public String type;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("span")
    public double span;
    @SerializedName("exposure")
    public double exposure;
    @SerializedName("option_premium")
    public double option_premium;
    @SerializedName("additional")
    public double additional;
    @SerializedName("bo")
    public double bo;
    @SerializedName("cash")
    public double cash;
    @SerializedName("var")
    public double var;
    @SerializedName("pnl")
    public PnL pnl;
    @SerializedName("total")
    public double total;

    public class PnL {
        @SerializedName("realised")
        double realised;
        @SerializedName("unrealised")
        double unrealised;
    }
}
