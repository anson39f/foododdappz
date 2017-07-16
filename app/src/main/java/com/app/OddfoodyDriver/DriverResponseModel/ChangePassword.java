package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ChangePassword {

    @SerializedName("response")
    @Expose
    private ChangePasswordResponse response;

    public ChangePasswordResponse getResponse() {
        return response;
    }

    public void setResponse(ChangePasswordResponse response) {
        this.response = response;
    }

}
