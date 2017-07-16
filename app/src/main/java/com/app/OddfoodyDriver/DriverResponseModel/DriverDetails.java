package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverDetails {

    @SerializedName("response")
    @Expose
    private DriverDetailsResponse response;

    public DriverDetailsResponse getResponse() {
        return response;
    }

    public void setResponse(DriverDetailsResponse response) {
        this.response = response;
    }

}