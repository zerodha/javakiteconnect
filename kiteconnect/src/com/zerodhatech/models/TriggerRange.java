package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for trigger range.
 */
public class TriggerRange {

    @SerializedName("lower")
    public double lower;
    @SerializedName("upper")
    public double upper;
    @SerializedName("percentage")
    public double percentage;
}
