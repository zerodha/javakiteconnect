# The Kite Connect API Java client
The official Java client for communicating with [Kite Connect API](https://kite.trade).

Kite Connect is a set of REST-like APIs that expose many capabilities required to build a complete investment and trading platform. Execute orders in real time, manage user portfolio, stream live market data (WebSockets), and more, with the simple HTTP API collection.

[Rainmatter](http://rainmatter.com) (c) 2016. Licensed under the MIT License.

## Documentation
- [Kite Connect HTTP API documentation](https://kite.trade/docs/connect/v1)
- [Java library documentation](https://kite.trade/docs/javakiteconnect)

## Usage
- [Download jar file](https://github.com/rainmattertech/javakiteconnect/blob/master/dist/kiteconnect.jar) and include it in your build path.

- Include com.rainmatter.kiteconnect into build path from maven. Use version 1.4.5

## API usage
```java
// Initialize Kiteconnect using apiKey.
Kiteconnect kiteSdk = new Kiteconnect("your_apiKey");

// Set userId.
kiteSdk.setUserId("your_userId");

/* First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteconnect api call.
Get login url. Use this url in webview to login user, after authenticating user you will get requestToken. Use the same to get accessToken. */
String url = kiteSdk.getLoginUrl();

// Get accessToken as follows,
UserModel userModel =  kiteSdk.requestAccessToken("request_token", "your_apiSecret");

// Set request token and public token which are obtained from login process.
kiteSdk.setAccessToken(userModel.accessToken);
kiteSdk.setPublicToken(userModel.publicToken);

// Set session expiry callback.
kiteSdk.setSessionExpiryHook(new SessionExpiryHook() {
    @Override
    public void sessionExpired() {
        System.out.println("session expired");                    
    }
});

// Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
Margin margins = kiteSdk.getMargins("equity");
System.out.println(margins.available.cash);
System.out.println(margins.utilised.debits);

/* Place order method requires a map argument which contains,
tradingsymbol, exchange, transaction_type, order_type, quantity, product, price, trigger_price, disclosed_quantity, validity
squareoff_value, stoploss_value, trailing_stoploss
and variety  which can be value can be regular, bo, co, amo.
place order which will return order model which will have only orderId in the order model.

Following is an example param for SL order,
if a call fails then KiteException will have error message in it
Success of this call implies only order has been placed successfully, not order execution.*/
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
    }};
Order order = kiteconnect.placeOrder(param, "regular");
System.out.println(order.orderId);
```
For more details, take a look at Examples.java in sample directory.

## WebSocket live streaming data
```java

/** To get live price use KiteTicker websocket connection. 
It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.*/
ArrayList tokens = new ArrayList<>();
tokens.add(53287175);
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

        tickerProvider.setOnTickerArrivalListener(new OnTick() {
            @Override
            public void onTick(ArrayList<Tick> ticks) {
                NumberFormat formatter = new DecimalFormat();
                System.out.println("ticks size "+ticks.size());
                if(ticks.size() > 0) {
                    System.out.println("last price "+ticks.get(0).getLastTradedPrice());
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

```
For more details about different mode of quotes and subscribing for them, take a look at Examples in sample directory.

## Breaking changes

* For placing bracket orders, use squareoff instead of squareoff_value and stoploss instead of stoploss_value.

* Changes in package name:
    1. **com.rainmatter.kitehttp** is now **com.rainmatter.kiteconnect.kitehttp**
    2. **com.rainmatter.utils** is now **com.rainmatter.kiteconnect.utils**

* Margins model is now Margin.

* Changes in UserModel(requestAccessToken API),
    1. product is now products    
    2. exchange is now exchanges
    3. order_types(orderType) is now order_types(orderTypes)
    4. password_reset is removed
    5. member_id is removed
    6. api_key(apiKey) is added

* Changes in positions model
    1. Added buy_m2m_value(buym2mValue)
    2. Added sell_m2m_value(sellm2mValue)
    3. Added day_buy_quantity(dayBuyQuantity)
    4. Added day_sell_quantity(daySellQuantity)
    5. Added day_buy_price(dayBuyPrice)
    6. Added day_sell_price(daySellPrice)
    7. Added day_buy_value(dayBuyValue)
    8. Added day_sell_value(daySellValue)
    9. Added value(value)
    10. Added token(token)

* **getMfOrders** is now **getMFOrders**

* **getMfOrder** is now **getMFOrder**      

* **getMfSips** is now **getMFSIPs**

* **getMfSip** is now **getMFSIP**

* **modifySip** is now **modifySIP**

* **cancelSip** is now **cancelSIP**

* **getMfInstruments** is now **getMFInstruments**

* **modifyProduct** is now **convertPosition**