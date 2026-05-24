package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.dto.MarginCalculationRequest;
import com.example.kiteconnectapp.dto.MarginLegRequest;
import com.example.kiteconnectapp.exception.TradingAppException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.CombinedMarginData;
import com.zerodhatech.models.MarginCalculationData;
import com.zerodhatech.models.MarginCalculationParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class MarginService {

    @Autowired
    private KiteClientService kiteClientService;

    public List<MarginCalculationData> calculateMargins(MarginCalculationRequest request) {
        try {
            List<MarginCalculationParams> params = toParams(request);
            return kiteClientService.getAuthenticatedClient().getMarginCalculation(params);
        } catch (KiteException | IOException e) {
            throw new TradingAppException("MARGIN_CALC_FAILED", e.getMessage(), e);
        }
    }

    public CombinedMarginData calculateCombinedMargins(MarginCalculationRequest request) {
        try {
            List<MarginCalculationParams> params = toParams(request);
            boolean considerPositions = request.getConsiderPositions() != null && request.getConsiderPositions();
            boolean mode = request.getMode() != null && request.getMode();
            return kiteClientService.getAuthenticatedClient().getCombinedMarginCalculation(params, considerPositions, mode);
        } catch (KiteException | IOException e) {
            throw new TradingAppException("COMBINED_MARGIN_CALC_FAILED", e.getMessage(), e);
        }
    }

    private List<MarginCalculationParams> toParams(MarginCalculationRequest request) {
        if (request.getLegs() == null || request.getLegs().isEmpty()) {
            throw new TradingAppException("INVALID_MARGIN_REQUEST", "At least one margin leg is required.");
        }

        List<MarginCalculationParams> params = new ArrayList<>();
        for (MarginLegRequest leg : request.getLegs()) {
            MarginCalculationParams param = new MarginCalculationParams();
            param.exchange = leg.getExchange();
            param.tradingSymbol = leg.getTradingSymbol();
            param.transactionType = leg.getTransactionType();
            param.variety = leg.getVariety();
            param.product = leg.getProduct();
            param.orderType = leg.getOrderType();
            param.quantity = leg.getQuantity() == null ? 0 : leg.getQuantity();
            param.price = leg.getPrice() == null ? 0.0 : leg.getPrice();
            param.triggerPrice = leg.getTriggerPrice() == null ? 0.0 : leg.getTriggerPrice();
            params.add(param);
        }
        return params;
    }
}
