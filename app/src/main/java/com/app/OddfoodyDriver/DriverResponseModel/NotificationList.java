package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nextbrain on 6/6/17.
 */

public class NotificationList {

    @SerializedName("response")
    @Expose
    private NotificationListResponse response;


    public NotificationListResponse getResponse() {
        return response;
    }

    public void setResponse(NotificationListResponse response) {
        this.response = response;
    }


}
