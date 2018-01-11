package com.zerodhatech.kiteconnect.kitehttp.exceptions;

/**
 * Represents permission denied exceptions for certain calls.
 * Default code is 403
 */
public class PermissionException extends KiteException {
    public PermissionException(String message, int code){
        super(message, code);
    }
}
