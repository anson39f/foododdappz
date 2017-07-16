package com.app.OddfoodyDriver.DotsProgressBar;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.LinearLayout;

public class DDProgressBarDialog extends Dialog {

    private Context context;

    public DDProgressBarDialog(Context context) {
        super(context);
        this.context = context;
    }

    public DDProgressBarDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);

        DilatingDotsProgressBar dilatingDotsProgressBar = new DilatingDotsProgressBar(context);
        dilatingDotsProgressBar = new DilatingDotsProgressBar(context);
        dilatingDotsProgressBar.setMinimumWidth(300);
        dilatingDotsProgressBar.setMinimumHeight(25);
        dilatingDotsProgressBar.setGrowthSpeed(600);
        dilatingDotsProgressBar.setNumberOfDots(5);
//        dilatingDotsProgressBar.setDotColor(Color.argb(255, 247, 138, 42));
        dilatingDotsProgressBar.setDotColor(Color.argb(255, 221, 0, 20));

        LinearLayout linLayout = new LinearLayout(getContext());
        linLayout.setOrientation(LinearLayout.VERTICAL);
        linLayout.setGravity(Gravity.CENTER);
        linLayout.addView(dilatingDotsProgressBar);
        ViewGroup.LayoutParams linLayoutParam = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        setContentView(linLayout, linLayoutParam);

        this.setCanceledOnTouchOutside(false);
        this.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
//        this.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);

    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
