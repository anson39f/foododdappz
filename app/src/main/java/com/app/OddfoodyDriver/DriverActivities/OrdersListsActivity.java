package com.app.OddfoodyDriver.DriverActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.app.OddfoodyDriver.DriverAdapter.OrdersAdapter;
import com.app.OddfoodyDriver.DriverLocationService.LocationService;
import com.app.OddfoodyDriver.DriverResponseModel.DrivLocRequestMethod;
import com.app.OddfoodyDriver.DriverResponseModel.DriverOrders;
import com.app.OddfoodyDriver.DriverResponseModel.OrderList;
import com.app.OddfoodyDriver.DriverResponseModel.OrderStatus;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.ListItemDecorations.OrderItemDecoration;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
import com.app.OddfoodyDriver.androidcharts.entity.TitleValueColorEntity;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 23-01-2017.
 */

public class OrdersListsActivity extends LocalizationActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, ResultCallback<LocationSettingsResult> {


    private static final int REQUEST_CHECK_SETTINGS = 0x1;
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS = UPDATE_INTERVAL_IN_MILLISECONDS / 2;
    private Boolean mRequestingLocationUpdates;

    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requesting-location-updates";
    private final static String KEY_LOCATION = "location";
    private final static String KEY_LAST_UPDATED_TIME_STRING = "last-updated-time-string";
    private static final String TAG = "OrdersListsActivity";

    private String mLastUpdateTime;
    private Location mCurrentLocation;

    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private LocationSettingsRequest mLocationSettingsRequest;


    Intent location_intent;

    FloatingActionsMenu fabMenu;
    FloatingActionButton fab_lang_but, fab_prof_but, fab_support_but, fab_report_but, fab_log_out_, fab_notifications;

    RecyclerView order_recycler_view;
    private OrdersAdapter orderAdapter;


    private Toolbar ordertoolbar;
    private TextView ordertoolbartext;

    private ArrayList<OrderList> orderItems = new ArrayList<>();

    private TextView order_list_empty_view;

