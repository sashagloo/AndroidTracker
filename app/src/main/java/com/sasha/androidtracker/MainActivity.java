package com.sasha.androidtracker;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.sasha.androidtracker.adaptor.GPSDataAdapter;
import com.sasha.androidtracker.model.GPSData;
import com.sasha.androidtracker.parsers.DataJSONParser;
import com.sasha.androidtracker.utils.RequestPackage;

import static com.sasha.androidtracker.utils.HTTPManager.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    LocationManager locationManager;
    LocationListener locationListener;
    Location location;
    AndroidAccelerometer accelerometer;
    Vibrator vibrator;
    ProgressBar progressBar;

    List<GPSData> dataList;
    Timer timer;
    MyTimerTask myTimerTask;
    List<MyLoadTask> myLoadTasks;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        progressBar = (ProgressBar) findViewById(R.id.progressBar1);
        progressBar.setVisibility(View.INVISIBLE);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        FloatingActionButton btn1 = (FloatingActionButton) findViewById(R.id.btn1);
        FloatingActionButton btn2 = (FloatingActionButton) findViewById(R.id.btn2);

        if (timer != null) {
            timer.cancel();
        }

        myLoadTasks = new ArrayList<>();

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                if (location != null) {
                    MainActivity.this.location = location;
                    Log.i("LOCATION", "" + location.getLatitude() + " : " +
                            location.getLongitude());
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            public void onProviderEnabled(String provider) {
            }

            public void onProviderDisabled(String provider) {
                Toast.makeText(MainActivity.this, "GPS/Use Wireless network is not enabled", Toast.LENGTH_SHORT).show();
            }
        };

        // Register the listener with the Location Manager to receive location updates
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // if API level is >=23 we need to ask permission from the user
                if (Build.VERSION.SDK_INT >= 23 &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                        ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return;
                }

                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

                accelerometer = new AndroidAccelerometer(MainActivity.this);
                accelerometer.onResume();

                if (timer != null) {
                    timer.cancel();
                } else {
                    timer = new Timer();
                    myTimerTask = new MyTimerTask();
                    //delay 1000ms, repeat in 5000ms
                    timer.schedule(myTimerTask, 1000, 5000);
                    vibrator.vibrate(1000);
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timer != null) {
                    locationManager.removeUpdates(locationListener);
                    timer.cancel();
                    timer = null;

                    accelerometer.onPause();
                    vibrator.vibrate(1000);
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.action_get_data) {
            // if is it network connection > request data -------------------
            //TODO define URL to connect RESTfull services
            if (isOnLine()) requestData(
                    "GET",
                    "http://services.hanselandpetal.com/secure/flowers.json"
            );
            else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }
        }
        if (item.getItemId() == R.id.action_send_data) {
            // if is it network connection > request data -------------------
            //TODO define URL to connect RESTfull services
            if (isOnLine()) requestData(
                    "POST",
                    "http://services.hanselandpetal.com/secure/flowers.json"
            );
            else {
                Toast.makeText(this, "Network isn't available", Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }

    /**
     * This method instantiate GPSDataAdapter and refresh content to display ----------------
     *
     * @param
     */
    protected View refreshDisplay() {

        GPSDataAdapter adapter = new GPSDataAdapter(this, dataList);
        ListView listView = (ListView) findViewById(android.R.id.list);
        listView.setAdapter(adapter);
        return listView;
    }

    protected void updateDataList() {

        GPSData data = new GPSData();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");

        data.setAccelerometerX(String.valueOf(accelerometer.lastY));
        data.setAccelerometerY(String.valueOf(accelerometer.lastY));
        data.setAccelerometerZ(String.valueOf(accelerometer.lastZ));
        data.setTimeStamp(String.valueOf(formater.format(new Date())));
        data.setLatitude(String.valueOf(location.getLatitude()));
        data.setLongitude(String.valueOf(location.getLongitude()));

        dataList.add(data);
    }

    /**
     * MyTimerTask inner class  ----------------------------------------------------------------
     * repeat running MainActivity class methods at defined delay
     */
    class MyTimerTask extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (MainActivity.this.location != null) {
                        MainActivity.this.updateDataList();
                        MainActivity.this.refreshDisplay();
                    }
                }
            });
        }
    }


