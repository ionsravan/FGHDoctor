package com.ambitious.fghdoctor.Interface;


import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface LoadInterface {

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//    http://webuddys.in/FGH/index.php/api/register?user_type=user&name=test&mobile=1234567890&email=email@gmail.com
  //  @Multipart
    @POST("signup")
    Call<ResponseBody> signup(@Query("device_id") String Device_id,
                              @Query("name") String name,
                              @Query("email") String email,
                              @Query("password") String password,
                              @Query("mobile") String mobile,
                              @Query("user_type") String user_type,
                              @Query("dob") String dob,
                              @Query("sex") String sex,
                              @Query("referby") String referby,
                              @Query("register_id") String register_id
                           //   @Part MultipartBody.Part file
    );

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Login %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("login")
    Call<ResponseBody> login(@Query("device_id") String Device_id,
                             @Query("username") String email,
                             @Query("user_type") String user_type,
                             @Query("password") String password,
                             @Query("register_id") String register_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Social SignUp %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("social_login")
    Call<ResponseBody> socialsignup(@Query("first_name") String name,
                                    @Query("last_name") String last_name,
                                    @Query("email") String email,
                                    @Query("social_id") String social_id,
                                    @Query("social_type") String social_type,
                                    @Query("referel_code") String referel_code);


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Profile %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_profile")
    Call<ResponseBody> getProfile(@Query("user_id") String user_id);

    @GET("get_usersingle")
    Call<ResponseBody> getSingleProfile(@Query("user_id") String user_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Profile Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Multipart
    @POST("signup")
    Call<ResponseBody> updateProfileImage(@Query("user_id") String user_id,
                                          @Query("name") String name,
                                          @Query("email") String email,
                                          @Query("password") String password,
                                          @Query("mobile") String mobile,
                                          @Query("account_no") String account_no,
                                          @Query("ifsc_code") String ifsc_code,
                                          @Query("account_first_name") String account_first_name,
                                          @Query("account_last_name") String account_last_name,
                                          @Query("payment_mobile") String payment_mobile,
                                          @Query("user_type") String user_type,
                                          @Part MultipartBody.Part file);


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Profile %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> updateProfile(@Query("user_id") String user_id,
                                     @Query("name") String name,
                                     @Query("email") String email,
                                     @Query("password") String password,
                                     @Query("mobile") String mobile,
                                     @Query("account_no") String account_no,
                                     @Query("ifsc_code") String ifsc_code,
                                     @Query("account_first_name") String account_first_name,
                                     @Query("account_last_name") String account_last_name,
                                     @Query("payment_mobile") String payment_mobile,
                                     @Query("user_type") String user_type);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Ambulance%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupAmbulance(@Query("name") String name,
                                       @Query("mobile") String mobile,
                                       @Query("email") String email,
                                       @Query("address") String address,
                                       @Query("aadhar") String aadhar,
                                       @Query("vehicle_no") String vehicle_no,
                                       @Query("user_type") String user_type,
                                       @Query("village") String village,
                                       @Query("city") String city,
                                       @Query("distric") String distric,
                                       @Query("department") String department,
                                       @Query("username") String username,
                                       @Query("password") String password,
                                       @Query("register_id") String register_id,
                                       @Query("lat") String lat,
                                       @Query("lng") String lng,
                                       @Part MultipartBody.Part file,
                                       @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Ambulance %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateAmbulanceUser(@Query("user_id") String user_id,
                                           @Query("name") String name,
                                           @Query("mobile") String mobile,
                                           @Query("email") String email,
                                           @Query("address") String address,
                                           @Query("aadhar") String aadhar,
                                           @Query("vehicle_no") String vehicle_no,
                                           @Query("user_type") String user_type,
                                           @Query("village") String village,
                                           @Query("city") String city,
                                           @Query("distric") String distric,
                                           @Query("department") String department,
                                           @Query("username") String username,
                                           @Query("password") String password,
                                           @Query("account_first_name") String account_first_name,
                                           @Query("account_last_name") String account_last_name,
                                           @Query("account_no") String account_no,
                                           @Query("ifsc_code") String ifsc_code,
                                           @Query("upi_id") String upi_id,
                                           @Query("payment_mobile") String payment_mobile,
                                           @Query("lat") String lat,
                                           @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Ambulance with image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateAmbulanceUserwithImage(@Query("user_id") String user_id,
                                                    @Query("name") String name,
                                                    @Query("mobile") String mobile,
                                                    @Query("email") String email,
                                                    @Query("address") String address,
                                                    @Query("aadhar") String aadhar,
                                                    @Query("vehicle_no") String vehicle_no,
                                                    @Query("user_type") String user_type,
                                                    @Query("village") String village,
                                                    @Query("city") String city,
                                                    @Query("distric") String distric,
                                                    @Query("department") String department,
                                                    @Query("username") String username,
                                                    @Query("password") String password,
                                                    @Query("account_first_name") String account_first_name,
                                                    @Query("account_last_name") String account_last_name,
                                                    @Query("account_no") String account_no,
                                                    @Query("ifsc_code") String ifsc_code,
                                                    @Query("upi_id") String upi_id,
                                                    @Query("payment_mobile") String payment_mobile,
                                                    @Query("lat") String lat,
                                                    @Query("lng") String lng,
                                                    @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Users %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_users")
    Call<ResponseBody> getUsers(
            @Query("user_type") String user_type,
            @Query("lat") String lat,
            @Query("long") String longitude
    );

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Remove Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("update_user_images")
    Call<ResponseBody> removeImage(@Query("user_id") String user_id,
                                   @Query("position") String position);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Multipart
    @POST("update_user_images")
    Call<ResponseBody> updateImage(@Query("user_id") String user_id,
                                   @Query("position") String position,
                                   @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Change Availablity Status %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> cahngeAvailablity(@Query("user_id") String user_id,
                                         @Query("available") String available);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Change Availablity Status Ambulance%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> cahngeAvailablityAmbulance(@Query("user_id") String user_id,
                                                  @Query("mobile") String mobile,
                                                  @Query("user_type") String user_type,
                                                  @Query("available") String available);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Blood Donor%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupBloodDonor(@Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("email") String email,
                                        @Query("address") String address,
                                        @Query("blood_group") String blood_group,
                                        @Query("user_type") String user_type,
                                        @Query("village") String village,
                                        @Query("city") String city,
                                        @Query("distric") String distric,
                                        @Query("username") String username,
                                        @Query("password") String password,
                                        @Query("register_id") String register_id,
                                        @Query("lat") String lat,
                                        @Query("lng") String lng,
                                        @Part MultipartBody.Part file,
                                        @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Blood Donor with Image%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateBloodDonorwithImage(@Query("user_id") String user_id,
                                                 @Query("name") String name,
                                                 @Query("mobile") String mobile,
                                                 @Query("email") String email,
                                                 @Query("address") String address,
                                                 @Query("blood_group") String blood_group,
                                                 @Query("user_type") String user_type,
                                                 @Query("village") String village,
                                                 @Query("city") String city,
                                                 @Query("distric") String distric,
                                                 @Query("username") String username,
                                                 @Query("password") String password,
                                                 @Query("lat") String lat,
                                                 @Query("lng") String lng,
                                                 @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Blood Donor %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateBloodDonor(@Query("user_id") String user_id,
                                        @Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("email") String email,
                                        @Query("address") String address,
                                        @Query("blood_group") String blood_group,
                                        @Query("user_type") String user_type,
                                        @Query("village") String village,
                                        @Query("city") String city,
                                        @Query("distric") String distric,
                                        @Query("username") String username,
                                        @Query("password") String password,
                                        @Query("lat") String lat,
                                        @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Market Price %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupMarketPrice(@Query("name") String name,
                                         @Query("mobile") String mobile,
                                         @Query("email") String email,
                                         @Query("address") String address,
                                         @Query("city") String city,
                                         @Query("distric") String distric,
                                         @Query("product_name") String product_name,
                                         @Query("product_price") String product_price,
                                         @Query("weight") String product_weight,
                                         @Query("user_type") String user_type,
                                         @Query("password") String password,
                                         @Query("register_id") String register_id,
                                         @Query("account_first_name") String account_first_name,
                                         @Query("account_last_name") String account_last_name,
                                         @Query("account_no") String account_no,
                                         @Query("ifsc_code") String ifsc_code,
                                         @Query("upi_id") String upi_id,
                                         @Query("payment_mobile") String payment_mobile,
                                         @Query("lat") String lat,
                                         @Query("lng") String lng,
                                         @Part MultipartBody.Part file,
                                         @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Market Price %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> updateMarketPrice(@Query("user_id") String user_id,
                                         @Query("name") String name,
                                         @Query("mobile") String mobile,
                                         @Query("email") String email,
                                         @Query("address") String address,
                                         @Query("city") String city,
                                         @Query("distric") String distric,
                                         @Query("product_name") String product_name,
                                         @Query("product_price") String product_price,
                                         @Query("weight") String product_weight,
                                         @Query("user_type") String user_type,
                                         @Query("password") String password,
                                         @Query("account_first_name") String account_first_name,
                                         @Query("account_last_name") String account_last_name,
                                         @Query("account_no") String account_no,
                                         @Query("ifsc_code") String ifsc_code,
                                         @Query("upi_id") String upi_id,
                                         @Query("payment_mobile") String payment_mobile,
                                         @Query("lat") String lat,
                                         @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Market Price with Image%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateMarketPriceWithImage(@Query("user_id") String user_id,
                                                  @Query("name") String name,
                                                  @Query("mobile") String mobile,
                                                  @Query("email") String email,
                                                  @Query("address") String address,
                                                  @Query("city") String city,
                                                  @Query("distric") String distric,
                                                  @Query("product_name") String product_name,
                                                  @Query("product_price") String product_price,
                                                  @Query("weight") String product_weight,
                                                  @Query("user_type") String user_type,
                                                  @Query("password") String password,
                                                  @Query("account_first_name") String account_first_name,
                                                  @Query("account_last_name") String account_last_name,
                                                  @Query("account_no") String account_no,
                                                  @Query("ifsc_code") String ifsc_code,
                                                  @Query("upi_id") String upi_id,
                                                  @Query("payment_mobile") String payment_mobile,
                                                  @Query("lat") String lat,
                                                  @Query("lng") String lng,
                                                  @Part MultipartBody.Part file,
                                                  @Part List<MultipartBody.Part> service_images);


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Blood Bank %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupBloodBank(@Query("name") String name,
                                       @Query("mobile") String mobile,
                                       @Query("email") String email,
                                       @Query("address") String address,
                                       @Query("product_name") String product_name,
                                       @Query("product_price") String product_price,
                                       @Query("user_type") String user_type,
                                       @Query("password") String password,
                                       @Query("register_id") String register_id,
                                       @Query("account_first_name") String account_first_name,
                                       @Query("account_last_name") String account_last_name,
                                       @Query("account_no") String account_no,
                                       @Query("ifsc_code") String ifsc_code,
                                       @Query("upi_id") String upi_id,
                                       @Query("payment_mobile") String payment_mobile,
                                       @Query("lat") String lat,
                                       @Query("lng") String lng,
                                       @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Blood Bank with Image%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateBloodBankWithImage(@Query("user_id") String user_id,
                                                @Query("name") String name,
                                                @Query("mobile") String mobile,
                                                @Query("email") String email,
                                                @Query("address") String address,
                                                @Query("product_name") String product_name,
                                                @Query("product_price") String product_price,
                                                @Query("user_type") String user_type,
                                                @Query("password") String password,
                                                @Query("account_first_name") String account_first_name,
                                                @Query("account_last_name") String account_last_name,
                                                @Query("account_no") String account_no,
                                                @Query("ifsc_code") String ifsc_code,
                                                @Query("upi_id") String upi_id,
                                                @Query("payment_mobile") String payment_mobile,
                                                @Query("lat") String lat,
                                                @Query("lng") String lng,
                                                @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Blood Bank %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateBloodBank(@Query("user_id") String user_id,
                                       @Query("name") String name,
                                       @Query("mobile") String mobile,
                                       @Query("email") String email,
                                       @Query("address") String address,
                                       @Query("product_name") String product_name,
                                       @Query("product_price") String product_price,
                                       @Query("user_type") String user_type,
                                       @Query("password") String password,
                                       @Query("account_first_name") String account_first_name,
                                       @Query("account_last_name") String account_last_name,
                                       @Query("account_no") String account_no,
                                       @Query("ifsc_code") String ifsc_code,
                                       @Query("upi_id") String upi_id,
                                       @Query("payment_mobile") String payment_mobile,
                                       @Query("lat") String lat,
                                       @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Medical %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupMedical(@Query("username") String username,
                                     @Query("name") String name,
                                     @Query("mobile") String mobile,
                                     @Query("email") String email,
                                     @Query("address") String address,
                                     @Query("user_type") String user_type,
                                     @Query("city") String city,
                                     @Query("distric") String distric,
                                     @Query("department") String department,
                                     @Query("password") String password,
                                     @Query("account_first_name") String account_first_name,
                                     @Query("account_last_name") String account_last_name,
                                     @Query("account_no") String account_no,
                                     @Query("ifsc_code") String ifsc_code,
                                     @Query("upi_id") String upi_id,
                                     @Query("payment_mobile") String payment_mobile,
                                     @Query("register_id") String register_id,
                                     @Query("lat") String lat,
                                     @Query("lng") String lng,
                                     @Part MultipartBody.Part file,
                                     @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Medical with Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateMedicalImage(@Query("user_id") String user_id,
                                          @Query("username") String username,
                                          @Query("name") String name,
                                          @Query("mobile") String mobile,
                                          @Query("email") String email,
                                          @Query("address") String address,
                                          @Query("user_type") String user_type,
                                          @Query("city") String city,
                                          @Query("distric") String distric,
                                          @Query("department") String department,
                                          @Query("password") String password,
                                          @Query("account_first_name") String account_first_name,
                                          @Query("account_last_name") String account_last_name,
                                          @Query("account_no") String account_no,
                                          @Query("ifsc_code") String ifsc_code,
                                          @Query("upi_id") String upi_id,
                                          @Query("payment_mobile") String payment_mobile,
                                          @Query("lat") String lat,
                                          @Query("lng") String lng,
                                          @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Medical without Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> updateMedical(@Query("user_id") String user_id,
                                     @Query("username") String username,
                                     @Query("name") String name,
                                     @Query("mobile") String mobile,
                                     @Query("email") String email,
                                     @Query("address") String address,
                                     @Query("user_type") String user_type,
                                     @Query("city") String city,
                                     @Query("distric") String distric,
                                     @Query("department") String department,
                                     @Query("password") String password,
                                     @Query("account_first_name") String account_first_name,
                                     @Query("account_last_name") String account_last_name,
                                     @Query("account_no") String account_no,
                                     @Query("ifsc_code") String ifsc_code,
                                     @Query("upi_id") String upi_id,
                                     @Query("payment_mobile") String payment_mobile,
                                     @Query("lat") String lat,
                                     @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp RMP %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupRMP(@Query("username") String username,
                                 @Query("name") String name,
                                 @Query("mobile") String mobile,
                                 @Query("email") String email,
                                 @Query("address") String address,
                                 @Query("user_type") String user_type,
                                 @Query("city") String city,
                                 @Query("distric") String distric,
                                 @Query("department") String department,
                                 @Query("password") String password,
                                 @Query("register_id") String register_id,
                                 @Query("lat") String lat,
                                 @Query("lng") String lng,
                                 @Part MultipartBody.Part file,
                                 @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update RMP %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateRMP(@Query("user_id") String user_id,
                                 @Query("username") String username,
                                 @Query("name") String name,
                                 @Query("mobile") String mobile,
                                 @Query("email") String email,
                                 @Query("address") String address,
                                 @Query("user_type") String user_type,
                                 @Query("city") String city,
                                 @Query("distric") String distric,
                                 @Query("department") String department,
                                 @Query("password") String password,
                                 @Query("account_first_name") String account_first_name,
                                 @Query("account_last_name") String account_last_name,
                                 @Query("account_no") String account_no,
                                 @Query("ifsc_code") String ifsc_code,
                                 @Query("upi_id") String upi_id,
                                 @Query("payment_mobile") String payment_mobile,
                                 @Query("lat") String lat,
                                 @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update RMP Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateRMPImage(@Query("user_id") String user_id,
                                      @Query("username") String username,
                                      @Query("name") String name,
                                      @Query("mobile") String mobile,
                                      @Query("email") String email,
                                      @Query("address") String address,
                                      @Query("user_type") String user_type,
                                      @Query("city") String city,
                                      @Query("distric") String distric,
                                      @Query("department") String department,
                                      @Query("password") String password,
                                      @Query("account_first_name") String account_first_name,
                                      @Query("account_last_name") String account_last_name,
                                      @Query("account_no") String account_no,
                                      @Query("ifsc_code") String ifsc_code,
                                      @Query("upi_id") String upi_id,
                                      @Query("payment_mobile") String payment_mobile,
                                      @Query("lat") String lat,
                                      @Query("lng") String lng,
                                      @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Vaterinary %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupVaterinary(@Query("username") String username,
                                        @Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("email") String email,
                                        @Query("address") String address,
                                        @Query("user_type") String user_type,
                                        @Query("city") String city,
                                        @Query("distric") String distric,
                                        @Query("department") String department,
                                        @Query("password") String password,
                                        @Query("register_id") String register_id,
                                        @Query("lat") String lat,
                                        @Query("lng") String lng,
                                        @Part MultipartBody.Part file,
                                        @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Vaterinary %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateVaterinary(@Query("user_id") String user_id,
                                        @Query("username") String username,
                                        @Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("email") String email,
                                        @Query("address") String address,
                                        @Query("user_type") String user_type,
                                        @Query("city") String city,
                                        @Query("distric") String distric,
                                        @Query("department") String department,
                                        @Query("password") String password,
                                        @Query("lat") String lat,
                                        @Query("lng") String lng);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Vaterinary Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateVaterinaryImage(@Query("user_id") String user_id,
                                             @Query("username") String username,
                                             @Query("name") String name,
                                             @Query("mobile") String mobile,
                                             @Query("email") String email,
                                             @Query("address") String address,
                                             @Query("user_type") String user_type,
                                             @Query("city") String city,
                                             @Query("distric") String distric,
                                             @Query("department") String department,
                                             @Query("password") String password,
                                             @Query("lat") String lat,
                                             @Query("lng") String lng,
                                             @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Lab %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupLab(@Query("username") String username,
                                 @Query("name") String name,
                                 @Query("mobile") String mobile,
                                 @Query("email") String email,
                                 @Query("address") String address,
                                 @Query("user_type") String user_type,
                                 @Query("city") String city,
                                 @Query("distric") String distric,
                                 @Query("department") String department,
                                 @Query("password") String password,
                                 @Query("register_id") String register_id,
                                 @Query("account_first_name") String account_first_name,
                                 @Query("account_last_name") String account_last_name,
                                 @Query("account_no") String account_no,
                                 @Query("ifsc_code") String ifsc_code,
                                 @Query("upi_id") String upi_id,
                                 @Query("payment_mobile") String payment_mobile,
                                 @Query("lat") String lat,
                                 @Query("lng") String lng,
                                 @Part MultipartBody.Part file,
                                 @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Report %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("add_lab_report")
    Call<ResponseBody> addPatientreport(@Query("lab_id") String lab_id,
                                        @Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("date") String date,
                                        @Query("hospital_name") String hospital_name,
                                        @Query("description") String description,
                                        @Query("file_type") String typ,
                                        @Part List<MultipartBody.Part> reports_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Report %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("add_lab_report_test")
    Call<ResponseBody> addPatientreport2(@Query("lab_id") String lab_id,
                                         @Query("name") String name,
                                         @Query("mobile") String mobile,
                                         @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Search report %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("get_lab_report")
    Call<ResponseBody> getPatientreport(@Query("name") String name,
                                        @Query("mobile") String mobile);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Lab without Image %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> updateLab(@Query("user_id") String user_id,
                                 @Query("username") String username,
                                 @Query("name") String name,
                                 @Query("mobile") String mobile,
                                 @Query("email") String email,
                                 @Query("address") String address,
                                 @Query("user_type") String user_type,
                                 @Query("city") String city,
                                 @Query("distric") String distric,
                                 @Query("department") String department,
                                 @Query("password") String password);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Catagories %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("get_category")
    Call<ResponseBody> getCatagories();

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Doctors %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("get_doctor")
    Call<ResponseBody> getDoctors(@Query("category_id") String category_id,
                                  @Query("expert") String expert,
                                  @Query("lat") String lat,
                                  @Query("long") String longitude);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% All Doctors %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("get_doctor")
    Call<ResponseBody> getAllDoctors(@Query("expert") String expert,
                                     @Query("lat") String lat,
                                     @Query("long") String longitude);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% AllDoc %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("get_doctor")
    Call<ResponseBody> getAllDoc(
            @Query("lat") String lat,
            @Query("long") String longitude);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% CatDoc %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("get_doctor")
    Call<ResponseBody> getCatDoc(@Query("category_id") String category_id,
                                 @Query("lat") String lat,
                                 @Query("long") String longitude);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Expert Doctors %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("get_doctor")
    Call<ResponseBody> getExpertDoctors(@Query("expert") String expert,
                                        @Query("lat") String lat,
                                        @Query("long") String longitude);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get shift %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_doctor_shift")
    Call<ResponseBody> getDoctorshift(@Query("doctor_id") String doctor_id,
                                      @Query("date") String date);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get token %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_token")
    Call<ResponseBody> getToken(@Query("doctor_id") String doctor_shift_id,
                                @Query("user_id") String user_id,
                                @Query("date") String date);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get order id %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("generate_order_id")
    Call<ResponseBody> getOrderID(@Query("amount") int amount);



    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Book Appointment %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("book_appointment")
    Call<ResponseBody> book(@Query("user_id") String user_id,
                            @Query("doctor_id") String doctor_id,
                            @Query("doctor_shift_id") String doctor_shift_id,
                            @Query("category_id") String category_id,
                            @Query("date") String date,
                            @Query("token") String token,
                            @Query("mobile") String mobile,
                            @Query("name") String name,
                            @Query("email") String email,
                            @Query("sex") String sex,
                            @Query("time") String time,
                            @Query("age") String age,
                            @Query("aadhar_no") String aadhar_no,
                            @Query("referby") String referby,
                            @Query("refer_name") String refer_name,
                            @Query("refer_num") String refer_num,
                            @Query("covid19") String covid19,
                            @Query("txn_amount") String txn_amount,
                            @Query("wallet_amt") String wallet_amt,
                            @Query("txn_id") String txn_id,
                            @Query("service_charge") String service_charge,
                            @Query("status") String status,
                            @Query("remark") String remark);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Banner %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_banner")
    Call<ResponseBody> getBanner();

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Banner %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("update_register_id")
    Call<ResponseBody> updateRegisteId(@Query("device_id") String Device_id,
                                       @Query("register_id") String register_id,
                                       @Query("city") String city,
                                       @Query("lat") String lat,
                                       @Query("lng") String lon);

//// %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Banner %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
//
//    @GET("update_register_id")
//    Call<ResponseBody> updateRegisteId(@Query("register_id") String register_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Appointments %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_appointment")
    Call<ResponseBody> getAppointments(@Query("user_id") String user_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Patient %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> addPatient(@Query("parent_id") String user_id,
                                  @Query("name") String name,
                                  @Query("mobile") String mobile,
                                  @Query("medical_history") String purpose,
                                  @Query("fees") String fees,
                                  @Query("service_charge") String arialfees,
                                  @Query("user_type") String user_type);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Get Patients %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_users")
    Call<ResponseBody> getPatients(@Query("parent_id") String parent_id,
                                   @Query("user_type") String user_type);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Meeting %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Multipart
    @POST("add_meeting")
    Call<ResponseBody> addMeeting(@Query("doctor_id") String doctor_id,
                                  @Query("user_id") String user_id,
                                  @Query("name") String name,
                                  @Query("date") String date,
                                  @Query("company_name") String company_name,
                                  @Query("mobile") String mobile,
                                  @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Rating Review %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("rating")
    Call<ResponseBody> addRating(@Query("user_id") String user_id,
                                 @Query("doctor_id") String doctor_id,
                                 @Query("appointment_id") String appointment_id,
                                 @Query("rating") String rating,
                                 @Query("review") String review);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Reviews %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_review")
    Call<ResponseBody> getReviews(@Query("doctor_id") String doctor_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Notifications %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_notification")
    Call<ResponseBody> getNotifications(@Query("user_id") String user_id,
                                        @Query("city") String city,
                                        @Query("lat") String lat,
                                        @Query("lng") String lon);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Notifications %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("get_notification_data")
    Call<ResponseBody> getNotification(@Query("notification_id") String notification_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Send Notifications %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("notification_to_all")
    Call<ResponseBody> sendNotification(@Query("send_by") String notification_id,
                                        @Query("title") String title,
                                        @Query("message") String message,
                                        @Query("city") String city,
                                        @Query("lat") String lat,
                                        @Query("lng") String lon);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Send with image Notifications %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Multipart
    @POST("notification_to_all")
    Call<ResponseBody> sendNotificationImage(@Query("send_by") String notification_id,
                                             @Query("title") String title,
                                             @Query("message") String message,
                                             @Query("city") String city,
                                             @Query("lat") String lat,
                                             @Query("lng") String lon,
                                             @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Video %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @Multipart
    @POST("signup")
    Call<ResponseBody> addvideo(@Query("user_id") String user_id,
                                @Part MultipartBody.Part file,
                                @Query("video_title") String video_title);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% get Video %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @GET("get_users")
    Call<ResponseBody> getVideo(@Query("has_video") String has_video);

//%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Account Detail %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> addAccountDetail(@Query("user_id") String user_id,
                                        @Query("account_first_name") String account_first_name,
                                        @Query("account_last_name") String account_last_name,
                                        @Query("account_no") String account_no,
                                        @Query("ifsc_code") String ifsc_code,
                                        @Query("upi_id") String upi_id,
                                        @Query("payment_mobile") String payment_mobile);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Account Detail %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> addAccountDetailFirstTime(@Query("user_id") String user_id,
                                                 @Query("account_first_name") String account_first_name,
                                                 @Query("account_last_name") String account_last_name,
                                                 @Query("account_no") String account_no,
                                                 @Query("ifsc_code") String ifsc_code,
                                                 @Query("upi_id") String upi_id,
                                                 @Query("payment_mobile") String payment_mobile,
                                                 @Query("donated") String donated,
                                                 @Query("activation_date") String activation_date,
                                                 @Query("expiry_date") String expiry_date);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Account Detail %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> updateHealthIDCard(@Query("user_id") String user_id,
                                          @Query("donated") String donated,
                                          @Query("activation_date") String activation_date,
                                          @Query("expiry_date") String expiry_date);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Add Withdrawal Request %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("withdrawal_request")
    Call<ResponseBody> addWithdrawalRequest(@Query("user_id") String user_id,
                                            @Query("amount") String amount,
                                            @Query("date") String date,
                                            @Query("remark") String remark);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Oxygen Bank %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupOxygenBank(@Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("email") String email,
                                        @Query("address") String address,
                                        @Query("about") String about,
                                        @Query("product_name") String product_name,
                                        @Query("product_price") String product_price,
                                        @Query("user_type") String user_type,
                                        @Query("password") String password,
                                        @Query("register_id") String register_id,
                                        @Query("city") String city,
                                        @Query("lat") String lat,
                                        @Query("lng") String lon,
                                        @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Oxygen Bank with Image%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateOxygenBankWithImage(@Query("user_id") String user_id,
                                                 @Query("name") String name,
                                                 @Query("mobile") String mobile,
                                                 @Query("email") String email,
                                                 @Query("address") String address,
                                                 @Query("about") String about,
                                                 @Query("product_name") String product_name,
                                                 @Query("product_price") String product_price,
                                                 @Query("user_type") String user_type,
                                                 @Query("password") String password,
                                                 @Query("city") String city,
                                                 @Query("lat") String lat,
                                                 @Query("lng") String lon,
                                                 @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Oxygen Bank %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateOxygenBank(@Query("user_id") String user_id,
                                        @Query("name") String name,
                                        @Query("mobile") String mobile,
                                        @Query("email") String email,
                                        @Query("address") String address,
                                        @Query("about") String about,
                                        @Query("product_name") String product_name,
                                        @Query("product_price") String product_price,
                                        @Query("user_type") String user_type,
                                        @Query("password") String password,
                                        @Query("city") String city,
                                        @Query("lat") String lat,
                                        @Query("lng") String lon);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp HelpingHand Donor%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupHelpingSoldier(@Query("name") String name,
                                            @Query("mobile") String mobile,
                                            @Query("email") String email,
                                            @Query("address") String address,
                                            @Query("about") String about,
                                            @Query("user_type") String user_type,
                                            @Query("village") String village,
                                            @Query("city") String city,
                                            @Query("distric") String distric,
                                            @Query("username") String username,
                                            @Query("password") String password,
                                            @Query("register_id") String register_id,
                                            @Query("lat") String lat,
                                            @Query("lng") String lng,
                                            @Part MultipartBody.Part file,
                                            @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Helping with Image%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> updateHelpingSoldierwithImage(@Query("user_id") String user_id,
                                                     @Query("name") String name,
                                                     @Query("mobile") String mobile,
                                                     @Query("email") String email,
                                                     @Query("address") String address,
                                                     @Query("about") String about,
                                                     @Query("user_type") String user_type,
                                                     @Query("village") String village,
                                                     @Query("city") String city,
                                                     @Query("distric") String distric,
                                                     @Query("username") String username,
                                                     @Query("password") String password,
                                                     @Part MultipartBody.Part file);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Helping Donor %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @POST("signup")
    Call<ResponseBody> updateHelpingSoldier(@Query("user_id") String user_id,
                                            @Query("name") String name,
                                            @Query("mobile") String mobile,
                                            @Query("email") String email,
                                            @Query("address") String address,
                                            @Query("about") String about,
                                            @Query("user_type") String user_type,
                                            @Query("village") String village,
                                            @Query("city") String city,
                                            @Query("distric") String distric,
                                            @Query("username") String username,
                                            @Query("password") String password);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% SignUp Vehicle%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%
    @Multipart
    @POST("signup")
    Call<ResponseBody> signupVehicle(@Query("name") String name,
                                     @Query("mobile") String mobile,
                                     @Query("email") String email,
                                     @Query("address") String address,
                                     @Query("aadhar") String aadhar,
                                     @Query("vehicle_no") String vehicle_no,
                                     @Query("user_type") String user_type,
                                     @Query("village") String village,
                                     @Query("city") String city,
                                     @Query("distric") String distric,
                                     @Query("department") String department,
                                     @Query("username") String username,
                                     @Query("password") String password,
                                     @Query("register_id") String register_id,
                                     @Query("account_first_name") String account_first_name,
                                     @Query("account_last_name") String account_last_name,
                                     @Query("account_no") String account_no,
                                     @Query("ifsc_code") String ifsc_code,
                                     @Query("upi_id") String upi_id,
                                     @Query("payment_mobile") String payment_mobile,
                                     @Query("lat") String lat,
                                     @Query("lng") String lng,
                                     @Part MultipartBody.Part file,
                                     @Part List<MultipartBody.Part> service_images);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Make Payment %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("make_payment")
    Call<ResponseBody> makePayment(@Query("user_id") String user_id,
                                   @Query("vendor_id") String vendor_id,
                                   @Query("txn_amunt") String txn_amunt,
                                   @Query("txn_id") String txn_id);

    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%% Update Wallet %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @POST("signup")
    Call<ResponseBody> updateWallet(@Query("user_id") String user_id,
                                    @Query("wallet") String wallet);


    //%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%  Wallet txn %%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%%

    @GET("wallet_txn")
    Call<ResponseBody> walletTxn(@Query("user_id") String user_id);

    @POST("resend_otp")
    Call<ResponseBody> resend_otp(
           // @Query("user_type") String user_type,
            @Query("mobile") String mobile
    );

    @POST("verifyotp")
    Call<ResponseBody> verifyotp(
            // @Query("user_type") String user_type,
            @Query("mobile") String mobile,
            @Query("otp") String otp
    );


    @FormUrlEncoded
    @POST("create_order_id")
    Call<ResponseBody> createOrderID(
            @Header("Cookie") String sessionIdAndToken,
            @Field("user_id") String user_id,
            @Field("amount") int amount
    );

    @FormUrlEncoded
    @POST("add_wallet_amount")
    Call<ResponseBody> addWalletAmount(
            @Header("Cookie") String sessionIdAndToken,
            @Field("user_id") String user_id,
            @Field("amount") String amount,
            @Field("order_id") String order_id
    );
}
