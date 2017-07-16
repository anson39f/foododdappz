package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageList {

    @SerializedName("response")
    @Expose
    private LanguageListResponse response;

    public LanguageListResponse getResponse() {
        return response;
    }

    public void setResponse(LanguageListResponse response) {
        this.response = response;
    }

}