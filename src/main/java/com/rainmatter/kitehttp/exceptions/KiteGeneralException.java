package com.rainmatter.kitehttp.exceptions;

/**
 * An unclassified, general error. Default code is 500
 */
public class KiteGeneralException extends KiteException {
    // initialize and call the base class
    public KiteGeneralException(String message, int code){
        super(message, code);
    }
}
