package com.app.OddfoodyDriver.DriverAdapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverActivities.OrderdetailActivity;
import com.app.OddfoodyDriver.DriverResponseModel.OrderList;
import com.app.OddfoodyDriver.DriverResponseModel.OrderStatus;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Next Brain on 23-01-2017.
 */

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.MyViewHolder> {

    private Context context;
    private LoginPrefManager loginPrefManager;

    private ArrayList<OrderList> order_items;
    private List<OrderStatus> orderStatus;

    public OrdersAdapter(Context context, ArrayList<OrderList> orderItems, List<OrderStatus> orderStatus) {
        this.context = context;
        this.order_items = orderItems;
        this.orderStatus = orderStatus;
        this.loginPrefManager = new LoginPrefManager(context);
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.activity_order_item, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide.with(context).load("" + order_items.get(position).getFeaturedImage()).centerCrop().error(R.color.app_background_color).into(holder.sto_info_activity_logo_img);

        holder.Order_date.setText(order_items.get(position).getCreatedDate());
        holder.Order_id.setText("" + context.getString(R.string.ordet_item_order_id_txt) + " " + order_items.get(position).getOrdersId());


        if (orderStatus.size() != 0) {
            for (int i = 0; i < orderStatus.size(); i++) {

                if (orderStatus.get(i).getId() == order_items.get(position).getOrder_status()) {
                    holder.Order_status_txt_view.setText("" + context.getString(R.string.order_item_status_txt) + " " + orderStatus.get(i).getName());
                    holder.Order_status_txt_view.setTextColor(Color.parseColor("" + orderStatus.get(i).getColorCode()));
                }
            }
        }


        holder.Order_price_value.setText("" + loginPrefManager.getFormatCurrencyValue("" + order_items.get(position).getTotalAmount()));


        Glide.with(context).load("" + order_items.get(position).getUserImage()).centerCrop().error(R.color.app_background_color).into(holder.order_item_user_img_view);

        holder.Pick_Up_Location.setText("" + order_items.get(position).getOutletAddress());
        holder.Destination_Locaton.setText("" + order_items.get(position).getUserAddress());

        holder.Overallaction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!NetworkStatus.isConnectingToInternet(context)) {
                    Toast.makeText(context, "" + context.getResources().getString(R.string.no_internet_conn_tit_txt), Toast.LENGTH_SHORT).show();
                    return;
                }

                loginPrefManager.setStringValue("order_id", "" + order_items.get(position).getOrdersId());
                Intent Order_Intent = new Intent(context, OrderdetailActivity.class);
                Order_Intent.putExtra("Order_ID", "" + order_items.get(position).getOrdersId());
                Log.e("order_id", "" + order_items.get(position).getOrdersId());
                context.startActivity(Order_Intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return order_items.size();
    }

    public void UpdateOrdersAdapterValues(ArrayList<OrderList> orderItems, List<OrderStatus> orderStatuses) {
        this.order_items = orderItems;
        this.orderStatus = orderStatuses;
        this.notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView sto_info_activity_logo_img;

        private TextView Order_date, Order_id;

        private TextView Order_status_txt_view;

        private ImageView order_item_user_img_view;

        private TextView Order_price_value, Pick_Up_Location, Destination_Locaton;

        private LinearLayout Overallaction;

        public MyViewHolder(View view) {
            super(view);

            sto_info_activity_logo_img = (ImageView) view.findViewById(R.id.sto_info_activity_logo_img);
            Order_date = (TextView) view.findViewById(R.id.Order_Date);
            Order_id = (TextView) view.findViewById(R.id.Order_Id);

            Order_status_txt_view = (TextView) view.findViewById(R.id.Order_status_txt_view);

            order_item_user_img_view = (ImageView) view.findViewById(R.id.order_item_user_img_view);


            Pick_Up_Location = (TextView) view.findViewById(R.id.pick_location);
            Order_price_value = (TextView) view.findViewById(R.id.Order_price);
            Destination_Locaton = (TextView) view.findViewById(R.id.destination_location);
            Overallaction = (LinearLayout) view.findViewById(R.id.overallview);

        }
    }
}
