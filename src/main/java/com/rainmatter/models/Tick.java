package com.rainmatter.models;

/**
 * A wrapper for tick.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Map;

public class Tick {

    @SerializedName("mode")
    private String mode;
    @SerializedName("tradeable")
    private boolean tradable;
    @SerializedName("Token")
    private int token;
    @SerializedName("LastTradedPrice")
    private double lastTradedPrice;
    @SerializedName("HighPrice")
    private double highPrice;
    @SerializedName("LowPrice")
    private double lowPrice;
    @SerializedName("OpenPrice")
    private double openPrice;
    @SerializedName("ClosePrice")
    private double closePrice;
    @SerializedName("NetPriceChangeFromClosingPrice")
    private double netPriceChangeFromClosingPrice;
    @SerializedName("LastTradeQuantity")
    private double lastTradedQuantity;
    @SerializedName("AverageTradePrice")
    private double averageTradePrice;
    @SerializedName("VolumeTradedToday")
    private double volumeTradedToday;
    @SerializedName("TotalBuyQuantity")
    private double totalBuyQuantity;
    @SerializedName("TotalSellQuantity")
    private double totalSellQuantity;

    @SerializedName("Depth")
    private Map<String, ArrayList<Depth>> depth;

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    public boolean isTradable() {
        return tradable;
    }

    public void setTradable(boolean tradable) {
        this.tradable = tradable;
    }

    public long getToken() {
        return token;
    }

    public void setToken(int token) {
        this.token = token;
    }

    public double getLastTradedPrice() {
        return lastTradedPrice;
    }

    public void setLastTradedPrice(double lastTradedPrice) {
        this.lastTradedPrice = lastTradedPrice;
    }

    public double getHighPrice() {
        return highPrice;
    }

    public void setHighPrice(double highPrice) {
        this.highPrice = highPrice;
    }

    public double getLowPrice() {
        return lowPrice;
    }

    public void setLowPrice(double lowPrice) {
        this.lowPrice = lowPrice;
    }

    public double getOpenPrice() {
        return openPrice;
    }

    public void setOpenPrice(double openPrice) {
        this.openPrice = openPrice;
    }

    public double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(double closePrice) {
        this.closePrice = closePrice;
    }

    public double getNetPriceChangeFromClosingPrice() {
        return netPriceChangeFromClosingPrice;
    }

    public void setNetPriceChangeFromClosingPrice(double netPriceChangeFromClosingPrice) {
        this.netPriceChangeFromClosingPrice = netPriceChangeFromClosingPrice;
    }

    public double getLastTradedQuantity() {
        return lastTradedQuantity;
    }

    public void setLastTradedQuantity(double lastTradedQuantity) {
        this.lastTradedQuantity = lastTradedQuantity;
    }

    public double getAverageTradePrice() {
        return averageTradePrice;
    }

    public void setAverageTradePrice(double averageTradePrice) {
        this.averageTradePrice = averageTradePrice;
    }

    public double getVolumeTradedToday() {
        return volumeTradedToday;
    }

    public void setVolumeTradedToday(double volumeTradedToday) {
        this.volumeTradedToday = volumeTradedToday;
    }

    public double getTotalBuyQuantity() {
        return totalBuyQuantity;
    }

    public void setTotalBuyQuantity(double totalBuyQuantity) {
        this.totalBuyQuantity = totalBuyQuantity;
    }

    public double getTotalSellQuantity() {
        return totalSellQuantity;
    }

    public void setTotalSellQuantity(double totalSellQuantity) {
        this.totalSellQuantity = totalSellQuantity;
    }

    public Map<String, ArrayList<Depth>> getMarketDepth() {
        return depth;
    }

    public void setMarketDepth(Map<String, ArrayList<Depth>> marketDepth) {
        this.depth = marketDepth;
    }

}
