package com.sasha.androidtracker;

import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sasha.androidtracker.model.GPSData;

public class GoogleMapsActivity extends FragmentActivity implements OnMapReadyCallback{

    private GoogleMap mMap;
    protected GPSData data;
    private Marker marker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_maps);

        Bundle b = getIntent().getExtras();
        data = b.getParcelable(MainActivity.GPSDATA_BUNDLE);

        // Toast.makeText(this, "selected data : " + data.getTimeStamp(), Toast.LENGTH_LONG).show();

        /** Obtain the SupportMapFragment and get notified when the map is ready to be used. */
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        // if API level is >=23 we need to ask permission from the user
        if (Build.VERSION.SDK_INT >= 23 &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        if ( mMap != null ) {
            // Setting a custom info window adapter for the google map
            mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        }

        mMap.setMyLocationEnabled(true);

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setCompassEnabled(true);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setRotateGesturesEnabled(true);
        mMap.getUiSettings().setScrollGesturesEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);

        // Add a marker in Sydney and move the camera
        LatLng currentLocation = new LatLng(GoogleMapsActivity.this.data.getLatitude(),
                                            GoogleMapsActivity.this.data.getLongitude());

        //Toast.makeText(this, "Marker on : " + GoogleMapsActivity.this.data.getTimeStamp(), Toast.LENGTH_LONG).show();

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation, 18));

        // Setting a custom info window adapter for the google map
        mMap.setInfoWindowAdapter(new MyInfoWindowAdapter());
        mMap.addMarker(new MarkerOptions()
                        .position(currentLocation)
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ROSE))
                        .visible(true)
        );


        mMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(final Marker marker) {
                if (marker.isInfoWindowShown()) {
                    marker.hideInfoWindow();
                } else {

                    /** This causes the marker  to bounce into position when it is clicked. */
                    final Handler handler = new Handler();
                    final long start = SystemClock.uptimeMillis();
                    final long duration = 1500;

                    final Interpolator interpolator = new BounceInterpolator();

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            long elapsed = SystemClock.uptimeMillis() - start;
                            float t = Math.max(
                                    1 - interpolator.getInterpolation((float) elapsed / duration), 0);
                            marker.setAnchor(0.5f, 1.0f + 2 * t);

                            if (t > 0.0) {
                                // Post again 16ms later.
                                handler.postDelayed(this, 16);
                            }
                        }
                    });

                    marker.showInfoWindow();
                }
                return true;
            }
        });
    }

    /**
     * inner class MyInfoWindowAdapter
     * to define custom info window contents
     */
    private class MyInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {


        // Use default InfoWindow frame
        @Override
        public View getInfoContents(Marker marker) {

            return null;
        }

        /**
         * This method defines the contents of the InfoWindow ---------------------------------
         * @param marker Marker
         */

        @Override
        public View getInfoWindow(final Marker marker) {

            // Getting view from the layout file info_window_layout
            View v = getLayoutInflater().inflate(R.layout.map_marker_layout, null);

            // Getting reference to the TextView and set timeStamp
            TextView timeStamp = (TextView) v.findViewById(R.id.tv_TimeStamp);
            timeStamp.setText("Date : " + GoogleMapsActivity.this.data.getTimeStamp());

            // Getting reference to the TextView and set latitude
            TextView tvLat = (TextView) v.findViewById(R.id.tv_Latitude);
            tvLat.setText("Lat : " + GoogleMapsActivity.this.data.getLatitude());

            // Getting reference to the TextView and set longitude
            TextView tvLng = (TextView) v.findViewById(R.id.tv_Longitude);
            tvLng.setText("Lng : " + GoogleMapsActivity.this.data.getLongitude());

            // Getting reference to the TextView and set X from accelerometer
            TextView x = (TextView) v.findViewById(R.id.tv_X);
            x.setText("X : " + GoogleMapsActivity.this.data.getAccelerometerX());

            // Getting reference to the TextView and set Y from accelerometer
            TextView y = (TextView) v.findViewById(R.id.tv_Y);
            y.setText("Y : " + GoogleMapsActivity.this.data.getAccelerometerY());

            // Getting reference to the TextView and set Z from accelerometer
            TextView z = (TextView) v.findViewById(R.id.tv_Z);
            z.setText("Z : " + GoogleMapsActivity.this.data.getAccelerometerZ());

            return v;
        }
    }
}
