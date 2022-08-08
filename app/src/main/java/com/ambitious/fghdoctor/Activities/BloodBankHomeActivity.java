package com.ambitious.fghdoctor.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BloodBankHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Profile,imgQrCode;
    private TextView tv_Login, tv_Available, tv_Head;
    EditText etFirstName,etLastName,etAccountNumber,etIfscCode,etUpiId,etPaymentMobile;
    private EditText et_name, et_Mobile, et_Email, et_Address, et_Password, et_Repassword, etLatitude,etLongitude,et_Aposprice, et_Anegprice, et_Bposprice, et_Bnegprice, et_ABposprice, et_ABnegprice, et_Oposprice, et_Onegprice;
    private CheckBox chk_Apositive, chk_Anegitive, chk_Bpositive, chk_Bnegitive, chk_ABpositive, chk_ABnegitive, chk_Opositive, chk_Onegitive;
    private Button btn_Login;
    private String path = "";
    private MultipartBody.Part body;
    private boolean atleastone = false;
    private RelativeLayout rl_Loader;
    private LinearLayout ll_Logout;
    private Switch switch_Available;
    private String bgroup = "", prc = "";
    private boolean is_Getting = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_home);
        finds();

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getProfile(uid, iv_Profile);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        switch_Available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                if (isChecked) {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "1", iv_Profile);
                } else {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "0", iv_Profile);
                }
            }
        });

        chk_Apositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_Aposprice.getText().toString().equalsIgnoreCase("")) {
                            chk_Apositive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Aposprice.setError("Can't be Empty!");
                            et_Aposprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Apositive.setChecked(true);
                        }
                    } else {
                        if (!et_Aposprice.getText().toString().equalsIgnoreCase("")) {
                            et_Aposprice.setText("");
                        }
                    }
                }
            }
        });

        chk_Anegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_Anegprice.getText().toString().equalsIgnoreCase("")) {
                            chk_Anegitive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Anegprice.setError("Can't be Empty!");
                            et_Anegprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Anegitive.setChecked(true);
                        }
                    } else {
                        if (!et_Anegprice.getText().toString().equalsIgnoreCase("")) {
                            et_Anegprice.setText("");
                        }
                    }
                }
            }
        });

        chk_Bpositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_Bposprice.getText().toString().equalsIgnoreCase("")) {
                            chk_Bpositive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Bposprice.setError("Can't be Empty!");
                            et_Bposprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Bpositive.setChecked(true);
                        }
                    } else {
                        if (!et_Bposprice.getText().toString().equalsIgnoreCase("")) {
                            et_Bposprice.setText("");
                        }
                    }
                }
            }
        });

        chk_Bnegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_Bnegprice.getText().toString().equalsIgnoreCase("")) {
                            chk_Bnegitive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Bnegprice.setError("Can't be Empty!");
                            et_Bnegprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Bnegitive.setChecked(true);
                        }
                    } else {
                        if (!et_Bnegprice.getText().toString().equalsIgnoreCase("")) {
                            et_Bnegprice.setText("");
                        }
                    }
                }
            }
        });

        chk_ABpositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_ABposprice.getText().toString().equalsIgnoreCase("")) {
                            chk_ABpositive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_ABposprice.setError("Can't be Empty!");
                            et_ABposprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_ABpositive.setChecked(true);
                        }
                    } else {
                        if (!et_ABposprice.getText().toString().equalsIgnoreCase("")) {
                            et_ABposprice.setText("");
                        }
                    }
                }
            }
        });

        chk_ABnegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_ABnegprice.getText().toString().equalsIgnoreCase("")) {
                            chk_ABnegitive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_ABnegprice.setError("Can't be Empty!");
                            et_ABnegprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_ABnegitive.setChecked(true);
                        }
                    } else {
                        if (!et_ABnegprice.getText().toString().equalsIgnoreCase("")) {
                            et_ABnegprice.setText("");
                        }
                    }
                }
            }
        });

        chk_Opositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_Oposprice.getText().toString().equalsIgnoreCase("")) {
                            chk_Opositive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Oposprice.setError("Can't be Empty!");
                            et_Oposprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Opositive.setChecked(true);
                        }
                    } else {
                        if (!et_Oposprice.getText().toString().equalsIgnoreCase("")) {
                            et_Oposprice.setText("");
                        }
                    }
                }
            }
        });

        chk_Onegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (!is_Getting) {
                    if (isChecked) {
                        if (et_Onegprice.getText().toString().equalsIgnoreCase("")) {
                            chk_Onegitive.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Onegprice.setError("Can't be Empty!");
                            et_Onegprice.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Onegitive.setChecked(true);
                        }
                    } else {
                        if (!et_Onegprice.getText().toString().equalsIgnoreCase("")) {
                            et_Onegprice.setText("");
                        }
                    }
                }
            }
        });

        et_Aposprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Aposprice.length() == 0) {
                    chk_Apositive.setChecked(false);
                }
            }
        });

        et_Anegprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Anegprice.length() == 0) {
                    chk_Anegitive.setChecked(false);
                }
            }
        });

        et_Bposprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Bposprice.length() == 0) {
                    chk_Bpositive.setChecked(false);
                }
            }
        });

        et_Bnegprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Bnegprice.length() == 0) {
                    chk_Bnegitive.setChecked(false);
                }
            }
        });

        et_ABposprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_ABposprice.length() == 0) {
                    chk_ABpositive.setChecked(false);
                }
            }
        });

        et_ABnegprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_ABnegprice.length() == 0) {
                    chk_ABnegitive.setChecked(false);
                }
            }
        });

        et_Oposprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Oposprice.length() == 0) {
                    chk_Opositive.setChecked(false);
                }
            }
        });

        et_Onegprice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Onegprice.length() == 0) {
                    chk_Onegitive.setChecked(false);
                }
            }
        });
    }

    private void requesToChangeAvailablity(String uid, String stts, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().cahngeAvailablity(uid, stts);
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
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");
                            String available = result.optString("available");

                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
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

    private void finds() {

        iv_Profile = findViewById(R.id.iv_Profile);
        imgQrCode = findViewById(R.id.imgQrCode);
        tv_Login = findViewById(R.id.tv_Login);
        tv_Available = findViewById(R.id.tv_Available);
        tv_Head = findViewById(R.id.tv_Head);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Email = findViewById(R.id.et_Email);
        et_Address = findViewById(R.id.et_Address);
        et_Password = findViewById(R.id.et_Password);
        et_Repassword = findViewById(R.id.et_Repassword);
        et_Aposprice = findViewById(R.id.et_Aposprice);
        et_Anegprice = findViewById(R.id.et_Anegprice);
        et_Bposprice = findViewById(R.id.et_Bposprice);
        et_Bnegprice = findViewById(R.id.et_Bnegprice);
        et_ABposprice = findViewById(R.id.et_ABposprice);
        et_ABnegprice = findViewById(R.id.et_ABnegprice);
        et_Oposprice = findViewById(R.id.et_Oposprice);
        et_Onegprice = findViewById(R.id.et_Onegprice);
        chk_Apositive = findViewById(R.id.chk_Apositive);
        chk_Anegitive = findViewById(R.id.chk_Anegitive);
        chk_Bpositive = findViewById(R.id.chk_Bpositive);
        chk_Bnegitive = findViewById(R.id.chk_Bnegitive);
        chk_ABpositive = findViewById(R.id.chk_ABpositive);
        chk_ABnegitive = findViewById(R.id.chk_ABnegitive);
        chk_Opositive = findViewById(R.id.chk_Opositive);
        chk_Onegitive = findViewById(R.id.chk_Onegitive);
        btn_Login = findViewById(R.id.btn_Login);
        ll_Logout = findViewById(R.id.ll_Logout);
        switch_Available = findViewById(R.id.switch_Available);
        rl_Loader = findViewById(R.id.rl_Loader);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etIfscCode = findViewById(R.id.etIfscCode);
        etUpiId = findViewById(R.id.etUpiId);
        etPaymentMobile = findViewById(R.id.etPaymentMobile);
        etLatitude = findViewById(R.id.et_Latitude);
        etLongitude = findViewById(R.id.et_Longitude);

        iv_Profile.setOnClickListener(this);
        btn_Login.setOnClickListener(this);
        ll_Logout.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Profile:
                Intent intent4 = new Intent(mContext, ImageSelectActivity.class);
                intent4.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent4.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent4.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent4, 1314);
                break;

            case R.id.btn_Login:
                if (Utility.isNetworkConnected(mContext)) {
                    Validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.ll_Logout:
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.logoutmsg))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                rl_Loader.setVisibility(View.VISIBLE);
                                Utility.setSharedPreference(mContext, "u_id", "");
                                Utility.setSharedPreference(mContext, "u_name", "");
                                Utility.setSharedPreference(mContext, "u_img", "");
                                Utility.setSharedPreference(mContext, "u_email", "");
                                Utility.setSharedPreference(mContext, "location", "");
                                Utility.setSharedPreference(mContext, "user_type", "");
                                Utility.setSharedPreferenceBoolean(mContext, "islogin", false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_Loader.setVisibility(View.GONE);
                                        Intent intent = new Intent(mContext, BloodBankLoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Animatoo.animateSlideLeft(mContext);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 3000);

                            }
                        })

                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;

        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1314 && resultCode == Activity.RESULT_OK) {
            path = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Profile.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(iv_Profile);
            }
        }
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
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String blood_group = result.optString("blood_group");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            String available = result.optString("available");
                            String product_name = result.optString("product_name");
                            String product_price = result.optString("product_price");
                            String account_first_name = result.optString("account_first_name");
                            String account_last_name = result.optString("account_last_name");
                            String account_no = result.optString("account_no");
                            String ifsc_code = result.optString("ifsc_code");
                            String upi_id = result.optString("upi_id");
                            String payment_mobile = result.optString("payment_mobile");
                            String latitude = result.optString("lat");
                            String longitude = result.optString("lng");
                            String qrCode = result.optString("qrcode");

                            etLatitude.setText(latitude);
                            etLongitude.setText(longitude);
                            Glide.with(mContext).load(qrCode).into(imgQrCode);
                            imgQrCode.setOnClickListener(v -> {
                                Intent intent = new Intent(getApplicationContext(),QrCodeActivity.class);
                                intent.putExtra("qrCode",qrCode);
                                intent.putExtra("title",name);

                                // intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            });

                            etFirstName.setText(account_first_name);
                            etLastName.setText(account_last_name);
                            etAccountNumber.setText(account_no);
                            etIfscCode.setText(ifsc_code);
                            etPaymentMobile.setText(payment_mobile);
                            etUpiId.setText(upi_id);

                            is_Getting = true;


                            if (product_name.contains(",")) {

                                String[] bgrp = product_name.split(",");

                                bgroup = "";
                                for (int i = 0; i < bgrp.length; i++) {

                                    if (bgrp[i].equalsIgnoreCase("A+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Apositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("A-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Anegitive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("B+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Bpositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("B-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Bnegitive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("AB+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_ABpositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("AB-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_ABnegitive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("O+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Opositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("O-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Onegitive.setChecked(true);
                                    }

                                }

                            } else {

                                if (product_name.equalsIgnoreCase("A+")) {
                                    bgroup = product_name;
                                    chk_Apositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("A-")) {
                                    bgroup = product_name;
                                    chk_Anegitive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("B+")) {
                                    bgroup = product_name;
                                    chk_Bpositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("B-")) {
                                    bgroup = product_name;
                                    chk_Bnegitive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("AB+")) {
                                    bgroup = product_name;
                                    chk_ABpositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("AB-")) {
                                    bgroup = product_name;
                                    chk_ABnegitive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("O+")) {
                                    bgroup = product_name;
                                    chk_Opositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("O-")) {
                                    bgroup = product_name;
                                    chk_Onegitive.setChecked(true);
                                }

                            }

                            if (product_price.contains(",")) {

                                String[] pprc = product_price.split(",");

                                prc = "";
                                for (int i = 0; i < pprc.length; i++) {

                                    if (chk_Apositive.isChecked() && et_Aposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Aposprice.setText(pprc[i]);
                                    } else if (chk_Anegitive.isChecked() && et_Anegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Anegprice.setText(pprc[i]);
                                    } else if (chk_Bpositive.isChecked() && et_Bposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Bposprice.setText(pprc[i]);
                                    } else if (chk_Bnegitive.isChecked() && et_Bnegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Bnegprice.setText(pprc[i]);
                                    } else if (chk_ABpositive.isChecked() && et_ABposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_ABposprice.setText(pprc[i]);
                                    } else if (chk_ABnegitive.isChecked() && et_ABnegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_ABnegprice.setText(pprc[i]);
                                    } else if (chk_Opositive.isChecked() && et_Oposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Oposprice.setText(pprc[i]);
                                    } else if (chk_Onegitive.isChecked() && et_Onegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Onegprice.setText(pprc[i]);
                                    }

                                }

                            } else {

                                if (chk_Apositive.isChecked()) {
                                    et_Aposprice.setText(product_price);
                                } else if (chk_Anegitive.isChecked()) {
                                    et_Anegprice.setText(product_price);
                                } else if (chk_Bpositive.isChecked()) {
                                    et_Bposprice.setText(product_price);
                                } else if (chk_Bnegitive.isChecked()) {
                                    et_Bnegprice.setText(product_price);
                                } else if (chk_ABpositive.isChecked()) {
                                    et_ABposprice.setText(product_price);
                                } else if (chk_ABnegitive.isChecked()) {
                                    et_ABnegprice.setText(product_price);
                                } else if (chk_Opositive.isChecked()) {
                                    et_Oposprice.setText(product_price);
                                } else if (chk_Onegitive.isChecked()) {
                                    et_Onegprice.setText(product_price);
                                }

                            }

                            Glide.with(mContext).load(user_image).into(iv_Profile);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);

                            tv_Head.setText(name);

                            is_Getting = false;

                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
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

    private void Validate(View v) {

        String uid = Utility.getSharedPreferences(mContext, "u_id");
        String name = et_name.getText().toString();
        String number = et_Mobile.getText().toString();
        String email = et_Email.getText().toString();
        String address = et_Address.getText().toString();
        String pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();
        String user_type = "bank";
        String ubgroup = "";
        String uprc = "";

        if (name.equalsIgnoreCase("")) {
            et_name.setError("Can,t be empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can,t be empty!");
            et_Mobile.requestFocus();
        } else if (address.equalsIgnoreCase("")) {
            et_Address.setError("Can,t be empty!");
            et_Address.requestFocus();
        } else if (latitude.equalsIgnoreCase("")) {
            etLatitude.setError("Can,t be empty!");
            etLatitude.requestFocus();
        } else if (longitude.equalsIgnoreCase("")) {
            etLongitude.setError("Can,t be empty!");
            etLongitude.requestFocus();
        } else if (pass.equalsIgnoreCase("")) {
            et_Password.setError("Can,t be empty!");
            et_Password.requestFocus();
        } else if (repass.equalsIgnoreCase("")) {
            et_Repassword.setError("Can,t be empty!");
            et_Repassword.requestFocus();
        } else if (!pass.equalsIgnoreCase(repass)) {
            et_Repassword.setError("Re Enter!");
            et_Repassword.requestFocus();
            CustomSnakbar.showDarkSnakabar(mContext, v, "Password does not matched!");
        }else if (etFirstName.getText().toString().isEmpty()){
            etFirstName.setError("Enter First Name");
            etFirstName.requestFocus();
        }

        else if (etLastName.getText().toString().isEmpty()){
            etLastName.setError("Enter Last Name");
            etLastName.requestFocus();
        }

        else if (etAccountNumber.getText().toString().isEmpty()){
            etAccountNumber.setError("Enter Account Number");
            etAccountNumber.requestFocus();
        }

        else if (etIfscCode.getText().toString().isEmpty()){
            etIfscCode.setError("Enter IFSC Code");
            etIfscCode.requestFocus();
        }

        else if (etPaymentMobile.getText().toString().isEmpty()){
            etPaymentMobile.setError("Enter Payment Mobile");
            etPaymentMobile.requestFocus();
        } else if (!atleastone) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please select Blood group which are availble in your bank!");
        } else {

            if (!chk_Apositive.isChecked() &&
                    !chk_Anegitive.isChecked() &&
                    !chk_Bpositive.isChecked() &&
                    !chk_Bnegitive.isChecked() &&
                    !chk_ABpositive.isChecked() &&
                    !chk_ABnegitive.isChecked() &&
                    !chk_Opositive.isChecked() &&
                    !chk_Onegitive.isChecked()) {
                CustomSnakbar.showDarkSnakabar(mContext, v, "Please select Blood group which are availble in your bank!");
            } else {

                if (!path.equalsIgnoreCase("")) {
                    File file = new File(path);
                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                    body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);
                }

                if (chk_Apositive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "A+";
                    } else {
                        ubgroup = ubgroup + ",A+";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_Aposprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_Aposprice.getText().toString();
                    }
                }

                if (chk_Anegitive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "A-";
                    } else {
                        ubgroup = ubgroup + ",A-";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_Anegprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_Anegprice.getText().toString();
                    }
                }

                if (chk_Bpositive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "B+";
                    } else {
                        ubgroup = ubgroup + ",B+";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_Bposprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_Bposprice.getText().toString();
                    }
                }

                if (chk_Bnegitive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "B-";
                    } else {
                        ubgroup = ubgroup + ",B-";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_Bnegprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_Bnegprice.getText().toString();
                    }
                }

                if (chk_ABpositive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "AB+";
                    } else {
                        ubgroup = ubgroup + ",AB+";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_ABposprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_ABposprice.getText().toString();
                    }
                }

                if (chk_ABnegitive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "AB-";
                    } else {
                        ubgroup = ubgroup + ",AB-";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_ABnegprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_ABnegprice.getText().toString();
                    }
                }

                if (chk_Opositive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "O+";
                    } else {
                        ubgroup = ubgroup + ",O+";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_Oposprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_Oposprice.getText().toString();
                    }
                }

                if (chk_Onegitive.isChecked()) {
                    if (ubgroup.equalsIgnoreCase("")) {
                        ubgroup = "O-";
                    } else {
                        ubgroup = ubgroup + ",O-";
                    }
                    if (uprc.equalsIgnoreCase("")) {
                        uprc = et_Onegprice.getText().toString();
                    } else {
                        uprc = uprc + "," + et_Onegprice.getText().toString();
                    }
                }

                Log.e("BG=>", "" + ubgroup);
                Log.e("PRC=>", "" + uprc);

                requestToUpdate(uid, name, number, email, address, pass, ubgroup, uprc, user_type, body, v,latitude,longitude);

            }
        }

    }

    private void requestToUpdate(String uid, String name, String number, String email, String address, String pass, String ubgroup, String uprc, String user_type, MultipartBody.Part body, final View view, String latitude, String longitude) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        if (path.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().updateBloodBank(uid, name, number, email, address, ubgroup, uprc, user_type, pass,etFirstName.getText().toString(),etLastName.getText().toString(),
                    etAccountNumber.getText().toString(),etIfscCode.getText().toString(),etUpiId.getText().toString(),etPaymentMobile.getText().toString(),latitude,longitude);
        } else {
            call = AppConfig.loadInterface().updateBloodBankWithImage(uid, name, number, email, address, ubgroup, uprc, user_type, pass, etFirstName.getText().toString(),etLastName.getText().toString(),
                    etAccountNumber.getText().toString(),etIfscCode.getText().toString(),etUpiId.getText().toString(),etPaymentMobile.getText().toString(),latitude,longitude,body);
        }
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
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Profile Update Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String blood_group = result.optString("blood_group");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            final String available = result.optString("available");
                            String product_name = result.optString("product_name");
                            String product_price = result.optString("product_price");

                            Utility.setSharedPreference(mContext, "u_id", user_id);
                            Utility.setSharedPreference(mContext, "u_name", name);
                            Utility.setSharedPreference(mContext, "u_img", user_image);
                            Utility.setSharedPreference(mContext, "u_email", email);
                            Utility.setSharedPreference(mContext, "location", address);
                            Utility.setSharedPreference(mContext, "user_type", user_type);
                            Utility.setSharedPreferenceBoolean(mContext, "islogin", true);

                            is_Getting = true;

                            if (product_name.contains(",")) {

                                String[] bgrp = product_name.split(",");

                                bgroup = "";
                                for (int i = 0; i < bgrp.length; i++) {

                                    if (bgrp[i].equalsIgnoreCase("A+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Apositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("A-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Anegitive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("B+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Bpositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("B-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Bnegitive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("AB+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_ABpositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("AB-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_ABnegitive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("O+")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Opositive.setChecked(true);
                                    } else if (bgrp[i].equalsIgnoreCase("O-")) {
                                        if (bgroup.equalsIgnoreCase("")) {
                                            bgroup = bgrp[i];
                                        } else {
                                            bgroup = bgroup + "," + bgrp[i];
                                        }
                                        atleastone = true;
                                        chk_Onegitive.setChecked(true);
                                    }

                                }

                            } else {

                                if (product_name.equalsIgnoreCase("A+")) {
                                    bgroup = product_name;
                                    chk_Apositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("A-")) {
                                    bgroup = product_name;
                                    chk_Anegitive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("B+")) {
                                    bgroup = product_name;
                                    chk_Bpositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("B-")) {
                                    bgroup = product_name;
                                    chk_Bnegitive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("AB+")) {
                                    bgroup = product_name;
                                    chk_ABpositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("AB-")) {
                                    bgroup = product_name;
                                    chk_ABnegitive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("O+")) {
                                    bgroup = product_name;
                                    chk_Opositive.setChecked(true);
                                } else if (product_name.equalsIgnoreCase("O-")) {
                                    bgroup = product_name;
                                    chk_Onegitive.setChecked(true);
                                }

                            }

                            if (product_price.contains(",")) {

                                String[] pprc = product_price.split(",");

                                prc = "";
                                for (int i = 0; i < pprc.length; i++) {

                                    if (chk_Apositive.isChecked() && et_Aposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Aposprice.setText(pprc[i]);
                                    } else if (chk_Anegitive.isChecked() && et_Anegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Anegprice.setText(pprc[i]);
                                    } else if (chk_Bpositive.isChecked() && et_Bposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Bposprice.setText(pprc[i]);
                                    } else if (chk_Bnegitive.isChecked() && et_Bnegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Bnegprice.setText(pprc[i]);
                                    } else if (chk_ABpositive.isChecked() && et_ABposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_ABposprice.setText(pprc[i]);
                                    } else if (chk_ABnegitive.isChecked() && et_ABnegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_ABnegprice.setText(pprc[i]);
                                    } else if (chk_Opositive.isChecked() && et_Oposprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Oposprice.setText(pprc[i]);
                                    } else if (chk_Onegitive.isChecked() && et_Onegprice.getText().toString().equalsIgnoreCase("")) {
                                        if (prc.equalsIgnoreCase("")) {
                                            prc = pprc[i];
                                        } else {
                                            prc = prc + "," + pprc[i];
                                        }
                                        et_Onegprice.setText(pprc[i]);
                                    }

                                }

                            } else {

                                if (chk_Apositive.isChecked()) {
                                    et_Aposprice.setText(product_price);
                                } else if (chk_Anegitive.isChecked()) {
                                    et_Anegprice.setText(product_price);
                                } else if (chk_Bpositive.isChecked()) {
                                    et_Bposprice.setText(product_price);
                                } else if (chk_Bnegitive.isChecked()) {
                                    et_Bnegprice.setText(product_price);
                                } else if (chk_ABpositive.isChecked()) {
                                    et_ABposprice.setText(product_price);
                                } else if (chk_ABnegitive.isChecked()) {
                                    et_ABnegprice.setText(product_price);
                                } else if (chk_Opositive.isChecked()) {
                                    et_Oposprice.setText(product_price);
                                } else if (chk_Onegitive.isChecked()) {
                                    et_Onegprice.setText(product_price);
                                }

                            }

                            Glide.with(mContext).load(user_image).into(iv_Profile);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);

                            tv_Head.setText(name);

                            is_Getting = false;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (available.equalsIgnoreCase("1")) {
                                        CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                        switch_Available.setChecked(true);
                                        tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_Available.setText("Available");
                                    } else {
                                        CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                        switch_Available.setChecked(false);
                                        tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                        tv_Available.setText("Not Available");
                                    }
                                }
                            }, 1500);


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
}
