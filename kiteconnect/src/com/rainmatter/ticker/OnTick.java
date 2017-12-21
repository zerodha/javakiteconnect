package com.rainmatter.ticker;

import com.rainmatter.models.Tick;

import java.util.ArrayList;

/**
 * Callback to listen to com.rainmatter.ticker websocket on tick arrival event.
 */

/** OnTick interface is called once ticks arrive.*/
public interface OnTick {
    void onTick(ArrayList<Tick> ticks);
}
