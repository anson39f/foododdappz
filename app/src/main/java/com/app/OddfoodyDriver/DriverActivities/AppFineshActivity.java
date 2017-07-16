package com.app.OddfoodyDriver.DriverActivities;

import android.app.LocalActivityManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;


/**
 * Created by nextbrain on 16/6/17.
 */

public class AppFineshActivity extends LocalizationActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        LocalActivityManager localActivityManager = new LocalActivityManager(this, true);
        localActivityManager.removeAllActivities();

        finish();

    }


}
