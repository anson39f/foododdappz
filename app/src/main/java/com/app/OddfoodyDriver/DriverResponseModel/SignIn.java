package com.app.OddfoodyDriver.DriverResponseModel;

/**
 * Created by test on 2/24/2017.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SignIn {

    @SerializedName("response")
    @Expose
    private SignInResponse response;

    public SignInResponse getResponse() {
        return response;
    }

    public void setResponse(SignInResponse response) {
        this.response = response;
    }

}