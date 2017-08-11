package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 * Wrapper around all timeout exceptions
 */

public class KiteTimeOutException extends KiteException {
    public KiteTimeOutException(String message, int code) {
        super(message, code);
    }
}
