package com.zerodhatech.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper for historical data.
 */
public class HistoricalData {

    public String timeStamp;
    public double open;
    public double high;
    public double low;
    public double close;
    public long volume;
    public List<HistoricalData> dataArrayList = new ArrayList<>();

    public void parseResponse(JSONObject response) throws JSONException {
        JSONObject data = response.getJSONObject("data");
        JSONArray candleArray = data.getJSONArray("candles");
        for(int i = 0; i < candleArray.length(); i++){
            JSONArray itemArray = candleArray.getJSONArray(i);
            HistoricalData historicalData = new HistoricalData();
            historicalData.timeStamp = itemArray.getString(0);
            historicalData.open = itemArray.getDouble(1);
            historicalData.high = itemArray.getDouble(2);
            historicalData.low = itemArray.getDouble(3);
            historicalData.close = itemArray.getDouble(4);
            historicalData.volume = itemArray.getLong(5);
            dataArrayList.add(historicalData);
        }
    }
}
