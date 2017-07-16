package com.app.OddfoodyDriver.DriverAdapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.app.OddfoodyDriver.DriverResponseModel.Order;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;

/**
 * Created by nextbrain on 8/6/17.
 */

public class OrdDetProdItemAdapter extends RecyclerView.Adapter<OrdDetProdItemAdapter.OrdDetProdItemViewHolder> {

    private Context context;

    private ArrayList<Order> orderItems;

    private LoginPrefManager loginPrefManager;

    private int sub_total = 0;

    public OrdDetProdItemAdapter(Context context, ArrayList<Order> orderItems) {
        this.context = context;
        this.orderItems = orderItems;

        loginPrefManager = new LoginPrefManager(context);
    }

    @Override
    public OrdDetProdItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new OrdDetProdItemViewHolder(LayoutInflater.from(context).inflate(R.layout.activity_bill_details, parent, false));
    }

    @Override
    public void onBindViewHolder(OrdDetProdItemViewHolder holder, int position) {

        try {

            holder.order_det_prod_image.setVisibility(View.VISIBLE);

            Glide.with(context).load("" + orderItems.get(position).getProductImage()).bitmapTransform(new CenterCrop(context), new RoundedCornersTransformation(context, 5, 0)).error(R.color.app_background_color).into(holder.order_det_prod_image);

            holder.prod_name.setText(orderItems.get(position).getProductName());
            holder.prod_qty.setText("" + orderItems.get(position).getItemUnit());

            if("" + orderItems.get(position).getItemUnit() != null && !orderItems.get(position).getItemCost().isEmpty()){
                int price = ( orderItems.get(position).getItemUnit() * (Integer.parseInt(orderItems.get(position).getItemCost())));
                holder.prod_price.setText(""  + loginPrefManager.getFormatCurrencyValue("" + price));

                sub_total = (sub_total + price);

                Log.e("sub_total", "-" + sub_total);
            }

//            holder.prod_price.setText("" + loginPrefManager.getFormatCurrencyValue("" + orderItems.get(position).getTotalAmount()));


        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

    }

    public int getSubTotalAmount(){
        return sub_total;
    }

    @Override
    public int getItemCount() {
        return orderItems.size();
    }


    public void UpdateArrayList(ArrayList<Order> orderItems) {
        this.sub_total = 0;
        this.orderItems = orderItems;
        notifyDataSetChanged();
    }


    public class OrdDetProdItemViewHolder extends RecyclerView.ViewHolder {

        private ImageView order_det_prod_image;

        private TextView prod_name;
        private TextView prod_qty;
        private TextView prod_price;

        public OrdDetProdItemViewHolder(View itemView) {
            super(itemView);

            order_det_prod_image = (ImageView) itemView.findViewById(R.id.order_det_prod_image);

            prod_name = (TextView) itemView.findViewById(R.id.order_prod);
            prod_qty = (TextView) itemView.findViewById(R.id.order_prod_quan);
            prod_price = (TextView) itemView.findViewById(R.id.order_prod_amt);

        }
    }

}
