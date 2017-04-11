package com.rainmatter.kiteconnect;

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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Provides all the capabilities of kiteconnect like place order, fetch orderbook, positions, holdings and more.
 */
public class KiteConnect {

    private String _apiKey;
    private String _accessToken;
    private String _publicToken;
    public static SessionExpiryHook sessionExpiryHook;
    public static HttpHost httpHost;

    private Routes routes = new Routes();
    private String userId;

    /** Initializes KiteSDK with the api key provided for your App.
     * @param apiKey
     */
    public KiteConnect(String apiKey){
        _apiKey = apiKey;
    }

    /** Registers callback for session error.
     * @param sessionExpiryHook
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
     * @param accessToken
     */
    public void setAccessToken(String accessToken){
        _accessToken = accessToken;
    }

    /**
     * Set publicToken.
     * @param publicToken
     * */
    public void setPublicToken(String publicToken){
        _publicToken = publicToken;
    }

    /**Retrives login url
     * @return String loginUrl */
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
     */
    public Margins getMargins(String segment) throws KiteException, JSONException {
       Map<String, Object> params = new HashMap<String, Object>();
       String url = routes.get("user.margins").replace(":segment", segment);
       return new Margins().parseMarginsResponse(new KiteRequest().getRequest(url, authorize(params)));
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
     */
    public Order placeOrder(Map<String, Object> params, String variety) throws KiteException, JSONException {
        String url = routes.get("orders.place").replace(":variety", variety);

        JSONObject jsonObject = new KiteRequest().postRequest(url, authorize(params));
        Order order =  new Order();
        order.parseOrderPlacedResponse(jsonObject);
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
     * @return Order object contains only orderId
     */
    public Order modifyOrder(String orderId, Map<String, Object> params, String variety) throws KiteException, JSONException {
        String url = routes.get("orders.modify").replace(":variety", variety).replace(":order_id", orderId);
        JSONObject jsonObject = new KiteRequest().putRequest(url, authorize(params));
        Order order =  new Order();
        order.parseOrderPlacedResponse(jsonObject);
        return order;
    }

    /**
     * Cancels an order.
     * @param orderId order id of the order to be cancelled.
     * @param variety [variety="regular"]. Order variety can be bo, co, amo, regular.
     * @return Order object contains only orderId
     */
    public Order cancelOrder(String orderId, String variety) throws KiteException, JSONException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<String, Object>();

        JSONObject jsonObject = new KiteRequest().deleteRequest(url, authorize(params));
        Order order =  new Order();
        order.parseOrderPlacedResponse(jsonObject);
        return order;
    }

    /**
     * Cancels special orders like BO, CO
     * @param params is map that contains parent_order_id
     * @param orderId order id of the order to be cancelled.
     * @param variety [variety="regular"]. Order variety can be bo, co, amo, regular.
     * @return Order object contains only orderId
     * */
    public Order cancelOrder(Map<String, Object> params, String orderId, String variety) throws KiteException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);
        params = authorize(params);

