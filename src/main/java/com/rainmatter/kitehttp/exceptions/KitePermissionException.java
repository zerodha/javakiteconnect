package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 * Represents permission denied exceptions for certain calls.
 * Default code is 403
 */
public class KitePermissionException extends KiteException {
    public KitePermissionException(String message, int code){
        super(message, code);
    }
}
