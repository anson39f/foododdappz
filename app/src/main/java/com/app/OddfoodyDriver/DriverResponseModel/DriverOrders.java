package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverOrders {

    @SerializedName("response")
    @Expose
    private DriverOrdersResponse response;

    public DriverOrdersResponse getResponse() {
        return response;
    }

    public void setResponse(DriverOrdersResponse response) {
        this.response = response;
    }

}
