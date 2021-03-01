package com.zerodhatech.kiteconnect.utils;

/**
 * Contains all the Strings that are being used in the Kite Connect library.
 */
public class Constants {

    /** Product types. */
    public static final String PRODUCT_MIS = "MIS";
    public static final String PRODUCT_CNC = "CNC";
    public static final String PRODUCT_NRML = "NRML";

    /** Order types. */
    public static final String ORDER_TYPE_MARKET = "MARKET";
    public static final String ORDER_TYPE_LIMIT = "LIMIT";
    public static final String ORDER_TYPE_SL = "SL";
    public static final String ORDER_TYPE_SLM = "SL-M";

    /** Variety types. */
    public static final String VARIETY_REGULAR = "regular";
    public static final String VARIETY_BO = "bo";
    public static final String VARIETY_CO = "co";
    public static final String VARIETY_AMO = "amo";

    /** Transaction types. */
    public static final String TRANSACTION_TYPE_BUY = "BUY";
    public static final String TRANSACTION_TYPE_SELL = "SELL";

    /** Position types. */
    public static final String POSITION_DAY = "day";
    public static final String POSITION_OVERNIGHT = "overnight";

    /** Validity types. */
    public static final String VALIDITY_DAY = "DAY";
    public static final String VALIDITY_IOC = "IOC";

    /** Exchanges. */
    public static final String EXCHANGE_NSE = "NSE";
    public static final String EXCHANGE_BSE = "BSE";
    public static final String EXCHANGE_NFO = "NFO";
    public static final String EXCHANGE_BFO = "BFO";
    public static final String EXCHANGE_MCX = "MCX";
    public static final String EXCHANGE_CDS = "CDS";

    /** Margin segments. */
    public static final String MARGIN_EQUITY = "equity";
    public static final String MARGIN_COMMODITY = "commodity";

    /** Instruments segments. */
    public static final String INSTRUMENTS_SEGMENTS_EQUITY = "equity";
    public static final String INSTRUMENTS_SEGMENTS_COMMODITY = "commodity";
    public static final String INSTRUMENTS_SEGMENTS_FUTURES = "futures";
    public static final String INSTRUMENTS_SEGMENTS_CURRENCY = "currency";

    /* GTT status */
    public static final String ACTIVE = "active";
    public static final String TRIGGERED = "triggered";
    public static final String DISABLED = "disabled";
    public static final String EXPIRED = "expired";
    public static final String CANCELLED = "cancelled";
    public static final String REJECTED = "rejected";
    public static final String DELETED = "deleted";


    /* GTT trigger type */
    public static final String OCO = "two-leg";
    public static final String SINGLE = "single";

    /*These are commonly used order statuses but there are many other order statuses.
    * Some of the statuses are only intermediate. */
    public static final String ORDER_CANCELLED = "CANCELLED";
    public static final String ORDER_REJECTED = "REJECTED";
    public static final String ORDER_COMPLETE = "COMPLETE";
    public static final String ORDER_OPEN = "OPEN";
    public static final String ORDER_LAPSED = "LAPSED";
    public static final String ORDER_TRIGGER_PENDING = "TRIGGER PENDING";
}
