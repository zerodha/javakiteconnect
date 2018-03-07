# The Kite Connect 3.0 API Java client
The official Java client for communicating with [Kite Connect API](https://kite.trade).

Kite Connect is a set of REST-like APIs that expose many capabilities required to build a complete investment and trading platform. Execute orders in real time, manage user portfolio, stream live market data (WebSockets), and more, with the simple HTTP API collection.

[Zerodha Technology Pvt Ltd](http://rainmatter.com) (c) 2016. Licensed under the MIT License.

## Documentation
- [Kite Connect HTTP API documentation](https://kite.trade/docs/connect/v3/)
- [Java library documentation](https://kite.trade/docs/javakiteconnect/v3/)

## Usage
- [Download Kite Connect 3 jar file](https://github.com/zerodhatech/javakiteconnect/tree/kite3/dist) and include it in your build path.

- Include com.rainmatter.kiteconnect into build path from maven. Use version 3.0.0

- To use javakiteconnect in **Android**, you need to include jar file in the libs directory and add the following line in you module's gradle file ``` compile files('libs/kiteconnect.jar') ```

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
 /** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
        KiteTicker tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());

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
                    System.out.println("open interest "+formatter.format(ticks.get(0).getOi()));
                    System.out.println("day high OI "+formatter.format(ticks.get(0).getOpenInterestDayHigh()));
                    System.out.println("day low OI "+formatter.format(ticks.get(0).getOpenInterestDayLow()));
                    System.out.println("change "+formatter.format(ticks.get(0).getChange()));
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

        /** connects to com.zerodhatech.com.zerodhatech.ticker server for getting live quotes*/
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

        // After using com.zerodhatech.com.zerodhatech.ticker, close websocket connection.
        tickerProvider.disconnect();

```
For more details about different mode of quotes and subscribing for them, take a look at Examples in sample directory.

 ## Breaking changes from version 2 to version 3

 #### Place order (bracket order) parameters

| version 2 | version 3 |
| :---: | :---:|
| squareoff_value | squareoff |
| stoploss_value | stoploss |

 #### Model name changes
 
 | version 2 | version 3 |
 | :---: | :---:|
 | MfHolding | MFHolding |
 | MfInstrument | MFInstrument |
 | MfOrder | MFOrder |
 | MfSip | MFSIP |

 ##### Order (model)
 
 * The orderTimestamp is now Date type.
 * The exchangeTimestamp is now Date type.
 
 #### Trades (model)
 
 * The orderTimestamp is now fillTimestamp.
 * The exchangeTimestamp is now Date type.
 
 #### MFOrder (model)

 * The orderTimestamp is now Date type.
 * The exchangeTimestamp is now Date type. 
 
 #### MFSIP (model)
 
 * The created is now Date type.
 * The Date is now Date type.
 
 #### MFInstrument (model)
 
 * The purchase_allowed is now boolean type.
 * The redemption_allowed is now boolean type.
 * The last_price_date is now Date type.
 
 #### Instrument (model)
 
 * The expiry is now Date type.

 #### Package name changes
 
 | version 2 | version 3 |
 | :---: | :---: |
 |com.rainmatter.kitehttp|com.zerodhatech.kiteconnect.kitehttp|
 |com.rainmatter.utils|com.zerodhatech.kiteconnect.utils|
 |com.rainmatter.kiteconnect|com.zerodhatech.kiteconnect|
 |com.rainmatter.ticker|com.zerodhatech.kiteconnect|
 |com.rainmatter.models|com.zerodhatech.models|
 
 #### Method name changes
 
 | version 2 | version 3 |
 | :---: | :---: |
 | requestAccessToken | generateSession |
 | modifyProduct | convertPosition |
 | getOrder | getOrderHistory |
 | getTrades(order_id) | getOrderTrades(order_id) |
 | getMfOrders | getMFOrders |
 | getMfOrder | getMFOrder |
 | getMfSips | getMFSIPs |
 | getMfSip | getMFSIP |
 | modifySip | modifySIP |
 | cancelSip | cancelSIP |
 | getMfInstruments | getMFInstruments |
 
 #### Method with signature change
 
 | version 2 |
 | :---: |
 | placeOrder |
 | modifyOrder |
 | cancelOrder |
 | convertPosition |
 | getTriggerRange |
 | getHistoricalData |
 | placeMFOrder |
 | placeMFSIP |
 | modifyMFSIP |
 
 For more details about each method go to [KiteConnect.java](https://github.com/zerodhatech/javakiteconnect/blob/kite3/kiteconnect/src/com/zerodhatech/kiteconnect/KiteConnect.java)
 
 #### Funds (model)
 
 | version 2 | version 3 |
 | :---: | :---: |
 | Margins | Margin |
  
 #### User (model)
 
 * UserModel is now User.
 
  | version 2 | version 3 |
  | :---: | :---: |
  | product | products |
  | exchange | exchanges |
  | orderType | orderTypes |
  | passwordReset | **NA** |
  | memberId | **NA** |
  | **NA** | apiKey |
  
 * loginTime is now of Date type.
  
 #### Position (model)
 
 Added new fields 
 
  | version 3 |
  | :---: |
  | dayBuyQuantity |
  | daySellQuantity |
  | dayBuyPrice |  
  | daySellPrice |
  | dayBuyValue |
  | daySellValue |
  | value |
  
  #### Kite Ticker (Websockets)
  
  * Kite Ticker is now authenticated using access_token and not public_token.
  
  Version 2: 
  ```java
  Kiteconnect kiteSdk = new Kiteconnect("your_apiKey");
  ```
  Version 3:
  ```java
  KiteTicker tickerProvider = new KiteTicker(kiteConnect.getUserId(), kiteConnect.getAccessToken(), kiteConnect.getApiKey());
  ```  
  * Order postbacks are now streamed on Kite Ticker.
   
  * Added new fields in full mode.
  
  | version 3 |
  | :---: |
  | lastTradedTime |
  | openInterest |
  | oiDayHigh |
  | oiDayLow |
  | tickTimestamp |
  
  * Changes:
  
  | version 2 | version 3 |
  | :---: | :---: |
  | OnTick | OnTicks |
  | setTimeIntervalForReconnection | **NA** |
  | **NA** | setMaximumRetryInterval |
  | netPriceChangeFromClosingPrice | change |
  
  #### Quote 
  
  * Quote will accept multiple params and returns a map of Quote model.
  * Added new fields open interest, tick timestamp, last traded time, average price, day high OI, day low OI.
  * lastTradedPrice is now lastPrice.
  
  | version 3 |
  | :---: |
  | instrumentToken |
  | timestamp |
  | averagePrice |
  | oiDayHigh |
  | oiDayLow |
  
  * Changes:
  
  | version 2 | version 3 |
  | :---: | :---: |
  | lastTime(String) | lastTradedTime(Date) |
  | changePercent | **NA** |
  | depth(Map<String, ArrayList<Depth>>) | depth(MarketDepth type) |
  
  
  * Removed: 
   
  | version 2 | version 3 |
  | :---: | :---: |
  | IndicesQuote | **NA** |
  
  #### Profile
  
  * Added new profile API call to fetch user details.
  
  #### Trigger range
  
  * Trigger range API supports fetching trigger range for multiple instruments in one request.
  
  #### TriggerRange (model)
  
  | version 2 | version 3 |
  | :---: | :---: |
  | start | lower |
  | end | upper |
  | percent | percentage |