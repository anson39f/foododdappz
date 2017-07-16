
package com.app.OddfoodyDriver.DriverResponseModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetaList {

    @SerializedName("order_id")
    @Expose
    private Integer orderId;
    @SerializedName("total_amount")
    @Expose
    private String totalAmount;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("outlet_address")
    @Expose
    private String outletAddress;
    @SerializedName("user_address")
    @Expose
    private String userAddress;
    @SerializedName("driver_first_name")
    @Expose
    private String driverFirstName;
    @SerializedName("driver_last_name")
    @Expose
    private String driverLastName;
    @SerializedName("user_first_name")
    @Expose
    private String userFirstName;
    @SerializedName("user_last_name")
    @Expose
    private String userLastName;
    @SerializedName("user_mobile")
    @Expose
    private String userMobile;
    @SerializedName("user_latitude")
    @Expose
    private String userLatitude;
    @SerializedName("user_longitude")
    @Expose
    private String userLongitude;
    @SerializedName("outlet_latitude")
    @Expose
    private String outlet_latitude;
    @SerializedName("outlet_longtitude")
    @Expose
    private String outlet_longtitude;
    @SerializedName("user_image")
    @Expose
    private String userImage;
    @SerializedName("featured_image")
    @Expose
    private String featuredImage;
    @SerializedName("orders")
    @Expose
    private List<Order> orders = null;
    @SerializedName("order_status")
    @Expose
    private Integer orderStatus;
    @SerializedName("modified_date")
    @Expose
    private Object modifiedDate;
    @SerializedName("tracking_result")
    @Expose
    private List<TrackingResult> trackingResult = null;

    public Integer getOrderId() {
        return orderId;
    }

    public void setOrderId(Integer orderId) {
        this.orderId = orderId;
    }

    public String getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getOutletAddress() {
        return outletAddress;
    }

    public void setOutletAddress(String outletAddress) {
        this.outletAddress = outletAddress;
    }

    public String getUserAddress() {
        return userAddress;
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
    }

    public String getDriverFirstName() {
        return driverFirstName;
    }

    public void setDriverFirstName(String driverFirstName) {
        this.driverFirstName = driverFirstName;
    }

    public String getDriverLastName() {
        return driverLastName;
    }

    public void setDriverLastName(String driverLastName) {
        this.driverLastName = driverLastName;
    }

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public String getUserMobile() {
        return userMobile;
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
    }

    public String getUserLatitude() {
        return userLatitude;
    }

    public void setUserLatitude(String userLatitude) {
        this.userLatitude = userLatitude;
    }

    public String getUserLongitude() {
        return userLongitude;
    }

    public void setUserLongitude(String userLongitude) {
        this.userLongitude = userLongitude;
    }


    public String getOutletLatitude() {
        return outlet_latitude;
    }

    public void setOutletLatitude(String outlet_latitude) {
        this.outlet_latitude = outlet_latitude;
    }

    public String getOutletLongtitude() {
        return outlet_longtitude;
    }

    public void setOutletLongtitude(String outlet_longtitude) {
        this.outlet_longtitude = outlet_longtitude;
    }



    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getFeaturedImage() {
        return featuredImage;
    }

    public void setFeaturedImage(String featuredImage) {
        this.featuredImage = featuredImage;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Object getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(Object modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public List<TrackingResult> getTrackingResult() {
        return trackingResult;
    }

    public void setTrackingResult(List<TrackingResult> trackingResult) {
        this.trackingResult = trackingResult;
    }



    @SerializedName("vendor_name")
    @Expose
    private String vendor_name;

    @SerializedName("outlet_name")
    @Expose
    private String outlet_name;


    public String getVendor_name() {
        return vendor_name;
    }

    public void setVendor_name(String vendor_name) {
        this.vendor_name = vendor_name;
    }


    public String getOutlet_name() {
        return outlet_name;
    }

    public void setOutlet_name(String outlet_name) {
        this.outlet_name = outlet_name;
    }


    @SerializedName("digital_signature")
    @Expose
    private String digital_signature;

    @SerializedName("order_attachment")
    @Expose
    private String order_attachment;


    public String getDigital_signature() {
        return digital_signature;
    }

    public void setDigital_signature(String digital_signature) {
        this.digital_signature = digital_signature;
    }

    public String getOrder_attachment() {
        return order_attachment;
    }

    public void setOrder_attachment(String order_attachment) {
        this.order_attachment = order_attachment;
    }

}