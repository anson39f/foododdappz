package com.app.OddfoodyDriver.DriverActivities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;

/**
 * Created by Next Brain on 27-01-2017.
 */

public class StatusActivity extends LocalizationActivity {

    Button Status_btn;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_status);
        Initializeview();

    }

    private void Initializeview() {
        Status_btn=(Button)findViewById(R.id.status_btn);
        StatusBtnClickActionCall();
    }
    private void StatusBtnClickActionCall() {
        Status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Animation bottomdown = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.bottom_up);

               finish();
                Status_btn.startAnimation(bottomdown);
            }
        });


    }

}
