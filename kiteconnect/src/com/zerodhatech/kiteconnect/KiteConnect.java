package com.zerodhatech.kiteconnect;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zerodhatech.kiteconnect.kitehttp.KiteRequestHandler;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.supercsv.cellprocessor.*;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.lang.reflect.Type;
import java.net.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by sujith on 11/16/17.
 */
public class KiteConnect {

    public static SessionExpiryHook sessionExpiryHook = null;
    public static boolean ENABLE_LOGGING = false;
    private Proxy proxy = null;
    private String apiKey;
    private String accessToken;
    private String publicToken;
    private Routes routes = new Routes();
    private String userId;
    private Gson gson;


    /** Initializes KiteSDK with the api key provided for your App.
     * @param apiKey is the api key provided after creating new Kite Connect App.
     */
    public KiteConnect(String apiKey){
        this.apiKey = apiKey;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

            @Override
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                try {
                    return format.parse(jsonElement.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        });
        gson = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
    }

    /** Registers callback for session error.
     * @param hook can be set to get callback when session is expired.
     * */
    public void setSessionExpiryHook(SessionExpiryHook hook){
        sessionExpiryHook = hook;
    }

    /** Enables logging of requests and responses.
     * @param enable is set to true to enable logging. */
    public void setEnableLogging(boolean enable) {
        ENABLE_LOGGING = enable;
    }

    /** Set proxy. */
    public void setProxy(Proxy proxy){
        this.proxy = proxy;
    }

    /**
     *  Returns apiKey of the App.
     * @return  String apiKey is returned.
     * @throws NullPointerException if _apiKey is not found.
     */
    public String getApiKey() throws NullPointerException{
        if (apiKey != null)
            return apiKey;
        else
            throw new NullPointerException();
    }

    /**
     * Returns accessToken.
     * @return String access_token is returned.
     * @throws NullPointerException if accessToken is null.
     */
    public String getAccessToken() throws NullPointerException{
        if(accessToken != null)
            return accessToken;
        else
            throw new NullPointerException();
    }

    /** Returns userId.
     * @return String userId is returned.
     * @throws  NullPointerException if userId is null.*/
    public String getUserId() throws NullPointerException{
        if(userId != null) {
            return userId;
        }else {
            throw new NullPointerException();
        }
    }

    /** Set userId.
     * @param id is user_id. */
    public void setUserId(String id){
        userId = id;
    }

    /** Returns publicToken.
     * @throws NullPointerException if publicToken is null.
     * @return String public token is returned.
     * */
    public String getPublicToken() throws NullPointerException{
        if(publicToken != null){
            return publicToken;
        }else {
            throw new NullPointerException();
        }
    }

    /**
     * Set the accessToken received after a successful authentication.
     * @param accessToken is the access token received after sending request token and api secret.
     */
    public void setAccessToken(String accessToken){
        this.accessToken = accessToken;
    }

    /**
     * Set publicToken.
     * @param publicToken is the public token received after sending request token and api secret.
     * */
    public void setPublicToken(String publicToken){
        this.publicToken = publicToken;
    }

    /**Retrives login url
     * @return String loginUrl is returned. */
    public String getLoginURL() throws NullPointerException{
        String baseUrl = routes.getLoginUrl();
        return baseUrl+"?api_key="+apiKey+"&v=3";
    }

