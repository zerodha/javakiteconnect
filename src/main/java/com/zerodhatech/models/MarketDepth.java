package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A wrapper for market depth data.
 */
public class MarketDepth {
    @SerializedName("buy")
    public List<Depth> buy;
    @SerializedName("sell")
    public List<Depth> sell;
}
