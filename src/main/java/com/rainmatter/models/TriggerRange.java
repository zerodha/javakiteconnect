package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONException;
import org.json.JSONObject;

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

    public TriggerRange parseResponse(JSONObject response) throws JSONException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        return gson.fromJson(String.valueOf(response.get("data")), TriggerRange.class);
    }
}
