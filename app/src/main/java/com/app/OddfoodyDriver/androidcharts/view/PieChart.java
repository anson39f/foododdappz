/*
 * PieChart.java
 * Android-Charts
 *
 * Created by limc on 2011/05/29.
 *
 * Copyright 2011 limc.cn All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 *
 */
package com.app.OddfoodyDriver.androidcharts.view;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Point;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;

import com.app.OddfoodyDriver.androidcharts.entity.TitleValueColorEntity;

import java.util.List;

public class PieChart extends RoundChart {

    protected static List<TitleValueColorEntity> data;

    public PieChart(Context context) {
        super(context);
    }

    public PieChart(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public PieChart(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // get safe rect
        int rect = super.getWidth() > super.getHeight() ? super.getHeight()
                : super.getWidth();

        // calculate radius length
        longitudeLength = (int) ((rect / 2f) * 0.90);

        // calculate position
        position = new Point((int) (getWidth() / 2f), (int) (getHeight() / 2f));

        // draw this chart
        drawCircle(canvas);


        // draw data on chart
        drawData(canvas);
    }

    public void drawCircle(Canvas canvas) {

        Paint mPaintCircleBorder = new Paint();
        mPaintCircleBorder.setColor(circleBorderColor);
        mPaintCircleBorder.setStyle(Style.STROKE);
        mPaintCircleBorder.setStrokeWidth(2);
        mPaintCircleBorder.setAntiAlias(true);

        // draw a circle
        canvas.drawCircle(position.x, position.y, longitudeLength, mPaintCircleBorder);

    }

    public void drawData(Canvas canvas) {
        if (null != data) {
            // sum all data's value
            float sum = 0;
            for (int i = 0; i < data.size(); i++) {
                Log.e("Data_size", "" + data.size());
                sum = sum + data.get(i).getValue();
            }

            Paint mPaintFill = new Paint();
            mPaintFill.setStyle(Style.FILL);
            mPaintFill.setAntiAlias(true);

            Paint mPaintBorder = new Paint();
            mPaintBorder.setStyle(Style.STROKE);
            mPaintBorder.setStrokeWidth(8);
            mPaintBorder.setColor(longitudeColor);
            mPaintBorder.setAntiAlias(true);

            int offset = -90;
            // draw arcs of every piece
            for (int j = 0; j < data.size(); j++) {
                TitleValueColorEntity e = data.get(j);

                // get color
                mPaintFill.setColor(e.getColor());

                RectF oval = new RectF(position.x - longitudeLength, position.y
                        - longitudeLength, position.x + longitudeLength, position.y
                        + longitudeLength);
                int sweep = Math.round(e.getValue() / sum * 360f);
                canvas.drawArc(oval, offset, sweep, true, mPaintFill);
                canvas.drawArc(oval, offset, sweep, true, mPaintBorder);
                offset = offset + sweep;
            }

            float sumvalue = 0f;
            for (int k = 0; k < data.size(); k++) {
                TitleValueColorEntity e = data.get(k);
                float value = e.getValue();
                sumvalue = sumvalue + value;
                float rate = (sumvalue - value / 2) / sum;
                mPaintFill.setColor(Color.WHITE);

                // percentage
                float percentage = (int) (value / sum * 10000) / 100f;

                float offsetX = (float) (position.x - longitudeLength * 0.5
                        * Math.sin(rate * -2 * Math.PI));
                float offsetY = (float) (position.y - longitudeLength * 0.5
                        * Math.cos(rate * -2 * Math.PI));

                Paint mPaintFont = new Paint();
                mPaintFont.setColor(Color.WHITE);
                mPaintFont.setTextSize(22);
                mPaintFont.setTextAlign(Paint.Align.CENTER);


                Paint mPaintFont1 = new Paint();
                mPaintFont1.setColor(Color.WHITE);
                mPaintFont1.setTextSize(22);
                mPaintFont1.setTextAlign(Paint.Align.CENTER);


                // draw titles
                String title = e.getTitle();

                float realx = 0;
                float realy = 0;

                // TODO title position
                if (offsetX < position.x) {
                    realx = offsetX - mPaintFont.measureText(title) + 30;
                } else if (offsetX > position.x) {
                    realx = offsetX + 10;
                }

                if (offsetY > position.y) {
                    if (value / sum < 0.2f) {
                        realy = offsetY + 10;
                    } else {
                        realy = offsetY - 10;
                    }
                } else if (offsetY < position.y) {
                    if (value / sum < 0.2f) {
                        realy = offsetY - 4;
                    } else {
                        realy = offsetY + 4;
                    }
                }

                int assigned = (int) Math.round(data.get(0).getValue());
                int delivery = (int) Math.round(data.get(1).getValue());

//                Log.e("getWidth", "-" + super.getWidth());
//                Log.e("getHeight", "-" + super.getHeight());

                if (assigned == 0 && delivery != 0) {

                    canvas.drawText(title, realx + (super.getWidth() / 2), realy, mPaintFont);
                    canvas.drawText(String.valueOf(percentage) + "%", realx + (super.getWidth() / 2), realy + 35, mPaintFont1);

                } else if (delivery == 0 && assigned != 0) {

                    canvas.drawText(title, realx + (super.getWidth() / 2), realy, mPaintFont);
                    canvas.drawText(String.valueOf(percentage) + "%", realx + (super.getWidth() / 2), realy + 35, mPaintFont1);

                } else if (assigned != 0 && delivery != 0) {

                    canvas.drawText(title, realx, 100, mPaintFont);
//                    canvas.drawText(String.valueOf(percentage) + "%", realx + 50, realy + 35, mPaintFont1);
                    canvas.drawText(String.valueOf(percentage) + "%", realx, realy + 35, mPaintFont1);

                }


            }
        }
    }

    /**
     * @return the data
     */
    public List<TitleValueColorEntity> getData() {
        return data;
    }

    /**
     * @param data the data to set
     */
    public void setData(List<TitleValueColorEntity> data) {
        this.data = data;
    }


}
