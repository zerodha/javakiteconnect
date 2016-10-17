package com.rainmatter.kitehttp;

/**
 * Created by H1ccup on 07/10/16.
 *
 * This is implemented as a hook for whenever there is a token expiry
 */
public interface SessionExpiryHook {


    public void sessionExpired();
}
