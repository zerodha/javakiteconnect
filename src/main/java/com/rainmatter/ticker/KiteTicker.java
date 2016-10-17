package com.rainmatter.ticker;

import com.neovisionaries.ws.client.*;
import com.rainmatter.models.Depth;
import com.rainmatter.models.Tick;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.kiteconnect.Routes;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.*;

/**
 * Provides capability to establish a websocket connection to get live quotes.
 */
public class KiteTicker {

    private String wsuri ;
    private OnTick onTickerArrivalListener;
    private OnConnect onConnectedListener;
    private OnDisconnect onDisconnectedListener;
    private WebSocket ws;

    //private Thread mThread;
    private KiteConnect _kiteSdk;

    public final int NseCM = 1,
            NseFO = 2,
            NseCD = 3,
            BseCM = 4,
            BseFO = 5,
            BseCD = 6,
            McxFO = 7,
            McxSX = 8,
            NseIndices = 9;

    private  final String mSubscribe = "subscribe",
            mUnSubscribe = "unsubscribe",
            mSetMode = "mode";


    private boolean connection = false;
    public static String modeFull  = "full", // Full quote inluding market depth. 172 bytes.
            modeQuote = "quote", // Quote excluding market depth. 52 bytes.
            modeLTP   = "ltp"; // Only LTP. 4 bytes.;

    public KiteTicker(KiteConnect kiteSdk){
        _kiteSdk = kiteSdk;
        createUrl();
    }

    /** Creates url for websocket connection.*/
    private void createUrl(){
        try {
            String userId =  _kiteSdk.getUserId();
            String publicToken = _kiteSdk.getPublicToken();
            String apiKey = _kiteSdk.getApiKey();
            wsuri = new Routes().getWsuri().replace(":user_id", userId).replace(":public_token", publicToken).replace(":api_key", apiKey);
        }catch (Exception e){
            e.printStackTrace();

        }
    }
    /** Set listener for listening to ticks.
     * @param onTickerArrivalListener is listener which listens for each tick.*/
    public void setOnTickerArrivalListener(OnTick onTickerArrivalListener){
        this.onTickerArrivalListener = onTickerArrivalListener;
    }

    /** Set listener for on connection established.
     * @param listener is used to listen to onConnected event. */
    public void setOnConnectedListener(OnConnect listener){
        onConnectedListener = listener;
    }

    /** Set listener for on connection is disconnected.
     * @param listener is used to listen to onDisconnected event.*/
    public void setOnDisconnectedListener(OnDisconnect listener){
        onDisconnectedListener = listener;
    }

    /** Establishes a web socket connection.*/
    public void connect() throws WebSocketException, IOException {

        if(connection){
            disconnect();
            connection = false;
        }

        if(wsuri == null){
            createUrl();
        }
        ws = new WebSocketFactory().createSocket(wsuri);
        ws.addListener(new WebSocketAdapter() {

            @Override
            public void onConnected(WebSocket websocket, Map<String, List<String>> headers) {
                connection = true;
                if(onConnectedListener != null) {
                    onConnectedListener.onConnected();
                }
            }

            @Override
            public void onTextMessage(WebSocket websocket, String message)  {
            }

            @Override
            public void onBinaryMessage(WebSocket websocket, byte[] binary)  {
                super.onBinaryMessage(websocket, binary);

                 ArrayList<Tick> tickerData = parseBinary(binary);

                 if (onTickerArrivalListener != null) {
                      onTickerArrivalListener.onTick(tickerData);
                 }
            }

            /**
             * On disconnection, return statement ensures that the thread ends.
             * @param websocket
             * @param serverCloseFrame
             * @param clientCloseFrame
             * @param closedByServer
             * @throws Exception
             */
             @Override
             public void onDisconnected(WebSocket websocket, WebSocketFrame serverCloseFrame, WebSocketFrame clientCloseFrame, boolean closedByServer) {
                if(onDisconnectedListener != null){
                    onDisconnectedListener.onDisconnected();
                }
                    return;
             }

             @Override
             public void onError(WebSocket websocket, WebSocketException cause) {
                    super.onError(websocket, cause);
                }
        });
            ws.connect();
    }

    /** Disconnects websocket connection.*/
    public void disconnect(){
        if (ws != null && ws.isOpen()) {
            ws.disconnect();
        }
    }

    /** Returns true if websocket connection is open.
     * @return boolean*/
    public boolean isConnectionOpen(){
        if(ws != null) {
            if (ws.isOpen()){
                return true;
            }
        }
        return false;
    }

