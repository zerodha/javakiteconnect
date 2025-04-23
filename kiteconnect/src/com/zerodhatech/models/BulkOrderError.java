package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

public  class BulkOrderError {
    @SerializedName("code")
    public int code;
    @SerializedName("error_type")
    public String errorType;
    @SerializedName("message")
    public String message;
}
