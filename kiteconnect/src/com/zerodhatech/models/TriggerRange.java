package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 13/10/16.
 */
public class TriggerRange {

    @SerializedName("start")
    public double start;
    @SerializedName("end")
    public double end;
    @SerializedName("percent")
    public double percent;
}
