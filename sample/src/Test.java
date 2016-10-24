import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kitehttp.SessionExpiryHook;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.*;
import org.json.JSONException;
import org.json.JSONObject;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.ticker.OnConnect;
import com.rainmatter.ticker.OnDisconnect;
import com.rainmatter.ticker.OnTick;
import com.rainmatter.ticker.KiteTicker;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by sujith on 7/10/16.
 * This class has example of how to initialize kiteSdk and make rest api calls to place order, get orders, modify order, cancel order,
 * get positions, get holdings, convert positions, get instruments, logout user, get historical data dump, get trades
 */
public class Test {

    public static void main(String[] args){
        try {
                // First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteconnect api call.
                // Initialize KiteSdk with your apiKey.
                KiteConnect kiteconnect = new KiteConnect("xxxxxx");

                // set userId
                kiteconnect.setUserId("xxxxxx");

                //set proxy is optional, if you want to set proxy.
                kiteconnect.setProxy(new HttpHost("host_name"))

                // Get login url
                String url = kiteconnect.getLoginUrl();

                // Set session expiry callback.
                kiteconnect.registerHook(new SessionExpiryHook() {
                    @Override
                    public void sessionExpired() {
                        System.out.println("session expired");
                    }
                });

                // Set request token and public token which are obtained from login process.
                UserModel userModel =  kiteconnect.requestAccessToken("xxxxxxxx", "xxxxxxx");

                kiteconnect.setAccessToken(userModel.accessToken);
                kiteconnect.setPublicToken(userModel.publicToken);

                Examples examples = new Examples();
                examples.getMargins(kiteconnect);

                examples.placeOrder(kiteconnect);

                examples.placeBracketOrder(kiteconnect);

                examples.getTriggerRange(kiteconnect);

                examples.placeCoverOrder(kiteconnect);

                examples.getOrders(kiteconnect);

                examples.getTrades(kiteconnect);

                examples.getTradesWithOrderId(kiteconnect);

                examples.modifyOrder(kiteconnect);

                examples.cancelOrder(kiteconnect);

                examples.getPositions(kiteconnect);

                examples.getHoldings(kiteconnect);

                examples.modifyProduct(kiteconnect);

                examples.getAllInstruments(kiteconnect);

                examples.getInstrumentsForExchange(kiteconnect);

                examples.getQuote(kiteconnect);

                examples.getHistoricalData(kiteconnect);

                examples.logout(kiteconnect);

                examples.tickerUsage(kiteconnect);

        } catch (KiteException e) {
            System.out.println(e.message);
        } catch (JSONException e){
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
