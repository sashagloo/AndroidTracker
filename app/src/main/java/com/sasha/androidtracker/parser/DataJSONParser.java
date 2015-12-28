package com.sasha.androidtracker.parser;

import com.sasha.androidtracker.model.GPSData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * DataJSONParser utility class
 * to parse a content from JSON array to List<GPSData>
 * Project : AndroidTracker
 *
 * @autor Sasha
 * @since 9-12-2015
 */
public class DataJSONParser {

    public static List<GPSData> parseFeed(String content) {

        try {
            JSONArray array = new JSONArray(content);
            List<GPSData> dataList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {

                JSONObject object = array.getJSONObject(i);
                GPSData data = new GPSData();

                data.setTimeStamp(object.getString("timeStamp"));
                data.setLatitude(object.getDouble("latitude"));
                data.setLongitude(object.getDouble("longitude"));
                data.setAccelerometerX((float) object.getDouble("accelerometerX"));
                data.setAccelerometerY((float) object.getDouble("accelerometerY"));
                data.setAccelerometerZ((float) object.getDouble("accelerometerZ"));

                dataList.add(data);
            }
            return dataList;

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

    }
}
