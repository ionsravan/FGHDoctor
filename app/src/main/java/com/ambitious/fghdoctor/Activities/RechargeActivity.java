package com.ambitious.fghdoctor.Activities;

import static com.ambitious.fghdoctor.Utils.AppConfig.amountOfPercentage;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.bottomsheet.BottomSheetDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RechargeActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "RechargeActivity";
    private ArrayList<String> operatorList = new ArrayList<>();
    private ArrayList<String> statesList = new ArrayList<>();
    private Context mContext = this;
    private ImageView iv_Bck, iv_Driver, iv_One, iv_Two, iv_Three;
    private TextView tv_Name, tv_Rating, tv_Address, tv_Desc, tv_Pay;
    private RatingBar bar_Rating;
    private CheckBox chk_Wallet;
    private EditText et_Amount, et_Pname, et_Email, et_Pnum,etOperator,etState;
    private RelativeLayout rl_Loader;
    private RelativeLayout rl_Call, rl_Whatsapp;

    private String contact = "", wallet = "", user_id = "", n_Wallet = "", Amnt = "", donated = "", from = "";
    private ArrayList<String> imagesStringsProfile;
    private ArrayList<String> imagesStrings;
    private int percentage = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge);

        // operator List
        operatorList.add("Airtel");
        operatorList.add("BSNL");
        operatorList.add("JIO");
        operatorList.add("Vi");

        // statesList
        statesList.add("AP and Telangana");
        statesList.add("Assam");
        statesList.add("Bihar");
        statesList.add("Gujarat");
        statesList.add("Haryana");
        statesList.add("Himachal Pradesh");
        statesList.add("Jammu and Kashmir");
        statesList.add("Karnataka");
        statesList.add("Kerala");
        statesList.add("Kolkata");
        statesList.add("Madhya Pradesh");
        statesList.add("Maharashtra");
        statesList.add("North East");
        statesList.add("Orissa");
        statesList.add("Punjab");
        statesList.add("Rajasthan");
        statesList.add("Tamil Nadu");
        statesList.add("Uttar Pradesh (East)");
        statesList.add("Uttar Pradesh (West)");
        statesList.add("West Bengal");

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
                from = getIntent().getStringExtra("FROM");

                if (wallet.equalsIgnoreCase("0") || wallet.equalsIgnoreCase("")) {
                    wallet = "0";
                    // chk_Wallet.setVisibility(View.GONE);
                } else if (donated.equalsIgnoreCase("0")) {
                    wallet = "0";
                    chk_Wallet.setVisibility(View.GONE);
                } else if (donated.equalsIgnoreCase("1") && wallet.equalsIgnoreCase("")) {
                    wallet = "0";
                    chk_Wallet.setVisibility(View.GONE);
                } else if (donated.equalsIgnoreCase("1") && wallet.equalsIgnoreCase("0")) {
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
        etOperator = findViewById(R.id.etOperator);
        etState = findViewById(R.id.etStates);


        etOperator.setOnClickListener(this);
        etState.setOnClickListener(this);
        iv_Bck.setOnClickListener(this);
        tv_Pay.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.etOperator:
                showOperatorDialog();
                break;

            case R.id.etStates:
                showStatesDialog();
                break;


            case R.id.tv_Pay:

                hide(RechargeActivity.this);

                String fee = et_Amount.getText().toString();
                String name = et_Pname.getText().toString();
                String email = et_Email.getText().toString();
                String number = et_Pnum.getText().toString();
                String operator = etOperator.getText().toString();
                String state = etState.getText().toString();
               /* if (name.equalsIgnoreCase("")) {
                    et_Pname.setError("Can't be empty!");
                    et_Pname.requestFocus();
                } else if (email.equalsIgnoreCase("")) {
                    et_Email.setError("Can't be empty!");
                    et_Email.requestFocus();
                }*/
                if (operator.equalsIgnoreCase("")) {
                    etOperator.setError("Can't be empty!");
                    etOperator.requestFocus();
                } else if (state.equalsIgnoreCase("")) {
                    etState.setError("Can't be empty!");
                    etState.requestFocus();
                }
                else if (number.equalsIgnoreCase("")) {
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
                            int finalAmount = Integer.parseInt(fee) + percentage;
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
                        Amnt = "" + (Integer.parseInt(fee));
                        n_Wallet = "";
                        // getOrderId(v, name, Amnt, email, number);
                        CustomSnakbar.showDarkSnakabar(mContext, v, "Please use Wallet Amount");

//                        startPayment(name, Amnt, email, number);
                    }
//                    Toast.makeText(mContext, "Amnt=>" + Amnt + "\nWallet=>" + n_Wallet, Toast.LENGTH_SHORT).show();
                }

                break;

        }

    }

    private void confirmationDialog(String uid, String user_id, String amnt, String n_Wallet, String wallet, View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(RechargeActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customview, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtPrice = dialogView.findViewById(R.id.txtPrice);
        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

        txtTitle.setText(et_Pname.getText().toString());
        txtPrice.setText("\u20b9 " + Amnt);

        btnContinue.setOnClickListener(v1 -> {
            alertDialog.dismiss();
            hide(RechargeActivity.this);
            //  makePayment(uid, user_id, Amnt, n_Wallet, "wallet", v);

            // pay to vendor
            int vendorAmount = Integer.parseInt(Amnt) - percentage;

            Log.d("TAG", "confirmationDialog: " + vendorAmount);
            makePayment(uid, user_id, String.valueOf(vendorAmount), n_Wallet, "wallet", v);


        });

        btnCancel.setOnClickListener(v1 -> {
            hide(RechargeActivity.this);
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


    private void showOperatorDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_list_dialog);

        ListView listView = bottomSheetDialog.findViewById(R.id.list_item);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,operatorList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: "+operatorList.get(position));
                etOperator.setText(operatorList.get(position));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }

    private void showStatesDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_list_dialog);

        ListView listView = bottomSheetDialog.findViewById(R.id.list_item);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,statesList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "onItemClick: "+statesList.get(position));
                etState.setText(statesList.get(position));
                bottomSheetDialog.dismiss();
            }
        });

        bottomSheetDialog.show();
    }
}