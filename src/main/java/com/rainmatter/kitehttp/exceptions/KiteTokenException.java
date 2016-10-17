package com.rainmatter.kitehttp.exceptions;

/**
 * Denotes session is expired.
 */
public class KiteTokenException extends KiteException {
    public KiteTokenException(String message, int code) {
        super(message, code);
    }
}
