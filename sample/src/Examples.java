import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.kiteconnect.utils.Constants;
import com.zerodhatech.models.*;
import com.zerodhatech.ticker.*;
import org.json.JSONObject;
import com.zerodhatech.models.Margin;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Sample usage snippets for the Kite Connect Java client.
 *
 * <p>Each method demonstrates a single API workflow such as placing orders,
 * fetching portfolio data, working with GTTs, or consuming ticker updates.</p>
 */
public class Examples {

    /**
     * Fetches the logged-in user's profile details.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the profile request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void getProfile(KiteConnect kiteConnect) throws IOException, KiteException {
        Profile profile = kiteConnect.getProfile();
        System.out.println(profile.userName);
    }

    /**
     * Fetches available and utilised margin details for the account.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the margin request fails due to an I/O error.
     */
    public void getMargins(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get margins returns margin model, you can pass equity or commodity as arguments to get margins of respective segments.
        //Margins margins = kiteConnect.getMargins("equity");
        Margin margins = kiteConnect.getMargins("equity");
        System.out.println(margins.available.cash);
        System.out.println(margins.utilised.debits);
        System.out.println(margins.utilised.m2mUnrealised);
    }

    /**
     * Calculates margin for a single order payload.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the margin calculation request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void getMarginCalculation(KiteConnect kiteConnect) throws IOException, KiteException {
        MarginCalculationParams param = new MarginCalculationParams();
        param.exchange = "NSE";
        param.tradingSymbol = "INFY";
        param.orderType = "MARKET";
        param.quantity = 1;
        param.product = "MIS";
        param.variety = "regular";
        param.transactionType = Constants.TRANSACTION_TYPE_BUY;
        List<MarginCalculationParams> params = new ArrayList<>();
        params.add(param);
        List<MarginCalculationData> data = kiteConnect.getMarginCalculation(params);
        System.out.println(data.get(0).total);
        System.out.println(data.get(0).leverage);
    }

    /**
     * Calculates combined margin for multiple order legs.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the margin calculation request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void getCombinedMarginCalculation(KiteConnect kiteConnect) throws IOException, KiteException{
        List<MarginCalculationParams> params = new ArrayList<>();

        MarginCalculationParams param = new MarginCalculationParams();
        param.exchange = "NFO";
        param.tradingSymbol = "NIFTY21MARFUT";
        param.orderType = "LIMIT";
        param.quantity = 75;
        param.product = "MIS";
        param.variety = "regular";
        param.transactionType = "BUY";
        param.price = 141819;

        MarginCalculationParams param2 = new MarginCalculationParams();
        param2.exchange = "NFO";
        param2.tradingSymbol = "NIFTY21MAR15000PE";
        param2.orderType = "LIMIT";
        param2.quantity = 75;
        param2.product = "MIS";
        param2.variety = "regular";
        param.transactionType = "BUY";
        param2.price = 300;

        params.add(param);
        params.add(param2);

        CombinedMarginData combinedMarginData = kiteConnect.getCombinedMarginCalculation(params, true, false);
        System.out.println(combinedMarginData.initialMargin.total);
    }

    /**
     * Generates a virtual contract note for the supplied executed order details.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getVirtualContractNote(KiteConnect kiteConnect) throws  KiteException, IOException {
        List<ContractNoteParams> virtualContractNoteParams = new ArrayList<ContractNoteParams>();
        ContractNoteParams contractNoteParams = new ContractNoteParams();
        contractNoteParams.orderID = "230727202226518";
        contractNoteParams.tradingSymbol = "ITC";
        contractNoteParams.exchange = Constants.EXCHANGE_NSE;
        contractNoteParams.product = Constants.PRODUCT_CNC;
        contractNoteParams.orderType = Constants.ORDER_TYPE_MARKET;
        contractNoteParams.variety = Constants.VARIETY_REGULAR;
        contractNoteParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        contractNoteParams.quantity = 1;
        contractNoteParams.averagePrice = 470.05;
        virtualContractNoteParams.add(contractNoteParams);

        List<ContractNote> data = kiteConnect.getVirtualContractNote(virtualContractNoteParams);
        System.out.println(data.size());

        System.out.println(data.get(0).charges.total);
    }

    /**
     * Demonstrates how to place a regular buy limit order.
     *
     * <p>This example builds an {@link OrderParams} payload with the minimum fields
     * typically required for a CNC limit order and sends it using
     * {@link Constants#VARIETY_REGULAR}. A successful response confirms only that
     * the order was accepted by the OMS and returns the generated order ID.</p>
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API rejects the order or returns an error response.
     * @throws IOException if the order request fails due to an I/O error.
     */
    public void placeOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = "ASHOKLEY";
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 122.2;
        orderParams.triggerPrice = 0.0;
        orderParams.tag = "myTag"; //tag is optional and it cannot be more than 8 characters and only alphanumeric is allowed

