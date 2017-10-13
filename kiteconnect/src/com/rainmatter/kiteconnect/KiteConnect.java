package com.rainmatter.kiteconnect;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.rainmatter.kitehttp.KiteRequest;
import com.rainmatter.kitehttp.SessionExpiryHook;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.http.HttpHost;
import org.json.JSONException;
import org.json.JSONObject;
import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.ParseDouble;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ParseLong;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;

import java.io.*;
import java.lang.reflect.Type;
import java.util.*;

/**
 * Provides all the capabilities of com.rainmatter.kiteconnect like place order, fetch orderbook, positions, holdings and more.
 */
public class KiteConnect {

    private String _apiKey;
    private String _accessToken;
    private String _publicToken;
    public static SessionExpiryHook sessionExpiryHook;
    public static HttpHost httpHost;

    private Routes routes = new Routes();
    private String userId;
    private Gson gson;

    /** Initializes KiteSDK with the api key provided for your App.
     * @param apiKey is the api key provided after creating new Kite Connect App.
     */
    public KiteConnect(String apiKey){
        _apiKey = apiKey;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gson = gsonBuilder.create();
    }

    /** Registers callback for session error.
     * @param sessionExpiryHook can be set to get callback when session is expired.
     * */
    public void registerHook(SessionExpiryHook sessionExpiryHook){
        this.sessionExpiryHook = sessionExpiryHook;
    }

    /*Set proxy
    * @param httpHost includes remote host name, port and scheme */
    public void setProxy(HttpHost httpHost){
        this.httpHost  = httpHost;
    }

    /**
     *  Returns apiKey of the App.
     * @return  String apiKey is returned.
     * @throws NullPointerException if _apiKey is not found.
     */
    public String getApiKey() throws NullPointerException{
        if (_apiKey != null)
            return _apiKey;
        else
            throw new NullPointerException();
    }

    /**
     * Returns accessToken.
     * @return String access_token is returned.
     * @throws NullPointerException if accessToken is null.
     */
    public String getAccessToken() throws NullPointerException{
        if(_accessToken != null)
            return _accessToken;
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
        if(_publicToken != null){
            return _publicToken;
        }else {
            throw new NullPointerException();
        }
    }

    /**
     * Adds apiKey and accessToken to the outgoing request.
     * @param params is map arguments which is sent for each request.
     * @return Map params with accessToken and apiKey.
     */
    private Map<String, Object> authorize(Map<String, Object> params){
        if (_apiKey != null)
            params.put("api_key", this._apiKey);

        if(_accessToken != null)
            params.put("access_token", _accessToken);

        return params;
    }

    /**
     * Set the accessToken received after a successful authentication.
     * @param accessToken is the access token received after sending request token and api secret.
     */
    public void setAccessToken(String accessToken){
        _accessToken = accessToken;
    }

    /**
     * Set publicToken.
     * @param publicToken is the public token received after sending request token and api secret.
     * */
    public void setPublicToken(String publicToken){
        _publicToken = publicToken;
    }

    /**Retrives login url
     * @return String loginUrl is returned. */
    public String getLoginUrl() throws NullPointerException{
        String baseUrl = routes.getLoginUrl();
        return baseUrl+"?"+"api_key="+_apiKey;
    }

