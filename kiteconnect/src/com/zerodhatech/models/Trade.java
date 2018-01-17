package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * A wrapper for trade.
 */
public class Trade {
	@SerializedName("trade_id")
	public String tradeId;
	@SerializedName("order_id")
	public String orderId;
	@SerializedName("exchange_order_id")
	public String exchangeOrderId;
	@SerializedName("tradingsymbol")
	public String tradingSymbol;
	@SerializedName("exchange")
	public String exchange;
	@SerializedName("instrument_token")
	public String instrumentToken;
	@SerializedName("product")
	public String product;
	@SerializedName("average_price")
	public String averagePrice;
	@SerializedName("quantity")
	public String quantity;
	@SerializedName("fill_timestamp")
	public Date fillTimestamp;
	@SerializedName("exchange_timestamp")
	public Date exchangeTimestamp;
	@SerializedName("transaction_type")
	public String transactionType;
}
