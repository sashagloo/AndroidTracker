package com.sasha.androidtracker.utils;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings;
import android.util.Base64;
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

    private Context context;

    public SendData(Context context){
        this.context = context;
    }

    @Override
    protected HttpResponse doInBackground(GPSData... gpsdata) {
        String url = "http://192.168.43.81:8080/RestApp/api/post";
        HttpResponse response = null;
        HttpClient httpClient = new DefaultHttpClient();
        try {
            JSONObject jsonObj = new JSONObject();

            // encrypt data using public key before sending
            // the encrypted byte array is encoded to a base64 string so it can be put in a json object
            EncryptionHelper encryptor = new EncryptionHelper(this.context);

            String latitude = Base64.encodeToString(encryptor.testEncrypt(
                            String.valueOf(gpsdata[0].getLatitude())), Base64.DEFAULT);
            String longitude = Base64.encodeToString(encryptor.testEncrypt(
                            String.valueOf(gpsdata[0].getLongitude())), Base64.DEFAULT);
            String acceleroMeterX = Base64.encodeToString(encryptor.testEncrypt(
                            String.valueOf(gpsdata[0].getAccelerometerX())), Base64.DEFAULT);
            String acceleroMeterY = Base64.encodeToString(encryptor.testEncrypt(
                            String.valueOf(gpsdata[0].getAccelerometerY())), Base64.DEFAULT);
            String acceleroMeterZ = Base64.encodeToString(encryptor.testEncrypt(
                            String.valueOf(gpsdata[0].getAccelerometerZ())), Base64.DEFAULT);
            String timestamp = Base64.encodeToString(encryptor.testEncrypt(
                            String.valueOf(gpsdata[0].getTimeStamp())), Base64.DEFAULT);

            // fill json object with encrypted data strings
            jsonObj.put("latitude", latitude);
            jsonObj.put("longitude", longitude);
            jsonObj.put("acceleroMeterX", acceleroMeterX);
            jsonObj.put("acceleroMeterY", acceleroMeterY);
            jsonObj.put("acceleroMeterZ", acceleroMeterZ);
            jsonObj.put("timestamp", timestamp);

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