/**
 * *****************************************************************************************
 *  RESTfull services
 * *****************************************************************************************
 */


    /**
     * This method checks network connectivity -----------------------------------------
     *
     * @param
     * @see android.net.ConnectivityManager
     * @see android.net.NetworkInfo
     */
    protected boolean isOnLine() {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
            Toast.makeText(MainActivity.this,
                    "Connected to the network",
                    Toast.LENGTH_LONG).show();
            return true;
        } else {
            return false;
        }
    }

    /**
     * This method instantiate RequestPackage class
     * and starts it in MyTask
     *
     * @param uri
     * @see com.sasha.androidtracker.MainActivity.MyLoadTask
     */

    private void requestData(String method, String uri) {

        MyLoadTask task = new MyLoadTask();
        RequestPackage requestPackage = new RequestPackage();
        requestPackage.setMethod(method);
        requestPackage.setUri(uri);

        if (method.equals("POST")) {
            if (dataList.size() > 0) {
                for (GPSData data : dataList) {
                    requestPackage.setParams("accelerometerX", data.getAccelerometerX());
                    requestPackage.setParams("accelerometerY", data.getAccelerometerY());
                    requestPackage.setParams("accelerometerZ", data.getAccelerometerZ());
                    requestPackage.setParams("timeStamp", data.getTimeStamp());
                    requestPackage.setParams("latitude", data.getLatitude());
                    requestPackage.setParams("longitude", data.getLongitude());

                    task.execute(requestPackage);
                }
            }
        } else if (method.equals("GET")) task.execute(requestPackage);


    }

    /**
     * inner  AsyncTask class  -----------------------------------------------
     * (Android-specific background threads manager)
     * to load data from source asynchronously
     * work in the background and control the foreground at the same time
     */
    private class MyLoadTask extends AsyncTask<RequestPackage, String, List<GPSData>> {

        /**
         * executed before do in background
         * has access to the main thread
         */
        @Override
        protected void onPreExecute() {

            if (myLoadTasks.size() == 0) {
                progressBar.setVisibility(View.VISIBLE);
            }
            myLoadTasks.add(this);
        }

        /**
         * This method can call {@link #publishProgress} to publish updates
         * on the UI thread.
         *
         * @param params The parameters of the task.
         * @return A result, defined by the subclass of this task.
         * @see #onPreExecute()
         * @see #onPostExecute
         * @see #publishProgress
         */
        @Override
        protected List<GPSData> doInBackground(RequestPackage... params) {

            try {
                /**  get data from HTTPManager ---------------------------*/

                if (params[0].getMethod().equals("GET")) {
                    String content = getData(params[0]);
                    MainActivity.this.dataList = DataJSONParser.parseFeed(content);
                }
                if (params[0].getMethod().equals("POST")) {
                    String content = getData(params[0]);
                    MainActivity.this.dataList = DataJSONParser.parseFeed(content);
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
            return MainActivity.this.dataList;
        }


        /**
         * This method receives a result from doInBackground()
         * has access to the main thread
         * executes automatically
         *
         * @param result
         */
        @Override
        protected void onPostExecute(List<GPSData> result) {

            myLoadTasks.remove(this);
            if (myLoadTasks.size() == 0) {
                progressBar.setVisibility(View.INVISIBLE);
            }
            if (result == null) {
                Toast.makeText(MainActivity.this,
                        "Web service not available",
                        Toast.LENGTH_LONG).show();
                return;
            }
            MainActivity.this.dataList = result;

            refreshDisplay();
        }

//		@Override
//		protected void onProgressUpdate(String... values) {
//			updateDisplay(values[0]);
//		}
    }
}
