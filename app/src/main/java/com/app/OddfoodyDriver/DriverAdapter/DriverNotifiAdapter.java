package com.app.OddfoodyDriver.DriverAdapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DotsProgressBar.DDProgressBarDialog;
import com.app.OddfoodyDriver.DriverCustomView.CircleImageView;
import com.app.OddfoodyDriver.DriverResponseModel.DriverStatus;
import com.app.OddfoodyDriver.DriverResponseModel.NotificationListResponseDatum;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.R;
import com.bumptech.glide.Glide;

import java.util.List;

import ng.max.slideview.SlideView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nextbrain on 7/6/17.
 */

public class DriverNotifiAdapter extends RecyclerView.Adapter<DriverNotifiAdapter.DriverNotifiAdaViewHolder> {

    private Context context;
    private DDProgressBarDialog ddProgressBarDialog;
    private LoginPrefManager loginPrefManager;

    private List<NotificationListResponseDatum> notificationListResponseData;


    public DriverNotifiAdapter(Context context, List<NotificationListResponseDatum> notificationListResponseData) {

        this.context = context;
        this.notificationListResponseData = notificationListResponseData;

        this.ddProgressBarDialog = new DDProgressBarDialog(context);
        this.loginPrefManager = new LoginPrefManager(context);


    }

    @Override
    public DriverNotifiAdaViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new DriverNotifiAdaViewHolder(LayoutInflater.from(context).inflate(R.layout.notification_list_row_item_lay, parent, false));
    }

    @Override
    public void onBindViewHolder(final DriverNotifiAdaViewHolder holder, final int position) {

        try {

            Glide.with(context).load("" + notificationListResponseData.get(position).getUser_image()).centerCrop().error(R.color.app_background_color).into(holder.notif_user_image);

            holder.notification_title_tv.setText("" + notificationListResponseData.get(position).getOrder_id_formated());
            holder.notf_amt_txt_view.setText("" + loginPrefManager.getFormatCurrencyValue("" + notificationListResponseData.get(position).getTotal_amount()));

            holder.notification_time.setText("" + notificationListResponseData.get(position).getUser_name());



            if (notificationListResponseData.get(position).getDriverResponse() == 0 && notificationListResponseData.get(position).getOrderDeliveryStatus() == 0
                    && notificationListResponseData.get(position).getAutoOrderRejected() == 0) {

                holder.notif_slider_layout.setVisibility(View.VISIBLE);
                holder.notifi_states_btn_layout.setVisibility(View.GONE);

            }else{

                holder.notif_slider_layout.setVisibility(View.GONE);
                holder.notifi_states_btn_layout.setVisibility(View.VISIBLE);

            }


            if (notificationListResponseData.get(position).getAutoOrderRejected() == 1) {
                holder.rejected_text_btn.setVisibility(View.VISIBLE);
            }

            if (notificationListResponseData.get(position).getDriverResponse() == 1) {
                holder.accepted_text_btn.setVisibility(View.VISIBLE);
            }

            if (notificationListResponseData.get(position).getDriverResponse() == 2) {
                holder.declined_text_btn.setVisibility(View.VISIBLE);
            }



            holder.accept_slide_btn.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideView slideView) {

                    AcceptedApiCall("" + notificationListResponseData.get(position).getOrderId(),
                            "" + notificationListResponseData.get(position).getDriverId()
                            , "1", "" + notificationListResponseData.get(position).getId(), position);

                }
            });

            holder.decline_slide_btn.setOnSlideCompleteListener(new SlideView.OnSlideCompleteListener() {
                @Override
                public void onSlideComplete(SlideView slideView) {

                    AcceptedApiCall("" + notificationListResponseData.get(position).getOrderId(),
                            "" + notificationListResponseData.get(position).getDriverId()
                            , "2", "" + notificationListResponseData.get(position).getId(), position);

                }
            });


        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

    }


    private void AcceptedApiCall(String order_id, String driver_id, final String driver_response, String login_id, final int positon) {

        try {

            if (ddProgressBarDialog != null) {
                ddProgressBarDialog.show();
            }

            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.driver_status_call(order_id, driver_id, driver_response, login_id).enqueue(new Callback<DriverStatus>() {
                @Override
                public void onResponse(Call<DriverStatus> call, Response<DriverStatus> response) {

                    try {

                        ddProgressBarDialog.dismiss();
                        Log.e("onResponse", "" + response.raw().toString());

                        if (response.body().getResponse().getHttpCode() == 200) {

                            NotificationListResponseDatum notificationListResponseDatum = notificationListResponseData.get(positon);

                            if (driver_response.equals("1")) {

                                Intent welcom_intent = new Intent("Loc_update_Rec");
                                LocalBroadcastManager.getInstance(context).sendBroadcast(welcom_intent);

                                notificationListResponseDatum.setDriverResponse(1);
                                notificationListResponseData.set(positon, notificationListResponseDatum);
                                notifyDataSetChanged();

                            } else {

                                notificationListResponseDatum.setDriverResponse(2);
                                notificationListResponseData.set(positon, notificationListResponseDatum);
                                notifyDataSetChanged();

                            }

                        } else if (response.body().getResponse().getHttpCode() == 400) {
                            Toast.makeText(context, response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        ddProgressBarDialog.dismiss();
                        Log.e("Exception", "" + e.getMessage());
                    }

                }

                @Override
                public void onFailure(Call<DriverStatus> call, Throwable t) {
                    ddProgressBarDialog.dismiss();
                }
            });


        } catch (Exception e) {
            ddProgressBarDialog.dismiss();
            Log.e("Exception", "" + e.getMessage());
        }

    }


    @Override
    public int getItemCount() {
        return notificationListResponseData.size();
    }


    public class DriverNotifiAdaViewHolder extends RecyclerView.ViewHolder {

        public TextView notification_title_tv;


        public TextView rejected_text_btn;
        public TextView accepted_text_btn;
        public TextView declined_text_btn;

        public CircleImageView notif_user_image;


        public TextView notf_amt_txt_view;
        public TextView notification_time;

        public LinearLayout notifi_states_btn_layout;

        public LinearLayout notif_slider_layout;
        public SlideView accept_slide_btn;
        public SlideView decline_slide_btn;


        public DriverNotifiAdaViewHolder(View itemView) {
            super(itemView);

            notif_user_image = (CircleImageView) itemView.findViewById(R.id.notif_user_image);
            notification_title_tv = (TextView) itemView.findViewById(R.id.notf_title);
            notf_amt_txt_view = (TextView) itemView.findViewById(R.id.notf_amt_txt_view);
            notification_time = (TextView) itemView.findViewById(R.id.notf_text);

            notifi_states_btn_layout = (LinearLayout) itemView.findViewById(R.id.notifi_states_btn_layout);

            rejected_text_btn = (TextView) itemView.findViewById(R.id.rejected_text_btn);
            accepted_text_btn = (TextView) itemView.findViewById(R.id.accepted_text_btn);
            declined_text_btn = (TextView) itemView.findViewById(R.id.declined_text_btn);

            notif_slider_layout = (LinearLayout) itemView.findViewById(R.id.notif_slider_layout);
            accept_slide_btn = (SlideView) itemView.findViewById(R.id.accept_slide_btn);
            decline_slide_btn = (SlideView) itemView.findViewById(R.id.decline_slide_btn);
        }

    }

}
