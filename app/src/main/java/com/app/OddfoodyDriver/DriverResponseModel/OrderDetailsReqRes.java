
package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderDetailsReqRes {

    @SerializedName("response")
    @Expose
    private DrivDetResp response;

    public DrivDetResp getResponse() {
        return response;
    }

    public void setResponse(DrivDetResp response) {
        this.response = response;
    }

}
