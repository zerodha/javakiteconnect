package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for trigger range.
 */
public class TriggerRange {

    @SerializedName("start")
    public double start;
    @SerializedName("end")
    public double end;
    @SerializedName("percent")
    public double percent;
}
