package com.rainmatter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 4/11/17.
 */
public class IndicesQuote {

    @SerializedName("change")
    public double change;
    @SerializedName("change_percent")
    public double changePercent;
    @SerializedName("last_price")
    public double lastPrice;
    @SerializedName("close")
    public double close;
    @SerializedName("open")
    public double open;
}
