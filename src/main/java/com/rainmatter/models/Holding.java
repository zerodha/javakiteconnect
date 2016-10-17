package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import com.rainmatter.utils.SegmentMap;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A wrapper for holdings.
 */
public class Holding {

    @SerializedName("product")
    public String product;
    @SerializedName("last_price")
    public String lastPrice;
    @SerializedName("price")
    public String price;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("t1_quantity")
    public String t1Quantity;
    @SerializedName("collateral_quantity")
    public String collateralQuantity;
    @SerializedName("collateral_type")
    public String collateraltype;
    @SerializedName("account_id")
    public String accountId;
    @SerializedName("isin")
    public String isin;
    @SerializedName("pnl")
    public String pnl;
    @SerializedName("quantity")
    public String quantity;
    @SerializedName("realised_quantity")
    public String realisedQuantity;
    /*@SerializedName("net_value")
    public String netValue;*/
    @SerializedName("average_price")
    public String averagePrice;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("instrument_token")
    public String instrumentToken;

    public List<Holding> holdings = new ArrayList<>();


    public Holding(){}

    /**
     * parses holdings response and create pojo
     * @param response
     * @throws JSONException
     */
    public void parseHoldingsResponse(JSONObject response) throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        holdings =  Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Holding[].class));
        modifyInstrumentToken(holdings);
    }

    /**
     * Modifies the instrument token by doing left shift and then concatenating Segment_id_map
     * @param holdings
     */
    private void modifyInstrumentToken(List<Holding> holdings){
        for (int i=0; i< holdings.size(); i++){
                holdings.get(i).instrumentToken = ((Integer.valueOf(holdings.get(i).instrumentToken) << 8) + Integer.valueOf(new SegmentMap().getMap().get(holdings.get(i).exchange)))+"";
        }
    }
}
