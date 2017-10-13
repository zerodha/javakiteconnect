package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

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
