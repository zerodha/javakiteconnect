import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.*;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.ticker.KiteTicker;
import com.rainmatter.ticker.OnConnect;
import com.rainmatter.ticker.OnDisconnect;
import com.rainmatter.ticker.OnTick;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 15/10/16.
 */
public class Examples {

    /**Gets Margin.*/
    public void getMargins(KiteConnect kiteconnect) throws KiteException {
        // Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
        Margins margins = kiteconnect.getMargins("equity");
        System.out.println(margins.available.cash);
        System.out.println(margins.utilised.debits);
    }

    /**Place order.*/
    public void placeOrder(KiteConnect kiteconnect) throws KiteException {
        /** Place order method requires a map argument which contains,
         * tradingsymbol, exchange, transaction_type, order_type, quantity, product, price, trigger_price, disclosed_quantity, validity
         * squareoff_value, stoploss_value, trailing_stoploss
         * and variety (value can be regular, bo, co, amo)
         * place order which will return order model which will have only orderId in the order model
         *
         * Following is an example param for SL order,
         * if a call fails then KiteException will have error message in it
         * Success of this call implies only order has been placed successfully, not order execution */
        Map<String, Object> param = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "SL");
                put("tradingsymbol", "HINDALCO");
                put("product", "CNC");
                put("exchange", "NSE");
                put("transaction_type", "BUY");
                put("validity", "DAY");
                put("price", "158.0");
                put("trigger_price", "157.5");
            }
        };
        Order order = kiteconnect.placeOrder(param, "regular");
        System.out.println(order.orderId);
    }

    /** Place bracket order.*/
    public void placeBracketOrder(KiteConnect kiteconnect) throws KiteException {
        /** Bracket order:- following is example param for bracket order*
         * trailing_stoploss and stoploss_value are points not tick or price
         */
        Map<String, Object> param10 = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "LIMIT");
                put("price", "1.4");
                put("transaction_type", "BUY");
                put("tradingsymbol", "ANKITMETAL");
                put("trailing_stoploss", "1");
                put("stoploss_value", "1");
                put("exchange", "NSE");
                put("validity", "DAY");
                put("squareoff_value", "1");
                put("product", "MIS");
            }
        };
        Order order10 = kiteconnect.placeOrder(param10, "bo");
        System.out.println(order10.orderId);
    }

    /** Place cover order.*/
    public void placeCoverOrder(KiteConnect kiteconnect) throws KiteException {
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
                put("tradingsymbol", "HINDALCO");
                put("exchange", "NSE");
                put("validity", "DAY");
                put("trigger_price", "157");
                put("order_type", "MARKET");
                put("product", "MIS");
            }
        };
        Order order11 = kiteconnect.placeOrder(param11, "co");
        System.out.println(order11.orderId);
    }

    /** Get trigger range.*/
    public void getTriggerRange(KiteConnect kiteconnect) throws KiteException {
        // You need to send a map with transaction_type, exchange and tradingsymbol to get trigger range.
        Map<String, Object> params12 = new HashMap<>();
        params12.put("transaction_type", "SELL");
        TriggerRange triggerRange = kiteconnect.getTriggerRange("NSE", "RELIANCE", params12);
        System.out.println(triggerRange.start);
    }

    /** Get orderbook.*/
    public void getOrders(KiteConnect kiteconnect) throws KiteException {
        // Get orders returns order model which will have list of orders inside, which can be accessed as follows,
        Order order1 = kiteconnect.getOrders();
        for(int i = 0; i< order1.orders.size(); i++){
            System.out.println(order1.orders.get(i).tradingSymbol+" "+order1.orders.get(i).orderId);
        }
        System.out.println("list of orders size is "+order1.orders.size());
    }

    /** Get tradebook*/
    public void getTrades(KiteConnect kiteconnect) throws KiteException {
        // Returns tradebook.
        Trade trade = kiteconnect.getTrades();
        System.out.println(trade.trades.size());
    }

    /** Get trades for an order.*/
    public void getTradesWithOrderId(KiteConnect kiteconnect) throws KiteException {
        // Returns trades for the given order.
        Trade trade1 = kiteconnect.getTrades("161007000088484");
        System.out.println(trade1.trades.size());
    }

    /** Modify order.*/
    public void modifyOrder(KiteConnect kiteconnect) throws KiteException {
        // Order modify request will return order model which will contain only order_id.
        Map<String, Object> params = new HashMap<String, Object>(){
            {
                put("quantity", "1");
                put("order_type", "SL");
                put("tradingsymbol", "HINDALCO");
                put("product", "CNC");
                put("exchange", "NSE");
                put("transaction_type", "BUY");
                put("validity", "DAY");
                put("price", "158.0");
                put("trigger_price", "157.5");
            }
        };
        Order order21 = kiteconnect.modifyOrder("161007000088484", params, "regular");
        System.out.println(order21.orderId);
    }

    /** Cancel an order*/
    public void cancelOrder(KiteConnect kiteconnect) throws KiteException {
        // Order modify request will return order model which will contain only order_id.
        // Cancel order will return order model which will only have orderId.
        Order order2 = kiteconnect.cancelOrder("161007000088484", "regular");
        System.out.println(order2.orderId);
    }

    /** Get all positions.*/
    public void getPositions(KiteConnect kiteconnect) throws KiteException {
        // Get positions returns position model which contains list of positions.
        Position position = kiteconnect.getPositions();
        System.out.println(position.positions.size());
    }

    /** Get holdings.*/
    public void getHoldings(KiteConnect kiteconnect) throws KiteException {
        // Get holdings returns holdings model which contains list of holdings.
        Holding holding = kiteconnect.getHoldings();
        System.out.println(holding.holdings);
    }

    /** Converts position*/
    public void modifyProduct(KiteConnect kiteconnect) throws KiteException {
        //Modify product can be used to change MIS to NRML(CNC) or NRML(CNC) to MIS.
        Map<String, Object> param6 = new HashMap<String, Object>(){
            {
                put("exchange", "NSE");
                put("tradingsymbol", "RELIANCE");
                put("transaction_type", "BUY");
                put("position_type", "day"); //can also be overnight
                put("quantity", "1");
                put("old_product", "MIS");
                put("new_product", "CNC");
            }
        };
        JSONObject jsonObject6 = kiteconnect.modifyProduct(param6);
        System.out.println(jsonObject6);
    }

    /** Get all instruments that can be traded using kite connect.*/
    public void getAllInstruments(KiteConnect kiteconnect) throws KiteException, IOException {
        // Get all instruments list. This call is very expensive as it involves downloading of large data dump.
        // Hence, it is recommended that this call be made once and the results stored locally once every morning before market opening.
        List<Instrument> instruments = kiteconnect.getInstruments();
        System.out.println(instruments.size());
    }

    /** Get instruments for the desired exchange.*/
    public void getInstrumentsForExchange(KiteConnect kiteconnect) throws KiteException, IOException {
        // Get instruments for an exchange.
        List<Instrument> nseInstruments = kiteconnect.getInstruments("NSE");
        System.out.println(nseInstruments.size());
    }

    /** Get quote for a scrip.*/
    public void getQuote(KiteConnect kiteconnect) throws KiteException {
        // Get quotes returns quote for desired tradingsymbol.
        Quote quote = kiteconnect.getQuote("NSE", "RELIANCE");
    }

    /** Get historical data for an instrument.*/
    public void getHistoricalData(KiteConnect kiteconnect) throws KiteException {
        /** Get historical data dump, requires from and to date, intrument token, interval
         * returns historical data object which will have list of historical data inside the object*/
        Map<String, Object> param8 = new HashMap<String, Object>(){
            {
                put("from", "2016-10-01");
                put("to", "2016-10-06");
            }
        };
        HistoricalData historicalData = kiteconnect.getHistoricalData(param8, "738561", "minute");
        System.out.println(historicalData.dataArrayList.size());
        System.out.println(historicalData.dataArrayList.get(0).volume);
        System.out.println(historicalData.dataArrayList.get(historicalData.dataArrayList.size() - 1).volume);
    }

    /** Logout user.*/
    public void logout(KiteConnect kiteconnect) throws KiteException {
        /** Logout user and kill session. */
        JSONObject jsonObject10 = kiteconnect.logout();
        System.out.println(jsonObject10);
    }

    /** Demonstrates ticker connection, subcribing for instruments, unsubscribing for instruments, set mode of tick data, ticker disconnection*/
    public void tickerUsage(KiteConnect kiteconnect) throws IOException, WebSocketException {
        /** To get live price use com.rainmatter.ticker websocket connection. It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.*/
        ArrayList tokens = new ArrayList<>();
        tokens.add(53287175);
        KiteTicker tickerProvider = new KiteTicker(kiteconnect);
        tickerProvider.setOnConnectedListener(new OnConnect() {
            @Override
            public void onConnected() {
                try {
                    /** Subscribe ticks for token.
                     * By default, all tokens are subscribed for modeQuote.
                     * */
                    tickerProvider.subscribe(tokens);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WebSocketException e) {
                    e.printStackTrace();
                }
            }
        });

        tickerProvider.setOnDisconnectedListener(new OnDisconnect() {
            @Override
            public void onDisconnected() {
                // your code goes here
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTick() {
            @Override
            public void onTick(ArrayList<Tick> ticks) {
                System.out.println(ticks.size());
            }
        });

        /** connects to com.rainmatter.ticker server for getting live quotes*/
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

        // After using com.rainmatter.ticker, close websocket connection.
        tickerProvider.disconnect();
    }
}
