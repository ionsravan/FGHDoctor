package com.ambitious.fghdoctor.Activities;

import static com.ambitious.fghdoctor.Utils.AppConfig.amountOfPercentage;
import static com.ambitious.fghdoctor.Utils.AppConfig.tax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
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
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class WithDrawActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "WithDrawActivity";
    private Context mContext = this;
    private ImageView iv_Bck, iv_Driver, iv_One, iv_Two, iv_Three;
    private TextView tv_Name, tv_Rating, tv_Address, tv_Desc, tv_Pay, txtIfscCode, txtAccountNo;
    private RatingBar bar_Rating;
    private CheckBox chk_Wallet;
    private EditText et_Amount, et_Pname, et_Email, et_Pnum;
    private RelativeLayout rl_Loader;
    private RelativeLayout rl_Call, rl_Whatsapp;
    LinearLayout bankLayout;

    private String contact = "", wallet = "", user_id = "", n_Wallet = "", Amnt = "", donated = "", account_no = "", ifsc_code = "";
    private ArrayList<String> imagesStringsProfile;
    private ArrayList<String> imagesStrings;
    private int percentage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_with_draw);

        finds();

        Log.d(TAG, "onCreate: " + Utility.getSharedPreferencesBoolean(mContext, "islogin", false));

        if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {

            et_Pname.setText(Utility.getSharedPreferences(mContext, "u_name"));
            et_Email.setText(Utility.getSharedPreferences(mContext, "u_email"));
            et_Pnum.setText("+91" + Utility.getSharedPreferences(mContext, "u_mobile"));

            if (getIntent().getExtras() != null) {

                String obj = getIntent().getStringExtra("obj");
                wallet = getIntent().getStringExtra("wallet");
                donated = getIntent().getStringExtra("donated");
                account_no = getIntent().getStringExtra("account_no");
                ifsc_code = getIntent().getStringExtra("ifsc_code");

                if (!ifsc_code.equalsIgnoreCase("") && !account_no.equalsIgnoreCase("")) {
                    txtAccountNo.setText(account_no);
                    txtIfscCode.setText(ifsc_code);
                } else {
                    txtIfscCode.setVisibility(View.GONE);
                    txtAccountNo.setVisibility(View.GONE);
                    Log.d(TAG, "onCreate: " + "no bank details found !");
                }


                if (wallet.equalsIgnoreCase("0") || wallet.equalsIgnoreCase("") || Integer.parseInt(wallet) <= 0) {
                    wallet = "0";
                    // chk_Wallet.setVisibility(View.GONE);
                }
                else if (donated.equalsIgnoreCase("0") || Integer.parseInt(wallet) <= 0) {
                    wallet = "0";
                    chk_Wallet.setVisibility(View.GONE);
                } else if (donated.equalsIgnoreCase("1") && (wallet.equalsIgnoreCase("") || Integer.parseInt(wallet) <= 0)) {
                    wallet = "0";
                    chk_Wallet.setVisibility(View.GONE);
                } else if (donated.equalsIgnoreCase("1") && (wallet.equalsIgnoreCase("0")|| Integer.parseInt(wallet) <= 0)) {
                    wallet = "0";
                    chk_Wallet.setVisibility(View.GONE);
                } else {
                    if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                        chk_Wallet.setVisibility(View.VISIBLE);
                        chk_Wallet.setText("Use Wallet Amount(₹" + wallet + ")");
                    } else {
                        chk_Wallet.setVisibility(View.GONE);
                    }

                }


            }

        } else {
            startActivity(new Intent(mContext, LoginActivity.class));
            finish();
            Animatoo.animateCard(mContext);
        }


    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Pay = findViewById(R.id.tv_Pay);
        chk_Wallet = findViewById(R.id.chk_Wallet);
        et_Amount = findViewById(R.id.et_Amount);
        et_Pname = findViewById(R.id.et_Pname);
        et_Email = findViewById(R.id.et_Email);
        et_Pnum = findViewById(R.id.et_Pnum);
        rl_Loader = findViewById(R.id.rl_Loader);
        txtAccountNo = findViewById(R.id.txtAccountNo);
        txtIfscCode = findViewById(R.id.txtIfscCode);
        bankLayout = findViewById(R.id.bankLayout);

        iv_Bck.setOnClickListener(this);
        tv_Pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;


            case R.id.tv_Pay:

                hide(WithDrawActivity.this);

                if (account_no.equalsIgnoreCase("") || ifsc_code.equalsIgnoreCase("")) {

                    Log.d(TAG, "onClick: " + "empty");

                    bankAlertDialog();

                } else {

                    String fee = et_Amount.getText().toString();
                    String name = et_Pname.getText().toString();
                    String email = et_Email.getText().toString();
                    String number = et_Pnum.getText().toString();
                    if (name.equalsIgnoreCase("")) {
                        et_Pname.setError("Can't be empty!");
                        et_Pname.requestFocus();
                    } else if (email.equalsIgnoreCase("")) {
                        et_Email.setError("Can't be empty!");
                        et_Email.requestFocus();
                    } else if (number.equalsIgnoreCase("")) {
                        et_Pnum.setError("Can't be empty!");
                        et_Pnum.requestFocus();
                    } else if (fee.equalsIgnoreCase("")) {
                        et_Amount.setError("Can't be empty!");
                        et_Amount.requestFocus();
                    } else {
                        Amnt = "";
                        n_Wallet = "";
                        if (chk_Wallet.isChecked()) {
                            if (Integer.parseInt(fee) <= Integer.parseInt(wallet)) {
                                // Amnt = "" + (Integer.parseInt(fee));
                                percentage = ((amountOfPercentage * Integer.parseInt(fee)) / 100);
                                int finalAmount = Integer.parseInt(fee) + percentage+tax;
                                Amnt = String.valueOf(finalAmount);

                                Log.d("TAG", "onClick: " + Amnt);

                                n_Wallet = String.valueOf(Integer.parseInt(wallet) - Integer.parseInt(Amnt));
                                String uid = Utility.getSharedPreferences(mContext, "u_id");

                                confirmationDialog(uid, uid, Amnt, n_Wallet, "wallet", v);

                                //  makePayment(uid, user_id, Amnt, n_Wallet, "wallet", v);
                            } else {
                                Amnt = String.valueOf((Integer.parseInt(fee) - Integer.parseInt(wallet)));
                                n_Wallet = "0";
                                // getOrderId(v, name, Amnt, email, number);
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Your wallet haven't sufficient amount");

//                            startPayment(name, Amnt, email, number);
                            }
                        } else {

                            if (Integer.parseInt(wallet) <= 0) {
                                n_Wallet = "0";
                                // getOrderId(v, name, Amnt, email, number);
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Your wallet haven't sufficient amount");

                            } else {

                                Amnt = "" + (Integer.parseInt(fee));
                                n_Wallet = "";
                                // getOrderId(v, name, Amnt, email, number);
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Please use Wallet Amount");

//                        startPayment(name, Amnt, email, number);
                            }
                        }
//                    Toast.makeText(mContext, "Amnt=>" + Amnt + "\nWallet=>" + n_Wallet, Toast.LENGTH_SHORT).show();
                    }
                }
                break;

        }

    }

    private void bankAlertDialog() {
        new AlertDialog.Builder(this)
                .setTitle("You Don't have Bank Details !")
                .setMessage("Are you sure you want to add bank details click YES ?")

                // Specifying a listener allows you to take an action before dismissing the dialog.
                // The dialog is automatically dismissed when a dialog button is clicked.
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Continue with delete operation
                        hide(WithDrawActivity.this);
                        Intent intent = new Intent(mContext, UserProfileActivity.class);
                        // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        Animatoo.animateSlideLeft(mContext);
                        startActivity(intent);
                        finish();
                    }
                })

                // A null listener allows the button to dismiss the dialog and take no further action.
                .setNegativeButton(android.R.string.no, null)
                .setIcon(R.drawable.logo)
                .show();
    }

    private void confirmationDialog(String uid, String user_id, String amnt, String n_Wallet, String wallet, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WithDrawActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customview, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtPrice = dialogView.findViewById(R.id.txtPrice);
        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

       /* // add 1Rupee to total amount
        Amnt = String.valueOf(Integer.parseInt(Amnt) + 1);*/
        txtTitle.setText(et_Pname.getText().toString());
        txtPrice.setText("\u20b9 " + Amnt);

        btnContinue.setOnClickListener(v1 -> {
            alertDialog.dismiss();
            hide(WithDrawActivity.this);
            //  makePayment(uid, user_id, Amnt, n_Wallet, "wallet", v);

            // pay to vendor
            int vendorAmount = (Integer.parseInt(Amnt) - percentage) - tax;

            Log.d("TAG", "confirmationDialog: " + vendorAmount);
            makePayment(uid, user_id, String.valueOf(vendorAmount), n_Wallet, "wallet", v);


        });

        btnCancel.setOnClickListener(v1 -> {
            hide(WithDrawActivity.this);
            alertDialog.dismiss();
        });

    }


    private void makePayment(String uid, String vendor_id, String amnt, String n_wallet, String txn_id, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().makePayment(uid, vendor_id, amnt, txn_id);
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
                        System.out.println("MakePayment=>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Paid Successfully.");
                            if (!n_wallet.equalsIgnoreCase("")) {
                                updateWallet(uid, n_wallet, view);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(mContext, PaidSuccessActivity.class);
                                        intent.putExtra("title", tv_Name.getText().toString());
                                        intent.putExtra("amount", Amnt);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Animatoo.animateSlideLeft(mContext);
                                        startActivity(intent);
                                        finish();

                                    }
                                }, 1500);
                            }
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
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

    private void updateWallet(String uid, String n_wallet, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().updateWallet(uid, n_wallet);
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
                            Intent intent = new Intent(mContext, PaidSuccessActivity.class);
                            intent.putExtra("title", et_Pname.getText().toString());
                            intent.putExtra("amount", Amnt);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Animatoo.animateSlideLeft(mContext);
                            startActivity(intent);
                            finish();
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
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
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    public static void hide(Activity activity) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0); // hide
    }
}
