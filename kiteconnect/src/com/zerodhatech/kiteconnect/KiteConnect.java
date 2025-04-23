package com.zerodhatech.kiteconnect;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;
import com.zerodhatech.kiteconnect.kitehttp.KiteRequestHandler;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import org.apache.commons.codec.digest.DigestUtils;
import org.json.JSONArray;
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
 * Offers all the functionality like placing order, fetch margins, orderbook, positions, fetch market snap quote.
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
    private KiteRequestHandler kiteRequestHandler;

    /** Initializes KiteSDK with the api key provided for your app.
     * @param apiKey is the api key provided after creating new Kite Connect app on developers console.
     */
    public KiteConnect(String apiKey){
        this(apiKey, null, false);
    }

    /** Initializes KiteSDK with the api key provided for your app.
     * @param apiKey is the api key provided after creating new Kite Connect app on developers console.
     * @param enableDebugLog is a boolean to enable debug logs
     */
    public KiteConnect(String apiKey, boolean enableDebugLog){
        this(apiKey, null, enableDebugLog);
    }

    /** Initializes KiteSDK with the api key provided for your app.
     * @param apiKey is the api key provided after creating new Kite Connect app on developers console.
     * @param userProxy is the user defined proxy. Can be used only if a user chose to use the proxy.
     */
    public KiteConnect(String apiKey, Proxy userProxy, boolean enableDebugLog) {
        this.proxy = userProxy;
        this.apiKey = apiKey;
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(Date.class, new JsonDeserializer<Date>() {

            @Override
            public Date deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
                try {
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    return format.parse(jsonElement.getAsString());
                } catch (ParseException e) {
                    return null;
                }
            }
        });
        gson = gsonBuilder.setDateFormat("yyyy-MM-dd HH:mm:ss").create();
        ENABLE_LOGGING = enableDebugLog;
        kiteRequestHandler = new KiteRequestHandler(proxy);
    }

    /** Registers callback for session error.
     * @param hook can be set to get callback when session is expired.
     * */
    public void setSessionExpiryHook(SessionExpiryHook hook){
        sessionExpiryHook = hook;
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

    /** Retrieves login url
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
     * @return User is the user model which contains user and session details.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
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

        return  new User().parseResponse(kiteRequestHandler.postRequest(routes.get("api.validate"), params, apiKey, accessToken));
    }

    /** Get a new access token using refresh token.
     * @param refreshToken is the refresh token obtained after generateSession.
     * @param apiSecret is unique for each app.
     * @return TokenSet contains user id, refresh token, api secret.
     * @throws IOException is thrown when there is connection error.
     * @throws KiteException is thrown for all Kite trade related errors. */
    public TokenSet renewAccessToken(String refreshToken, String apiSecret) throws IOException, KiteException, JSONException {
        String hashableText = this.apiKey + refreshToken + apiSecret;
        String sha256hex = sha256Hex(hashableText);

        Map<String, Object> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("refresh_token", refreshToken);
        params.put("checksum", sha256hex);

        JSONObject response = kiteRequestHandler.postRequest(routes.get("api.refresh"), params, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), TokenSet.class);
    }

    /** Hex encodes sha256 output for android support.
     * @return Hex encoded String.
     * @param str is the String that has to be encrypted.
     * */
    public String sha256Hex(String str) {
        byte[] a = DigestUtils.sha256(str);
        StringBuilder sb = new StringBuilder(a.length * 2);
        for(byte b: a)
            sb.append(String.format("%02x", b));
        return sb.toString();
    }

    /** Get the profile details of the use.
     * @return Profile is a POJO which contains profile related data.
     * @throws IOException is thrown when there is connection error.
     * @throws KiteException is thrown for all Kite trade related errors.*/
    public Profile getProfile() throws IOException, KiteException, JSONException {
        String url = routes.get("user.profile");
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), Profile.class);
    }

    /**
     * Gets account balance and cash margin details for a particular segment.
     * Example for segment can be equity or commodity.
     * @param segment can be equity or commodity.
     * @return Margins object.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public Margin getMargins(String segment) throws KiteException, JSONException, IOException {
        String url = routes.get("user.margins.segment").replace(":segment", segment);
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), Margin.class);
    }

    /**
     * Gets account balance and cash margin details for a equity and commodity.
     * @return Map of String and Margin is a map of commodity or equity string and funds data.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public Map<String, Margin> getMargins() throws KiteException, JSONException, IOException {
        String url = routes.get("user.margins");
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        Type type = new TypeToken<Map<String, Margin>>(){}.getType();
        return gson.fromJson(String.valueOf(response.get("data")), type);
    }

    /** Get margins required data before placing an order.
     * @return MarginCalculationData list contains the total, var, exposure, span and other components of the margin required.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     * */
    public List<MarginCalculationData> getMarginCalculation(List<MarginCalculationParams> params) throws IOException, KiteException, JSONException{
        String url = routes.get("margin.calculation.order");
        JSONArray jsonArray = new JSONArray();
        for(int k =0; k< params.size(); k++) {
            MarginCalculationParams param = params.get(k);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("tradingsymbol", param.tradingSymbol);
            jsonObject.put("exchange", param.exchange);
            jsonObject.put("transaction_type", param.transactionType);
            jsonObject.put("variety", param.variety);
            jsonObject.put("product", param.product);
            jsonObject.put("order_type", param.orderType);
            jsonObject.put("quantity", param.quantity);
            jsonObject.put("price", param.price);
            jsonObject.put("trigger_price", param.triggerPrice);
            jsonArray.put(jsonObject);
        }
        JSONObject response = kiteRequestHandler.postRequestJSON(url, jsonArray, new HashMap<String, Object>(), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MarginCalculationData[].class));
    }

    /** Get margins required data for multiple instruments before placing an order,
     * this can be used to check the margin required for taking hedged positions.
     * @return CombinedMarginData object, it contains the initial, final and margin calculation for each order.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     * */
    public CombinedMarginData getCombinedMarginCalculation(List<MarginCalculationParams> params, boolean considerPositions, boolean compactMode) throws IOException, KiteException, JSONException{
        String url = routes.get("margin.calculation.basket");
        Map<String, Object> queryParams = new HashMap<String, Object>();

        if(considerPositions) queryParams.put("consider_positions", true);
        if(compactMode) queryParams.put("mode", "compact");

        JSONArray jsonArray = new JSONArray();
        for(int k = 0; k < params.size(); k++){
            MarginCalculationParams param = params.get(k);
            JSONObject jsonObject = new JSONObject();

            jsonObject.put("tradingsymbol", param.tradingSymbol);
            jsonObject.put("exchange", param.exchange);
            jsonObject.put("transaction_type", param.transactionType);
            jsonObject.put("variety", param.variety);
            jsonObject.put("product", param.product);
            jsonObject.put("order_type", param.orderType);
            jsonObject.put("quantity", param.quantity);
            jsonObject.put("price", param.price);
            jsonObject.put("trigger_price", param.triggerPrice);
            jsonArray.put(jsonObject);
        }
        JSONObject response = kiteRequestHandler.postRequestJSON(url, jsonArray, queryParams, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), CombinedMarginData.class);
    }

    /** A virtual contract provides detailed charges order-wise for brokerage, STT, stamp duty, exchange transaction charges, SEBI turnover charge, and GST.
     * @return ContractNote list contains the data and charges for all the orders passed as input.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     * */
    public List<ContractNote> getVirtualContractNote(List<ContractNoteParams> params) throws IOException, KiteException, JSONException {
        String url = routes.get("contractnote");
        JSONArray jsonArray = new JSONArray();
        for(int k = 0; k < params.size(); k++){
            JSONObject jsonObject = new JSONObject();
            ContractNoteParams param = params.get(k);
            jsonObject.put("tradingsymbol", param.tradingSymbol);
            jsonObject.put("exchange", param.exchange);
            jsonObject.put("order_id", param.orderID);
            jsonObject.put("transaction_type", param.transactionType);
            jsonObject.put("product", param.product);
            jsonObject.put("variety", param.variety);
            jsonObject.put("order_type", param.orderType);
            jsonObject.put("quantity", param.quantity);
            jsonObject.put("average_price", param.averagePrice);
            jsonArray.put(jsonObject);
        }
        JSONObject response = kiteRequestHandler.postRequestJSON(url, jsonArray, new HashMap<String, Object>(), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), ContractNote[].class));
    }

    /**
     * Places an order.
     * @param orderParams is Order params.
     * @param variety variety="regular". Order variety can be bo, co, amo, regular.
     * @return Order contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public Order placeOrder(OrderParams orderParams, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.place").replace(":variety", variety);

        Map<String, Object> params = new HashMap<>();

        if(orderParams.exchange != null) params.put("exchange", orderParams.exchange);
        if(orderParams.tradingsymbol != null) params.put("tradingsymbol", orderParams.tradingsymbol);
        if(orderParams.transactionType != null) params.put("transaction_type", orderParams.transactionType);
        if(orderParams.quantity != null) params.put("quantity", orderParams.quantity);
        if(orderParams.price != null) params.put("price", orderParams.price);
        if(orderParams.product != null) params.put("product", orderParams.product);
        if(orderParams.orderType != null) params.put("order_type", orderParams.orderType);
        if(orderParams.validity != null) params.put("validity", orderParams.validity);
        if(orderParams.disclosedQuantity != null) params.put("disclosed_quantity", orderParams.disclosedQuantity);
        if(orderParams.triggerPrice != null) params.put("trigger_price", orderParams.triggerPrice);
        if(orderParams.squareoff != null) params.put("squareoff", orderParams.squareoff);
        if(orderParams.stoploss != null) params.put("stoploss", orderParams.stoploss);
        if(orderParams.trailingStoploss != null) params.put("trailing_stoploss", orderParams.trailingStoploss);
        if(orderParams.tag != null) params.put("tag", orderParams.tag);
        if(orderParams.validity != null && orderParams.validity.equals(Constants.VALIDITY_TTL))
            params.put("validity_ttl",orderParams.validityTTL);
        if(variety.equals(Constants.VARIETY_ICEBERG)){
         params.put("iceberg_legs", orderParams.icebergLegs);
         params.put("iceberg_quantity", orderParams.icebergQuantity);
        }
        if(variety.equals(Constants.VARIETY_AUCTION)){
            params.put("auction_number", orderParams.auctionNumber);
        }
        params.put("market_protection", orderParams.marketProtection);

        JSONObject jsonObject = kiteRequestHandler.postRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**
     * Place auto slice order.
     * @param orderParams is Order params.
     * @param variety variety="regular". Order variety can be bo, co, amo, regular.
     * @return Order contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     * If the parent order placement fails then users will see error message in
     * the KiteException thrown from global response handler. But if subsequent
     * order placement fails then error message is inside the AutoSliceOrderResponse model
     * sample response is
     * {
     *     "status": "success",
     *     "data": [
     *         {
     *             "order_id": "1914227164488687616"
     *         },
     *         {
     *             "error": {
     *                 "code": 400,
     *                 "error_type": "MarginException",
     *                 "message": "Insufficient funds. Required margin is 228365.92 but available margin is 228358.50.",
     *                 "data": null
     *             }
     *         },
     *         {
     *             "order_id": "1914227164681625600"
     *         }
     *     ]
     * }
     * */
    public List<BulkOrderResponse> placeAutoSliceOrder(OrderParams orderParams, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.place").replace(":variety", variety);
        Map<String, Object> params = new HashMap<>();

        if(orderParams.exchange != null) params.put("exchange", orderParams.exchange);
        if(orderParams.tradingsymbol != null) params.put("tradingsymbol", orderParams.tradingsymbol);
        if(orderParams.transactionType != null) params.put("transaction_type", orderParams.transactionType);
        if(orderParams.quantity != null) params.put("quantity", orderParams.quantity);
        if(orderParams.price != null) params.put("price", orderParams.price);
        if(orderParams.product != null) params.put("product", orderParams.product);
        if(orderParams.orderType != null) params.put("order_type", orderParams.orderType);
        if(orderParams.validity != null) params.put("validity", orderParams.validity);
        if(orderParams.disclosedQuantity != null) params.put("disclosed_quantity", orderParams.disclosedQuantity);
        if(orderParams.triggerPrice != null) params.put("trigger_price", orderParams.triggerPrice);
        if(orderParams.squareoff != null) params.put("squareoff", orderParams.squareoff);
        if(orderParams.stoploss != null) params.put("stoploss", orderParams.stoploss);
        if(orderParams.trailingStoploss != null) params.put("trailing_stoploss", orderParams.trailingStoploss);
        if(orderParams.tag != null) params.put("tag", orderParams.tag);
        if(orderParams.validity != null && orderParams.validity.equals(Constants.VALIDITY_TTL))
            params.put("validity_ttl",orderParams.validityTTL);
        if(variety.equals(Constants.VARIETY_ICEBERG)){
            params.put("iceberg_legs", orderParams.icebergLegs);
            params.put("iceberg_quantity", orderParams.icebergQuantity);
        }
        if(variety.equals(Constants.VARIETY_AUCTION)){
            params.put("auction_number", orderParams.auctionNumber);
        }
        params.put("autoslice", true);
        params.put("market_protection", orderParams.marketProtection);

        JSONObject response = kiteRequestHandler.postRequest(url, params, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), BulkOrderResponse[].class));
    }

    /**
     * Modifies an open order.
     *
     * @param orderParams is Order params.
     * @param variety variety="regular". Order variety can be bo, co, amo, regular.
     * @param orderId order id of the order being modified.
     * @return Order object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public Order modifyOrder(String orderId, OrderParams orderParams, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.modify").replace(":variety", variety).replace(":order_id", orderId);

        Map<String, Object> params = new HashMap<>();

        if(orderParams.exchange != null) params.put("exchange", orderParams.exchange);
        if(orderParams.tradingsymbol != null) params.put("tradingsymbol", orderParams.tradingsymbol);
        if(orderParams.transactionType != null) params.put("transaction_type", orderParams.transactionType);
        if(orderParams.quantity != null) params.put("quantity", orderParams.quantity);
        if(orderParams.price != null) params.put("price", orderParams.price);
        if(orderParams.product != null) params.put("product", orderParams.product);
        if(orderParams.orderType != null) params.put("order_type", orderParams.orderType);
        if(orderParams.validity != null) params.put("validity", orderParams.validity);
        if(orderParams.disclosedQuantity != null) params.put("disclosed_quantity", orderParams.disclosedQuantity);
        if(orderParams.triggerPrice != null) params.put("trigger_price", orderParams.triggerPrice);
        if(orderParams.squareoff != null) params.put("squareoff", orderParams.squareoff);
        if(orderParams.stoploss != null) params.put("stoploss", orderParams.stoploss);
        if(orderParams.trailingStoploss != null) params.put("trailing_stoploss", orderParams.trailingStoploss);
        if(orderParams.parentOrderId != null) params.put("parent_order_id", orderParams.parentOrderId);

        JSONObject jsonObject = kiteRequestHandler.putRequest(url, params, apiKey, accessToken);
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
     * @throws IOException is thrown when there is connection error.
     */
    public Order cancelOrder(String orderId, String variety) throws KiteException, JSONException, IOException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);
        Map<String, Object> params = new HashMap<String, Object>();

        JSONObject jsonObject = kiteRequestHandler.deleteRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /**
     * Cancel/exit special orders like BO, CO
     * @param parentOrderId order id of first leg.
     * @param orderId order id of the order to be cancelled.
     * @param variety [variety="regular"]. Order variety can be bo, co, amo, regular.
     * @return Order object contains only orderId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection error.
     * */
    public Order cancelOrder(String orderId, String parentOrderId, String variety) throws KiteException, IOException, JSONException {
        String url = routes.get("orders.cancel").replace(":variety", variety).replace(":order_id", orderId);

        Map<String, Object> params = new HashMap<>();
        params.put("parent_order_id", parentOrderId);

        JSONObject jsonObject = kiteRequestHandler.deleteRequest(url, params, apiKey, accessToken);
        Order order =  new Order();
        order.orderId = jsonObject.getJSONObject("data").getString("order_id");
        return order;
    }

    /** Fetches collection of orders from the orderbook.
     * @return List of orders.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     * */
    public List<Order> getOrders() throws KiteException, JSONException, IOException {
        String url = routes.get("orders");
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }

    /** Fetches collection of auction instruments available for bidding.
     * @return List of Auction Instruments.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     * */
    public List<AuctionInstrument> getAuctionInstruments() throws KiteException, JSONException, IOException{
        String url = routes.get("portfolio.auctions.instruments");
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), AuctionInstrument[].class));
    }

    /** Fetches list of gtt existing in an account.
    * @return List of GTTs.
    * @throws KiteException is thrown for all Kite trade related errors.
    * @throws IOException is thrown when there is connection error.
    * */
    public List<GTT> getGTTs() throws KiteException, IOException, JSONException {
        String url = routes.get("gtt");
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), GTT[].class));
    }

    /** Fetch details of a GTT.
     * @param gttId is the id of the GTT that needs to be fetched.
     * @return GTT object which contains all the details.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection error.
     * @throws JSONException is thrown when there is exception while parsing response.
     * */
    public GTT getGTT(int gttId) throws IOException, KiteException, JSONException {
        String url = routes.get("gtt.info").replace(":id", gttId+"");
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return gson.fromJson(String.valueOf(response.get("data")), GTT.class);
    }

    /** Place a GTT.
     * @param gttParams is GTT param which container condition, type, order details. It can contain one or two orders.
     * @throws IOException  is thrown when there is connection error.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @return GTT object contains only gttId.*/
    public GTT placeGTT(GTTParams gttParams) throws IOException, KiteException, JSONException {
        String url = routes.get("gtt.place");
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> conditionParam = new HashMap<>();
        JSONArray ordersParam = new JSONArray();

        conditionParam.put("exchange", gttParams.exchange);
        conditionParam.put("tradingsymbol", gttParams.tradingsymbol);
        conditionParam.put("trigger_values", gttParams.triggerPrices.toArray());
        conditionParam.put("last_price", gttParams.lastPrice);
        conditionParam.put("instrument_token", gttParams.instrumentToken);

        for(GTTParams.GTTOrderParams order : gttParams.orders) {
            JSONObject gttOrderItem = new JSONObject();
            gttOrderItem.put("exchange", gttParams.exchange);
            gttOrderItem.put("tradingsymbol", gttParams.tradingsymbol);
            gttOrderItem.put("transaction_type", order.transactionType);
            gttOrderItem.put("quantity", order.quantity);
            gttOrderItem.put("price", order.price);
            gttOrderItem.put("order_type", order.orderType);
            gttOrderItem.put("product", order.product);
            ordersParam.put(gttOrderItem);
        }

        params.put("condition", new JSONObject(conditionParam).toString());
        params.put("orders", ordersParam.toString());
        params.put("type", gttParams.triggerType);

        JSONObject response = kiteRequestHandler.postRequest(url, params, apiKey, accessToken);
        GTT gtt = new GTT();
        gtt.id = response.getJSONObject("data").getInt("trigger_id");
        return gtt;
    }

    /** Modify a GTT.
     * @param gttParams is GTT param which container condition, type, order details. It can contain one or two orders.
     * @param gttId is the id of the GTT to be modified.
     * @throws IOException  is thrown when there is connection error.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @return GTT object contains only gttId.*/
    public GTT modifyGTT(int gttId, GTTParams gttParams) throws IOException, KiteException, JSONException {
        String url = routes.get("gtt.modify").replace(":id", gttId+"");
        Map<String, Object> params = new HashMap<>();
        Map<String, Object> conditionParam = new HashMap<>();
        JSONArray ordersParam = new JSONArray();

        conditionParam.put("exchange", gttParams.exchange);
        conditionParam.put("tradingsymbol", gttParams.tradingsymbol);
        conditionParam.put("trigger_values", gttParams.triggerPrices.toArray());
        conditionParam.put("last_price", gttParams.lastPrice);
        conditionParam.put("instrument_token", gttParams.instrumentToken);

        for(GTTParams.GTTOrderParams order : gttParams.orders) {
            JSONObject gttOrderItem = new JSONObject();
            gttOrderItem.put("exchange", gttParams.exchange);
            gttOrderItem.put("tradingsymbol", gttParams.tradingsymbol);
            gttOrderItem.put("transaction_type", order.transactionType);
            gttOrderItem.put("quantity", order.quantity);
            gttOrderItem.put("price", order.price);
            gttOrderItem.put("order_type", order.orderType);
            gttOrderItem.put("product", order.product);
            ordersParam.put(gttOrderItem);
        }

        params.put("condition", new JSONObject(conditionParam).toString());
        params.put("orders", ordersParam.toString());
        params.put("type", gttParams.triggerType);

        JSONObject response = kiteRequestHandler.putRequest(url, params, apiKey, accessToken);
        GTT gtt = new GTT();
        gtt.id = response.getJSONObject("data").getInt("trigger_id");
        return  gtt;
    }

    /**
     * Cancel GTT.
     * @param gttId order id of first leg.
     * @return GTT object contains only gttId.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection error.
     * @throws JSONException is thrown when there is exception while parsing response.
     * */
    public GTT cancelGTT(int gttId) throws IOException, KiteException, JSONException {
        String url = routes.get("gtt.delete").replace(":id", gttId+"");
        JSONObject response  = kiteRequestHandler.deleteRequest(url, new HashMap<String, Object>(), apiKey, accessToken);
        GTT gtt = new GTT();
        gtt.id = response.getJSONObject("data").getInt("trigger_id");
        return gtt;
    }

    /** Returns list of different stages an order has gone through.
     * @return List of multiple stages an order has gone through in the system.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @param orderId is the order id which is obtained from orderbook.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection error.
     * */
    public List<Order> getOrderHistory(String orderId) throws KiteException, IOException, JSONException {
        String url = routes.get("order").replace(":order_id", orderId);
        JSONObject response = kiteRequestHandler.getRequest(url, apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Order[].class));
    }

    /**
     * Retrieves list of trades executed.
     * @return List of trades.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public List<Trade> getTrades() throws KiteException, JSONException, IOException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("trades"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Trade[].class));
    }

    /**
     * Retrieves list of trades executed of an order.
     * @param orderId order if of the order whose trades are fetched.
     * @return List of trades for the given order.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public List<Trade> getOrderTrades(String orderId) throws KiteException, JSONException, IOException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("orders.trades").replace(":order_id", orderId), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Trade[].class));
    }

    /**
     * Retrieves the list of holdings.
     * @return List of holdings.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public List<Holding> getHoldings() throws KiteException, JSONException, IOException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("portfolio.holdings"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), Holding[].class));
    }

    /**
     * Retrieves the list of positions.
     * @return List of positions.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public Map<String, List<Position>> getPositions() throws KiteException, JSONException, IOException {
        Map<String, List<Position>> positionsMap = new HashMap<>();
        JSONObject response = kiteRequestHandler.getRequest(routes.get("portfolio.positions"), apiKey, accessToken);
        JSONObject allPositions = response.getJSONObject("data");
        positionsMap.put("net", Arrays.asList(gson.fromJson(String.valueOf(allPositions.get("net")), Position[].class)));
        positionsMap.put("day", Arrays.asList(gson.fromJson(String.valueOf(allPositions.get("day")), Position[].class)));
        return positionsMap;
    }


    /**
     * Modifies an open position's product type. Only an MIS, CNC, and NRML positions can be converted.
     * @param tradingSymbol Tradingsymbol of the instrument  (ex. RELIANCE, INFY).
     * @param exchange Exchange in which instrument is listed (NSE, BSE, NFO, BFO, CDS, MCX).
     * @param transactionType Transaction type (BUY or SELL).
     * @param positionType day or overnight position
     * @param oldProduct Product code (NRML, MIS, CNC).
     * @param newProduct Product code (NRML, MIS, CNC).
     * @param quantity Order quantity
     * @return JSONObject  which will have status.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection error.
     */
    public JSONObject convertPosition(String tradingSymbol, String exchange, String transactionType, String positionType, String oldProduct, String newProduct, int quantity) throws KiteException, JSONException, IOException {
        Map<String, Object> params = new HashMap<>();
        params.put("tradingsymbol", tradingSymbol);
        params.put("exchange", exchange);
        params.put("transaction_type", transactionType);
        params.put("position_type", positionType);
        params.put("old_product", oldProduct);
        params.put("new_product", newProduct);
        params.put("quantity", quantity);

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
     * @return List of instruments which are available to trade.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related errors.
     */
    public List<Instrument> getInstruments() throws KiteException, IOException, JSONException {
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
     * @throws IOException is thrown when there is connection related error.
     */
    public List<Instrument> getInstruments(String exchange) throws KiteException, JSONException, IOException {
        return readCSV(kiteRequestHandler.getCSVRequest(routes.get("market.instruments").replace(":exchange", exchange), apiKey, accessToken));
    }

    /**
     * Retrieves quote and market depth for an instrument
     *
     * @param instruments is the array of tradingsymbol and exchange or instrument token. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     *
     * @return Map of String and Quote.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection related error.
     */
    public Map<String, Quote> getQuote(String [] instruments) throws KiteException, JSONException, IOException {
        JSONObject jsonObject = kiteRequestHandler.getRequest(routes.get("market.quote"), "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, Quote>>(){}.getType();
        return gson.fromJson(String.valueOf(jsonObject.get("data")), type);
    }

    /** Retrieves OHLC and last price.
     * User can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}
     * @return Map of String and OHLCQuote.
     * @param instruments is the array of tradingsymbol and exchange or instruments token.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public Map<String, OHLCQuote> getOHLC(String [] instruments) throws KiteException, IOException, JSONException {
        JSONObject resp = kiteRequestHandler.getRequest(routes.get("quote.ohlc"), "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, OHLCQuote>>(){}.getType();
        return gson.fromJson(String.valueOf(resp.get("data")), type);
    }

    /** Retrieves last price.
     * User can either pass exchange with tradingsymbol or instrument token only. For example {NSE:NIFTY 50, BSE:SENSEX} or {256265, 265}.
     * @return Map of String and LTPQuote.
     * @param instruments is the array of tradingsymbol and exchange or instruments token.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public Map<String, LTPQuote> getLTP(String[] instruments) throws KiteException, IOException, JSONException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("quote.ltp"), "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, LTPQuote>>(){}.getType();
        return gson.fromJson(String.valueOf(response.get("data")), type);
    }

    /**
     * Retrieves buy or sell trigger range for Cover Orders.
     * @return TriggerRange object is returned.
     * @param instruments is the array of tradingsymbol and exchange or instrument token.
     * @param transactionType "BUY or "SELL".
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws JSONException is thrown when there is exception while parsing response.
     * @throws IOException is thrown when there is connection related error.
     */
    public Map<String, TriggerRange> getTriggerRange(String[] instruments, String transactionType) throws KiteException, JSONException, IOException {
        String url = routes.get("market.trigger_range").replace(":transaction_type", transactionType.toLowerCase());
        JSONObject response = kiteRequestHandler.getRequest(url, "i", instruments, apiKey, accessToken);
        Type type = new TypeToken<Map<String, TriggerRange>>(){}.getType();
        return gson.fromJson(String.valueOf(response.get("data")), type);
    }

    /** Retrieves historical data for an instrument.
     * @param from "yyyy-mm-dd" for fetching candles between days and "yyyy-mm-dd hh:mm:ss" for fetching candles between timestamps.
     * @param to "yyyy-mm-dd" for fetching candles between days and "yyyy-mm-dd hh:mm:ss" for fetching candles between timestamps.
     * @param continuous set to true for fetching continuous data of expired instruments.
     * @param interval can be minute, day, 3minute, 5minute, 10minute, 15minute, 30minute, 60minute.
     * @param token is instruments token.
     * @param oi set to true for fetching open interest data. The default value is 0.
     * @return HistoricalData object which contains list of historical data termed as dataArrayList.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public HistoricalData getHistoricalData(Date from, Date to, String token, String interval, boolean continuous, boolean oi) throws KiteException, IOException, JSONException {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Map<String, Object> params = new HashMap<>();
        params.put("from", format.format(from));
        params.put("to", format.format(to));
        params.put("continuous", continuous ? 1 : 0);
        params.put("oi", oi ? 1 : 0);

        String url = routes.get("market.historical").replace(":instrument_token", token).replace(":interval", interval);
        HistoricalData historicalData = new HistoricalData();
        historicalData.parseResponse(kiteRequestHandler.getRequest(url, params, apiKey, accessToken));
        return historicalData;
    }

    /** Retrieves mutualfunds instruments.
     * @return returns list of mutual funds instruments.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related errors.
     * */
    public List<MFInstrument> getMFInstruments() throws KiteException, IOException, JSONException {
        return readMfCSV(kiteRequestHandler.getCSVRequest(routes.get("mutualfunds.instruments"), apiKey, accessToken));
    }

    /** Place a mutualfunds order.
     * @return MFOrder object contains only orderId.
     * @param tradingsymbol Tradingsymbol (ISIN) of the fund.
     * @param transactionType BUY or SELL.
     * @param amount Amount worth of units to purchase. Not applicable on SELLs.
     * @param quantity Quantity to SELL. Not applicable on BUYs. If the holding is less than minimum_redemption_quantity, all the units have to be sold.
     * @param tag An optional tag to apply to an order to identify it (alphanumeric, max 8 chars).
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public MFOrder placeMFOrder(String tradingsymbol, String transactionType, double amount, double quantity, String tag) throws KiteException, IOException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tradingsymbol", tradingsymbol);
        params.put("transaction_type", transactionType);
        params.put("amount", amount);
        if(transactionType.equals(Constants.TRANSACTION_TYPE_SELL)) params.put("quantity", quantity);
        params.put("tag", tag);

        JSONObject response = kiteRequestHandler.postRequest(routes.get("mutualfunds.orders.place"), params, apiKey, accessToken);
        MFOrder MFOrder = new MFOrder();
        MFOrder.orderId = response.getJSONObject("data").getString("order_id");
        return MFOrder;
    }

    /** If cancel is successful then api will respond as 200 and send back true else it will be sent back to user as KiteException.
     * @return true if api call is successful.
     * @param orderId is the order id of the mutualfunds order.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there connection related error.
     * */
    public boolean cancelMFOrder(String orderId) throws KiteException, IOException, JSONException {
        kiteRequestHandler.deleteRequest(routes.get("mutualfunds.cancel_order").replace(":order_id", orderId), new HashMap<String, Object>(), apiKey, accessToken);
        return true;
    }

    /** Retrieves all mutualfunds orders.
     * @return List of all the mutualfunds orders.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public List<MFOrder> getMFOrders() throws KiteException, IOException, JSONException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("mutualfunds.orders"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MFOrder[].class));
    }

    /** Retrieves individual mutualfunds order.
     * @param orderId is the order id of a mutualfunds scrip.
     * @return returns a single mutualfunds object with all the parameters.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public MFOrder getMFOrder(String orderId) throws KiteException, IOException, JSONException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("mutualfunds.order").replace(":order_id", orderId), apiKey, accessToken);
        return gson.fromJson(response.get("data").toString(), MFOrder.class);
    }

    /** Place a mutualfunds sip.
     * @param tradingsymbol Tradingsymbol (ISIN) of the fund.
     * @param frequency weekly, monthly, or quarterly.
     * @param amount Amount worth of units to purchase. It should be equal to or greated than minimum_additional_purchase_amount and in multiple of purchase_amount_multiplier in the instrument master.
     * @param installmentDay If Frequency is monthly, the day of the month (1, 5, 10, 15, 20, 25) to trigger the order on.
     * @param instalments Number of instalments to trigger. If set to -1, instalments are triggered at fixed intervals until the SIP is cancelled.
     * @param initialAmount Amount worth of units to purchase before the SIP starts. Should be equal to or greater than minimum_purchase_amount and in multiple of purchase_amount_multiplier. This is only considered if there have been no prior investments in the target fund.
     * @return MFSIP object which contains sip id and order id.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public MFSIP placeMFSIP(String tradingsymbol, String frequency, int installmentDay, int instalments, int initialAmount, double amount) throws KiteException, IOException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("tradingsymbol", tradingsymbol);
        params.put("frequency", frequency);
        params.put("instalment_day", installmentDay);
        params.put("instalments", instalments);
        params.put("initial_amount", initialAmount);
        params.put("amount", amount);

        MFSIP MFSIP = new MFSIP();
        JSONObject response = kiteRequestHandler.postRequest(routes.get("mutualfunds.sips.place"),params, apiKey, accessToken);
        MFSIP.orderId = response.getJSONObject("data").getString("order_id");
        MFSIP.sipId = response.getJSONObject("data").getString("sip_id");
        return MFSIP;
    }

    /** Modify a mutualfunds sip.
     * @param frequency weekly, monthly, or quarterly.
     * @param status Pause or unpause an SIP (active or paused).
     * @param amount Amount worth of units to purchase. It should be equal to or greated than minimum_additional_purchase_amount and in multiple of purchase_amount_multiplier in the instrument master.
     * @param day If Frequency is monthly, the day of the month (1, 5, 10, 15, 20, 25) to trigger the order on.
     * @param instalments Number of instalments to trigger. If set to -1, instalments are triggered at fixed intervals until the SIP is cancelled.
     * @param sipId is the id of the sip.
     * @return returns true, if modify sip is successful else exception is thrown.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public boolean modifyMFSIP(String frequency, int day, int instalments, double amount, String status, String sipId) throws KiteException, IOException, JSONException {
        Map<String, Object> params = new HashMap<String, Object>();
        params.put("frequency", frequency);
        params.put("day", day);
        params.put("instalments", instalments);
        params.put("amount", amount);
        params.put("status", status);

        kiteRequestHandler.putRequest(routes.get("mutualfunds.sips.modify").replace(":sip_id", sipId), params, apiKey, accessToken);
        return true;
    }

    /** Cancel a mutualfunds sip.
     * @param sipId is the id of mutualfunds sip.
     * @return returns true, if cancel sip is successful else exception is thrown.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public boolean cancelMFSIP(String sipId) throws KiteException, IOException, JSONException {
        kiteRequestHandler.deleteRequest(routes.get("mutualfunds.sip").replace(":sip_id", sipId), new HashMap<String, Object>(), apiKey, accessToken);
        return true;
    }

    /** Retrieve all mutualfunds sip.
     * @return List of sips.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public List<MFSIP> getMFSIPs() throws KiteException, IOException, JSONException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("mutualfunds.sips"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MFSIP[].class));
    }

    /** Retrieve an individual sip.
     * @param sipId is the id of a particular sip.
     * @return MFSIP object which contains all the details of the sip.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public MFSIP getMFSIP(String sipId) throws KiteException, IOException, JSONException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("mutualfunds.sip").replace(":sip_id", sipId), apiKey, accessToken);
        return gson.fromJson(response.get("data").toString(), MFSIP.class);
    }

    /** Retrieve all the mutualfunds holdings.
     * @return List of mutualfunds holdings.
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     * */
    public List<MFHolding> getMFHoldings() throws KiteException, IOException, JSONException {
        JSONObject response = kiteRequestHandler.getRequest(routes.get("mutualfunds.holdings"), apiKey, accessToken);
        return Arrays.asList(gson.fromJson(String.valueOf(response.get("data")), MFHolding[].class));
    }
    /**
     * Logs out user by invalidating the access token.
     * @return JSONObject which contains status
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     */
    public JSONObject logout() throws KiteException, IOException, JSONException {
        return invalidateAccessToken();
    }

    /**
     * Kills the session by invalidating the access token.
     * @return JSONObject which contains status
     * @throws KiteException is thrown for all Kite trade related errors.
     * @throws IOException is thrown when there is connection related error.
     */
    public JSONObject invalidateAccessToken() throws IOException, KiteException, JSONException {
        String url = routes.get("api.token");
        Map<String, Object> params = new HashMap<>();
        params.put("api_key", apiKey);
        params.put("access_token", accessToken);
        return kiteRequestHandler.deleteRequest(url, params, apiKey, accessToken);
    }

    /**
     * Kills the refresh token.
     * @return JSONObject contains status.
     * @param refreshToken is the token received after successful log in.
     * @throws IOException is thrown for connection related errors.
     * @throws KiteException is thrown for Kite trade related errors.
     * */
    public JSONObject invalidateRefreshToken(String refreshToken) throws IOException, KiteException, JSONException {
        Map<String, Object> param = new HashMap<>();
        param.put("refresh_token", refreshToken);
        param.put("api_key", apiKey);
        String url = routes.get("api.token");
        return kiteRequestHandler.deleteRequest(url, param, apiKey, accessToken);
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
    private List<MFInstrument> readMfCSV(String input) throws IOException{
        ICsvBeanReader beanReader = null;
        File temp = File.createTempFile("tempfile", ".tmp");
        BufferedWriter bw = new BufferedWriter(new FileWriter(temp));
        bw.write(input);
        bw.close();

        beanReader = new CsvBeanReader(new FileReader(temp), CsvPreference.STANDARD_PREFERENCE);
        String[] header = beanReader.getHeader(true);
        CellProcessor[] processors = getMfProcessors();
        MFInstrument instrument;
        List<MFInstrument> instruments = new ArrayList<>();
        while((instrument = beanReader.read(MFInstrument.class, header, processors)) != null ) {
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
                new org.supercsv.cellprocessor.Optional(new ParseDate("yyyy-MM-dd")),                 //expiry
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
                new org.supercsv.cellprocessor.Optional(new ParseBool()),    //purchase_allowed
                new org.supercsv.cellprocessor.Optional(new ParseBool()),    //redemption_allowed
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
                new org.supercsv.cellprocessor.Optional(new ParseDate("yyyy-MM-dd"))                   //last_price_date
        };
        return processors;
    }

}