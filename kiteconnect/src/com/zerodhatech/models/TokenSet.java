package com.zerodhatech.models;

import com.google.gson.annotations.SerializedName;

/**
 * A wrapper for user id, access token, refresh token.
 */
public class TokenSet {

    @SerializedName("user_id")
    public String userId;
    @SerializedName("access_token")
    public String accessToken;
    @SerializedName("refresh_token")
    public String refreshToken;

}
