package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * A wrapper for mutual funds sip.
 */
public class MFSIP {
    @SerializedName("dividend_type")
    public String dividendType;
    @SerializedName("pending_instalments")
    public int pendingInstalments;
    @SerializedName("created")
    public Date created;
    @SerializedName("last_instalment")
    public Date lastInstalment;
    @SerializedName("transaction_type")
    public String transactionType;
    @SerializedName("frequency")
    public String frequency;
    @SerializedName("instalment_date")
    public int instalmentDate;
    @SerializedName("fund")
    public String fund;
    @SerializedName("sip_id")
    public String sipId;
    @SerializedName("tradingsymbol")
    public String tradingsymbol;
    @SerializedName("tag")
    public String tag;
    @SerializedName("instalment_amount")
    public int instalmentAmount;
    @SerializedName("instalments")
    public int instalments;
    @SerializedName("status")
    public String status;
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("next_instalment")
    public Date nextInstalment;
}
