package com.sasha.androidtracker.utils;

import android.os.AsyncTask;
import android.util.Log;

import com.sasha.androidtracker.model.GPSData;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.HTTP;
import org.json.JSONObject;

/**
 * Created by thijs on 17-12-15.
 */
public class SendData extends AsyncTask<GPSData, Void, HttpResponse>{

    @Override
    protected HttpResponse doInBackground(GPSData... gpsdata) {
        String url = "http://192.168.43.81:8080/RestApp/api/post";
        HttpResponse response = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("latitude", gpsdata[0].getLatitude());
            jsonObj.put("longitude", gpsdata[0].getLongitude());
            jsonObj.put("acceleroMeterX", gpsdata[0].getAccelerometerX());
            jsonObj.put("acceleroMeterY", gpsdata[0].getAccelerometerY());
            jsonObj.put("acceleroMeterZ", gpsdata[0].getAccelerometerZ());
            jsonObj.put("timestamp", gpsdata[0].getTimeStamp());

            HttpPost httpPost = new HttpPost(url);
            StringEntity entity = new StringEntity(jsonObj.toString(), HTTP.UTF_8);
            entity.setContentType("application/json");
            httpPost.setEntity(entity);
            HttpClient client = new DefaultHttpClient();
            response = client.execute(httpPost);
            // handle response here...
        }catch (Exception ex) {
            Log.d("POST", ex.toString());
        } finally {
            //httpClient.getConnectionManager().shutdown(); //Deprecated
        }
        return response;
    }

}
