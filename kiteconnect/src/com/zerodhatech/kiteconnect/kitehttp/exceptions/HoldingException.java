package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * There are insufficient units in the holdings.
 * */
public class HoldingException extends KiteException{
    public HoldingException(String message, int code) {
        super(message, code);
    }
}
