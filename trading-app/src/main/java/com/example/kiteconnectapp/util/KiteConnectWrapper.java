package com.example.kiteconnectapp.util;

import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Wrapper around KiteConnect client initialization and management.
 * Handles creation and configuration of KiteConnect instances.
 */
@Component
public class KiteConnectWrapper {
    
    @Value("${kite.api.key}")
    private String apiKey;
    
    @Value("${kite.api.secret}")
    private String apiSecret;
    
    @Value("${kite.login.url}")
    private String loginUrl;
    
    private KiteConnect kiteConnect;
    
    /**
     * Initialize KiteConnect with API key.
     * This is the first step in the OAuth flow.
     */
    public KiteConnect initializeKiteConnect() {
        if (kiteConnect == null) {
            kiteConnect = new KiteConnect(apiKey);
        }
        return kiteConnect;
    }
    
    /**
     * Get the login URL for OAuth authentication.
     */
    public String getLoginURL() {
        KiteConnect client = initializeKiteConnect();
        return client.getLoginURL();
    }
    
    /**
     * Create a new KiteConnect instance with the provided access token.
     * Call this after authentication is complete.
     */
    public KiteConnect createAuthenticatedClient(String accessToken) {
        KiteConnect client = new KiteConnect(apiKey);
        client.setAccessToken(accessToken);
        return client;
    }
    
    public String getApiKey() {
        return apiKey;
    }
    
    public String getApiSecret() {
        return apiSecret;
    }
}
