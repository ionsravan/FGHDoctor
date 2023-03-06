package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.goodiebag.pinview.Pinview;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private Button btn_Login;
    private ImageView iv_Cancel;
    private TextView txtRegister;
    private EditText et_Email, et_Password;
    private RelativeLayout rl_Loader;
    String otp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        finds();
    }

    private void finds() {

        rl_Loader = findViewById(R.id.rl_Loader);
        btn_Login = findViewById(R.id.btn_Login);
        iv_Cancel = findViewById(R.id.iv_Cancel);
        txtRegister = findViewById(R.id.txtRegister);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);

        btn_Login.setOnClickListener(this);
        iv_Cancel.setOnClickListener(this);
        txtRegister.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.btn_Login:
                if (Utility.isNetworkConnected(mContext)) {
                    Validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.iv_Cancel:
                startActivity(new Intent(mContext, SignupActivity.class));
                Animatoo.animateCard(mContext);
                break;

                case R.id.txtRegister:
                startActivity(new Intent(mContext, SignupActivity.class));
                Animatoo.animateCard(mContext);
                break;
        }

    }

    private void Validate(View v) {

        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String email = et_Email.getText().toString();
        String pass = et_Password.getText().toString();
        // String register_id = FirebaseInstanceId.getInstance().getToken();
        String register_id = Utility.getSharedPreferences(getApplicationContext(),"regId");

        if (email.equalsIgnoreCase("")) {
            et_Email.setError("Can't be Empty");
            et_Email.requestFocus();
        } else if (pass.equalsIgnoreCase("")) {
            et_Password.setError("Can't be Empty");
            et_Password.requestFocus();
        } else {
            requestToLogin(m_androidId, email, pass, register_id, v);
        }

    }

    private void requestToLogin(String did, String email, String pass, String reg_id, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().login(did, email, "user", pass, reg_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                       /* String total_count = object.getString("total_count");
                        Log.d("TAG", "onResponse: "+total_count);*/

                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showSnakabar(mContext, view, "Login Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");
                            String mobile = result.optString("mobile");
                            String code = result.optString("code");
                            otp = result.optString("otp");
                            Log.d("TAG", "onResponse: "+otp);
                            String mobile_verified = result.optString("mobile_verified");

                            if (mobile_verified.equalsIgnoreCase("0")){

                                fullScreenDiloge(otp,user_id,name,user_image,email,mobile,address,user_type,code);

                            }else {

                                Utility.setSharedPreference(mContext, "u_id", user_id);
                                Utility.setSharedPreference(mContext, "u_name", name);
                                Utility.setSharedPreference(mContext, "u_img", user_image);
                                Utility.setSharedPreference(mContext, "u_email", email);
                                Utility.setSharedPreference(mContext, "u_mobile", mobile);
                                Utility.setSharedPreference(mContext, "location", address);
                                Utility.setSharedPreference(mContext, "user_type", user_type);
                                Utility.setSharedPreference(mContext, "code", code);
                                Utility.setSharedPreferenceBoolean(mContext, "islogin", true);


                                new Handler().postDelayed(() -> {

                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();

                                }, 1500);
                            }




                        } else {
                            Log.d("TAG", "onResponse: FGH"+resultmessage);
                            CustomSnakbar.showSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Profile " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void fullScreenDiloge(String otp, String user_id, String name, String user_image, String email, String mobile, String address, String user_type, String code) {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(LoginActivity.this, R.style.fullscreen);
        View view = getLayoutInflater().inflate(R.layout.otpscreen, null);

        TextView txtMessage = view.findViewById(R.id.txtMessage);
        TextView txtResend = view.findViewById(R.id.txtResend);
        TextView txtResendTimer = view.findViewById(R.id.txtResendTimer);
        TextView txtVerify = view.findViewById(R.id.txtVerify);
        Pinview pinview = view.findViewById(R.id.pinview);

        String phoneNumber = mobile.trim();
        String strLast2Di = phoneNumber.length() >= 2 ? phoneNumber.substring(phoneNumber.length() - 2): "";
        txtMessage.setText("Enter the code we sent to the phone number ending in +91xxxxxxxx"+strLast2Di);

        txtResend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resendOTP(mobile,v);
            }
        });

        txtVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("TAG", "onClick: " + pinview.getValue().toString());

                if (pinview.getValue().equalsIgnoreCase("")) {
                    Toast.makeText(LoginActivity.this, "Enter OTP", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!otp.equalsIgnoreCase(pinview.getValue())) {
                    Toast.makeText(LoginActivity.this, "Enter Valid OTP", Toast.LENGTH_SHORT).show();
                    return;
                } else {

                    verifyOTP(mobile,otp,v,user_id,name,user_image,email,address,user_type,code);


                    finish();
                }

            }
        });

        otpTimer(txtResend,txtResendTimer);

        mBuilder.setView(view);
        AlertDialog dialog = mBuilder.create();
        dialog.show();
    }

    private void otpTimer(TextView txtResend, TextView txtResendTimer){
        txtResend.setEnabled(false);
        txtResend.setClickable(false);
        txtResend.setTextColor(Color.LTGRAY);
        txtResendTimer.setVisibility(View.VISIBLE);
        new CountDownTimer(1000 * 120, 1000) {

            public void onTick(long millisUntilFinished) {

                int minutes = (int) ((millisUntilFinished / (1000 * 60)) % 60);
                int seconds = (int) (millisUntilFinished / 1000) % 60;

                txtResendTimer.setText("Resend OTP in "+minutes + ":" + " " + seconds + " " + "left");

            }

            public void onFinish() {

                txtResend.setEnabled(true);
                txtResend.setClickable(true);
                txtResend.setTextColor(Color.BLACK);
                txtResendTimer.setVisibility(View.GONE);


            }

        }.start();
    }


    private void resendOTP( String mobile, View view)
    {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().resend_otp(mobile);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {


                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("UpdateWallet=>" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");
                            String mobile = result.optString("mobile");
                            String code = result.optString("code");
                            otp = result.optString("otp");
                            Log.d("TAG", "onResponse: "+otp);

                            Toast.makeText(LoginActivity.this, "OTP sent Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, ""+resultmessage, Toast.LENGTH_SHORT).show();
                            //  CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view,  e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }


    private void verifyOTP( String mobile,String otp, View view,String user_id, String name, String user_image, String email, String address, String user_type, String code)
    {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().verifyotp(mobile,otp);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("UpdateWallet=>" + object);

                        if (status.equalsIgnoreCase("1")) {

                            Utility.setSharedPreference(mContext, "u_id", user_id);
                            Utility.setSharedPreference(mContext, "u_name", name);
                            Utility.setSharedPreference(mContext, "u_img", user_image);
                            Utility.setSharedPreference(mContext, "u_email", email);
                            Utility.setSharedPreference(mContext, "u_mobile", mobile);
                            Utility.setSharedPreference(mContext, "location", address);
                            Utility.setSharedPreference(mContext, "user_type", user_type);
                            Utility.setSharedPreference(mContext, "code", code);
                            Utility.setSharedPreferenceBoolean(mContext, "islogin", true);


                            Intent intent = new Intent(mContext, HomeActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Animatoo.animateSlideLeft(mContext);
                            startActivity(intent);

                            Toast.makeText(LoginActivity.this, "Login Successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(LoginActivity.this, ""+resultmessage, Toast.LENGTH_SHORT).show();
                            //  CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view,  e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}
