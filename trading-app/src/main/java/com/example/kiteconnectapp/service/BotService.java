package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.dto.BotConfigRequest;
import com.example.kiteconnectapp.dto.BotOrderLog;
import com.example.kiteconnectapp.dto.OrderRequest;
import com.example.kiteconnectapp.exception.TradingAppException;
import com.example.kiteconnectapp.persistence.BotConfigEntity;
import com.example.kiteconnectapp.persistence.BotConfigRepository;
import com.example.kiteconnectapp.persistence.BotLogEntity;
import com.example.kiteconnectapp.persistence.BotLogRepository;
import com.zerodhatech.kiteconnect.utils.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class BotService {

    @Autowired
    private OrderService orderService;

    @Autowired
    private BotConfigRepository botConfigRepository;

    @Autowired
    private BotLogRepository botLogRepository;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);
    private final Map<String, ScheduledFuture<?>> runningBots = new ConcurrentHashMap<>();
    private final Map<String, BotConfigRequest> configs = new ConcurrentHashMap<>();
    private final List<BotOrderLog> logs = Collections.synchronizedList(new ArrayList<>());
    private final Map<String, AtomicInteger> orderCountByBot = new ConcurrentHashMap<>();
    private volatile LocalDate orderCounterDate = LocalDate.now();

    @PostConstruct
    public void resumeEnabledBots() {
        List<BotConfigEntity> enabledBots = botConfigRepository.findByEnabledTrue();
        for (BotConfigEntity entity : enabledBots) {
            BotConfigRequest req = toRequest(entity);
            try {
                startBot(req);
                appendLog(new BotOrderLog(req.getBotId(), "RESUME", "OK", "Resumed enabled bot on startup", Instant.now()));
            } catch (Exception ex) {
                appendLog(new BotOrderLog(req.getBotId(), "RESUME", "ERROR", ex.getMessage(), Instant.now()));
            }
        }
    }

    public Map<String, String> startBot(BotConfigRequest config) {
        validate(config);
        String botId = config.getBotId() == null || config.getBotId().isBlank() ? UUID.randomUUID().toString() : config.getBotId();
        stopBot(botId);

        config.setBotId(botId);
        configs.put(botId, config);
        saveConfig(config);

        int interval = config.getIntervalSeconds() == null ? 30 : Math.max(5, config.getIntervalSeconds());
        ScheduledFuture<?> future = scheduler.scheduleAtFixedRate(() -> executeStrategy(config), 2, interval, TimeUnit.SECONDS);
        runningBots.put(botId, future);

        appendLog(new BotOrderLog(botId, "START", "OK", "Bot started", Instant.now()));
        return Map.of("botId", botId, "status", "running");
    }

    public Map<String, String> stopBot(String botId) {
        ScheduledFuture<?> future = runningBots.remove(botId);
        if (future != null) {
            future.cancel(false);
            appendLog(new BotOrderLog(botId, "STOP", "OK", "Bot stopped", Instant.now()));
            return Map.of("botId", botId, "status", "stopped");
        }
        return Map.of("botId", botId, "status", "not_running");
    }

    public Map<String, Object> getStatus() {
        return Map.of(
                "runningCount", runningBots.size(),
                "runningBotIds", runningBots.keySet(),
                "configs", configs
        );
    }

    public List<BotOrderLog> getLogs() {
        List<BotLogEntity> entities = botLogRepository.findTop200ByOrderByIdDesc();
        List<BotOrderLog> out = new ArrayList<>();
        for (BotLogEntity entity : entities) {
            out.add(new BotOrderLog(entity.getBotId(), entity.getAction(), entity.getStatus(), entity.getDetail(), entity.getTimestamp()));
        }
        Collections.reverse(out);
        return out;
    }

    private void executeStrategy(BotConfigRequest config) {
        try {
            resetOrderCountersIfNewDay();

            if (!isWithinTradingWindow(config)) {
                appendLog(new BotOrderLog(config.getBotId(), "WINDOW", "SKIP", "Outside configured trading window", Instant.now()));
                return;
            }

            if (isMaxOrdersReached(config)) {
                appendLog(new BotOrderLog(config.getBotId(), "LIMIT", "SKIP", "Max orders per day reached", Instant.now()));
                return;
            }

            if (isPositionSizeExceeded(config)) {
                appendLog(new BotOrderLog(config.getBotId(), "LIMIT", "SKIP", "Quantity exceeds max position size", Instant.now()));
                return;
            }

            boolean dryRun = config.getDryRun() == null || config.getDryRun();
            if (dryRun) {
                appendLog(new BotOrderLog(config.getBotId(), "DRY_RUN", "OK", "Simulated order for " + config.getTradingSymbol(), Instant.now()));
                return;
            }

            OrderRequest order = new OrderRequest();
            order.setExchange(config.getExchange());
            order.setTradingSymbol(config.getTradingSymbol());
            order.setTransactionType(config.getTransactionType() == null ? Constants.TRANSACTION_TYPE_BUY : config.getTransactionType());
            order.setOrderType(config.getOrderType() == null ? Constants.ORDER_TYPE_MARKET : config.getOrderType());
            order.setProduct(config.getProduct() == null ? Constants.PRODUCT_MIS : config.getProduct());
            order.setQuantity(config.getQuantity() == null ? 1 : config.getQuantity());
            order.setValidity(Constants.VALIDITY_DAY);
            order.setVariety(Constants.VARIETY_REGULAR);

            String orderId = orderService.placeOrder(order).orderId;
            orderCountByBot.computeIfAbsent(config.getBotId(), k -> new AtomicInteger(0)).incrementAndGet();
            appendLog(new BotOrderLog(config.getBotId(), "LIVE_ORDER", "OK", "Order placed: " + orderId, Instant.now()));
        } catch (Exception ex) {
            appendLog(new BotOrderLog(config.getBotId(), "EXECUTE", "ERROR", ex.getMessage(), Instant.now()));
        }
    }

    private void saveConfig(BotConfigRequest config) {
        BotConfigEntity entity = new BotConfigEntity();
        entity.setBotId(config.getBotId());
        entity.setName(config.getName());
        entity.setExchange(config.getExchange());
        entity.setTradingSymbol(config.getTradingSymbol());
        entity.setTransactionType(config.getTransactionType());
        entity.setProduct(config.getProduct());
        entity.setOrderType(config.getOrderType());
        entity.setQuantity(config.getQuantity());
        entity.setIntervalSeconds(config.getIntervalSeconds());
        entity.setEnabled(config.getEnabled() == null || config.getEnabled());
        entity.setDryRun(config.getDryRun() == null || config.getDryRun());
        entity.setMaxOrdersPerDay(config.getMaxOrdersPerDay());
        entity.setMaxPositionSize(config.getMaxPositionSize());
        entity.setStartTime(config.getStartTime());
        entity.setEndTime(config.getEndTime());
        botConfigRepository.save(entity);
    }

    private BotConfigRequest toRequest(BotConfigEntity entity) {
        BotConfigRequest req = new BotConfigRequest();
        req.setBotId(entity.getBotId());
        req.setName(entity.getName());
        req.setExchange(entity.getExchange());
        req.setTradingSymbol(entity.getTradingSymbol());
        req.setTransactionType(entity.getTransactionType());
        req.setProduct(entity.getProduct());
        req.setOrderType(entity.getOrderType());
        req.setQuantity(entity.getQuantity());
        req.setIntervalSeconds(entity.getIntervalSeconds());
        req.setEnabled(entity.getEnabled());
        req.setDryRun(entity.getDryRun());
        req.setMaxOrdersPerDay(entity.getMaxOrdersPerDay());
        req.setMaxPositionSize(entity.getMaxPositionSize());
        req.setStartTime(entity.getStartTime());
        req.setEndTime(entity.getEndTime());
        return req;
    }

    private void appendLog(BotOrderLog logEntry) {
        logs.add(logEntry);
        BotLogEntity entity = new BotLogEntity();
        entity.setBotId(logEntry.getBotId());
        entity.setAction(logEntry.getAction());
        entity.setStatus(logEntry.getStatus());
        entity.setDetail(logEntry.getDetail());
        entity.setTimestamp(logEntry.getTimestamp() == null ? Instant.now() : logEntry.getTimestamp());
        botLogRepository.save(entity);
    }

    private void validate(BotConfigRequest config) {
        if (config == null) {
            throw new TradingAppException("INVALID_BOT", "Bot config cannot be null");
        }
        if (config.getTradingSymbol() == null || config.getTradingSymbol().isBlank()) {
            throw new TradingAppException("INVALID_BOT", "tradingSymbol is required");
        }
        if (config.getExchange() == null || config.getExchange().isBlank()) {
            throw new TradingAppException("INVALID_BOT", "exchange is required");
        }
    }

    private boolean isMaxOrdersReached(BotConfigRequest config) {
        Integer max = config.getMaxOrdersPerDay();
        if (max == null || max <= 0) {
            return false;
        }
        int current = orderCountByBot.computeIfAbsent(config.getBotId(), k -> new AtomicInteger(0)).get();
        return current >= max;
    }

    private boolean isPositionSizeExceeded(BotConfigRequest config) {
        Integer maxPos = config.getMaxPositionSize();
        Integer qty = config.getQuantity() == null ? 1 : config.getQuantity();
        return maxPos != null && maxPos > 0 && qty > maxPos;
    }

    private boolean isWithinTradingWindow(BotConfigRequest config) {
        if (config.getStartTime() == null || config.getEndTime() == null || config.getStartTime().isBlank() || config.getEndTime().isBlank()) {
            return true;
        }
        try {
            LocalTime now = LocalTime.now();
            LocalTime start = LocalTime.parse(config.getStartTime());
            LocalTime end = LocalTime.parse(config.getEndTime());
            return !now.isBefore(start) && !now.isAfter(end);
        } catch (DateTimeParseException e) {
            return true;
        }
    }

    private void resetOrderCountersIfNewDay() {
        LocalDate today = LocalDate.now();
        if (!today.equals(orderCounterDate)) {
            orderCounterDate = today;
            orderCountByBot.clear();
        }
    }
}
