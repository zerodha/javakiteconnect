package com.rainmatter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 13/10/16.
 */
public class OHLC {

    @SerializedName("high")
    public double high;
    @SerializedName("low")
    public double low;
    @SerializedName("close")
    public double close;
    @SerializedName("open")
    public double open;
}
