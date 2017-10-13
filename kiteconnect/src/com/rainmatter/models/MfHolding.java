package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A wrapper for Mutualfunds holding.
 */
public class MfHolding {
    @SerializedName("quantity")
    public double quantity;
    @SerializedName("fund")
    public String fund;
    @SerializedName("folio")
    public String folio;
    @SerializedName("average_price")
    public double averagePrice;
    @SerializedName("tradingsymbol")
    public String tradingsymbol;
    @SerializedName("last_price")
    public double lastPrice;
    @SerializedName("pnl")
    public double pnl;

}
