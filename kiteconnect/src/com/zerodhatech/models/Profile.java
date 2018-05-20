package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for profile response.
 */
public class Profile {

    @SerializedName("user_type")
    public String userType;
    @SerializedName("email")
    public String email;
    @SerializedName("user_name")
    public String userName;
    @SerializedName("user_shortname")
    public String userShortname;
    @SerializedName("broker")
    public String broker;
    @SerializedName("exchanges")
    public String[] exchanges;
    @SerializedName("products")
    public String[] products;
    @SerializedName("order_types")
    public String[] orderTypes;
    @SerializedName("avatar_url")
    public String avatarURL;

}
