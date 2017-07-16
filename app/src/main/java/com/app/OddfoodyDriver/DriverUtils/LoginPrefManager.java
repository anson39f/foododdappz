package com.app.OddfoodyDriver.DriverUtils;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.app.OddfoodyDriver.DriverActivities.SplashScreenActivity;
import com.app.OddfoodyDriver.androidcharts.entity.TitleValueColorEntity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class LoginPrefManager {

    private final Context _context;

    private SharedPreferences pref;
    private Editor editor;

    private static final String PREF_NAME = "AndroidTijikExpressDriverPref";
    private static final String IS_LOGIN = "IsLoggedIn";
    private static final String Check_login = "0";
    private static JSONObject jsonObject;


    private static List<TitleValueColorEntity> titleValueColorEntities = new ArrayList<>();
    private static String chartDate = "";

    public LoginPrefManager(Context context) {
        this._context = context;
        int PRIVATE_MODE = 0;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();

        editor.apply();
    }

    public void setLoginPrefData(String text, String data) {
        editor.putString(text, data).commit();
        editor.putBoolean(IS_LOGIN, true);
        editor.putString(Check_login, "1").commit();
        editor.commit();
    }

    public void setPrefData(String text, String data) {
        editor.putString(text, data).commit();
        editor.commit();
    }

    public String getPrefData(String text) {
        return pref.getString(text, "");
    }

    public Editor getEditor() {
        return editor;
    }

    public SharedPreferences getShaPref() {
        return pref;
    }

    public void setEditor(Editor editor) {
        this.editor = editor;
    }

    public void setMyPref(SharedPreferences pref) {
        this.pref = pref;
    }

    public void setIntValue(String keyName, int value) {
        pref.edit().putInt(keyName, value).apply();
    }

    public int getIntValue(String keyName) {
        return pref.getInt(keyName, 0);
    }

    public void setStringValue(String keyName, String value) {
        pref.edit().putString(keyName, value).apply();
    }

    public String getStringValue(String keyName) {
        return pref.getString(keyName, "");
    }

    public void setBooleanValue(String keyName, boolean value) {
        pref.edit().putBoolean(keyName, value).apply();
    }

    public Boolean getBooleanValue(String keyName) {
        return pref.getBoolean(keyName, false);
    }

    public void remove(String key) {
        pref.edit().remove(key).apply();
    }

    public boolean clear() {
        return pref.edit().clear().commit();
    }


    /**
     * Check login method wil check user login status
     * If false it will redirect user to login page
     * Else won't do anything
     */
    public void checkPrefLogin() {
        if (!this.isLoggedIn()) {
            Intent i = new Intent(_context, SplashScreenActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            _context.startActivity(i);
        }
    }

    /**
     * Clear session details
     */
    public void logoutUser() {
        editor.clear();
        editor.putBoolean(IS_LOGIN, false);
        editor.commit();
        Intent logi = new Intent(_context, SplashScreenActivity.class);
        logi.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        logi.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        _context.startActivity(logi);
    }

    /**
     * Quick check for login
     **/
    // Get Login State
    private boolean isLoggedIn() {
        return pref.getBoolean(IS_LOGIN, false);
    }

    public static void setJsonObject(JSONObject jsonObject) {
        LoginPrefManager.jsonObject = jsonObject;
    }

    public static JSONObject getJsonObject() {
        return jsonObject;
    }


    //Pic a City details
    public void setCityIDandName(String City_Id, String City_Name) {
        pref.edit().putString("Pic_City_Id", City_Id).apply();
        pref.edit().putString("Pic_City_Name", City_Name).apply();
    }

    public String getCityID() {
        return pref.getString("Pic_City_Id", "");
    }

    public String getCityName() {
        return pref.getString("Pic_City_Name", "");
    }

    // Pic a Location Details
    public void setLocIDandName(String Loc_Id, String Loc_Name) {
        pref.edit().putString("Pic_loc_id", Loc_Id).apply();
        pref.edit().putString("Pic_loc_name", Loc_Name).apply();
    }

    public String getLocID() {
        return pref.getString("Pic_loc_id", "");
    }

    public String getLocName() {
        return pref.getString("Pic_loc_name", "");
    }

    public String getCurrencySide() {
        return pref.getString("currency_side", "");
    }

    public String getCurrencySymbole() {
        return pref.getString("currency_symbol", "");
    }

    public String getCurrencyName() {
        return pref.getString("currency_name", "");
    }


    public void LoginUserDetails(String user_id, String email_id, String password, String first_name, String last_name, String user_token, String image) {

        pref.edit().putString("driver_id", user_id).apply();
        pref.edit().putString("email", email_id).apply();
        pref.edit().putString("password", password).apply();
        pref.edit().putString("first_name", first_name).apply();
        pref.edit().putString("last_name", last_name).apply();
        pref.edit().putString("token", user_token).apply();
        pref.edit().putString("image", image).apply();

    }

    public String getUserLoginDriverId() {
        return pref.getString("driver_id", "");
    }

    public String getUserLoginEmail() {
        return pref.getString("email", "");
    }

    public String getUserLoginPassword() {
        return pref.getString("password", "");
    }

    public String getUserLoginFirstName() {
        return pref.getString("first_name", "");
    }

    public String getUserLoginLastName() {
        return pref.getString("last_name", "");
    }

    public String getUserLoginToken() {
        return pref.getString("token", "");
    }

    public String getUserLoginfImg() {
        return pref.getString("image", "");
    }


    public void LogOutClearDataMethod() {

        pref.edit().putString("login_email", "").apply();
        pref.edit().putString("login_password", "").apply();
        pref.edit().putString("driver_id", "").apply();
        pref.edit().putString("user_token", "").apply();
        pref.edit().putString("user_email", "").apply();
        pref.edit().putString("user_name", "").apply();
        pref.edit().putString("user_social_title", "").apply();
        pref.edit().putString("user_first_name", "").apply();
        pref.edit().putString("user_last_name", "").apply();
        pref.edit().putString("user_image", "").apply();
        pref.edit().putString("Pic_City_Id", "").apply();
        pref.edit().putString("Pic_City_Name", "").apply();
        pref.edit().putString("Pic_loc_id", "").apply();
        pref.edit().putString("Pic_loc_name", "").apply();

//        pref.edit().putString("default_lang", "").apply();


    }


    public String getFormatCurrencyValue(String Currency) {
        if (getCurrencySide().equals("1")) {
            return String.format("%s %s", "" + getCurrencySymbole(), "" + Currency);
        } else {
            return String.format("%s %s", "" + Currency, "" + getCurrencySymbole());
        }
    }

    // Init Default Language
    public void setDefaultLang(String defa_lang) {
        pref.edit().putString("default_lang", defa_lang).apply();
    }

    public String getDefaultLang() {
        return pref.getString("default_lang", "");
    }


    public List<TitleValueColorEntity> getChartDatas() {
        return titleValueColorEntities;
    }

    public void setChartDatas(List<TitleValueColorEntity> titleValueColorEntities) {
        this.titleValueColorEntities = titleValueColorEntities;
    }

    public String getChartDate() {
        return chartDate;
    }

    public void setChartDate(String chartDate){
        this.chartDate = chartDate;
    }


}
