package com.app.OddfoodyDriver.DriverActivities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DotsProgressBar.DDProgressBarDialog;
import com.app.OddfoodyDriver.DriverCustomView.CircleImageView;
import com.app.OddfoodyDriver.DriverPhotoPicker.DefaultCallback;
import com.app.OddfoodyDriver.DriverPhotoPicker.EasyImage;
import com.app.OddfoodyDriver.DriverResponseModel.CountryList;
import com.app.OddfoodyDriver.DriverResponseModel.CountryListResponseDatum;
import com.app.OddfoodyDriver.DriverResponseModel.SignUp;
import com.app.OddfoodyDriver.DriverResponseModel.cityDatum;
import com.app.OddfoodyDriver.DriverResponseModel.cityList;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompleteFilter;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocomplete;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by nextbrain on 5/5/17.
 */

public class SignUpActivity extends LocalizationActivity {
    private TextView inputTermsandCondition,termConditions;
    private CheckBox inputIAgree;

    private TextInputLayout inputLayoutEmail;
    private TextInputLayout inputLayoutPassword, inputLayoutConfirmPswd;
    private TextInputLayout input_layout_fn, input_layout_ln;
    private TextInputLayout inputLayoutMobile, contact_address_layout;


    private EditText last_name_edt_txt, inputEmail, inputPassword, inputMobile, inputCivilID,
            inputMemberID, country_code, input_fname, input_cnfrm_pswd_text, contact_add_text;
    private TextView driving_lic_text, driver_lic_text, id_card_text;

    private Button btnCancel, btnSignUp;
    int PLACE_AUTOCOMPLETE_REQUEST_CODE = 1;
    private CircleImageView ProfImgeView, sign_up_prof_add_prof_pic_btn;
    private Spinner CitySpinner;
    private ArrayAdapter<cityDatum> cityArrayAdapter;

    private AlertDialog dialog;
    private Spinner CountrySpinner;
    private ArrayAdapter<CountryListResponseDatum> countryArrayAdapter;


    private LoginPrefManager loginPrefManager;
    private DDProgressBarDialog progressDialog;

    private String regExpName = "^[a-zA-Z\\s]*$";
    private String regNumber = "^[0-9]{10}$";

    private APIService addService;
    String country_id;
    String city_id;

    File Driving_License_file, Driver_License_file, Id_card_file, Sign_up_file;
    Place place;

    String mob_num_;
    String termsandcondition;

