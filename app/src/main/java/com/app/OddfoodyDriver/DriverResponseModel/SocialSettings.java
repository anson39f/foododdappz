package com.app.OddfoodyDriver.DriverResponseModel;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SocialSettings {

    @SerializedName("facebook_page")
    @Expose
    private String facebookPage;
    @SerializedName("twitter_page")
    @Expose
    private String twitterPage;
    @SerializedName("linkedin_page")
    @Expose
    private String linkedinPage;
    @SerializedName("instagram_page")
    @Expose
    private String instagramPage;

    public String getFacebookPage() {
        return facebookPage;
    }

    public void setFacebookPage(String facebookPage) {
        this.facebookPage = facebookPage;
    }

    public String getTwitterPage() {
        return twitterPage;
    }

    public void setTwitterPage(String twitterPage) {
        this.twitterPage = twitterPage;
    }

    public String getLinkedinPage() {
        return linkedinPage;
    }

    public void setLinkedinPage(String linkedinPage) {
        this.linkedinPage = linkedinPage;
    }

    public String getInstagramPage() {
        return instagramPage;
    }

    public void setInstagramPage(String instagramPage) {
        this.instagramPage = instagramPage;
    }

}
