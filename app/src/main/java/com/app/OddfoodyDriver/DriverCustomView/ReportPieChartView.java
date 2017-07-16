package com.app.OddfoodyDriver.DriverCustomView;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.view.View;

import java.util.ArrayList;

/**
 * Created by Next Brain on 25-01-2017.
 */

public class ReportPieChartView extends View {
    float start=0;
    int width;
    int[] data;
    int cx,cy;
    int numberOfparts;//it tells many data or item will be placed in chart
    private int[] color;
    private ArrayList<String> label = new ArrayList<>();
    private int temp;

    public ReportPieChartView(Context context, int numOfItems, int[] data, int[] color, ArrayList<String> labels) {
        super(context);
        setFocusable(true);
        this.numberOfparts=numOfItems;
        this.data=data;
        this.label = labels;
        this.color=color;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.WHITE);
        Paint p = new Paint();
        p.setAntiAlias(true);
        p.setColor(Color.WHITE);
        p.setStyle(Paint.Style.STROKE);
        p.setStrokeWidth(0);
        p.setStyle(Paint.Style.FILL);
        float[] scaledValues = scale();

        RectF rectF = new RectF(0,0,getWidth(),getWidth());


//
//        int centerX = (int) ((rectF.left + rectF.right) / 2);
//        int centerY = (int) ((rectF.top + rectF.bottom) / 2);
//        int radius = (int) ((rectF.right - rectF.left) / 2);
//
//        radius *= 0.5;

//        cx=cy= getWidth()/2;

        p.setColor(Color.BLACK);
        for(int i=0;i<numberOfparts;i++){


            Path path = new Path();
            p.setColor(color[i]);
            p.setStyle(Paint.Style.FILL);

            Paint paint = new Paint();
            paint.setColor(Color.WHITE);
            paint.setTextSize(24);
            paint.setTextAlign(Paint.Align.CENTER);


            canvas.drawArc(rectF,start,scaledValues[i]-1,true,p);

            int radius=getWidth()/2-100;
            cx=cy=getWidth()/2;

            canvas.drawText(label.get(0),cx,cy,paint);


//
//            canvas.drawTextOnPath(label.get(i),path,20,40*3,paint);

            start=start+scaledValues[i];
        }

//        Paint cenPaint=new Paint();
//        Paint paint=new Paint();
//        paint.setColor(Color.BLUE);
//        paint.setTextSize(24);
//        paint.setTextAlign(Paint.Align.CENTER);
//        int radius=getWidth()/2-80;
//        cenPaint.setStyle(Paint.Style.FILL);
//        cenPaint.setColor(Color.WHITE);
//        cx=cy=getWidth()/2;
//        canvas.drawCircle(cx,cy,radius,cenPaint); //this is middle black circle to hide some portions of arc
//        canvas.drawText(centerText,cx,cy,paint);
    }

    //this scale method used to calculate how much portion will be covered by specific data
    private float[] scale() {
        float[] scaledValues = new float[this.data.length];
        float total = getTotal(); //Total all values supplied to the chart
        for (int i = 0; i < this.data.length; i++) {

                scaledValues[i] = (this.data[i] / total) * 360; //Scale each value
            }
            return scaledValues;
        }


//need the sum of the data to calculate scale

    private float getTotal() {
        float total = 0;
        for (float val : this.data)
            total += val;
        return total;
    }


}