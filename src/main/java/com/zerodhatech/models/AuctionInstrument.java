package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

public class AuctionInstrument {
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("instrument_token")
    public long instrumentToken;
    @SerializedName("isin")
    public String isin;
    @SerializedName("product")
    public String product;
    @SerializedName("price")
    public double price;
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("t1_quantity")
    public int t1Quantity;
    @SerializedName("realised_quantity")
    public int realisedQuantity;
    @SerializedName("authorised_quantity")
    public int authorisedQuantity;
    @SerializedName("authorised_date")
    public String authorisedDate;
    @SerializedName("opening_quantity")
    public String openingQuantity;
    @SerializedName("collateral_quantity")
    public int collateralQuantity;
    @SerializedName("collateral_type")
    public String collateralType;
    @SerializedName("discrepancy")
    public boolean discrepancy;
    @SerializedName("average_price")
    public double averagePrice;
    @SerializedName("last_price")
    public double lastPrice;
    @SerializedName("close_price")
    public double closePrice;
    @SerializedName("pnl")
    public double pnl;
    @SerializedName("day_change")
    public double dayChange;
    @SerializedName("day_change_percentage")
    public double dayChangePercentage;
    @SerializedName("auction_number")
    public String auctionNumber;
}
