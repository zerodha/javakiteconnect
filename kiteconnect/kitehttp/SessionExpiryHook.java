package com.rainmatter.kiteconnect.kitehttp;

/**
 * A callback whenever there is a token expiry
 */
public interface SessionExpiryHook {


    public void sessionExpired();
}
