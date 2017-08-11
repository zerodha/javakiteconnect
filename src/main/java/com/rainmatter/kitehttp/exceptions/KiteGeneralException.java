package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 * An unclassified, general error. Default code is 500
 */
public class KiteGeneralException extends KiteException {
    // initialize and call the base class
    public KiteGeneralException(String message, int code){
        super(message, code);
    }
}
