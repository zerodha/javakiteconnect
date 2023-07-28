package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for virtual contract note response data.
 */
public class ContractNote {
    @SerializedName("transaction_type")
    public String transactionType;
    @SerializedName("tradingsymbol")
    public String tradingSymbol;
    @SerializedName("exchange")
    public String exchange;
    @SerializedName("variety")
    public String variety;
    @SerializedName("product")
    public String product;
    @SerializedName("order_type")
    public String orderType;
    @SerializedName("quantity")
    public int quantity = 0;
    @SerializedName("price")
    public double price = 0.0;
    @SerializedName("charges")
    public MarginCalculationData.Charges charges;
}
