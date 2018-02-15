package com.zerodhatech.kiteconnect;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates endpoints for all api calls.
 *
 * Here all the routes are translated into a Java Map.
 *
 */
public class Routes {

    /*

            "parameters": "/parameters",
            "api.validate": "/session/token",
            "api.invalidate": "/session/token",
            "user.margins": "/user/margins/{segment}",

            "orders": "/orders",
            "trades": "/trades",
            "orders.info": "/orders/{order_id}",

            "orders.place": "/orders/{variety}",
            "orders.modify": "/orders/{variety}/{order_id}",
            "orders.cancel": "/orders/{variety}/{order_id}",
            "orders.trades": "/orders/{order_id}/trades",

            "portfolio.positions": "/portfolio/positions",
            "portfolio.holdings": "/portfolio/holdings",
            "portfolio.positions.modify": "/portfolio/positions",

            "market.instruments.all": "/instruments",
            "market.instruments": "/instruments/{exchange}",
            "market.quote": "/instruments/{exchange}/{tradingsymbol}",
            "market.historical": "/instruments/historical/{instrument_token}/{interval}",
            "market.trigger_range": "/instruments/{exchange}/{tradingsymbol}/trigger_range"
    */

    public Map<String, String> routes;
    private static String _rootUrl = "https://api.kite.trade";
    private static String  _loginUrl = "https://kite.trade/connect/login";
    private static String _wsuri = "wss://ws.kite.trade/"+"?access_token=:access_token&api_key=:api_key";

    // Initialize all routes,
       public Routes(){
        routes = new HashMap<String, String>(){{
           put("parameters", "/parameters");
            put("api.validate", "/session/token");
            put("api.invalidate", "/session/token");
            put("user.margins.segment", "/user/margins/:segment");
            put("user.margins", "/user/margins");
            put("user.profile", "/user/profile");
            put("api.refresh", "/session/refresh_token");

            put("instruments.margins", "/margins/:segment");

            put("orders", "/orders");
            put("order", "/orders/:order_id");
            put("trades", "/trades");
            put("orders.info", "/orders/:order_id");
            put("orders.place", "/orders/:variety");
            put("orders.modify", "/orders/:variety/:order_id");
            put("orders.cancel", "/orders/:variety/:order_id");
            put("orders.trades", "/orders/:order_id/trades");

            put("portfolio.positions", "/portfolio/positions");
            put("portfolio.holdings", "/portfolio/holdings");
            put("portfolio.positions.modify", "/portfolio/positions");

            put("market.instruments.all", "/instruments");
            put("market.instruments", "/instruments/:exchange");
            put("market.quote", "/quote");
            put("market.historical", "/instruments/historical/:instrument_token/:interval");
            put("market.trigger_range", "/instruments/trigger_range/:transaction_type");

            put("quote.ohlc", "/quote/ohlc");
            put("quote.ltp", "/quote/ltp");

            put("mutualfunds.orders", "/mf/orders");
            put("mutualfunds.orders.place", "/mf/orders");
            put("mutualfunds.cancel_order", "/mf/orders/:order_id");
            put("mutualfunds.order", "/mf/orders/:order_id");

            put("mutualfunds.sips", "/mf/sips");
            put("mutualfunds.sips.place", "/mf/sips");
            put("mutualfunds.cancel_sips", "/mf/sips/:sip_id");
            put("mutualfunds.sips.modify", "/mf/sips/:sip_id");
            put("mutualfunds.sip", "/mf/sips/:sip_id");

            put("mutualfunds.instruments", "/mf/instruments");
            put("mutualfunds.holdings", "/mf/holdings");

            put("api.token", "/session/token");
        }};
    }

    public String get(String key){
        return _rootUrl + routes.get(key);
    }

    public String getWsuri(){
        return _wsuri;
    }

    public String getLoginUrl(){
        return _loginUrl;
    }
}
