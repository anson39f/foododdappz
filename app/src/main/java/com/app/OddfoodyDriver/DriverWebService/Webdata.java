package com.app.OddfoodyDriver.DriverWebService;

import android.content.Context;

import com.app.OddfoodyDriver.DotsProgressBar.DDProgressBarDialog;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class Webdata {


    private static DDProgressBarDialog ddProgressBarDialog = null;

    //    public static String BaseUrl = "http://192.168.0.143:1004/api/";

    private static String BaseUrl = "http://food.oddappz.com/api/";
//        private static String BaseUrl = "http://107.170.233.165/api/";

    //    private static String BaseUrl = "http://192.168.0.133:2039/api/";


    public static final String term_condi_Url = "https://food.oddappz.com/cms-mob/terms-amp-conditions";
    public static final String privacy_policy = "https://food.oddappz.com/cms-mob/privacy-policy";
    public static final String contracts_users = "https://food.oddappz.com/cms-mob/driver-contract";


    public static DDProgressBarDialog getProgressBarDialog(Context context) {
        ddProgressBarDialog = new DDProgressBarDialog(context);
        return ddProgressBarDialog;
    }

    public static Retrofit getRetrofit() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(30, TimeUnit.SECONDS);
        httpClient.addInterceptor(interceptor);
        httpClient.addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {
                Request original = chain.request();

                // Customize the request
                Request request = original.newBuilder()
                        .header("Accept", "application/json")
                        .header("Authorization", "auth-token")
                        .method(original.method(), original.body())
                        .build();

                Response response = chain.proceed(request);

                // Customize or return the response
                return response;
            }
        });

        OkHttpClient OkHttpClient = httpClient.build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BaseUrl)
                .addConverterFactory(new NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .client(OkHttpClient)
                .build();

        return retrofit;
    }


}
