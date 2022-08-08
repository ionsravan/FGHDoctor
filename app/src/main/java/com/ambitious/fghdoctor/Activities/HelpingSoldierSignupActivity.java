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
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class HelpingSoldierSignupActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Driver, iv_One, iv_Two, iv_Three;
    private TextView tv_Login;
    private Button btn_Login;
    private RelativeLayout rl_Loader;
    private EditText et_name, et_Mobile, et_Email, et_Address, et_Deasc, et_Villagename, et_Cityname, et_Districtname, et_Username, et_Password, et_Repassword;
    private String path = "", path1 = "", path2 = "", path3 = "";
    private MultipartBody.Part body;
    private String city = "", lat = "", lon = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping_soldier_signup);
        finds();

        city = getIntent().getStringExtra("city");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lon");

        et_Cityname.setText(city);

    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Driver = findViewById(R.id.iv_Driver);
        iv_One = findViewById(R.id.iv_One);
        iv_Two = findViewById(R.id.iv_Two);
        iv_Three = findViewById(R.id.iv_Three);
        tv_Login = findViewById(R.id.tv_Login);
        btn_Login = findViewById(R.id.btn_Login);
        rl_Loader = findViewById(R.id.rl_Loader);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Email = findViewById(R.id.et_Email);
        et_Address = findViewById(R.id.et_Address);
        et_Deasc = findViewById(R.id.et_Deasc);
        et_Villagename = findViewById(R.id.et_Villagename);
        et_Cityname = findViewById(R.id.et_Cityname);
        et_Districtname = findViewById(R.id.et_Districtname);
        et_Username = findViewById(R.id.et_Username);
        et_Password = findViewById(R.id.et_Password);
        et_Repassword = findViewById(R.id.et_Repassword);


        iv_Bck.setOnClickListener(this);
        tv_Login.setOnClickListener(this);
        iv_Driver.setOnClickListener(this);
        iv_One.setOnClickListener(this);
        iv_Two.setOnClickListener(this);
        iv_Three.setOnClickListener(this);
        btn_Login.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
            case R.id.tv_Login:
                onBackPressed();
                break;


            case R.id.iv_One:
            case R.id.iv_Two:
            case R.id.iv_Three:
                Intent intent = new Intent(mContext, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
                break;

            case R.id.iv_Driver:
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
        String desc = et_Deasc.getText().toString();
        String village = et_Villagename.getText().toString();
        String city = et_Cityname.getText().toString();
        String district = et_Districtname.getText().toString();
        String username = et_Username.getText().toString();
        String pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
        String user_type = "Helping";
        boolean img_sel = false;
        String register_id = FirebaseInstanceId.getInstance().getToken();

        if (!path1.equalsIgnoreCase("")) {
            img_sel = true;
        }

        if (!path2.equalsIgnoreCase("")) {
            img_sel = true;
        }

        if (!path3.equalsIgnoreCase("")) {
            img_sel = true;
        }


        if (path.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Soldier image.");
        } else if (name.equalsIgnoreCase("")) {
            et_name.setError("Can,t be empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can,t be empty!");
            et_Mobile.requestFocus();
        } else if (address.equalsIgnoreCase("")) {
            et_Address.setError("Can,t be empty!");
            et_Address.requestFocus();
        } else if (desc.equalsIgnoreCase("")) {
            et_Deasc.setError("Can,t be empty!");
            et_Deasc.requestFocus();
        } else if (!img_sel) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Select atleast one Helping Histry image.");
        } else if (village.equalsIgnoreCase("")) {
            et_Villagename.setError("Can,t be empty!");
            et_Villagename.requestFocus();
        } else if (city.equalsIgnoreCase("")) {
            et_Cityname.setError("Can,t be empty!");
            et_Cityname.requestFocus();
        } else if (district.equalsIgnoreCase("")) {
            et_Districtname.setError("Can,t be empty!");
            et_Districtname.requestFocus();
        } else if (username.equalsIgnoreCase("")) {
            et_Username.setError("Can,t be empty!");
            et_Username.requestFocus();
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
        } else {

            if (!path.equalsIgnoreCase("")) {
                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);
            }

            ArrayList<MultipartBody.Part> parts = new ArrayList<>();

            if (!path1.equalsIgnoreCase("")) {
                File file = new File(path1);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", file.getName(), requestFile);
                parts.add(body);
            }

            if (!path2.equalsIgnoreCase("")) {
                File file = new File(path2);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", file.getName(), requestFile);
                parts.add(body);
            }

            if (!path3.equalsIgnoreCase("")) {
                File file = new File(path3);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", file.getName(), requestFile);
                parts.add(body);
            }

            requestToRegister(name, number, email, address, desc, village, city, district, username, pass, user_type, register_id, lat, lon, body, parts, v);

        }

    }

    private void requestToRegister(String name, String number, String email, String address, String desc, String village, String city, String district, String username, String pass, String user_type, String reg_id, String lat, String lon, MultipartBody.Part body, ArrayList<MultipartBody.Part> parts, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().signupHelpingSoldier(name, number, email, address, desc, user_type, village, city, district, username, pass, reg_id, lat, lon, body, parts);
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
                        System.out.println("HelpingSignup" + object);

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

                                    Intent intent = new Intent(mContext, HelpingSoldierHomeActivity.class);
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
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            if (path1.equalsIgnoreCase("")) {
                path1 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
                File imgFile = new File(path1);
                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    iv_One.setImageBitmap(myBitmap);

                } else {
                    Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                    Glide.with(mContext).load(path1).into(iv_One);
                }
            } else if (path2.equalsIgnoreCase("")) {
                path2 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
                File imgFile = new File(path2);
                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    iv_Two.setImageBitmap(myBitmap);

                } else {
                    Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                    Glide.with(mContext).load(path2).into(iv_Two);
                }
            } else if (path3.equalsIgnoreCase("")) {
                path3 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
                File imgFile = new File(path3);
                if (imgFile.exists()) {

                    Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    iv_Three.setImageBitmap(myBitmap);

                } else {
                    Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                    Glide.with(mContext).load(path3).into(iv_Three);
                }
            }
        } else if (requestCode == 1314 && resultCode == Activity.RESULT_OK) {
            path = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Driver.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(iv_Driver);
            }
        }
    }
}