package com.zerodhatech.models;

/**
 * A wrapper for tick.
 */

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

public class Tick {

    @SerializedName("mode")
    private String mode;
    @SerializedName("tradable")
    private boolean tradable;
    @SerializedName("token")
    private long instrumentToken;
    @SerializedName("lastTradedPrice")
    private double lastTradedPrice;
    @SerializedName("highPrice")
    private double highPrice;
    @SerializedName("lowPrice")
    private double lowPrice;
    @SerializedName("openPrice")
    private double openPrice;
    @SerializedName("closePrice")
    private double closePrice;
    @SerializedName("change")
    private double change;
    @SerializedName("lastTradeQuantity")
    private double lastTradedQuantity;
    @SerializedName("averageTradePrice")
    private double averageTradePrice;
    @SerializedName("volumeTradedToday")
    private double volumeTradedToday;
    @SerializedName("totalBuyQuantity")
    private double totalBuyQuantity;
    @SerializedName("totalSellQuantity")
    private double totalSellQuantity;
    @SerializedName("lastTradedTime")
    private Date lastTradedTime;
    @SerializedName("oi")
    private double oi;
    @SerializedName("openInterestDayHigh")
    private double oiDayHigh;
    @SerializedName("openInterestDayLow")
    private double oiDayLow;
    @SerializedName("tickTimestamp")
    private Date tickTimestamp;

    @SerializedName("depth")
    private Map<String, ArrayList<Depth>> depth;

    public Date getLastTradedTime() {
        return lastTradedTime;
    }

    public void setLastTradedTime(Date lastTradedTime) {
        this.lastTradedTime = lastTradedTime;
    }

    public double getOi() {
        return oi;
    }

    public void setOi(double oi) {
        this.oi = oi;
    }

    public double getOpenInterestDayHigh() {
        return oiDayHigh;
    }

    public void setOpenInterestDayHigh(double dayHighOpenInterest) {
        this.oiDayHigh = dayHighOpenInterest;
    }

    public double getOpenInterestDayLow() {
        return oiDayLow;
    }

    public void setOpenInterestDayLow(double dayLowOpenInterest) {
        this.oiDayLow = dayLowOpenInterest;
    }

    public Date getTickTimestamp() {
        return tickTimestamp;
    }

    public void setTickTimestamp(Date tickTimestamp) {
        this.tickTimestamp = tickTimestamp;
    }

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

    public long getInstrumentToken() {
        return instrumentToken;
    }

    public void setInstrumentToken(long token) {
        this.instrumentToken = token;
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

    public double getChange() {
        return change;
    }

    public void setNetPriceChangeFromClosingPrice(double netPriceChangeFromClosingPrice) {
        this.change = netPriceChangeFromClosingPrice;
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
