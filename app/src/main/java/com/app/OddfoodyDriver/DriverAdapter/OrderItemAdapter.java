package com.app.OddfoodyDriver.DriverAdapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.app.OddfoodyDriver.DriverResponseModel.Order;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.R;

import java.util.ArrayList;

/**
 * Created by Next Brain on 27-01-2017.
 */

public class OrderItemAdapter extends BaseAdapter {

    private LoginPrefManager loginPrefManager;

    private final LayoutInflater inflater;
    private final Context context;

    private ArrayList<Order> orderItems;

    public OrderItemAdapter(Context context, ArrayList<Order> orderItems) {

        this.context = context;
        inflater = LayoutInflater.from(this.context);
        this.orderItems = orderItems;

        loginPrefManager = new LoginPrefManager(context);

    }

    @Override
    public int getCount() {
        return orderItems.size();
    }

    public void UpdateArrayList(ArrayList<Order> orderItems) {
        this.orderItems = orderItems;
        notifyDataSetChanged();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public static class ViewHolder {
        private TextView prod_name, prod_qty, prod_price;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        final ViewHolder holder;

        if (convertView == null) {


            vi = inflater.inflate(R.layout.activity_bill_details, null);

            holder = new ViewHolder();
            holder.prod_name = (TextView) vi.findViewById(R.id.order_prod);
            holder.prod_qty = (TextView) vi.findViewById(R.id.order_prod_quan);
            holder.prod_price = (TextView) vi.findViewById(R.id.order_prod_amt);

            vi.setTag(holder);

        } else {
            holder = (ViewHolder) vi.getTag();
        }

        holder.prod_name.setText(orderItems.get(position).getProductName());
        holder.prod_qty.setText("" + orderItems.get(position).getItemUnit());

        holder.prod_price.setText("" + loginPrefManager.getFormatCurrencyValue("" + orderItems.get(position).getTotalAmount()));

        return vi;
    }
}
