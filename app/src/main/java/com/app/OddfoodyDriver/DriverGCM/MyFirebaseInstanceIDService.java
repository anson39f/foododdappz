package com.app.OddfoodyDriver.DriverGCM;

/**
 * Created by Invenzo on 03-10-2016.
 */


import android.util.Log;

import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;


//Class extending FirebaseInstanceIdService
public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = "MyFirebaseIIDService";
    private LoginPrefManager Preference;

    @Override
    public void onTokenRefresh() {

        //Getting registration token
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if(refreshedToken != null){

            Log.e("Refreshed token: " ,refreshedToken);

            Preference = new LoginPrefManager(getApplicationContext());
            Preference.setStringValue("device_token",""+refreshedToken);

            Log.e("Refreshed token: " ,refreshedToken);
            Log.e("token", "..."+refreshedToken);
        }

    }

    private void sendRegistrationToServer(String token) {
        //You can implement this method to store the token on your server
        //Not required for current project
    }

}