        OrderResponse orderResponse = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        System.out.println(orderResponse.orderId);
    }

    /**
     * Places an iceberg order using TTL validity.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the order request fails due to an I/O error.
     */
    public void placeIcebergOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Iceberg order:- following is example param for iceberg order with ttl validity.
         * Minimum number of legs is 2 and maximum number of legs is 10.
         * TTL validity is always sent as integer wherein number denotes number of minutes an order can be alive.
         */
        OrderParams orderParams = new OrderParams();
        orderParams.quantity = 10;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.price = 1440.0;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.tradingsymbol = "INFY";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_TTL;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.validityTTL = 10;
        orderParams.icebergLegs = 2;
        orderParams.icebergQuantity = 5;
        OrderResponse orderResponse = kiteConnect.placeOrder(orderParams, Constants.VARIETY_ICEBERG);
        System.out.println(orderResponse.orderId);
    }

    /**
     * Places a cover order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the order request fails due to an I/O error.
     */
    public void placeCoverOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Cover Order:- following is an example param for the cover order
         * key: quantity value: 1
         * key: price value: 0
         * key: transaction_type value: BUY
         * key: tradingsymbol value: HINDALCO
         * key: exchange value: NSE
         * key: validity value: DAY
         * key: trigger_price value: 157
         * key: order_type value: MARKET
         * key: variety value: co
         * key: product value: MIS
         */
        OrderParams orderParams = new OrderParams();
        orderParams.price = 0.0;
        orderParams.quantity = 1;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        orderParams.tradingsymbol = "SOUTHBANK";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.triggerPrice = 30.5;
        orderParams.product = Constants.PRODUCT_MIS;

        OrderResponse orderResponse = kiteConnect.placeOrder(orderParams, Constants.VARIETY_CO);
        System.out.println(orderResponse.orderId);
    }

    /**
     * Places a regular order with automatic slicing enabled.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the order request fails due to an I/O error.
     */
    public void placeOrderWithAutoSlice(KiteConnect kiteConnect) throws KiteException, IOException {
        OrderParams orderParams = new OrderParams();
        orderParams.price = 146.55;
        orderParams.quantity = 5925;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = "NIFTY2632423000PE";
        orderParams.exchange = Constants.EXCHANGE_NFO;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.product = Constants.PRODUCT_MIS;
        orderParams.autoslice = true;

        OrderResponse orderResponse = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        List<BulkOrderResponse> orders = new ArrayList<>();
        if (orderResponse.orderId != null) {
            BulkOrderResponse parentOrder = new BulkOrderResponse();
            parentOrder.orderId = orderResponse.orderId;
            orders.add(parentOrder);
        }
        if (orderResponse.children != null) {
            orders.addAll(orderResponse.children);
        }
        for (BulkOrderResponse order : orders) {
            if (order.orderId!=null) {
                System.out.println(order.orderId);
            } else {
                System.out.println(order.bulkOrderError.code);
                System.out.println(order.bulkOrderError.message);
            }
        }
    }

    /**
     * Places a market order with market protection enabled.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the order request fails due to an I/O error.
     */
    public  void placeMarketProtectionOrder(KiteConnect kiteConnect) throws KiteException, IOException{
        OrderParams orderParams = new OrderParams();
        orderParams.price = 0.0;
        orderParams.quantity = 1;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        orderParams.tradingsymbol = "INFY";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.product = Constants.PRODUCT_MIS;
        /** Market protection value (0–100) represents the % distance from LTP used to convert a MARKET order into a LIMIT order.
         Example: 10.5 → limit price will be placed 10.5% away from LTP to prevent extreme slippage.
         -1 → Kite backend automatically applies market protection.*/
        orderParams.marketProtection = -1;

        OrderResponse orderResponse = kiteConnect.placeOrder(orderParams, Constants.VARIETY_REGULAR);
        System.out.println(orderResponse.orderId);
    }

    /**
     * Fetches auction instruments available for the current trading day.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getAuctionInstruments(KiteConnect kiteConnect) throws KiteException, IOException{
        List<AuctionInstrument> auctions = kiteConnect.getAuctionInstruments();
        for (int i =0; i< auctions.size(); i++){
            System.out.println(auctions.get(i).tradingSymbol+" "+auctions.get(i).quantity);
        }
    }

    /**
     * Places an auction order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the order request fails due to an I/O error.
     */
    public void placeAuctionOrder(KiteConnect kiteConnect) throws KiteException, IOException{
        OrderParams orderParams = new OrderParams();
        orderParams.price = 365.5;
        orderParams.quantity = 1;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = "ITC";
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.auctionNumber= "2559";
        kiteConnect.placeOrder(orderParams, Constants.VARIETY_AUCTION);
    }

    /**
     * Fetches the full order book.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getOrders(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get orders returns order model which will have list of orders inside, which can be accessed as follows,
        List<Order> orders = kiteConnect.getOrders();
        for(int i = 0; i< orders.size(); i++){
            System.out.println(orders.get(i).tradingSymbol+" "+orders.get(i).orderId+" "+orders.get(i).parentOrderId+
                " "+orders.get(i).orderType+" "+orders.get(i).averagePrice+" "+orders.get(i).exchangeTimestamp+" "+orders.get(i).exchangeUpdateTimestamp+" "+orders.get(i).guid);
        }
        // Read iceberg params
        /** Map<String, Object>  meta = orders.get(0).meta;
        Map icebergObject = (Map) meta.get("iceberg");
        System.out.println(icebergObject.keySet());*/

        System.out.println("list of orders size is "+orders.size());
    }

    /**
     * Fetches order history for a specific order ID.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        List<Order> orders = kiteConnect.getOrderHistory("180111000561605");
        for(int i = 0; i< orders.size(); i++){
            System.out.println(orders.get(i).orderId+" "+orders.get(i).status);
        }
        System.out.println("list size is "+orders.size());
    }

    /**
     * Fetches the complete trade book.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getTrades(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns tradebook.
        List<Trade> trades = kiteConnect.getTrades();
        for (int i=0; i < trades.size(); i++) {
            System.out.println(trades.get(i).tradingSymbol+" "+trades.size());
        }
        System.out.println(trades.size());
    }

    /**
     * Fetches trades for a specific order ID.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getTradesWithOrderId(KiteConnect kiteConnect) throws KiteException, IOException {
        // Returns trades for the given order.
        List<Trade> trades = kiteConnect.getOrderTrades("180111000561605");
        System.out.println(trades.size());
    }

    /**
     * Modifies an existing open order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void modifyOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.
        OrderParams orderParams =  new OrderParams();
        orderParams.quantity = 1;
        orderParams.orderType = Constants.ORDER_TYPE_LIMIT;
        orderParams.tradingsymbol = "ASHOKLEY";
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.exchange = Constants.EXCHANGE_NSE;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_BUY;
        orderParams.validity = Constants.VALIDITY_DAY;
        orderParams.price = 122.25;

        Order order21 = kiteConnect.modifyOrder("180116000984900", orderParams, Constants.VARIETY_REGULAR);
        System.out.println(order21.orderId);
    }

    /**
     * Cancels an open order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void cancelOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        // Order modify request will return order model which will contain only order_id.
        // Cancel order will return order model which will only have orderId.
        Order order2 = kiteConnect.cancelOrder("180116000727266", Constants.VARIETY_REGULAR);
        System.out.println(order2.orderId);
    }

    /**
     * Exits a bracket order by cancelling a child leg.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void exitBracketOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        Order order = kiteConnect.cancelOrder("180116000812153","180116000798058", Constants.VARIETY_BO);
        System.out.println(order.orderId);
    }

    /**
     * Fetches all configured GTT triggers.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getGTTs(KiteConnect kiteConnect) throws KiteException, IOException {
        List<GTT> gtts = kiteConnect.getGTTs();
        System.out.println(gtts.get(0).createdAt);
        System.out.println(gtts.get(0).condition.exchange);
        System.out.println(gtts.get(0).orders.get(0).price);
    }

    /**
     * Fetches a single GTT trigger by ID.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void getGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTT gtt = kiteConnect.getGTT(177574);
        System.out.println(gtt.condition.tradingSymbol);
    }

    /**
     * Places a single-trigger GTT order using market-order parameters.
     * Market protection is mandatory when placing a GTT market order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void placeSingleTriggerGTT(KiteConnect kiteConnect) throws IOException, KiteException{
        GTTParams gttParams = new GTTParams();
        gttParams.triggerType = Constants.SINGLE;
        gttParams.exchange = "NSE";
        gttParams.tradingsymbol = "SBIN";
        gttParams.lastPrice = 1031.05;

        List<Double> triggerPrices = new ArrayList<>();
        triggerPrices.add(1035d);
        gttParams.triggerPrices = triggerPrices;

        GTTParams.GTTOrderParams orderParams = gttParams. new GTTOrderParams();
        orderParams.orderType = Constants.ORDER_TYPE_MARKET;
        orderParams.marketProtection = -1;
        orderParams.product = Constants.PRODUCT_CNC;
        orderParams.transactionType = Constants.TRANSACTION_TYPE_SELL;
        orderParams.quantity = 1;

        List<GTTParams.GTTOrderParams> ordersList = new ArrayList();
        ordersList.add(orderParams);
        gttParams.orders = ordersList;

        GTT gtt = kiteConnect.placeGTT(gttParams);
        System.out.println(gtt.id);
    }

    /**
     * Places a two-leg OCO GTT trigger.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void placeGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTTParams gttParams = new GTTParams();
        gttParams.triggerType = Constants.OCO;
        gttParams.exchange = "NSE";
        gttParams.tradingsymbol = "SBIN";
        gttParams.lastPrice = 302.95;

        List<Double> triggerPrices = new ArrayList<>();
        triggerPrices.add(290d);
        triggerPrices.add(320d);
        gttParams.triggerPrices = triggerPrices;

        /** Only sell is allowed for OCO or two-leg orders.
         * Single leg orders can be buy or sell order.
         * Passing a last price is mandatory.
         * A stop-loss order must have trigger and price below last price and target order must have trigger and price above last price.
         * Only limit order type  and CNC product type is allowed for now.
         * */

        /** Stop-loss or lower trigger. */
        GTTParams.GTTOrderParams order1Params = gttParams. new GTTOrderParams();
        order1Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order1Params.price = 290;
        order1Params.product = Constants.PRODUCT_CNC;
        order1Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order1Params.quantity = 0;

        GTTParams.GTTOrderParams order2Params = gttParams. new GTTOrderParams();
        order2Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order2Params.price = 320;
        order2Params.product = Constants.PRODUCT_CNC;
        order2Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order2Params.quantity = 1;

        /** Target or upper trigger. */
        List<GTTParams.GTTOrderParams> ordersList = new ArrayList();
        ordersList.add(order1Params);
        ordersList.add(order2Params);
        gttParams.orders = ordersList;

        GTT gtt = kiteConnect.placeGTT(gttParams);
        System.out.println(gtt.id);
    }

    /**
     * Modifies an existing GTT trigger.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void modifyGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTTParams gttParams = new GTTParams();
        gttParams.triggerType = Constants.OCO;
        gttParams.exchange = "NSE";
        gttParams.tradingsymbol = "SBIN";
        gttParams.lastPrice = 302.95;

        List<Double> triggerPrices = new ArrayList<>();
        triggerPrices.add(290d);
        triggerPrices.add(320d);
        gttParams.triggerPrices = triggerPrices;

        GTTParams.GTTOrderParams order1Params = gttParams. new GTTOrderParams();
        order1Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order1Params.price = 290;
        order1Params.product = Constants.PRODUCT_CNC;
        order1Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order1Params.quantity = 1;

        GTTParams.GTTOrderParams order2Params = gttParams. new GTTOrderParams();
        order2Params.orderType = Constants.ORDER_TYPE_LIMIT;
        order2Params.price = 320;
        order2Params.product = Constants.PRODUCT_CNC;
        order2Params.transactionType = Constants.TRANSACTION_TYPE_SELL;
        order2Params.quantity = 1;

        List<GTTParams.GTTOrderParams> ordersList = new ArrayList();
        ordersList.add(order1Params);
        ordersList.add(order2Params);
        gttParams.orders = ordersList;

        GTT gtt = kiteConnect.modifyGTT(176036, gttParams);
        System.out.println(gtt.id);
    }

    /**
     * Cancels a GTT trigger.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws IOException if the request fails due to an I/O error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void cancelGTT(KiteConnect kiteConnect) throws IOException, KiteException {
        GTT gtt = kiteConnect.cancelGTT(175859);
        System.out.println(gtt.id);
    }

    /**
     * Fetches day and net positions.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getPositions(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get positions returns position model which contains list of positions.
        Map<String, List<Position>> position = kiteConnect.getPositions();
        System.out.println(position.get("net").size());
        System.out.println(position.get("day").size());
        System.out.println(position.get("net").get(0).averagePrice);
    }

    /**
     * Fetches equity holdings.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get holdings returns holdings model which contains list of holdings.
        List<Holding> holdings = kiteConnect.getHoldings();
        System.out.println(holdings.size());
        System.out.println(holdings.get(0).tradingSymbol);
        System.out.println(holdings.get(0).dayChange);
        System.out.println(holdings.get(0).dayChangePercentage);
    }

    /**
     * Filters and prints holdings with MTF quantity.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMTFHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get holdings returns holdings model which contains list of holdings.
        List<Holding> holdings = kiteConnect.getHoldings();
        List<Holding> mtfHoldings = new ArrayList<>();
        for (Holding holding : holdings) {
            if (holding.mtf.quantity > 0) {
                mtfHoldings.add(holding);
            }
        }
        System.out.println(mtfHoldings.size());
        System.out.println(mtfHoldings.get(0).tradingSymbol);
        System.out.println(mtfHoldings.get(0).mtf.quantity);
        System.out.println(mtfHoldings.get(0).mtf.averagePrice);
    }

    /**
     * Converts a position from one product type to another.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void converPosition(KiteConnect kiteConnect) throws KiteException, IOException {
        //Modify product can be used to change MIS to NRML(CNC) or NRML(CNC) to MIS.
        JSONObject jsonObject6 = kiteConnect.convertPosition("ASHOKLEY", Constants.EXCHANGE_NSE, Constants.TRANSACTION_TYPE_BUY, Constants.POSITION_DAY, Constants.PRODUCT_MIS, Constants.PRODUCT_CNC, 1);
        System.out.println(jsonObject6);
    }

    /**
     * Fetches the complete instruments dump.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getAllInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get all instruments list. This call is very expensive as it involves downloading of large data dump.
        // Hence, it is recommended that this call be made once and the results stored locally once every morning before market opening.
        List<Instrument> instruments = kiteConnect.getInstruments();
        System.out.println(instruments.size());
    }

    /**
     * Fetches instruments for a single exchange.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getInstrumentsForExchange(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get instruments for an exchange.
        List<Instrument> nseInstruments = kiteConnect.getInstruments("CDS");
        System.out.println(nseInstruments.size());
    }

    /**
     * Fetches quote data for multiple instruments.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getQuote(KiteConnect kiteConnect) throws KiteException, IOException {
        // Get quotes returns quote for desired tradingsymbol.
        String[] instruments = {"256265","BSE:INFY", "NSE:APOLLOTYRE", "NSE:NIFTY 50", "24507906"};
        Map<String, Quote> quotes = kiteConnect.getQuote(instruments);
        System.out.println(quotes.get("NSE:APOLLOTYRE").instrumentToken+"");
        System.out.println(quotes.get("NSE:APOLLOTYRE").oi +"");
        System.out.println(quotes.get("NSE:APOLLOTYRE").depth.buy.get(4).getPrice());
        System.out.println(quotes.get("NSE:APOLLOTYRE").timestamp);
        System.out.println(quotes.get("NSE:APOLLOTYRE").lowerCircuitLimit+"");
        System.out.println(quotes.get("NSE:APOLLOTYRE").upperCircuitLimit+"");
        System.out.println(quotes.get("24507906").oiDayHigh);
        System.out.println(quotes.get("24507906").oiDayLow);
    }

    /**
     * Fetches OHLC and last traded price data for multiple instruments.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getOHLC(KiteConnect kiteConnect) throws KiteException, IOException {
        String[] instruments = {"256265","BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
        System.out.println(kiteConnect.getOHLC(instruments).get("256265").lastPrice);
        System.out.println(kiteConnect.getOHLC(instruments).get("NSE:NIFTY 50").ohlc.open);
    }

    /**
     * Fetches LTP data for multiple instruments.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getLTP(KiteConnect kiteConnect) throws KiteException, IOException {
        String[] instruments = {"256265","BSE:INFY", "NSE:INFY", "NSE:NIFTY 50"};
        System.out.println(kiteConnect.getLTP(instruments).get("256265").lastPrice);
    }

    /**
     * Fetches historical candle data for a single instrument token.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getHistoricalData(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Get historical data dump, requires from and to date, intrument token, interval, continuous (for expired F&O contracts), oi (open interest)
         * returns historical data object which will have list of historical data inside the object.*/
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date from =  new Date();
        Date to = new Date();
        try {
            from = formatter.parse("2019-09-20 09:15:00");
            to = formatter.parse("2019-09-20 15:30:00");
        }catch (ParseException e) {
            e.printStackTrace();
        }
        HistoricalData historicalData = kiteConnect.getHistoricalData(from, to, "54872327", "15minute", false, true);
        System.out.println(historicalData.dataArrayList.size());
        System.out.println(historicalData.dataArrayList.get(0).volume);
        System.out.println(historicalData.dataArrayList.get(historicalData.dataArrayList.size() - 1).volume);
        System.out.println(historicalData.dataArrayList.get(0).oi);
    }

    /**
     * Logs out the current user session.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void logout(KiteConnect kiteConnect) throws KiteException, IOException {
        /** Logout user and kill session. */
        JSONObject jsonObject10 = kiteConnect.logout();
        System.out.println(jsonObject10);
    }

    /**
     * Fetches the mutual fund instruments dump.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMFInstruments(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MFInstrument> mfList = kiteConnect.getMFInstruments();
        System.out.println("size of mf instrument list: "+mfList.size());
    }

    /**
     * Fetches all mutual fund holdings.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMFHoldings(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MFHolding> MFHoldings = kiteConnect.getMFHoldings();
        System.out.println("mf holdings "+ MFHoldings.size());
    }

    /**
     * Places a mutual fund order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void placeMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("place order: "+ kiteConnect.placeMFOrder("INF174K01LS2", Constants.TRANSACTION_TYPE_BUY, 5000, 0, "myTag").orderId);
    }

    /**
     * Cancels a mutual fund order.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void cancelMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        kiteConnect.cancelMFOrder("668604240868430");
        System.out.println("cancel order successful");
    }

    /**
     * Fetches all mutual fund orders.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMFOrders(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MFOrder> MFOrders = kiteConnect.getMFOrders();
        System.out.println("mf orders: "+ MFOrders.size());
    }

    /**
     * Fetches a single mutual fund order by ID.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMFOrder(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("mf order: "+ kiteConnect.getMFOrder("106580291331583").tradingsymbol);
    }

    /**
     * Places a mutual fund SIP.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void placeMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("mf place sip: "+ kiteConnect.placeMFSIP("INF174K01LS2", "monthly", 1, -1, 5000, 1000).sipId);
    }

    /**
     * Modifies an existing mutual fund SIP.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void modifyMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        kiteConnect.modifyMFSIP("weekly", 1, 5, 1000, "active", "504341441825418");
    }

    /**
     * Cancels a mutual fund SIP.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void cancelMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        kiteConnect.cancelMFSIP("504341441825418");
        System.out.println("cancel sip successful");
    }

    /**
     * Fetches all mutual fund SIPs.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMFSIPS(KiteConnect kiteConnect) throws KiteException, IOException {
        List<MFSIP> sips = kiteConnect.getMFSIPs();
        System.out.println("mf sips: "+ sips.size());
    }

    /**
     * Fetches a single mutual fund SIP by ID.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @throws KiteException if the Kite API returns an error response.
     * @throws IOException if the request fails due to an I/O error.
     */
    public void getMFSIP(KiteConnect kiteConnect) throws KiteException, IOException {
        System.out.println("mf sip: "+ kiteConnect.getMFSIP("291156521960679").instalments);
    }

    /**
     * Demonstrates ticker connection setup, subscription, mode changes, and disconnection for live market data.
     *
     * @param kiteConnect initialized Kite Connect client.
     * @param tokens instrument tokens to subscribe to.
     * @throws IOException if the ticker connection fails due to an I/O error.
     * @throws WebSocketException if the websocket client reports an error.
     * @throws KiteException if the Kite API returns an error response.
     */
    public void tickerUsage(KiteConnect kiteConnect, ArrayList<Long> tokens) throws IOException, WebSocketException, KiteException {
        /** To get live price use websocket connection.
         * It is recommended to use only one websocket connection at any point of time and make sure you stop connection, once user goes out of app.
         * custom url points to new endpoint which can be used till complete Kite Connect 3 migration is done. */
        final KiteTicker tickerProvider = new KiteTicker(kiteConnect.getAccessToken(), kiteConnect.getApiKey());

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

        /** Set error listener to listen to errors.*/
        tickerProvider.setOnErrorListener(new OnError() {
            @Override
            public void onError(Exception exception) {
                //handle here.
            }

            @Override
            public void onError(KiteException kiteException) {
                //handle here.
            }

            @Override
            public void onError(String error) {
                System.out.println(error);
            }
        });

        tickerProvider.setOnTickerArrivalListener(new OnTicks() {
            @Override
            public void onTicks(ArrayList<Tick> ticks) {
                NumberFormat formatter = new DecimalFormat();
                System.out.println("ticks size " + ticks.size());
                if(ticks.size() > 0) {
                    System.out.println("last price " + ticks.get(0).getLastTradedPrice());
                    System.out.println("open interest " + formatter.format(ticks.get(0).getOi()));
                    System.out.println("day high OI " + formatter.format(ticks.get(0).getOpenInterestDayHigh()));
                    System.out.println("day low OI " + formatter.format(ticks.get(0).getOpenInterestDayLow()));
                    System.out.println("change " + formatter.format(ticks.get(0).getChange()));
                    System.out.println("tick timestamp " + ticks.get(0).getTickTimestamp());
                    System.out.println("last traded time " + ticks.get(0).getLastTradedTime());
                    System.out.println(ticks.get(0).getMarketDepth().get("buy").size());
                }
            }
        });
        // Make sure this is called before calling connect.
        tickerProvider.setTryReconnection(true);
        //maximum retries and should be greater than 0
        tickerProvider.setMaximumRetries(10);
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
    }
}
