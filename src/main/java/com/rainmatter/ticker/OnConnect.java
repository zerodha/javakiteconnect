package com.rainmatter.ticker;

import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kitehttp.exceptions.KiteException;

import java.io.IOException;

/**
 * Callback to listen to ticker websocket connected event.
 */
public interface OnConnect {
    void onConnected();
}
