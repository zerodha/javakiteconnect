package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for holdings.
 */
public class Holding {

    @SerializedName("product")
    public String product;
    @SerializedName("last_price")
    public Double lastPrice;
    @SerializedName("price")
    public String price;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("t1_quantity")
    public int t1Quantity;
    @SerializedName("collateral_quantity")
    public String collateralQuantity;
    @SerializedName("collateral_type")
    public String collateraltype;
    @SerializedName("isin")
    public String isin;
    @SerializedName("pnl")
    public Double pnl;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("realised_quantity")
    public String realisedQuantity;
    @SerializedName("average_price")
    public Double averagePrice;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("instrument_token")
    public String instrumentToken;
}
