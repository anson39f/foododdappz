package com.app.OddfoodyDriver.DriverActivities;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverAdapter.OrdDetProdItemAdapter;
import com.app.OddfoodyDriver.DriverAdapter.OrderItemAdapter;
import com.app.OddfoodyDriver.DriverCustomView.CircleImageView;
import com.app.OddfoodyDriver.DriverCustomView.ExpandableHeightListView;
import com.app.OddfoodyDriver.DriverCustomView.SignatureViewDialog;
import com.app.OddfoodyDriver.DriverCustomView.WorkaroundMapFragment;
import com.app.OddfoodyDriver.DriverPhotoPicker.DefaultCallback;
import com.app.OddfoodyDriver.DriverPhotoPicker.EasyImage;
import com.app.OddfoodyDriver.DriverResponseModel.Order;
import com.app.OddfoodyDriver.DriverResponseModel.OrderDetailsReqRes;
import com.app.OddfoodyDriver.DriverResponseModel.OrderReportResp;
import com.app.OddfoodyDriver.DriverResponseModel.TrackingResult;
import com.app.OddfoodyDriver.DriverResponseModel.UpdateStatusRes;
import com.app.OddfoodyDriver.DriverUtils.DirectionsJSONParser;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.ListItemDecorations.OrderItemDecoration;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import ng.max.slideview.SlideView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 27-01-2017.
 */

public class OrderdetailActivity extends LocalizationActivity {

    private Toolbar toolbar;

    private CircleImageView ord_det_customer_img_view;

    private TextView order_det_driver_name_txt_view;
    private TextView pick_location_txt_view;

    private TextView pick_loca_time_txt_view;
    private TextView destin_location_txt_view;

    private TextView destin_locat_time_txt_view;
    private TextView order_det_total_amt_txt_view;

    private TextView ord_det_delivery_charge_txt_view;
    private TextView ord_det_service_tax_txt_view;
    private TextView order_deta_grand_total_txt_view;

    private TextView order_det_payment_amt_txt_view;

    //    private Button order_det_invoice_btn;
    private Button order_det_call_btn;


    private TextView order_det_initiated_txt_view;
    private TextView order_det_precessed_txt_view;
    private TextView order_det_packed_txt_view;
    private TextView order_det_dispatched_txt_view;
    private TextView order_det_delivery_txt_view;
    private TextView order_det_deliv_request_txt_view;


    private ImageView ord_det_featured_image_view;
    private TextView order_det_venter_name_txt_view;
    private TextView order_det_outlet_name_txt_view;


    private ArrayList<Order> orderItems;


    ExpandableHeightListView orderlistview;
    private String[] order_item;
    private int[] order_quantity;
    private int[] order_total;
    OrderItemAdapter orderItemAdapter;

    Button order_det_status_btn;

    private String Order_date, OrderId;
    private String Order_id = "";

    private ImageView make_call;
    private TextView Order_person_name, Order_person_number;

    GoogleMap map;
    ArrayList<LatLng> markerPoints;
    private LinearLayout status_layout;

    private LocationManager locationManager;
    LocationListener networkLocationListener;
    LocationListener gpsLocationListener;
    boolean visibility_Flag = false;

    private LinearLayout states_dialog_style_layout;


    private String user_phone_no = "";

    private String invoic_pdf = "";


    SimpleDateFormat old_format = new SimpleDateFormat("MMM dd yyyy hh:mm a", Locale.ENGLISH);
    SimpleDateFormat new_format = new SimpleDateFormat("hh:mm a", Locale.ENGLISH);


    private BroadcastReceiver my_order_receiver;


    private RecyclerView order_product_recy_view;
    private OrdDetProdItemAdapter ordDetProdItemAdapter;

    private NestedScrollView ord_det_nested_scroll_view;
    private SlideView ord_det_deliver_slide_btn;

    private LinearLayout ord_det_sig_layout;
    private TextView ord_det_sig_add_txt_view;
    private ImageView order_signature_view;
    private TextView ord_det_images_add_txt_view;
    private TextView click_txt_view;

    private LinearLayout ord_det_att_image_layout;
    private ImageView ord_det_attached_image_view;
    private LinearLayout ord_det_delive_slide_btn_lay;

    private String order_status = "";

    File attachment_file;

    private Runnable runnable;

    public SignatureViewDialog signatureViewDialog;

    public String Order_signature_image = "";


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

//        Log.e("Order_ID", "-" + getIntent().hasExtra("Order_ID"));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
//            Order_date = extras.getString("Order_Date");
            Order_id = extras.getString("Order_ID");
        }


        toolbar = (Toolbar) findViewById(R.id.order_detail_toolbar_id);
