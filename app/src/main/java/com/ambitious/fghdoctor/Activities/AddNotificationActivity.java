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
import android.widget.Button;
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

public class AddNotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Image;
    private EditText et_Title, et_Message;
    private TextView tv_Location;
    private RelativeLayout rl_Loader;
    private Button btn_Submit;
    private String uid = "", city = "", lat = "", lon = "", path = "";
    private MultipartBody.Part body;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_notification);
        finds();

        if (getIntent().getExtras() != null) {

            uid = getIntent().getStringExtra("uid");
            city = getIntent().getStringExtra("city");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");

            tv_Location.setText(city);

        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Image = findViewById(R.id.iv_Image);
        et_Title = findViewById(R.id.et_Title);
        et_Message = findViewById(R.id.et_Message);
        rl_Loader = findViewById(R.id.rl_Loader);
        tv_Location = findViewById(R.id.tv_Location);
        btn_Submit = findViewById(R.id.btn_Submit);

        iv_Bck.setOnClickListener(this);
        iv_Image.setOnClickListener(this);
        btn_Submit.setOnClickListener(this);
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
                onBackPressed();
                break;

            case R.id.iv_Image:
                Intent intent1 = new Intent(mContext, ImageSelectActivity.class);
                intent1.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent1.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent1.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent1, 1314);
                break;

            case R.id.btn_Submit:
                if (Utility.isNetworkConnected(mContext)) {
                    validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

        }

    }

    private void validate(View view) {

        String title = et_Title.getText().toString();
        String msg = et_Message.getText().toString();

        if (title.equalsIgnoreCase("")) {
            et_Title.setError("Can't be Empty!");
            et_Title.requestFocus();
        } else if (msg.equalsIgnoreCase("")) {
            et_Message.setError("Can't be Empty!");
            et_Message.requestFocus();
        } else {

            if (!path.equalsIgnoreCase("")) {
                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("image", file.getName(), requestFile);
            }

            requestToNotify(uid, title, msg, city, lat, lon, body, view);

        }

    }

    private void requestToNotify(String uid, String title, String msg, String city, String lat, String lon, MultipartBody.Part body, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        if (path.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().sendNotification(uid, title, msg, city, lat, lon);
        } else {
            call = AppConfig.loadInterface().sendNotificationImage(uid, title, msg, city, lat, lon, body);
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
                        System.out.println("SendNotification" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Sent Successfully.");
                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    rl_Loader.setVisibility(View.GONE);
                                    Intent intent = new Intent(mContext, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "SendNotification " + e.getMessage());
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
                iv_Image.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(iv_Image);
            }
        }
    }
}