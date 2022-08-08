package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
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
import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MyWalletActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Play;
    private TextView tv_Withdraw, tv_Balance, tv_Pay, tv_Membershipmsg;
    private RelativeLayout rl_Loader;
    private String name = "", email = "", mobile = "", donated = "", wallet = "", account_first_name = "", account_last_name = "", account_no = "", ifsc_code = "", upi_id = "", payment_mobile = "", activation_date = "", expiry_date = "", txn_id = "";
    private boolean is_paid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        finds();

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getProfile(uid, iv_Bck);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Play = findViewById(R.id.iv_Play);
        tv_Withdraw = findViewById(R.id.tv_Withdraw);
        tv_Balance = findViewById(R.id.tv_Balance);
        tv_Pay = findViewById(R.id.tv_Pay);
        tv_Membershipmsg = findViewById(R.id.tv_Membershipmsg);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        iv_Play.setOnClickListener(this);
        tv_Withdraw.setOnClickListener(this);
        tv_Pay.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.iv_Play:
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=v1o9djkTm2A&feature=youtu.be")));
                break;

            case R.id.tv_Pay:
                getOrderId(v, name, "151", email, mobile);
//                startPayment(name, "151", email, mobile);
                break;

            case R.id.tv_Withdraw:
                startActivity(new Intent(mContext, WithdrawalActivity.class)
                        .putExtra("name", "" + name)
                        .putExtra("email", "" + email)
                        .putExtra("mobile", "" + mobile)
                        .putExtra("donated", "" + donated)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("account_first_name", "" + account_first_name)
                        .putExtra("account_last_name", "" + account_last_name)
                        .putExtra("account_no", "" + account_no)
                        .putExtra("ifsc_code", "" + ifsc_code)
                        .putExtra("upi_id", "" + upi_id)
                        .putExtra("payment_mobile", "" + payment_mobile)
                        .putExtra("activation_date", "" + activation_date)
                        .putExtra("expiry_date", "" + expiry_date)
                );
                Animatoo.animateCard(mContext);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    private void getProfile(String uid, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getProfile(uid);
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            name = result.optString("name");
                            String user_image = result.optString("user_image");
                            email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            donated = result.optString("donated");
                            wallet = result.optString("wallet");
                            account_first_name = result.optString("account_first_name");
                            account_last_name = result.optString("account_last_name");
                            account_no = result.optString("account_no");
                            ifsc_code = result.optString("ifsc_code");
                            upi_id = result.optString("upi_id");
                            payment_mobile = result.optString("payment_mobile");
                            activation_date = result.optString("activation_date");
                            expiry_date = result.optString("expiry_date");

                            if (wallet.equalsIgnoreCase("")) {
                                tv_Balance.setText("Amount : ₹0");
                                tv_Withdraw.setVisibility(View.GONE);
                            } else if (wallet.equalsIgnoreCase("0")) {
                                tv_Balance.setText("Amount : ₹" + wallet);
                                tv_Withdraw.setVisibility(View.GONE);
                            } else {
                                tv_Balance.setText("Amount : ₹" + wallet);
                                tv_Withdraw.setVisibility(View.GONE);
                            }

                            if (donated.equalsIgnoreCase("1")) {
                                tv_Membershipmsg.setVisibility(View.GONE);
                                tv_Pay.setVisibility(View.GONE);
                            }

                        } else {
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

    private void getOrderId(View view, String name, String amnt, String email, String number) {
        rl_Loader.setVisibility(View.VISIBLE);
        int amount = Integer.parseInt(amnt) * 100;
        Call<ResponseBody> call = AppConfig.loadInterface().getOrderID(amount);
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
                        System.out.println("Genrate Order ID =>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            startPayment(resultmessage, name, amnt, email, number);
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Order ID " + e.getMessage());
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

    public void startPayment(String orderid, String name, String amount, String email, String mobile) {
        try {

            final Activity activity = this;
            final Checkout co = new Checkout();

            try {
                JSONObject options = new JSONObject();
                options.put("name", name);
                options.put("description", "FGH Health ID Card Charge");
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("order_id", orderid);
                options.put("currency", "INR");
                options.put("amount", (Integer.parseInt(amount) * 100));
//                options.put("amount", 100);

                JSONObject preFill = new JSONObject();
                preFill.put("email", email);
                preFill.put("contact", mobile);

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("onPaymentSuccess", "---->" + s);
        txn_id = s;
        is_paid = true;
        String uid = Utility.getSharedPreferences(mContext, "u_id");
        long date = System.currentTimeMillis();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        String dt = sdf.format(date);
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(sdf.parse(dt));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        c.add(Calendar.DATE, 28);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
        String output = sdf1.format(c.getTime());
        Log.e("Date1=>", "" + dt);
        Log.e("Date2=>", "" + output);
        addPaymentDetail(uid, "1", dt, output, tv_Pay);
    }

    private void addPaymentDetail(String uid, String donated, String date1, String date2, TextView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().updateHealthIDCard(uid, donated, date1, date2);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
//                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("HealthIDCardUpdate=>" + object);

                        if (status.equalsIgnoreCase("1")) {
//                            CustomSnakbar.showSnakabar(mContext, view, "Membership Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");

                            CustomSnakbar.showSnakabar(mContext, view, "Health Card Purchased Successfully.");

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1500);

                        } else {
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

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("onPaymentError", i + "----->" + s);
        txn_id = "";
        is_paid = false;
//        if (cdialog != null) {
//            cdialog.dismiss();
//        }
        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment Failed\nGet Health ID Card Failed.");
    }
}