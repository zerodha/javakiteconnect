package com.example.kiteconnectapp.exception;

/**
 * Base exception for trading app errors.
 */
public class TradingAppException extends RuntimeException {
    
    private String code;
    
    public TradingAppException(String message) {
        super(message);
        this.code = "GENERAL_ERROR";
    }
    
    public TradingAppException(String code, String message) {
        super(message);
        this.code = code;
    }
    
    public TradingAppException(String code, String message, Throwable cause) {
        super(message, cause);
        this.code = code;
    }
    
    public String getCode() {
        return code;
    }
}
