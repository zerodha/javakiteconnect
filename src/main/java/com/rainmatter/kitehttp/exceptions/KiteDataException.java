package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 * Exceptions raised when invalid data is returned from kite trade.
 */

public class KiteDataException extends KiteException {

    // initialize 2fa exception and call constructor of Base Exception
    public KiteDataException(String message, int code){
        super(message, code);
    }
}

