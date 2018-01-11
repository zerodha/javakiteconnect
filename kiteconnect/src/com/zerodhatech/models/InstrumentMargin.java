package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 11/21/17.
 */
public class InstrumentMargin {
    @SerializedName("margin")
    public double margin;
    @SerializedName("co_lower")
    public double coLower;
    @SerializedName("mis_multiplier")
    public int misMultiplier;
    @SerializedName("tradingsymbol")
    public String tradingsymbol;
    @SerializedName("co_upper")
    public double coUpper;
    @SerializedName("nrml_margin")
    public int nrmlMargin;
    @SerializedName("mis_margin")
    public int misMargin;
}
