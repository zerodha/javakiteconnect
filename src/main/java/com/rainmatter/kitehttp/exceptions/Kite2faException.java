package com.rainmatter.kitehttp.exceptions;

/**
 * Raised when there is a two FA related error.
 * Default code is 403.
 */
public class Kite2faException extends KiteException {

    // initialize 2fa exception and call constructor of Base Exception
    public Kite2faException(String message, int code){
        super(message, code);
    }
}
