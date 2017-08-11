package com.rainmatter.kitehttp.exceptions;

/**
 * This deals with user exceptions, like
 * user is not allowed to trade in a segment,
 * or invalid username/password.
 *
 * It extends from the base exception.
 */

public class KiteUserException extends KiteException {

    // initialize the class and call the base exception consturctor
    public KiteUserException(String message, int code){
        super(message, code);
    }
}
