package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

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
    @SerializedName("used_quantity")
    public int usedQuantity;
    @SerializedName("authorised_quantity")
    public int authorisedQuantity;
    @SerializedName("authorised_date")
    public Date authorisedDate;
    @SerializedName("discrepancy")
    public boolean discrepancy;
    @SerializedName("day_change")
    public  double dayChange;
    @SerializedName("day_change_percentage")
    public  double dayChangePercentage;
    @SerializedName("mtf")
    public  MTFHolding mtf;
}
