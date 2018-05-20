package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * A wrapper for mutual funds order.
 */
public class MFOrder {
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
    public Date orderTimestamp;
    @SerializedName("average_price")
    public double averagePrice;
    @SerializedName("transaction_type")
    public String transactionType;
    @SerializedName("exchange_order_id")
    public Date exchangeOrderId;
    @SerializedName("exchange_timestamp")
    public Date exchangeTimestamp;
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
