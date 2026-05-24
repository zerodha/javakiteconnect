package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.dto.TickerSubscribeRequest;
import com.example.kiteconnectapp.exception.TradingAppException;
import com.example.kiteconnectapp.util.KiteConnectWrapper;
import com.example.kiteconnectapp.util.TokenManager;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.Tick;
import com.zerodhatech.ticker.KiteTicker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class TickerService {

    @Autowired
    private TokenManager tokenManager;

    @Autowired
    private KiteConnectWrapper kiteConnectWrapper;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    private KiteTicker kiteTicker;
    private boolean connected;

    public synchronized void connectIfRequired() {
        if (connected && kiteTicker != null && kiteTicker.isConnectionOpen()) {
            return;
        }
        if (!tokenManager.isAuthenticated()) {
            throw new TradingAppException("UNAUTHORIZED", "Please login before opening ticker stream.");
        }

        kiteTicker = new KiteTicker(tokenManager.getAccessToken(), kiteConnectWrapper.getApiKey());
        kiteTicker.setTryReconnection(true);
        try {
            kiteTicker.setMaximumRetries(10);
            kiteTicker.setMaximumRetryInterval(30);
        } catch (KiteException e) {
            throw new TradingAppException("TICKER_SETUP_FAILED", e.getMessage(), e);
        }

        kiteTicker.setOnConnectedListener(() -> {
            connected = true;
            messagingTemplate.convertAndSend("/topic/ticker-status", Collections.singletonMap("status", "connected"));
        });

        kiteTicker.setOnDisconnectedListener(() -> {
            connected = false;
            messagingTemplate.convertAndSend("/topic/ticker-status", Collections.singletonMap("status", "disconnected"));
        });

        kiteTicker.setOnTickerArrivalListener(ticks -> {
            List<Map<String, Object>> payload = ticks.stream().map(this::toTickPayload).collect(Collectors.toList());
            messagingTemplate.convertAndSend("/topic/ticks", payload);
        });

        kiteTicker.setOnErrorListener(new com.zerodhatech.ticker.OnError() {
            @Override
            public void onError(Exception exception) {
                messagingTemplate.convertAndSend("/topic/ticker-errors", Collections.singletonMap("error", exception.getMessage()));
            }

            @Override
            public void onError(KiteException kiteException) {
                messagingTemplate.convertAndSend("/topic/ticker-errors", Collections.singletonMap("error", kiteException.message));
            }

            @Override
            public void onError(String error) {
                messagingTemplate.convertAndSend("/topic/ticker-errors", Collections.singletonMap("error", error));
            }
        });

        kiteTicker.connect();
    }

    public synchronized void subscribe(TickerSubscribeRequest request) {
        connectIfRequired();
        List<Long> tokens = request.getTokens() == null ? Collections.emptyList() : request.getTokens();
        if (tokens.isEmpty()) {
            throw new TradingAppException("INVALID_SUBSCRIPTION", "tokens list cannot be empty.");
        }
        ArrayList<Long> tokenList = new ArrayList<>(tokens);
        kiteTicker.subscribe(tokenList);
        String mode = request.getMode() == null ? KiteTicker.modeQuote : request.getMode();
        kiteTicker.setMode(tokenList, mode);
    }

    public synchronized void unsubscribe(List<Long> tokens) {
        if (kiteTicker == null || tokens == null || tokens.isEmpty()) {
            return;
        }
        kiteTicker.unsubscribe(new ArrayList<>(tokens));
    }

    public synchronized void disconnect() {
        if (kiteTicker != null) {
            kiteTicker.disconnect();
        }
        connected = false;
    }

    private Map<String, Object> toTickPayload(Tick tick) {
        return Map.of(
                "instrumentToken", tick.getInstrumentToken(),
                "lastTradedPrice", tick.getLastTradedPrice(),
                "lastTradedQuantity", tick.getLastTradedQuantity(),
                "change", tick.getChange(),
                "mode", tick.getMode()
        );
    }
}
