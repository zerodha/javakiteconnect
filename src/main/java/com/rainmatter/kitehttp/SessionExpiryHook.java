package com.rainmatter.kitehttp;

/**
 * A callback whenever there is a token expiry.
 *
 * This is implemented as a hook for whenever there is a token expiry
 */
public interface SessionExpiryHook {


    public void sessionExpired();
}
