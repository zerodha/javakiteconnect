package com.rainmatter.utils;

import java.util.HashMap;

/**
 * A Wrapper for segment map.
 */
public class SegmentMap {
    private HashMap<String, String> SEGMENTS_ID_MAP = new HashMap<String, String>();

    public SegmentMap(){
        SEGMENTS_ID_MAP.put("NSE", "1");
        SEGMENTS_ID_MAP.put("NFO", "2");
        SEGMENTS_ID_MAP.put("NFO-FUT", "2");
        SEGMENTS_ID_MAP.put("NFO-OPT", "2");
        SEGMENTS_ID_MAP.put("CDS", "3");
        SEGMENTS_ID_MAP.put("CDS-FUT", "3");
        SEGMENTS_ID_MAP.put("CDS-OPT", "3");
        SEGMENTS_ID_MAP.put("BSE", "4");
        SEGMENTS_ID_MAP.put("BFO", "5");
        SEGMENTS_ID_MAP.put("BFO-FUT", "5");
        SEGMENTS_ID_MAP.put("BFO-OPT", "5");
        SEGMENTS_ID_MAP.put("BSE-CDS", "6");
        SEGMENTS_ID_MAP.put("MCX", "7");
        SEGMENTS_ID_MAP.put("MCXSX", "8");
        SEGMENTS_ID_MAP.put("NSE-INDICES", "9");
    }

    public HashMap<String, String> getMap(){
        return SEGMENTS_ID_MAP;
    }
}
