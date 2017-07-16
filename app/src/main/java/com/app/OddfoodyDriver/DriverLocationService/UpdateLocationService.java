package com.app.OddfoodyDriver.DriverLocationService;

import android.app.IntentService;
import android.content.Intent;

/**
 * Created by NEXTBRAIN on 2/27/2017.
 */

public class UpdateLocationService extends IntentService {

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public UpdateLocationService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {

    }

}
