package com.zerodhatech.models;

/**
 * A wrapper for funds.
 */

import com.google.gson.annotations.SerializedName;


public class Margin {


    public Margin(){};
    public Available available;
    public Utilised utilised;


    // Serialized names are the actual keys in json response
    @SerializedName("net")
    public String net;

    /**
     * Class available is a wrapper around available cash margins used by GSON.
     *
     */
    public static class Available{

        @SerializedName("cash")
        public String cash;

        @SerializedName("intraday_payin")
        public String intradayPayin;

        @SerializedName("adhoc_margin")
        public String adhocMargin;

        @SerializedName("collateral")
        public String collateral;

    }

    /**
     * Utilised is a wrapper around utilised margins, used by GSON.
     */

    public static class Utilised{
        @SerializedName("m2m_unrealised")
        public String m2mUnrealised;

        @SerializedName("m2m_realised")
        public String m2mRealised;

        @SerializedName("debits")
        public String debits;

        @SerializedName("span")
        public String span;

        @SerializedName("option_premium")
        public String optionPremium;

        @SerializedName("holding_sales")
        public String holdingSales;

        @SerializedName("exposure")
        public String exposure;

        @SerializedName("turnover")
        public String turnover;
    }
}
