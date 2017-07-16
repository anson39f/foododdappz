package com.app.OddfoodyDriver.LocalizationActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import com.app.OddfoodyDriver.AppControler.CustomFontStyle;
import com.app.OddfoodyDriver.AppControler.OddfoodyDriver;
import com.app.OddfoodyDriver.DotsProgressBar.DDProgressBarDialog;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.R;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Locale;


public class LocalizationActivity extends AppCompatActivity implements com.app.OddfoodyDriver.LocalizationActivity.OnLocaleChangedListener {


    // Bundle key
    private static final String KEY_ACTIVIY_LOCALE_CHANGED = "activity_locale_changed";

    // Boolean flag to check that activity was recreated from locale changed.
    private boolean isLocalizationChanged = false;

    // Prepare default language.
    private String currentLanguage = LanguageSetting.getDefaultLanguage();


    private AlertDialog.Builder alertDialogBuilder = null;
    private AlertDialog networkAlertDialog = null;

    private static final String LOG_TAG = "CheckNetworkStatus";

    private static final int ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE = 5469;

    public LoginPrefManager loginPrefManager;
    public DDProgressBarDialog progressBarDialog;

//    private MenuNetworkChangeLocaReceiver menuNetworkChangeLocaReceiver;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        CustomFontStyle.replaceDefaultFount(LocalizationActivity.this);

        setupLanguage();
        checkBeforeLocaleChanging();
        super.onCreate(savedInstanceState);

