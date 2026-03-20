package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * A wrapper for the new auto slice order response payload.
 */
public class OrderResponse {
    @SerializedName("order_id")
    public String orderId;

    @SerializedName("children")
    public List<BulkOrderResponse> children;
}
