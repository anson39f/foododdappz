package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nextbrain on 5/5/17.
 */

public class CountryList {

    @SerializedName("response")
    @Expose
    private CountryListResponse response;

    public CountryListResponse getResponse() {
        return response;
    }

    public void setResponse(CountryListResponse response) {
        this.response = response;
    }

}
