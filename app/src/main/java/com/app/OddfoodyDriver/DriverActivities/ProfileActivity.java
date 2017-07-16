package com.app.OddfoodyDriver.DriverActivities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverResponseModel.ChangePassword;
import com.app.OddfoodyDriver.DriverResponseModel.DriverDetail;
import com.app.OddfoodyDriver.DriverResponseModel.DriverDetails;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
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
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.text.DateFormat;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 25-01-2017.
 */

public class ProfileActivity extends LocalizationActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {

    private TextInputLayout first_name_layout, sur_name_layout, contact_number_layout, address_layout;

    private EditText first_name_text, sur_name_text,
            contact_text, address_text;

    private Spinner Location_spinner;

    private Button profile_cancel, submit_button, prof_change_pass_btn;

    private Toolbar profiletoolbar;

    private RadioButton prof_male_radio_btn, prof_female_radio_btn, prof_available_radio_btn, prof_busy_radio_btn, prof_offline_radio_btn;


    private final int PLACE_AUTOCOMPLETE_REQUEST_CODE = 100;

    /**
     * Constant used in the location settings dialog.
     */
    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Exact. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";

    private static final String TAG = "ProfileActivity";


    /**
     * Provides the entry point to Google Play services.
     */
    private GoogleApiClient mGoogleApiClient;

    /**
     * Stores parameters for requests to the FusedLocationProviderApi.
     */
    private LocationRequest mLocationRequest;

    /**
     * Stores the types of location services the client is interested in using. Used for checking
     * settings to determine if the device has optimal location settings.
     */
    private LocationSettingsRequest mLocationSettingsRequest;

    /**
     * Represents a geographical location.
     */
    private Location mCurrentLocation;

    /**
     * Tracks the status of the location updates request. Value changes when the user presses the
     * Start Updates and Stop Updates buttons.
     */
    private Boolean mRequestingLocationUpdates;

    /**
     * Time when the location was updated represented as a String.
     */
    private String mLastUpdateTime;


    private boolean first_time_loc_update = true;
    private TextView mail_text;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profiletoolbar = (Toolbar) findViewById(R.id.profile_app_bar);
        profiletoolbar.setTitle(getResources().getString(R.string.nav_profile));
        setSupportActionBar(profiletoolbar);

        profiletoolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (LanguageSetting.getLanguage().equals("en")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        profiletoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        InitializeView();

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        // Kick off the process of building the GoogleApiClient, LocationRequest, and
        // LocationSettingsRequest objects.
        buildGoogleApiClient();

        createLocationRequest();

        buildLocationSettingsRequest();

        checkLocationSettings();
    }

    private void InitializeView() {

        first_name_layout = (TextInputLayout) findViewById(R.id.profile_input_layout_fn);
        sur_name_layout = (TextInputLayout) findViewById(R.id.profile_input_layout_sn);

        contact_number_layout = (TextInputLayout) findViewById(R.id.profile_input_layout_cn);
        address_layout = (TextInputLayout) findViewById(R.id.profile_input_layout_address);

        first_name_text = (EditText) findViewById(R.id.profile_input_fn);
        sur_name_text = (EditText) findViewById(R.id.profile_input_sn);
        mail_text = (TextView) findViewById(R.id.profile_input_email);

        contact_text = (EditText) findViewById(R.id.profile_input_cn);
        address_text = (EditText) findViewById(R.id.profile_input_address);

        profile_cancel = (Button) findViewById(R.id.prof_btn_cancel);
        submit_button = (Button) findViewById(R.id.btn_submit);
        prof_change_pass_btn = (Button) findViewById(R.id.prof_change_pass_btn);


        prof_female_radio_btn = (RadioButton) findViewById(R.id.prof_female_radio_btn);
        prof_male_radio_btn = (RadioButton) findViewById(R.id.prof_male_radio_btn);
        prof_available_radio_btn = (RadioButton) findViewById(R.id.prof_available_radio_btn);
        prof_busy_radio_btn = (RadioButton) findViewById(R.id.prof_busy_radio_btn);
        prof_offline_radio_btn = (RadioButton) findViewById(R.id.prof_offline_radio_btn);


        first_name_text.addTextChangedListener(new MyTextWatcher(first_name_text));
        sur_name_text.addTextChangedListener(new MyTextWatcher(sur_name_text));

        contact_text.addTextChangedListener(new MyTextWatcher(contact_text));


//        get driver detail

        getDriverDetail();

        //cancel action
        ProfileCancelClickActionCall();

        //Submit action
        SubmitClickActionCall();

//        change password button click

        changePasswordButtonClickEvent();


        LocationCallbackMethod();


    }

