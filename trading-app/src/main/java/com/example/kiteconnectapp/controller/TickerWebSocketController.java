package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.dto.TickerSubscribeRequest;
import com.example.kiteconnectapp.service.TickerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Controller;

import java.util.Collections;
import java.util.List;

@Controller
public class TickerWebSocketController {

    @Autowired
    private TickerService tickerService;

    @MessageMapping("/ticker.connect")
    public void connect() {
        tickerService.connectIfRequired();
    }

    @MessageMapping("/ticker.subscribe")
    public void subscribe(@Payload TickerSubscribeRequest request) {
        tickerService.subscribe(request);
    }

    @MessageMapping("/ticker.unsubscribe")
    public void unsubscribe(@Payload TickerSubscribeRequest request) {
        List<Long> tokens = request == null ? Collections.emptyList() : request.getTokens();
        tickerService.unsubscribe(tokens);
    }

    @MessageMapping("/ticker.disconnect")
    public void disconnect() {
        tickerService.disconnect();
    }
}
