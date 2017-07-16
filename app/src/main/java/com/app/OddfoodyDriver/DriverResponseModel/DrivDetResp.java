
package com.app.OddfoodyDriver.DriverResponseModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrivDetResp {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("order_list")
    @Expose
    private List<OrderDetaList> orderdetaList = null;

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<OrderDetaList> getOrderDetaList() {
        return orderdetaList;
    }

    public void setOrderDetaList(List<OrderDetaList> orderList) {
        this.orderdetaList = orderList;
    }

}
