import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.kiteconnect.kitehttp.exceptions.KiteException;
import com.rainmatter.models.*;
import com.rainmatter.ticker.*;
import org.json.JSONObject;
import  com.rainmatter.models.Margin;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;

/**
 * Created by sujith on 15/10/16.
 */
public class Examples {


    public void getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
        Profile profile = kiteConnect.getProfile();
        System.out.println(profile.userName);
    }

    /**Gets Margin.*/
    public void getMargins(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
        //Margins margins = kiteConnect.getMargins("equity");
        Margin margins = kiteConnect.getMargins("equity");
        System.out.println(margins.available.cash);
        System.out.println(margins.utilised.debits);
        System.out.println(margins.utilised.m2mUnrealised);
    }

    /**Place order.*/
    public void placeOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Place order method requires a map argument which contains,
         * tradingsymbol, exchange, transaction_type, order_type, quantity, product, price, trigger_price, disclosed_quantity, validity
         * squareoff_value, stoploss_value, trailing_stoploss
         * and variety (value can be regular, bo, co, amo)
         * place order will return order model which will have only orderId in the order model
         *
         * Following is an example param for LIMIT order,
         * if a call fails then KiteException will have error message in it
         * Success of this call implies only order has been placed successfully, not order execution */
        Map<String, Object> param = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "LIMIT");
                put("tradingsymbol", "ASHOKLEY");
                put("product", "CNC");
                put("exchange", "NSE");
                put("transaction_type", "BUY");
                put("validity", "DAY");
                put("price", "123.50");
                put("trigger_price", "0");
                put("tag", "myTag");   //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed
            }
        };
        Order order = kiteConnect.placeOrder(param, "regular");
        System.out.println(order.orderId);
    }

    /** Place bracket order.*/
    public void placeBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Bracket order:- following is example param for bracket order*
         * trailing_stoploss and stoploss_value are points and not tick or price
         */
        Map<String, Object> param10 = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "LIMIT");
                put("price", "32.5");
                put("transaction_type", "BUY");
                put("tradingsymbol", "SOUTHBANK");
                put("trailing_stoploss", "1");
                put("stoploss", "2");
                put("exchange", "NSE");
                put("validity", "DAY");
                put("squareoff", "3");
                put("product", "MIS");
                put("tag", "myTag");
            }
        };
        Order order10 = kiteConnect.placeOrder(param10, "bo");
        System.out.println(order10.orderId);
    }

    /** Place cover order.*/
    public void placeCoverOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Cover Order:- following is example param for cover order and params sample
         * key: quantity value: 1
         key: price value: 0
         key: transaction_type value: BUY
         key: tradingsymbol value: HINDALCO
         key: exchange value: NSE
         key: validity value: DAY
         key: trigger_price value: 157
         key: order_type value: MARKET
         key: variety value: co
         key: product value: MIS
         */
        Map<String, Object> param11 = new HashMap<String , Object>(){
            {
                put("price", "0");
                put("transaction_type", "BUY");
                put("quantity", "1");
                put("tradingsymbol", "ASHOKLEY");
                put("exchange", "NSE");
                put("validity", "DAY");
                put("trigger_price", "117.5");
                put("order_type", "MARKET");
                put("product", "MIS");
            }
        };
        Order order11 = kiteConnect.placeOrder(param11, "co");
        System.out.println(order11.orderId);
    }

    /** Get trigger range.*/
    public void getTriggerRange(KiteConnect kiteConnect) throws KiteException, IOException {
        // You need to send a map with transaction_type, exchange and tradingsymbol to get trigger range.
        Map<String, Object> params12 = new HashMap<>();
        params12.put("transaction_type", "BUY");
        TriggerRange triggerRange = kiteConnect.getTriggerRange("NSE", "RELIANCE", params12);
        System.out.println(triggerRange.start);
    }

    /** Get orderbook.*/
    public void getOrders(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get orders returns order model which will have list of orders inside, which can be accessed as follows,
        List<Order> orders = kiteConnect.getOrders();
        for(int i = 0; i< orders.size(); i++){
            System.out.println(orders.get(i).tradingSymbol+" "+orders.get(i).orderId+" "+orders.get(i).parentOrderId+" "+orders.get(i).orderType+" "+orders.get(i).averagePrice);
        }
        System.out.println("list of orders size is "+orders.size());
    }

    /** Get order details*/
    public void getOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        List<Order> orders = kiteConnect.getOrderHistory("180111000561605");
        for(int i = 0; i< orders.size(); i++){
            System.out.println(orders.get(i).orderId+" "+orders.get(i).status);
        }
        System.out.println("list size is "+orders.size());
    }

    /** Get tradebook*/
    public void getTrades(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns tradebook.
        List<Trade> trades = kiteConnect.getTrades();
        System.out.println(trades.size());
    }

    /** Get trades for an order.*/
    public void getTradesWithOrderId(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns trades for the given order.
        List<Trade> trades = kiteConnect.getOrderTrades("180111000561605");
        System.out.println(trades.size());
    }

    /** Modify order.*/
    public void modifyOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.
        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "LIMIT");
                put("tradingsymbol", "GOLDGUINEA17AUGFUT");
                put("product", "MIS");
                put("exchange", "MCX");
                put("transaction_type", "BUY");
                put("validity", "DAY");
                put("price", "23098.00");
                //put("trigger_price", "157.5");
            }
        };
        Order order21 = kiteConnect.modifyOrder("170810001077550", params, "regular");
        System.out.println(order21.orderId);
    }

    /** Modify first leg bracket order.*/
    public void modifyFirstLegBo(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "LIMIT");
                put("price", "32.8");
                put("transaction_type", "BUY");
                put("tradingsymbol", "SOUTHBANK");
                put("trailing_stoploss", "1");
                put("exchange", "NSE");
                put("validity", "DAY");
                put("product", "MIS");
                put("tag", "myTag");
                put("trigger_price", "0");
            }
        };
        Order order = kiteConnect.modifyOrder("180111000561605", params, "bo");
        System.out.println(order.orderId);
    }

    public void modifySecondLegBoSLM(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("order_id", "180111000603824");
                put("parent_order_id", "180111000603822");
                put("tradingsymbol", "SOUTHBANK");
                put("exchange", "NSE");
                put("product","MIS");
                put("validity", "DAY");
                put("trigger_price", "31");
                put("price", "0");
                put("order_type", "SL-M");
                put("transaction_type", "SELL");
            }
        };
        Order order = kiteConnect.modifyOrder("180111000603824", params, "bo");
        System.out.println(order.orderId);
    }

    public void modifySecondLegBoLIMIT(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("order_id", "180111000581854");
                put("parent_order_id", "180111000561605");
                put("tradingsymbol", "SOUTHBANK");
                put("exchange", "NSE");
                put("quantity", "1");
                put("product","MIS");
                put("validity", "DAY");
                put("price", "35");
                put("trigger_price", "0");
                put("order_type", "LIMIT");
                put("transaction_type", "SELL");
            }
        };
        Order order = kiteConnect.modifyOrder("180111000581854", params, "bo");
        System.out.println(order.orderId);
    }

    /** Cancel an order*/
    public void cancelOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.
        // Cancel order will return order model which will only have orderId.
        Order order2 = kiteConnect.cancelOrder("180111000543558", "regular");
        System.out.println(order2.orderId);
    }

    public void exitBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("parent_order_id", "180111000823758");
        Order order = kiteConnect.cancelOrder(params, "180111000823761", "bo");
        System.out.println(order.orderId);
    }

    /** Get all positions.*/
    public void getPositions(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get positions returns position model which contains list of positions.
        Map<String, List<Position>> position = kiteConnect.getPositions();
        System.out.println(position.get("net").size());
        System.out.println(position.get("day").size());
    }

    /** Get holdings.*/
    public void getHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get holdings returns holdings model which contains list of holdings.
        List<Holding> holdings = kiteConnect.getHoldings();
        System.out.println(holdings.size());
    }

    /** Converts position*/
    public void converPosition(KiteConnect kiteConnect) throws KiteException, IOException {
        //Modify product can be used to change MIS to NRML(CNC) or NRML(CNC) to MIS.
        Map<String, Object> param6 = new HashMap<String, Object>(){
            {
                put("exchange", "NSE");
                put("tradingsymbol", "SOUTHBANK");
                put("transaction_type", "BUY");
                put("position_type", "day"); //can also be day
                put("quantity", "1");
                put("old_product", "MIS");
                put("new_product", "CNC");
            }
        };
        JSONObject jsonObject6 = kiteConnect.convertPosition(param6);
        System.out.println(jsonObject6);
    }

    /** Get all instruments that can be traded using kite connect.*/
    public void getAllInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get all instruments list. This call is very expensive as it involves downloading of large data dump.
        // Hence, it is recommended that this call be made once and the results stored locally once every morning before market opening.
        List<Instrument> instruments = kiteConnect.getInstruments();
        System.out.println(instruments.size());
    }

    /** Get instruments for the desired exchange.*/
    public void getInstrumentsForExchange(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get instruments for an exchange.
        List<Instrument> nseInstruments = kiteConnect.getInstruments("CDS");
        System.out.println(nseInstruments.size());
    }

    /** Get quote for a scrip.*/
    public void getQuote(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get quotes returns quote for desired tradingsymbol.
        String[] instruments = {"256265","BSE:INFY", "NSE:APOLLOTYRE", "NSE:NIFTY 50"};
        Map<String, Quote> quotes = kiteConnect.getQuote(instruments);
        System.out.println(quotes.get("NSE:APOLLOTYRE").instrumentToken+"");
        System.out.println(quotes.get("NSE:APOLLOTYRE").openInterest+"");
        System.out.println(quotes.get("NSE:APOLLOTYRE").depth.buy.get(4).getPrice());
        System.out.println(quotes.get("NSE:APOLLOTYRE").timestamp);
    }

    /* Get ohlc and lastprice for multiple instruments at once.
     * Users can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}*/
    public void getOHLC(KiteConnect kiteConnect) throws KiteException, IOException {
        String[] instruments = {"256265","BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
        System.out.println(kiteConnect.getOHLC(instruments).get("256265").lastPrice);
        System.out.println(kiteConnect.getOHLC(instruments).get("NSE:NIFTY 50").ohlc.open);
    }

    /** Get last price for multiple instruments at once.
     * USers can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}*/
    public void getLTP(KiteConnect kiteConnect) throws KiteException, IOException {
        String[] instruments = {"256265","BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
        System.out.println(kiteConnect.getLTP(instruments).get("256265").lastPrice);
    }

    /** Get historical data for an instrument.*/
    public void getHistoricalData(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Get historical data dump, requires from and to date, intrument token, interval
         * returns historical data object which will have list of historical data inside the object*/
        Map<String, Object> param8 = new HashMap<String, Object>(){
            {
                put("continuous", 0);
                put("from", "2018-01-03 12:00:00");
                put("to", "2018-01-03 22:49:12");
            }
        };
        HistoricalData historicalData = kiteConnect.getHistoricalData(param8, "11946498", "15minute");
        System.out.println(historicalData.dataArrayList.size());
        System.out.println(historicalData.dataArrayList.get(0).volume);
        System.out.println(historicalData.dataArrayList.get(historicalData.dataArrayList.size() - 1).volume);
    }

    /** Logout user.*/
    public void logout(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Logout user and kill session. */
        JSONObject jsonObject10 = kiteConnect.logout();
        System.out.println(jsonObject10);
    }

    /** Retrieve mf instrument dump */
    public void getMFInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MfInstrument> mfList = kiteConnect.getMFInstruments();
        System.out.println("size of mf instrument list: "+mfList.size());
    }

    /* Get all mutualfunds holdings */
    public void getMFHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MfHolding> mfHoldings = kiteConnect.getMFHoldings();
        System.out.println("mf holdings "+mfHoldings.size());
    }

    /* Place a mutualfunds order */
    public void placeMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("tradingsymbol", "INF174K01LS2");
        params.put("transaction_type", "BUY");
        params.put("amount", "5000");
        System.out.println("place order: "+ kiteConnect.placeMFOrder(params).orderId);
    }

    /* cancel mutualfunds order */
    public void cancelMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        kiteConnect.cancelMFOrder("106580291331583");
        System.out.println("cancel order successful");
    }

    /* retrieve all mutualfunds orders */
    public void getMFOrders(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MfOrder> mfOrders = kiteConnect.getMFOrders();
        System.out.println("mf orders: "+mfOrders.get(0).orderId+ " "+mfOrders.get(0).tradingsymbol);
    }

    /* retrieve individual mutualfunds order */
    public void getMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("mf order: "+ kiteConnect.getMFOrder("106580291331583").tradingsymbol);
    }

    /* place mutualfunds sip */
    public void placeMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("tradingsymbol", "INF174K01LS2");
        params.put("frequency", "monthly");
        params.put("instalment_day",1);
        params.put("instalments", -1);
        params.put("initial_amount", 5000);
        params.put("amount", 1000);
        System.out.println("mf place sip: "+ kiteConnect.placeMFSIP(params).sipId);
    }

    /* modify a mutual fund sip */
    public void modifyMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("frequency", "weekly");
        params.put("instalments", 5);
        params.put("amount", 1000);
        params.put("status", "active");
        params.put("day", 1);
        kiteConnect.modifyMFSIP(params, "291156521960679");
    }

    /* cancel a mutualfunds sip */
    public void cancelMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        kiteConnect.cancelMFSIP("291156521960679");
        System.out.println("cancel sip successful");
    }

    /* retrieve all mutualfunds sip */
    public void getMFSIPS(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("mf sips: "+ kiteConnect.getMFSIPs().size());
    }

    /* retrieve individual mutualfunds sip */
    public void getMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("mf sip: "+ kiteConnect.getMFSIP("291156521960679").instalments);
    }

    /** Demonstrates com.rainmatter.ticker connection, subcribing for instruments, unsubscribing for instruments, set mode of tick data, com.rainmatter.ticker disconnection*/
    public void tickerUsage(KiteConnect kiteConnect, ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        /** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
        KiteTicker tickerProvider = new KiteTicker(kiteConnect.getUserId(), kiteConnect.getAccessToken(), kiteConnect.getApiKey(), "wss://websocket.kite.trade/v3");

        tickerProvider.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                /** Subscribe ticks for token.
                 * By default, all tokens are subscribed for modeQuote.
                 * */
                tickerProvider.subscribe(tokens);
                tickerProvider.setMode(tokens, KiteTicker.modeFull);
            }
        });

        tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                // your code goes here
            }
        });

        /** Set listener to get order updates.*/
        tickerProvider.setOnOrderUpdateListener(new OnOrderUpdate() {
            @Override
            public void onOrderUpdate(Order order) {
                System.out.println("order update "+order.orderId);
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                NumberFormat formatter = new DecimalFormat();
                System.out.println("ticks size "+ticks.size());
                if(ticks.size() > 0) {
                    System.out.println("last price "+ticks.get(0).getLastTradedPrice());
                    System.out.println("open interest "+formatter.format(ticks.get(0).getOpenInterest()));
                    System.out.println("day high OI "+formatter.format(ticks.get(0).getDayHighOpenInterest()));
                    System.out.println("day low OI "+formatter.format(ticks.get(0).getDayLowOpenInterest()));
                    System.out.println("tick timestamp "+ticks.get(0).getTickTimestamp());
                    System.out.println("tick timestamp date "+ticks.get(0).getTickTimestamp());
                    System.out.println("last traded time "+ticks.get(0).getLastTradedTime());
                    System.out.println(ticks.get(0).getMarketDepth().get("buy").size());
                }
            }
        });

        tickerProvider.setTryReconnection(true);
        //maximum retries and should be greater than 0
        tickerProvider.setMaximumRetries(50);
        //set maximum retry interval in seconds
        tickerProvider.setMaximumRetryInterval(30);

        /** connects to com.rainmatter.com.rainmatter.ticker server for getting live quotes*/
        tickerProvider.connect();

        /** You can check, if websocket connection is open or not using the following method.*/
        boolean isConnected = tickerProvider.isConnectionOpen();
        System.out.println(isConnected);

        /** set mode is used to set mode in which you need tick for list of tokens.
         * Ticker allows three modes, modeFull, modeQuote, modeLTP.
         * For getting only last traded price, use modeLTP
         * For getting last traded price, last traded quantity, average price, volume traded today, total sell quantity and total buy quantity, open, high, low, close, change, use modeQuote
         * For getting all data with depth, use modeFull*/
        tickerProvider.setMode(tokens, KiteTicker.modeLTP);

        // Unsubscribe for a token.
        tickerProvider.unsubscribe(tokens);

        // After using com.rainmatter.com.rainmatter.ticker, close websocket connection.
       tickerProvider.disconnect();
    }
}
