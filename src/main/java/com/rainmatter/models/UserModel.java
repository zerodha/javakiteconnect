package com.rainmatter.models;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.SerializedName;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * A wrapper for user and session details.
 */
public class UserModel {

    @SerializedName("member_id")
    public String memberId;
    public String[] product;
    @SerializedName("password_reset")
    public boolean passwordReset;
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
    public String[] exchange;
    public String[] orderType;
    @SerializedName("email")
    public String email;

    public UserModel parseResponse(JSONObject response) throws JSONException{
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        UserModel userModel = gson.fromJson(String.valueOf(response.get("data")), UserModel.class);
        userModel = parseArray(userModel, response.getJSONObject("data"));
        return userModel;
    }

    public UserModel parseArray(UserModel userModel, JSONObject response) throws JSONException{
        JSONArray productArray = response.getJSONArray("product");
        userModel.product  = new String[productArray.length()];
        for(int i = 0; i < productArray.length(); i++) {
            userModel.product[i] = productArray.getString(i);
        }

        JSONArray exchangesArray = response.getJSONArray("exchange");
        userModel.exchange = new String[exchangesArray.length()];
        for (int j = 0; j < exchangesArray.length(); j++){
            userModel.exchange[j] = exchangesArray.getString(j);
        }

        JSONArray orderTypeArray = response.getJSONArray("order_type");
        userModel.orderType = new String[orderTypeArray.length()];
        for(int k = 0; k < orderTypeArray.length(); k++){
            userModel.orderType[k] = orderTypeArray.getString(k);
        }

        return userModel;
    }
}
