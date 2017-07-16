package com.app.OddfoodyDriver.DriverResponseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class CurrencyList {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("currency_name")
    @Expose
    private String currencyName;
    @SerializedName("currency_symbol")
    @Expose
    private String currencySymbol;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCurrencyName() {
        return currencyName;
    }

    public void setCurrencyName(String currencyName) {
        this.currencyName = currencyName;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

}