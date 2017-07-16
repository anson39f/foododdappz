package com.app.OddfoodyDriver.DriverActivities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.app.OddfoodyDriver.DriverCustomView.InkView;
import com.app.OddfoodyDriver.DriverPhotoPicker.DefaultCallback;
import com.app.OddfoodyDriver.DriverPhotoPicker.EasyImage;
import com.app.OddfoodyDriver.DriverResponseModel.UpdateStatusRes;
import com.app.OddfoodyDriver.DriverUtils.NetworkStatus;
import com.app.OddfoodyDriver.DriverWebService.APIService;
import com.app.OddfoodyDriver.DriverWebService.Webdata;
import com.app.OddfoodyDriver.LocalizationActivity.LocalizationActivity;
import com.app.OddfoodyDriver.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by Next Brain on 27-01-2017.
 */

public class SignatureActivity extends LocalizationActivity {

    public final static String APP_PATH_SD_CARD = "/DesiredSubfolderName/";
    public final static String APP_THUMBNAIL_PATH_SD_CARD = "thumbnails";

    InkView inkView;

    Button cancel_btn;
    Button ok_btn;

    String Order_id = "";


    File signature_file;

    String filePath = "";

    File file = null;

    File attachment_file;


    private TextView attachment_upload_text;
    private ImageView tick_image;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signature_view);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            this.Order_id = bundle.getString("order_id");
        }


        Log.e("getUserLoginToken", "" + loginPrefManager.getUserLoginToken());
        Log.e("getUserLoginDriverId", "" + loginPrefManager.getUserLoginDriverId());

        EasyImage.configuration(this)
                .setImagesFolderName("OddappzDriver")
                .setCopyTakenPhotosToPublicGalleryAppFolder(true)
                .setCopyPickedImagesToPublicGalleryAppFolder(true)
                .setAllowMultiplePickInGallery(false);

        InitilizeView();

    }

    private void InitilizeView() {

        inkView = (InkView) findViewById(R.id.ink);

        cancel_btn = (Button) findViewById(R.id._cancel);
        ok_btn = (Button) findViewById(R.id._ok);

        attachment_upload_text = (TextView) findViewById(R.id.upload_doc_text);
        tick_image = (ImageView) findViewById(R.id.upload_image_status);

        CancelButtonCallAction();
        OkButtonClickActionCall();

        uploadAttachment();

    }


    private void uploadAttachment() {

        hideKeyboard();

        attachment_upload_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EasyImage.openChooserWithGallery(SignatureActivity.this, "" + getString(R.string.pick_image_intent_text), 2);
            }
        });

    }

    private void OkButtonClickActionCall() {

        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SignatureUpdateMethod();

            }
        });

    }

    private void SignatureUpdateMethod(){

        if (!NetworkStatus.isConnectingToInternet(SignatureActivity.this)) {
            Toast.makeText(SignatureActivity.this, "" + getString(R.string.no_internet_conn_tit_txt), Toast.LENGTH_SHORT).show();
            return;
        }

//        if(attachment_file == null){
//            Toast.makeText(SignatureActivity.this, "" + getString(R.string.please_add_atta_txt), Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if (!attachment_file.exists() && !attachment_file.canRead()) {
//            Toast.makeText(SignatureActivity.this, "" + getString(R.string.attachemnt_receipt_err_msg), Toast.LENGTH_SHORT).show();
//            return;
//        }


        if (progressBarDialog != null) {
            progressBarDialog.show();
        }

        SaveImageTask saveBitmap = new SaveImageTask(inkView.getBitmap());
        saveBitmap.execute();

//        if (!file.exists() && !file.canRead()) {
//            return;
//        }

    }



    private void CancelButtonCallAction() {

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    public class SaveImageTask extends AsyncTask<Void, Integer, Boolean> {

        private Bitmap bitmap;

        SaveImageTask(Bitmap bitmap) {
            this.bitmap = bitmap;
        }

        @Override
        protected Boolean doInBackground(Void... params) {

            // Create a media file name
            Calendar c = Calendar.getInstance();
            String timeStamp = new SimpleDateFormat("ddMMyyyy_HHmm").format(c.getTime());
            String mImageName = "Signature_" + timeStamp + ".jpg";
            String albumName = "Driver App";

            String state = Environment.getExternalStorageState();

            // If there is external storage, save it in the pictures album. If not, save on internal storage
            if (Environment.MEDIA_MOUNTED.equals(state)) {
                file = new File(SignatureActivity.this.getExternalFilesDir(Environment.DIRECTORY_PICTURES), albumName);
                if (!file.mkdirs()) {
                    file = new File(SignatureActivity.this.getFilesDir(), mImageName);
                }
            } else {
                file = new File(SignatureActivity.this.getFilesDir(), mImageName);
            }


            OutputStream fOut = null;
            try {
                fOut = new FileOutputStream(file);
                fOut.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            try {
                FileOutputStream fos = new FileOutputStream(file);
                filePath = file.getAbsolutePath();
                Log.e("Filepath", filePath);
                bitmap.compress(Bitmap.CompressFormat.PNG, 90, fos);
                fos.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return false;
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }

            return true;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(Boolean success) {
//            Log.e("onPostExecute", "success");
//            Log.e("onPostExecute", "file path:-" + filePath);
            DeliverySucessRequestMethod();
        }

    }

    private void DeliverySucessRequestMethod() {

        try {

            if (!file.exists() && !file.canRead()) {
                progressBarDialog.dismiss();
                return;
            }

            RequestBody language = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getStringValue("Lang_code"));
            RequestBody driver_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getUserLoginDriverId());
            RequestBody token = RequestBody.create(MediaType.parse("multipart/form-data"), "" + loginPrefManager.getUserLoginToken());
            RequestBody order_id = RequestBody.create(MediaType.parse("multipart/form-data"), "" + Order_id);
            RequestBody order_status = RequestBody.create(MediaType.parse("multipart/form-data"), "12");

            // attacched document file
//            RequestBody upload_attachment = RequestBody.create(MediaType.parse("multipart/form-data"), attachment_file);
//            final MultipartBody.Part order_attachment = MultipartBody.Part.createFormData("order_attachment", attachment_file.getName(), upload_attachment);

            // attached digital signature
            RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
            final MultipartBody.Part signature_file = MultipartBody.Part.createFormData("digital_signature", file.getName(), requestFile);


            APIService order_deliverd_request = Webdata.getRetrofit().create(APIService.class);
            order_deliverd_request.change_order_status(language, driver_id, token, order_id, order_status, signature_file).enqueue(new Callback<UpdateStatusRes>() {
                @Override
                public void onResponse(Call<UpdateStatusRes> call, Response<UpdateStatusRes> response) {

                    try {

                        progressBarDialog.dismiss();
                        Log.e("onResponse", "" + response.raw().toString());

                        if (response.body().getResponse().getHttpCode() == 200) {

                            LocalBroadcastManager.getInstance(SignatureActivity.this).sendBroadcast(new Intent("driver_order_list"));

                            Toast.makeText(SignatureActivity.this, "" + response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();

                            finish();

                        } else {
                            Toast.makeText(SignatureActivity.this, "" + response.body().getResponse().getMessage(), Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        progressBarDialog.dismiss();
                        Log.e("Exception", "" + e.getMessage());
                    }
                }

                @Override
                public void onFailure(Call<UpdateStatusRes> call, Throwable t) {
                    progressBarDialog.dismiss();
                }
            });

        } catch (Exception e) {
            progressBarDialog.dismiss();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        EasyImage.handleActivityResult(requestCode, resultCode, data, this, new DefaultCallback() {
            @Override
            public void onImagePickerError(Exception e, EasyImage.ImageSource source, int type) {
                //Some error handling
                e.printStackTrace();
            }

            @Override
            public void onImagesPicked(List<File> imageFiles, EasyImage.ImageSource source, int type) {
                Log.e("type", "" + type);
                if (type == 2) {
                    onPhotosDrivingReturned(imageFiles);
                }
            }

            @Override
            public void onCanceled(EasyImage.ImageSource source, int type) {
                //Cancel handling, you might wanna remove taken photo if it was canceled
                if (source == EasyImage.ImageSource.CAMERA) {
                    File photoFile = EasyImage.lastlyTakenButCanceledPhoto(SignatureActivity.this);
                    if (photoFile != null) photoFile.delete();
                }
            }
        });

    }

    private void onPhotosDrivingReturned(List<File> imageFiles) {
        attachment_file = imageFiles.get(0).getAbsoluteFile();
//        Log.e("Attachments path", "" + attachment_file.getAbsolutePath());
        tick_image.setVisibility(View.VISIBLE);
    }


    private void hideKeyboard() {
        InputMethodManager inputMethodManager = (InputMethodManager) this.getSystemService(Activity.INPUT_METHOD_SERVICE);
        View focusedView = this.getCurrentFocus();
        if (focusedView != null) {
            inputMethodManager.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }


    @Override
    protected void onDestroy() {
        // Clear any configuration that was done!
        EasyImage.clearConfiguration(SignatureActivity.this);
        super.onDestroy();
    }


}
