package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for mtf data inside holdings response.
 */
public class MTFHolding {
    @SerializedName("quantity")
    public int quantity;
    @SerializedName("used_quantity")
    public int usedQuantity;
    @SerializedName("average_price")
    public Double averagePrice;
    @SerializedName("value")
    public Double value;
    @SerializedName("initial_margin")
    public Double initialMargin;
}
