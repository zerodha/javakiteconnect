package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.exception.TradingAppException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Holding;
import com.zerodhatech.models.Margin;
import com.zerodhatech.models.Position;
import com.zerodhatech.models.Profile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class PortfolioService {

    @Autowired
    private KiteClientService kiteClientService;

    public List<Holding> getHoldings() {
        try {
            return kiteClientService.getAuthenticatedClient().getHoldings();
        } catch (KiteException | IOException e) {
            throw new TradingAppException("HOLDINGS_FETCH_FAILED", e.getMessage(), e);
        }
    }

    public Map<String, List<Position>> getPositions() {
        try {
            return kiteClientService.getAuthenticatedClient().getPositions();
        } catch (KiteException | IOException e) {
            throw new TradingAppException("POSITIONS_FETCH_FAILED", e.getMessage(), e);
        }
    }

    public Map<String, Margin> getMargins() {
        try {
            return kiteClientService.getAuthenticatedClient().getMargins();
        } catch (KiteException | IOException e) {
            throw new TradingAppException("MARGINS_FETCH_FAILED", e.getMessage(), e);
        }
    }

    public Profile getProfile() {
        try {
            return kiteClientService.getAuthenticatedClient().getProfile();
        } catch (KiteException | IOException e) {
            throw new TradingAppException("PROFILE_FETCH_FAILED", e.getMessage(), e);
        }
    }
}
