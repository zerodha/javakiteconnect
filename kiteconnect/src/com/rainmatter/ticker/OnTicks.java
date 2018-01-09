package com.rainmatter.ticker;

import com.rainmatter.models.Tick;

import java.util.ArrayList;

/**
 * Callback to listen to com.rainmatter.ticker websocket on tick arrival event.
 */

/** OnTicks interface is called once ticks arrive.*/
public interface OnTicks {
    void onTicks(ArrayList<Tick> ticks);
}
