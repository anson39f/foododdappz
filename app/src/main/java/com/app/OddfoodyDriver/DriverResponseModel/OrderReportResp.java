
package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class OrderReportResp {

    @SerializedName("response")
    @Expose
    private OrderReportRespData response;

    public OrderReportRespData getResponse() {
        return response;
    }

    public void setResponse(OrderReportRespData response) {
        this.response = response;
    }

}
