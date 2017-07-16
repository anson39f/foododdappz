
package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UpdateStatusRes {

    @SerializedName("response")
    @Expose
    private UpdateStatusRespdata response;

    public UpdateStatusRespdata getResponse() {
        return response;
    }

    public void setResponse(UpdateStatusRespdata response) {
        this.response = response;
    }

}
