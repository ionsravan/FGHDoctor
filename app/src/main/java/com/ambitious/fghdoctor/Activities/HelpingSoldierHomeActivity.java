package com.ambitious.fghdoctor.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
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

public class HelpingSoldierHomeActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext = this;
    private ImageView iv_Driver, iv_One, iv_Two, iv_Three;
    private TextView tv_Login, tv_Available, tv_Head;
    private Button btn_Login;
    private RelativeLayout rl_Loader;
    private LinearLayout ll_Logout;
    private Switch switch_Available;
    private EditText et_name, et_Mobile, et_Email, et_Address, et_Deasc, et_Villagename, et_Cityname, et_Districtname, et_Username, et_Password, et_Repassword;
    private String path = "", path1 = "", path2 = "", path3 = "";
    private String str_pp = "", str_1 = "", str_2 = "", str_3 = "";
    private MultipartBody.Part body;
    private String city = "", lat = "", lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_helping_soldier_home);
        finds();

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getProfile(uid, iv_One);
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
                    requesToChangeAvailablity(uid, "1", iv_One);
                } else {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "0", iv_One);
                }
            }
        });
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
                            String about = result.optString("about");
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

                            if (images.contains(",")) {

                                String[] img = images.split(",");

                                if (img.length == 1) {
                                    str_1 = img[0];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    iv_Two.setImageResource(R.drawable.ic_add_blue);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 2) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 3) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    str_3 = img[2];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    Glide.with(mContext).load(img[2]).into(iv_Three);
                                }

                            } else if (images.contains("png") || images.contains("jpg")) {

                                str_1 = images;
                                Glide.with(mContext).load(images).into(iv_One);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            } else {

                                iv_One.setImageResource(R.drawable.ic_add_blue);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            }

                            str_pp = user_image;
                            Glide.with(mContext).load(user_image).into(iv_Driver);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Deasc.setText(about);
                            et_Username.setText(username);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);
                            et_Mobile.setText(mobile);
                            et_Villagename.setText(village);
                            et_Cityname.setText(city);
                            et_Districtname.setText(distric);

                            tv_Head.setText(username);

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

        iv_Driver = findViewById(R.id.iv_Driver);
        iv_One = findViewById(R.id.iv_One);
        iv_Two = findViewById(R.id.iv_Two);
        iv_Three = findViewById(R.id.iv_Three);
        tv_Login = findViewById(R.id.tv_Login);
        tv_Available = findViewById(R.id.tv_Available);
        tv_Head = findViewById(R.id.tv_Head);
        btn_Login = findViewById(R.id.btn_Login);
        rl_Loader = findViewById(R.id.rl_Loader);
        ll_Logout = findViewById(R.id.ll_Logout);
        switch_Available = findViewById(R.id.switch_Available);
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


        tv_Login.setOnClickListener(this);
        iv_Driver.setOnClickListener(this);
        iv_One.setOnClickListener(this);
        iv_Two.setOnClickListener(this);
        iv_Three.setOnClickListener(this);
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
                                        Intent intent = new Intent(mContext, HelpingSoldierLoginActivity.class);
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

            case R.id.iv_One:
                openSelectionDialog("0", 1111, str_1);
                break;

            case R.id.iv_Two:
                openSelectionDialog("1", 2222, str_2);
                break;

            case R.id.iv_Three:
                openSelectionDialog("2", 3333, str_3);
                break;

            case R.id.iv_Driver:
                Intent intent4 = new Intent(mContext, ImageSelectActivity.class);
                intent4.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent4.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent4.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent4, 1314);
                break;
        }

    }

    private void openSelectionDialog(final String pos, final int i, String str) {

        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_imageoperation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_Changeimg = dialog.findViewById(R.id.tv_Changeimg);
        TextView tv_Removeimg = dialog.findViewById(R.id.tv_Removeimg);
        View view = dialog.findViewById(R.id.view);

        if (str.equalsIgnoreCase("")) {
            tv_Changeimg.setText("Choose Image");
            tv_Removeimg.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else if (str.contains("png") || str.contains("jpg")) {

        } else {
            tv_Removeimg.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }

        dialog.show();

        tv_Changeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, i);

            }
        });

        tv_Removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                removeImage(uid, pos, v);
            }
        });
    }

    private void removeImage(String uid, String pos, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = AppConfig.loadInterface().removeImage(uid, pos);
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

                            CustomSnakbar.showDarkSnakabar(mContext, view, "Image Remove Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String about = result.optString("about");
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

                            if (images.contains(",")) {

                                String[] img = images.split(",");

                                if (img.length == 1) {
                                    str_1 = img[0];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    iv_Two.setImageResource(R.drawable.ic_add_blue);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 2) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 3) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    str_3 = img[2];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    Glide.with(mContext).load(img[2]).into(iv_Three);
                                }

                            } else if (images.contains("png") || images.contains("jpg")) {

                                str_1 = images;
                                Glide.with(mContext).load(images).into(iv_One);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            } else {

                                iv_One.setImageResource(R.drawable.ic_add_blue);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            }

                            str_pp = user_image;
                            Glide.with(mContext).load(user_image).into(iv_Driver);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Deasc.setText(about);
                            et_Username.setText(username);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);
                            et_Mobile.setText(mobile);
                            et_Villagename.setText(village);
                            et_Cityname.setText(city);
                            et_Districtname.setText(distric);

                            tv_Head.setText(username);


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
        if (requestCode == 1111 && resultCode == Activity.RESULT_OK) {
            path1 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path1);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_One.setImageBitmap(myBitmap);
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile.getName(), requestFile);
                UpdateImage(uid, "0", body, iv_One);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path1).into(iv_One);
                File imgFile1 = new File(path1);
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile1);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile1.getName(), requestFile);
                UpdateImage(uid, "0", body, iv_One);
            }
        }
        if (requestCode == 2222 && resultCode == Activity.RESULT_OK) {
            path2 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path2);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Two.setImageBitmap(myBitmap);
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile.getName(), requestFile);
                UpdateImage(uid, "1", body, iv_One);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path2).into(iv_Two);
                File imgFile1 = new File(path2);
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile1);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile1.getName(), requestFile);
                UpdateImage(uid, "1", body, iv_One);
            }
        }
        if (requestCode == 3333 && resultCode == Activity.RESULT_OK) {
            path3 = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path3);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Three.setImageBitmap(myBitmap);
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile.getName(), requestFile);
                UpdateImage(uid, "2", body, iv_One);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path3).into(iv_Three);
                File imgFile1 = new File(path3);
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile1);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile1.getName(), requestFile);
                UpdateImage(uid, "2", body, iv_One);
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

    private void UpdateImage(String uid, String pos, MultipartBody.Part body, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = AppConfig.loadInterface().updateImage(uid, pos, body);
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

                            CustomSnakbar.showDarkSnakabar(mContext, view, "Image Update Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String about = result.optString("about");
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

                            if (images.contains(",")) {

                                String[] img = images.split(",");

                                if (img.length == 1) {
                                    str_1 = img[0];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    iv_Two.setImageResource(R.drawable.ic_add_blue);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 2) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 3) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    str_3 = img[2];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    Glide.with(mContext).load(img[2]).into(iv_Three);
                                }

                            } else if (images.contains("png") || images.contains("jpg")) {

                                str_1 = images;
                                Glide.with(mContext).load(images).into(iv_One);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            } else {

                                iv_One.setImageResource(R.drawable.ic_add_blue);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            }

                            str_pp = user_image;
                            Glide.with(mContext).load(user_image).into(iv_Driver);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Deasc.setText(about);
                            et_Username.setText(username);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);
                            et_Mobile.setText(mobile);
                            et_Villagename.setText(village);
                            et_Cityname.setText(city);
                            et_Districtname.setText(distric);

                            tv_Head.setText(username);


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
        String about = et_Deasc.getText().toString();
        String village = et_Villagename.getText().toString();
        String city = et_Cityname.getText().toString();
        String district = et_Districtname.getText().toString();
        String username = et_Username.getText().toString();
        String pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
        String user_type = "Helping";
        boolean img_sel = false;

        if (!path1.equalsIgnoreCase("") || !str_1.equalsIgnoreCase("")) {
            img_sel = true;
        } else if (!path2.equalsIgnoreCase("") || !str_2.equalsIgnoreCase("")) {
            img_sel = true;
        } else if (!path3.equalsIgnoreCase("") || !str_3.equalsIgnoreCase("")) {
            img_sel = true;
        }


        if (path.equalsIgnoreCase("") && str_pp.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Helping Soldier image.");
        } else if (name.equalsIgnoreCase("")) {
            et_name.setError("Can,t be empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can,t be empty!");
            et_Mobile.requestFocus();
        } else if (address.equalsIgnoreCase("")) {
            et_Address.setError("Can,t be empty!");
            et_Address.requestFocus();
        } else if (about.equalsIgnoreCase("Select Blood Group*")) {
            et_Deasc.setError("Select Blood Group!");
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

            requestToUpdate(uid, name, number, email, address, about, village, city, district, username, pass, user_type, body, v);

        }
    }

    private void requestToUpdate(String uid, String name, String number, String email, String address, String about, String village, String city, String district, String username, String pass, String user_type, MultipartBody.Part body, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call;
        if (path.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().updateHelpingSoldier(uid, name, number, email, address, about, user_type, village, city, district, username, pass);
        } else {
            call = AppConfig.loadInterface().updateHelpingSoldierwithImage(uid, name, number, email, address, about, user_type, village, city, district, username, pass, body);
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
                            String uabout = result.optString("about");
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

                            if (images.contains(",")) {

                                String[] img = images.split(",");

                                if (img.length == 1) {
                                    str_1 = img[0];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    iv_Two.setImageResource(R.drawable.ic_add_blue);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 2) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    iv_Three.setImageResource(R.drawable.ic_add_blue);
                                } else if (img.length == 3) {
                                    str_1 = img[0];
                                    str_2 = img[1];
                                    str_3 = img[2];
                                    Glide.with(mContext).load(img[0]).into(iv_One);
                                    Glide.with(mContext).load(img[1]).into(iv_Two);
                                    Glide.with(mContext).load(img[2]).into(iv_Three);
                                }

                            } else if (images.contains("png") || images.contains("jpg")) {

                                str_1 = images;
                                Glide.with(mContext).load(images).into(iv_One);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            } else {

                                iv_One.setImageResource(R.drawable.ic_add_blue);
                                iv_Two.setImageResource(R.drawable.ic_add_blue);
                                iv_Three.setImageResource(R.drawable.ic_add_blue);

                            }

                            str_pp = user_image;
                            Glide.with(mContext).load(user_image).into(iv_Driver);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Deasc.setText(uabout);
                            et_Username.setText(username);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);
                            et_Mobile.setText(mobile);
                            et_Villagename.setText(village);
                            et_Cityname.setText(city);
                            et_Districtname.setText(distric);

                            tv_Head.setText(username);

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