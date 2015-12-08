package com.sasha.androidtracker;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import com.sasha.androidtracker.adaptor.GPSDataAdapter;
import com.sasha.androidtracker.model.GPSData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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

    List<GPSData> dataList;
    Timer timer;
    MyTimerTask myTimerTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        FloatingActionButton btn1 = (FloatingActionButton) findViewById(R.id.btn1);
        FloatingActionButton btn2 = (FloatingActionButton) findViewById(R.id.btn2);

        if (timer != null) {
            timer.cancel();
        }

        dataList = new ArrayList<>();
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
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            // Remove the listener you previously added
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        data.setTimeStamp(accelerometer.lastX + ":"
                + accelerometer.lastY + ":"
                + accelerometer.lastZ + ":"
                + " \n " + String.valueOf(formater.format(new Date())));
        data.setLatitude(String.valueOf(location.getLatitude()));
        data.setLongitude(String.valueOf(location.getLongitude()));

        dataList.add(data);

    }

    /**
     * MyTimerTask inner class
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
}
