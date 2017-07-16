package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by NEXTBRAIN on 2/28/2017.
 */

public class TrackingResult {

    @SerializedName("text")
    @Expose
    private String text;
    @SerializedName("process")
    @Expose
    private String process;
    @SerializedName("date")
    @Expose
    private String date;
    @SerializedName("order_comments")
    @Expose
    private String orderComments;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getProcess() {
        return process;
    }

    public void setProcess(String process) {
        this.process = process;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getOrderComments() {
        return orderComments;
    }

    public void setOrderComments(String orderComments) {
        this.orderComments = orderComments;
    }

}
