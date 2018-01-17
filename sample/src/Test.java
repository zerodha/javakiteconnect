import com.neovisionaries.ws.client.WebSocketException;
import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.SessionExpiryHook;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import com.zerodhatech.models.User;
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
                KiteConnect kiteConnect = new KiteConnect("xxxxxxyyyyyzzz");

                // Set userId
                kiteConnect.setUserId("xxxxyyy");

                //Enable logs for debugging purpose. This will log request and response.
                kiteConnect.setEnableLogging(true);

                // Get login url
                String url = kiteConnect.getLoginURL();

                // Set session expiry callback.
                kiteConnect.setSessionExpiryHook(new SessionExpiryHook() {
                    @Override
                    public void sessionExpired() {
                        System.out.println("session expired");
                    }
                });

                /* The request token can to be obtained after completion of login process. Check out https://kite.trade/docs/connect/v1/#login-flow for more information.
                   A request token is valid for only a couple of minutes and can be used only once. An access token is valid for one whole day. Don't call this method for every app run.
                   Once an access token is received it should be stored in preferences or database for further usage.
                 */
                User user =  kiteConnect.generateSession("xxxxxtttyyy", "xxxxxxxyyyyy");
                kiteConnect.setAccessToken(user.accessToken);
                kiteConnect.setPublicToken(user.publicToken);

                Examples examples = new Examples();

                examples.getProfile(kiteConnect);

                examples.getMargins(kiteConnect);

                examples.placeOrder(kiteConnect);

                examples.modifyOrder(kiteConnect);

                examples.cancelOrder(kiteConnect);

                examples.placeBracketOrder(kiteConnect);

                examples.modifyFirstLegBo(kiteConnect);

                examples.modifySecondLegBoSLM(kiteConnect);

                examples.modifySecondLegBoLIMIT(kiteConnect);

                examples.exitBracketOrder(kiteConnect);

                examples.getTriggerRange(kiteConnect);

                examples.placeCoverOrder(kiteConnect);

                examples.converPosition(kiteConnect);

                examples.getHistoricalData(kiteConnect);

                examples.getOrders(kiteConnect);

                examples.getOrder(kiteConnect);

                examples.getTrades(kiteConnect);

                examples.getTradesWithOrderId(kiteConnect);

                examples.getPositions(kiteConnect);

                examples.getHoldings(kiteConnect);

                examples.getAllInstruments(kiteConnect);

                examples.getInstrumentsForExchange(kiteConnect);

                examples.getQuote(kiteConnect);

                examples.getOHLC(kiteConnect);

                examples.getLTP(kiteConnect);

                examples.getMFInstruments(kiteConnect);

                examples.placeMFOrder(kiteConnect);

                examples.cancelMFOrder(kiteConnect);

                examples.getMFOrders(kiteConnect);

                examples.getMFOrder(kiteConnect);

                examples.placeMFSIP(kiteConnect);

                examples.modifyMFSIP(kiteConnect);

                examples.cancelMFSIP(kiteConnect);

                examples.getMFSIPS(kiteConnect);

                examples.getMFSIP(kiteConnect);

                examples.getMFHoldings(kiteConnect);

                examples.logout(kiteConnect);

                ArrayList<Long> tokens = new ArrayList<>();
                tokens.add(Long.parseLong("265"));
                examples.tickerUsage(kiteConnect, tokens);
        } catch (KiteException e) {
            System.out.println(e.message+" "+e.code+" "+e.getClass().getName());
        } catch (JSONException e) {
            e.printStackTrace();
        }catch (IOException e) {
            e.printStackTrace();
        } catch (WebSocketException e) {
            e.printStackTrace();
        }
    }
}
