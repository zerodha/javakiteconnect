package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.Map;

/**
*   A wrapper for order.
*/
public class Order {

    @SerializedName("exchange_order_id")
    public String exchangeOrderId;
    @SerializedName("disclosed_quantity")
    public String disclosedQuantity;
    @SerializedName("validity")
    public String validity;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("variety")
    public String orderVariety;
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
    @SerializedName("pending_quantity")
    public String pendingQuantity;
    @SerializedName("order_timestamp")
    public Date orderTimestamp;
    @SerializedName("exchange_timestamp")
    public Date exchangeTimestamp;
    @SerializedName("exchange_update_timestamp")
    public Date exchangeUpdateTimestamp;
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
    @SerializedName("guid")
    public String guid;
    @SerializedName("validity_ttl")
    public int validityTTL;
    @SerializedName("meta")
    public Map<String, Object> meta;
    @SerializedName("auction_number")
    public String auctionNumber;
}
