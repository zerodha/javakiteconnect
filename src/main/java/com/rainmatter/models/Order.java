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
 * A wrapper for order.
 * */
public class Order {

    @SerializedName("exchange_order_id")
    public String exchangeOrderId;
    @SerializedName("disclosed_quantity")
    public String disclosedQuantity;
    @SerializedName("market_production")
    public String marketProduction;
    @SerializedName("validity")
    public String validity;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("variety")
    public String orderVariety;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("order_type")
    public String orderType;
    @SerializedName("trigger_price")
    public String triggerPrice;
    @SerializedName("status_message")
    public String statusMessage;
    @SerializedName("price")
    public String price;
    @SerializedName("status")
    public String status;
    @SerializedName("product")
    public String product;
    @SerializedName("placed_by")
    public String accountId;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("symbol")
    public String symbol;
    @SerializedName("pending_quantity")
    public String pendingQuantity;
    @SerializedName("order_timestamp")
    public String orderTimestamp;
    @SerializedName("exchange_timestamp")
    public String exchangeTimestamp;
    @SerializedName("average_price")
    public String averagePrice;
    @SerializedName("transaction_type")
    public String transactionType;
    @SerializedName("filled_quantity")
    public String filledQuantity;
    @SerializedName("quantity")
    public String quantity;
    @SerializedName("parent_order_id")
    public String parentOrderId;
    @SerializedName("tag")
    public String tag;


    public Order (){}
    public List<Order> orders = new ArrayList<>();


    public void parseOrderPlacedResponse(JSONObject jsonObject) throws JSONException {
        this.orderId = jsonObject.getJSONObject("data").getString("order_id");
    }


    public void parseListOrdersResponse(JSONObject response) throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        orders = Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }
}
