package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nextbrain on 6/6/17.
 */

public class NotificationListResponseDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("orders_id")
    @Expose
    private Integer orderId;
    @SerializedName("driver_id")
    @Expose
    private Integer driverId;
    @SerializedName("assign_date")
    @Expose
    private String assignDate;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("notification_read")
    @Expose
    private Integer notificationRead;
    @SerializedName("order_delivery_status")
    @Expose
    private Integer orderDeliveryStatus;
    @SerializedName("order_subject")
    @Expose
    private String orderSubject;
    @SerializedName("driver_response")
    @Expose
    private Integer driverResponse;
    @SerializedName("auto_order_rejected")
    @Expose
    private Integer autoOrderRejected;
    @SerializedName("order_message")
    @Expose
    private String orderMessage;
    @SerializedName("created_date_formated")
    @Expose
    private String createdDateFormated;




    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public Integer getDriverId() {
        return driverId;
    }

    public void setDriverId(Integer driverId) {
        this.driverId = driverId;
    }

    public String getAssignDate() {
        return assignDate;
    }

    public void setAssignDate(String assignDate) {
        this.assignDate = assignDate;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public Integer getNotificationRead() {
        return notificationRead;
    }

    public void setNotificationRead(Integer notificationRead) {
        this.notificationRead = notificationRead;
    }

    public Integer getOrderDeliveryStatus() {
        return orderDeliveryStatus;
    }

    public void setOrderDeliveryStatus(Integer orderDeliveryStatus) {
        this.orderDeliveryStatus = orderDeliveryStatus;
    }

    public String getOrderSubject() {
        return orderSubject;
    }

    public void setOrderSubject(String orderSubject) {
        this.orderSubject = orderSubject;
    }

    public Integer getDriverResponse() {
        return driverResponse;
    }

    public void setDriverResponse(Integer driverResponse) {
        this.driverResponse = driverResponse;
    }

    public Integer getAutoOrderRejected() {
        return autoOrderRejected;
    }

    public void setAutoOrderRejected(Integer autoOrderRejected) {
        this.autoOrderRejected = autoOrderRejected;
    }

    public String getOrderMessage() {
        return orderMessage;
    }

    public void setOrderMessage(String orderMessage) {
        this.orderMessage = orderMessage;
    }

    public String getCreatedDateFormated() {
        return createdDateFormated;
    }

    public void setCreatedDateFormated(String createdDateFormated) {
        this.createdDateFormated = createdDateFormated;
    }


    @SerializedName("order_id_formated")
    @Expose
    private String order_id_formated;

    @SerializedName("total_amount")
    @Expose
    private String total_amount;

    @SerializedName("user_name")
    @Expose
    private String user_name;

    @SerializedName("user_email")
    @Expose
    private String user_email;

    @SerializedName("user_address")
    @Expose
    private String user_address;

    @SerializedName("user_image")
    @Expose
    private String user_image;




    public String getOrder_id_formated() {
        return order_id_formated;
    }

    public void setOrder_id_formated(String order_id_formated) {
        this.order_id_formated = order_id_formated;
    }

    public String getTotal_amount() {
        return total_amount;
    }

    public void setTotal_amount(String total_amount) {
        this.total_amount = total_amount;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_email() {
        return user_email;
    }

    public void setUser_email(String user_email) {
        this.user_email = user_email;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_image() {
        return user_image;
    }

    public void setUser_image(String user_image) {
        this.user_image = user_image;
    }

}