//        toolbar.setTitle("" + Order_date);
        toolbar.setSubtitle(getResources().getString(R.string.ordet_item_order_id_txt) + " " + Order_id);
        setSupportActionBar(toolbar);

        // getResources().getString(R.string.ordet_item_order_id_txt)

        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);


        EasyImage.configuration(this)
                .setImagesFolderName("OddfoodyDriverApp")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);


        ord_det_nested_scroll_view = (NestedScrollView) findViewById(R.id.ord_det_nested_scroll_view);

        WorkaroundMapFragment fm = (WorkaroundMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        fm.setListener(new WorkaroundMapFragment.OnTouchListener() {

            @Override
            public void onTouch() {
                ord_det_nested_scroll_view.requestDisallowInterceptTouchEvent(true);
            }
        });

        // Getting Map for the SupportMapFragment
        map = fm.getMap();


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

        OrderDetailView();


        my_order_receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                OrderDetailsRequestMethod();

            }
        };
        LocalBroadcastManager.getInstance(OrderdetailActivity.this).registerReceiver(my_order_receiver, new IntentFilter("driver_order_list"));

    }

    private void OrderDetailView() {

        ord_det_customer_img_view = (CircleImageView) findViewById(R.id.ord_det_customer_img_view);

        order_det_driver_name_txt_view = (TextView) findViewById(R.id.order_det_driver_name_txt_view);

        destin_location_txt_view = (TextView) findViewById(R.id.destin_location_txt_view);
        pick_loca_time_txt_view = (TextView) findViewById(R.id.pick_loca_time_txt_view);

        pick_location_txt_view = (TextView) findViewById(R.id.pick_location_txt_view);

        destin_locat_time_txt_view = (TextView) findViewById(R.id.destin_locat_time_txt_view);
        order_det_total_amt_txt_view = (TextView) findViewById(R.id.order_det_total_amt_txt_view);
        ord_det_delivery_charge_txt_view = (TextView) findViewById(R.id.ord_det_delivery_charge_txt_view);
        ord_det_service_tax_txt_view = (TextView) findViewById(R.id.ord_det_service_tax_txt_view);
        order_deta_grand_total_txt_view = (TextView) findViewById(R.id.order_deta_grand_total_txt_view);

        order_det_payment_amt_txt_view = (TextView) findViewById(R.id.order_det_payment_amt_txt_view);

        //  SlideView

        ord_det_deliver_slide_btn = (SlideView) findViewById(R.id.ord_det_deliver_slide_btn);

        ord_det_sig_layout = (LinearLayout) findViewById(R.id.ord_det_sig_layout);
        ord_det_sig_add_txt_view = (TextView) findViewById(R.id.ord_det_sig_add_txt_view);
        order_signature_view = (ImageView) findViewById(R.id.signature_view_imageview);
        click_txt_view = (TextView) findViewById(R.id.click_here);
        ord_det_images_add_txt_view = (TextView) findViewById(R.id.ord_det_images_add_txt_view);

        ord_det_att_image_layout = (LinearLayout) findViewById(R.id.ord_det_att_image_layout);
        ord_det_attached_image_view = (ImageView) findViewById(R.id.ord_det_attached_image_view);
        ord_det_delive_slide_btn_lay = (LinearLayout) findViewById(R.id.ord_det_delive_slide_btn_lay);


//        order_det_invoice_btn = (Button) findViewById(R.id.order_det_invoice_btn);
        order_det_status_btn = (Button) findViewById(R.id.order_det_status_btn);
        order_det_call_btn = (Button) findViewById(R.id.order_det_call_btn);


        order_det_initiated_txt_view = (TextView) findViewById(R.id.order_det_initiated_txt_view);
        order_det_precessed_txt_view = (TextView) findViewById(R.id.order_det_precessed_txt_view);
        order_det_packed_txt_view = (TextView) findViewById(R.id.order_det_packed_txt_view);
        order_det_dispatched_txt_view = (TextView) findViewById(R.id.order_det_dispatched_txt_view);
        order_det_delivery_txt_view = (TextView) findViewById(R.id.order_det_delivery_txt_view);
        order_det_deliv_request_txt_view = (TextView) findViewById(R.id.order_det_deliv_request_txt_view);


        ord_det_featured_image_view = (ImageView) findViewById(R.id.ord_det_featured_image_view);
        order_det_venter_name_txt_view = (TextView) findViewById(R.id.order_det_venter_name_txt_view);
        order_det_outlet_name_txt_view = (TextView) findViewById(R.id.order_det_outlet_name_txt_view);


        orderlistview = (ExpandableHeightListView) findViewById(R.id.order_expand_list);

        order_product_recy_view = (RecyclerView) findViewById(R.id.order_product_recy_view);
        order_product_recy_view.setHasFixedSize(true);
        order_product_recy_view.setLayoutManager(new LinearLayoutManager(OrderdetailActivity.this));
        order_product_recy_view.addItemDecoration(new OrderItemDecoration(OrderdetailActivity.this, R.dimen.order_list_item_devider_height));


        make_call = (ImageView) findViewById(R.id.call_icon_view);

        Order_person_name = (TextView) findViewById(R.id.person_name);
        Order_person_number = (TextView) findViewById(R.id.person_number);

        order_item = getResources().getStringArray(R.array.order_item_array);
        order_quantity = getResources().getIntArray(R.array.order_quan_array);
        order_total = getResources().getIntArray(R.array.order_amount_array);


        states_dialog_style_layout = (LinearLayout) findViewById(R.id.states_dialog_style_layout);
        status_layout = (LinearLayout) findViewById(R.id.status_layout_view);

        LayoutInflater myinflater = getLayoutInflater();
        ViewGroup myHeader = (ViewGroup) myinflater.inflate(R.layout.activity_listview_header, orderlistview, false);
        orderlistview.addHeaderView(myHeader, null, false);

        orderItems = new ArrayList<Order>();
        orderItemAdapter = new OrderItemAdapter(OrderdetailActivity.this, orderItems);
        orderlistview.setAdapter(orderItemAdapter);
        orderlistview.setExpanded(true);

        ordDetProdItemAdapter = new OrdDetProdItemAdapter(OrderdetailActivity.this, orderItems);
        order_product_recy_view.setAdapter(ordDetProdItemAdapter);

        StatusBtnClickActionCall();
        MakeCallClickAction();

        OrderDetailsRequestMethod();

        SignatureViewDialogActionCall();

    }

    private void SignatureViewDialogActionCall() {

        click_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                signatureViewDialog = new SignatureViewDialog(OrderdetailActivity.this, Order_signature_image);
                signatureViewDialog.show();

            }
        });
    }

    private void StatusBtnClickActionCall() {

        ord_det_deliver_slide_btn.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {

                DeliverySucessRequestMethod();
            }
        });


        ord_det_sig_add_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent signature_intent = new Intent(OrderdetailActivity.this, SignatureActivity.class);
                signature_intent.putExtra("order_id", "" + Order_id);
                startActivity(signature_intent);

            }
        });

        ord_det_images_add_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EasyImage.openChooserWithGallery(OrderdetailActivity.this, "" + getString(R.string.pick_image_intent_text), 2);
            }
        });

        order_det_status_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (visibility_Flag) {
                    status_layout.setVisibility(View.GONE);
                    states_dialog_style_layout.setVisibility(View.GONE);
                    visibility_Flag = false;
                } else {
                    status_layout.setVisibility(View.VISIBLE);
                    states_dialog_style_layout.setVisibility(View.VISIBLE);
                    visibility_Flag = true;
                }

            }
        });


        order_det_call_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Order_person_number.getText().toString().isEmpty()) {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:" + user_phone_no));
                    if (ActivityCompat.checkSelfPermission(OrderdetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(phoneIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "" + getString(R.string.ord_det_pahone_no_err_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    private void MakeCallClickAction() {

        make_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Order_person_number.getText().toString().isEmpty()) {
                    Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                    phoneIntent.setData(Uri.parse("tel:" + user_phone_no));
                    if (ActivityCompat.checkSelfPermission(OrderdetailActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    startActivity(phoneIntent);
                } else {
                    Toast.makeText(getApplicationContext(), "" + getString(R.string.ord_det_pahone_no_err_msg), Toast.LENGTH_SHORT).show();
                }
            }
        });


        order_det_deliv_request_txt_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DeliverySucessRequestMethod();
                Intent signature_intent = new Intent(OrderdetailActivity.this, SignatureActivity.class);
                signature_intent.putExtra("order_id", "" + Order_id);
                startActivity(signature_intent);
            }
        });

    }


    private void OrderDetailsRequestMethod() {

        try {

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }

            Log.e("Lang_code", "" + loginPrefManager.getStringValue("Lang_code"));
            Log.e("getUserLoginDriverId", "" + loginPrefManager.getUserLoginDriverId());
            Log.e("getUserLoginToken", "" + loginPrefManager.getUserLoginToken());
            Log.e("Order_id", "" + Order_id);

            APIService order_details_request = Webdata.getRetrofit().create(APIService.class);
            order_details_request.driver_order_detail("" + loginPrefManager.getStringValue("Lang_code"),
                    "" + loginPrefManager.getUserLoginDriverId(), "" + loginPrefManager.getUserLoginToken(),
                    "" + Order_id).enqueue(new Callback<OrderDetailsReqRes>() {
                @Override
                public void onResponse(Call<OrderDetailsReqRes> call, final Response<OrderDetailsReqRes> response) {

                    try {

                        progressBarDialog.dismiss();

                        Log.e("OrderDetaRequestMethod", "" + response.raw().toString());

                        if (response.body().getResponse().getHttpCode() == 200) {

                            toolbar.setTitle("" + response.body().getResponse().getOrderDetaList().get(0).getCreatedDate());
//                            toolbar.setSubtitle("" + loginPrefManager.getStringValue("order_id"));

                            Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getUserImage()).centerCrop().error(R.color.app_background_color).into(ord_det_customer_img_view);
                            String user_name = "" + response.body().getResponse().getOrderDetaList().get(0).getUserFirstName() + " " + response.body().getResponse().getOrderDetaList().get(0).getUserLastName();
                            Order_person_name.setText("" + user_name);

                            user_phone_no = "" + response.body().getResponse().getOrderDetaList().get(0).getUserMobile();

                            Order_person_number.setText("" + user_phone_no);

                            String driver_name = "" + response.body().getResponse().getOrderDetaList().get(0).getDriverFirstName() + " " + response.body().getResponse().getOrderDetaList().get(0).getDriverLastName();
                            order_det_driver_name_txt_view.setText("" + driver_name);


                            Log.e("getFeaturedImage", "" + response.body().getResponse().getOrderDetaList().get(0).getFeaturedImage());
                            Log.e("getVendor_name", "" + response.body().getResponse().getOrderDetaList().get(0).getVendor_name());
                            Log.e("getOutlet_name", "" + response.body().getResponse().getOrderDetaList().get(0).getOutlet_name());


                            // Vender and outlet name and image values updated.
                            Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getFeaturedImage()).bitmapTransform(new CenterCrop(OrderdetailActivity.this), new RoundedCornersTransformation(OrderdetailActivity.this, 5, 0)).error(R.color.app_background_color).into(ord_det_featured_image_view);
//                            Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getFeaturedImage()).centerCrop().error(R.color.app_background_color).into(ord_det_featured_image_view);
                            order_det_venter_name_txt_view.setText("" + response.body().getResponse().getOrderDetaList().get(0).getVendor_name());
                            order_det_outlet_name_txt_view.setText("" + response.body().getResponse().getOrderDetaList().get(0).getOutlet_name());


                            pick_location_txt_view.setText("" + response.body().getResponse().getOrderDetaList().get(0).getOutletAddress());
                            destin_location_txt_view.setText("" + response.body().getResponse().getOrderDetaList().get(0).getUserAddress());

                            order_status = "" + response.body().getResponse().getOrderDetaList().get(0).getOrderStatus();

                            if (response.body().getResponse().getOrderDetaList().get(0).getOrderStatus() == 12) {
                                pick_loca_time_txt_view.setText("");
                                destin_locat_time_txt_view.setText("");
                            }

                            invoic_pdf = "" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getInvoicPdf();

                            orderItems = (ArrayList<Order>) response.body().getResponse().getOrderDetaList().get(0).getOrders();
                            if (orderItems.size() != 0) {
                                ordDetProdItemAdapter.UpdateArrayList(orderItems);
                            }

                             /*Vender and outlet name and image values updated.*/

                            Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getFeaturedImage()).bitmapTransform(new CenterCrop(OrderdetailActivity.this), new RoundedCornersTransformation(OrderdetailActivity.this, 5, 0)).error(R.color.app_background_color).into(ord_det_featured_image_view);
//                            Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getFeaturedImage()).centerCrop().error(R.color.app_background_color).into(ord_det_featured_image_view);
                            order_det_venter_name_txt_view.setText("" + response.body().getResponse().getOrderDetaList().get(0).getVendor_name());
                            order_det_outlet_name_txt_view.setText("" + response.body().getResponse().getOrderDetaList().get(0).getOutlet_name());


//                            order_det_total_amt_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + response.body().getResponse().getOrderDetaList().get(0).getTotalAmount()));

//                            order_det_total_amt_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + price));
                            ord_det_delivery_charge_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getDeliveryCharge()));
                            ord_det_service_tax_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getServiceTax()));