    /**
     * Do the token exchange with the `request_token` obtained after the login flow,
     * and retrieve the `access_token` required for all subsequent requests.
     * @param requestToken received from login process.
     * @param apiSecret which is unique for each aap.
     * @return User is usermodel which contains user and session details.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public User generateSession(String requestToken, String apiSecret) throws KiteException, JSONException, IOException {

        // Create the checksum needed for authentication.
        String hashableText = this.apiKey + requestToken + apiSecret;
        String sha256hex = sha256Hex(hashableText);

        // Create JSON params object needed to be sent to api.
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("api_key", apiKey);
        params.put("request_token", requestToken);
        params.put("checksum", sha256hex);

        return  new User().parseResponse(new KiteRequestHandler(proxy).postRequest(routes.get("api.validate"), params, apiKey, accessToken));
    }

    /** Get a new access token using refresh token.
     * @param refreshToken is the refresh token obtained after generateSession.
     * @param apiSecret is unique for each app.
     * @return TokenSet contains user id, refresh token, api secret.
     * @throws IOException is thrown when there is connection error.
     * @throws KiteException is thrown for all Kite trade related errors. */
    public TokenSet renewAccessToken(String refreshToken, String apiSecret) throws IOException, KiteException {
        String hashableText = this.apiKey + refreshToken + apiSecret;
        String sha256hex = sha256Hex(hashableText);

        Map<String, Object> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("refresh_token", refreshToken);
        params.put("checksum", sha256hex);

        JSONObject response = new KiteRequestHandler(proxy).postRequest(routes.get("api.refresh"), params, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), TokenSet.class);
    }

    /** Hex encodes sha256 ouput for android support.*/
    public String sha256Hex(String str) {
        byte[] a = DigestUtils.sha256(str);
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    public Profile getProfile() throws IOException, KiteException {
        String url = routes.get("user.profile");
        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), Profile.class);
    }

    /**
     * Gets account balance and cash margin details for a particular segment.
     * Example for segment can be equity or commodity.
     * @param segment can be equity or commodity.
     * @return Margins object.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Margin getMargins(String segment) throws KiteException, JSONException, IOException {
        String url = routes.get("user.margins.segment").replace(":segment", segment);
        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), Margin.class);
    }

    /**
     * Gets account balance and cash margin details for a equity and commodity.
     * Example for segment can be equity or commodity.
     * @return Map<String, Margin> map of commodity and equity margins data.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Map<String, Margin> getMargins() throws KiteException, JSONException, IOException {
        String url = routes.get("user.margins");
        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, apiKey, accessToken);
        Type type = new TypeToken<Map<String, Margin>>(){}.getType();
        return gson.fromJson(String.valueOf(response.get("data")), type);
    }

    /** Gets instruments margins for equity, commodity, currency, futures.
     * @return List<InstrumetMargin> is the list of instruments margins data.
     * @throws IOException is thrown when there is conection error.
     * @throws KiteException is thrown for all kite trade related errors.
     * */
    public List<InstrumentMargin> getInstrumentsMargins(String segment) throws IOException, KiteException {
        String url = routes.get("instruments.margins").replace("segment", segment);
        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), InstrumentMargin[].class));
    }

    /**
     * Places an order.
     * @param params is Order params.
     *               params.exchange - Exchange in which instrument is listed (NSE, BSE, NFO, BFO, CDS, MCX).
     *               params.tradingsymbol - Tradingsymbol of the instrument  (ex. RELIANCE, INFY).
     *               params.transaction_type - Transaction type (BUY or SELL).
     *               params.quantity - Order quantity
     *               params.price - Order Price
     *               params.product	- Product code (NRML, MIS, CNC).
     *               params.order_type - Order type (NRML, SL, SL-M, MARKET).
     *               params.validity - Order validity (DAY, IOC).
     *               params.disclosed_quantity - Disclosed quantity
     *               params.trigger_price - Trigger price
     *               params.squareoff_value - Square off value (only for bracket orders)
     *               params.stoploss_value - Stoploss value (only for bracket orders)
     *               params.trailing_stoploss - Trailing stoploss value (only for bracket orders)
     *
     * @param variety variety="regular". Order variety can be bo, co, amo, regular.
     * @return Order contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Order placeOrder(Map<String, Object> params, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.place").replace(":variety", variety);

        JSONObject jsonObject = new KiteRequestHandler(proxy).postRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**
     * Modifies an open order.
     *
     * @param params
     *               params.exchange - Exchange in which instrument is listed (NSE, BSE, NFO, BFO, CDS, MCX).
     *               params.tradingsymbol - Tradingsymbol of the instrument  (ex. RELIANCE, INFY).
     *               params.transaction_type - Transaction type (BUY or SELL).
     *               params.quantity - Order quantity
     *               params.price - Order Price
     *               params.product - Product code (NRML, MIS, CNC).
     *               params.order_type - Order type (NRML, SL, SL-M, MARKET).
     *               params.validity - Order validity (DAY, IOC).
     *               params.disclosed_quantity - Disclosed quantity
     *               params.trigger_price - Trigger price
     * @param variety variety="regular". Order variety can be bo, co, amo, regular.
     * @param orderId order id of the order being modified.
     * @return Order object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Order modifyOrder(String orderId, Map<String, Object> params, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.modify").replace(":variety", variety).replace(":order_id", orderId);
        JSONObject jsonObject = new KiteRequestHandler(proxy).putRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**
     * Cancels an order.
     * @param orderId order id of the order to be cancelled.
     * @param variety [variety="regular"]. Order variety can be bo, co, amo, regular.
     * @return Order object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Order cancelOrder(String orderId, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<String, Object>();

        JSONObject jsonObject = new KiteRequestHandler(proxy).deleteRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**
     * Cancels special orders like BO, CO
     * @param params is map that contains parent_order_id
     * @param orderId order id of the order to be cancelled.
     * @param variety [variety="regular"]. Order variety can be bo, co, amo, regular.
     * @return Order object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public Order cancelOrder(Map<String, Object> params, String orderId, String variety) throws KiteException, IOException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);

        JSONObject jsonObject = new KiteRequestHandler(proxy).deleteRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**Gets collection of orders from the orderbook..
     * @return List of orders.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * */
    public List<Order> getOrders() throws KiteException, JSONException, IOException {
        String url = routes.get("orders");
        Map<String, Object> params = new HashMap<>();

        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }

    /** Returns list of different stages an order has gone through.
     * @return List of multiple stages an order has gone through in the system.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param orderId is the order id which is obtained from orderbook.
     * */
    public List<Order> getOrderHistory(String orderId) throws KiteException, IOException {
        String url = routes.get("order").replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<>();
        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }

    /**
     * Retreives list of trades executed.
     * @return List of trades.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public List<Trade> getTrades() throws KiteException, JSONException, IOException {
        Map<String, Object> params = new HashMap<>();
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("trades"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Trade[].class));
    }

    /**
     * Retreives list of trades executed of an order.
     * @param orderId order if of the order whose trades are fetched.
     * @return List of trades for the given order.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public List<Trade> getOrderTrades(String orderId) throws KiteException, JSONException, IOException {
        Map<String, Object> params = new HashMap<String, Object>();
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("orders.trades").replace(":order_id", orderId), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Trade[].class));
    }

    /**
     * Retrieves the list of holdings.
     * @return List of holdings.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public List<Holding> getHoldings() throws KiteException, JSONException, IOException {
        Map<String, Object> params = new HashMap<>();
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("portfolio.holdings"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Holding[].class));
    }

    /**
     * Retrieves the list of positions.
     * @return List of positions.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Map<String, List<Position>> getPositions() throws KiteException, JSONException, IOException {
        Map<String, Object> params = new HashMap<>();
        Map<String, List<Position>> positionsMap = new HashMap<>();
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("portfolio.positions"), apiKey, accessToken);
        JSONObject allPositions = response.getJSONObject("data");
        positionsMap.put("net", Arrays.asList(gson.fromJson(String.valueOf(allPositions.get("net")), Position[].class)));
        positionsMap.put("day", Arrays.asList(gson.fromJson(String.valueOf(allPositions.get("day")), Position[].class)));
        return positionsMap;
    }


    /**
     * Modifies an open position's product type.
     * @param params include tradingsymbol, exchange, transaction_type, position_type, old_product, new_product, quantity
     * @return JSONObject  which will have status.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public JSONObject convertPosition(Map<String, Object> params) throws KiteException, JSONException, IOException {
        KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(proxy);
        return kiteRequestHandler.putRequest(routes.get("portfolio.positions.modify"), params, apiKey, accessToken);
    }

    /**
     * Retrieves list of market instruments available to trade.
     *
     * 	 Response is array for objects. For example,
     * 	{
     * 		instrument_token: '131098372',
     *		exchange_token: '512103',
     *		tradingsymbol: 'NIDHGRN',
     *		name: 'NIDHI GRANITES',
     *		last_price: '0.0',
     *		expiry: '',
     *		strike: '0.0',
     *		tick_size: '0.05',
     *		lot_size: '1',
     *		instrument_type: 'EQ',
     *		segment: 'BSE',
     *		exchange: 'BSE' }, ...]
     * @return List of intruments which are available to trade.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related errors.
     */
    public List<Instrument> getInstruments() throws KiteException, IOException {
        KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(proxy);
        return readCSV(kiteRequestHandler.getCSVRequest(routes.get("market.instruments.all"), apiKey, accessToken));
    }

    /**
     * Retrieves list of market instruments available to trade for an exchange
     *
     * 	 Response is array for objects. For example,
     * 	{
     * 		instrument_token: '131098372',
     *		exchange_token: '512103',
     *		tradingsymbol: 'NIDHGRN',
     *		name: 'NIDHI GRANITES',
     *		last_price: '0.0',
     *		expiry: '',
     *		strike: '0.0',
     *		tick_size: '0.05',
     *		lot_size: '1',
     *		instrument_type: 'EQ',
     *		segment: 'BSE',
     *		exchange: 'BSE' }, ...]
     * @param exchange  Filter instruments based on exchange. exchange can be NSE, BSE, NFO, BFO, CDS, MCX.
     * @return List of instruments which are available to trade for an exchange.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there connection related error.
     */
    public List<Instrument> getInstruments(String exchange) throws KiteException, JSONException, IOException {
        KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(proxy);
        return readCSV(kiteRequestHandler.getCSVRequest(routes.get("market.instruments").replace(":exchange", exchange), apiKey, accessToken));
    }

    /**
     * Retrieves quote and market depth for an instrument
     *
     * @param instruments is the array of tradingsymbol and exchange or instrument token. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     *
     * @return Quote object.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Map<String, Quote> getQuote(String [] instruments) throws KiteException, JSONException, IOException {
        KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(proxy);
        JSONObject jsonObject = kiteRequestHandler.getRequest(routes.get("market.quote"), "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, Quote>>(){}.getType();
        return gson.fromJson(String.valueOf(jsonObject.get("data")), type);
    }

    /** Retrieves ohlc and last price.
     * User can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     * @return Map which contains key value pair of user input data as key and data as value.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param instruments is the array of tradingsymbol and exchange or instruments token.
     * */
    public Map<String, OHLCQuote> getOHLC(String [] instruments) throws KiteException, IOException {
        JSONObject resp = new KiteRequestHandler(proxy).getRequest(routes.get("quote.ohlc"), "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, OHLCQuote>>(){}.getType();
        return gson.fromJson(String.valueOf(resp.get("data")), type);
    }

    /** Retrieves last price.
     * User can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}.
     * @return Map which contains key value pair of user input data as key and data as value.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param instruments is the array of tradingsymbol and exchange or instruments token.
     * */
    public Map<String, LTPQuote> getLTP(String[] instruments) throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("quote.ltp"), "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, LTPQuote>>(){}.getType();
        return gson.fromJson(String.valueOf(response.get("data")), type);
    }

    /**
     * Retrieves buy or sell trigger range for Cover Orders.
     * @param exchange can be NSE, BSE, MCX
     * @param tradingSymbol is the instrument name.
     * @param params must have transaction_type as "BUY or "SELL".
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @return TriggerRange object is returned.
     */
    public TriggerRange getTriggerRange(String exchange, String tradingSymbol, Map<String, Object> params) throws KiteException, JSONException, IOException {
        String url = routes.get("market.trigger_range").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol);
        JSONObject response = new KiteRequestHandler(proxy).getRequest(url, params, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), TriggerRange.class);
    }

    /** Retrieves historical data for an instrument.
     * @param params contains from = "yyyy-mm-dd" and to = "yyyy-mm-dd" for fetching candles between two days or
     *               from = "yyyy-mm-dd hh:mm:ss" and to = "yyyy-mm-dd hh:mm:ss" for fetching candles between two timestamps.
     * @param params continuous = 1 can be used for fetching continuous data of expired instruments.
     * @param interval can be minute, day, 3minute, 5minute, 10minute, 15minute, 30minute, 60minute.
     * @param token is instruments token.
     * @return HistoricalData object which contains list of historical data termed as dataArrayList.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public HistoricalData getHistoricalData(Map<String, Object> params, String token, String interval) throws KiteException, IOException {
        String url = routes.get("market.historical").replace(":instrument_token", token).replace(":interval", interval);
        HistoricalData historicalData = new HistoricalData();
        historicalData.parseResponse(new KiteRequestHandler(proxy).getRequest(url, params, apiKey, accessToken));
        return historicalData;
    }

    /** Retrieves mutualfunds instruments.
     * @return returns list of mutual funds instruments.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related errors.
     * */
    public List<MfInstrument> getMFInstruments() throws KiteException, IOException{
        Map<String, Object> params = new HashMap<String, Object>();
        KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(proxy);
        return readMfCSV(kiteRequestHandler.getCSVRequest(routes.get("mutualfunds.instruments"), apiKey, accessToken));
    }

    /** Place a mutualfunds order.
     * @param params includes tradingsymbol, transaction_type, amount.
     * @return MfOrder object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfOrder placeMFOrder(Map<String, Object> params) throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).postRequest(routes.get("mutualfunds.orders.place"), params, apiKey, accessToken);
        MfOrder mfOrder = new MfOrder();
        mfOrder.orderId = response.getJSONObject("data").getString("order_id");
        return mfOrder;
    }

    /** If cancel is successful then api will respond as 200 and send back true else it will be sent back to user as KiteException.
     * @return true if api call is successful.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param orderId is the order id of the mutualfunds order.
     * */
    public boolean cancelMFOrder(String orderId) throws KiteException, IOException {
        KiteRequestHandler kiteRequestHandler = new KiteRequestHandler(proxy);
        kiteRequestHandler.deleteRequest(routes.get("mutualfunds.cancel_order").replace(":order_id", orderId), new HashMap<>(), apiKey, accessToken);
        return true;
    }

    /** Retrieves all mutualfunds orders.
     * @return List of all the mutualfunds orders.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public List<MfOrder> getMFOrders() throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("mutualfunds.orders"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfOrder[].class));
    }

    /** Retrieves individual mutualfunds order.
     * @param orderId is the order id of a mutualfunds scrip.
     * @return returns a single mutualfunds object with all the parameters.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfOrder getMFOrder(String orderId) throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("mutualfunds.order").replace(":order_id", orderId), apiKey, accessToken);
        return gson.fromJson(response.get("data").toString(), MfOrder.class);
    }

    /** Place a mutualfunds sip.
     * @param params contains tradingsymbol, frequency, day, instalments, initial_amount, amount.
     * @return MfSip object which contains sip id and order id.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfSip placeMFSIP(Map<String, Object> params) throws KiteException, IOException {
        MfSip mfSip = new MfSip();
        JSONObject response = new KiteRequestHandler(proxy).postRequest(routes.get("mutualfunds.sips.place"),params, apiKey, accessToken);
        mfSip.orderId = response.getJSONObject("data").getString("order_id");
        mfSip.sipId = response.getJSONObject("data").getString("sip_id");
        return mfSip;
    }

    /** Modify a mutualfunds sip.
     * @param params contains frequency, instalments, amount, status, day.
     * @param sipId is the id of the sip.
     * @return returns true, if modify sip is successful else exception is thrown.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public boolean modifyMFSIP(Map<String, Object> params, String sipId) throws KiteException, IOException {
        new KiteRequestHandler(proxy).putRequest(routes.get("mutualfunds.sips.modify").replace(":sip_id", sipId), params, apiKey, accessToken);
        return true;
    }

    /** Cancel a mutualfunds sip.
     * @param sipId is the id of mutualfunds sip.
     * @return returns true, if cancel sip is successful else exception is thrown.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public boolean cancelMFSIP(String sipId) throws KiteException, IOException {
        new KiteRequestHandler(proxy).deleteRequest(routes.get("mutualfunds.sip").replace(":sip_id", sipId), new HashMap<>(), apiKey, accessToken);
        return true;
    }

    /** Retrieve all mutualfunds sip.
     * @return List of sips.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public List<MfSip> getMFSIPs() throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("mutualfunds.sips"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfSip[].class));
    }

    /** Retrieve an individual sip.
     * @param sipId is the id of a particular sip.
     * @return MfSip object which contains all the details of the sip.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfSip getMFSIP(String sipId) throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("mutualfunds.sip").replace(":sip_id", sipId), apiKey, accessToken);
        return gson.fromJson(response.get("data").toString(), MfSip.class);
    }

    /** Retrieve all the mutualfunds holdings.
     * @return List of mutualfunds holdings.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public List<MfHolding> getMFHoldings() throws KiteException, IOException {
        JSONObject response = new KiteRequestHandler(proxy).getRequest(routes.get("mutualfunds.holdings"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfHolding[].class));
    }
    /**
     * Logs out user by invalidating the access token.
     * @return JSONObject which contains status
     * @throws KiteException is thrown for all Kite trade related errors.
     */
    public JSONObject logout() throws KiteException, IOException {
        return invalidateAccessToken();
    }

    /**
     * Kills the session by invalidating the access token.
     * @return JSONObject which contains status
     * @throws KiteException is thrown for all Kite trade related errors.
     */
    public JSONObject invalidateAccessToken() throws IOException, KiteException {
        String url = routes.get("logout");
        return new KiteRequestHandler(proxy).deleteRequest(url, new HashMap<>(), apiKey, accessToken);
    }

    /**
     * Kills the refresh token.
     * @return JSONObject contains status.
     * @throws IOException is thrown for connection related errors.
     * @throws KiteException is thrown for Kite trade related errors.
     * */
    public JSONObject invalidateRefreshToken(String refreshToken) throws IOException, KiteException {
        Map<String, Object> param = new HashMap<>();
        param.put("refresh_token", refreshToken);
        String url = routes.get("api.refresh");
        return new KiteRequestHandler(proxy).deleteRequest(url, param, apiKey, accessToken);
    }

    /**This method parses csv and returns instrument list.
     * @param input is csv string.
     * @return  returns list of instruments.
     * @throws IOException is thrown when there is connection related error.
     * */
    private List<Instrument> readCSV(String input) throws IOException {
        ICsvBeanReader beanReader = null;
        File temp = File.createTempFile("tempfile", ".tmp");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(input);
        bw.close();

        beanReader = new CsvBeanReader(new FileReader(temp), CsvPreference.STANDARD_PREFERENCE);
        String[] header = beanReader.getHeader(true);
        CellProcessor[] processors = getProcessors();
        Instrument instrument;
        List<Instrument> instruments = new ArrayList<>();
        while((instrument = beanReader.read(Instrument.class, header, processors)) != null ) {
            instruments.add(instrument);
        }
        return instruments;
    }

    /**This method parses csv and returns instrument list.
     * @param input is mutualfunds csv string.
     * @return  returns list of mutualfunds instruments.
     * @throws IOException is thrown when there is connection related error.
     * */
    private List<MfInstrument> readMfCSV(String input) throws IOException{
        ICsvBeanReader beanReader = null;
        File temp = File.createTempFile("tempfile", ".tmp");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(input);
        bw.close();

        beanReader = new CsvBeanReader(new FileReader(temp), CsvPreference.STANDARD_PREFERENCE);
        String[] header = beanReader.getHeader(true);
        CellProcessor[] processors = getMfProcessors();
        MfInstrument instrument;
        List<MfInstrument> instruments = new ArrayList<>();
        while((instrument = beanReader.read(MfInstrument.class, header, processors)) != null ) {
            System.out.println(instrument.tradingsymbol);
            instruments.add(instrument);
        }
        return instruments;
    }

    /** This method returns array of cellprocessor for parsing csv.
     * @return CellProcessor[] array
     * */
    private CellProcessor[] getProcessors(){
        CellProcessor[] processors = new CellProcessor[]{
                new NotNull(new ParseLong()),   //instrument_token
                new NotNull(new ParseLong()),   //exchange_token
                new NotNull(),                  //trading_symbol
                new org.supercsv.cellprocessor.Optional(),                 //company name
                new NotNull(new ParseDouble()), //last_price
                new org.supercsv.cellprocessor.Optional(),                 //expiry
                new org.supercsv.cellprocessor.Optional(),                 //strike
                new NotNull(new ParseDouble()), //tick_size
                new NotNull(new ParseInt()),    //lot_size
                new NotNull(),                  //instrument_type
                new NotNull(),                  //segment
                new NotNull()                   //exchange
        };
        return processors;
    }

    /** This method returns array of cellprocessor for parsing mutual funds csv.
     * @return CellProcessor[] array
     * */
    private CellProcessor[] getMfProcessors(){
        CellProcessor[] processors = new CellProcessor[]{
                new org.supercsv.cellprocessor.Optional(),                  //tradingsymbol
                new org.supercsv.cellprocessor.Optional(),                  //amc
                new org.supercsv.cellprocessor.Optional(),                  //name
                new org.supercsv.cellprocessor.Optional(new ParseInt()),    //purchase_allowed
                new org.supercsv.cellprocessor.Optional(new ParseInt()),    //redemption_allowed
                new org.supercsv.cellprocessor.Optional(new ParseDouble()), //minimum_purchase_amount
                new org.supercsv.cellprocessor.Optional(new ParseDouble()), //purchase_amount_multiplier
                new org.supercsv.cellprocessor.Optional(new ParseDouble()), //minimum_additional_purchase_amount
                new org.supercsv.cellprocessor.Optional(new ParseDouble()), //minimum_redemption_quantity
                new org.supercsv.cellprocessor.Optional(new ParseDouble()), //redemption_quantity_multiplier
                new org.supercsv.cellprocessor.Optional(),                  //dividend_type
                new org.supercsv.cellprocessor.Optional(),                  //scheme_type
                new org.supercsv.cellprocessor.Optional(),                  //plan
                new org.supercsv.cellprocessor.Optional(),                  //settlement_type
                new org.supercsv.cellprocessor.Optional(new ParseDouble()), //last_price
                new org.supercsv.cellprocessor.Optional()                   //last_price_date
        };
        return processors;
    }

}