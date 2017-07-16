
package com.app.OddfoodyDriver.DriverResponseModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class cityList {


    @SerializedName("response")
    @Expose
    private cityResponse response;

    public cityResponse getResponse() {
        return response;
    }

    public void setResponse(cityResponse response) {
        this.response = response;
    }

}

