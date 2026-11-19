package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * A market alert. A simple alert notifies when its condition is met; an ATO (alert triggers order)
 * alert also places the orders in its basket.
 */
public class Alert {

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("type")
    public String type;

    @SerializedName("user_id")
    public String userId;

    @SerializedName("name")
    public String name;

    @SerializedName("status")
    public String status;

    @SerializedName("disabled_reason")
    public String disabledReason;

    @SerializedName("lhs_attribute")
    public String lhsAttribute;

    @SerializedName("lhs_exchange")
    public String lhsExchange;

    @SerializedName("lhs_tradingsymbol")
    public String lhsTradingSymbol;

    @SerializedName("operator")
    public String operator;

    @SerializedName("rhs_type")
    public String rhsType;

    @SerializedName("rhs_attribute")
    public String rhsAttribute;

    @SerializedName("rhs_exchange")
    public String rhsExchange;

    @SerializedName("rhs_tradingsymbol")
    public String rhsTradingSymbol;

    @SerializedName("rhs_constant")
    public double rhsConstant;

    @SerializedName("alert_count")
    public int alertCount;

    @SerializedName("created_at")
    public Date createdAt;

    @SerializedName("updated_at")
    public Date updatedAt;

    /** Orders placed when an ATO alert triggers; null for simple alerts. */
    @SerializedName("basket")
    public Basket basket;

    public static class Basket {
        @SerializedName("name")
        public String name;

        @SerializedName("type")
        public String type;

        @SerializedName("tags")
        public List<String> tags;

        @SerializedName("items")
        public List<BasketItem> items;
    }

    public static class BasketItem {
        @SerializedName("type")
        public String type;

        @SerializedName("tradingsymbol")
        public String tradingSymbol;

        @SerializedName("exchange")
        public String exchange;

        @SerializedName("weight")
        public int weight;

        @SerializedName("params")
        public BasketOrderParams params;

        @SerializedName("id")
        public Integer id;

        @SerializedName("instrument_token")
        public Integer instrumentToken;
    }

    /** Order parameters of a basket item. */
    public static class BasketOrderParams {
        @SerializedName("transaction_type")
        public String transactionType;

        @SerializedName("product")
        public String product;

        @SerializedName("order_type")
        public String orderType;

        @SerializedName("validity")
        public String validity;

        @SerializedName("validity_ttl")
        public int validityTTL;

        @SerializedName("quantity")
        public int quantity;

        @SerializedName("price")
        public double price;

        @SerializedName("trigger_price")
        public double triggerPrice;

        @SerializedName("disclosed_quantity")
        public int disclosedQuantity;

        @SerializedName("last_price")
        public double lastPrice;

        @SerializedName("variety")
        public String variety;

        @SerializedName("tags")
        public List<String> tags;

        @SerializedName("squareoff")
        public double squareoff;

        @SerializedName("stoploss")
        public double stoploss;

        @SerializedName("trailing_stoploss")
        public double trailingStoploss;

        @SerializedName("iceberg_legs")
        public int icebergLegs;

        @SerializedName("market_protection")
        public double marketProtection;

        @SerializedName("gtt")
        public GTTTargets gtt;
    }

    public static class GTTTargets {
        @SerializedName("target")
        public double target;

        @SerializedName("stoploss")
        public double stoploss;
    }
}
