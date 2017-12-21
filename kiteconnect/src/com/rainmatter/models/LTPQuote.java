package com.rainmatter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 10/11/17.
 */
public class LTPQuote {

    @SerializedName("instrument_token")
    public long instrumentToken;
    @SerializedName("last_price")
    public double lastPrice;

}
