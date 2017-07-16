package com.app.OddfoodyDriver.DriverActivities;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;

/**
 * Created by Next Brain on 27-01-2017.
 */
public class SupportActivity extends LocalizationActivity {

    private Toolbar toolbar;

    private TextView support_name_txt_view;
    private TextView support_email_txt_view;
    private TextView support_phone_no_txt_view;
    private TextView support_address_txt_view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_support_layout);


        toolbar = (Toolbar) findViewById(R.id.support_app_bar);
        toolbar.setTitle("" + getResources().getString(R.string.support));
        setSupportActionBar(toolbar);

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (LanguageSetting.getLanguage().equals("en")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        support_name_txt_view = (TextView) findViewById(R.id.support_name_txt_view);
        support_email_txt_view = (TextView) findViewById(R.id.support_email_txt_view);
        support_phone_no_txt_view = (TextView) findViewById(R.id.support_phone_no_txt_view);
        support_address_txt_view = (TextView) findViewById(R.id.support_address_txt_view);

    }

    @Override
    public void onResume() {
        super.onResume();

        support_name_txt_view.setText("" + loginPrefManager.getStringValue("name"));
        support_email_txt_view.setText("" + loginPrefManager.getStringValue("site_email"));
        support_phone_no_txt_view.setText("" + loginPrefManager.getStringValue("phone_no"));
        support_address_txt_view.setText("" + loginPrefManager.getStringValue("contact_address"));

    }
}
