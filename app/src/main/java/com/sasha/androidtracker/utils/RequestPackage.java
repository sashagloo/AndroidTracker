package com.sasha.androidtracker.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * RequestPackage class
 * to manage params for request
 * Project : AndroidTracker
 *
 * @autor Sasha Antipin
 * @since 2-12-2015
 * @version 0.1
 */
public class RequestPackage {

    private String uri;
    private String method = "GET";
    private Map<String, String> params = new HashMap<>();


    public String getUri() {
        return uri;
    }
    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getMethod() {
        return method;
    }
    public void setMethod(String method) {
        this.method = method;
    }

    public Map<String, String> getParams() { return params;  }
    public void setParams(Map<String, String> params) {
        this.params = params;
    }

    /**
     * method to set one parameter at a time for request
     * @param
     */
    public void setParams(String key, String valuue) {
        params.put(key, valuue);
    }

    /**
     * method retrieve the parameters as an encoded String
     * for request
     * @param
     */
     public String getEncodedParams() {
         StringBuilder sb = new StringBuilder();
         String value = null;

         for (String key : params.keySet()) {
             try {
                 value = URLEncoder.encode(
                                            params.get(key),  // value
                                            "UTF-8");         // character set name
             } catch (UnsupportedEncodingException e) {
                 e.printStackTrace();
             }
             if (sb.length() > 0) {
                 sb.append("&");
             }
             sb.append(key + "=" + value);
         }
         return sb.toString();
     }



}
