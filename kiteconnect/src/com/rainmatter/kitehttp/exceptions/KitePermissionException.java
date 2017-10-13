package com.rainmatter.kitehttp.exceptions;

/**
 * Represents permission denied exceptions for certain calls.
 * Default code is 403
 */
public class KitePermissionException extends KiteException {
    public KitePermissionException(String message, int code){
        super(message, code);
    }
}
