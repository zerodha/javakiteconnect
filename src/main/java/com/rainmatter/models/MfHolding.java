package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
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

    public List<MfHolding> mfHoldings = new ArrayList<>();

    /** Parse mutualfunds holdings response.
     * @param response constains list of mutualfunds holdings. */
    public void parseMfHoldings(JSONObject response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        mfHoldings = Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfHolding[].class));
    }
}
