package com.rainmatter.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by H1ccup on 10/09/16.
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