package com.rainmatter.kitehttp;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import com.rainmatter.kiteconnect.KiteConnect;
import com.rainmatter.kitehttp.exceptions.*;
import org.apache.http.protocol.HTTP;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

/**
 * A wrapper of Kite Connect for making http calls.
 *
 * This is a non-Async wrapper of Kite Connect.
 */
public class KiteRequest {

    public KiteRequest() {
    }

    /**
     * POST request using UniRest library.
     *
     * @param url is endpoint to which request has to be done.
     * @param params is data that has to be sent in body.
     * @return JSONObject returns json response.
     * @throws KiteException contains error message and error code inside.
     * @throws JSONException occurs when there is error while parsing data.
     */

    public JSONObject postRequest(String url, Map<String, Object> params) throws KiteException, JSONException {
        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<JsonNode> response = Unirest.post(url)
                    .header("accept", "application/json")
                    .fields(params)
                    .asJson();

            JsonNode body = response.getBody();
            JSONObject jsonObject = body.getObject();
            int code = response.getStatus();

            if (code == 200)
                return jsonObject;
            else
                throw dealWithKiteException(body, code);

        } catch (UnirestException e) {
           throw new KiteNoNetworkException("Connection error");
        }

    }

    /**
     * GET request using UniRest library.
     *
     * @param url is endpoint to which request has to be done.
     * @param params is data that has to be sent.
     * @return JSONObject returns json response.
     * @throws KiteException contains error message and error code inside.
     * @throws JSONException occurs when there is error while parsing data.
     */
    public JSONObject getRequest(String url, Map<String, Object> params) throws KiteException, JSONException {

        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<JsonNode> response = Unirest.get(url).queryString(params)
                    .header("accept", "application/json")
                    .asJson();
            JsonNode body = response.getBody();
            JSONObject jsonObject = body.getObject();
            int code = response.getStatus();

            if (code == 200)
                return jsonObject;
            else
                throw dealWithKiteException(body, code);

        } catch (UnirestException e) {
           throw new KiteNoNetworkException("Connection error");
        }
    }

    /**
     * GET request using UniRest library.
     *
     * @param url is endpoint to which request has to be done.
     * @param params is data that has to be sent.
     * @return JSONObject returns json response.
     * @throws KiteException contains error message and error code inside.
     * @throws JSONException occurs when there is error while parsing data.
     */
    public JSONObject getRequest(String url, Map<String, Object> params, String commonKey, String[] values) throws KiteException, JSONException {
        StringBuilder builder = new StringBuilder();
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value != null) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), HTTP.UTF_8);

                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        for(int i = 0; i < values.length; i++){
            if(values != null & values.length > 0){
                try {
                    if(builder.length() > 0){
                        builder.append("&");
                    }
                    Object v = URLEncoder.encode(String.valueOf(values[i]), HTTP.UTF_8);
                    builder.append(commonKey).append("=").append(v);
                }catch (UnsupportedEncodingException e){

                }
            }
        }
        url += "?" + builder.toString();

        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson();
            JsonNode body = response.getBody();
            JSONObject jsonObject = body.getObject();
            int code = response.getStatus();

            if (code == 200)
                return jsonObject;
            else
                throw dealWithKiteException(body, code);

        } catch (UnirestException e) {
            throw new KiteNoNetworkException("Connection error");
        }
    }


    /**
     * GET request using UniRest library without params.
     * @param url is endpoint to which request has to be done.
     * @return JSONObject returns json response.
     * @throws KiteException contains error message and error code inside.
     * @throws JSONException occurs when there is error while parsing data.
     */
    public JSONObject getRequest(String url) throws KiteException, JSONException {

        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<JsonNode> response = Unirest.get(url)
                    .header("accept", "application/json")
                    .asJson();

            JsonNode body = response.getBody();
            JSONObject jsonObject = body.getObject();
            int code = response.getStatus();

            if (code == 200)
                return jsonObject;
            else
                throw dealWithKiteException(body, code);

        } catch (UnirestException e) {
            throw new KiteNoNetworkException("Connection error");
        }

    }


    /**
     * PUT request.
     * @param url is endpoint to which request has to be done.
     * @param params is data that has to be sent.
     * @return JSONObject returns json response.
     * @throws KiteException contains error message and error code inside.
     * @throws JSONException occurs when there is error while parsing data.
     */
    public JSONObject putRequest(String url, Map<String, Object> params) throws KiteException, JSONException {

        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<JsonNode> response = Unirest.put(url)
                    .header("accept", "application/json")
                    .fields(params)
                    .asJson();
            JsonNode body = response.getBody();
            JSONObject jsonObject = body.getObject();
            int code = response.getStatus();

            if (code == 200)
                return jsonObject;
            else
                throw dealWithKiteException(body, code);

        } catch (UnirestException e) {
            throw new KiteNoNetworkException("Connection error");
        }


    }

    /**
     * DELETE request.
     * @param url is endpoint to which request has to be done.
     * @param params is data that has to be sent.
     * @return JSONObject returns json response.
     * @throws KiteException contains error message and error code inside.
     * @throws JSONException occurs when there is error while parsing data.
     */
    public JSONObject deleteRequest(String url, Map<String, Object> params) throws KiteException, JSONException {

        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<JsonNode> response = Unirest.delete(url).queryString(params)
                    .header("accept", "application/json")
                    .asJson();
            JsonNode body = response.getBody();
            JSONObject jsonObject = body.getObject();
            int code = response.getStatus();

            if (code == 200)
                return jsonObject;
            else
                throw dealWithKiteException(body, code);

        } catch (UnirestException e) {
            throw new KiteNoNetworkException("Connection error");
        }

    }


    /**
     * Used to get csv response for instruments.
     * @param url is endpoint to which request has to be done.
     * @return returns String response.
     * @throws KiteException contains error message and error code inside.
     */
    public String getCsvRequest(String url) throws KiteException{
        String resp;
        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<String> response = Unirest.get(url).asString();
            if(response.getStatus() == 200) {
                resp = response.getBody();
                return resp;
            }
            else {
                throw new KiteGeneralException("Csv fetch failed.", 400);
            }
        } catch (UnirestException e) {
            throw new KiteNoNetworkException("Connection error");
        }

    }

    /**
     * Used to get csv response for mutualfunds instruments.
     * @param url is endpoint to which request has to be done.
     * @param params id the data that has to be sent.
     * @return returns String response.
     * @throws KiteException contains error message and error code inside.
     */
    public String getCsvRequest(String url, Map<String, Object> params) throws KiteException{
        StringBuilder builder = new StringBuilder();
        String resp;

        // create url using string builder.
        for (String key : params.keySet()) {
            Object value = params.get(key);
            if (value != null) {
                try {
                    value = URLEncoder.encode(String.valueOf(value), HTTP.UTF_8);

                    if (builder.length() > 0)
                        builder.append("&");
                    builder.append(key).append("=").append(value);
                } catch (UnsupportedEncodingException e) {
                }
            }
        }
        url += "?" + builder.toString();

        try {
            if(KiteConnect.httpHost != null){
                Unirest.setProxy(KiteConnect.httpHost);
            }
            HttpResponse<String> response = Unirest.get(url).asString();
            if(response.getStatus() == 200) {
                resp = response.getBody();
                return resp;
            }
            else {
                throw new KiteGeneralException("Csv fetch failed.", 400);
            }
        } catch (UnirestException e) {
            throw new KiteNoNetworkException("Connection error");
        }
    }

    /**
     * Deals with all kite exceptions.
     *
     * @param jsonObject is response from server.
     * @param errorCode is http error code.
     * @return returns one of the type of KiteException.
     * @throws JSONException occurs when there is error while parsing data.
     */
    public KiteException dealWithKiteException(JsonNode jsonObject, int errorCode) throws JSONException {

        // get the exception
        String exception = jsonObject.getObject().get("error_type").toString();
        String message = jsonObject.getObject().get("message").toString();

        // handling all exception cases.
        switch (exception) {
            // if there is a token exception, generate a signal to logout the user.
            case "TokenException":
                if(KiteConnect.sessionExpiryHook != null) {
                    KiteConnect.sessionExpiryHook.sessionExpired();
                }
                return new KiteTokenException(message, errorCode);

            case "UserException":
                return new KiteUserException(message, errorCode);

            case "DataException":
                return new KiteDataException(message, errorCode);

            case "ClientNetworkException":
                return new KiteClientNetworkException(message, errorCode);

            case "GeneralException":
                return new KiteGeneralException(message, errorCode);

            case "InputException":
                return new KiteInputException(message, errorCode);

            case "OrderException":
                return new KiteOrderException(message, errorCode);

            case "NetworkException":
                return new KiteNetworkException(message, errorCode);

            case "InvalidDataError":
                return new KiteGeneralException(message, errorCode);

            case "PermissionException":
                return new KitePermissionException(message, errorCode);
        }

        return null;
    }

}
