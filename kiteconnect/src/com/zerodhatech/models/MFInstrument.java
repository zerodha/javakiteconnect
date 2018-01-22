package com.zerodhatech.models;

import java.util.Date;

/**
 * A wrapper for mutual funds instrument.
 */
public class MFInstrument {
    public String tradingsymbol, amc, name;
    public boolean purchase_allowed, redemption_allowed;
    public double minimum_purchase_amount, purchase_amount_multiplier, minimum_additional_purchase_amount, minimum_redemption_quantity;
    public double redemption_quantity_multiplier, last_price;
    public String dividend_type, scheme_type, plan, settlement_type;
    public Date last_price_date;

    public String getTradingsymbol() {
        return tradingsymbol;
    }

    public void setTradingsymbol(String tradingsymbol) {
        this.tradingsymbol = tradingsymbol;
    }

    public String getAmc() {
        return amc;
    }

    public void setAmc(String amc) {
        this.amc = amc;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean getPurchase_allowed() {
        return purchase_allowed;
    }

    public void setPurchase_allowed(boolean purchase_allowed) {
        this.purchase_allowed = purchase_allowed;
    }

    public boolean getRedemption_allowed() {
        return redemption_allowed;
    }

    public void setRedemption_allowed(boolean redemption_allowed) {
        this.redemption_allowed = redemption_allowed;
    }

    public double getMinimum_purchase_amount() {
        return minimum_purchase_amount;
    }

    public void setMinimum_purchase_amount(double minimum_purchase_amount) {
        this.minimum_purchase_amount = minimum_purchase_amount;
    }

    public double getPurchase_amount_multiplier() {
        return purchase_amount_multiplier;
    }

    public void setPurchase_amount_multiplier(double purchase_amount_multiplier) {
        this.purchase_amount_multiplier = purchase_amount_multiplier;
    }

    public double getMinimum_additional_purchase_amount() {
        return minimum_additional_purchase_amount;
    }

    public void setMinimum_additional_purchase_amount(double minimum_additional_purchase_amount) {
        this.minimum_additional_purchase_amount = minimum_additional_purchase_amount;
    }

    public double getMinimum_redemption_quantity() {
        return minimum_redemption_quantity;
    }

    public void setMinimum_redemption_quantity(double minimum_redemption_quantity) {
        this.minimum_redemption_quantity = minimum_redemption_quantity;
    }

    public double getRedemption_quantity_multiplier() {
        return redemption_quantity_multiplier;
    }

    public void setRedemption_quantity_multiplier(double redemption_quantity_multiplier) {
        this.redemption_quantity_multiplier = redemption_quantity_multiplier;
    }

    public double getLast_price() {
        return last_price;
    }

    public void setLast_price(double last_price) {
        this.last_price = last_price;
    }

    public String getDividend_type() {
        return dividend_type;
    }

    public void setDividend_type(String dividend_type) {
        this.dividend_type = dividend_type;
    }

    public String getScheme_type() {
        return scheme_type;
    }

    public void setScheme_type(String scheme_type) {
        this.scheme_type = scheme_type;
    }

    public String getPlan() {
        return plan;
    }

    public void setPlan(String plan) {
        this.plan = plan;
    }

    public String getSettlement_type() {
        return settlement_type;
    }

    public void setSettlement_type(String settlement_type) {
        this.settlement_type = settlement_type;
    }

    public Date getLast_price_date() {
        return last_price_date;
    }

    public void setLast_price_date(Date last_price_date) {
        this.last_price_date = last_price_date;
    }
}
