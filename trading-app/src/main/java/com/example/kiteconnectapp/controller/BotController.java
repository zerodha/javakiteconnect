package com.example.kiteconnectapp.controller;

import com.example.kiteconnectapp.dto.BotConfigRequest;
import com.example.kiteconnectapp.dto.BotOrderLog;
import com.example.kiteconnectapp.service.BotService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/bot")
public class BotController {

    @Autowired
    private BotService botService;

    @PostMapping("/start")
    public ResponseEntity<Map<String, String>> start(@RequestBody BotConfigRequest config) {
        return ResponseEntity.ok(botService.startBot(config));
    }

    @PostMapping("/stop/{botId}")
    public ResponseEntity<Map<String, String>> stop(@PathVariable String botId) {
        return ResponseEntity.ok(botService.stopBot(botId));
    }

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> status() {
        return ResponseEntity.ok(botService.getStatus());
    }

    @GetMapping("/logs")
    public ResponseEntity<List<BotOrderLog>> logs() {
        return ResponseEntity.ok(botService.getLogs());
    }
}
