package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * Represents a network issue between Kite and the backend Order Management System (OMS).
 * Default code is 503.
 */

public class NetworkException extends KiteException {

    // initialize Kite Network exception and call Base Exception constructor
    public NetworkException(String message, int code){
        super(message, code);
    }
}
