package com.app.OddfoodyDriver.DriverActivities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.view.View;
import android.view.WindowManager;
import android.widget.RadioButton;

import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;


public class SelectLanguageActivity extends LocalizationActivity {

    private RadioButton zh_btn;
    private RadioButton english_btn;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        overridePendingTransition(R.anim.trans_left_in, R.anim.trans_left_out);
        setContentView(R.layout.activity_select_language);

        zh_btn = (RadioButton) findViewById(R.id.select_lang_zh_btn);
        english_btn = (RadioButton) findViewById(R.id.select_lang_english_btn);

        FloatingActionButton next_img_btn = (FloatingActionButton) findViewById(R.id.fab);

        if (getLanguage().equals("en")) {
            english_btn.setChecked(true);
        } else if (getLanguage().equals("ar")) {
            //            arabic_btn.setChecked(true);
        } else if (getLanguage().equals("zh")) {
            zh_btn.setChecked(true);

        }

        english_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!english_btn.isChecked()) {
                    setLanguage("en");
                }
            }
        });


        zh_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!zh_btn.isChecked()) {
                    setLanguage("zh");
                }
            }
        });

        next_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (english_btn.isChecked()) {

                    if (!loginPrefManager.getStringValue("en_Id").equals("") || loginPrefManager.getStringValue("en_Id") != null) {
                        loginPrefManager.setStringValue("Lang", "en");
                        loginPrefManager.setStringValue("Lang_code", "" + loginPrefManager.getStringValue("en_Id"));
                    }

                } else if (zh_btn.isChecked()) {

                    if (!loginPrefManager.getStringValue("zh_Id").equals("") || loginPrefManager
                            .getStringValue("zh_Id") != null) {
                        loginPrefManager.setStringValue("Lang", "zh");
                        loginPrefManager.setStringValue("Lang_code", "" + loginPrefManager
                                .getStringValue("en_Id"));
                    }

                } else {

                    if (!loginPrefManager.getStringValue("ar_Id").equals("") || loginPrefManager.getStringValue("ar_Id") != null) {
                        loginPrefManager.setStringValue("Lang", "ar");
                        loginPrefManager.setStringValue("Lang_code", "" + loginPrefManager.getStringValue("ar_Id"));
                    }
                }

                Intent Sign_in = new Intent(SelectLanguageActivity.this, SignInActivity.class);
                startActivity(Sign_in);
                finish();
            }
        });

    }

    @Override
    public void onBackPressed() {
    }

}
