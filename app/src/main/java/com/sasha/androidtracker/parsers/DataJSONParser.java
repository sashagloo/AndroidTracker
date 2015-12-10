package com.sasha.androidtracker.parsers;

import com.sasha.androidtracker.model.GPSData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * DataJSONParser utility class
 * to parse a content to List<GPSData>
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

                    //TODO definition data fields from original Data Base
//                    data.setProductId(object.getInt("productId"));
//                    data.setName(object.getString("name"));
//                    data.setCategory(object.getString("category"));
//                    data.setInstructions(object.getString("instructions"));
//                    data.setPhoto(object.getString("photo"));
//                    data.setPrice(object.getDouble("price"));

                    dataList.add(data);
                }
                return dataList;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }

        }
    }
