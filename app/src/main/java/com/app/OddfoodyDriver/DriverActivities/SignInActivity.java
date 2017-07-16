package com.app.OddfoodyDriver.DriverActivities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverResponseModel.SignIn;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
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
 * Created by Next Brain on 23-01-2017.
 */

public class SignInActivity extends LocalizationActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener, ResultCallback<LocationSettingsResult> {


    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static final String TAG = "OrdersListsActivity";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;

    private Boolean mRequestingLocationUpdates;
    private Location mCurrentLocation;
    private String mLastUpdateTime;


//    247 138 42


    private TextInputLayout email_layout, password_layout;
    private EditText email_text, password_text;
    private TextView forgot_password_link;
    private Button sign_in_cancel, sign_in_button;
    private LinearLayout layoutCreateNewAccount;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        mRequestingLocationUpdates = false;
        mLastUpdateTime = "";
        // Update values using data stored in the Bundle.
        updateValuesFromBundle(savedInstanceState);

        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();

        checkLocationSettings();

        InitializeView();

    }

    private void InitializeView() {

        email_layout = (TextInputLayout) findViewById(R.id.input_layout_email);
        password_layout = (TextInputLayout) findViewById(R.id.input_layout_password);

        email_text = (EditText) findViewById(R.id.input_email);
        password_text = (EditText) findViewById(R.id.input_password);

        sign_in_cancel = (Button) findViewById(R.id.btn_cancel);
        sign_in_button = (Button) findViewById(R.id.btn_signin);

        forgot_password_link = (TextView) findViewById(R.id.forgot_pass_link);

        email_text.addTextChangedListener(new MyTextWatcher(email_text));
        password_text.addTextChangedListener(new MyTextWatcher(password_text));
//         layoutCreateNewAccount = (LinearLayout) findViewById(R.id.layout_create_one);

        ForgotPasswordClickActionCall();


        SigninClickActionCall();


        SigninCancelClickActionCall();


//        CreateAccount();

    }

    private void CreateAccount() {
        layoutCreateNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Signup create new account
                Intent signUp = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(signUp);
            }
        });
    }

    private void SigninClickActionCall() {

        sign_in_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ValidateSignIn();
                hideKeyboard();

            }
        });

    }

    private void ForgotPasswordClickActionCall() {
        forgot_password_link.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SignInActivity.this, ForgotPasswordActivity.class));

            }
        });


    }

    private void SigninCancelClickActionCall() {
        sign_in_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void ValidateSignIn() {

        if (!validatePassword() && !validateEmail()) {
            return;
        }
        if (!validateEmail()) {
            return;
        }
        if (!validatePassword()) {
            return;
        }

        if (mCurrentLocation == null) {
            Toast.makeText(SignInActivity.this, "" + getString(R.string.curr_loc_not_upd_msg_txt), Toast.LENGTH_SHORT).show();
            return;
        }

        SignInApiCallMethod();


    }

    private void SignInApiCallMethod() {
        try {

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }

            Log.e("email_text", "" + email_text.getText().toString());
            Log.e("password_text", "" + password_text.getText().toString());
            Log.e("Lang_code", "" + loginPrefManager.getStringValue("Lang_code"));
            Log.e("login_type", "" + getString(R.string.login_type));
            Log.e("device_id", "" + loginPrefManager.getStringValue("device_id"));
            Log.e("device_token", "" + loginPrefManager.getStringValue("device_token"));
            Log.e("Latitude", "" + mCurrentLocation.getLatitude());
            Log.e("Longitude", "" + mCurrentLocation.getLongitude());

            APIService SignInService = Webdata.getRetrofit().create(APIService.class);
            SignInService.SIGN_IN_CALL(email_text.getText().toString(), password_text.getText().toString(),
                    loginPrefManager.getStringValue("Lang_code"), getString(R.string.login_type), loginPrefManager.getStringValue("device_id"),
                    loginPrefManager.getStringValue("device_token"), "" + mCurrentLocation.getLatitude(),
                    "" + mCurrentLocation.getLongitude()).enqueue(new Callback<SignIn>() {
                @Override
                public void onResponse(Call<SignIn> call, Response<SignIn> response) {
                    progressBarDialog.dismiss();
                    if (response.body().getResponse().getHttpCode() == 200) {

                        loginPrefManager.LoginUserDetails("" + response.body().getResponse().getDriverId(), "" + response.body().getResponse().getEmail(),
                                "" + password_text.getText().toString(), "" + response.body().getResponse().getFirstName(),
                                "" + response.body().getResponse().getLastName(), "" + response.body().getResponse().getToken(),
                                "" + response.body().getResponse().getImage());

                        loginPrefManager.setStringValue("User_Id", "" + response.body().getResponse().getDriverId());
                        loginPrefManager.setStringValue("login_password", "" + password_text.getText().toString());

                        loginPrefManager.setStringValue("driver_curr_status", "" + response.body().getResponse().getDriver_status());

                        Intent sign_in = new Intent(SignInActivity.this, OrdersListsActivity.class);
//                        sign_in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(sign_in);

                        finish();

                    } else if (response.body().getResponse().getHttpCode() == 400) {
                        Toast.makeText(SignInActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                    }

                }

                @Override
                public void onFailure(Call<SignIn> call, Throwable t) {
                    progressBarDialog.dismiss();
                    Log.e("onFailure", t.getMessage());
                }
            });
        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }
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
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;

            }
        }
    }

    private boolean validateEmail() {
        String email = email_text.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            email_layout.setError(getString(R.string.error_msg_Email));
//            requestFocus(email_text);
            return false;
        } else {
            email_layout.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (password_text.getText().toString().trim().isEmpty() || password_text.getText().length() <= 5) {
            password_layout.setError(getString(R.string.error_msg_Email));
            return false;
        } else {
            password_layout.setErrorEnabled(false);
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
        if (focusedView != null)

        {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // Within {@code onPause()}, we pause location updates, but leave the
        // connection to GoogleApiClient intact.  Here, we resume receiving
        // location updates if the user has requested them.

        if (mGoogleApiClient.isConnected() && mRequestingLocationUpdates) {
            startLocationUpdates();
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

    private synchronized void buildGoogleApiClient() {
        Log.e(TAG, "Building GoogleApiClient");
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
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "Connected to GoogleApiClient");

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
            LocationUpdateMethod();
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
                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(SignInActivity.this, REQUEST_CHECK_SETTINGS);
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

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        startLocationUpdates();
                        break;
                    case Activity.RESULT_CANCELED:
                        showExitWarningDialog(SignInActivity.this);
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }

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
            public void onResult(Status status) {
                mRequestingLocationUpdates = true;
            }
        });
    }

    private void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(Status status) {
                mRequestingLocationUpdates = false;
            }
        });
    }

    private void LocationUpdateMethod() {

        if (mCurrentLocation == null) {
            return;
        } else if (mCurrentLocation != null) {
            Log.e("getLatitude", "" + mCurrentLocation.getLatitude());
            Log.e("getLongitude", "" + mCurrentLocation.getLongitude());
        }

    }

    @Override
    public void onLocationChanged(Location location) {

        Log.e("", "");

        mCurrentLocation = location;
        mLastUpdateTime = DateFormat.getTimeInstance().format(new Date());

    }


}
