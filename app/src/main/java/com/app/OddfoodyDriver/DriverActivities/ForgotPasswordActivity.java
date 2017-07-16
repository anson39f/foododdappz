package com.app.OddfoodyDriver.DriverActivities;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverResponseModel.ChangePassword;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 23-01-2017.
 */

public class ForgotPasswordActivity extends LocalizationActivity {

    private TextInputLayout input_email_layout;
    private EditText mail_edit_text;
    private Button forgot_pswd_cancel_btn, forgot_pswd_submit_btn;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        InitializeView();
    }

    private void InitializeView() {

        input_email_layout = (TextInputLayout) findViewById(R.id.forpswd_input_layout_email);

        mail_edit_text = (EditText) findViewById(R.id.forpswd_input_email);

        forgot_pswd_cancel_btn = (Button) findViewById(R.id.forpswd_btn_cancel);

        forgot_pswd_submit_btn = (Button) findViewById(R.id.forpswd_btn_submit);

        mail_edit_text.addTextChangedListener(new MyTextWatcher(mail_edit_text));

        ForgotPasswordSubmitClickActionCall();

        ForgotPasswordCancelClickActionCall();


    }


    private void ForgotPasswordCancelClickActionCall() {
        forgot_pswd_cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(ForgotPasswordActivity.this, SignInActivity.class));
                finish();
            }
        });
    }


    private void ForgotPasswordSubmitClickActionCall() {
        forgot_pswd_submit_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateData();
                hideKeyboard();
            }
        });
    }


    private void ValidateData() {

        if (!validateEmail()) {
            return;
        }

        ForGoTPasswordApiCall();

    }

    private void ForGoTPasswordApiCall() {
        try {
            if (progressBarDialog!=null){
                progressBarDialog.show();
            }


            APIService apiService = Webdata.getRetrofit().create(APIService.class);
            apiService.forgot_password(mail_edit_text.getText().toString().trim(),loginPrefManager.getStringValue("Lang_code")).enqueue(new Callback<ChangePassword>() {
                @Override
                public void onResponse(Call<ChangePassword> call, Response<ChangePassword> response) {
                    progressBarDialog.dismiss();
                    try
                    {
                        if(response.body().getResponse().getHttpCode() == 200)
                        {
                           showToast(response.body().getResponse().getMessage());
                            finish();
                        }else if(response.body().getResponse().getHttpCode() == 400)
                        {
                            showToast(response.body().getResponse().getMessage());

                        }

                    }catch (Exception e)
                    {

                    }
                }

                @Override
                public void onFailure(Call<ChangePassword> call, Throwable t) {

                    progressBarDialog.dismiss();
                }
            });

        }catch (Exception e){
            if(progressBarDialog != null)
            progressBarDialog.dismiss();
        }

    }

    private void showToast(String message) {
        Toast.makeText(ForgotPasswordActivity.this,message,Toast.LENGTH_LONG).show();
    }


    private class MyTextWatcher implements TextWatcher {
        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            switch (view.getId()) {

                case R.id.forpswd_input_email:
                    validateEmail();
                    break;

            }
        }
    }



    private boolean validateEmail() {
        String email = mail_edit_text.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            input_email_layout.setError(getString(R.string.error_msg_Email));
            requestFocus(mail_edit_text);
            return false;
        } else {
            input_email_layout.setErrorEnabled(false);
        }

        return true;

    }


    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }


    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }


    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null)

        {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }
}
