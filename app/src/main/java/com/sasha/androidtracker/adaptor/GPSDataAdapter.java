package com.sasha.androidtracker.adaptor;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sasha.androidtracker.model.GPSData;
import com.sasha.androidtracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * GPSDataAdapter class
 * responsible to present GPSData objects in particular View's
 * designed to work with GPSData class
 * uses AsyncTask to optimize data loading
 * @see ArrayAdapter, AsyncTask
 * @author Sasha Antipin
 * @version 0.1
 * @since 29-11-2015
 */

public class GPSDataAdapter extends ArrayAdapter<GPSData> {

    private Context context;
    private List<GPSData> dataList;

    /** Least Recently Used caching (Map object) ----
     *  use it to cache GPS data
     * @key Integer (productId)
     * @value Bitmap
     * */
    private LruCache<Integer, ArrayList<GPSData>> dataCache;

    /**
     * Constructor
     * to instantiate a GPSDataAdapter object
     * @param context identifier of present Activity
     * @param objects List of objects to display
     */
    public GPSDataAdapter(Context context, List<GPSData> objects) {
        super(context, R.layout.data_item, objects);
        this.context = context;
        this.dataList = objects;

        /** set cache size ------*/
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024) ; // total amount of available memory
        final int cacheSize = maxMemory / 8;

        /** create image cache ---*/
        dataCache = new LruCache(cacheSize);
    }

    /**
     * Method to instantiate views en set content in it to display
     * This method override default super.getView() method
     *
     * @param position id of the layout source
     * @param convertView View to display content
     * @param parent View group from parent to inflate result in it
     * @return view with particular properties
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater =
                (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.data_item, parent, false);

        GPSData data = dataList.get(position);

        /** Display timeStamp of data in the TextView widget */
        TextView timeStamp = (TextView) view.findViewById(R.id.timeStamp);
        timeStamp.setText(data.getTimeStamp());

        /** Display Latitude from data in the TextView widget */
        TextView tvLatitude = (TextView) view.findViewById(R.id.tvLatitude);
        tvLatitude.setText(data.getLatitude());

        /** Display Longitude from data in the TextView widget */
        TextView tvLongitude = (TextView) view.findViewById(R.id.tvLongitude);
        tvLongitude.setText(data.getLongitude());


        return view;
    }
}