        JSONObject jsonObject = new KiteRequest().deleteRequest(url, params);
        Order order =  new Order();
        order.parseOrderPlacedResponse(jsonObject);
        return order;
    }

    /**Gets collection of orders from the orderbook..
     * @return Order object contains orders which is list of orders.
     * */
    public Order getOrders() throws KiteException, JSONException {
        String url = routes.get("orders");
        Map<String, Object> params = new HashMap<String, Object>();

        JSONObject jsonObject = new KiteRequest().getRequest(url, authorize(params));
        Order order =  new Order();
        order.parseListOrdersResponse(jsonObject);
        return order;
    }

    /** Returns list of different stages an order has gone through.
     * @return Order object contains orders which is list of multiple stages an order has gone through.
     * */
    public Order getOrder(String orderId) throws KiteException {
        String url = routes.get("order").replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<>();

        JSONObject jsonObject = new KiteRequest().getRequest(url, authorize(params));
        Order order = new Order();
        order.parseListOrdersResponse(jsonObject);
        return order;
    }

    /**
     * Retreives list of trades executed.
     * @return Trade object contains trades which is list of trades.
     */
    public Trade getTrades() throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        JSONObject jsonObject = new KiteRequest().getRequest(routes.get("trades"), authorize(params));
        Trade trade = new Trade();
        trade.parseListTradesResponse(jsonObject);
        return trade;
    }

    /**
     * Retreives list of trades executed of an order.
     * @param orderId order if of the order whose trades are fetched.
     * @return Trade object contains trades list for the given order
     */
    public Trade getTrades(String orderId) throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        JSONObject jsonObject = new KiteRequest().getRequest(routes.get("orders.trades").replace(":order_id", orderId), authorize(params));
        Trade trade = new Trade();
        trade.parseListTradesResponse(jsonObject);
        return trade;
    }

    /**
     * Retrieves the list of holdings.
     * @return Holding object contains holdings which is list of holdings.
     */
    public Holding getHoldings() throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        Holding holding = new Holding();
        holding.parseHoldingsResponse(new KiteRequest().getRequest(routes.get("portfolio.holdings"), authorize(params)));
        return holding;
    }

    /**
     * Retrieves the list of positions.
     * @return Position object contains positions which is list of positions
     */
    public Position getPositions() throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        Position position = new Position();
        position.parseGetPositionsResponse(new KiteRequest().getRequest(routes.get("portfolio.positions"), authorize(params)));
        return position;
    }


    /**
     * Modifies an open position's product type.
     * @param params include tradingsymbol, exchange, transaction_type, position_type, old_product, new_product, quantity
     * @return JSONObject  which will have status.
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
     * @return List<Instrument> is list of intruments which are available to trade.
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
     * @return List<Instrument> is list of intruments which are available to trade for an exchange.
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
     */
    public Quote getQuote(String exchange, String tradingSymbol) throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        KiteRequest kiteRequest = new KiteRequest();
        return new Quote().parseResponse(kiteRequest.getRequest(routes.get("market.quote").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol), authorize(params)));
    }

    /**
     * Retrieves quote for an index
     *
     * @param exchange  Exchange in which instrument is listed. exchange can be NSE, BSE.
     * @param tradingSymbol Tradingsymbol of the instrument (ex. NIFTY 50).
     * @return IndicesQuote object.
     */
    public IndicesQuote getQuoteIndices(String exchange, String tradingSymbol) throws KiteException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        KiteRequest kiteRequest = new KiteRequest();
        return new IndicesQuote().parseIndicesResponse(kiteRequest.getRequest(routes.get("market.quote").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol), authorize(params)));
    }

    /**
     * Retrieves buy or sell trigger range for Cover Orders.
     * @param exchange
     * @param tradingSymbol
     * @param params must have transaction_type as "BUY or "SELL"
     */
    public TriggerRange getTriggerRange(String exchange, String tradingSymbol, Map<String, Object> params) throws KiteException, JSONException {
        String url = routes.get("market.trigger_range").replace(":exchange", exchange).replace(":tradingsymbol", tradingSymbol);
        return new TriggerRange().parseResponse(new KiteRequest().getRequest(url, authorize(params)));
    }

    /** Retrieves historical data for an instrument.
     * @param params contains from = "yyyy-mm-dd" and to = "yyyy-mm-dd".
     * @param interval can be minute, day, 3minute, 5minute, 10minute, 15minute, 30minute, 60minute.
     * @param token is instruments token.
     * @return HistoricalData object which contains list of historical data termed as dataArrayList.
     * */
    public HistoricalData getHistoricalData(Map<String, Object> params, String token, String interval) throws KiteException {
        String url = routes.get("market.historical").replace(":instrument_token", token).replace(":interval", interval);
        HistoricalData historicalData = new HistoricalData();
        historicalData.parseResponse(new KiteRequest().getRequest(url, authorize(params)));
        return historicalData;
    }

    /**
     * Kills the session by invalidating the access token.
     * @return JSONObject which contains status
     * @throws KiteException
     */
    public JSONObject logout() throws KiteException {
        Map<String, Object> params = new HashMap<String, Object>();
        String url = routes.get("logout");
        return new KiteRequest().deleteRequest(url, authorize(params));
    }

    /**This method parses csv and returns instrument list.
     * @param input is csv string.
     * @return  returns list of instruments.
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
        List<Instrument> instruments = new ArrayList<Instrument>();
        while((instrument = beanReader.read(Instrument.class, header, processors)) != null ) {
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
}
