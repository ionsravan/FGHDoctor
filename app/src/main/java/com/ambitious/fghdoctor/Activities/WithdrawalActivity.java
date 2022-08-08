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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class WithdrawalActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Play;
    private TextView tv_Pay, tv_Submit;
    private RelativeLayout rl_Viedo, rl_Loader;
    private LinearLayout ll_Membership;
    private EditText et_Fname, et_Lname, et_Accno, et_IFSC, et_UPI, et_Number;
    private String name = "", email = "", mobile = "", donated = "", wallet = "", txn_id = "", account_first_name = "", account_last_name = "", account_no = "", ifsc_code = "", upi_id = "", payment_mobile = "", activation_date = "", expiry_date = "";
    private boolean is_paid = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_withdrawal);
        finds();
        if (getIntent().getExtras() != null) {

            donated = getIntent().getStringExtra("donated");
            wallet = getIntent().getStringExtra("wallet");
            account_first_name = getIntent().getStringExtra("account_first_name");
            account_last_name = getIntent().getStringExtra("account_last_name");
            account_no = getIntent().getStringExtra("account_no");
            ifsc_code = getIntent().getStringExtra("ifsc_code");
            upi_id = getIntent().getStringExtra("upi_id");
            payment_mobile = getIntent().getStringExtra("payment_mobile");
            activation_date = getIntent().getStringExtra("activation_date");
            expiry_date = getIntent().getStringExtra("expiry_date");

            if (donated.equalsIgnoreCase("0")) {
                rl_Viedo.setVisibility(View.VISIBLE);
                ll_Membership.setVisibility(View.VISIBLE);
                tv_Submit.setVisibility(View.GONE);
            } else {
                tv_Submit.setVisibility(View.VISIBLE);
                rl_Viedo.setVisibility(View.GONE);
                ll_Membership.setVisibility(View.GONE);
            }

            et_Fname.setText(account_first_name);
            et_Lname.setText(account_last_name);
            et_Accno.setText(account_no);
            et_IFSC.setText(ifsc_code);
            et_UPI.setText(upi_id);
            et_Number.setText(payment_mobile);

        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Play = findViewById(R.id.iv_Play);
        tv_Pay = findViewById(R.id.tv_Pay);
        tv_Submit = findViewById(R.id.tv_Submit);
        et_Fname = findViewById(R.id.et_Fname);
        et_Lname = findViewById(R.id.et_Lname);
        et_Accno = findViewById(R.id.et_Accno);
        et_IFSC = findViewById(R.id.et_IFSC);
        et_UPI = findViewById(R.id.et_UPI);
        et_Number = findViewById(R.id.et_Number);
        rl_Viedo = findViewById(R.id.rl_Viedo);
        rl_Loader = findViewById(R.id.rl_Loader);
        ll_Membership = findViewById(R.id.ll_Membership);

        iv_Bck.setOnClickListener(this);
        iv_Play.setOnClickListener(this);
        tv_Pay.setOnClickListener(this);
        tv_Submit.setOnClickListener(this);

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
                validate(v);
                break;

            case R.id.tv_Submit:
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    String fname = et_Fname.getText().toString();
                    String lname = et_Lname.getText().toString();
                    String accno = et_Accno.getText().toString();
                    String ifsc = et_IFSC.getText().toString();
                    String upiid = et_UPI.getText().toString();
                    String googlepay = et_Number.getText().toString();
                    if (fname.equalsIgnoreCase("")) {
                        et_Fname.setError("Can't be empty!");
                        et_Fname.requestFocus();
                    } else if (lname.equalsIgnoreCase("")) {
                        et_Lname.setError("Can't be empty!");
                        et_Lname.requestFocus();
                    } else if (!accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("")) {
                        et_IFSC.setError("Can't be empty!");
                        et_IFSC.requestFocus();
                    } else if (accno.equalsIgnoreCase("") && !ifsc.equalsIgnoreCase("")) {
                        et_Accno.setError("Can't be empty!");
                        et_Accno.requestFocus();
                    } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && !upiid.equalsIgnoreCase("") && googlepay.equalsIgnoreCase("")) {
                        et_Number.setError("Can't be empty!");
                        et_Number.requestFocus();
                    } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && upiid.equalsIgnoreCase("") && !googlepay.equalsIgnoreCase("")) {
                        et_UPI.setError("Can't be empty!");
                        et_UPI.requestFocus();
                    } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && upiid.equalsIgnoreCase("") && googlepay.equalsIgnoreCase("")) {
                        CustomSnakbar.showDarkSnakabar(mContext, v, "Please Enter Account Number with IFSC Code \nOR\n UPI ID with Mobile Number.");
                    } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && !upiid.equalsIgnoreCase("") && !googlepay.equalsIgnoreCase("")) {
                        Log.e("Request=>", "Fname=>" + fname + "\n" + "Lname=>" + lname + "\n" + "accnum=>" + accno + "\n" + "IFSC=>" + ifsc + "\n" + "UPIID=>" + upiid + "\n" + "Number=>" + googlepay);

                        long date = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String dt = sdf.format(date);
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sdf.parse(dt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 365);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String output = sdf1.format(c.getTime());
                        Log.e("Date1=>", "" + dt);
                        Log.e("Date2=>", "" + output);
                        addPaymentDetail(uid, fname, lname, accno, ifsc, upiid, googlepay, "1", dt, output, v);
                    } else {
                        Log.e("Request=>", "Fname=>" + fname + "\n" + "Lname=>" + lname + "\n" + "accnum=>" + accno + "\n" + "IFSC=>" + ifsc + "\n" + "UPIID=>" + upiid + "\n" + "Number=>" + googlepay);
                        long date = System.currentTimeMillis();
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                        String dt = sdf.format(date);
                        Calendar c = Calendar.getInstance();
                        try {
                            c.setTime(sdf.parse(dt));
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        c.add(Calendar.DATE, 365);  // number of days to add, can also use Calendar.DAY_OF_MONTH in place of Calendar.DATE
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String output = sdf1.format(c.getTime());
                        Log.e("Date1=>", "" + dt);
                        Log.e("Date2=>", "" + output);
                        addPaymentDetail(uid, fname, lname, accno, ifsc, upiid, googlepay, "1", dt, output, v);
                    }
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

        }

    }

    private void addPaymentDetail(String uid, String fname, String lname, String accno, String ifsc, String upiid, String googlepay, String donated, String date1, String date2, View view) {
        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        if (activation_date.equalsIgnoreCase("") && expiry_date.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().addAccountDetailFirstTime(uid, fname, lname, accno, ifsc, upiid, googlepay, donated, date1, date2);
        } else {
            call = AppConfig.loadInterface().addAccountDetail(uid, fname, lname, accno, ifsc, upiid, googlepay);
        }
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
                        System.out.println("AccountDetail=>" + object);

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

                            long date = System.currentTimeMillis();

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                            String dateString = sdf.format(date);
                            addWithdrawalRequest(uid, wallet, dateString, "", view);

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

    private void addWithdrawalRequest(String uid, String amount, String date, String remark, View view) {

        Call<ResponseBody> call = AppConfig.loadInterface().addWithdrawalRequest(uid, amount, date, remark);
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
                        System.out.println("WithdrawalRequest=>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showSnakabar(mContext, view, "Withdrawal Requested Successfully.");
                            JSONObject result = object.optJSONObject("result");

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

    private void validate(View v) {

        String fname = et_Fname.getText().toString();
        String lname = et_Lname.getText().toString();
        String accno = et_Accno.getText().toString();
        String ifsc = et_IFSC.getText().toString();
        String upiid = et_UPI.getText().toString();
        String googlepay = et_Number.getText().toString();

        if (fname.equalsIgnoreCase("")) {
            et_Fname.setError("Can't be empty!");
            et_Fname.requestFocus();
        } else if (lname.equalsIgnoreCase("")) {
            et_Lname.setError("Can't be empty!");
            et_Lname.requestFocus();
        } else if (!accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("")) {
            et_IFSC.setError("Can't be empty!");
            et_IFSC.requestFocus();
        } else if (accno.equalsIgnoreCase("") && !ifsc.equalsIgnoreCase("")) {
            et_Accno.setError("Can't be empty!");
            et_Accno.requestFocus();
        } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && !upiid.equalsIgnoreCase("") && googlepay.equalsIgnoreCase("")) {
            et_Number.setError("Can't be empty!");
            et_Number.requestFocus();
        } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && upiid.equalsIgnoreCase("") && !googlepay.equalsIgnoreCase("")) {
            et_UPI.setError("Can't be empty!");
            et_UPI.requestFocus();
        } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && upiid.equalsIgnoreCase("") && googlepay.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Enter Account Number with IFSC Code \nOR\n UPI ID with Mobile Number.");
        } else if (accno.equalsIgnoreCase("") && ifsc.equalsIgnoreCase("") && !upiid.equalsIgnoreCase("") && !googlepay.equalsIgnoreCase("")) {
            Log.e("Request=>", "Fname=>" + fname + "\n" + "Lname=>" + lname + "\n" + "accnum=>" + accno + "\n" + "IFSC=>" + ifsc + "\n" + "UPIID=>" + upiid + "\n" + "Number=>" + googlepay);
            startPayment(fname + " " + lname, "20", email, googlepay);
        } else {
            Log.e("Request=>", "Fname=>" + fname + "\n" + "Lname=>" + lname + "\n" + "accnum=>" + accno + "\n" + "IFSC=>" + ifsc + "\n" + "UPIID=>" + upiid + "\n" + "Number=>" + googlepay);
            startPayment(fname + " " + lname, "20", email, googlepay);
        }

    }

    public void startPayment(String name, String amount, String email, String mobile) {
        try {

            final Activity activity = this;
            final Checkout co = new Checkout();

            try {
                JSONObject options = new JSONObject();
                options.put("name", name);
                options.put("description", "FGH Membership Charge");
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
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
        tv_Submit.performClick();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("onPaymentError", i + "----->" + s);
        txn_id = "";
        is_paid = false;
//        if (cdialog != null) {
//            cdialog.dismiss();
//        }
        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment Failed\nGet Membership Failed.");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}