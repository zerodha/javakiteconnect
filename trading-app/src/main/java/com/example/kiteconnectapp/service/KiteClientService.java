package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.exception.TradingAppException;
import com.example.kiteconnectapp.util.KiteConnectWrapper;
import com.example.kiteconnectapp.util.TokenManager;
import com.zerodhatech.kiteconnect.KiteConnect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Provides authenticated KiteConnect clients for API operations.
 */
@Service
public class KiteClientService {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private KiteConnectWrapper kiteConnectWrapper;

    public KiteConnect getAuthenticatedClient() {
        if (!tokenManager.isAuthenticated()) {
            throw new TradingAppException("UNAUTHORIZED", "Please login with Kite before using trading APIs.");
        }
        return kiteConnectWrapper.createAuthenticatedClient(tokenManager.getAccessToken());
    }
}
