package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
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
import com.ambitious.fghdoctor.Utils.CircleImageView;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AppointmentDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private CircleImageView civ_Doctor;
    private TextView tv_Name, tv_Apdate, tv_Aptime, tv_Tokenno, tv_Bookap, tv_Apstatus;
    private RatingBar bar_Rating;
    private EditText et_Msg;
    private LinearLayout ll_Feedback;
    private RelativeLayout rl_Loader;
    private String user_id = "", doctor_id = "", appointment_id = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_detail);
        finds();

        if (getIntent().getExtras() != null) {

            String obj = getIntent().getStringExtra("obj");

            try {

                JSONObject result = new JSONObject(obj);

                user_id = result.optString("user_id");
                doctor_id = result.optString("doctor_id");
                appointment_id = result.optString("appointment_id");
                String token = result.optString("token");
                String date = result.optString("date");
                String time = result.optString("time");
                String category_name = result.optString("category_name");
                String doctor_name = result.optString("doctor_name");
                String doctor_image = result.optString("doctor_image");
                String status = result.optString("status");
                String reviewed = result.optString("reviewed");


                if (doctor_image.contains("png") || doctor_image.contains("jpg")) {
                    Glide.with(mContext).load(doctor_image).into(civ_Doctor);
                } else {
                    civ_Doctor.setImageResource(R.drawable.profile_new);
                }

                if (doctor_name.contains("Dr.")) {
                    tv_Name.setText(doctor_name + " (" + category_name + ")");
                } else {
                    tv_Name.setText("Dr." + doctor_name + " (" + category_name + ")");
                }
                tv_Tokenno.setText(token);
                tv_Aptime.setText(time);
                tv_Apstatus.setText(status);

                SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dat = inFormat.parse(date);
                SimpleDateFormat outFormat1 = new SimpleDateFormat("dd MMM yyyy");
                String datee = outFormat1.format(dat);
                tv_Apdate.setText(datee);

                if (status.equalsIgnoreCase("Attended") && reviewed.equalsIgnoreCase("0")) {
                    ll_Feedback.setVisibility(View.VISIBLE);
                } else {
                    ll_Feedback.setVisibility(View.GONE);
                }

            } catch (JSONException | ParseException e) {
                e.printStackTrace();
            }
        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        civ_Doctor = findViewById(R.id.civ_Doctor);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Apdate = findViewById(R.id.tv_Apdate);
        tv_Aptime = findViewById(R.id.tv_Aptime);
        tv_Tokenno = findViewById(R.id.tv_Tokenno);
        tv_Bookap = findViewById(R.id.tv_Bookap);
        tv_Apstatus = findViewById(R.id.tv_Apstatus);
        bar_Rating = findViewById(R.id.bar_Rating);
        et_Msg = findViewById(R.id.et_Msg);
        ll_Feedback = findViewById(R.id.ll_Feedback);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        tv_Bookap.setOnClickListener(this);

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

            case R.id.tv_Bookap:
                if (Utility.isNetworkConnected(mContext)) {
                    validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

        }

    }

    private void validate(View v) {

        String rate = String.valueOf(bar_Rating.getRating());
        String msg = et_Msg.getText().toString();

        if (rate.equalsIgnoreCase("0.0")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Rating can't be empty!");
        } else {

            requestToSubmit(user_id, doctor_id, appointment_id, rate, msg, v);

        }


    }

    private void requestToSubmit(String user_id, String doctor_id, String appointment_id, String rate, String msg, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().addRating(user_id, doctor_id, appointment_id, rate, msg);
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
                        System.out.println("Feedback" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Feedback give Successfully.");

                            /*JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");*/

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
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Addpatient " + e.getMessage());
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