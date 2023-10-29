package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * Exceptions raised when invalid data is returned from kite trade.
 */

public class DataException extends KiteException {

    // initialize 2fa exception and call constructor of Base Exception
    public DataException(String message, int code){
        super(message, code);
    }
}