    private ImageView tick_driver, tick_driving, tick_id;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        IntializeView();
    }

    private void IntializeView() {
        loginPrefManager = new LoginPrefManager(SignUpActivity.this);
        progressDialog = Webdata.getProgressBarDialog(SignUpActivity.this);

        input_layout_fn = (TextInputLayout) findViewById(R.id.input_layout_name);
        input_layout_ln = (TextInputLayout) findViewById(R.id.sign_up_last_name_txt_lay);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);
        inputLayoutMobile = (TextInputLayout) findViewById(R.id.input_layout_mobile);
        inputLayoutConfirmPswd = (TextInputLayout) findViewById(R.id.input_layout_cnpassword);
        contact_address_layout = (TextInputLayout) findViewById(R.id.input_layout_address);


        input_fname = (EditText) findViewById(R.id.input_name);
        last_name_edt_txt = (EditText) findViewById(R.id.signup_last_name_edt_txt);
        inputEmail = (EditText) findViewById(R.id.input_email);
        inputPassword = (EditText) findViewById(R.id.input_password);
        inputMobile = (EditText) findViewById(R.id.input_mobile);
        country_code = (EditText) findViewById(R.id.country_code_text);
        input_cnfrm_pswd_text = (EditText) findViewById(R.id.input_cnpassword);
        contact_add_text = (EditText) findViewById(R.id.input_address);

        driving_lic_text = (TextView) findViewById(R.id.input_dr_licence);
        driver_lic_text = (TextView) findViewById(R.id.input_driver_lic);
        id_card_text = (TextView) findViewById(R.id.input_id_card);

        inputTermsandCondition = (TextView) findViewById(R.id.terms_and_condition);
        termConditions = (TextView) findViewById(R.id.terms_condition);
        inputIAgree = (CheckBox) findViewById(R.id.input_i_agree);

        btnCancel = (Button) findViewById(R.id.btn_cancel);
        btnSignUp = (Button) findViewById(R.id.btn_signUp);

        ProfImgeView = (CircleImageView) findViewById(R.id.sign_up_prof_image_view);
        sign_up_prof_add_prof_pic_btn = (CircleImageView) findViewById(R.id.sign_up_prof_add_prof_pic_btn);


        CitySpinner = (Spinner) findViewById(R.id.pick_city_spinner);

        CountrySpinner = (Spinner) findViewById(R.id.pick_country_spinner);

        tick_driving = (ImageView) findViewById(R.id.driving_image);
        tick_driver = (ImageView) findViewById(R.id.driver_image);
        tick_id = (ImageView) findViewById(R.id.id_card_image);

        LinearLayout layoutSignin = (LinearLayout) findViewById(R.id.layout_signin);


        input_fname.addTextChangedListener(new MyTextWatcher(input_fname));
        last_name_edt_txt.addTextChangedListener(new MyTextWatcher(last_name_edt_txt));
        inputEmail.addTextChangedListener(new MyTextWatcher(inputEmail));
        inputPassword.addTextChangedListener(new MyTextWatcher(inputPassword));
        input_cnfrm_pswd_text.addTextChangedListener(new MyTextWatcher(input_cnfrm_pswd_text));
        inputMobile.addTextChangedListener(new MyTextWatcher(inputMobile));
        contact_add_text.addTextChangedListener(new MyTextWatcher(contact_add_text));

//        SpannableString spannableString = new SpannableString((getResources().getString(R.string.hint_i_agree_the_terms_and_conditions_txt)));
//        SpannableString spannableString = new SpannableString(Html.fromHtml(getResources().getString(R.string.hint_i_agree_the_terms_and_conditions_txt)));
//        ClickableSpan clickableSpan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                Intent SignIntIntent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
//                SignIntIntent.putExtra("cmstype", "1");
//                SignIntIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(SignIntIntent);
//
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        };
//
//        if (LanguageSetting.getLanguage().equals("en")) {
//            spannableString.setSpan(clickableSpan, 17, 36, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        } else {
//            spannableString.setSpan(clickableSpan, 17, 30, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//
//
//        ClickableSpan privacyspan = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                Intent SignIntIntent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
//                SignIntIntent.putExtra("cmstype", "2");
//                SignIntIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(SignIntIntent);
//
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        };
//
//        if (LanguageSetting.getLanguage().equals("en")) {
//            spannableString.setSpan(privacyspan, 38, 54, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        } else {
//            spannableString.setSpan(privacyspan, 31, 51, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }
//        ClickableSpan contracts = new ClickableSpan() {
//            @Override
//            public void onClick(View textView) {
//                Intent SignIntIntent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
//                SignIntIntent.putExtra("cmstype", "3");
//                SignIntIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//                startActivity(SignIntIntent);
//
//            }
//
//            @Override
//            public void updateDrawState(TextPaint ds) {
//                super.updateDrawState(ds);
//                ds.setUnderlineText(false);
//            }
//        };
//        if (LanguageSetting.getLanguage().equals("en")) {
//            spannableString.setSpan(contracts, 59, 79, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        } else {
//            spannableString.setSpan(contracts, 53, 70, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
//        }


//        inputTermsandCondition.setText(spannableString);
//        inputTermsandCondition.setMovementMethod(LinkMovementMethod.getInstance());
//        inputTermsandCondition.setHighlightColor(Color.TRANSPARENT);

        CancelButtonClickActionCall();

        SignUpButtonClickActionCall();

        LocationOnClickActionCall();

        UploadDriverLicence();

        UploadDrivingLicecne();

        UploadIdCard();

        ProfilePic();

        TermsandConditions();

        CountrytRequestMethod();

