package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 * Represents all order placement and manipulation errors.
 * Default code is 500.
 */

public class KiteOrderException extends KiteException {

    // initialize Order Exception and call base exception constructor
    public KiteOrderException(String message, int code){
        super(message, code);
    }
}
