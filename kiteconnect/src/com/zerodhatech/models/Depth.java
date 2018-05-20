package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for market depth.
 */
public class Depth  {

    @SerializedName("quantity")
    private int quantity;
    @SerializedName("price")
    private double price;
    @SerializedName("orders")
    private int orders;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getOrders() {
        return orders;
    }

    public void setOrders(int orders) {
        this.orders = orders;
    }
}