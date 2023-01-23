package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * There are insufficient funds in the account.
 * */
public class MarginException extends KiteException{
    public MarginException(String message, int code) {
        super(message, code);
    }
}
