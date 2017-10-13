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
	@SerializedName("order_timestamp")
	public String orderTimestamp;
	@SerializedName("exchange_timestamp")
	public String exchangeTimestamp;
	@SerializedName("transaction_type")
	public String transactionType;

}
