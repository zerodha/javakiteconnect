package com.rainmatter.kitehttp.exceptions;

/**
 * Deals with all kinds of parse and encoding errors.
 */

public class KiteParseException extends KiteException {
    public KiteParseException(String message, int code){
        super(message, code);
    }
}
