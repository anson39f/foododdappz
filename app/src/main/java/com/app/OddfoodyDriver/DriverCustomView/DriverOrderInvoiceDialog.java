package com.app.OddfoodyDriver.DriverCustomView;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.R;


public class DriverOrderInvoiceDialog extends Dialog {

    private Context context;
    private String invoice_url;

    private ImageView my_ord_invoice_close_btn;

    private WebView my_ord_dialog_invoice_web_view;

    public DriverOrderInvoiceDialog(Context context, int themeResId) {
        super(context, themeResId);
    }

    public DriverOrderInvoiceDialog(Context context, String invoice_url) {
        super(context);
        this.context = context;
        this.invoice_url = invoice_url;
    }

    protected DriverOrderInvoiceDialog(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.my_order_invoice_dialog);
        this.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        this.setCanceledOnTouchOutside(false);

        my_ord_invoice_close_btn = (ImageView)findViewById(R.id.my_ord_invoice_close_btn);
        my_ord_invoice_close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        my_ord_dialog_invoice_web_view = (WebView)findViewById(R.id.my_ord_dialog_invoice_web_view);

        my_ord_dialog_invoice_web_view.setBackgroundColor(Color.TRANSPARENT);
        my_ord_dialog_invoice_web_view.setWebViewClient(new WebViewClient());
        WebSettings settings = my_ord_dialog_invoice_web_view.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setAllowContentAccess(true);
        settings.setAppCacheEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setRenderPriority(WebSettings.RenderPriority.HIGH);
        settings.setJavaScriptEnabled(true);
        settings.setSupportZoom(true);
        settings.setJavaScriptCanOpenWindowsAutomatically(true);
        settings.setBuiltInZoomControls(true);

//        if (Build.VERSION.SDK_INT >= 11){
//            my_ord_dialog_invoice_web_view.setLayerType(View.LAYER_TYPE_HARDWARE, null);
//        }

        if (!NetworkStatus.isConnectingToInternet(context)) {
            Toast.makeText(context, "" + context.getString(R.string.no_internet), Toast.LENGTH_SHORT).show();
        }

        String myPdfUrl = ""+invoice_url;
        String url = "http://docs.google.com/gview?embedded=true&url=" + myPdfUrl;
        my_ord_dialog_invoice_web_view.getSettings().setJavaScriptEnabled(true);
        my_ord_dialog_invoice_web_view.loadUrl(""+ url);


    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
