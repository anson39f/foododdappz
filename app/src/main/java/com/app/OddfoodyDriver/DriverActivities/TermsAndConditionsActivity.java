package com.app.OddfoodyDriver.DriverActivities;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;


public class TermsAndConditionsActivity extends LocalizationActivity {
    String cms_type;
    String about_us_url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_terms_and_conditions_layout);

        Intent intent = getIntent();
        if (intent.hasExtra("cmstype")) {
            cms_type = intent.getStringExtra("cmstype");
            Log.e("cms_type", "" + cms_type);
        }

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);


        if (LanguageSetting.getLanguage().equals("en")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        WebView terms_and_condi_web_view = (WebView) findViewById(R.id.terms_and_condi_web_view);
        terms_and_condi_web_view.setBackgroundColor(Color.TRANSPARENT);

        terms_and_condi_web_view.setWebViewClient(new WebViewClient());
        WebSettings settings = terms_and_condi_web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);

        if (!NetworkStatus.isConnectingToInternet(getApplicationContext())) {
            Toast.makeText(TermsAndConditionsActivity.this, "" + getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        if (cms_type.equals("1")) {
            getSupportActionBar().setTitle("" + getString(R.string.contracts_users));
            about_us_url = String.format("%s/%s", "" + Webdata.contracts_users, "" + loginPrefManager.getStringValue("Lang_code"));
//        } else if (cms_type.equals("2")) {
//            getSupportActionBar().setTitle("" + getString(R.string.privacy_policy));
//            about_us_url = String.format("%s/%s", "" + Webdata.privacy_policy, "" + loginPrefManager.getStringValue("Lang_code"));
        } else {
            getSupportActionBar().setTitle("" + getString(R.string.contracts_users));
            about_us_url = String.format("%s/%s", "" + Webdata.contracts_users, "" + loginPrefManager.getStringValue("Lang_code"));
        }
        terms_and_condi_web_view.loadUrl("" + about_us_url);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }


}