        loginPrefManager = new LoginPrefManager(LocalizationActivity.this);
        progressBarDialog = new DDProgressBarDialog(LocalizationActivity.this);


//        CallNetWorkStatusDialog();

    }

    // Provide method to set application language by country name.
    protected final void setLanguage(String language) {
        if (!isDuplicatedLanguageSetting(language)) {
            LanguageSetting.setLanguage(LocalizationActivity.this, language);
            notifyLanguageChanged();
        }
    }

    // Provide method to set application language by locale.
    public final void setLanguage(Locale locale) {
        setLanguage(locale.getLanguage());
    }

    public final void setDefaultLanguage(String language) {
        LanguageSetting.setDefaultLanguage(language);
    }

    public final void setDefaultLanguage(Locale locale) {
        LanguageSetting.setDefaultLanguage(locale.getLanguage());
    }

    // Get current language
    protected final String getLanguage() {
        return LanguageSetting.getLanguage();
    }

    // Get current locale
    public final Locale getLocale() {
        return LanguageSetting.getLocale(LocalizationActivity.this);
    }

    // Check that bundle come from locale change.
    // If yes, bundle will obe remove and set boolean flag to "true".
    private void checkBeforeLocaleChanging() {
        boolean isLocalizationChanged = getIntent().getBooleanExtra(KEY_ACTIVIY_LOCALE_CHANGED, false);
        if (isLocalizationChanged) {
            this.isLocalizationChanged = true;
            getIntent().removeExtra(KEY_ACTIVIY_LOCALE_CHANGED);
        }
    }

    // Setup language to locale and language preference.
    // This method will called before onCreate.
    private void setupLanguage() {

//        Log.e("Current Language initial :",currentLanguage);
        Locale locale = LanguageSetting.getLocale(LocalizationActivity.this);
        setupLocale(locale);
        currentLanguage = locale.getLanguage();
//        Log.e("Current Language :",currentLanguage);
        LanguageSetting.setLanguage(LocalizationActivity.this, locale.getLanguage());
    }

    // Set locale configuration.
    private void setupLocale(Locale locale) {
        updateLocaleConfiguration(LocalizationActivity.this, locale);
    }

    private void updateLocaleConfiguration(Context context, Locale locale) {
        Configuration config = new Configuration();
        config.locale = locale;
        DisplayMetrics dm = context.getResources().getDisplayMetrics();
        context.getResources().updateConfiguration(config, dm);
    }

    // Avoid duplicated setup
    private boolean isDuplicatedLanguageSetting(String language) {
        return language.toLowerCase(Locale.getDefault()).equals(LanguageSetting.getLanguage());
    }

    // Let's take it change! (Using recreate method that available on API 11 or more.
    private void notifyLanguageChanged() {
        onBeforeLocaleChanged();
        getIntent().putExtra(KEY_ACTIVIY_LOCALE_CHANGED, true);
        callDummyActivity();
        recreate();
    }

    // If activity is run to backstack. So we have to check if this activity is resume working.
    @Override
    public void onResume() {
        super.onResume();

        new Handler().post(new Runnable() {
            @Override
            public void run() {
                checkLocaleChange();
                checkAfterLocaleChanging();
            }
        });

    }

    private void CheckNetworkValidation() {

//        LocalBroadcastManager.getInstance(LocalizationActivity.this).sendBroadcast(new Intent("Localization_On_Resume"));
//        Log.e("Common Activity","onResume");

        CheckNetworkStatusMethod();

    }

    // Check if locale has change while this activity was run to backstack.
    private void checkLocaleChange() {
        if (!LanguageSetting.getLanguage().toLowerCase(Locale.getDefault())
                .equals(currentLanguage.toLowerCase(Locale.getDefault()))) {
            callDummyActivity();
            recreate();
        }
    }

    // Call override method if local is really changed
    private void checkAfterLocaleChanging() {
        if (isLocalizationChanged) {
            onAfterLocaleChanged();
            isLocalizationChanged = false;
        }
    }

    private void callDummyActivity() {
        startActivity(new Intent(LocalizationActivity.this, BlankDummyActivity.class));
    }

    // Just override method locale change event
    @Override
    public void onBeforeLocaleChanged() {
    }

    @Override
    public void onAfterLocaleChanged() {
    }


    private void CallNetWorkStatusDialog() {

        alertDialogBuilder = new AlertDialog.Builder(LocalizationActivity.this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);

        alertDialogBuilder
                .setTitle("" + getString(R.string.no_internet_conn_tit_txt))
                .setMessage("" + getString(R.string.no_internet_conn_msg_txt))
                .setCancelable(false)
                .setPositiveButton("" + getString(R.string.no_internet_conn_ok_btn_txt),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                finish();
                                System.exit(0);
                            }
                        });

        networkAlertDialog = alertDialogBuilder.create();
        networkAlertDialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);

    }

    protected void CheckNetworkStatusMethod() {

        // Check if Android M or higher
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (!Settings.canDrawOverlays(LocalizationActivity.this)) {

                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivityForResult(intent, ACTION_MANAGE_OVERLAY_PERMISSION_REQUEST_CODE);

            } else {

                if (networkAlertDialog != null && alertDialogBuilder != null) {
                    if (networkAlertDialog.isShowing()) {
                        if (NetworkStatus.getConnectivityStatusBoolean(LocalizationActivity.this)) {
                            networkAlertDialog.dismiss();
                            Log.e(LOG_TAG, "Receieved networkAlertDialog not isShowing");
                        }
                    } else {
                        if (!NetworkStatus.getConnectivityStatusBoolean(LocalizationActivity.this)) {
                            networkAlertDialog.show();
                            Log.e(LOG_TAG, "Receieved networkAlertDialog isShowing");
                        }
                    }
                }

            }

        } else {

            if (networkAlertDialog != null && alertDialogBuilder != null) {
                if (networkAlertDialog.isShowing()) {
                    if (NetworkStatus.getConnectivityStatusBoolean(LocalizationActivity.this)) {
                        networkAlertDialog.dismiss();
                        Log.e(LOG_TAG, "Receieved networkAlertDialog not isShowing");
                    }
                } else {
                    if (!NetworkStatus.getConnectivityStatusBoolean(LocalizationActivity.this)) {
                        networkAlertDialog.show();
                        Log.e(LOG_TAG, "Receieved networkAlertDialog isShowing");
                    }
                }
            }

        }

    }

    private void setMobileDataEnabled(Context context, boolean enabled) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException, NoSuchMethodException, InvocationTargetException {
        final ConnectivityManager conman = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        final Class conmanClass = Class.forName(conman.getClass().getName());
        final Field connectivityManagerField = conmanClass.getDeclaredField("mService");
        connectivityManagerField.setAccessible(true);
        final Object connectivityManager = connectivityManagerField.get(conman);
        final Class connectivityManagerClass =  Class.forName(connectivityManager.getClass().getName());
        final Method setMobileDataEnabledMethod = connectivityManagerClass.getDeclaredMethod("setMobileDataEnabled", Boolean.TYPE);
        setMobileDataEnabledMethod.setAccessible(true);

        setMobileDataEnabledMethod.invoke(connectivityManager, enabled);
    }


}