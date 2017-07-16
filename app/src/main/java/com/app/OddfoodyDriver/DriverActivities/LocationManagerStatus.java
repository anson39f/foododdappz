package com.app.OddfoodyDriver.DriverActivities;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;

import android.location.LocationProvider;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.app.OddfoodyDriver.R;

/**
 * Created by test on 2/23/2017.
 */

public class LocationManagerStatus extends Activity {

    private LocationManager locationManager;
    private TextView textView;

    private final LocationListener gpsLocationListener = new LocationListener() {

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            final String tvTxt = textView.getText().toString();
            switch (status) {
                case LocationProvider.AVAILABLE:
                    textView.setText(tvTxt + "GPS available again\n");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    textView.setText(tvTxt + "GPS out of service\n");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    textView.setText(tvTxt + "GPS temporarily unavailable\n");
                    break;
            }
        }

        @Override
        public void onProviderEnabled(String provider) {
            textView.setText(textView.getText().toString()
                    + "GPS Provider Enabled\n");
        }

        @Override
        public void onProviderDisabled(String provider) {
            textView.setText(textView.getText().toString()
                    + "GPS Provider Disabled\n");
        }

        @Override
        public void onLocationChanged(Location location) {
            if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            locationManager.removeUpdates(networkLocationListener);
            textView.setText(textView.getText().toString()
                    + "New GPS location: "
                    + String.format("%9.6f", location.getLatitude()) + ", "
                    + String.format("%9.6f", location.getLongitude()) + "\n");
        }
    };
    private final LocationListener networkLocationListener =
            new LocationListener() {

                @Override
                public void onStatusChanged(String provider, int status, Bundle extras) {
                    final String tvTxt = textView.getText().toString();
                    switch (status) {
                        case LocationProvider.AVAILABLE:
                            textView.setText(tvTxt + "Network location available again\n");
                            break;
                        case LocationProvider.OUT_OF_SERVICE:
                            textView.setText(tvTxt + "Network location out of service\n");
                            break;
                        case LocationProvider.TEMPORARILY_UNAVAILABLE:
                            textView.setText(tvTxt
                                    + "Network location temporarily unavailable\n");
                            break;
                    }
                }

                @Override
                public void onProviderEnabled(String provider) {
                    textView.setText(textView.getText().toString()
                            + "Network Provider Enabled\n");
                }

                @Override
                public void onProviderDisabled(String provider) {
                    textView.setText(textView.getText().toString()
                            + "Network Provider Disabled\n");
                }

                @Override
                public void onLocationChanged(Location location) {
                    textView.setText(textView.getText().toString()
                            + "New network location: "
                            + String.format("%9.6f", location.getLatitude()) + ", "
                            + String.format("%9.6f", location.getLongitude()) + "\n");
                }
            };

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        textView = (TextView) findViewById(R.id.textview);
        locationManager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(
                LocationManager.NETWORK_PROVIDER, 5000, 0,
                networkLocationListener);
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                3000, 0, gpsLocationListener);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.removeUpdates(networkLocationListener);
        locationManager.removeUpdates(gpsLocationListener);
    }
}
