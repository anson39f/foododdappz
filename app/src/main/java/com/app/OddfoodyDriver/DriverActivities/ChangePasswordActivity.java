package com.app.OddfoodyDriver.DriverActivities;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverResponseModel.ChangePassword;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChangePasswordActivity extends LocalizationActivity {

    private Toolbar change_password_toolbar;

    private TextInputLayout chang_old_pass_txt_in_lay, chang_new_pass_txt_in_lay, chang_confirm_pass_txt_in_lay;

    private EditText chang_old_pass_edt_txt, chang_new_pass_edt_txt, chang_confirm_pass_edt_txt;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        change_password_toolbar = (Toolbar) findViewById(R.id.change_password_toolbar);
        change_password_toolbar.setTitle("" + getString(R.string.change_password_txt));
        change_password_toolbar.setTitleTextColor(Color.WHITE);
        setSupportActionBar(change_password_toolbar);


        if (getSupportActionBar() != null)
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (LanguageSetting.getLanguage().equals("en") || LanguageSetting.getLanguage().equals
                ("zh")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else if (LanguageSetting.getLanguage().equals("ar")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        change_password_toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        chang_old_pass_txt_in_lay = (TextInputLayout) findViewById(R.id.chang_old_pass_txt_in_lay);
        chang_old_pass_edt_txt = (EditText) findViewById(R.id.chang_old_pass_edt_txt);
        chang_old_pass_edt_txt.addTextChangedListener(new ChangePasswordActivity.MyChangePassTextWatcher(chang_old_pass_edt_txt));

        chang_new_pass_txt_in_lay = (TextInputLayout) findViewById(R.id.chang_new_pass_txt_in_lay);
        chang_new_pass_edt_txt = (EditText) findViewById(R.id.chang_new_pass_edt_txt);
        chang_new_pass_edt_txt.addTextChangedListener(new ChangePasswordActivity.MyChangePassTextWatcher(chang_new_pass_edt_txt));

        chang_confirm_pass_txt_in_lay = (TextInputLayout) findViewById(R.id.chang_confirm_pass_txt_in_lay);
        chang_confirm_pass_edt_txt = (EditText) findViewById(R.id.chang_confirm_pass_edt_txt);
        chang_confirm_pass_edt_txt.addTextChangedListener(new ChangePasswordActivity.MyChangePassTextWatcher(chang_confirm_pass_edt_txt));

        Button change_pass_cancel_btn = (Button) findViewById(R.id.change_pass_cancel_btn);
        Button change_pass_update_btn = (Button) findViewById(R.id.change_pass_update_btn);

        change_pass_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        change_pass_update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changePasswordUpdateRequest();
            }
        });

    }


    private void changePasswordUpdateRequest() {

        hideKeyboard();

        if (!validateOldPassword()) {
            return;
        }

        if (!validateNewPassword()) {
            return;
        }

        if (!validateConfirmPassword()) {
            return;
        }

        if (!NetworkStatus.isConnectingToInternet(ChangePasswordActivity.this)) {
            Toast.makeText(ChangePasswordActivity.this, "" + getString(R.string.no_internet_conn_tit_txt), Toast.LENGTH_SHORT).show();
            return;
        }


        if (progressBarDialog != null)
            progressBarDialog.show();

            APIService update_password_request = Webdata.getRetrofit().create(APIService.class);
            update_password_request.change_password("" + loginPrefManager.getStringValue("Lang_code"),
                    "" + loginPrefManager.getUserLoginDriverId(),
                    "" + loginPrefManager.getUserLoginToken(),
                    "" + chang_old_pass_edt_txt.getText().toString(),
                    "" + chang_new_pass_edt_txt.getText().toString(),
                    "" + chang_confirm_pass_edt_txt.getText().toString()).enqueue(new Callback<ChangePassword>() {
                @Override
                public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {

                    progressBarDialog.dismiss();

                    try {
                        if (response.body().getResponse().getHttpCode() == 200) {

                            loginPrefManager.setStringValue("login_password", "" + chang_confirm_pass_edt_txt.getText().toString());

                            showToast(response.body().getResponse().getMessage());
                            finish();
                        } else if (response.body().getResponse().getHttpCode() == 400) {

//                            Toast.makeText(ChangePasswordActivity.this, getResources().getString(R.string.old_password_miss_match_txt), Toast.LENGTH_SHORT).show();
                            showToast(response.body().getResponse().getMessage());
                        }

                    } catch (Exception e) {

                    }
                }

                @Override
                public void onFailure(Call<ChangePassword> call, Throwable t) {

                    progressBarDialog.dismiss();
                }
            });


    }

    private class MyChangePassTextWatcher implements TextWatcher {

        private final View view;

        private MyChangePassTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        public void afterTextChanged(Editable s) {
            switch (view.getId()) {
                case R.id.chang_old_pass_edt_txt:
                    validateOldPassword();
                    break;
                case R.id.chang_new_pass_edt_txt:
                    validateNewPassword();
                    break;
                case R.id.chang_confirm_pass_edt_txt:
                    validateConfirmPassword();
                    break;
            }
        }
    }

    private boolean validateOldPassword() {

        if (chang_old_pass_edt_txt.getText().toString().trim().isEmpty()) {
            chang_old_pass_txt_in_lay.setError(getString(R.string.old_password_error_txt));
            requestFocus(chang_old_pass_edt_txt);
            return false;

        } else {

            if (!loginPrefManager.getStringValue("login_password").equals("" + chang_old_pass_edt_txt.getText().toString())) {
                chang_old_pass_txt_in_lay.setError(getString(R.string.old_password_miss_match_txt));
                requestFocus(chang_old_pass_edt_txt);
                return false;
            } else {
                chang_old_pass_txt_in_lay.setErrorEnabled(false);
            }
        }

        return true;
    }

    private boolean validateNewPassword() {
        if (chang_new_pass_edt_txt.getText().toString().trim().isEmpty() || chang_new_pass_edt_txt.getText().length() <= 5) {
            chang_new_pass_txt_in_lay.setError(getString(R.string.err_msg_password));
            requestFocus(chang_new_pass_edt_txt);
            return false;
        } else {
            chang_new_pass_txt_in_lay.setErrorEnabled(false);
        }

        return true;
    }

//    private boolean validateNewPassword() {
//        if (chang_new_pass_edt_txt.getText().toString().trim().isEmpty()) {
//            chang_new_pass_txt_in_lay.setError(getString(R.string.new_password_error_txt));
//            requestFocus(chang_new_pass_edt_txt);
//            return false;
//        } else {
//            chang_new_pass_txt_in_lay.setErrorEnabled(false);
//        }
//
//        return true;
//    }

    private boolean validateConfirmPassword() {
        if (chang_confirm_pass_edt_txt.getText().toString().trim().isEmpty()) {
            chang_confirm_pass_txt_in_lay.setError(getString(R.string.confirm_password_error_txt));
            requestFocus(chang_confirm_pass_edt_txt);
            return false;
        } else {
            if (!chang_confirm_pass_edt_txt.getText().toString().equals("" + chang_new_pass_edt_txt.getText().toString())) {
                chang_confirm_pass_txt_in_lay.setError(getString(R.string.incorrect_pswd));
                requestFocus(chang_confirm_pass_edt_txt);
                return false;
            } else {
                chang_confirm_pass_txt_in_lay.setErrorEnabled(false);
            }
        }

        return true;
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void showToast(String message) {
        Toast.makeText(ChangePasswordActivity.this, message, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
    }


}
