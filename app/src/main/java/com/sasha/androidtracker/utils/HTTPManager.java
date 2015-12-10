package com.sasha.androidtracker.utils;

import android.util.Base64;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * HTTPManager class
 * uses java.io, java.net
 * Project : AndroidTracker
 *
 * @see HttpURLConnection, URL, InputStreamReader, BufferedReader
 * @author Sasha Antipin
 * @since 09-12-2015
 * @version 0.1
 */
public class HTTPManager {

    /**
     * make connection with the online data source, get data, read it to String
     * @param requestPackage String representing the location of the data
     * @return String with content
     * @throws IOException
     */
    public static String getData(RequestPackage requestPackage) throws IOException {

        BufferedReader reader = null;
        String uri = requestPackage.getUri();

        // append parameters for GET ----------------------
        if (requestPackage.getMethod().equals("GET")) {
            uri += "?" + requestPackage.getEncodedParams();
        }

        try {
            // get connection with URL ----------------------------------------
            URL url = new URL(uri);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();

            /** make params for JSON format ----------------------*/
            JSONObject json = new JSONObject(requestPackage.getParams());
            String params = "params=" + json.toString();

            // append method (GET, POST, PUT, DELETE) ------------------------------------
            connection.setRequestMethod(requestPackage.getMethod());


            if (requestPackage.getMethod().equals("POST")) {
                connection.setDoOutput(true);
                OutputStreamWriter writer = new OutputStreamWriter(connection.getOutputStream());
                writer.write(params);       // for JSON format

                writer.flush();
            }

            // read input (RESPONSE) -------------------------------------------
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            // get separate lines from reader ------------------
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        } finally {
            if (reader != null) {
                try {
                    // close connection !!! ---------------------
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }

    /**
     * make connection throw authorisation with the online data source, get data, read it to String
     * @param uri String representing the location of the data
     * @return String with content
     * @throws IOException
     */
    public static String getData(String uri) throws IOException {

        BufferedReader reader = null;
        HttpURLConnection connection = null;

        try {
            /** get connection with URL ---------------------*/
            URL url = new URL(uri);
            connection = (HttpURLConnection) url.openConnection();

            // read input
            StringBuilder sb = new StringBuilder();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            // get separate lines from reader ------------------
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();

            try {
                int status = connection.getResponseCode();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            return null;
        } finally {
            if (reader != null) {
                try {
                    // close connection !!! ---------------------
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
        }
    }
}