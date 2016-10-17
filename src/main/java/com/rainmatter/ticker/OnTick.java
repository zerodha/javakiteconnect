package com.rainmatter.ticker;

import com.rainmatter.models.Tick;

import java.util.ArrayList;

/**
 * Callback to listen to ticker websocket on tick arrival event.
 */

public interface OnTick {
    void onTick(ArrayList<Tick> ticks);
}
