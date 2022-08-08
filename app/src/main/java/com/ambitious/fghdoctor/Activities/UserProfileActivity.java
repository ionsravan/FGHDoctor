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

public class UserProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Camera;
    private TextView tv_Start;
    private String path1 = "";
    private MultipartBody.Part body;
    private EditText et_Fname, et_Lname, et_Email, et_Password, et_Mobile;
    private RelativeLayout rl_Loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
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

        rl_Loader = findViewById(R.id.rl_Loader);
        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Start = findViewById(R.id.tv_Start);
        iv_Camera = findViewById(R.id.iv_Camera);
        et_Fname = findViewById(R.id.et_Fname);
        et_Lname = findViewById(R.id.et_Lname);
        et_Email = findViewById(R.id.et_Email);
        et_Password = findViewById(R.id.et_Password);
        et_Mobile = findViewById(R.id.et_Mobile);

        iv_Bck.setOnClickListener(this);
        tv_Start.setOnClickListener(this);
        iv_Camera.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.tv_Start:
                if (Utility.isNetworkConnected(mContext)) {
                    Validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.iv_Camera:
                Intent intent = new Intent(mContext, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, 1213);
                break;
        }

    }

    private void Validate(View v) {

        String fname = et_Fname.getText().toString();
        String lname = et_Lname.getText().toString();
        String email = et_Email.getText().toString();
        String pass = et_Password.getText().toString();
        String number = et_Mobile.getText().toString();
        String fullname = fname + " " + lname;
        String type = "user";
        String uid = Utility.getSharedPreferences(mContext, "u_id");

        if (fname.equalsIgnoreCase("")) {
            et_Fname.setError("Can't be Empty");
            et_Fname.requestFocus();
        } else if (lname.equalsIgnoreCase("")) {
            et_Lname.setError("Can't be Empty");
            et_Lname.requestFocus();
        } else if (email.equalsIgnoreCase("")) {
            et_Email.setError("Can't be Empty");
            et_Email.requestFocus();
        } else if (pass.equalsIgnoreCase("")) {
            et_Password.setError("Can't be Empty");
            et_Password.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can't be Empty");
            et_Mobile.requestFocus();
        } else {

            if (!path1.equalsIgnoreCase("")) {
                File file = new File(path1);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);
            }

            requestToUpdate(uid, fullname, email, pass, number, type, body, v);

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
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");

                            Glide.with(mContext).load(user_image).into(iv_Camera);
                            String[] n = name.split(" ");
                            et_Fname.setText(n[0]);
                            et_Lname.setText(n[1]);
                            et_Email.setText(email);
                            et_Password.setText(password);
                            et_Mobile.setText(mobile);


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

    private void requestToUpdate(String uid, String fullname, String email, String pass, String number, String type, MultipartBody.Part body, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        if (path1.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().updateProfile(uid, fullname, email, pass, number, type);
        } else {
            call = AppConfig.loadInterface().updateProfileImage(uid, fullname, email, pass, number, type, body);
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
                            CustomSnakbar.showSnakabar(mContext, view, "Profile Update Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");

                            Utility.setSharedPreference(mContext, "u_id", user_id);
                            Utility.setSharedPreference(mContext, "u_name", name);
                            Utility.setSharedPreference(mContext, "u_img", user_image);
                            Utility.setSharedPreference(mContext, "u_email", email);
                            Utility.setSharedPreference(mContext, "location", address);
                            Utility.setSharedPreference(mContext, "user_type", user_type);
                            Utility.setSharedPreferenceBoolean(mContext, "islogin", true);

                            Glide.with(mContext).load(user_image).into(iv_Camera);
                            String[] n = name.split(" ");
                            et_Fname.setText(n[0]);
                            et_Lname.setText(n[1]);
                            et_Email.setText(email);
                            et_Password.setText(password);
                            et_Mobile.setText(mobile);


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
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
            path1 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path1);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Camera.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path1).into(iv_Camera);
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}
