package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * Represents user input errors such as missing and invalid parameters.
 * Default code is 400.
 */
public class InputException extends KiteException {
    // initialize and call base exception constructor
    public InputException(String message, int code){
        super(message, code);
    }
}

