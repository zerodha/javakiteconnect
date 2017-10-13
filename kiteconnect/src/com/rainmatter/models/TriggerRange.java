package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

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
