package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by nextbrain on 6/6/17.
 */

public class NotificationListResponse {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<NotificationListResponseDatum> data = new ArrayList<>();
    @SerializedName("Message")
    @Expose
    private String message;

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<NotificationListResponseDatum> getData() {
        return data;
    }

    public void setData(List<NotificationListResponseDatum> data) {
        this.data = data;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}