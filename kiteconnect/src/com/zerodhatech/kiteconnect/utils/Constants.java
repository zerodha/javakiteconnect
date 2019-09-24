package com.zerodhatech.kiteconnect.utils;

/**
 * Contains all the Strings that are being used in the Kite Connect library.
 */
public class Constants {

    /** Product types. */
    public static String PRODUCT_MIS = "MIS";
    public static String PRODUCT_CNC = "CNC";
    public static String PRODUCT_NRML = "NRML";

    /** Order types. */
    public static String ORDER_TYPE_MARKET = "MARKET";
    public static String ORDER_TYPE_LIMIT = "LIMIT";
    public static String ORDER_TYPE_SL = "SL";
    public static String ORDER_TYPE_SLM = "SL-M";

    /** Variety types. */
    public static String VARIETY_REGULAR = "regular";
    public static String VARIETY_BO = "bo";
    public static String VARIETY_CO = "co";
    public static String VARIETY_AMO = "amo";

    /** Transaction types. */
    public static String TRANSACTION_TYPE_BUY = "BUY";
    public static String TRANSACTION_TYPE_SELL = "SELL";

    /** Position types. */
    public static String POSITION_DAY = "day";
    public static String POSITION_OVERNIGHT = "overnight";

    /** Validity types. */
    public static String VALIDITY_DAY = "DAY";
    public static String VALIDITY_IOC = "IOC";

    /** Exchanges. */
    public static String EXCHANGE_NSE = "NSE";
    public static String EXCHANGE_BSE = "BSE";
    public static String EXCHANGE_NFO = "NFO";
    public static String EXCHANGE_BFO = "BFO";
    public static String EXCHANGE_MCX = "MCX";
    public static String EXCHANGE_CDS = "CDS";

    /** Margin segments. */
    public static String MARGIN_EQUITY = "equity";
    public static String MARGIN_COMMODITY = "commodity";

    /** Instruments segments. */
    public static String INSTRUMENTS_SEGMENTS_EQUITY = "equity";
    public static String INSTRUMENTS_SEGMENTS_COMMODITY = "commodity";
    public static String INSTRUMENTS_SEGMENTS_FUTURES = "futures";
    public static String INSTRUMENTS_SEGMENTS_CURRENCY = "currency";

    /* GTT status */
    public static String ACTIVE = "active";
    public static String TRIGGERED = "triggered";
    public static String DISABLED = "disabled";
    public static String EXPIRED = "expired";
    public static String CANCELLED = "cancelled";
    public static String REJECTED = "rejected";
    public static String DELETED = "deleted";


    /* GTT trigger type */
    public static String OCO = "two-leg";
    public static String SINGLE = "single";
}
