
package com.app.OddfoodyDriver.DriverResponseModel;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class cityDatum {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("zone_code")
    @Expose
    private String zoneCode;
    @SerializedName("country_id")
    @Expose
    private Integer countryId;
    @SerializedName("active_status")
    @Expose
    private String activeStatus;
    @SerializedName("default_status")
    @Expose
    private Integer defaultStatus;
    @SerializedName("created_date")
    @Expose
    private String createdDate;
    @SerializedName("url_index")
    @Expose
    private String urlIndex;
    @SerializedName("modified_date")
    @Expose
    private String modifiedDate;
    @SerializedName("latitude")
    @Expose
    private Object latitude;
    @SerializedName("longtitude")
    @Expose
    private Object longtitude;
    @SerializedName("cid")
    @Expose
    private Integer cid;
    @SerializedName("city_name")
    @Expose
    private String cityName;
    @SerializedName("language_id")
    @Expose
    private Integer languageId;
    @SerializedName("info_id")
    @Expose
    private Integer infoId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getZoneCode() {
        return zoneCode;
    }

    public void setZoneCode(String zoneCode) {
        this.zoneCode = zoneCode;
    }

    public Integer getCountryId() {
        return countryId;
    }

    public void setCountryId(Integer countryId) {
        this.countryId = countryId;
    }

    public String getActiveStatus() {
        return activeStatus;
    }

    public void setActiveStatus(String activeStatus) {
        this.activeStatus = activeStatus;
    }

    public Integer getDefaultStatus() {
        return defaultStatus;
    }

    public void setDefaultStatus(Integer defaultStatus) {
        this.defaultStatus = defaultStatus;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getUrlIndex() {
        return urlIndex;
    }

    public void setUrlIndex(String urlIndex) {
        this.urlIndex = urlIndex;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public Object getLatitude() {
        return latitude;
    }

    public void setLatitude(Object latitude) {
        this.latitude = latitude;
    }

    public Object getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(Object longtitude) {
        this.longtitude = longtitude;
    }

    public Integer getCid() {
        return cid;
    }

    public void setCid(Integer cid) {
        this.cid = cid;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public Integer getLanguageId() {
        return languageId;
    }

    public void setLanguageId(Integer languageId) {
        this.languageId = languageId;
    }

    public Integer getInfoId() {
        return infoId;
    }

    public void setInfoId(Integer infoId) {
        this.infoId = infoId;
    }

}
