package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 *   A wrapper for place auto slice order response.
 */
public class AutoSliceOrderResponse {
    @SerializedName("order_id")
    public String orderId;
    @SerializedName("error")
    public AutoSliceException autoSliceException;
}