//                            int grand_total = ((Math.round((price)) +  Math.round(Float.parseFloat("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getDeliveryCharge())) +
//                                    Math.round(Float.parseFloat("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getServiceTax()))));
//                            int grand_total = ((Math.round(Float.parseFloat("" + response.body().getResponse().getOrderDetaList().get(0).getTotalAmount())) +
//                                    Math.round(Float.parseFloat("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getServiceTax()))));

//                                                       String grand_total = "" + (Integer.parseInt(response.body().getResponse().getOrderDetaList().get(0).getTotalAmount()) + Integer.parseInt(response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getServiceTax()));
//                            order_deta_grand_total_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + grand_total));


//                            order_det_payment_amt_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + grand_total));


                            if (response.body().getResponse().getOrderDetaList().get(0).getOrderStatus() == 19) {
                                order_det_deliv_request_txt_view.setVisibility(View.VISIBLE);
                            }

                            for (int i = 0; i < response.body().getResponse().getOrderDetaList().get(0).getTrackingResult().size(); i++) {

                                TrackingResult trackingResult = response.body().getResponse().getOrderDetaList().get(0).getTrackingResult().get(i);

                                if (trackingResult.getText().equals("Initiated")) {

                                    if (trackingResult.getProcess().equals("1")) {
                                        order_det_initiated_txt_view.setVisibility(View.VISIBLE);

                                        order_det_initiated_txt_view.setText("" + getResources().getString(R.string.initiated_text) + " on " + trackingResult.getDate());

                                    } else if (trackingResult.getProcess().equals("0")) {
                                        order_det_initiated_txt_view.setVisibility(View.GONE);
                                    }

                                }

                                if (trackingResult.getText().equals("Processed")) {

                                    if (trackingResult.getProcess().equals("1")) {
                                        order_det_precessed_txt_view.setVisibility(View.VISIBLE);

                                        order_det_precessed_txt_view.setText("" + getResources().getString(R.string.processed_text) + " on " + trackingResult.getDate());

                                    } else if (trackingResult.getProcess().equals("0")) {
                                        order_det_precessed_txt_view.setVisibility(View.GONE);
                                    }

                                }

                                if (trackingResult.getText().equals("Packed")) {

                                    if (trackingResult.getProcess().equals("1")) {

                                        order_det_packed_txt_view.setVisibility(View.VISIBLE);
                                        order_det_packed_txt_view.setText("" + getResources().getString(R.string.packed_text) + " on " + trackingResult.getDate());

                                    } else if (trackingResult.getProcess().equals("0")) {
                                        order_det_packed_txt_view.setVisibility(View.GONE);
                                    }

                                }

                                if (trackingResult.getText().equals("Dispatched")) {

                                    if (trackingResult.getProcess().equals("1")) {
                                        order_det_dispatched_txt_view.setVisibility(View.VISIBLE);

                                        order_det_dispatched_txt_view.setText("" + getResources().getString(R.string.dispatch_text) + " on " + trackingResult.getDate());

                                        order_det_deliv_request_txt_view.setVisibility(View.VISIBLE);

                                    } else if (trackingResult.getProcess().equals("0")) {
                                        order_det_dispatched_txt_view.setVisibility(View.GONE);
                                    }

                                }

                                if (trackingResult.getText().equals("Delivered")) {

                                    if (trackingResult.getProcess().equals("1")) {
                                        order_det_delivery_txt_view.setVisibility(View.VISIBLE);

                                        order_det_delivery_txt_view.setText("" + getResources().getString(R.string.delivered_text) + " on " + trackingResult.getDate());

                                        order_det_deliv_request_txt_view.setVisibility(View.GONE);

                                    } else if (trackingResult.getProcess().equals("0")) {
                                        order_det_delivery_txt_view.setVisibility(View.GONE);
                                    }

                                }

                            }


                            LatLng user_latLng = null;


                            if (!response.body().getResponse().getOrderDetaList().get(0).getUserLatitude().isEmpty() && !response.body().getResponse().getOrderDetaList().get(0).getUserLongitude().isEmpty()) {
                                double user_latitude = Double.parseDouble("" + response.body().getResponse().getOrderDetaList().get(0).getUserLatitude());
                                double user_longitude = Double.parseDouble("" + response.body().getResponse().getOrderDetaList().get(0).getUserLongitude());
                                user_latLng = new LatLng(user_latitude, user_longitude);

                            }

                            LatLng outlet_latLng = null;

                            if (!response.body().getResponse().getOrderDetaList().get(0).getOutletLatitude().isEmpty() && !response.body().getResponse().getOrderDetaList().get(0).getOutletLongtitude().isEmpty()) {
                                double outlet_latitude = Double.parseDouble("" + response.body().getResponse().getOrderDetaList().get(0).getOutletLatitude());
                                double outlet_longitude = Double.parseDouble("" + response.body().getResponse().getOrderDetaList().get(0).getOutletLongtitude());
                                outlet_latLng = new LatLng(outlet_latitude, outlet_longitude);

                            }

                            if (user_latLng != null && outlet_latLng == null) {


                                if (map != null) {

                                    // Enable MyLocation Button in the Map
                                    if (ActivityCompat.checkSelfPermission(OrderdetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderdetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }

                                    map.addMarker(new MarkerOptions().position(user_latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
                                }


                            }
                            if (user_latLng == null || outlet_latLng != null) {

                                if (map != null) {

                                    // Enable MyLocation Button in the Map
                                    if (ActivityCompat.checkSelfPermission(OrderdetailActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(OrderdetailActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                                        // TODO: Consider calling
                                        //    ActivityCompat#requestPermissions
                                        // here to request the missing permissions, and then overriding
                                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                        //                                          int[] grantResults)
                                        // to handle the case where the user grants the permission. See the documentation
                                        // for ActivityCompat#requestPermissions for more details.
                                        return;
                                    }

                                    MarkerOptions option = new MarkerOptions();
                                    option.position(outlet_latLng).icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));


                                    map.addMarker(option);

                                }
                            }

                            if (user_latLng != null && outlet_latLng != null) {
                                MapLocaion(user_latLng, outlet_latLng);
                            }



//                            response.body().getResponse().getOrderDetaList().get(0).setDigital_signature("");  // Testing ---------------



                            Order_signature_image = response.body().getResponse().getOrderDetaList().get(0).getDigital_signature();



                            if (response.body().getResponse().getOrderDetaList().get(0).getOrderStatus() == 12) {

                                if (response.body().getResponse().getOrderDetaList().get(0).getDigital_signature().isEmpty()) {
                                    ord_det_sig_add_txt_view.setText("" + getString(R.string.ord_det_sig_add_btn_txt));
                                    ord_det_sig_layout.setVisibility(View.VISIBLE);
                                } else {
//                                    ord_det_sig_layout.setVisibility(View.GONE);
                                    ord_det_sig_add_txt_view.setVisibility(View.GONE);
                                    order_signature_view.setVisibility(View.VISIBLE);

                                    click_txt_view.setVisibility(View.VISIBLE);

                                    Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getDigital_signature()).listener(new RequestListener<String, GlideDrawable>() {
                                        @Override
                                        public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
//                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }

                                        @Override
                                        public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
//                                            progressBar.setVisibility(View.GONE);
                                            return false;
                                        }
                                    }).into(order_signature_view);
                                }

                                ord_det_delive_slide_btn_lay.setVisibility(View.GONE);

                            } else {
                                ord_det_sig_layout.setVisibility(View.VISIBLE);
                                ord_det_delive_slide_btn_lay.setVisibility(View.VISIBLE);
                            }


                            if (response.body().getResponse().getOrderDetaList().get(0).getDigital_signature().isEmpty()) {
                                ord_det_sig_add_txt_view.setText("" + getString(R.string.ord_det_sig_add_btn_txt));
                            } else {
                                ord_det_sig_add_txt_view.setText("" + getString(R.string.ord_det_sig_update_btn_txt));
                            }


                            if (response.body().getResponse().getOrderDetaList().get(0).getOrder_attachment().isEmpty()) {
                                ord_det_att_image_layout.setVisibility(View.GONE);
                                ord_det_images_add_txt_view.setText("" + getString(R.string.ord_det_sig_add_btn_txt));
                            } else {
                                ord_det_images_add_txt_view.setText("" + getString(R.string.ord_det_sig_update_btn_txt));
                                ord_det_att_image_layout.setVisibility(View.VISIBLE);
                                Glide.with(OrderdetailActivity.this).load("" + response.body().getResponse().getOrderDetaList().get(0).getOrder_attachment()).centerCrop().error(R.color.app_background_color).into(ord_det_attached_image_view);
                            }


                            order_det_deliv_request_txt_view.setVisibility(View.GONE);


                            runnable = new Runnable() {
                                @Override
                                public void run() {

                                    if (ordDetProdItemAdapter != null) {

                                        ordDetProdItemAdapter.notifyDataSetChanged();

                                        Log.e("getSubTotalAmount", "-" + ordDetProdItemAdapter.getSubTotalAmount());

                                        order_det_total_amt_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + ordDetProdItemAdapter.getSubTotalAmount()));


                                        Float delivery_charge_tax = ((Float.parseFloat("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getDeliveryCharge()) +
                                                Float.parseFloat("" + response.body().getResponse().getOrderDetaList().get(0).getOrders().get(0).getServiceTax())));

                                        Float grand_final_total = (Float.parseFloat("" + ordDetProdItemAdapter.getSubTotalAmount()) + delivery_charge_tax);


                                        order_deta_grand_total_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + grand_final_total.toString()));
                                        order_det_payment_amt_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + grand_final_total.toString()));

                                    }

                                }
                            };
                            new Handler().postDelayed(runnable, 1000);


                        } else {

                        }

                    } catch (Exception e) {

                        progressBarDialog.dismiss();

                    }

                }

                @Override
                public void onFailure(Call<OrderDetailsReqRes> call, Throwable throwable) {
                    progressBarDialog.dismiss();

                    Log.e("onFailure", "" + throwable.getMessage());
                }
            });

        } catch (Exception e) {
            progressBarDialog.dismiss();
            Log.e("OrderDetailsReqMethod", "-" + e.getMessage());
        }
    }

    private void EmailOrderInvoiceRequestMethod() {

        try {

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }

            APIService order_email_invoice = Webdata.getRetrofit().create(APIService.class);
            order_email_invoice.driver_email_invoice("", "", "", "").enqueue(new Callback<OrderReportResp>() {
                @Override
                public void onResponse(Call<OrderReportResp> call, Response<OrderReportResp> response) {

                    progressBarDialog.dismiss();

                }

                @Override
                public void onFailure(Call<OrderReportResp> call, Throwable t) {
                    progressBarDialog.dismiss();
                }
            });


        } catch (Exception e) {
            progressBarDialog.dismiss();
        }

    }


    private void DeliverySucessRequestMethod() {

        try {

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }


            RequestBody language = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getStringValue("Lang_code"));
            RequestBody driver_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getUserLoginDriverId());
            RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getUserLoginToken());
            RequestBody order_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + Order_id);
            RequestBody order_status = RequestBody.create(MediaType.parse("multipart/form-data"), "12");

