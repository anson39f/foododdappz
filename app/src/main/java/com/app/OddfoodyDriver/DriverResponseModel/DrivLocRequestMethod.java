
package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DrivLocRequestMethod {

    @SerializedName("response")
    @Expose
    private DriverLocUpdateResp response;

    public DriverLocUpdateResp getResponse() {
        return response;
    }

    public void setResponse(DriverLocUpdateResp response) {
        this.response = response;
    }

}
