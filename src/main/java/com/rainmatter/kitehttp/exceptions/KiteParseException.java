package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 * Deals with all kinds of parse and encoding errors.
 */

public class KiteParseException extends KiteException {
    public KiteParseException(String message, int code){
        super(message, code);
    }
}