    private BroadcastReceiver broadcastReceiver;


    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders_recyclerview);

        onLowMemory();

        ordertoolbar = (Toolbar) findViewById(R.id.order_app_bar);
        ordertoolbar.setTitle(getResources().getString(R.string.nav_orders));
        setSupportActionBar(ordertoolbar);

        ordertoolbar.setTitleTextColor(Color.WHITE);

        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (LanguageSetting.getLanguage().equals("en")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else if (LanguageSetting.getLanguage().equals("zh")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        ordertoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ExitOrLogoutDialog();
                //                finish();
            }
        });

        InitializeView();


        buildGoogleApiClient();
        createLocationRequest();
        buildLocationSettingsRequest();


        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                DriverOrderListRequestMethod(true);

            }
        };
        LocalBroadcastManager.getInstance(OrdersListsActivity.this).registerReceiver(broadcastReceiver, new IntentFilter("Loc_update_Rec"));


        //        File dstDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "OddfoodyDriverApp");
        //        Log.e("getAbsolutePath", "" + dstDir.getAbsolutePath());


    }

    private void InitializeView() {

        order_list_empty_view = (TextView) findViewById(R.id.order_list_empty_view);

        final FrameLayout frameLayout = (FrameLayout) findViewById(R.id.frame_layout);
        fab_lang_but = (FloatingActionButton) findViewById(R.id.fab_lang_);
        fab_prof_but = (FloatingActionButton) findViewById(R.id.fab_profile_);
        fab_support_but = (FloatingActionButton) findViewById(R.id.fab_support_);
        fab_report_but = (FloatingActionButton) findViewById(R.id.fab_report_);
        fab_log_out_ = (FloatingActionButton) findViewById(R.id.fab_log_out_);
        fab_notifications = (FloatingActionButton) findViewById(R.id.fab_notifications);


        frameLayout.getBackground().setAlpha(0);
        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);
        fabMenu.setOnFloatingActionsMenuUpdateListener(new FloatingActionsMenu.OnFloatingActionsMenuUpdateListener() {
            @Override
            public void onMenuExpanded() {
                frameLayout.getBackground().setAlpha(240);

                frameLayout.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        fabMenu.collapse();
                        return true;
                    }
                });
            }

            @Override
            public void onMenuCollapsed() {
                frameLayout.getBackground().setAlpha(0);
                frameLayout.setOnTouchListener(null);
            }

        });

        fabLanguageClickActionCall();

        fabSupportClickActionCall();

        fabReportClickActionCall();

        fabProfileClickActionCall();

        fabLogoutClickActionCall();

        fabNotificationActionCall();

        order_recycler_view = (RecyclerView) findViewById(R.id.order_recycler_view);

        //        order_recycler_view.getScrollX();

        order_recycler_view.setHasFixedSize(true);
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(OrdersListsActivity.this);
        order_recycler_view.setLayoutManager(linearLayoutManager);
        order_recycler_view.addItemDecoration(new OrderItemDecoration(OrdersListsActivity.this, R.dimen.order_list_item_devider_height));

        List<OrderStatus> orderStatuses = new ArrayList<OrderStatus>();
        orderAdapter = new OrdersAdapter(OrdersListsActivity.this, orderItems, orderStatuses);
        order_recycler_view.setAdapter(orderAdapter);

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.order_list_swipeRefreshLayout);
        //        mSwipeRefreshLayout.setEnabled(false);
        mSwipeRefreshLayout.setColorSchemeColors(R.color.colorAccent, R.color.orange, R.color.green_color, R.color.blue);
        mSwipeRefreshLayout.setOnRefreshListener((new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == 0) {
                    DriverOrderListRequestMethod(false);
                } else if (linearLayoutManager.findFirstCompletelyVisibleItemPosition() == -1) {
                    DriverOrderListRequestMethod(false);
                }

            }
        }));

        DriverOrderListRequestMethod(true);

    }


    public void DriverOrderListRequestMethod(final boolean progress_option) {

        try {

            Log.e("Lang_code", "" + loginPrefManager.getStringValue("Lang_code"));
            Log.e("User_Id", "" + loginPrefManager.getStringValue("User_Id"));

            Log.e("getUserLoginToken", "" + loginPrefManager.getUserLoginToken());
            Log.e("getUserLoginDriverId", "" + loginPrefManager.getUserLoginDriverId());

            if (progress_option) {
                if (progressBarDialog != null) {
                    progressBarDialog.show();
                }
            }

            APIService OrederService = Webdata.getRetrofit().create(APIService.class);
            OrederService.driver_orders("" + loginPrefManager.getStringValue("Lang_code"),
                    "" + loginPrefManager.getUserLoginDriverId(), "" + loginPrefManager.getUserLoginToken()).enqueue(new Callback<DriverOrders>() {
                @Override
                public void onResponse(Call<DriverOrders> call, Response<DriverOrders> response) {

                    try {

                        if (progress_option) {
                            progressBarDialog.dismiss();
                        }

                        Log.e("onResponse", "" + response.raw().toString());
                        Log.e("driver_login_id", "" + loginPrefManager.getUserLoginDriverId());


                        if (orderAdapter != null && response != null) {

                            if (response.body().getResponse().getHttpCode() == 200) {

                                orderItems = (ArrayList<OrderList>) response.body().getResponse().getOrderList();

                                //                            if(response.body().getResponse().getOrderList().size() != 0){}

                                orderAdapter.UpdateOrdersAdapterValues((ArrayList<OrderList>) response.body().getResponse().getOrderList(), response.body().getResponse().getOrderStatus());

                                Log.e("getOrderList", "-" + response.body().getResponse().getOrderList().size());

                                if (response.body().getResponse().getOrderList() != null) {
                                    OrderListEmptyView(response.body().getResponse().getOrderList().size());
                                }

                                //                            OrderListEmptyView(response.body().getResponse().getOrderList().size());

                            } else {
                                //                            OrderListEmptyView(response.body().getResponse().getOrderList().size());
                            }

                        }

                    } catch (Exception e) {
                        Log.e("Exception", "" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<DriverOrders> call, Throwable t) {
                    if (progress_option) {
                        progressBarDialog.dismiss();
                    }
                    Log.e("Order List onFailure", "-" + t.getMessage());
                }
            });

        } catch (Exception e) {
            if (progress_option) {
                progressBarDialog.dismiss();
            }
            Log.e("Exception", "-" + e.getMessage().toString());
        }

        if (!progress_option) {
            mSwipeRefreshLayout.setRefreshing(false);
        }

    }

    private void OrderListEmptyView(int item_count) {

        if (item_count == 0) {
            order_list_empty_view.setVisibility(View.VISIBLE);
        } else {
            order_list_empty_view.setVisibility(View.GONE);
        }

    }


    private void fabLanguageClickActionCall() {
        if (LanguageSetting.getLanguage().equals("en")) {
            fab_lang_but.setTitle(getString(R.string.btn_zh));
        } else {
            fab_lang_but.setTitle(getString(R.string.btn_english));
        }

        fab_lang_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fabMenu.collapse();

                if (LanguageSetting.getLanguage().equals("en")) {
                    loginPrefManager.setStringValue("Lang", "zh");
                    loginPrefManager.setStringValue("Lang_code", "" + loginPrefManager.getStringValue("en_Id"));

                    Intent intent = new Intent(OrdersListsActivity.this, OrdersListsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } else {
                    loginPrefManager.setStringValue("Lang", "en");
                    loginPrefManager.setStringValue("Lang_code", "" + loginPrefManager.getStringValue("en_Id"));

                    Intent intent = new Intent(OrdersListsActivity.this, OrdersListsActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                }

            }
        });


    }


    private void fabSupportClickActionCall() {
        fab_support_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                startActivity(new Intent(OrdersListsActivity.this, SupportActivity.class));
            }
        });

    }

    private void fabReportClickActionCall() {
        fab_report_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();

                Intent intentreport = new Intent(OrdersListsActivity.this, ReportActivity.class);
                //                intentreport.putExtra("","");
                startActivity(intentreport);
            }
        });

    }

    private void fabProfileClickActionCall() {

        fab_prof_but.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                startActivity(new Intent(OrdersListsActivity.this, ProfileActivity.class));
            }
        });

    }

    private void fabLogoutClickActionCall() {

        fab_log_out_.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fabMenu.collapse();
                LogoutDialog();
            }
        });

    }

    private void fabNotificationActionCall() {

        fab_notifications.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fabMenu.collapse();
                Intent notifi_Intent = new Intent(OrdersListsActivity.this, NotificationListActivity.class);
                notifi_Intent.putExtra("notif_conf_dialog", "0");
                startActivity(notifi_Intent);
            }
        });
    }


    @Override
    public void onBackPressed() {
        if (fabMenu.isExpanded()) {
            fabMenu.collapse();
        } else {
            ExitOrLogoutDialog();
        }
    }

    private void ExitOrLogoutDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialog.setTitle("" + getString(R.string.message));
        alertDialog.setMessage("" + getString(R.string.log_out_or_exit_txt));
        alertDialog.setNegativeButton("" + getString(R.string.cancel_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                dialog.dismiss();

            }

        });

        alertDialog.setPositiveButton("" + getString(R.string.ok_btn_txt),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        finish();
                    }
                });
        alertDialog.show();

    }


    private void LogoutDialog() {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, AlertDialog.THEME_DEVICE_DEFAULT_LIGHT);
        alertDialog.setTitle("" + getString(R.string.message));
        alertDialog.setMessage("" + getString(R.string.exit_dialog));
        alertDialog.setNegativeButton("" + getString(R.string.cancel_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDialog.setPositiveButton("" + getString(R.string.ok_btn_txt),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                        loginPrefManager.LogOutClearDataMethod();

                        dialog.dismiss();

                        Intent signInActivity = new Intent(OrdersListsActivity.this, SignInActivity.class);
                        signInActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(signInActivity);

                        finish();
                    }
                });
        alertDialog.show();
    }


    @Override
    protected void onPause() {
        super.onPause();
        System.gc();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        LocalBroadcastManager.getInstance(OrdersListsActivity.this).unregisterReceiver(broadcastReceiver);

        try {

            if (location_intent != null) {
                stopService(location_intent);
            }

        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.e("OrdersListsActivity", "onResume");

        loginPrefManager.setChartDatas(new ArrayList<TitleValueColorEntity>());
        loginPrefManager.setChartDate("");

        loginPrefManager.setStringValue("assigned", "0");
        loginPrefManager.setStringValue("deliverd", "0");

        checkLocationSettings();
    }

    private synchronized void buildGoogleApiClient() {
        Log.e(TAG, "Building GoogleApiClient");
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();

        mGoogleApiClient.connect();
    }

    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(mLocationRequest);
        mLocationSettingsRequest = builder.build();
    }

    private void checkLocationSettings() {
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient, mLocationSettingsRequest);
        result.setResultCallback(this);
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Log.e(TAG, "Connected to GoogleApiClient");
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.e(TAG, "Connection suspended");
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e(TAG, "Connection failed: ConnectionResult.getErrorCode() = " + connectionResult.getErrorCode());
    }

    @Override
    public void onResult(@NonNull LocationSettingsResult locationSettingsResult) {
        final Status status = locationSettingsResult.getStatus();
        switch (status.getStatusCode()) {
            case LocationSettingsStatusCodes.SUCCESS:
                Log.e(TAG, "All location settings are satisfied.");
                LocationServiceStartMethod();
                break;
            case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                Log.e(TAG, "Location settings are not satisfied. Show the user a dialog to" + "upgrade location settings ");
                try {
                    // Show the dialog by calling startResolutionForResult(), and check the result
                    // in onActivityResult().
                    status.startResolutionForResult(OrdersListsActivity.this, REQUEST_CHECK_SETTINGS);
                } catch (IntentSender.SendIntentException e) {
                    Log.e(TAG, "PendingIntent unable to execute request.");
                }
                break;
            case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                Log.e(TAG, "Location settings are inadequate, and cannot be fixed here. Dialog " + "not created.");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        //        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {
        //
        //            if (resultCode == RESULT_OK) {
        //
        //                Place place = PlaceAutocomplete.getPlace(this, data);
        //
        //
        //                Log.e("place", "" + place);
        //                Log.e("data", "Place: " + place.getAddress());
        //                Log.e("lat", "" + place.getLatLng().latitude);
        //                Log.e("lat", "" + place.getLatLng().longitude);
        //
        //
        //                address_text.setText(place.getAddress());
        //
        //            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
        //
        //                Status status = PlaceAutocomplete.getStatus(this, data);
        //                Log.e("data", status.getStatusMessage());
        //
        //            } else if (resultCode == RESULT_CANCELED) {
        //                // The user canceled the operation.
        //            }
        //
        //        }

        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.e(TAG, "User agreed to make required location settings changes.");
                        LocationServiceStartMethod();
                        break;
                    case Activity.RESULT_CANCELED:
                        showExitWarningDialog(OrdersListsActivity.this);
                        Log.e(TAG, "User chose not to make required location settings changes.");
                        break;
                }
                break;
        }

    }

    //      Gps Location Cancel alert
    private void showExitWarningDialog(Context context) {
        android.app.AlertDialog.Builder alertDialog = new android.app.AlertDialog.Builder(context, R.style.MyDialogStyle);
        alertDialog.setTitle("" + getString(R.string.loc_aleart_content_txt));
        alertDialog.setPositiveButton("" + getApplicationContext().getString(R.string.ok_btn_txt), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                checkLocationSettings();
            }
        });
        alertDialog.setNegativeButton("" + getApplicationContext().getString(R.string.cancel_btn_txt),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        finish();
                    }
                });
        alertDialog.show();
    }


    private void LocationServiceStartMethod() {
        if (location_intent == null) {
            location_intent = new Intent(this, LocationService.class);
            location_intent.putExtra("LocBundle", new Bundle());
            startService(location_intent);
        }
    }


    private void ProfileAPICall(String Latitude, String Longitude) {

        if (!NetworkStatus.isConnectingToInternet(getApplicationContext())) {
            return;
        }

        try {

            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.update_driver_location("" + loginPrefManager.getStringValue("Lang_code"),
                    "" + loginPrefManager.getUserLoginDriverId(), "" + loginPrefManager.getUserLoginToken(),
                    "" + Latitude, "" + Longitude, "" + loginPrefManager.getStringValue("device_id"),
                    "" + loginPrefManager.getStringValue("device_token"), "2").enqueue(new Callback<DrivLocRequestMethod>() {
                @Override
                public void onResponse(Call<DrivLocRequestMethod> call, Response<DrivLocRequestMethod> response) {

                    Log.e("onResponse", "-" + response.raw().toString());
                    try {
                        if (response.body().getResponse().getHttpCode() == 200) {
                            Log.e("onResponse  Sucess", "" + response.body().getResponse().getMessage());
                        }
                    } catch (Exception e) {
                        Log.e("onResponse Exception", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<DrivLocRequestMethod> call, Throwable throwable) {
                    Log.e("onFailure", "" + throwable.getMessage());
                }
            });

            Log.e("- " + Latitude, "- " + Longitude);

        } catch (Exception e) {
            Log.e("try Exception", "-" + e.getMessage());
        }

    }


}