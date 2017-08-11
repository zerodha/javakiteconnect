package com.rainmatter.ticker;

/**
 * Created by H1ccup on 10/09/16.
 */

import com.rainmatter.models.Tick;

import java.util.ArrayList;

/**
 * Created by Sujith KS on 9/16/2015.
 */

/** OnTick interface is called once ticks arrive.*/
public interface OnTick {
    void onTick(ArrayList<Tick> ticks);
}
