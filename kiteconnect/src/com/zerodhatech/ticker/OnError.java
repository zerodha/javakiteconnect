package com.zerodhatech.ticker;

import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;

/**
 * Created by sujith on 11/21/17.
 */
public interface OnError {

    public void onError(Exception exception);

    public void onError(KiteException kiteException);
}
