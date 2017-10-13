import com.neovisionaries.ws.client.WebSocketException;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.kitehttp.SessionExpiryHook;
import com.rainmatter.kitehttp.exceptions.KiteException;
import com.rainmatter.models.UserModel;
import org.json.JSONException;

import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by sujith on 7/10/16.
 * This class has example of how to initialize kiteSdk and make rest api calls to place order, get orders, modify order, cancel order,
 * get positions, get holdings, convert positions, get instruments, logout user, get historical data dump, get trades
 */
public class Test {

    public static void main(String[] args){
        try {
                // First you should get request_token, public_token using kitconnect login and then use request_token, public_token, api_secret to make any kiteConnect api call.
                // Initialize KiteSdk with your apiKey.
                KiteConnect kiteConnect = new KiteConnect("xxxx");

                // set userId
                kiteConnect.setUserId("xxxxxx");

                // Get login url
                //String url = kiteConnect.getLoginUrl();

                // Set session expiry callback.
                kiteConnect.registerHook(new SessionExpiryHook() {
                    @Override
                    public void sessionExpired() {
                        System.out.println("session expired");
                    }
                });

                //Set request token and public token which are obtained from login process.
                UserModel userModel =  kiteConnect.requestAccessToken("xxxxxxxxyyxyyxyyx", "yyyyyyyyyyy");
                kiteConnect.setAccessToken(userModel.accessToken);
                kiteConnect.setPublicToken(userModel.publicToken);
                System.out.println(userModel.accessToken);

                Examples examples = new Examples();

                 examples.getMargins(kiteConnect);

                examples.placeOrder(kiteConnect);

                examples.placeBracketOrder(kiteConnect);

                examples.modifyFirstLegBo(kiteConnect);

                examples.getTriggerRange(kiteConnect);

                examples.placeCoverOrder(kiteConnect);

                examples.getOrders(kiteConnect);

                examples.getOrder(kiteConnect);

                examples.getTrades(kiteConnect);

                examples.getTradesWithOrderId(kiteConnect);

                examples.modifyOrder(kiteConnect);

                examples.modifySecondLegBoSLM(kiteConnect);

                examples.modifySecondLegBoLIMIT(kiteConnect);

                examples.cancelOrder(kiteConnect);

                examples.exitBracketOrder(kiteConnect);

                examples.getPositions(kiteConnect);

                examples.getHoldings(kiteConnect);

                examples.modifyProduct(kiteConnect);

                examples.getAllInstruments(kiteConnect);

                examples.getInstrumentsForExchange(kiteConnect);

                examples.getQuote(kiteConnect);

                examples.getHistoricalData(kiteConnect);

                examples.getOHLC(kiteConnect);

                examples.getLTP(kiteConnect);

                examples.getMfInstruments(kiteConnect);

                examples.placeMfOrder(kiteConnect);

                examples.cancelMfOrder(kiteConnect);

                examples.getMfOrders(kiteConnect);

                examples.getMfOrder(kiteConnect);

                examples.placeMfSip(kiteConnect);

                examples.modifyMfSip(kiteConnect);

                examples.cancelMfSip(kiteConnect);

                examples.getMfSips(kiteConnect);

                examples.getMfSip(kiteConnect);

                examples.getMfHoldings(kiteConnect);

                examples.logout(kiteConnect);

                ArrayList<Long> tokens = new ArrayList<>();
                tokens.add(Long.parseLong("256265"));
                tokens.add(Long.parseLong("265"));
                examples.tickerUsage(kiteConnect, tokens);
        } catch (KiteException e) {
            System.out.println(e.message+" "+e.code+" "+e.getClass().getName());
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
