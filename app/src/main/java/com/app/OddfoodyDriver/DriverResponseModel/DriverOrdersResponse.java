package com.app.OddfoodyDriver.DriverResponseModel;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverOrdersResponse {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
//    @SerializedName("status")
//    @Expose
//    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String Message;
    @SerializedName("order_list")
    @Expose
    private List<OrderList> orderList = null;

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

//    public Boolean getStatus() {
//        return status;
//    }
//
//    public void setStatus(Boolean status) {
//        this.status = status;
//    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String status) {
        this.Message = status;
    }

    public List<OrderList> getOrderList() {
        return orderList;
    }

    public void setOrderList(List<OrderList> orderList) {
        this.orderList = orderList;
    }



    @SerializedName("order_status")
    @Expose
    private List<OrderStatus> orderStatus = null;

    public List<OrderStatus> getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(List<OrderStatus> orderStatus) {
        this.orderStatus = orderStatus;
    }



}