package com.rainmatter.kitehttp.exceptions;

/**
 * Created by H1ccup on 04/09/16.
 *  Raised when Kite SDK is unable to connect to the Kite Connect servers.
 * Default code is 504.
 */

public class KiteClientNetworkException extends KiteException {
    public KiteClientNetworkException(String message, int code){
        super(message, code);
    }
}

