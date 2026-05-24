package com.example.kiteconnectapp.util;

import lombok.Data;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.SessionScope;

/**
 * Session-scoped component to store user's Kite authentication tokens.
 * Stores tokens per user session.
 */
@Data
@Component
@SessionScope
public class TokenManager {
    
    private String accessToken;
    private String publicToken;
    private String refreshToken;
    private String userId;
    private boolean authenticated = false;
    
    public void setTokens(String accessToken, String publicToken, String userId) {
        this.accessToken = accessToken;
        this.publicToken = publicToken;
        this.userId = userId;
        this.authenticated = true;
    }
    
    public void clear() {
        this.accessToken = null;
        this.publicToken = null;
        this.refreshToken = null;
        this.userId = null;
        this.authenticated = false;
    }
    
    public boolean isAuthenticated() {
        return authenticated && accessToken != null;
    }
}
