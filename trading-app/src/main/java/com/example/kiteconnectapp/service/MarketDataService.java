package com.example.kiteconnectapp.service;

import com.example.kiteconnectapp.exception.TradingAppException;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.HistoricalData;
import com.zerodhatech.models.Instrument;
import com.zerodhatech.models.Quote;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class MarketDataService {

    @Autowired
    private KiteClientService kiteClientService;

    private Map<String, Long> instrumentCache = new HashMap<>();
    private Instant instrumentCacheAt;
    private static final long INSTRUMENT_CACHE_TTL_SECONDS = 300;

    public Map<String, Quote> getQuotes(String[] instruments) {
        try {
            return kiteClientService.getAuthenticatedClient().getQuote(instruments);
        } catch (KiteException | IOException e) {
            throw new TradingAppException("QUOTES_FETCH_FAILED", e.getMessage(), e);
        }
    }

    public List<Instrument> getInstruments() {
        try {
            return kiteClientService.getAuthenticatedClient().getInstruments();
        } catch (KiteException | IOException e) {
            throw new TradingAppException("INSTRUMENTS_FETCH_FAILED", e.getMessage(), e);
        }
    }

    public HistoricalData getHistoricalData(String instrumentToken, String interval, String from, String to) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            Date fromDate = formatter.parse(from);
            Date toDate = formatter.parse(to);
            return kiteClientService.getAuthenticatedClient().getHistoricalData(fromDate, toDate, instrumentToken, interval, false, false);
        } catch (KiteException | IOException | ParseException e) {
            throw new TradingAppException("HISTORICAL_FETCH_FAILED", e.getMessage(), e);
        }
    }

    public Map<String, Long> getInstrumentTokensBySymbols(List<String> symbols) {
        if (symbols == null || symbols.isEmpty()) {
            return Map.of();
        }

        ensureInstrumentCache();
        Map<String, Long> result = new HashMap<>();
        for (String symbol : symbols) {
            String key = symbol == null ? "" : symbol.trim().toUpperCase();
            if (instrumentCache.containsKey(key)) {
                result.put(key, instrumentCache.get(key));
            }
        }
        return result;
    }

    private synchronized void ensureInstrumentCache() {
        if (instrumentCacheAt != null && Instant.now().minusSeconds(INSTRUMENT_CACHE_TTL_SECONDS).isBefore(instrumentCacheAt)) {
            return;
        }
        List<Instrument> instruments = getInstruments();
        Map<String, Long> updated = new HashMap<>();
        for (Instrument instrument : instruments) {
            if (instrument.exchange == null || instrument.tradingsymbol == null) {
                continue;
            }
            String key = (instrument.exchange + ":" + instrument.tradingsymbol).toUpperCase();
            updated.put(key, instrument.instrument_token);
        }
        instrumentCache = updated;
        instrumentCacheAt = Instant.now();
    }
}
