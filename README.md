# The Kite Connect API Java client
The official Java client for communicating with [Kite Connect API](https://kite.trade).

Kite Connect is a set of REST-like APIs that expose many capabilities required to build a complete investment and trading platform. Execute orders in real time, manage user portfolio, stream live market data (WebSockets), and more, with the simple HTTP API collection.

[Rainmatter](http://rainmatter.com) (c) 2016. Licensed under the MIT License.

##Documentation
- [Kite Connect HTTP API documentation](https://kite.trade/docs/connect/v1)
- [Java library documentation](https://kite.trade/docs/javakiteconnect)

##Usage
- [Download jar file](https://github.com/rainmattertech/kiteconnectjava/raw/master/dist/kiteconnectjava.jar) and include it in your build path.

- Include com.rainmatter.kiteconnect into build path from maven. Use version 1.4.3

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
kiteSdk.registerHook(new SessionExpiryHook() {
    @Override
    public void sessionExpired() {
        System.out.println("session expired");                    
    }
});

// Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
Margins margins = kiteSdk.getMargins("equity");
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

##WebSocket live streaming data
```java

/** To get live price use KiteTicker websocket connection. 
It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.*/
ArrayList tokens = new ArrayList<>();
tokens.add(53287175);
KiteTicker tickerProvider = new KiteTicker(kiteconnect);
tickerProvider.setOnConnectedListener(new OnConnect() {
    @Override
    public void onConnected() {
        try {
            /** Subscribe ticks for token.
              * By default, all tokens are subscribed for modeQuote.*/
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

// Connects to ticker server for getting live quotes.
tickerProvider.connect();

// Disconnect from ticker server.
tickerProvider.disconnect();

```
For more details about different mode of quotes and subscribing for them, take a look at Examples in sample directory.
