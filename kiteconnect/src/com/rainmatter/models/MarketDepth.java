package com.rainmatter.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by sujith on 1/8/18.
 */
public class MarketDepth {
    @SerializedName("buy")
    public List<Depth> buy;
    @SerializedName("sell")
    public List<Depth> sell;
}
