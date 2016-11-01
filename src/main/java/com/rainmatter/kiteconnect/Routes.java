package com.rainmatter.kiteconnect;

import java.util.HashMap;
import java.util.Map;

/**
 * Generates endpoints for all api calls.
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
    private static String _wsuri = "wss://websocket.kite.trade/"+"?user_id=:user_id&public_token=:public_token&api_key=:api_key";

    // Initialize all routes,
       public Routes(){
        routes = new HashMap<String, String>(){{
           put("parameters", "/parameters");
            put("api.validate", "/session/token");
            put("api.invalidate", "/session/token");
            put("user.margins", "/user/margins/:segment");

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
            put("market.quote", "/instruments/:exchange/:tradingsymbol");
            put("market.historical", "/instruments/historical/:instrument_token/:interval");
            put("market.trigger_range", "/instruments/:exchange/:tradingsymbol/trigger_range");

            put("logout", "/session/token");
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
