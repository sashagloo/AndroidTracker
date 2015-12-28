package com.sasha.androidtracker.utils;

import android.os.AsyncTask;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * Created by thijs on 15-12-15.
 */
public class TestPost extends AsyncTask<Void, Void, Void>{


    @Override
    protected Void doInBackground(Void... Void) {
        String url = "http://192.168.43.81:8080/RestApp/api/post";

        HttpClient httpClient = new DefaultHttpClient();

        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("latitude", "1");
            jsonObj.put("longitude", "1");
            jsonObj.put("acceleroMeterX", "2");
            jsonObj.put("acceleroMeterY", "2");
            jsonObj.put("acceleroMeterZ", "2");
            jsonObj.put("timestamp", "12/12/12 12:12:12");

            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response = client.execute(httpPost);
            // handle response here...
        }catch (Exception ex) {
            // handle exception here
        } finally {
            httpClient.getConnectionManager().shutdown(); //Deprecated
        }
        return null;
    }


}

