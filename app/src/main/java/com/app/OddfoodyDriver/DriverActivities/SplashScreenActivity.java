package com.app.OddfoodyDriver.DriverActivities;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.app.OddfoodyDriver.AppControler.OddfoodyDriver;
import com.app.OddfoodyDriver.DriverResponseModel.CurrencyList;
import com.app.OddfoodyDriver.DriverResponseModel.Language;
import com.app.OddfoodyDriver.DriverResponseModel.LanguageList;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 23-01-2017.
 */

public class SplashScreenActivity extends LocalizationActivity implements OddfoodyDriver.SplashScreenInterface{

    private static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;
    private Runnable runnable;

    private static final int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private static final String TAG = "SplashScreen";

    public final static int OVERLAY_PERMISSION_REQUEST_CODE = 12345;

    private static android.app.AlertDialog.Builder alertDialogBuilder = null;
    private static android.app.AlertDialog networkAlertDialog = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash_screen);


        OddfoodyDriver.CallSplashScreenInterface(SplashScreenActivity.this);


        runnable = new Runnable() {
            @Override
            public void run() {

                if (loginPrefManager.getDefaultLang().equals("false")) {
                    Intent select_LanguageList = new Intent(SplashScreenActivity.this, SelectLanguageActivity.class);
                    select_LanguageList.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(select_LanguageList);
                    finish();
                } else {

                    if (loginPrefManager.getUserLoginDriverId().equals("")) {
                        Intent signInActivity = new Intent(SplashScreenActivity.this, SignInActivity.class);
                        signInActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(signInActivity);
                        finish();
                    } else {
                        Intent sign_in = new Intent(SplashScreenActivity.this, OrdersListsActivity.class);
                        sign_in.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(sign_in);
                        finish();
                    }

                }

            }
        };

        GeneralSettingsReq();

