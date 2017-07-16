
package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderReportRespData {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("delivered_status_count")
    @Expose
    private int deliveredStatusCount;
    @SerializedName("dispatched_status_count")
    @Expose
    private int dispatchedStatusCount;

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

    public int getDeliveredStatusCount() {
        return deliveredStatusCount;
    }

    public void setDeliveredStatusCount(int deliveredStatusCount) {
        this.deliveredStatusCount = deliveredStatusCount;
    }

    public int getDispatchedStatusCount() {
        return dispatchedStatusCount;
    }

    public void setDispatchedStatusCount(int dispatchedStatusCount) {
        this.dispatchedStatusCount = dispatchedStatusCount;
    }

}
