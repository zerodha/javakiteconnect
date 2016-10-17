package com.rainmatter.kitehttp.exceptions;

/**
 * Represents a network issue between Kite and the backend Order Management System (OMS).
 * Default code is 503.
 */

public class KiteNetworkException extends KiteException {

    // initialize Kite Network exception and call Base Exception constructor
    public KiteNetworkException(String message, int code){
        super(message, code);
    }
}