//        if (checkPlayServices()) {
//            runOnUiThread(new Runnable() {
//                @Override
//                public void run() {
//                    if (preference.getStringValue("device_token").isEmpty()) {
//                        MyFirebaseInstanceIDService firebaseIntent = new MyFirebaseInstanceIDService();
//                        firebaseIntent.onTokenRefresh();
//                    }
//                }
//            });
//        }

        Log.e("device_token", ""+loginPrefManager.getStringValue("device_token"));

        String device_id = Settings.Secure.getString(this.getContentResolver(), Settings.Secure.ANDROID_ID);
        loginPrefManager.setStringValue("device_id", "" + device_id);

    }

    @Override
    public void onResume() {
        super.onResume();

        if (NetworkStatus.isConnectingToInternet(SplashScreenActivity.this)) {
            checkPermision();
        }

//        if (!NetworkStatus.isConnectingToInternet(SplashScreenActivity.this)) {
//            showExitWarningDialog();
//        } else {
//            if (checkDrawOverlayPermission()) {
//                checkPermision();
//            }
//        }

    }

    public boolean checkDrawOverlayPermission() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        if (!Settings.canDrawOverlays(SplashScreenActivity.this)) {
            showOverlayPermissionWarningDialog();
            return false;
        } else {
            return true;
        }

    }


    //    Screen Overlay permission dialog
    private void showOverlayPermissionWarningDialog() {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(SplashScreenActivity.this, R.style.MyDialogStyle);
        alertDialog.setTitle("" + getString(R.string.screen_over_lay_title));
        alertDialog.setMessage("" + getString(R.string.screen_over_lay_permission_msg_txt));
        alertDialog.setPositiveButton("" + getApplicationContext().getString(R.string.screen_over_lay_ok_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, OVERLAY_PERMISSION_REQUEST_CODE);
            }
        });
        alertDialog.setNegativeButton("" + getApplicationContext().getString(R.string.screen_over_lay_cancel_btn_txt),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.setCancelable(false);
        alertDialog.show();
    }


    private boolean checkPlayServices() {
        GoogleApiAvailability apiAvailability = GoogleApiAvailability.getInstance();
        int resultCode = apiAvailability.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (apiAvailability.isUserResolvableError(resultCode)) {
                apiAvailability.getErrorDialog(this, resultCode, PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.e(TAG, "This device is not supported.");
                finish();
            }
            return false;
        }
        return true;
    }


    private void GeneralSettingsReq() {

        final APIService LanguageListdata = Webdata.getRetrofit().create(APIService.class);
        LanguageListdata.getLanguage().enqueue(new Callback<LanguageList>() {
            @Override
            public void onResponse(Call<LanguageList> call, Response<LanguageList> response) {

                try {

                    Log.e("GeneralSettingsReq", "" + response.raw().toString());

                    if (response.body().getResponse().getHttpCode() == 200) {

                        loginPrefManager.setStringValue("currency_side", "" + response.body().getResponse().getGeneralSettings().getCurrencySide());

                        loginPrefManager.setStringValue("name", "" + response.body().getResponse().getGeneralSettings().getSiteName());
                        loginPrefManager.setStringValue("email", "" + response.body().getResponse().getGeneralSettings().getEmail());
                        loginPrefManager.setStringValue("site_email", "" + response.body().getResponse().getGeneralSettings().getEmail());
                        loginPrefManager.setStringValue("phone_no", "" + response.body().getResponse().getGeneralSettings().getTelephone());
                        loginPrefManager.setStringValue("contact_address", "" + response.body().getResponse().getGeneralSettings().getContactAddress());

                        List<Language> langugeedata = response.body().getResponse().getLanguages();

                        for (int i = 0; i < langugeedata.size(); i++) {

                            if (i == 0) {
                                loginPrefManager.setStringValue("en_Name", langugeedata.get(i).getName());
                                loginPrefManager.setStringValue("en_Id", langugeedata.get(i).getId());
                            } else if (i == 1) {
                                loginPrefManager.setStringValue("ar_Name", langugeedata.get(i).getName());
                                loginPrefManager.setStringValue("ar_Id", langugeedata.get(i).getId());
                            }

                            if (langugeedata.get(i).getId().equals(response.body().getResponse().getGeneralSettings().getDefaultLanguage())) {

                                if (loginPrefManager.getDefaultLang().isEmpty()) {

                                    loginPrefManager.setStringValue("Lang", "" + langugeedata.get(i).getLanguageCode());
                                    loginPrefManager.setStringValue("Lang_code", "" + langugeedata.get(i).getId());

                                    Log.e("getDefaultLang", "isEmpty");

                                    loginPrefManager.setDefaultLang("false");

                                } else {

                                    Log.e("getDefaultLang", "notEmpty");
                                    loginPrefManager.setDefaultLang("true");
                                }

                            }

                            List<CurrencyList> currency = response.body().getResponse().getCurrencyList();

                            for (int j = 0; j < currency.size(); j++) {
                                if (currency.get(j).getId() == response.body().getResponse().getGeneralSettings().getDefaultCurrency()) {
                                    loginPrefManager.setStringValue("currency_symbol", currency.get(j).getCurrencySymbol().trim());
                                    loginPrefManager.setStringValue("currency_name", currency.get(j).getCurrencyName());
                                    break;
                                }
                            }

                        }

                    }

                } catch (Exception e) {
                    Log.e(TAG, " General Settings Exception Error " + e.getMessage());
                }

            }

            @Override
            public void onFailure(Call<LanguageList> call, Throwable t) {
                Log.e(TAG + " onFailure:", "" + t.getMessage());
            }

        });
    }

    private void checkPermision() {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            if (checkAndRequestPermissions()) {
                new Handler().postDelayed(runnable, 3000);
            }
        } else {
            new Handler().postDelayed(runnable, 3000);
        }

    }

    //  Internet connection alert
    private void showExitWarningDialog() {
        alertDialogBuilder = new android.app.AlertDialog.Builder(SplashScreenActivity.this, android.app.AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialogBuilder.setCancelable(false);
        alertDialogBuilder.setTitle("" + getString(R.string.no_internet_conn_tit_txt))
                .setMessage("" + getString(R.string.no_internet_conn_msg_txt))
                .setPositiveButton("" + getString(R.string.no_internet_conn_ok_btn_txt),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                            }
                        });

        networkAlertDialog = alertDialogBuilder.create();
        networkAlertDialog.show();
    }

    //    Check permission
    private boolean checkAndRequestPermissions() {
        int permissionLocation = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);

        int permissionReadMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int permissionReceiveMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int permissionSendMessage = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);

        int permissionCallPhone = ContextCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE);
        int permissionCamera = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
        int permissionReadExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
        int permissionWriteExternalStorage = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        List<String> listPermissionsNeeded = new ArrayList<>();

        if (permissionLocation != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (permissionReadMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }
        if (permissionReceiveMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (permissionSendMessage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }
        if (permissionCallPhone != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CALL_PHONE);
        }
        if (permissionCamera != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.CAMERA);
        }
        if (permissionReadExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (permissionWriteExternalStorage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }

        return true;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_ID_MULTIPLE_PERMISSIONS: {

                Map<String, Integer> perms = new HashMap<>();
                perms.put(Manifest.permission.ACCESS_FINE_LOCATION, PackageManager.PERMISSION_GRANTED);

                perms.put(Manifest.permission.READ_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.RECEIVE_SMS, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.SEND_SMS, PackageManager.PERMISSION_GRANTED);

                perms.put(Manifest.permission.CALL_PHONE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.CAMERA, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.READ_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);
                perms.put(Manifest.permission.WRITE_EXTERNAL_STORAGE, PackageManager.PERMISSION_GRANTED);

                if (grantResults.length > 0) {

                    for (int i = 0; i < permissions.length; i++)
                        perms.put(permissions[i], grantResults[i]);

                    // Check for both permissions
                    if (perms.get(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

                            && perms.get(Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.RECEIVE_SMS) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED

                            && perms.get(Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED

                            && perms.get(Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED
                            && perms.get(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {

                        new Handler().postDelayed(runnable, 3000);

                    } else {

                        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)

                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.RECEIVE_SMS)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.SEND_SMS)

                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CALL_PHONE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)

                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                                || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

                            showDialogOK("Call Phone, Read & Write External Storage, SMS and Location Services Permission required for this app",
                                    new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            switch (which) {
                                                case DialogInterface.BUTTON_POSITIVE:
                                                    checkAndRequestPermissions();
                                                    break;
                                                case DialogInterface.BUTTON_NEGATIVE:
                                                    finish();
                                                    break;
                                            }
                                        }
                                    });
                        } else {
                            Toast.makeText(SplashScreenActivity.this, "Go to settings and enable permissions", Toast.LENGTH_LONG).show();
                        }

                    }
                }
            }
        }

    }

    private void showDialogOK(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this).setMessage(message).setPositiveButton("OK", okListener).create().show();
    }


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    @Override
    public void ReloadPermissionMethod() {
        checkPermision();
    }


}
