package com.app.OddfoodyDriver.DriverResponseModel;


import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DriverDetailsResponse {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("Message")
    @Expose
    private String message;
    @SerializedName("driver_details")
    @Expose
    private List<DriverDetail> driverDetails = null;

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

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<DriverDetail> getDriverDetails() {
        return driverDetails;
    }

    public void setDriverDetails(List<DriverDetail> driverDetails) {
        this.driverDetails = driverDetails;
    }

}