package com.app.OddfoodyDriver.DriverActivities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.app.OddfoodyDriver.DotsProgressBar.DDProgressBarDialog;
import com.app.OddfoodyDriver.DriverAdapter.DriverNotifiAdapter;
import com.app.OddfoodyDriver.DriverCustomView.OrderConfirmationDialog;
import com.app.OddfoodyDriver.DriverResponseModel.NotificationList;
import com.app.OddfoodyDriver.DriverResponseModel.NotificationListResponseDatum;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.ListItemDecorations.OrderItemDecoration;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nextbrain on 6/6/17.
 */

public class NotificationListActivity extends LocalizationActivity implements OrderConfirmationDialog.OrderConfDialogInterface {

    private Toolbar notificationtoolbar;
    private TextView notifi_list_empty_txt_view;

    private LoginPrefManager loginPrefManager;
    private DDProgressBarDialog progressDialog;

    private RecyclerView notification_recycler_view;
    private DriverNotifiAdapter driverNotifiAdapter;


    private String Order_ID;
    private String notif_conf_dialog;


    public List<NotificationListResponseDatum> notificationListResponseData;
    public OrderConfirmationDialog orderConfirmationDialog;


    private BroadcastReceiver broadcastReceiver;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_list_layout);

        IntializeView();

    }

    private void IntializeView() {

        loginPrefManager = new LoginPrefManager(NotificationListActivity.this);
        progressDialog = Webdata.getProgressBarDialog(NotificationListActivity.this);

        notificationtoolbar = (Toolbar) findViewById(R.id.notification_app_bar);
        notificationtoolbar.setTitle(getResources().getString(R.string.fab_notifications));
        setSupportActionBar(notificationtoolbar);

        notificationtoolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (LanguageSetting.getLanguage().equals("en") || LanguageSetting.getLanguage().equals
                ("zh")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else if (LanguageSetting.getLanguage().equals("ar")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        notificationtoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        notifi_list_empty_txt_view = (TextView) findViewById(R.id.notifi_list_empty_txt_view);

        notification_recycler_view = (RecyclerView) findViewById(R.id.notification_recycler_view);
        notification_recycler_view.setHasFixedSize(true);
        notification_recycler_view.setLayoutManager(new LinearLayoutManager(NotificationListActivity.this));
        notification_recycler_view.addItemDecoration(new OrderItemDecoration(NotificationListActivity.this, R.dimen.order_list_item_devider_height));


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                finish();
            }
        };
        LocalBroadcastManager.getInstance(NotificationListActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("notifi_broadcast"));

    }

    @Override
    public void onResume() {
        super.onResume();

        if (getIntent().getExtras() != null) {
            notif_conf_dialog = getIntent().getExtras().getString("notif_conf_dialog");
        }

        NotifiListRequestMethod();
    }

    @Override
    public void onBackPressed() {
        //        super.onBackPressed();

        Intent welcom_intent = new Intent("notifi_broadcast");
        LocalBroadcastManager.getInstance(NotificationListActivity.this).sendBroadcast(welcom_intent);

    }

    private void NotifiListRequestMethod() {

        if (progressDialog != null) {
            progressDialog.show();
        }

        Log.e("driver_id", "" + loginPrefManager.getUserLoginDriverId());
        Log.e("driver_token", "" + loginPrefManager.getUserLoginToken());
        Log.e("lang_code", "" + loginPrefManager.getStringValue("Lang_code"));

        try {
            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.notification_list("" + loginPrefManager.getStringValue("Lang_code"),
                    "" + loginPrefManager.getUserLoginToken(),
                    "" + loginPrefManager.getUserLoginDriverId()).enqueue(new Callback<NotificationList>() {
                @Override
                public void onResponse(Call<NotificationList> call, Response<NotificationList> response) {

                    try {

                        progressDialog.dismiss();

                        Log.e("response", response.raw().toString());

                        if (response.body().getResponse().getHttpCode() == 200) {

                            if (response.body().getResponse().getData().size() != 0) {

                                notificationListResponseData = response.body().getResponse().getData();

                                driverNotifiAdapter = new DriverNotifiAdapter(NotificationListActivity.this, response.body().getResponse().getData());
                                notification_recycler_view.setAdapter(driverNotifiAdapter);

                            } else {
                                notifi_list_empty_txt_view.setVisibility(View.VISIBLE);
                            }


                            if (notif_conf_dialog.equals("1")) {

                                if (notificationListResponseData.size() != 0) {

                                    if (orderConfirmationDialog != null) {

                                        if (orderConfirmationDialog.isShowing()) {
                                            orderConfirmationDialog.dismiss();
                                        } else {

                                            NotificationListResponseDatum NotifListRespeDatum = notificationListResponseData.get(0);

                                            if (NotifListRespeDatum.getDriverResponse() == 0 && NotifListRespeDatum.getOrderDeliveryStatus() == 0
                                                    && NotifListRespeDatum.getAutoOrderRejected() == 0) {
                                                orderConfirmationDialog = new OrderConfirmationDialog(NotificationListActivity.this, notificationListResponseData.get(0));
                                                orderConfirmationDialog.OrderConfDailCallMethod(NotificationListActivity.this);
                                                orderConfirmationDialog.show();
                                            }

                                        }

                                    } else {

                                        NotificationListResponseDatum NotifListRespeDatum = notificationListResponseData.get(0);

                                        if (NotifListRespeDatum.getDriverResponse() == 0 && NotifListRespeDatum.getOrderDeliveryStatus() == 0
                                                && NotifListRespeDatum.getAutoOrderRejected() == 0) {
                                            orderConfirmationDialog = new OrderConfirmationDialog(NotificationListActivity.this, notificationListResponseData.get(0));
                                            orderConfirmationDialog.OrderConfDailCallMethod(NotificationListActivity.this);
                                            orderConfirmationDialog.show();
                                        }

                                    }
                                }
                            }

                        } else if (response.body().getResponse().getHttpCode() == 400) {
                            progressDialog.dismiss();
                        }

                    } catch (Exception e) {
                        Log.e("Exception", "" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<NotificationList> call, Throwable t) {
                    progressDialog.dismiss();
                }
            });

        } catch (Exception e) {
            progressDialog.dismiss();

        }

    }


    @Override
    public void OrderConfDialogMethod(NotificationListResponseDatum notificationListResponseDatum) {

        notificationListResponseData.set(0, notificationListResponseDatum);

        Intent welcom_intent = new Intent("Loc_update_Rec");
        LocalBroadcastManager.getInstance(NotificationListActivity.this).sendBroadcast(welcom_intent);

        if (driverNotifiAdapter != null) {
            driverNotifiAdapter = new DriverNotifiAdapter(NotificationListActivity.this, notificationListResponseData);
            notification_recycler_view.setAdapter(driverNotifiAdapter);
            driverNotifiAdapter.notifyDataSetChanged();
        }

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(NotificationListActivity.this).unregisterReceiver(broadcastReceiver);
    }

}