    /**
     * Setting different modes for an arraylist of tokens.
     * @param tokens an arraylist of tokens
     * @param mode the mode that needs to be set. Scroll up to see different
     *             kind of modes
     */
    public void setMode(ArrayList<Long> tokens, String mode){
        JSONObject jobj = new JSONObject();
        try {
            // int a[] = {256265, 408065, 779521, 738561, 177665, 25601};
            JSONArray list = new JSONArray();
            JSONArray listMain = new JSONArray();
            listMain.put(0, mode);
            for(int i=0; i< tokens.size(); i++){
                list.put(i, tokens.get(i));
            }
            listMain.put(1, list);
            jobj.put("a", mSetMode);
            jobj.put("v", listMain);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if(ws != null) {
            ws.sendText(jobj.toString());
        }
    }

    /** Subscribes for list of tokens.
     * @param tokens is list of tokens to be subscribed for.*/
    public void subscribe(ArrayList<Long> tokens) throws IOException, WebSocketException {
        if(ws != null) {
            if (ws.isOpen()) {
                createTickerJsonObject(tokens, mSubscribe);
                ws.sendText(createTickerJsonObject(tokens, mSubscribe).toString());
                setMode(tokens, modeQuote);
            }else
                connect();
        }else
            connect();
    }

    private JSONObject createTickerJsonObject(ArrayList<Long> tokens, String action) {
        JSONObject jobj = new JSONObject();
        try {
            JSONArray list = new JSONArray();
            for (int i = 0; i < tokens.size(); i++) {
                list.put(i, tokens.get(i));
            }
            jobj.put("v", list);
            jobj.put("a", action);
        }
        catch (JSONException e){
        }

        return jobj;
    }

    /** Unsubscribes ticks for list of tokens.*/
    public void unsubscribe(ArrayList<Long> tokens){
        if(ws != null) {
            if (ws.isOpen()) {
                ws.sendText(createTickerJsonObject(tokens, mUnSubscribe).toString());
            }
        }
    }

    /*
     * This method parses binary data got from kite server to get ticks for each token subscribed.
     * we have to keep a main Array List which is global and keep deleting element in the list and add new data element in that place and call notify data set changed
     */
    private ArrayList<Tick> parseBinary(byte [] binaryPackets) {
        ArrayList<Tick> ticks = new ArrayList<Tick>();

        ArrayList<byte[]> packets = splitPackets(binaryPackets);

        for (int i = 0; i < packets.size(); i++) {
            byte[] bin = packets.get(i);
            byte[] t = Arrays.copyOfRange(bin, 0, 4);
            int x = ByteBuffer.wrap(t).getInt();

            //int token = x >> 8;
            int segment = x & 0xff;

            switch (segment) {
                case NseIndices:
                    Tick tick = getNseIndeciesData(bin, x);
                    ticks.add(tick);
                    break;

                case McxFO:
                case NseCM:
                case NseFO:
                case NseCD:
                case BseCM:
                case BseCD:
                case BseFO:

                    /** decimal precision*/
                    int dec1 = (segment == NseCD) ? 10000000 : 100;
                    /** ltp only quote*/
                    if (bin.length == 8) {
                        Tick tick1 = getLtpQuote(bin, x, dec1);

                        ticks.add(tick1);
                        continue;
                    }

                    Tick tick2 = getTickData(bin, x, dec1);
                    // full quote including depth
                    if (bin.length > 60) {
                        tick2.setMode(modeFull);
                        Map<String, ArrayList<Depth>> depthMap = getDepthData(bin, dec1);
                        tick2.setMarketDepth(depthMap);
                    }else {
                        //Log.e("Ticker", "market depth bytes not available");
                    }
                    ticks.add(tick2);
                    break;
            }
        }
        return ticks;
    }

    private Tick getNseIndeciesData(byte[] bin, int x){
        int dec = 100;
        Tick tick = new Tick();
        tick.setMode(modeFull);
        tick.setTradable(false);
        tick.setToken(x);
        tick.setLastTradedPrice(convertToDouble(getBytes(bin, 4, 8)) / dec);
        tick.setHighPrice(convertToDouble(getBytes(bin, 8, 12)) / dec);
        tick.setLowPrice(convertToDouble(getBytes(bin, 12, 16)) / dec);
        tick.setOpenPrice(convertToDouble(getBytes(bin, 16, 20)) / dec);
        tick.setClosePrice(convertToDouble(getBytes(bin, 20, 24)) / dec);
        tick.setNetPriceChangeFromClosingPrice(convertToDouble(getBytes(bin, 24, 28)) / dec);
        return tick;
    }

    private Tick getLtpQuote(byte[] bin, int x, int dec1){
        Tick tick1 = new Tick();
        tick1.setMode(modeLTP);
        tick1.setTradable(true);
        tick1.setToken(x);
        tick1.setLastTradedPrice(convertToDouble(getBytes(bin, 4, 8)) / dec1);
        return tick1;
    }

    /**get tick data for MCXFO, NSEFO, NSECM and NSECD*/
    private Tick getTickData(byte[] bin, int x, int dec1){
        Tick tick2 = new Tick();
        tick2.setMode(modeQuote);
        tick2.setToken(x);
        double lastTradedPrice = convertToDouble(getBytes(bin, 4, 8)) / dec1;
        tick2.setLastTradedPrice(lastTradedPrice);
        tick2.setLastTradedQuantity(convertToDouble(getBytes(bin, 8, 12)));
        tick2.setAverageTradePrice(convertToDouble(getBytes(bin, 12, 16)) / dec1);
        tick2.setVolumeTradedToday(convertToDouble(getBytes(bin, 16, 20)));
        tick2.setTotalBuyQuantity(convertToDouble(getBytes(bin, 20, 24)));
        tick2.setTotalSellQuantity(convertToDouble(getBytes(bin, 24, 28)));
        tick2.setOpenPrice(convertToDouble(getBytes(bin, 28, 32)) / dec1);
        tick2.setHighPrice(convertToDouble(getBytes(bin, 32, 36)) / dec1);
        tick2.setLowPrice(convertToDouble(getBytes(bin, 36, 40)) / dec1);
        double closePrice = convertToDouble(getBytes(bin, 40, 44)) / dec1;
        tick2.setClosePrice(closePrice);
        setChangeForTick(tick2, lastTradedPrice, closePrice);
        return tick2;
    }

    private void setChangeForTick(Tick tick, double lastTradedPrice, double closePrice){
        if (closePrice != 0)
            tick.setNetPriceChangeFromClosingPrice((lastTradedPrice - closePrice) * 100 / closePrice);
        else
            tick.setNetPriceChangeFromClosingPrice(0);

    }

    /** Reads all bytes and returns map of depth values for offer and bid*/
    private Map<String, ArrayList<Depth>> getDepthData(byte[] bin, int dec){
        byte[] depthBytes = getBytes(bin, 44, 164);
        int s = 0;
        ArrayList<Depth> buy = new ArrayList<Depth>();
        ArrayList<Depth> sell = new ArrayList<Depth>();
        for (int k = 0; k < 10; k++) {
            s = k * 12;
            Depth depth = new Depth();
            depth.setQuantity((int)convertToDouble(getBytes(depthBytes, s, s + 4)));
            depth.setPrice(convertToDouble(getBytes(depthBytes, s + 4, s + 8))/dec);
            depth.setOrders((int)convertToDouble(getBytes(depthBytes, s + 8, s + 10)));

            if (k < 5) {
                buy.add(depth);
            } else {
                sell.add(depth);
            }
        }
        Map<String, ArrayList<Depth>> depthMap = new HashMap<String, ArrayList<Depth>>();
        depthMap.put("buy", buy);
        depthMap.put("sell", sell);
        return depthMap;
    }

    /**Each byte stream contains many packets. This method reads first two bits and calculates number of packets in the byte stream and split it.*/
    private ArrayList<byte []> splitPackets(byte[] bin){

        ArrayList<byte []> packets = new ArrayList<byte []>();
        int noOfPackets = getLengthFromByteArray(getBytes(bin, 0, 2)); //in.read(bin, 0, 2);
        int j = 2;

        for(int i = 0; i < noOfPackets; i++){
            int sizeOfPacket = getLengthFromByteArray(getBytes(bin, j, j + 2));//in.read(bin, j, j+2);
            byte[] packet = Arrays.copyOfRange(bin, j + 2, j + 2 + sizeOfPacket);
            packets.add(packet);
            j = j + 2 + sizeOfPacket;
        }
        return packets;
    }

    /** Reads values of specified position in byte array*/
    private byte[] getBytes(byte[] bin, int start, int end){
        return Arrays.copyOfRange(bin, start, end);
    }

    /** Convert binary data to double datatype*/
    private double convertToDouble(byte[] bin){
        ByteBuffer bb = ByteBuffer.wrap(bin);
        bb.order(ByteOrder.BIG_ENDIAN);
        if(bin.length < 4)
            return bb.getShort();
        else if(bin.length < 8)
            return bb.getInt();
        else
            return bb.getDouble();
    }

    /** Returns length of packet by reading byte array values*/
    private int getLengthFromByteArray(byte[] bin){
        ByteBuffer bb = ByteBuffer.wrap(bin);
        bb.order(ByteOrder.BIG_ENDIAN);
        return bb.getShort();
    }

}