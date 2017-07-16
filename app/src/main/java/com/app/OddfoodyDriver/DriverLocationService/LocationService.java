package com.app.OddfoodyDriver.DriverLocationService;

import android.Manifest;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.app.OddfoodyDriver.DriverResponseModel.DrivLocRequestMethod;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by NEXTBRAIN on 2/27/2017.
 */

public class LocationService extends Service implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {


    private LoginPrefManager loginPrefManager;

    private final String TAG = "LocationService";

    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;
    private final int REQUEST_CHECK_SETTINGS = 0x1;

    private final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    private final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";


    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;
    private Location mCurrentLocation;

    private Boolean mRequestingLocationUpdates;
    private String mLastUpdateTime;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

//        Log.e("LocationService", "onStartCommand");

        loginPrefManager = new LoginPrefManager(getApplicationContext());

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";

        if (intent != null) {
            updateValuesFromBundle(intent.getBundleExtra("LocBundle"));
        }

        buildGoogleApiClient();

        createLocationRequest();

        buildLocationSettingsRequest();

        checkLocationSettings();

        return Service.START_STICKY;
    }


    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {

            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(KEY_REQUESTING_LOCATION_UPDATES);
            }

            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    private synchronized void buildGoogleApiClient() {
//        Log.e(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }


    private void startLocationUpdates() {
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
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                mRequestingLocationUpdates = true;
            }
        });
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

        LocationUpdateMethod("" + mCurrentLocation.getLatitude(), "" + mCurrentLocation.getLongitude());

    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

//        Log.e(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation != null) {
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
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
        }

    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
        stopSelf();
    }

    private void LocationUpdateMethod(String Latitude, String Longitude) {

        if (!NetworkStatus.isConnectingToInternet(getApplicationContext())) {
            return;
        }


        if (loginPrefManager.getStringValue("driver_curr_status").equals("3")) {
            return;
        }


        try {

            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.update_driver_location("" + loginPrefManager.getStringValue("Lang_code"),
                    "" + loginPrefManager.getUserLoginDriverId(), "" + loginPrefManager.getUserLoginToken(),
                    "" + Latitude, "" + Longitude, "" + loginPrefManager.getStringValue("device_id"),
                    "" + loginPrefManager.getStringValue("device_token"), "2").enqueue(new Callback<DrivLocRequestMethod>() {
                @Override
                public void onResponse(Call<DrivLocRequestMethod> call, Response<DrivLocRequestMethod> response) {

                    try {
                        if (response.body().getResponse().getHttpCode() == 200) {
//                            Log.e("onResponse  Sucess", "" + response.body().getResponse().getMessage());
                        }
                    } catch (Exception e) {
                        Log.e("onResponse Exception", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<DrivLocRequestMethod> call, Throwable throwable) {
                    Log.e("onFailure", "" + throwable.getMessage());
                }
            });

        } catch (Exception e) {
            Log.e("try Exception", "-" + e.getMessage());
        }

    }

}
