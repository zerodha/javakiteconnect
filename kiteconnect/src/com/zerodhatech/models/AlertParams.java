package com.zerodhatech.models;

/**
 * Parameters for creating or modifying an alert.
 * The condition reads: lhs (an attribute of an instrument) operator rhs (a constant, or an attribute of another instrument).
 */
public class AlertParams {
    /** Name of the alert. */
    public String name;

    /** Alert type, {@code simple} or {@code ato} (see Constants.ALERT_TYPE_*). */
    public String type;

    /** Exchange of the instrument the condition watches (NSE, BSE, NFO, INDICES, ...). */
    public String lhsExchange;

    /** Tradingsymbol of the instrument the condition watches. */
    public String lhsTradingSymbol;

    /** Attribute of the instrument to compare, for example {@code LastTradedPrice}. */
    public String lhsAttribute;

    /** Comparison operator: {@code <=}, {@code >=}, {@code <}, {@code >} or {@code ==} (see Constants.ALERT_OPERATOR_*). */
    public String operator;

    /** {@code constant} or {@code instrument} (see Constants.ALERT_RHS_TYPE_*). */
    public String rhsType;

    /** Value to compare against, used when rhsType is {@code constant}. */
    public double rhsConstant;

    /** Exchange of the instrument to compare against, used when rhsType is {@code instrument}. */
    public String rhsExchange;

    /** Tradingsymbol of the instrument to compare against, used when rhsType is {@code instrument}. */
    public String rhsTradingSymbol;

    /** Attribute of the instrument to compare against, used when rhsType is {@code instrument}. */
    public String rhsAttribute;

    /** Orders to place when the alert triggers, required when type is {@code ato}. */
    public Alert.Basket basket;
}