    private void changePasswordButtonClickEvent() {

        prof_change_pass_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ProfileActivity.this, ChangePasswordActivity.class));
            }
        });

    }

    private void getDriverDetail() {

        try {
            if (progressBarDialog != null)
                progressBarDialog.show();

            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.driver_details(loginPrefManager.getUserLoginDriverId(),
                    loginPrefManager.getUserLoginToken()).enqueue(new Callback<DriverDetails>() {
                @Override
                public void onResponse(Call<DriverDetails> call, Response<DriverDetails> response) {
                    try {
                        progressBarDialog.dismiss();
                        if (response.body().getResponse().getHttpCode() == 200) {
                            setValuesToProfile(response.body().getResponse().getDriverDetails().get(0));

                        } else if (response.body().getResponse().getHttpCode() == 400) {
                            showToast(response.body().getResponse().getMessage());
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<DriverDetails> call, Throwable t) {

                    progressBarDialog.dismiss();
                }
            });


        } catch (Exception e) {

        }
    }

    private void setValuesToProfile(DriverDetail driverDetail) {

        first_name_text.setText(driverDetail.getFirstName());
        sur_name_text.setText(driverDetail.getLastName());
        mail_text.setText(driverDetail.getEmail());
        contact_text.setText(driverDetail.getMobileNumber());
        address_text.setText(driverDetail.getAddress());

        if (driverDetail.getGender().equals("M")) {
            prof_male_radio_btn.setChecked(true);
        } else if (driverDetail.getGender().equals("F")) {
            prof_female_radio_btn.setChecked(true);
        }

        if (driverDetail.getDriverStatus() == 1) {
            prof_available_radio_btn.setChecked(true);
        } else if (driverDetail.getDriverStatus() == 2) {
            prof_busy_radio_btn.setChecked(true);
        } else {
            prof_offline_radio_btn.setChecked(true);
        }

        loginPrefManager.setStringValue("driver_curr_status", "" + driverDetail.getDriverStatus());

    }

    private void LocationCallbackMethod() {

        address_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {

                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).build(ProfileActivity.this);
                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                    Log.e("exception", e.getMessage());
                }
            }
        });
    }


    private void ProfileCancelClickActionCall() {
        profile_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void SubmitClickActionCall() {

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProfileDetails();
                hideKeyboard();
            }
        });
    }


    private void ValidateProfileDetails() {

        if (!validateFirstName() && !validateSurName() && !validateMobile()) {
            return;
        }

        if (!validateFirstName()) {
            return;
        }
        if (!validateSurName()) {
            return;
        }

        if (!validateMobile()) {
            return;
        }

        ProfileAPICall();

    }

    private String getGender() {
        if (prof_female_radio_btn.isChecked()) {
            return "F";
        } else
            return "M";
    }


    private String getDriverStatus() {

        if (prof_available_radio_btn.isChecked()) {
            return "1";
        } else if (prof_busy_radio_btn.isChecked()) {
            return "2";
        } else {
            return "3";
        }

    }

    private void ProfileAPICall() {
        //Api Call

        try {
            if (progressBarDialog != null)
                progressBarDialog.show();

            APIService apiService = Webdata.getRetrofit().create(APIService.class);

            apiService.update_profile(loginPrefManager.getStringValue("Lang_code"),
                    loginPrefManager.getUserLoginDriverId(), first_name_text.getText().toString().trim(),
                    sur_name_text.getText().toString().trim(), contact_text.getText().toString().trim(),
                    getGender(), mail_text.getText().toString().trim(), getDriverStatus(),
                    loginPrefManager.getUserLoginToken()).enqueue(new Callback<ChangePassword>() {
                @Override
                public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {

                    try {

                        progressBarDialog.dismiss();

                        if (response.body().getResponse().getHttpCode() == 200) {
                            showToast(response.body().getResponse().getMessage());

                            loginPrefManager.setStringValue("driver_curr_status", "" + getDriverStatus());

                        } else if (response.body().getResponse().getHttpCode() == 400) {
                            showToast(response.body().getResponse().getMessage());
                        }

                    } catch (Exception e) {

                    }

                }

                @Override
                public void onFailure(Call<ChangePassword> call, Throwable t) {

                    progressBarDialog.dismiss();
                }
            });

        } catch (Exception e) {

            progressBarDialog.dismiss();
        }

    }

    private void showToast(String message) {
        Toast.makeText(ProfileActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        Log.e(TAG, "Connected to GoogleApiClient");

        if (mCurrentLocation != null) {
            mCurrentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//            LocationUpdateMethod();
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
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e(TAG, "All location settings are satisfied.");
                startLocationUpdates();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" +
                        "upgrade location settings ");

                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(ProfileActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                Place place = PlaceAutocomplete.getPlace(this, data);


                Log.e("place", "" + place);
                Log.e("data", "Place: " + place.getAddress());
                Log.e("lat", "" + place.getLatLng().latitude);
                Log.e("lat", "" + place.getLatLng().longitude);


                address_text.setText(place.getAddress());

            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {

                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("data", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }

        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        showExitWarningDialog(ProfileActivity.this);
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }

    }


    /**
     * Requests location updates from the FusedLocationApi.
     */
    private void startLocationUpdates() {
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
            }
        });
    }

    //      Gps Location Cancel alert
    private void showExitWarningDialog(Context context) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context, R.style.MyDialogStyle);
        alertDialog.setTitle("" + getString(R.string.loc_aleart_content_txt));
        alertDialog.setPositiveButton("" + getApplicationContext().getString(R.string.ok_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                checkLocationSettings();
            }
        });
        alertDialog.setNegativeButton("" + getApplicationContext().getString(R.string.cancel_btn_txt),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.show();
    }

    @Override
    public void onLocationChanged(Location location) {
        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());
