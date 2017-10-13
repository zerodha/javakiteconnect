package com.rainmatter.kitehttp.exceptions;

/**
 * Represents user input errors such as missing and invalid parameters.
 * Default code is 400.
 */
public class KiteInputException extends KiteException {
    // initialize and call base exception constructor
    public KiteInputException(String message, int code){
        super(message, code);
    }
}