//        Signin
        layoutSignin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Signup create new account
                Intent SignIntIntent = new Intent(SignUpActivity.this, SignInActivity.class);
                SignIntIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                SignIntIntent.putExtra("login_type", "menu_page");
                startActivity(SignIntIntent);
                finish();
            }
        });

    }

    private void TermsandConditions() {

        inputTermsandCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent SignIntIntent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
                SignIntIntent.putExtra("cmstype","1");
                SignIntIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
               startActivity(SignIntIntent);
            }
        });

        termConditions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent SignIntIntent = new Intent(SignUpActivity.this, TermsAndConditionsActivity.class);
                SignIntIntent.putExtra("cmstype","2");
                SignIntIntent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(SignIntIntent);
            }
        });

    }


    private void CountrytRequestMethod() {

        addService = Webdata.getRetrofit().create(APIService.class);
        addService.Country_List("" + loginPrefManager.getStringValue("Lang_code")).enqueue(new Callback<CountryList>() {
            @Override
            public void onResponse(Call<CountryList> call, Response<CountryList> response) {


                try {
                    if (response.body().getResponse() != null) {
                        if (response.body().getResponse().getHttpCode() == 200) {

                            CountryListSpinner(response.body().getResponse().getData());

                        } else {

                        }
                    }
                } catch (Exception e) {

                }
            }

            @Override
            public void onFailure(Call<CountryList> call, Throwable t) {

            }

        });


    }

    public void CountryListSpinner(final List<CountryListResponseDatum> country_list) {


        CountryListResponseDatum countryListResponseDatum = new CountryListResponseDatum();
        countryListResponseDatum.setCountryName("" + getString(R.string.pick_country_txt));
        countryListResponseDatum.setCid(0);
        country_list.add(countryListResponseDatum);

        countryArrayAdapter = new ArrayAdapter<CountryListResponseDatum>(this, android.R.layout.simple_spinner_dropdown_item, country_list) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                CountryListResponseDatum countryList = country_list.get(position);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(countryList.getCountryName());
                text.setTextSize(14);
                text.setTextColor(getResources().getColor(R.color.light_grey_color));
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                CountryListResponseDatum countryList = country_list.get(position);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                if (position == getCount()) {
                    text.setText(countryList.getCountryName());
                    text.setTextSize(14);
                    text.setTextColor(getResources().getColor(R.color.light_grey_color));
                } else {
                    text.setHint(getItem(position).toString());
                    text.setText(countryList.getCountryName());
                    text.setTextSize(14);
                    text.setTextColor(getResources().getColor(R.color.light_grey_color));
                }

                return view;
            }

            @Override
            public int getCount() {
                return super.getCount() - 1;
            }

        };

        CountrySpinner.setAdapter(countryArrayAdapter);
        CountrySpinner.setSelection(countryArrayAdapter.getCount());


        CountrySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();

                Log.e("country_size", "" + country_list.size());
                Log.e("adapter", "" + countryArrayAdapter.getCount());
                Log.e("getitem", "" + CountrySpinner.getSelectedItemPosition());
                if (countryArrayAdapter.getCount() == CountrySpinner.getSelectedItemPosition()) {

                    AddSelectCityMethod(new ArrayList<cityDatum>());

                } else {

                    if (countryArrayAdapter.getItem(position).getCid() != 0) {
                        country_id = String.valueOf(countryArrayAdapter.getItem(position).getCid());
                        CountryBasedCitySearch(country_id);
                    }

                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    public void AddSelectCityMethod(ArrayList<cityDatum> cityLists) {

        cityDatum cityDatum = new cityDatum();
        cityDatum.setCityName("" + getString(R.string.pick_loc_select_city_txt));
        cityDatum.setId(0);
        cityLists.add(cityDatum);

        CountryBasedCityList(cityLists);

    }

    private void CountryBasedCityList(final List<cityDatum> citybasedlist) {

        cityDatum cityDatum = new cityDatum();
        cityDatum.setCityName("" + getString(R.string.pick_loc_select_city_txt));
        cityDatum.setId(0);
        citybasedlist.add(cityDatum);

        cityArrayAdapter = new ArrayAdapter<cityDatum>(SignUpActivity.this, android.R.layout.simple_spinner_dropdown_item, citybasedlist) {
            @Override
            public View getDropDownView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);
                text.setText(citybasedlist.get(position).getCityName());
                text.setTextSize(14);
                text.setTextColor(getResources().getColor(R.color.light_grey_color));
                return view;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                // TODO Auto-generated method stub
                View view = super.getView(position, convertView, parent);
                TextView text = (TextView) view.findViewById(android.R.id.text1);

                if (position == getCount()) {
                    text.setText(citybasedlist.get(position).getCityName());
                    text.setTextSize(14);
                    text.setTextColor(getResources().getColor(R.color.light_grey_color));
                } else {
                    text.setHint(getItem(position).toString());
                    text.setText(citybasedlist.get(position).getCityName());
                    text.setTextSize(14);
                    text.setTextColor(getResources().getColor(R.color.light_grey_color));
                }

                return view;
            }

            @Override
            public int getCount() {
                if (citybasedlist.size() == 1) {
                    return super.getCount();
                } else {
                    return super.getCount() - 1;
                }
            }
        };

        CitySpinner.setAdapter(cityArrayAdapter);
        CitySpinner.setSelection(citybasedlist.size() - 1);


        CitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                hideKeyboard();

                if (cityArrayAdapter.getItem(position).getId() != 0) {
                    city_id = String.valueOf(cityArrayAdapter.getItem(position).getId());

                }


            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


    }

    private void CountryBasedCitySearch(String country_id) {
        this.country_id = country_id;
        if (progressDialog != null) {
            progressDialog.show();
        }
        addService.CityList("" + loginPrefManager.getStringValue("Lang_code"), country_id).enqueue(new Callback<cityList>() {
            @Override
            public void onResponse(Call<cityList> call, Response<cityList> response) {
                progressDialog.dismiss();

                if (response.body().getResponse().getHttpCode() == 200) {

                    if (response.body().getResponse().getData().size() != 0) {

                        CountryBasedCityList(response.body().getResponse().getData());
                    } else {
                        AddSelectCityMethod(new ArrayList<cityDatum>());
                    }

                } else if (response.body().getResponse().getHttpCode() == 400) {
                    Log.e("error", "" + response.body().getResponse().getStatus());

                    AddSelectCityMethod(new ArrayList<cityDatum>());
                }
            }

            @Override
            public void onFailure(Call<cityList> call, Throwable t) {
                progressDialog.dismiss();
            }
        });

    }

    private void ProfilePic() {

        sign_up_prof_add_prof_pic_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(SignUpActivity.this, "" + getString(R.string.pick_image_intent_text), 0);

            }
        });

        ProfImgeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(SignUpActivity.this, "" + getString(R.string.pick_image_intent_text), 0);

            }
        });
    }


    private void UploadDrivingLicecne() {

        hideKeyboard();
        driving_lic_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(SignUpActivity.this, "" + getString(R.string.pick_image_intent_text), 1);
            }
        });

    }

    private void UploadDriverLicence() {
        hideKeyboard();
        driver_lic_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(SignUpActivity.this, "" + getString(R.string.pick_image_intent_text), 2);
            }
        });

    }

    private void UploadIdCard() {
        hideKeyboard();
        id_card_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(SignUpActivity.this, "" + getString(R.string.pick_image_intent_text), 3);
            }
        });

    }

    private void SignUpButtonClickActionCall() {
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignUp();
                hideKeyboard();
            }
        });

        EasyImage.configuration(this)
                .setImagesFolderName("Arabty")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);


    }


    private void LocationOnClickActionCall() {

        contact_add_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    AutocompleteFilter typeFilter = new AutocompleteFilter.Builder()
                            .setTypeFilter(AutocompleteFilter.TYPE_FILTER_ADDRESS)
                            .build();
                    Intent intent = new PlaceAutocomplete.IntentBuilder(PlaceAutocomplete.MODE_FULLSCREEN).setFilter(typeFilter)
                            .build(SignUpActivity.this);

                    startActivityForResult(intent, PLACE_AUTOCOMPLETE_REQUEST_CODE);

                } catch (GooglePlayServicesRepairableException e) {
                    // TODO: Handle the error.
                } catch (GooglePlayServicesNotAvailableException e) {
                    // TODO: Handle the error.
                }
            }
        });

    }

    private void CancelButtonClickActionCall() {

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void SignUp() {
        if (!validateName()) {
            return;
        }

        if (!validateLastName()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validatePassword()) {
            return;
        }
        if (!validateConfirmPassword()) {
            return;
        }

        if (!validateMobile()) {
            return;
        }
        if (!validateAddress()) {
            return;
        }

        if (!validateCheckbox()) {
            return;
        }

        mob_num_ = country_code.getText().toString() + "" + inputMobile.getText().toString().trim();


        if (countryArrayAdapter != null && countryArrayAdapter.getCount() != 0) {

            if (!countryArrayAdapter.getItem(CountrySpinner.getSelectedItemPosition()).getCid().equals("0")) {

                if (cityArrayAdapter != null && cityArrayAdapter.getCount() != 0) {

                    if (!cityArrayAdapter.getItem(CitySpinner.getSelectedItemPosition()).getId().equals("0")) {

                        loginPrefManager.setCityIDandName("" + countryArrayAdapter.getItem(CountrySpinner.getSelectedItemPosition()).getCid(),
                                "" + countryArrayAdapter.getItem(CountrySpinner.getSelectedItemPosition()).getCountryName());

                        loginPrefManager.setLocIDandName("" + cityArrayAdapter.getItem(CitySpinner.getSelectedItemPosition()).getId(),
                                "" + cityArrayAdapter.getItem(CitySpinner.getSelectedItemPosition()).getCityName());

                    } else {

                        if (cityArrayAdapter.getCount() == 1) {
                            showToast("" + getString(R.string.err_msg_select_any_loc_txt));
                        } else {
                            showToast("" + getString(R.string.err_msg_select_any_loc));
                        }

                    }

                } else {
                    showToast("" + getString(R.string.err_msg_no_loc_found));
                }

            } else {
                showToast("" + getString(R.string.err_msg_select_any_city));
            }

        } else {
            showToast("" + getString(R.string.err_msg_city_not_found));
        }

//
//        if (Driver_License_file != null && Driving_License_file != null && Id_card_file != null) {
//            SignUpCall();
//        }else {
//            Toast.makeText(this, getResources().getString(R.string.attachment), Toast.LENGTH_SHORT).show();
//        }


        if (Sign_up_file != null && Driver_License_file != null && Driving_License_file != null && Id_card_file != null) {
            SignUpCall();
        }else {
            if (Sign_up_file == null){

                Toast.makeText(this, getResources().getString(R.string.uploadprofilephoto), Toast.LENGTH_SHORT).show();
            }else{
            Toast.makeText(this, getResources().getString(R.string.attachment), Toast.LENGTH_SHORT).show();
        }
        }
    }

    private void SignUpCall() {
        try {
            if (progressDialog != null) {
                progressDialog.show();
            }
            if (!Sign_up_file.exists() && !Sign_up_file.canRead()) {

                return;
            }

            if (!Driver_License_file.exists() && !Driver_License_file.canRead()) {
                return;
            }

            if (!Driving_License_file.exists() && !Driving_License_file.canRead()) {
                return;
            }

            if (!Id_card_file.exists() && !Id_card_file.canRead()) {
                return;
            }


            Log.e("fnmae",""+input_fname.getText().toString());
            Log.e("lname",""+last_name_edt_txt.getText().toString());
            Log.e("email",""+inputEmail.getText().toString());
            Log.e("passeod",""+inputPassword.getText().toString());
            Log.e("confirm",""+input_cnfrm_pswd_text.getText().toString());
            Log.e("mob",mob_num_);
            Log.e("country_id",""+country_id);
            Log.e("city_id",""+city_id);
            Log.e("add",""+contact_add_text.getText().toString());
            Log.e("pplace", "" + place.getLatLng().latitude);
            Log.e("place", "" + place.getLatLng().longitude);
            Log.e("driving_lic", "" + Driving_License_file.getName());
            Log.e("driver_lic", "" + Driver_License_file.getName());
            Log.e("id_lic", "" + Sign_up_file.getName());
            Log.e("prof_lic", "" + Id_card_file.getName());
            Log.e("language",""+loginPrefManager.getStringValue("Lang_code"));



            RequestBody language = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getStringValue("Lang_code"));
            RequestBody first_name = RequestBody.create(MediaType.parse("multipart/form-data"), input_fname.getText().toString().trim());
            RequestBody last_name = RequestBody.create(MediaType.parse("multipart/form-data"), last_name_edt_txt.getText().toString().trim());
            RequestBody email = RequestBody.create(MediaType.parse("multipart/form-data"), inputEmail.getText().toString().trim());
            RequestBody Password = RequestBody.create(MediaType.parse("multipart/form-data"), inputPassword.getText().toString().trim());
            RequestBody Confirm_Password = RequestBody.create(MediaType.parse("multipart/form-data"), input_cnfrm_pswd_text.getText().toString().trim());
            RequestBody mobile = RequestBody.create(MediaType.parse("multipart/form-data"), mob_num_.trim());
            RequestBody Country_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + country_id);
            RequestBody City_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + city_id);
            RequestBody contact_add = RequestBody.create(MediaType.parse("multipart/form-data"), contact_add_text.getText().toString());
            RequestBody latitude = RequestBody.create(MediaType.parse("multipart/form-data"), "" + place.getLatLng().latitude);
            RequestBody longitude = RequestBody.create(MediaType.parse("multipart/form-data"), "" + place.getLatLng().longitude);
            RequestBody terms_and_codition = RequestBody.create(MediaType.parse("multipart/form-data"), "1");

            // create RequestBody instance from file
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), Driving_License_file);
            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part driving_lic_ = MultipartBody.Part.createFormData("driving_licence", Driving_License_file.getName(), requestFile);
            // create RequestBody instance from file
            RequestBody driverlic = RequestBody.create(MediaType.parse("multipart/form-data"), Driver_License_file);
            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part driver_lic_ = MultipartBody.Part.createFormData("driver_licence", Driver_License_file.getName(), driverlic);


            // create RequestBody instance from file
            RequestBody id_card = RequestBody.create(MediaType.parse("multipart/form-data"), Id_card_file);
            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part id_card_file = MultipartBody.Part.createFormData("identification_card", Id_card_file.getName(), id_card);
            // create RequestBody instance from file
            RequestBody prof_pic = RequestBody.create(MediaType.parse("multipart/form-data"), Sign_up_file);
            // MultipartBody.Part is used to send also the actual file name
            final MultipartBody.Part prof_pic_file = MultipartBody.Part.createFormData("profile_image", Sign_up_file.getName(), prof_pic);

            APIService signupservice = Webdata.getRetrofit().create(APIService.class);
            signupservice.SIGN_UP_CALL(language, first_name, last_name, email, Password, Confirm_Password, mobile, Country_id, City_id,
                    contact_add, latitude, longitude, driving_lic_, driver_lic_, id_card_file, prof_pic_file, terms_and_codition).enqueue(new Callback<SignUp>() {
                @Override
                public void onResponse(Call<SignUp> call, Response<SignUp> response) {
                    progressDialog.dismiss();

                    Log.e("response", "" + response.raw().toString());

                    if (response.body().getResponse().getHttpCode() == 200) {


//                       final Toast toast= Toast.makeText(SignUpActivity.this, response.body().getResponse().getMessage(), Toast.LENGTH_LONG);
//                        toast.show();

                        AlertDialog.Builder builder1 = new AlertDialog.Builder(SignUpActivity.this);
                        builder1.setMessage(response.body().getResponse().getMessage());
                        builder1.setCancelable(true);

                        builder1.setPositiveButton(
                                getString(R.string.yes),
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        Intent sign_in = new Intent(SignUpActivity.this, SignInActivity.class);
                                        startActivity(sign_in);
                                        finish();
                                    }
                                });

//                        builder1.setNegativeButton(
//                                "No",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int id) {
//                                        dialog.cancel();
//                                    }
//                                });

                        AlertDialog alert11 = builder1.create();
                        alert11.setCanceledOnTouchOutside(false);
                        alert11.show();


//                        // Set the countdown to display the toast
//                        int toastDurationInMilliSeconds = 10000;
//                        CountDownTimer toastCountDown;
//                        toastCountDown = new CountDownTimer(toastDurationInMilliSeconds, 1000000 /*Tick duration*/) {
//                            public void onTick(long millisUntilFinished) {
//                                toast.show();
//                            }
//                            public void onFinish() {
//                                toast.cancel();
//                            }
//                        };
//
//                        // Show the toast and starts the countdown
//                        toast.show();
//                        toastCountDown.start();
//                        Toast.makeText(SignUpActivity.this,getResources().getString(R.string.signup_success), Toast.LENGTH_SHORT).show();
//                        Handler handler = new Handler();
//                        handler.postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                toast.cancel();
//                            }
//                        }, 10000000);

                    } else if (response.body().getResponse().getHttpCode() == 400) {
                        Log.e("onapifailure", "" + response.body().getResponse().getMessage());
                        Toast.makeText(SignUpActivity.this, getString(R.string.email_existed), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Call<SignUp> call, Throwable t) {
                    progressDialog.dismiss();
                    Log.e("onfailure", t.getMessage());
                    hideKeyboard();
                    Toast.makeText(SignUpActivity.this, getString(R.string.email_existed), Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {
            Log.e("Exception", e.getMessage());
        }


    }

    private void showToast(String message) {
        Toast.makeText(SignUpActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private class MyTextWatcher implements TextWatcher {

        private final View view;

        private MyTextWatcher(View view) {
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
                case R.id.input_name:
                    validateName();
                    break;
                case R.id.signup_last_name_edt_txt:
                    validateLastName();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validatePassword();
                    break;
                case R.id.input_cnpassword:
                    validateConfirmPassword();
                    break;
                case R.id.input_mobile:
                    validateMobile();
                    break;
                case R.id.input_address:
                    validateAddress();
                    break;
                case R.id.input_i_agree:
                    validateCheckbox();
                    break;
            }
        }

    }


    private boolean validateName() {
        if (input_fname.getText().toString().trim().isEmpty()) {
            input_layout_fn.setError(getString(R.string.err_msg_first_name));
            requestFocus(input_fname);
            return false;
        }else if (! input_fname.getText().toString().trim().matches(regExpName)){

            input_layout_fn.setError(getString(R.string.error_msg_name));
        }

        else {
            input_layout_fn.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateLastName() {
        if (last_name_edt_txt.getText().toString().trim().isEmpty()) {
            input_layout_ln.setError(getString(R.string.err_msg_last_name));
            requestFocus(last_name_edt_txt);
            return false;
        } else if (! last_name_edt_txt.getText().toString().trim().matches(regExpName)){

            input_layout_ln.setError(getString(R.string.error_msg_name));
        }

        else {
            input_layout_ln.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputEmail.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            inputLayoutEmail.setError(getString(R.string.error_msg_Email));
//            requestFocus(inputEmail);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validatePassword() {
        if (inputPassword.getText().toString().trim().isEmpty() || inputPassword.getText().length() <= 5) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateConfirmPassword() {
        if (!inputPassword.getText().toString().equals(input_cnfrm_pswd_text.getText().toString())) {
            inputLayoutConfirmPswd.setError(getResources().getString(R.string.incorrect_pswd));
//            requestFocus(input_cnfrm_pswd_text);
            return false;
        } else {
            inputLayoutConfirmPswd.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateMobile() {

        if (inputMobile.getText().toString().trim().isEmpty()) {
            inputLayoutMobile.setError(getString(R.string.err_msg_mobile));
//            requestFocus(inputMobile);
            return false;
        } else {
            inputLayoutMobile.setErrorEnabled(false);
        }

        return true;
    }
    private boolean validateCheckbox() {
        if (!inputIAgree.isChecked()) {
            inputIAgree.setError(getString(R.string.err_msg_checkbox));
            return false;
        } else {
            inputIAgree.setError(null);
        }

        return true;
    }

    private boolean validateAddress() {
        if (contact_add_text.getText().toString().isEmpty()) {
            contact_address_layout.setError(getString(R.string.err_msg_location));
            return false;
        } else {
            contact_address_layout.setError(null);
        }
        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null)

        {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_AUTOCOMPLETE_REQUEST_CODE) {

            if (resultCode == RESULT_OK) {

                hideKeyboard();

                place = PlaceAutocomplete.getPlace(this, data);


                Log.e("place", "" + place);
                Log.e("data", "Place: " + place.getAddress());
                Log.e("lat", "" + place.getLatLng().latitude);
                Log.e("lat", "" + place.getLatLng().longitude);


                contact_add_text.setText(place.getAddress());


            } else if (resultCode == PlaceAutocomplete.RESULT_ERROR) {
                hideKeyboard();

                Status status = PlaceAutocomplete.getStatus(this, data);
                Log.e("data", status.getStatusMessage());

            } else if (resultCode == RESULT_CANCELED) {
                hideKeyboard();
                // The user canceled the operation.
            }

        }
        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {

                Log.e("type", "" + type);
                if (type == 0) {
                    onPhotosReturned(imageFiles);
                } else if (type == 1) {
                    onPhotosDrivingReturned(imageFiles);
                } else if (type == 2) {
                    onPhotosDriverReturned(imageFiles);
                } else {
                    onPhotosidReturned(imageFiles);
                }

            }


            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(SignUpActivity.this);
                    if (photoFile != null) photoFile.delete();
                }


            }


        });
    }


    private void onPhotosDrivingReturned(List<File> imageFiles) {

        Driving_License_file = imageFiles.get(0).getAbsoluteFile();
        Log.e("driving_licence", "" + Driving_License_file);
        tick_driving.setVisibility(View.VISIBLE);

    }

    private void onPhotosDriverReturned(List<File> imageFiles) {

        Driver_License_file = imageFiles.get(0).getAbsoluteFile();
        Log.e("driver_licence", "" + Driver_License_file);
        tick_driver.setVisibility(View.VISIBLE);

    }

    private void onPhotosidReturned(List<File> imageFiles) {
        Id_card_file = imageFiles.get(0).getAbsoluteFile();
        tick_id.setVisibility(View.VISIBLE);
        Log.e("id_card", "" + Id_card_file);
    }

    private void onPhotosReturned(List<File> returnedPhotos) {
        Log.e("onPhotosReturned", "" + returnedPhotos.get(0).getAbsolutePath());
        Sign_up_file = returnedPhotos.get(0).getAbsoluteFile();
        Glide.with(SignUpActivity.this).load(returnedPhotos.get(0).getAbsoluteFile()).centerCrop().diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(ProfImgeView);

    }


    @Override
    protected void onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(SignUpActivity.this);
        super.onDestroy();
    }

}