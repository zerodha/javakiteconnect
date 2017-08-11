package com.rainmatter.ticker;

import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kitehttp.exceptions.KiteException;

import java.io.IOException;

/**
 * Created by sujith on 14/10/16.
 */
public interface OnConnect {
    void onConnected();
}
