package com.rainmatter.kiteconnect.kitehttp;

import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.kiteconnect.kitehttp.exceptions.GeneralException;
import com.rainmatter.kiteconnect.kitehttp.exceptions.KiteException;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.Proxy;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * Request handler for all Http requests
 */
public class KiteRequestHandler {

    private String GET = "GET";
    private String POST = "POST";
    private String PUT = "PUT";
    private String DELETE = "DELETE";
    private OkHttpClient client;
    private String USER_AGENT = "javakiteconnect/3.0.0";

    public KiteRequestHandler(Proxy proxy) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10000, TimeUnit.MILLISECONDS);
        if(proxy != null) {
            builder.proxy(proxy);
        }
        client = builder.build();
    }

    public JSONObject getRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        logRequest(url, params, GET);
        Request request = createGetRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logResponse(response, body, true);
        return new KiteResponseHandler().handle(response, body);
    }

    public JSONObject postRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        logRequest(url, params, POST);
        Request request = createPostRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logResponse(response, body, true);
        return new KiteResponseHandler().handle(response, body);
    }

    public JSONObject putRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        logRequest(url, params, PUT);
        Request request = createPutRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logResponse(response, body, true);
        return new KiteResponseHandler().handle(response, body);
    }

    public JSONObject deleteRequest(String url, Map<String, Object> params) throws IOException, KiteException, JSONException {
        logRequest(url, params, DELETE);
        Request request = createDeleteRequest(url, params);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logResponse(response, body, true);
        return new KiteResponseHandler().handle(response, body);
    }

    public JSONObject getRequest(String url, Map<String, Object> params, String commonKey, String[] values) throws IOException, KiteException, JSONException {
        logRequest(url, params, commonKey, values, GET);
        Request request = createGetRequest(url, params, commonKey, values);
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logResponse(response, body, true);
        return new KiteResponseHandler().handle(response, body);
    }

    public String getCSVRequest(String url, Map<String, Object> params) throws IOException, KiteException {
        logRequest(url, params, GET);
        Request request = new Request.Builder().url(url).build();
        Response response = client.newCall(request).execute();
        String body = response.body().string();
        logResponse(response, body, false);
        return new KiteResponseHandler().handle(response, body, "csv");
    }

    /*Creates get request */
    public Request createGetRequest(String url, Map<String, Object> params) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        return new Request.Builder().url(httpBuilder.build()).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").build();
    }

    public Request createGetRequest(String url, Map<String, Object> params, String commonKey, String[] values) {
        HttpUrl.Builder httpBuilder = HttpUrl.parse(url).newBuilder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            httpBuilder.addQueryParameter(entry.getKey(), entry.getValue().toString());
        }
        for(int i = 0; i < values.length; i++) {
            httpBuilder.addQueryParameter(commonKey, values[i]);
        }
        return new Request.Builder().url(httpBuilder.build()).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").build();
    }

    public Request createPostRequest(String url, Map<String, Object> params) {
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            builder.add(entry.getKey(), entry.getValue().toString());
        }

        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).post(requestBody).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").build();
        return request;
    }

    public Request createPutRequest(String url, Map<String, Object> params){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            builder.add(entry.getKey(), entry.getValue().toString());
        }
        RequestBody requestBody = builder.build();
        Request request = new Request.Builder().url(url).put(requestBody).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").build();
        return request;
    }

    public Request createDeleteRequest(String url, Map<String, Object> params){
        FormBody.Builder builder = new FormBody.Builder();
        for(Map.Entry<String, Object> entry: params.entrySet()){
            builder.add(entry.getKey(), entry.getValue().toString());
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder().url(url).delete(body).header("User-Agent", USER_AGENT).header("X-Kite-Version", "3").build();
        return request;
    }

    public void logRequest(String url, Map<String, Object> params, String type){
        if(KiteConnect.ENABLE_LOGGING) {
            System.out.println(url);
            System.out.println(type);
            if(params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " " + entry.getValue().toString());
                }
            }
        }
    }

    public void logRequest(String url, Map<String, Object> params, String commonKey, String[] values, String type) {
        if(KiteConnect.ENABLE_LOGGING) {
            System.out.println(url);
            System.out.println(type);
            if(params != null) {
                for (Map.Entry<String, Object> entry : params.entrySet()) {
                    System.out.println(entry.getKey() + " " + entry.getValue().toString());
                }
            }
            for(int i = 0; i < values.length; i++) {
                System.out.println(commonKey + " " + values[i]);
            }
        }
    }

    public void logResponse(Response response, String body, boolean enabled){
        if(KiteConnect.ENABLE_LOGGING) {
            System.out.println(response.code());
            /*Set<String> headerKeys = response.headers().names();
               for(String headerItem: headerKeys){
                   System.out.println(headerItem);
               }*/
            if(enabled) {
                System.out.println(body);
            }
        }
    }
}
