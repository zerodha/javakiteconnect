package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * Denotes session is expired.
 */
public class TokenException extends KiteException {
    public TokenException(String message, int code) {
        super(message, code);
    }
}