//            String uriStr = "android.resource://" + "com.app.yo9lekdriver" + "/" + R.drawable.signature_img;
//            File imageFile = new File(uriStr);
//
//            Log.e("imageFile", "" + imageFile.getAbsolutePath());

//            Uri path = Uri.parse("android.resource://com.app.yo9lekdriver/signature_img");
//            File imageFiletype_two = new File(path);

//            if (!imageFile.exists() && !imageFile.canRead()) {
//                progressBarDialog.dismiss();
//                return;
//            }

//            Log.e("imageFile", "" + imageFile.getAbsolutePath());
//
//            // create RequestBody instance from file
//            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imageFile.getAbsoluteFile());
//            // MultipartBody.Part is used to send also the actual file name
//            final MultipartBody.Part ProfImgFile = MultipartBody.Part.createFormData("digital_signature", imageFile.getName(), requestFile);


            APIService order_deliverd_request = Webdata.getRetrofit().create(APIService.class);
            order_deliverd_request.change_order_status_with_out_img(language, driver_id, token, order_id, order_status).enqueue(new Callback<UpdateStatusRes>() {
                @Override
                public void onResponse(Call<UpdateStatusRes> call, Response<UpdateStatusRes> response) {

                    progressBarDialog.dismiss();

                    Log.e("onResponse", "" + response.raw().toString());

                    if (response.body().getResponse().getHttpCode() == 200) {
                        OrderDetailsRequestMethod();
                    }

                }

                @Override
                public void onFailure(Call<UpdateStatusRes> call, Throwable t) {
                    progressBarDialog.dismiss();
                }
            });


        } catch (Exception e) {
            progressBarDialog.dismiss();
        }

    }


    private void MapLocaion(LatLng origin, LatLng dest) {

        // Initializing
        markerPoints = new ArrayList<LatLng>();

        markerPoints.add(origin);
        markerPoints.add(dest);

        // Getting reference to SupportMapFragment of the activity_main
        SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        // Getting Map for the SupportMapFragment
        map = fm.getMap();

        if (map != null) {

            // Enable MyLocation Button in the Map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            map.setMyLocationEnabled(true);

            // Setting onclick event listener for the map
            map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                @Override
                public void onMapClick(LatLng point) {
                }

            });

            SetMarkerLocation(origin, dest);


        }
    }

    private void SetMarkerLocation(LatLng start, LatLng end) {

//        // Already two locations
//        if (markerPoints.size() > 1) {
//            markerPoints.clear();
//            map.clear();
//        }
//
//        // Adding new item to the ArrayList
//        markerPoints.add(point);

        // Creating MarkerOptions
        MarkerOptions options = new MarkerOptions();
        // Setting the position of the marker
        options.position(start);

        // Creating MarkerOptions
        MarkerOptions options_end = new MarkerOptions();
        // Setting the position of the marker
        options_end.position(end);

        /**
         * For the start location, the color of marker is GREEN and
         * for the end location, the color of marker is RED.
         */
        if (markerPoints.size() == 1) {
            options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        } else if (markerPoints.size() == 2) {
            options_end.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
        }

        // Add new marker to the Google Map Android API V2
        map.addMarker(options);
        map.addMarker(options_end);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(start, 11));


        // Checks, whether start and end locations are captured
        if (markerPoints.size() >= 2) {
            LatLng origin = markerPoints.get(0);
            LatLng dest = markerPoints.get(1);

            // Getting URL to the Google Directions API
            String url = getDirectionsUrl(origin, dest);

            DownloadTask downloadTask = new DownloadTask();

            // Start downloading json data from Google Directions API
            downloadTask.execute(url);
        }

    }

    private String getDirectionsUrl(LatLng origin, LatLng dest) {

        // Origin of route
        String str_origin = "origin=" + origin.latitude + "," + origin.longitude;

        // Destination of route
        String str_dest = "destination=" + dest.latitude + "," + dest.longitude;

        // Sensor enabled
        String sensor = "sensor=false";

        // Building the parameters to the web service
        String parameters = str_origin + "&" + str_dest + "&" + sensor;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/" + output + "?" + parameters;

        return url;
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {

        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";

            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
//            Log.d("Exception while downloading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches data from url passed
    private class DownloadTask extends AsyncTask<String, Void, String> {

        // Downloading data in non-ui thread
        @Override
        protected String doInBackground(String... url) {

            // For storing data from web service
            String data = "";

            try {
                // Fetching the data from web service
                data = downloadUrl(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        // Executes in UI thread, after the execution of
        // doInBackground()
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            ParserTask parserTask = new ParserTask();

            // Invokes the thread for parsing the JSON data
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<List<HashMap<String, String>>>> {

        // Parsing the data in non-ui thread
        @Override
        protected List<List<HashMap<String, String>>> doInBackground(String... jsonData) {

            JSONObject jObject;
            List<List<HashMap<String, String>>> routes = null;

            try {
                jObject = new JSONObject(jsonData[0]);

                DirectionsJSONParser parser = new DirectionsJSONParser();

                // Starts parsing data
                routes = parser.parse(jObject);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return routes;
        }

        // Executes in UI thread, after the parsing process
        @Override
        protected void onPostExecute(List<List<HashMap<String, String>>> result) {

            ArrayList<LatLng> points = null;
            PolylineOptions lineOptions = null;
            MarkerOptions markerOptions = new MarkerOptions();

            // Traversing through all the routes
            for (int i = 0; i < result.size(); i++) {
                points = new ArrayList<LatLng>();
                lineOptions = new PolylineOptions();

                // Fetching i-th route
                List<HashMap<String, String>> path = result.get(i);

                // Fetching all the points in i-th route
                for (int j = 0; j < path.size(); j++) {
                    HashMap<String, String> point = path.get(j);

                    double lat = Double.parseDouble(point.get("lat"));
                    double lng = Double.parseDouble(point.get("lng"));
                    LatLng position = new LatLng(lat, lng);

                    points.add(position);
                }

                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(5);
                lineOptions.color(Color.RED);
            }

            // Drawing polyline in the Google Map for the i-th route
            if (lineOptions != null) {
                map.addPolyline(lineOptions);
            }

        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Log.e("type", "" + type);
                if (type == 2) {
                    onPhotosDrivingReturned(imageFiles);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(OrderdetailActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

    }

    private void onPhotosDrivingReturned(List<File> imageFiles) {

        attachment_file = imageFiles.get(0).getAbsoluteFile();
//        Log.e("Attachments path", "" + attachment_file.getAbsolutePath());
//        tick_image.setVisibility(View.VISIBLE);

        if (!attachment_file.exists() && !attachment_file.canRead()) {
            Toast.makeText(OrderdetailActivity.this, "Can not read attachement", Toast.LENGTH_SHORT).show();
            return;
        } else {
            UpdateAttachemntRequestMethod();
        }

    }


    private void UpdateAttachemntRequestMethod() {

        try {

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }

            RequestBody language = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getStringValue("Lang_code"));
            RequestBody driver_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getUserLoginDriverId());
            RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getUserLoginToken());
            RequestBody order_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + Order_id);
            RequestBody order_status_value = RequestBody.create(MediaType.parse("multipart/form-data"), "" + order_status);

            // attacched document file
            RequestBody upload_attachment = RequestBody.create(MediaType.parse("multipart/form-data"), attachment_file);
            final MultipartBody.Part order_attachment = MultipartBody.Part.createFormData("order_attachment", attachment_file.getName(), upload_attachment);


            APIService order_deliverd_request = Webdata.getRetrofit().create(APIService.class);
            order_deliverd_request.update_order_attachement(language, driver_id, token, order_id, order_status_value,
                    order_attachment).enqueue(new Callback<UpdateStatusRes>() {
                @Override
                public void onResponse(Call<UpdateStatusRes> call, Response<UpdateStatusRes> response) {

                    try {

                        progressBarDialog.dismiss();
                        Log.e("onResponse", "" + response.raw().toString());

                        if (response.body().getResponse().getHttpCode() == 200) {

                            OrderDetailsRequestMethod();

                            Toast.makeText(OrderdetailActivity.this, "" + response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                        } else {
                            Toast.makeText(OrderdetailActivity.this, "" + response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressBarDialog.dismiss();
                        Log.e("Exception", "" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<UpdateStatusRes> call, Throwable t) {
                    progressBarDialog.dismiss();
                }
            });

        } catch (Exception e) {
            progressBarDialog.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        clearImageData();
        LocalBroadcastManager.getInstance(OrderdetailActivity.this).unregisterReceiver(my_order_receiver);
        super.onDestroy();
    }

    private void clearImageData() {

        try {

            File dstDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "OddfoodyDriverApp");

            if (dstDir.isDirectory()) {

                String[] children = dstDir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dstDir, children[i]).delete();
                }

                dstDir.delete();

            }

        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

    }

}

