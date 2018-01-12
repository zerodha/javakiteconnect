package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by sujith on 1/12/18.
 */
public class TokenSet {

    @SerializedName("user_id")
    public String userId;
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;

}
