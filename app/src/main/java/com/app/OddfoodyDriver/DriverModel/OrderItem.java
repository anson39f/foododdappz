package com.app.OddfoodyDriver.DriverModel;

/**
 * Created by Next Brain on 23-01-2017.
 */

public class OrderItem {

    private String Order_date;
    private String Order_id;
    private int Order_amt;
    private String pick_up_point;
    private String destination_point;
    private int order_price;
    private int order_quantity;
    private String order_name;

    public int getOrder_price() {
        return order_price;
    }

    public void setOrder_price(int order_price) {
        this.order_price = order_price;
    }

    public int getOrder_quantity() {
        return order_quantity;
    }

    public void setOrder_quantity(int order_quantity) {
        this.order_quantity = order_quantity;
    }

    public String getOrder_name() {
        return order_name;
    }

    public void setOrder_name(String order_name) {
        this.order_name = order_name;
    }

    public String getOrder_date() {
        return Order_date;
    }

    public void setOrder_date(String order_date) {
        Order_date = order_date;
    }

    public String getOrder_id() {
        return Order_id;
    }

    public void setOrder_id(String order_id) {
        Order_id = order_id;
    }

    public int getOrder_amt() {
        return Order_amt;
    }

    public void setOrder_amt(int order_amt) {
        Order_amt = order_amt;
    }

    public String getPick_up_point() {
        return pick_up_point;
    }

    public void setPick_up_point(String pick_up_point) {
        this.pick_up_point = pick_up_point;
    }

    public String getDestination_point() {
        return destination_point;
    }

    public void setDestination_point(String destination_point) {
        this.destination_point = destination_point;
    }

}
