package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class ReportsActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private Button btn_Add;
    private EditText et_name, et_Mobile;
    private RelativeLayout rl_Loader;
    private FloatingActionButton addReport;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        finds();
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        btn_Add = findViewById(R.id.btn_Add);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        rl_Loader = findViewById(R.id.rl_Loader);
        addReport = findViewById(R.id.addReport);

        iv_Bck.setOnClickListener(this);
        btn_Add.setOnClickListener(this);
        addReport.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

                case R.id.addReport:

                    if (Utility.isNetworkConnected(mContext)) {
                        String uid = Utility.getSharedPreferences(mContext, "u_id");

                        if (!uid.equalsIgnoreCase("")) {
                            Intent intent = new Intent(mContext, LabsHomeActivity.class);
                          //  intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Animatoo.animateSlideLeft(mContext);
                            startActivity(intent);
                           // finish();
                        }else {
                            Intent intent = new Intent(mContext, LoginActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Animatoo.animateSlideLeft(mContext);
                            startActivity(intent);
                            finish();
                        }

                    } else {
                        AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }

                break;

            case R.id.btn_Add:
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    valiidate(uid, v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

        }

    }

    private void valiidate(String uid, View v) {

        String name = et_name.getText().toString();
        String number = et_Mobile.getText().toString();

        if (name.equalsIgnoreCase("")) {
            et_name.setError("Can't be Empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can't be Empty!");
            et_Mobile.requestFocus();
        } else {

            requestToSerach(name, number, v);

        }

    }

    private void requestToSerach(String name, String number, View view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = AppConfig.loadInterface().getPatientreport(name, number);
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
                        System.out.println("GetReport" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONArray array = object.optJSONArray("result");

                            startActivity(new Intent(mContext, ReportListActivity.class)
                                    .putExtra("arr", "" + array.toString()));
                            Animatoo.animateCard(mContext);

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
}