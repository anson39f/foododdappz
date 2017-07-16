package com.app.OddfoodyDriver.AppControler;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.app.OddfoodyDriver.DriverActivities.AppFineshActivity;
import com.app.OddfoodyDriver.DriverPhotoPicker.EasyImage;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.R;

import java.util.List;


public class OddfoodyDriver extends Application implements Application.ActivityLifecycleCallbacks {

    private static Context applicationContext;
    private static final Point displaySize = new Point();

    private static AlertDialog.Builder alertDialogBuilder = null;
    private static AlertDialog networkAlertDialog = null;

    private static final String LOG_TAG = "CheckNetworkStatus";

    public static Activity currentActivity;


    public Activity current_activity = null;


    @Override
    public void onCreate() {
        super.onCreate();

        registerActivityLifecycleCallbacks(this);

        applicationContext = getApplicationContext();

        CustomFontStyle.replaceDefaultFount(this);

        MultiDex.install(this);


        EasyImage.configuration(this)
                .setImagesFolderName("OddfoodyDriverApp")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);


    }





    public static Context getContext() {
        return applicationContext;
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityStarted(Activity activity) {
    }

    @Override
    public void onActivityResumed(Activity activity) {

        if (current_activity == null) {

            this.current_activity = activity;

            CreateNetWorkAleartDialog(current_activity);

            if (activity.getClass().getName().equals("com.app.OddfoodyDriver.DriverActivities.SplashScreenActivity")) {
                CallNetworkDialogMethod();
            }

        } else {


            if (!this.current_activity.getClass().getName().equals("" + activity.getClass().getName())) {

                this.current_activity = activity;
                CreateNetWorkAleartDialog(current_activity);

            } else {
                CallNetworkDialogMethod();
            }


        }

    }

    @Override
    public void onActivityPaused(Activity activity) {
    }

    @Override
    public void onActivityStopped(Activity activity) {
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
    }


    // Create Current Activity Dialog.
    private void CreateNetWorkAleartDialog(Activity activity) {

        if (alertDialogBuilder != null) {
            alertDialogBuilder = null;
            networkAlertDialog = null;
        }

        alertDialogBuilder = new AlertDialog.Builder(activity);
        alertDialogBuilder.setTitle("" + getString(R.string.no_internet_conn_tit_txt))
                .setMessage("" + getString(R.string.no_internet_conn_msg_txt))
                .setCancelable(false)
                .setPositiveButton("" + getString(R.string.no_internet_conn_ok_btn_txt),
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                AppExitMethod();
                            }
                        });
        networkAlertDialog = alertDialogBuilder.create();

    }


    public void CallNetworkDialogMethod() {

        if (isAppForground(applicationContext)) {

            if (networkAlertDialog != null && alertDialogBuilder != null) {
                if (networkAlertDialog.isShowing()) {

                    if (NetworkStatus.getConnectivityStatusBoolean(applicationContext)) {

                        networkAlertDialog.dismiss();

                        if (current_activity.getClass().getName().equals("com.app.OddfoodyDriver.DriverActivities.SplashScreenActivity")) {
                            if (splashScreenInterface_method != null) {
                                splashScreenInterface_method.ReloadPermissionMethod();
                            }
                        }
                    }

                } else {

                    if (!NetworkStatus.getConnectivityStatusBoolean(applicationContext)) {
                        networkAlertDialog.show();
                    }

                }
            }

        }

    }


    public static boolean isAppForground(Context mContext) {
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(1);
        if (!tasks.isEmpty()) {
            ComponentName topActivity = tasks.get(0).topActivity;
            if (!topActivity.getPackageName().equals(mContext.getPackageName())) {
                return false;
            }
        }
        return true;
    }


    private void AppExitMethod() {
        Intent signInActivity = new Intent(getApplicationContext(), AppFineshActivity.class);
        signInActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(signInActivity);
    }




    public static SplashScreenInterface splashScreenInterface_method;

    public static void CallSplashScreenInterface(SplashScreenInterface splashScreenInterface) {
        splashScreenInterface_method = splashScreenInterface;
    }

    public interface SplashScreenInterface {
        void ReloadPermissionMethod();
    }




    public static void checkDisplaySize() {
        try {
            WindowManager manager = (WindowManager) applicationContext.getSystemService(Context.WINDOW_SERVICE);
            if (manager != null) {
                Display display = manager.getDefaultDisplay();
                if (display != null) {
                    if (android.os.Build.VERSION.SDK_INT < 13) {
                        displaySize.set(display.getWidth(), display.getHeight());
                    } else {
                        display.getSize(displaySize);
                    }
                }
            }
        } catch (Exception ignored) {
        }
    }



}
