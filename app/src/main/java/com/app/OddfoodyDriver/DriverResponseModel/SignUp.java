package com.app.OddfoodyDriver.DriverResponseModel;

import com.app.OddfoodyDriver.DriverModel.SignUpResponse;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nextbrain on 8/5/17.
 */

public class SignUp {

    @SerializedName("response")
    @Expose
    private SignUpResponse response;

    public SignUpResponse getResponse() {
        return response;
    }

    public void setResponse(SignUpResponse response) {
        this.response = response;
    }

}