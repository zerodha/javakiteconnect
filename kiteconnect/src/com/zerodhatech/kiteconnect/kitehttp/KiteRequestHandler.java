package com.zerodhatech.kiteconnect.kitehttp;

import com.zerodhatech.kiteconnect.KiteConnect;
import com.zerodhatech.kiteconnect.kitehttp.exceptions.KiteException;
import okhttp3.*;
import okhttp3.logging.HttpLoggingInterceptor;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Request handler for all Http requests
 */
public class KiteRequestHandler {

    private OkHttpClient client;
    private String USER_AGENT = "javakiteconnect/3.1.14";

    /** Initialize request handler.
     * @param proxy to be set for making requests.*/
    public KiteRequestHandler(Proxy proxy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        if(proxy != null) {
            builder.proxy(proxy);
        }

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        if(KiteConnect.ENABLE_LOGGING) {
            client = builder.addInterceptor(logging).build();
        }else {
            client = builder.build();
        }
    }

    /** Makes a GET request.
     * @return JSONObject which is received by Kite Trade.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.*/
    public JSONObject getRequest(String url, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createGetRequest(url, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /** Makes a GET request.
     * @return JSONObject which is received by Kite Trade.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of params which has to be sent as query params.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.*/
    public JSONObject getRequest(String url, Map<String, Object> params, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createGetRequest(url, params, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /** Makes a POST request.
     * @return JSONObject which is received by Kite Trade.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of params which has to be sent in the body.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.*/
    public JSONObject postRequest(String url, Map<String, Object> params, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createPostRequest(url, params, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /** Make a JSON POST request.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param jsonArray is the JSON array of params which has to be sent in the body.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     * */
    public JSONObject postRequestJSON(String url, JSONArray jsonArray, Map<String, Object> queryParams, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createJsonPostRequest(url, jsonArray, queryParams, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return  new KiteResponseHandler().handle(response, body);
    }

    /** Makes a PUT request.
     * @return JSONObject which is received by Kite Trade.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of params which has to be sent in the body.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.*/
    public JSONObject putRequest(String url, Map<String, Object> params, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createPutRequest(url, params, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /** Makes a DELETE request.
     * @return JSONObject which is received by Kite Trade.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of params which has to be sent in the query params.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.*/
    public JSONObject deleteRequest(String url, Map<String, Object> params, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createDeleteRequest(url, params, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /** Makes a GET request.
     * @return JSONObject which is received by Kite Trade.
     * @param url is the endpoint to which request has to be sent.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param commonKey is the key that has to be sent in query param for quote calls.
     * @param values is the values that has to be sent in query param like 265, 256265, NSE:INFY.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * @throws JSONException is thrown for parsing errors.
     * */
    public JSONObject getRequest(String url, String commonKey, String[] values, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = createGetRequest(url, commonKey, values, apiKey, accessToken);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body);
    }

    /** Makes GET request to fetch CSV dump.
     * @return String which is received from server.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @throws IOException is thrown when there is a connection related error.
     * @throws KiteException is thrown for all Kite Trade related errors.
     * */
    public String getCSVRequest(String url, String apiKey, String accessToken) throws IOException, KiteException, JSONException {
        Request request = new Request.Builder().url(url).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        return new KiteResponseHandler().handle(response, body, "csv");
    }

    /** Creates a GET request.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * */
    public Request createGetRequest(String url, String apiKey, String accessToken) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        return new Request.Builder().url(httpBuilder.build()).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
    }

    /** Creates a GET request.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of data that has to be sent in query params.
     * */
    public Request createGetRequest(String url, Map<String, Object> params, String apiKey, String accessToken) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        return new Request.Builder().url(httpBuilder.build()).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
    }

    /** Creates a GET request.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param commonKey is the key that has to be sent in query param for quote calls.
     * @param values is the values that has to be sent in query param like 265, 256265, NSE:INFY.
     * */
    public Request createGetRequest(String url, String commonKey, String[] values, String apiKey, String accessToken) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(int i = 0; i < values.length; i++) {
            httpBuilder.addQueryParameter(commonKey, values[i]);
        }
        return new Request.Builder().url(httpBuilder.build()).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
    }

    /** Creates a POST request.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of data that has to be sent in the body.
     * */
    public Request createPostRequest(String url, Map<String, Object> params, String apiKey, String accessToken) {
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            builder.add(entry.getKey(), entry.getValue().toString());
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
        return request;
    }

    /** Create a POST request with body type JSON.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param jsonArray is the JSONArray of data that has to be sent in the body.
     * */
    public Request createJsonPostRequest(String url, JSONArray jsonArray, Map<String, Object> queryParams, String apiKey, String accessToken) {
        MediaType JSON = MediaType.parse("application/json; charset=utf-8");

        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        if(queryParams.size() > 0) {
            for (Map.Entry<String, Object> entry : queryParams.entrySet()) {
                httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
            }
        }

        RequestBody body = RequestBody.create(jsonArray.toString(), JSON);
        Request request;
        request = queryParams.size() > 0?  new Request.Builder()
            .url(httpBuilder.build())
            .header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken)
            .post(body)
            .build() : new Request.Builder()
            .url(url)
            .header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken)
            .post(body)
            .build();
        return request;
    }

    /** Creates a PUT request.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of data that has to be sent in the body.
     * */
    public Request createPutRequest(String url, Map<String, Object> params, String apiKey, String accessToken){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            builder.add(entry.getKey(), entry.getValue().toString());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).put(requestBody).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
        return request;
    }

    /** Creates a DELETE request.
     * @param url is the endpoint to which request has to be done.
     * @param apiKey is the api key of the Kite Connect app.
     * @param accessToken is the access token obtained after successful login process.
     * @param params is the map of data that has to be sent in the query params.
     * */
    public Request createDeleteRequest(String url, Map<String, Object> params, String apiKey, String accessToken){
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }

        Request request = new Request.Builder().url(httpBuilder.build()).delete().header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").header("Authorization", "token "+apiKey+":"+accessToken).build();
        return request;
    }
}