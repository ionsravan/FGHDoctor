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
import android.widget.RadioButton;
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

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class OxygenBankSignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Profile;
    private RadioButton rbtn_Free, rbtn_Paid;
    private CheckBox chk_Oxygencylinder, chk_Oxygenconcentrator;
    private EditText et_name, et_Mobile, et_Email, et_Address, et_Desc, et_Password, et_Repassword, et_Oxygencylinder, et_Oxygenconcentrator;
    private TextView tv_Login, tv_Avail;
    private RelativeLayout rl_Loader;
    private Button btn_Login;
    private String type = "", path = "", city = "", lat = "", lon = "";
    private boolean atleastone = false;
    private MultipartBody.Part body;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxygen_bank_signup);
        finds();

        city = getIntent().getStringExtra("city");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lon");

        rbtn_Free.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "oxygen free";
                    tv_Avail.setText("Select Available Service*");
                    tv_Avail.setVisibility(View.VISIBLE);
                    et_Oxygencylinder.setVisibility(View.GONE);
                    et_Oxygenconcentrator.setVisibility(View.GONE);
                    if (chk_Oxygencylinder.isChecked()) {
                        et_Oxygencylinder.setText("");
                        chk_Oxygencylinder.setChecked(false);
                    } else {
                        et_Oxygencylinder.setText("");
                    }
                    if (chk_Oxygenconcentrator.isChecked()) {
                        et_Oxygenconcentrator.setText("");
                        chk_Oxygenconcentrator.setChecked(false);
                    } else {
                        et_Oxygenconcentrator.setText("");
                    }
                    atleastone = false;
                }
            }
        });

        rbtn_Paid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    type = "oxygen paid";
                    tv_Avail.setText("Select Available Service with thier price per day*");
                    tv_Avail.setVisibility(View.VISIBLE);
                    et_Oxygencylinder.setVisibility(View.VISIBLE);
                    et_Oxygenconcentrator.setVisibility(View.VISIBLE);
                    if (chk_Oxygencylinder.isChecked()) {
                        chk_Oxygencylinder.setChecked(false);
                    }
                    if (chk_Oxygenconcentrator.isChecked()) {
                        chk_Oxygenconcentrator.setChecked(false);
                    }
                    atleastone = false;
                }
            }
        });

        chk_Oxygencylinder.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (type.equalsIgnoreCase("")) {
                    chk_Oxygencylinder.setChecked(false);
                    CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Select Service Type First!");
                } else if (type.equalsIgnoreCase("oxygen free")) {
                    if (isChecked) {
                        atleastone = true;
                        chk_Oxygencylinder.setChecked(true);
                    }
                } else if (type.equalsIgnoreCase("oxygen paid")) {
                    if (isChecked) {
                        if (et_Oxygencylinder.getText().toString().equalsIgnoreCase("")) {
                            chk_Oxygencylinder.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Oxygencylinder.setError("Can't be Empty!");
                            et_Oxygencylinder.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Oxygencylinder.setChecked(true);
                        }
                    }
                }
            }
        });

        chk_Oxygenconcentrator.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (type.equalsIgnoreCase("")) {
                    chk_Oxygenconcentrator.setChecked(false);
                    CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Select Service Type First!");
                } else if (type.equalsIgnoreCase("oxygen free")) {
                    if (isChecked) {
                        atleastone = true;
                        chk_Oxygenconcentrator.setChecked(true);
                    }
                } else if (type.equalsIgnoreCase("oxygen paid")) {
                    if (isChecked) {
                        if (et_Oxygenconcentrator.getText().toString().equalsIgnoreCase("")) {
                            chk_Oxygenconcentrator.setChecked(false);
                            CustomSnakbar.showDarkSnakabar(mContext, buttonView, "Please Enter Amount!");
                            et_Oxygenconcentrator.setError("Can't be Empty!");
                            et_Oxygenconcentrator.requestFocus();
                        } else {
                            atleastone = true;
                            chk_Oxygenconcentrator.setChecked(true);
                        }
                    }
                }
            }
        });

        et_Oxygencylinder.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Oxygencylinder.length() == 0) {
                    chk_Oxygencylinder.setChecked(false);
                }
            }
        });

        et_Oxygenconcentrator.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (et_Oxygenconcentrator.length() == 0) {
                    chk_Oxygenconcentrator.setChecked(false);
                }
            }
        });

    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Profile = findViewById(R.id.iv_Profile);
        rbtn_Free = findViewById(R.id.rbtn_Free);
        rbtn_Paid = findViewById(R.id.rbtn_Paid);
        chk_Oxygencylinder = findViewById(R.id.chk_Oxygencylinder);
        chk_Oxygenconcentrator = findViewById(R.id.chk_Oxygenconcentrator);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Email = findViewById(R.id.et_Email);
        et_Address = findViewById(R.id.et_Address);
        et_Desc = findViewById(R.id.et_Desc);
        et_Password = findViewById(R.id.et_Password);
        et_Repassword = findViewById(R.id.et_Repassword);
        et_Oxygencylinder = findViewById(R.id.et_Oxygencylinder);
        et_Oxygenconcentrator = findViewById(R.id.et_Oxygenconcentrator);
        tv_Login = findViewById(R.id.tv_Login);
        tv_Avail = findViewById(R.id.tv_Avail);
        tv_Avail = findViewById(R.id.tv_Avail);
        rl_Loader = findViewById(R.id.rl_Loader);
        btn_Login = findViewById(R.id.btn_Login);

        iv_Bck.setOnClickListener(this);
        iv_Profile.setOnClickListener(this);
        tv_Login.setOnClickListener(this);
        btn_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_Login:
            case R.id.iv_Bck:
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
        String desc = et_Desc.getText().toString();
        String pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
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
        } else if (!rbtn_Free.isChecked() && !rbtn_Paid.isChecked()) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please select which type of Service availble in your bank!");
        } else if (!chk_Oxygencylinder.isChecked() && !chk_Oxygenconcentrator.isChecked()) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please select which type of equipments are availble in your bank!");
        } else {

            if (!path.equalsIgnoreCase("")) {
                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);
            }

            if (chk_Oxygencylinder.isChecked()) {
                if (bgroup.equalsIgnoreCase("")) {
                    bgroup = "OxygenCylinder";
                } else {
                    bgroup = bgroup + ",OxygenCylinder";
                }
                if (prc.equalsIgnoreCase("")) {
                    prc = et_Oxygencylinder.getText().toString();
                } else {
                    prc = prc + "," + et_Oxygencylinder.getText().toString();
                }
            }

            if (chk_Oxygenconcentrator.isChecked()) {
                if (bgroup.equalsIgnoreCase("")) {
                    bgroup = "OxygenConcentrator";
                } else {
                    bgroup = bgroup + ",OxygenConcentrator";
                }
                if (prc.equalsIgnoreCase("")) {
                    prc = et_Oxygenconcentrator.getText().toString();
                } else {
                    prc = prc + "," + et_Oxygenconcentrator.getText().toString();
                }
            }

            Log.e("BG=>", "" + bgroup);
            Log.e("PRC=>", "" + prc);

            requestToRegister(name, number, email, address, desc, pass, bgroup, prc, type, register_id, body, v);

        }

    }

    private void requestToRegister(String name, String number, String email, String address, String desc, String pass, String bgroup, String prc, String type, String register_id, MultipartBody.Part body, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().signupOxygenBank(name, number, email, address, desc, bgroup, prc, type, pass, register_id, city, lat, lon, body);
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
                        System.out.println("OxygenBankSignup" + object);

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

                                    Intent intent = new Intent(mContext, OxygenBankHomeActivity.class);
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}