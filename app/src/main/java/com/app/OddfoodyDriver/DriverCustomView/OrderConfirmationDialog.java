package com.app.OddfoodyDriver.DriverCustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DotsProgressBar.DDProgressBarDialog;
import com.app.OddfoodyDriver.DriverResponseModel.DriverStatus;
import com.app.OddfoodyDriver.DriverResponseModel.NotificationListResponseDatum;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import ng.max.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nextbrain on 6/6/17.
 */

public class OrderConfirmationDialog extends Dialog {


    private Context mContext;
    private NotificationListResponseDatum notificationListResponseDatum;

    private DDProgressBarDialog progressBarDialog;
    private LoginPrefManager loginPrefManager;

    private Button accept_button, decline_button;
    private TextView title_text, time_text, name_text, address_text;

    private ImageView cancel_dialog_img_btn;

    private SlideView accept_slide_btn;
    private SlideView decline_slide_btn;



    private SimpleDateFormat old_date_format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");  // 2017-06-06 15:47:10
    private SimpleDateFormat new_date_format = new SimpleDateFormat("MMM dd yyyy  HH:mm:ss");


    public OrderConfirmationDialog(Context context, NotificationListResponseDatum notificationListResponseDatum) {
        super(context);
        this.mContext = context;
        this.notificationListResponseDatum = notificationListResponseDatum;
    }

    public OrderConfirmationDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected OrderConfirmationDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.order_confirmation_dialog);

        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);


        loginPrefManager = new LoginPrefManager(mContext);
        progressBarDialog = new DDProgressBarDialog(getContext());

        cancel_dialog_img_btn = (ImageView) findViewById(R.id.cancel_dialog_img_btn);

        accept_button = (Button) findViewById(R.id.btn_accept);
        decline_button = (Button) findViewById(R.id.btn_decline);

        accept_slide_btn = (SlideView) findViewById(R.id.accept_slide_btn);
        decline_slide_btn = (SlideView) findViewById(R.id.decline_slide_btn);

        title_text = (TextView) findViewById(R.id.text_dialog_title);
        time_text = (TextView) findViewById(R.id.text_time);
        name_text = (TextView) findViewById(R.id.text_name);
        address_text = (TextView) findViewById(R.id.text_address);


        try {

            title_text.setText("" + getContext().getString(R.string.order_conf_dia_tit_txt));

            name_text.setText("" + notificationListResponseDatum.getOrderSubject());
            address_text.setText("" + notificationListResponseDatum.getOrderMessage());


            if (!notificationListResponseDatum.getAssignDate().isEmpty()) {
                Date start_date = old_date_format.parse("" + notificationListResponseDatum.getAssignDate());
                time_text.setText("" + new_date_format.format(start_date));
            }


        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

        OnClickEvents();

    }

    public void OnClickEvents() {


        cancel_dialog_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        accept_slide_btn.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                AcceptedApiCall("" + notificationListResponseDatum.getOrderId(),
                        "" + notificationListResponseDatum.getDriverId(),"1", "" + notificationListResponseDatum.getId());
            }
        });


        decline_slide_btn.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
            @Override
            public void onSlideComplete(SlideView slideView) {
                AcceptedApiCall("" + notificationListResponseDatum.getOrderId(),
                        "" + notificationListResponseDatum.getDriverId(),"2", "" + notificationListResponseDatum.getId());
            }
        });


        accept_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptedApiCall("" + notificationListResponseDatum.getOrderId(),
                        "" + notificationListResponseDatum.getDriverId(),"1", "" + notificationListResponseDatum.getId());
            }
        });


        decline_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AcceptedApiCall("" + notificationListResponseDatum.getOrderId(),
                        "" + notificationListResponseDatum.getDriverId(),"2", "" + notificationListResponseDatum.getId());
            }
        });

    }


    private void AcceptedApiCall(String order_id, String driver_id, final String driver_response, String login_id) {

        try {

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }

            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.driver_status_call(order_id, driver_id, driver_response, login_id).enqueue(new Callback<DriverStatus>() {
                @Override
                public void onResponse(Call<DriverStatus> call, Response<DriverStatus> response) {

                    try {

                        progressBarDialog.dismiss();
                        Log.e("onResponse", "" + response.raw().toString());

                        if (response.body().getResponse().getHttpCode() == 200) {

                            if (driver_response.equals("1")) {

                                notificationListResponseDatum.setDriverResponse(1);

                                if (orderConfDialogInterface != null) {
                                    orderConfDialogInterface.OrderConfDialogMethod(notificationListResponseDatum);
                                    dismiss();
                                }

                            } else {

                                notificationListResponseDatum.setDriverResponse(2);

                                if (orderConfDialogInterface != null) {
                                    orderConfDialogInterface.OrderConfDialogMethod(notificationListResponseDatum);
                                    dismiss();
                                }

                            }

                        } else if (response.body().getResponse().getHttpCode() == 400) {
                            Toast.makeText(getContext(), response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressBarDialog.dismiss();
                        Log.e("Exception", "" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<DriverStatus> call, Throwable t) {
                    progressBarDialog.dismiss();
                }
            });


        } catch (Exception e) {
            progressBarDialog.dismiss();
            Log.e("Exception", "" + e.getMessage());
        }

    }


    public OrderConfDialogInterface orderConfDialogInterface;

    public void OrderConfDailCallMethod(OrderConfDialogInterface orderConfDialogInterface) {
        this.orderConfDialogInterface = orderConfDialogInterface;
    }

    public interface OrderConfDialogInterface {
        void OrderConfDialogMethod(NotificationListResponseDatum notificationListResponseDatum);
    }


}
