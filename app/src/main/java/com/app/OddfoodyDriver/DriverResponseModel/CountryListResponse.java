package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by nextbrain on 5/5/17.
 */

public class CountryListResponse {
    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("data")
    @Expose
    private List<CountryListResponseDatum> data = null;

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public List<CountryListResponseDatum> getData() {
        return data;
    }

    public void setData(List<CountryListResponseDatum> data) {
        this.data = data;
    }

}
