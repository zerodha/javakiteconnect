package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for instrument token, OHLC data.
 */
public class LTPQuote {

    @SerializedName("instrument_token")
    public long instrumentToken;
    @SerializedName("last_price")
    public double lastPrice;

}
