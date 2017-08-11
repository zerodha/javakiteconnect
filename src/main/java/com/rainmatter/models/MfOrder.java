package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONObject;
import org.omg.Messaging.SYNC_WITH_TRANSPORT;

import javax.swing.event.ListDataEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A wrapper for Mutualfunds order.
 */
public class MfOrder {
    @SerializedName("status_message")
    public String statusMessage;
    @SerializedName("purchase_type")
    public String purchaseType;
    @SerializedName("placed_by")
    public String placedBy;
    @SerializedName("amount")
    public double amount;
    @SerializedName("quantity")
    public double quantity;
    @SerializedName("settlement_id")
    public String settlementId;
    @SerializedName("order_timestamp")
    public String orderTimestamp;
    @SerializedName("average_price")
    public double averagePrice;
    @SerializedName("transaction_type")
    public String transactionType;
    @SerializedName("exchange_order_id")
    public String exchangeOrderId;
    @SerializedName("exchange_timestamp")
    public String exchangeTimestamp;
    @SerializedName("fund")
    public String fund;
    @SerializedName("variety")
    public String variety;
    @SerializedName("folio")
    public String folio;
    @SerializedName("tradingsymbol")
    public String tradingsymbol;
    @SerializedName("tag")
    public String tag;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("status")
    public String status;
    @SerializedName("last_price")
    public double lastPrice;

    public MfOrder(){}
    public List<MfOrder> mfOrders = new ArrayList<>();

    /** Parse mutualfunds orders list response.
     * @param response contains list of mutualfunds. */
    public void parseMfOrders(JSONObject response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        mfOrders = Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfOrder[].class));
    }

    /** Parse individual mutualfunds order response.
     * @param response contains JSONObject of individual order details. */
    public MfOrder parseMfOrder(JSONObject response){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(response.get("data").toString(), MfOrder.class);
    }

    /** Parse place order response.
     * @param response contains order id. */
    public void parseOrderPlaceResponse(JSONObject response){
        this.orderId = response.getJSONObject("data").getString("order_id");
    }
}
