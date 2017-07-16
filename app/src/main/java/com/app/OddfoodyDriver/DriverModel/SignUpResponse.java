package com.app.OddfoodyDriver.DriverModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by nextbrain on 8/5/17.
 */

public class SignUpResponse {
    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("Message")
    @Expose
    private String message;

    public Integer getHttpCode() {
        return httpCode;
    }

    public void setHttpCode(Integer httpCode) {
        this.httpCode = httpCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

}