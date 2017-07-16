package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GeneralSettings {

    @SerializedName("site_name")
    @Expose
    private String siteName;
    @SerializedName("default_language")
    @Expose
    private String defaultLanguage;
    @SerializedName("default_country")
    @Expose
    private String defaultCountry;
    @SerializedName("contact_address")
    @Expose
    private String contactAddress;
    @SerializedName("copyrights")
    @Expose
    private String copyrights;
    @SerializedName("site_owner")
    @Expose
    private String siteOwner;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("telephone")
    @Expose
    private String telephone;
    @SerializedName("fax")
    @Expose
    private String fax;
    @SerializedName("geocode")
    @Expose
    private String geocode;
    @SerializedName("site_description")
    @Expose
    private String siteDescription;
    @SerializedName("default_currency")
    @Expose
    private Integer defaultCurrency;
    @SerializedName("currency_side")
    @Expose
    private Integer currencySide;

    public String getSiteName() {
        return siteName;
    }

    public void setSiteName(String siteName) {
        this.siteName = siteName;
    }

    public String getDefaultLanguage() {
        return defaultLanguage;
    }

    public void setDefaultLanguage(String defaultLanguage) {
        this.defaultLanguage = defaultLanguage;
    }

    public String getDefaultCountry() {
        return defaultCountry;
    }

    public void setDefaultCountry(String defaultCountry) {
        this.defaultCountry = defaultCountry;
    }

    public String getContactAddress() {
        return contactAddress;
    }

    public void setContactAddress(String contactAddress) {
        this.contactAddress = contactAddress;
    }

    public String getCopyrights() {
        return copyrights;
    }

    public void setCopyrights(String copyrights) {
        this.copyrights = copyrights;
    }

    public String getSiteOwner() {
        return siteOwner;
    }

    public void setSiteOwner(String siteOwner) {
        this.siteOwner = siteOwner;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getFax() {
        return fax;
    }

    public void setFax(String fax) {
        this.fax = fax;
    }

    public String getGeocode() {
        return geocode;
    }

    public void setGeocode(String geocode) {
        this.geocode = geocode;
    }

    public String getSiteDescription() {
        return siteDescription;
    }

    public void setSiteDescription(String siteDescription) {
        this.siteDescription = siteDescription;
    }

    public Integer getDefaultCurrency() {
        return defaultCurrency;
    }

    public void setDefaultCurrency(Integer defaultCurrency) {
        this.defaultCurrency = defaultCurrency;
    }

    public Integer getCurrencySide() {
        return currencySide;
    }

    public void setCurrencySide(Integer currencySide) {
        this.currencySide = currencySide;
    }

}