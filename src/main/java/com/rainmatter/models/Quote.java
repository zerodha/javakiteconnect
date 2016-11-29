package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

/**
 * A wrapper for quote.
 */
public class Quote {

    @SerializedName("volume")
    public String volume;
    @SerializedName("last_quantity")
    public String lastQuantity;
    @SerializedName("last_time")
    public String lastTime;
    @SerializedName("change")
    public double change;
    @SerializedName("open_interest")
    public double openInterest;
    @SerializedName("sell_quantity")
    public double sellQuantity;
    @SerializedName("change_percent")
    public double changePercent;
    @SerializedName("last_price")
    public double lastPrice;
    @SerializedName("buy_quantity")
    public double buyQuantity;
    public OHLC ohlc;
    public Map<String, ArrayList<Depth>> depth;

    public Quote parseResponse(JSONObject response) throws JSONException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Quote quote = gson.fromJson(String.valueOf(response.get("data")), Quote.class);
        quote = parseOtherData(response.getJSONObject("data"), quote);
        return quote;
    }

    public Quote parseOtherData(JSONObject data, Quote quote) throws JSONException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        OHLC ohlc = gson.fromJson(String.valueOf(data.get("ohlc")), OHLC.class);
        quote.ohlc = ohlc;

        JSONObject depthJson = (JSONObject) data.get("depth");
        JSONArray sellArray = depthJson.getJSONArray("sell");
        JSONArray buyArray = depthJson.getJSONArray("buy");
        ArrayList<Depth> buy = new ArrayList<>();
        ArrayList<Depth> sell = new ArrayList<>();
        for(int i = 0; i < buyArray.length(); i++){
            GsonBuilder gsonBuilder1 = new GsonBuilder();
            Gson gson1 = gsonBuilder1.create();
            Depth depth = gson1.fromJson(String.valueOf(buyArray.getJSONObject(i)), Depth.class);
            buy.add(depth);
        }
        for(int k = 0; k < sellArray.length(); k++){
            GsonBuilder gsonBuilder2 = new GsonBuilder();
            Gson gson2 = gsonBuilder2.create();
            Depth depth = gson2.fromJson(String.valueOf(sellArray.getJSONObject(k)), Depth.class);
            sell.add(depth);
        }

        quote.depth.put("buy", buy);
        quote.depth.put("sell", sell);

        return quote;
    }

    public Quote parseIndicesResponse(JSONObject response) throws JSONException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        Quote quote = gson.fromJson(String.valueOf(response.get("data")), Quote.class);
        return quote;
    }
}
