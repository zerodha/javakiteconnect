package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.dto.SessionResponse;
import com.example.kiteconnectapp.exception.TradingAppException;
import com.example.kiteconnectapp.util.KiteConnectWrapper;
import com.example.kiteconnectapp.util.TokenManager;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.models.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Service to handle Kite Connect OAuth authentication flow.
 */
@Service
@Slf4j
public class AuthenticationService {
    
    @Autowired
    private KiteConnectWrapper kiteConnectWrapper;
    
    @Autowired
    private TokenManager tokenManager;
    
    /**
     * Get the Kite login URL for initiating OAuth flow.
     * User should be redirected to this URL to authenticate.
     */
    public String getLoginURL() {
        log.info("Generating Kite login URL");
        return kiteConnectWrapper.getLoginURL();
    }
    
    /**
     * Generate session: Exchange request token (from OAuth callback) for access token.
     * This completes the OAuth authentication flow.
     * 
     * @param requestToken Token received from Kite after user authentication
     * @return SessionResponse containing access token and user details
     */
    public SessionResponse generateSession(String requestToken) {
        log.info("Generating session with request token");
        
        try {
            KiteConnect kiteConnect = kiteConnectWrapper.initializeKiteConnect();
            
            // Exchange request token for access token
            User user = kiteConnect.generateSession(
                requestToken, 
                kiteConnectWrapper.getApiSecret()
            );
            
            // Store tokens in session-scoped TokenManager
            tokenManager.setTokens(
                user.accessToken,
                user.publicToken,
                user.userId
            );
            
            log.info("Session generated successfully for user: {}", user.userId);
            
            // Return session details
            SessionResponse response = new SessionResponse();
            response.setAccessToken(user.accessToken);
            response.setPublicToken(user.publicToken);
            response.setUserId(user.userId);
            response.setUserName(user.userName);
            response.setEmail(user.email);
            response.setBrokerName(user.broker);
            response.setAuthenticated(true);
            response.setMessage("Authentication successful");
            
            return response;
            
        } catch (Exception e) {
            log.error("Failed to generate session: {}", e.getMessage(), e);
            throw new TradingAppException(
                "AUTH_ERROR",
                "Failed to authenticate: " + e.getMessage(),
                e
            );
        }
    }
    
    /**
     * Check if user is currently authenticated.
     */
    public boolean isAuthenticated() {
        return tokenManager.isAuthenticated();
    }
    
    /**
     * Get current session details.
     */
    public SessionResponse getCurrentSession() {
        SessionResponse response = new SessionResponse();
        response.setAuthenticated(tokenManager.isAuthenticated());
        response.setUserId(tokenManager.getUserId());
        response.setAccessToken(tokenManager.getAccessToken());
        return response;
    }
    
    /**
     * Logout: Clear stored tokens.
     */
    public void logout() {
        log.info("User logging out");
        tokenManager.clear();
    }
}