    /**
     * Do the token exchange with the `request_token` obtained after the login flow,
     * and retrieve the `access_token` required for all subsequent requests.
     * @param requestToken received from login process.
     * @param apiSecret which is unique for each aap.
     * @return UserModel is usermodel which contains user and session details.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public UserModel requestAccessToken(String requestToken, String apiSecret) throws KiteException, JSONException {

       // Create the checksum needed for authentication.
       String hashableText = this._apiKey + requestToken + apiSecret;
       String sha256hex = DigestUtils.sha256Hex(hashableText);

       // Create JSON params object needed to be sent to api.
       Map<String, Object> params = new HashMap<String, Object>();
       params.put("request_token", requestToken);
       params.put("checksum", sha256hex);

       return  new UserModel().parseResponse(new KiteRequest().postRequest(routes.get("api.validate"), authorize(params)));
    }


    /**
     * Gets account balance and cash margin details for a particular segment.
     * Example for segment can be equity or commodity.
     * @param segment can be equity or commodity.
     * @return Margins object.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Margins getMargins(String segment) throws KiteException, JSONException {
       Map<String, Object> params = new HashMap<String, Object>();
       String url = routes.get("user.margins").replace(":segment", segment);
       JSONObject response = new KiteRequest().getRequest(url, authorize(params));
       return gson.fromJson(String.valueOf(response.get("data")), Margins.class);
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
    public Order placeOrder(Map<String, Object> params, String variety) throws KiteException, JSONException {
        String url = routes.get("orders.place").replace(":variety", variety);

        JSONObject jsonObject = new KiteRequest().postRequest(url, authorize(params));
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
    public Order modifyOrder(String orderId, Map<String, Object> params, String variety) throws KiteException, JSONException {
        String url = routes.get("orders.modify").replace(":variety", variety).replace(":order_id", orderId);
        JSONObject jsonObject = new KiteRequest().putRequest(url, authorize(params));
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
    public Order cancelOrder(String orderId, String variety) throws KiteException, JSONException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<String, Object>();

        JSONObject jsonObject = new KiteRequest().deleteRequest(url, authorize(params));
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
    public Order cancelOrder(Map<String, Object> params, String orderId, String variety) throws KiteException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);
        params = authorize(params);

        JSONObject jsonObject = new KiteRequest().deleteRequest(url, params);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**Gets collection of orders from the orderbook..
     * @return List of orders.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * */
    public List<Order> getOrders() throws KiteException, JSONException {
        String url = routes.get("orders");
        Map<String, Object> params = new HashMap<>();

        JSONObject response = new KiteRequest().getRequest(url, authorize(params));
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }

    /** Returns list of different stages an order has gone through.
     * @return List of multiple stages an order has gone through in the system.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param orderId is the order id which is obtained from orderbook.
     * */
    public List<Order> getOrder(String orderId) throws KiteException {
        String url = routes.get("order").replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<>();
        JSONObject response = new KiteRequest().getRequest(url, authorize(params));
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }

