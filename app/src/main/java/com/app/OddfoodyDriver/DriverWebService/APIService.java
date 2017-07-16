package com.app.OddfoodyDriver.DriverWebService;

import com.app.OddfoodyDriver.DriverResponseModel.ChangePassword;
import com.app.OddfoodyDriver.DriverResponseModel.CountryList;
import com.app.OddfoodyDriver.DriverResponseModel.DrivLocRequestMethod;
import com.app.OddfoodyDriver.DriverResponseModel.DriverDetails;
import com.app.OddfoodyDriver.DriverResponseModel.DriverOrders;
import com.app.OddfoodyDriver.DriverResponseModel.DriverStatus;
import com.app.OddfoodyDriver.DriverResponseModel.LanguageList;
import com.app.OddfoodyDriver.DriverResponseModel.NotificationList;
import com.app.OddfoodyDriver.DriverResponseModel.OrderDetailsReqRes;
import com.app.OddfoodyDriver.DriverResponseModel.OrderReportResp;
import com.app.OddfoodyDriver.DriverResponseModel.SignIn;
import com.app.OddfoodyDriver.DriverResponseModel.SignUp;
import com.app.OddfoodyDriver.DriverResponseModel.UpdateStatusRes;
import com.app.OddfoodyDriver.DriverResponseModel.cityList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface APIService {


    @FormUrlEncoded
    @POST("driver-login")
    Call<SignIn> SIGN_IN_CALL(@Field("email") String email,
                              @Field("password") String password,
                              @Field("language") String language,
                              @Field("login_type") String loginType,
                              @Field("device_id") String deviceID,
                              @Field("device_token") String deviceToken,
                              @Field("latitude") String latitude,
                              @Field("longitude") String longitude);


    @Multipart
    @POST("driver-signup")
    Call<SignUp> SIGN_UP_CALL(@Part("language") RequestBody language,
                              @Part("first_name") RequestBody firstname,
                              @Part("last_name") RequestBody lastname,
                              @Part("email") RequestBody email,
                              @Part("password") RequestBody password,
                              @Part("password_confirmation") RequestBody pswdconfirmation,
                              @Part("mobile") RequestBody mobile,
                              @Part("country") RequestBody country,
                              @Part("city") RequestBody city,
                              @Part("contact_address") RequestBody address,
                              @Part("latitude") RequestBody latitude,
                              @Part("longitude") RequestBody longitude,
                              @Part MultipartBody.Part driving_lic_file,
                              @Part MultipartBody.Part driver_lic_file,
                              @Part MultipartBody.Part id_card,
                              @Part MultipartBody.Part prof_image,
                              @Part("terms_and_codintion") RequestBody terms_condition);

    @GET("languages")
    Call<LanguageList> getLanguage();

    @FormUrlEncoded
    @POST("getcountrybasedcity")
    Call<cityList> CityList(@Field("language") String lang,
                            @Field("country_id") String country_id);


    @GET("getcountry_select/{lang}")
    Call<CountryList> Country_List(@Path("lang") String lang);


    @FormUrlEncoded
    @POST("driver-change-password")
    Call<ChangePassword> change_password(@Field("language") String language,
                                         @Field("driver_id") String driverID,
                                         @Field("token") String token,
                                         @Field("old_password") String oldPassword,
                                         @Field("password") String password,
                                         @Field("password_confirmation") String passwordConfirmation);


    @FormUrlEncoded
    @POST("driver-forgot-password")
    Call<ChangePassword> forgot_password(@Field("email") String email,
                                         @Field("language") String language);


    @FormUrlEncoded
    @POST("driver-update-profile")
    Call<ChangePassword> update_profile(@Field("language") String language,
                                        @Field("driver_id") String driverID,
                                        @Field("first_name") String firstName,
                                        @Field("last_name") String lastName,
                                        @Field("mobile") String mobile,
                                        @Field("gender") String gender,
                                        @Field("email") String email,
                                        @Field("driver_status") String driverStatus,
                                        @Field("token") String token);



    @Multipart
    @POST("driver-update-profile")
    Call<ChangePassword> update_profile_with_image(@Part MultipartBody.Part file,
                                                   @Part("language") RequestBody language,
                                                   @Part("driver_id") RequestBody driverID,
                                                   @Part("first_name") RequestBody firstName,
                                                   @Part("last_name") RequestBody lastName,
                                                   @Part("mobile") RequestBody mobile,
                                                   @Part("gender") RequestBody gender,
                                                   @Part("email") RequestBody email);


    @FormUrlEncoded
    @POST("driver-orders")
    Call<DriverOrders> driver_orders(@Field("language") String language,
                                     @Field("driver_id") String driverID,
                                     @Field("token") String token);


    @FormUrlEncoded
    @POST("driver-detail")
    Call<DriverDetails> driver_details(@Field("driver_id") String driverID,
                                       @Field("token") String token);


    @FormUrlEncoded
    @POST("driver-update-location")
    Call<DrivLocRequestMethod> update_driver_location(@Field("language") String language,
                                                      @Field("driver_id") String driver_id,
                                                      @Field("token") String token,
                                                      @Field("latitude") String latitude,
                                                      @Field("longitude") String longitude,
                                                      @Field("device_id") String device_id,
                                                      @Field("device_token") String device_token,
                                                      @Field("login_type") String login_type);


    @FormUrlEncoded
    @POST("driver-order-detail")
    Call<OrderDetailsReqRes> driver_order_detail(@Field("language") String language,
                                                 @Field("driver_id") String driver_id,
                                                 @Field("token") String token,
                                                 @Field("order_id") String order_id);


    @Multipart
    @POST("change-order-status")
    Call<UpdateStatusRes> change_order_status_with_out_img(@Part("language") RequestBody language,
                                                           @Part("driver_id") RequestBody driver_id,
                                                           @Part("token") RequestBody token,
                                                           @Part("order_id") RequestBody order_id,
                                                           @Part("order_status") RequestBody order_status);




    @Multipart
    @POST("change-order-status")
    Call<UpdateStatusRes> change_order_status(@Part("language") RequestBody language,
                                              @Part("driver_id") RequestBody driver_id,
                                              @Part("token") RequestBody token,
                                              @Part("order_id") RequestBody order_id,
                                              @Part("order_status") RequestBody order_status,
                                              @Part MultipartBody.Part file);


    @Multipart
    @POST("update-order-attachments")
    Call<UpdateStatusRes> update_order_attachement(@Part("language") RequestBody language,
                                                   @Part("driver_id") RequestBody driver_id,
                                                   @Part("token") RequestBody token,
                                                   @Part("order_id") RequestBody order_id,
                                                   @Part("order_status") RequestBody order_status,
                                                   @Part MultipartBody.Part order_attachment);


    @Multipart
    @POST("update-order-attachments")
    Call<UpdateStatusRes> update_order_status(@Part("language") RequestBody language,
                                              @Part("driver_id") RequestBody driver_id,
                                              @Part("token") RequestBody token,
                                              @Part("order_id") RequestBody order_id,
                                              @Part("order_status") RequestBody order_status);



    @FormUrlEncoded
    @POST("order-report")
    Call<OrderReportResp> driver_order_report(@Field("language") String language,
                                              @Field("driver_id") String driver_id,
                                              @Field("token") String token,
                                              @Field("search_date") String search_date);


    @FormUrlEncoded
    @POST("order-report")
    Call<OrderReportResp> driver_email_invoice(@Field("language") String language,
                                               @Field("driver_id") String driver_id,
                                               @Field("token") String token,
                                               @Field("order_id") String order_id);

    @FormUrlEncoded
    @POST("driver-notification-list")
    Call<NotificationList> notification_list(@Field("language") String language,
                                             @Field("token") String token,
                                             @Field("driver_id") String driver_id);


    @FormUrlEncoded
    @POST("order-assign-driver")
    Call<DriverStatus> driver_status_call(@Field("order_id") String order_id,
                                          @Field("driver_id") String driver_id,
                                          @Field("driver_responce") String driver_responce,
                                          @Field("autoassign_order_log_id") String autoassign_order_log_id);




}
