package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by sujith on 9/21/19.
 */
public class GTT {
    @SerializedName("id")
    public int id;

    @SerializedName("condition")
    public Condition condition;

    @SerializedName("type")
    public String triggerType;

    @SerializedName("orders")
    public List<GTTOrder> orders;

    @SerializedName("status")
    public String status;

    @SerializedName("created_at")
    public String createdAt;

    @SerializedName("updated_at")
    public String updatedAt;

    @SerializedName("expires_at")
    public String expiresAt;

    @SerializedName("meta")
    public GTTMeta meta;

    public class GTTMeta {
        @SerializedName("rejection_reason")
        public String rejectionReason;
    }

    public class Condition {
        @SerializedName("instrument_token")
        public int instrumentToken;

        @SerializedName("exchange")
        public String exchange;

        @SerializedName("tradingsymbol")
        public String tradingSymbol;

        @SerializedName("trigger_values")
        public List<Double> triggerValues;

        @SerializedName("last_price")
        public double lastPrice;
    }

    public class GTTOrder {
        @SerializedName("transaction_type")
        public String transactionType;

        @SerializedName("product")
        public String product;

        @SerializedName("order_type")
        public String orderType;

        @SerializedName("quantity")
        public int quantity;

        @SerializedName("price")
        public double price;

        @SerializedName("result")
        public GTTResult result;
    }

    public class GTTResult {
        @SerializedName("order_result")
        public GTTOrderResult orderResult;

        @SerializedName("timestamp")
        public String timestamp = "-";

        @SerializedName("triggered_at")
        public double triggeredAtPrice;
    }

    public class GTTOrderResult {
        @SerializedName("order_id")
        public String orderId = "-";

        @SerializedName("rejection_reason")
        public String reason;
    }
}
