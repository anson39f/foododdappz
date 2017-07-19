package com.app.OddfoodyDriver.DriverActivities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverResponseModel.OrderReportResp;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LanguageSetting;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;
import com.app.OddfoodyDriver.androidcharts.entity.TitleValueColorEntity;
import com.app.OddfoodyDriver.androidcharts.view.PieChart;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 25-01-2017.
 */

public class ReportActivity extends LocalizationActivity {

    private PieChart piechart;

    private Toolbar reporttoolbar;
    private TextView reporttoolbartext;
    Button pick_date;

    private int mYear, mMonth, mDay, selected_year, selected_month, day_month;

    Calendar calendar;

    Calendar format_calendar = Calendar.getInstance();

    private final SimpleDateFormat old_date_format = new SimpleDateFormat("yyyy-MM-dd");

    private Runnable runnable;

    Bundle bundle;

    private TextView chart_empty_msg_txt_view;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        bundle = savedInstanceState;

        reporttoolbar = (Toolbar) findViewById(R.id.report_app_bar);
        reporttoolbar.setTitle(getResources().getString(R.string.nav_reports));
        setSupportActionBar(reporttoolbar);

        reporttoolbar.setTitleTextColor(Color.WHITE);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);

        if (LanguageSetting.getLanguage().equals("en") || LanguageSetting.getLanguage().equals
                ("zh")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_en);
        } else if (LanguageSetting.getLanguage().equals("ar")) {
            getSupportActionBar().setHomeAsUpIndicator(R.drawable.nav_arrow_back_ic_ar);
        }

        reporttoolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        InitPieChart();

    }


    private void InitPieChart() {

        pick_date = (Button) findViewById(R.id.pick_date_);
        piechart = (PieChart) findViewById(R.id.piechart);

        chart_empty_msg_txt_view = (TextView) findViewById(R.id.chart_empty_msg_txt_view);

        piechart.setVisibility(View.INVISIBLE);
        chart_empty_msg_txt_view.setVisibility(View.VISIBLE);

//        Log.e("getChartDatas", "" + loginPrefManager.getChartDatas().size());
        piechart.setData(loginPrefManager.getChartDatas());


//        Log.e("getChartDate", "" + loginPrefManager.getChartDate());
        if (!loginPrefManager.getChartDate().isEmpty()) {
            pick_date.setText(loginPrefManager.getChartDate());
        } else {
            CurrentDateChartRequestMethod();
        }

//        Log.e("assigned", "loginPrefManager" + loginPrefManager.getStringValue("assigned"));
//        Log.e("deliverd", "loginPrefManager" + loginPrefManager.getStringValue("deliverd"));

        if (loginPrefManager.getStringValue("assigned").equals("0") && loginPrefManager.getStringValue("deliverd").equals("0")) {
            piechart.setVisibility(View.INVISIBLE);
            chart_empty_msg_txt_view.setVisibility(View.VISIBLE);
        } else {
            piechart.setVisibility(View.VISIBLE);
            chart_empty_msg_txt_view.setVisibility(View.GONE);
        }


        CalenderViewCall();

    }

    private void CurrentDateChartRequestMethod() {

        int date = format_calendar.get(Calendar.DAY_OF_MONTH);
        int month = format_calendar.get(Calendar.MONTH);
        int year = format_calendar.get(Calendar.YEAR);

//        Log.e("Current date", "" + old_date_format.format(getDate(year, month, date)));

        pick_date.setText("" + old_date_format.format(getDate(year, month, date)));

        ReportRequestMethod("" + old_date_format.format(getDate(year, month, date)));

    }

    private void CalenderViewCall() {

        pick_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get Current Date
                calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(ReportActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

                                if (validatePastDate(ReportActivity.this, dayOfMonth, (monthOfYear + 1), year)) {
                                    selected_year = year;
                                    selected_month = monthOfYear + 1;
                                    day_month = dayOfMonth;
                                    pick_date.setText(year + "/" + (monthOfYear + 1) + "/" + dayOfMonth);
                                    format_calendar.set(dayOfMonth, selected_month, selected_year);

                                    pick_date.setText("" + old_date_format.format(getDate(year, monthOfYear, dayOfMonth)));

                                    Log.e("old_date_format", "" + old_date_format.format(getDate(year, monthOfYear, dayOfMonth)));

                                    ReportRequestMethod("" + old_date_format.format(getDate(year, monthOfYear, dayOfMonth)));

                                }

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

    }

    public Date getDate(int year, int month, int day) {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, year);
        cal.set(Calendar.MONTH, month);
        cal.set(Calendar.DAY_OF_MONTH, day);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTime();
    }


    public void ReportRequestMethod(String selected_date) {

        try {

            Log.e("Lang_code", "" + loginPrefManager.getStringValue("Lang_code"));
            Log.e("User_Id", "" + loginPrefManager.getStringValue("User_Id"));

            Log.e("getUserLoginToken", "" + loginPrefManager.getUserLoginToken());
            Log.e("getUserLoginDriverId", "" + loginPrefManager.getUserLoginDriverId());

            if (progressBarDialog != null) {
                progressBarDialog.show();
            }

            APIService ReportService = Webdata.getRetrofit().create(APIService.class);
            ReportService.driver_order_report("" + loginPrefManager.getStringValue("Lang_code"), "" + loginPrefManager.getUserLoginDriverId(),
                    "" + loginPrefManager.getUserLoginToken(), "" + selected_date).enqueue(new Callback<OrderReportResp>() {
                @Override
                public void onResponse(Call<OrderReportResp> call, Response<OrderReportResp> response) {

                    Log.e("driver_order_report", "" + response.raw().toString());

                    progressBarDialog.dismiss();

//                    Log.e("getDispatchedStatuCount", "" + response.body().getResponse().getDispatchedStatusCount());
//                    Log.e("getDeliveredStatusCount", "" + response.body().getResponse().getDeliveredStatusCount());
//                    Log.e("getHttpCode", "-" + response.body().getResponse().getHttpCode());

                    try {

                        if (response.body().getResponse().getHttpCode() == 200) {
                            PieChartReportCall(response.body().getResponse().getDispatchedStatusCount(), response.body().getResponse().getDeliveredStatusCount());
                        }

                    }catch (Exception e){
                        Log.e("Exception"," error");

                    }

                }

                @Override
                public void onFailure(Call<OrderReportResp> call, Throwable throwable) {
                    progressBarDialog.dismiss();
                }
            });


        } catch (Exception e) {
            progressBarDialog.dismiss();
        }
    }


    //    For past Date
    public static boolean validatePastDate(Context mContext, int day, int month, int year) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);
        if (day > currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (month > currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_SHORT).show();
            return false;
        } else if (year > currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    //    For Future Date
    public static boolean validateFutureDate(Context mContext, int day, int month, int year) {
        final Calendar c = Calendar.getInstance();
        int currentYear = c.get(Calendar.YEAR);
        int currentMonth = c.get(Calendar.MONTH) + 1;
        int currentDay = c.get(Calendar.DAY_OF_MONTH);

//        Log.e("currentDay",""+currentDay);
//        Log.e("currentMonth",""+currentMonth);
//        Log.e("currentYear",""+currentYear);
//
//        Log.e("day",""+day);
//        Log.e("month",""+month);
//        Log.e("year",""+year);

        if (day < currentDay && year == currentYear && month == currentMonth) {
            Toast.makeText(mContext, "Please select valid date", Toast.LENGTH_SHORT).show();
            return false;
        } else if (month < currentMonth && year == currentYear) {
            Toast.makeText(mContext, "Please select valid month", Toast.LENGTH_SHORT).show();
            return false;
        } else if (year < currentYear) {
            Toast.makeText(mContext, "Please select valid year", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }


    private void PieChartReportCall(int assigned, int deliverd) {

//        Log.e("assigned", "" + assigned);
//        Log.e("deliverd", "" + deliverd);

        loginPrefManager.setStringValue("assigned", "" + assigned);
        loginPrefManager.setStringValue("deliverd", "" + deliverd);

        final List<TitleValueColorEntity> data3 = new ArrayList<TitleValueColorEntity>();

//        data3.add(new TitleValueColorEntity(getResources().getString(R.string.piechart_title2), 5, getResources().getColor(R.color.pickup)));
        data3.add(new TitleValueColorEntity(getResources().getString(R.string.piechart_title3), (float) assigned, getResources().getColor(R.color.assigned)));
        data3.add(new TitleValueColorEntity(getResources().getString(R.string.piechart_title1), (float) deliverd, getResources().getColor(R.color.delivery)));

        loginPrefManager.setChartDate("" + pick_date.getText());
        loginPrefManager.setChartDatas(data3);

//        Log.e("getChartDatas", "" + loginPrefManager.getChartDatas().size());

        this.recreate();


    }

    @Override
    public void onResume() {
        super.onResume();

//        piechart.setData(new ArrayList<TitleValueColorEntity>());

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.e("ReportActivity", "onRestart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e("ReportActivity", "onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e("ReportActivity", "onStop");
    }

}

