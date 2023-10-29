package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * An unclassified, general error. Default code is 500
 */
public class GeneralException extends KiteException {
    // initialize and call the base class
    public GeneralException(String message, int code){
        super(message, code);
    }
}
