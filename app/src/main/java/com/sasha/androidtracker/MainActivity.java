package com.sasha.androidtracker;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.sasha.androidtracker.adaptor.GPSDataAdapter;
import com.sasha.androidtracker.db.GPSDataSource;
import com.sasha.androidtracker.model.GPSData;
<<<<<<< HEAD
import com.sasha.androidtracker.utility.AndroidAccelerometer;

=======
import com.sasha.androidtracker.parsers.DataJSONParser;
import com.sasha.androidtracker.utils.RequestPackage;
import com.sasha.androidtracker.utils.SendData;
import static com.sasha.androidtracker.utils.HTTPManager.*;
>>>>>>> origin/dev
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * MainActivity class
 *  (main) launcher class of the app
 *  start data base connection, instantiate sensors listeners, display current data
 *
 * @see AppCompatActivity, AndroidAccelerometer, LocationManager, LocationListener, Vibrator, GPSDataSource,
 *      GPSData, Timer, TimerTask
 * @author Sasha Antipin
 * @version 0.9
 * @since 29-11-2015
 */

public class MainActivity extends AppCompatActivity {

    private LocationManager locationManager;
    private LocationListener locationListener;
    private Location location;
    private AndroidAccelerometer accelerometer;
    private Vibrator vibrator;
    private Intent intent;

    private GPSDataSource dataSource;
    private List<GPSData> dataList;
    private Timer timer;
    private MyTimerTask myTimerTask;

    public static final String GPSDATA_BUNDLE = "GPSDATA_BUNDLE";
    public static final String LOGTAG = "TRACKER";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        final Context context = getApplicationContext();

        /** get connection with the database en open it -------------------------*/
        dataSource = new GPSDataSource(this);
        dataSource.open();

        Log.i(LOGTAG, "database connected");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        vibrator = (Vibrator) this.getSystemService(Context.VIBRATOR_SERVICE);

        FloatingActionButton btn1 = (FloatingActionButton) findViewById(R.id.btn1);
        FloatingActionButton btn2 = (FloatingActionButton) findViewById(R.id.btn2);

        if (timer != null) {
            timer.cancel();
        }

        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {

                if (location != null) {
                    MainActivity.this.location = location;
                    Log.i("LOCATION", "" + location.getLatitude() + " : " +
                            location.getLongitude());
                }
            }

            public void onStatusChanged(String provider, int status, Bundle extras) { }

            public void onProviderEnabled(String provider) { }

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
                    //timer.schedule(myTimerTask, 1000, 5000);
                    timer.schedule(new TimerSendData(), 1000, (1000 * 30));
                    vibrator.vibrate(1000);

                    Snackbar.make(v, "The launch of data registration", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (timer != null) {
                    // if API level is >=23 we need to ask permission from the user
                    if (Build.VERSION.SDK_INT >= 23 &&
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                            ContextCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        return;
                    }
<<<<<<< HEAD

=======
>>>>>>> origin/dev
                    locationManager.removeUpdates(locationListener);
                    timer.cancel();
                    timer = null;

                    accelerometer.onPause();
                    vibrator.vibrate(1000);

                    Snackbar.make(v, "Data registration is stopped", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            }
        });

        refreshDisplay();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.menu_clear:
                dataSource.clearData();

                Toast.makeText(this, "Data has been DELETED", Toast.LENGTH_LONG).show();

                refreshDisplay();
                break;

            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This method instantiate GPSDataAdapter and refresh content to display ----------------
     */
    protected View refreshDisplay() {

        dataList = dataSource.findAll();
        intent = new Intent(this, GoogleMapsActivity.class);

<<<<<<< HEAD
        Log.i(LOGTAG, "database findAll");
=======
        data.setAccelerometerX(String.valueOf(accelerometer.lastY));
        data.setAccelerometerY(String.valueOf(accelerometer.lastY));
        data.setAccelerometerZ(String.valueOf(accelerometer.lastZ));
        data.setTimeStamp(String.valueOf(formater.format(new Date())));
        data.setLatitude(String.valueOf(location.getLatitude()));
        data.setLongitude(String.valueOf(location.getLongitude()));

        dataList.add(data);
    }

    protected GPSData getGpsData() {
        GPSData data = new GPSData();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");

        data.setAccelerometerX(String.valueOf(accelerometer.lastY));
        data.setAccelerometerY(String.valueOf(accelerometer.lastY));
        data.setAccelerometerZ(String.valueOf(accelerometer.lastZ));
        data.setTimeStamp(String.valueOf(formater.format(new Date())));
        data.setLatitude(String.valueOf(location.getLatitude()));
        data.setLongitude(String.valueOf(location.getLongitude()));

        return data;
    }

    /**
     * MyTimerTask inner class  ----------------------------------------------------------------
     * repeat running MainActivity class methods at defined delay
     */
    class MyTimerTask extends TimerTask {
>>>>>>> origin/dev

        if (dataList.size() > 0 ) {
            GPSDataAdapter adapter = new GPSDataAdapter(this, dataList);
            ListView listView = (ListView) findViewById(android.R.id.list);
            listView.setAdapter(adapter);

            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    GPSData data = dataList.get(position);
                    intent.putExtra(MainActivity.this.GPSDATA_BUNDLE, data);
                            startActivity(intent);
                }
            });

            return listView;
        }
        return null;
    }

<<<<<<< HEAD
    /**
     * This method add data item to the List<GPSData> dataList
     */
    protected void updateDataList() {
=======
    class TimerSendData extends TimerTask {

        @Override
        public void run() {
            runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    if (MainActivity.this.location != null) {
                        SendData sendData = new SendData(getApplicationContext());
                        GPSData[] dataArray = new GPSData[1];
                        GPSData data = MainActivity.this.getGpsData();
                        dataArray[0] = data;
                        sendData.execute(dataArray);
                    }
                }
            });
        }
    }

>>>>>>> origin/dev

        GPSData data = new GPSData();
        SimpleDateFormat formater = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss");

        data.setTimeStamp(formater.format(new Date()));
        data.setAccelerometerX(accelerometer.lastX);
        data.setAccelerometerY(accelerometer.lastY);
        data.setAccelerometerZ(accelerometer.lastZ);
        data.setLatitude(location.getLatitude());
        data.setLongitude(location.getLongitude());

        dataSource.create(data);
    }

    /**
     * Methode opens dataSource
     * is called explicitly as the activity comes to the screen
     * (automatically called by Android during the life cycle of the activity)
     * The connection object within any activity is cached. You can call open() as
     * many times as you will within one activity!!!
     * @param
     */
    @Override
    protected void onResume() {
        super.onResume();
        dataSource.open();
    }

    /**
     * Methode close the data source connection
     * whenever the activity pauses (as the activity closes down)
     * don't forget close() the connection whenever the activity is going away!!!
     * @param
     */
    @Override
    protected void onPause() {
        super.onPause();
        dataSource.close();
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
