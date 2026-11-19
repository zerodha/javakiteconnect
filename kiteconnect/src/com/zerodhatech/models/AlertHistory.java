package com.zerodhatech.models;

import com.google.gson.JsonElement;
import com.google.gson.annotations.SerializedName;

import java.util.Date;
import java.util.List;

/**
 * One occasion on which an alert triggered.
 */
public class AlertHistory {

    @SerializedName("uuid")
    public String uuid;

    @SerializedName("type")
    public String type;

    /** Market data of the instruments in the condition at the time it triggered. */
    @SerializedName("meta")
    public List<Meta> meta;

    /** The condition that was met, for example {@code LastTradedPrice("INDICES:NIFTY 50") >= 27000}. */
    @SerializedName("condition")
    public String condition;

    @SerializedName("created_at")
    public Date createdAt;

    /** Details of the orders placed by an ATO alert, null for simple alerts. */
    @SerializedName("order_meta")
    public JsonElement orderMeta;

    public static class Meta {
        @SerializedName("instrument_token")
        public long instrumentToken;

        @SerializedName("tradingsymbol")
        public String tradingSymbol;

        @SerializedName("exchange")
        public String exchange;

        @SerializedName("timestamp")
        public String timestamp;

        @SerializedName("last_trade_time")
        public String lastTradeTime;

        @SerializedName("last_price")
        public double lastPrice;

        @SerializedName("ohlc")
        public OHLC ohlc;

        @SerializedName("net_change")
        public double netChange;

        @SerializedName("last_quantity")
        public long lastQuantity;

        @SerializedName("buy_quantity")
        public long buyQuantity;

        @SerializedName("sell_quantity")
        public long sellQuantity;

        @SerializedName("volume")
        public long volume;

        @SerializedName("volume_tick")
        public long volumeTick;

        @SerializedName("average_price")
        public double averagePrice;

        @SerializedName("oi")
        public long oi;

        @SerializedName("oi_day_high")
        public long oiDayHigh;

        @SerializedName("oi_day_low")
        public long oiDayLow;

        @SerializedName("lower_circuit_limit")
        public double lowerCircuitLimit;

        @SerializedName("upper_circuit_limit")
        public double upperCircuitLimit;
    }
}
