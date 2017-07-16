package com.app.OddfoodyDriver.DriverCustomView;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StyleRes;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.app.OddfoodyDriver.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

/**
 * Created by nextbrain on 15/6/17.
 */

public class SignatureViewDialog extends Dialog {


    private Context mContext;

    private ImageView cancel_dialog_img_btn, signature_view;
    private ProgressBar detailprogressBar;

    private String ord_sig_img_path = "";


    public SignatureViewDialog(Context context, String ord_sig_img_path) {
        super(context);

        this.mContext = context;
        this.ord_sig_img_path = ord_sig_img_path;
    }

    public SignatureViewDialog(@NonNull Context context, @StyleRes int themeResId) {
        super(context, themeResId);
    }

    protected SignatureViewDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.signature_view_dialog);

        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.setCanceledOnTouchOutside(false);
        this.setCancelable(false);


        cancel_dialog_img_btn = (ImageView) findViewById(R.id.cancel_dialog_img_btn);
        signature_view = (ImageView) findViewById(R.id.signature_view);
        detailprogressBar = (ProgressBar) findViewById(R.id.detailprogressBar);


        cancel_dialog_img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });


        try {

            Glide.with(mContext).load(ord_sig_img_path).listener(new RequestListener<String, GlideDrawable>() {
                @Override
                public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                    detailprogressBar.setVisibility(View.GONE);
                    return false;
                }

                @Override
                public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                    detailprogressBar.setVisibility(View.GONE);
                    return false;
                }
            }).into(signature_view);

        } catch (Exception e) {
            Log.e("Exception", "" + e.getMessage());
        }

    }

}
