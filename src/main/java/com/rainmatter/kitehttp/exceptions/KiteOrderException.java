package com.rainmatter.kitehttp.exceptions;

/**
 * Represents all order placement and manipulation errors.
 * Default code is 500.
 */

public class KiteOrderException extends KiteException {

    // initialize Order Exception and call base exception constructor
    public KiteOrderException(String message, int code){
        super(message, code);
    }
}