    /**
     * Retreives list of trades executed.
     * @return List of trades.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public List<Trade> getTrades() throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<>();
        JSONObject response = new KiteRequest().getRequest(routes.get("trades"), authorize(params));
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Trade[].class));
    }

    /**
     * Retreives list of trades executed of an order.
     * @param orderId order if of the order whose trades are fetched.
     * @return List of trades for the given order.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public List<Trade> getTrades(String orderId) throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        JSONObject response = new KiteRequest().getRequest(routes.get("orders.trades").replace(":order_id", orderId), authorize(params));
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Trade[].class));
    }

    /**
     * Retrieves the list of holdings.
     * @return List of holdings.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public List<Holding> getHoldings() throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<>();
        JSONObject response = new KiteRequest().getRequest(routes.get("portfolio.holdings"), authorize(params));
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Holding[].class));
    }

    /**
     * Retrieves the list of positions.
     * @return List of positions.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Map<String, List<Position>> getPositions() throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<>();
        Map<String, List<Position>> positionsMap = new HashMap<>();
        JSONObject response = new KiteRequest().getRequest(routes.get("portfolio.positions"), authorize(params));
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
    public JSONObject modifyProduct(Map<String, Object> params) throws KiteException, JSONException {
        KiteRequest kiteRequest = new KiteRequest();
        return kiteRequest.putRequest(routes.get("portfolio.positions.modify"), authorize(params));
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
        KiteRequest kiteRequest = new KiteRequest();
        return readCSV(kiteRequest.getCsvRequest(routes.get("market.instruments.all")));
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
        KiteRequest kiteRequest = new KiteRequest();
        return readCSV(kiteRequest.getCsvRequest(routes.get("market.instruments").replace(":exchange", exchange)));
    }

    /**
     * Retrieves quote and market depth for an instrument
     *
     * @param exchange  Exchange in which instrument is listed. exchange can be NSE, BSE, NFO, BFO, CDS, MCX.
     * @param tradingSymbol Tradingsymbol of the instrument (ex. RELIANCE, INFY).
     * @return Quote object.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public Quote getQuote(String exchange, String tradingSymbol) throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        KiteRequest kiteRequest = new KiteRequest();
        JSONObject jsonObject = kiteRequest.getRequest(routes.get("market.quote").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol), authorize(params));
        return new Quote().parseResponse(jsonObject);
    }

    /**
     * Retrieves quote for an index
     *
     * @param exchange  Exchange in which instrument is listed. exchange can be NSE, BSE.
     * @param tradingSymbol Tradingsymbol of the instrument (ex. NIFTY 50).
     * @return IndicesQuote object.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     */
    public IndicesQuote getQuoteIndices(String exchange, String tradingSymbol) throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        KiteRequest kiteRequest = new KiteRequest();
        JSONObject response = kiteRequest.getRequest(routes.get("market.quote").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol), authorize(params));
        return gson.fromJson(String.valueOf(response.get("data")), IndicesQuote.class);
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
    public TriggerRange getTriggerRange(String exchange, String tradingSymbol, Map<String, Object> params) throws KiteException, JSONException {
        String url = routes.get("market.trigger_range").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol);
        JSONObject response = new KiteRequest().getRequest(url, authorize(params));
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
    public HistoricalData getHistoricalData(Map<String, Object> params, String token, String interval) throws KiteException {
        String url = routes.get("market.historical").replace(":instrument_token", token).replace(":interval", interval);
        HistoricalData historicalData = new HistoricalData();
        historicalData.parseResponse(new KiteRequest().getRequest(url, authorize(params)));
        return historicalData;
    }

    /** Retrieves mutualfunds instruments.
     * @return returns list of mutual funds instruments.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related errors.
     * */
    public List<MfInstrument> getMfInstruments() throws KiteException, IOException{
        Map<String, Object> params = new HashMap<String, Object>();
        KiteRequest kiteRequest = new KiteRequest();
        return readMfCSV(kiteRequest.getCsvRequest(routes.get("mutualfunds.instruments"), params));
    }

    /** Place a mutualfunds order.
     * @param params includes tradingsymbol, transaction_type, amount.
     * @return MfOrder object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfOrder placeMfOrder(Map<String, Object> params) throws KiteException {
        params = authorize(params);
        JSONObject response = new KiteRequest().postRequest(routes.get("mutualfunds.orders.place"), params);
        MfOrder mfOrder = new MfOrder();
        mfOrder.orderId = response.getJSONObject("data").getString("order_id");
        return mfOrder;
    }

    /** If cancel is successful then api will respond as 200 and send back true else it will be sent back to user as KiteException.
     * @return true if api call is successful.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param orderId is the order id of the mutualfunds order.
     * */
    public boolean cancelMfOrder(String orderId) throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        KiteRequest kiteRequest = new KiteRequest();
        kiteRequest.deleteRequest(routes.get("mutualfunds.cancel_order").replace(":order_id", orderId), params);
        return true;
    }

    /** Retrieves all mutualfunds orders.
     * @return List of all the mutualfunds orders.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public List<MfOrder> getMfOrders() throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject response = new KiteRequest().getRequest(routes.get("mutualfunds.orders"), params);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfOrder[].class));
    }

    /** Retrieves individual mutualfunds order.
     * @param orderId is the order id of a mutualfunds scrip.
     * @return returns a single mutualfunds object with all the parameters.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfOrder getMfOrder(String orderId) throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject response = new KiteRequest().getRequest(routes.get("mutualfunds.order").replace(":order_id", orderId), params);
        return gson.fromJson(response.get("data").toString(), MfOrder.class);
    }

    /** Place a mutualfunds sip.
     * @param params contains tradingsymbol, frequency, day, instalments, initial_amount, amount.
     * @return MfSip object which contains sip id and order id.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfSip placeMfSip(Map<String, Object> params) throws KiteException {
        params = authorize(params);
        MfSip mfSip = new MfSip();
        JSONObject response = new KiteRequest().postRequest(routes.get("mutualfunds.sips.place"),params);
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
    public boolean modifyMfSip(Map<String, Object> params, String sipId) throws KiteException {
        params = authorize(params);
        new KiteRequest().putRequest(routes.get("mutualfunds.sips.modify").replace(":sip_id", sipId), params);
        return true;
    }

    /** Cancel a mutualfunds sip.
     * @param sipId is the id of mutualfunds sip.
     * @return returns true, if cancel sip is successful else exception is thrown.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public boolean cancelMfSip(String sipId) throws KiteException {
        Map<String, Object> params = new HashMap<>();
        authorize(params);
        new KiteRequest().deleteRequest(routes.get("mutualfunds.sip").replace(":sip_id", sipId), params);
        return true;
    }

    /** Retrieve all mutualfunds sip.
     * @return List of sips.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public List<MfSip> getMfSips() throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject response = new KiteRequest().getRequest(routes.get("mutualfunds.sips"), params);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfSip[].class));
    }

    /** Retrieve an individual sip.
     * @param sipId is the id of a particular sip.
     * @return MfSip object which contains all the details of the sip.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public MfSip getMfSip(String sipId) throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject response = new KiteRequest().getRequest(routes.get("mutualfunds.sip").replace(":sip_id", sipId), params);
        return gson.fromJson(response.get("data").toString(), MfSip.class);
    }

    /** Retrieve all the mutualfunds holdings.
     * @return List of mutualfunds holdings.
     * @throws KiteException is thrown for all Kite trade related errors.
     * */
    public List<MfHolding> getMfHoldings() throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject response = new KiteRequest().getRequest(routes.get("mutualfunds.holdings"), params);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MfHolding[].class));
    }

    /** Retrieves ohlc and last price.
     * User can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     * @return Map which contains key value pair of user input data as key and data as value.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param instruments is the array of tradingsymbol and exchange or instruments token.
     * */
    public Map<String, OHLCQuote> getOHLC(String [] instruments) throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject resp = new KiteRequest().getRequest(routes.get("quote.ohlc"), params, "i", instruments);
        Type type = new TypeToken<Map<String, OHLCQuote>>(){}.getType();
        return gson.fromJson(String.valueOf(resp.get("data")), type);
    }

    /** Retrieves last price.
     * User can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}.
     * @return Map which contains key value pair of user input data as key and data as value.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param instruments is the array of tradingsymbol and exchange or instruments token.
     * */
    public Map<String, LTPQuote> getLTP(String[] instruments) throws KiteException {
        Map<String, Object> params = new HashMap<>();
        params = authorize(params);
        JSONObject response = new KiteRequest().getRequest(routes.get("quote.ltp"), params, "i", instruments);
        Type type = new TypeToken<Map<String, LTPQuote>>(){}.getType();
        return gson.fromJson(String.valueOf(response.get("data")), type);
    }

    /**
     * Kills the session by invalidating the access token.
     * @return JSONObject which contains status
     * @throws KiteException is thrown for all Kite trade related errors.
     */
    public JSONObject logout() throws KiteException {
        Map<String, Object> params = new HashMap<String, Object>();
        String url = routes.get("logout");
        return new KiteRequest().deleteRequest(url, authorize(params));
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
                new Optional(),                 //company name
                new NotNull(new ParseDouble()), //last_price
                new Optional(),                 //expiry
                new Optional(),                 //strike
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
                new Optional(),                  //tradingsymbol
                new Optional(),                  //amc
                new Optional(),                  //name
                new Optional(new ParseInt()),    //purchase_allowed
                new Optional(new ParseInt()),    //redemption_allowed
                new Optional(new ParseDouble()), //minimum_purchase_amount
                new Optional(new ParseDouble()), //purchase_amount_multiplier
                new Optional(new ParseDouble()), //minimum_additional_purchase_amount
                new Optional(new ParseDouble()), //minimum_redemption_quantity
                new Optional(new ParseDouble()), //redemption_quantity_multiplier
                new Optional(),                  //dividend_type
                new Optional(),                  //scheme_type
                new Optional(),                  //plan
                new Optional(),                  //settlement_type
                new Optional(new ParseDouble()), //last_price
                new Optional()                   //last_price_date
        };
        return processors;
    }
}
