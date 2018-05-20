package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for open, high, low, close.
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
