package com.app.OddfoodyDriver.DriverResponseModel;

import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LanguageListResponse {

    @SerializedName("httpCode")
    @Expose
    private Integer httpCode;
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("languages")
    @Expose
    private List<Language> languages = null;
    @SerializedName("modules_list")
    @Expose
    private ModulesList modulesList;
    @SerializedName("general_settings")
    @Expose
    private GeneralSettings generalSettings;
    @SerializedName("social_settings")
    @Expose
    private SocialSettings socialSettings;
    @SerializedName("currency_list")
    @Expose
    private List<CurrencyList> currencyList = null;

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

    public List<Language> getLanguages() {
        return languages;
    }

    public void setLanguages(List<Language> languages) {
        this.languages = languages;
    }

    public ModulesList getModulesList() {
        return modulesList;
    }

    public void setModulesList(ModulesList modulesList) {
        this.modulesList = modulesList;
    }

    public GeneralSettings getGeneralSettings() {
        return generalSettings;
    }

    public void setGeneralSettings(GeneralSettings generalSettings) {
        this.generalSettings = generalSettings;
    }

    public SocialSettings getSocialSettings() {
        return socialSettings;
    }

    public void setSocialSettings(SocialSettings socialSettings) {
        this.socialSettings = socialSettings;
    }

    public List<CurrencyList> getCurrencyList() {
        return currencyList;
    }

    public void setCurrencyList(List<CurrencyList> currencyList) {
        this.currencyList = currencyList;
    }

}