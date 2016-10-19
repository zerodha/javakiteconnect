package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;
import com.rainmatter.utils.SegmentMap;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A wrapper for psoition.
 */
public class Position {

    @SerializedName("product")
    public String product;
    @SerializedName("symbol_code")
    public String symbolCode;
    @SerializedName("name")
    public String name;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("sell_value")
    public Double sellValue;
    @SerializedName("last_price")
    public Double lastPrice;
    @SerializedName("unrealised")
    public Double unrealised;
    @SerializedName("buy_price")
    public Double buyPrice;
    @SerializedName("sell_price")
    public Double sellPrice;
    @SerializedName("m2m")
    public Double m2m;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("quantity")
    public int netQuantity;
    @SerializedName("sell_quantity")
    public int sellQuantity;
    @SerializedName("realised")
    public Double realised;
    @SerializedName("buy_quantity")
    public int buyQuantity;
    @SerializedName("net_value")
    public Double netValue;
    @SerializedName("buy_value")
    public Double buyValue;
    @SerializedName("multiplier")
    public Double multiplier;
    @SerializedName("instrument_token")
    public String instrumentToken;
    @SerializedName("close_price")
    public Double closePrice;
    @SerializedName("pnl")
    public Double pnl;
    @SerializedName("overnight_quantity")
    public int overnightQuantity;

    public List<Position> netPositions = new ArrayList<>();
    public List<Position> dayPositions = new ArrayList<>();

    public Position(){
    }

    /**
     * parses response from positions api
     * @param response
     * @return
     * @throws JSONException
     */
    public void parseGetPositionsResponse(JSONObject response) throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        JSONObject allPositions = response.getJSONObject("data");
        netPositions =  Arrays.asList(gson.fromJson(String.valueOf(allPositions.get("net")), Position[].class));
        dayPositions = Arrays.asList(gson.fromJson(String.valueOf(allPositions.get("day")), Position[].class));
    }
}

