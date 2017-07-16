package com.app.OddfoodyDriver.DriverActivities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.app.OddfoodyDriver.R;
import com.app.OddfoodyDriver.androidcharts.entity.TitleValueColorEntity;
import com.app.OddfoodyDriver.androidcharts.view.PieChart;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Next Brain on 25-01-2017.
 */

public class ReportPie extends AppCompatActivity {

    PieChart piechart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pie_chart);

        initPieChart();
    }



    private void initPieChart() {
        this.piechart = (PieChart) findViewById(R.id.piechart);
        final List<TitleValueColorEntity> data3 = new ArrayList<TitleValueColorEntity>();

        data3.add(new TitleValueColorEntity(getResources().getString(R.string.piechart_title2),5,
                getResources().getColor(R.color.pickup)));
        data3.add(new TitleValueColorEntity(getResources().getString(R.string.piechart_title3),10,
                getResources().getColor(R.color.assigned)));
        data3.add(new TitleValueColorEntity(getResources().getString(R.string.piechart_title1),20,
                getResources().getColor(R.color.delivery)));
        piechart.setData(data3);
    }

}
