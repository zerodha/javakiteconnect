package com.zerodhatech.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A wrapper for user and session details.
 */
public class User {

    public String[] products;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("broker")
    public String broker;
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("public_token")
    public String publicToken;
    @SerializedName("user_type")
    public String userType;
    @SerializedName("user_id")
    public String userId;
    @SerializedName("login_time")
    public String loginTime;
    @SerializedName("api_key")
    public String apiKey;
    public String[] exchanges;
    public String[] orderTypes;
    @SerializedName("email")
    public String email;
    @SerializedName("refresh_token")
    public String refreshToken;
    @SerializedName("user_shortname")
    public String shortName;

    /** Parses user details response from server.
     * @param response is the json response from server.
     * @throws JSONException is thrown when there is error while parsing response.
     * @return User is the parsed data.
     * */
    public User parseResponse(JSONObject response) throws JSONException {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        User user = gson.fromJson(String.valueOf(response.get("data")), User.class);
        user = parseArray(user, response.getJSONObject("data"));
        return user;
    }

    /** Parses array details of product, exchange and order_type from json response.
     *  @param response is the json response from server.
     *  @param user is the object to which data is copied to from json response.
     *  @return User is the pojo of parsed data.
     *  */
    public User parseArray(User user, JSONObject response) throws JSONException {
        JSONArray productArray = response.getJSONArray("products");
        user.products  = new String[productArray.length()];
        for(int i = 0; i < productArray.length(); i++) {
            user.products[i] = productArray.getString(i);
        }

        JSONArray exchangesArray = response.getJSONArray("exchanges");
        user.exchanges = new String[exchangesArray.length()];
        for (int j = 0; j < exchangesArray.length(); j++){
            user.exchanges[j] = exchangesArray.getString(j);
        }

        JSONArray orderTypeArray = response.getJSONArray("order_types");
        user.orderTypes = new String[orderTypeArray.length()];
        for(int k = 0; k < orderTypeArray.length(); k++){
            user.orderTypes[k] = orderTypeArray.getString(k);
        }

        return user;
    }
}
