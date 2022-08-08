package com.ambitious.fghdoctor.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Context;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class BloodBankSignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Profile;
    private TextView tv_Login;
    EditText etFirstName,etLastName,etAccountNumber,etIfscCode,etUpiId,etPaymentMobile;
    private EditText et_name, et_Mobile, et_Email, et_Address, et_Password, et_Repassword, etLatitude,etLongitude,et_Aposprice, et_Anegprice, et_Bposprice, et_Bnegprice, et_ABposprice, et_ABnegprice, et_Oposprice, et_Onegprice;
    private CheckBox chk_Apositive, chk_Anegitive, chk_Bpositive, chk_Bnegitive, chk_ABpositive, chk_ABnegitive, chk_Opositive, chk_Onegitive;
    private Button btn_Login;
    private String path = "";
    private MultipartBody.Part body;
    private boolean atleastone = false;
    private RelativeLayout rl_Loader;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_bank_signup);
        finds();

        chk_Apositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_Anegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_Bpositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_Bnegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_ABpositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_ABnegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_Opositive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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
                }
            }
        });

        chk_Onegitive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
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

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Profile = findViewById(R.id.iv_Profile);
        tv_Login = findViewById(R.id.tv_Login);
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
        rl_Loader = findViewById(R.id.rl_Loader);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etIfscCode = findViewById(R.id.etIfscCode);
        etUpiId = findViewById(R.id.etUpiId);
        etPaymentMobile = findViewById(R.id.etPaymentMobile);
        etLatitude = findViewById(R.id.et_Latitude);
        etLongitude = findViewById(R.id.et_Longitude);

        iv_Bck.setOnClickListener(this);
        tv_Login.setOnClickListener(this);
        iv_Profile.setOnClickListener(this);
        btn_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
            case R.id.tv_Login:
                onBackPressed();
                break;

            case R.id.iv_Profile:
                Intent intent1 = new Intent(mContext, ImageSelectActivity.class);
                intent1.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent1.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent1.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent1, 1314);
                break;

            case R.id.btn_Login:
                if (Utility.isNetworkConnected(mContext)) {
                    Validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;
        }
    }

    private void Validate(View v) {

        String name = et_name.getText().toString();
        String number = et_Mobile.getText().toString();
        String email = et_Email.getText().toString();
        String address = et_Address.getText().toString();
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();
        String pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
        String user_type = "bank";
        String bgroup = "";
        String prc = "";
        String register_id = FirebaseInstanceId.getInstance().getToken();

        if (path.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Bank Profile image.");
        } else if (name.equalsIgnoreCase("")) {
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
        } /*else if (chk_Apositive.isChecked()) {
            if (et_Aposprice.getText().toString().equalsIgnoreCase("")) {
                et_Aposprice.setError("Can,t be empty!");
                et_Aposprice.requestFocus();
            }
        } else if (chk_Anegitive.isChecked()) {
            if (et_Anegprice.getText().toString().equalsIgnoreCase("")) {
                et_Anegprice.setError("Can,t be empty!");
                et_Anegprice.requestFocus();
            }
        } else if (chk_Bpositive.isChecked()) {
            if (et_Bposprice.getText().toString().equalsIgnoreCase("")) {
                et_Bposprice.setError("Can,t be empty!");
                et_Bposprice.requestFocus();
            }
        } else if (chk_Bnegitive.isChecked()) {
            if (et_Bnegprice.getText().toString().equalsIgnoreCase("")) {
                et_Bnegprice.setError("Can,t be empty!");
                et_Bnegprice.requestFocus();
            }
        } else if (chk_ABpositive.isChecked()) {
            if (et_ABposprice.getText().toString().equalsIgnoreCase("")) {
                et_ABposprice.setError("Can,t be empty!");
                et_ABposprice.requestFocus();
            }
        } else if (chk_ABnegitive.isChecked()) {
            if (et_ABnegprice.getText().toString().equalsIgnoreCase("")) {
                et_ABnegprice.setError("Can,t be empty!");
                et_ABnegprice.requestFocus();
            }
        } else if (chk_Opositive.isChecked()) {
            if (et_Oposprice.getText().toString().equalsIgnoreCase("")) {
                et_Oposprice.setError("Can,t be empty!");
                et_Oposprice.requestFocus();
            }
        } else if (chk_Onegitive.isChecked()) {
            if (et_Onegprice.getText().toString().equalsIgnoreCase("")) {
                et_Onegprice.setError("Can,t be empty!");
                et_Onegprice.requestFocus();
            }
        } */ else {

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
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "A+";
                    } else {
                        bgroup = bgroup + ",A+";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_Aposprice.getText().toString();
                    } else {
                        prc = prc + "," + et_Aposprice.getText().toString();
                    }
                }

                if (chk_Anegitive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "A-";
                    } else {
                        bgroup = bgroup + ",A-";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_Anegprice.getText().toString();
                    } else {
                        prc = prc + "," + et_Anegprice.getText().toString();
                    }
                }

                if (chk_Bpositive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "B+";
                    } else {
                        bgroup = bgroup + ",B+";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_Bposprice.getText().toString();
                    } else {
                        prc = prc + "," + et_Bposprice.getText().toString();
                    }
                }

                if (chk_Bnegitive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "B-";
                    } else {
                        bgroup = bgroup + ",B-";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_Bnegprice.getText().toString();
                    } else {
                        prc = prc + "," + et_Bnegprice.getText().toString();
                    }
                }

                if (chk_ABpositive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "AB+";
                    } else {
                        bgroup = bgroup + ",AB+";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_ABposprice.getText().toString();
                    } else {
                        prc = prc + "," + et_ABposprice.getText().toString();
                    }
                }

                if (chk_ABnegitive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "AB-";
                    } else {
                        bgroup = bgroup + ",AB-";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_ABnegprice.getText().toString();
                    } else {
                        prc = prc + "," + et_ABnegprice.getText().toString();
                    }
                }

                if (chk_Opositive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "O+";
                    } else {
                        bgroup = bgroup + ",O+";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_Oposprice.getText().toString();
                    } else {
                        prc = prc + "," + et_Oposprice.getText().toString();
                    }
                }

                if (chk_Onegitive.isChecked()) {
                    if (bgroup.equalsIgnoreCase("")) {
                        bgroup = "O-";
                    } else {
                        bgroup = bgroup + ",O-";
                    }
                    if (prc.equalsIgnoreCase("")) {
                        prc = et_Onegprice.getText().toString();
                    } else {
                        prc = prc + "," + et_Onegprice.getText().toString();
                    }
                }

                Log.e("BG=>", "" + bgroup);
                Log.e("PRC=>", "" + prc);

                requestToRegister(name, number, email, address, pass, bgroup, prc, user_type, register_id, body, v,latitude,longitude);

            }
        }

    }

    private void requestToRegister(String name, String number, String email, String address, String pass, String bgroup, String prc, String user_type, String reg_id, MultipartBody.Part body, final View view, String latitude, String longitude) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().signupBloodBank(name, number, email, address, bgroup, prc, user_type, pass, reg_id,etFirstName.getText().toString(),etLastName.getText().toString(),
                etAccountNumber.getText().toString(),etIfscCode.getText().toString(),etUpiId.getText().toString(),etPaymentMobile.getText().toString(),latitude,longitude, body);
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
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Registration Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");

                            Utility.setSharedPreference(mContext, "u_id", user_id);
                            Utility.setSharedPreference(mContext, "u_name", name);
                            Utility.setSharedPreference(mContext, "u_img", user_image);
                            Utility.setSharedPreference(mContext, "u_email", email);
                            Utility.setSharedPreference(mContext, "location", address);
                            Utility.setSharedPreference(mContext, "user_type", user_type);
                            Utility.setSharedPreferenceBoolean(mContext, "islogin", true);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(mContext, BloodBankHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
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
}
