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

}