//
//        if(first_time_loc_update){
//            LocationUpdateMethod();
//            first_time_loc_update = false;
//            progressDialog.dismiss();
//        }

    }


    /**
     * Updates fields based on data stored in the bundle.
     *
     * @param savedInstanceState The activity state saved in the Bundle.
     */
    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Update the value of mRequestingLocationUpdates from the Bundle, and make sure that
            // the Start Updates and Stop Updates buttons are correctly enabled or disabled.
            if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
                mRequestingLocationUpdates = savedInstanceState.getBoolean(
                        KEY_REQUESTING_LOCATION_UPDATES);
            }

            // Update the value of mCurrentLocation from the Bundle and update the UI to show the
            // correct latitude and longitude.
            if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
                // Since KEY_LOCATION was found in the Bundle, we can be sure that mCurrentLocation
                // is not null.
                mCurrentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            }

            // Update the value of mLastUpdateTime from the Bundle and update the UI.
            if (savedInstanceState.keySet().contains(KEY_LAST_UPDATED_TIME_STRING)) {
                mLastUpdateTime = savedInstanceState.getString(KEY_LAST_UPDATED_TIME_STRING);
            }
        }
    }

    /**
     * Builds a GoogleApiClient. Uses the {@code #addApi} method to request the
     * LocationServices API.
     */
    private synchronized void buildGoogleApiClient() {
        Log.e(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    /**
     * Sets up the location request. Android has two location request settings:
     * {@code ACCESS_COARSE_LOCATION} and {@code ACCESS_FINE_LOCATION}. These settings control
     * the accuracy of the current location. This sample uses ACCESS_FINE_LOCATION, as defined in
     * the AndroidManifest.xml.
     * <p/>
     * When the ACCESS_FINE_LOCATION setting is specified, combined with a fast update
     * interval (5 seconds), the Fused Location Provider API returns location updates that are
     * accurate to within a few feet.
     * <p/>
     * These settings are appropriate for mapping applications that show real-time location
     * updates.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();

        // Sets the desired interval for active location updates. This interval is
        // inexact. You may not receive updates at all if no location sources are available, or
        // you may receive them slower than requested. You may also receive updates faster than
        // requested if other applications are requesting location at a faster interval.
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);

        // Sets the fastest rate for active location updates. This interval is exact, and your
        // application will never receive updates faster than this value.
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);

        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    /**
     * Check if the device's location settings are adequate for the app's needs using the
     * {@link com.google.android.gms.location.SettingsApi#checkLocationSettings(GoogleApiClient,
     * LocationSettingsRequest)} method, with the results provided through a {@code PendingResult}.
     */
    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    /**
     * Removes location updates from the FusedLocationApi.
     */
    private void stopLocationUpdates() {
        // It is a good practice to remove location requests when the activity is in a paused or
        // stopped state. Doing so helps battery performance and is especially
        // recommended in applications that request frequent location updates.
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {

            switch (view.getId()) {
                case R.id.profile_input_fn:
                    validateFirstName();
                    break;
                case R.id.profile_input_sn:
                    validateSurName();
                    break;
                case R.id.profile_input_cn:
                    validateMobile();
                    break;


            }
        }

    }

    private boolean validateFirstName() {
        if (first_name_text.getText().toString().trim().isEmpty()) {
            first_name_layout.setError(getString(R.string.err_msg_name));
            requestFocus(first_name_text);
            return false;
        } else {
            first_name_layout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateSurName() {
        String surname = sur_name_text.getText().toString().trim();
        if (surname.isEmpty()) {
            sur_name_layout.setError(getString(R.string.err_msg_sur_name));
            requestFocus(sur_name_text);
            return false;
        } else {
            sur_name_layout.setErrorEnabled(false);
        }

        return true;
    }


//    private boolean validateEmail() {
//        String email = mail_text.getText().toString().trim();
//
//        if (email.isEmpty() || !isValidEmail(email)) {
//            mail_layout.setError(getString(R.string.error_msg_Email));
//            requestFocus(mail_text);
//            return false;
//        } else {
//            mail_layout.setErrorEnabled(false);
//        }
//
//        return true;
//    }

    private boolean validateMobile() {
        if (contact_text.getText().toString().trim().isEmpty()) {
            contact_number_layout.setError(getString(R.string.err_msg_mobile));
            requestFocus(contact_text);
            return false;
        } else if (contact_text.getText().toString().length() < 10) {
            contact_number_layout.setError(getString(R.string.err_msg_mobile));
            requestFocus(contact_text);
            return false;
        } else {
            contact_number_layout.setErrorEnabled(false);
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Stop location updates to save battery, but don't disconnect the GoogleApiClient object.
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


}
