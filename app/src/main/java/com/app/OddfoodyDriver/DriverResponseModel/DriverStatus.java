package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nextbrain on 6/6/17.
 */

public class DriverStatus {
    @SerializedName("response")
    @Expose
    private DriverStatusResponse response;

    public DriverStatusResponse getResponse() {
        return response;
    }

    public void setResponse(DriverStatusResponse response) {
        this.response = response;
    }

}
