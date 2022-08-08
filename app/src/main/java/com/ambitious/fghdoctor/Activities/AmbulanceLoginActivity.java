package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.AdapterView;
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
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AmbulanceLoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private EditText et_Email, et_Pass;
    private TextView tv_Signup;
    private Button btn_Login;
    private ImageView iv_Bck;
    private TextView tv_Type;
    private String[] type = {"Select Type*", "108", "Private", "Team"};
    private Spinner sp_Type;
    private RelativeLayout rl_Loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_login);
        finds();

        sp_Type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_Type.setText(type[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_Type.setHint("Select Type*");
            }
        });

    }

    private void finds() {

        tv_Type = findViewById(R.id.tv_Type);
        rl_Loader = findViewById(R.id.rl_Loader);
        iv_Bck = findViewById(R.id.iv_Bck);
        et_Email = findViewById(R.id.et_Email);
        et_Pass = findViewById(R.id.et_Pass);
        tv_Signup = findViewById(R.id.tv_Signup);
        btn_Login = findViewById(R.id.btn_Login);
        sp_Type = findViewById(R.id.sp_Type);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, type);
        sp_Type.setAdapter(adapter);

        tv_Signup.setOnClickListener(this);
        iv_Bck.setOnClickListener(this);
        btn_Login.setOnClickListener(this);
        tv_Type.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_Signup:
                startActivity(new Intent(mContext, AmbulanceSignupActivity.class));
                Animatoo.animateCard(mContext);
                break;

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.tv_Type:
                sp_Type.performClick();
                break;

            case R.id.btn_Login:
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

        String m_androidId = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String email = et_Email.getText().toString();
        String pass = et_Pass.getText().toString();
        String type = tv_Type.getText().toString();
        String user_type = type + " ambulance";
        String register_id = FirebaseInstanceId.getInstance().getToken();

        if (email.equalsIgnoreCase("")) {
            et_Email.setError("Can't be Empty");
            et_Email.requestFocus();
        } else if (pass.equalsIgnoreCase("")) {
            et_Pass.setError("Can't be Empty");
            et_Pass.requestFocus();
        } else if (type.equalsIgnoreCase("Select Type*")) {
            tv_Type.setError("Select Type!");
            tv_Type.requestFocus();
            CustomSnakbar.showDarkSnakabar(mContext, v, "Select Type.");
        } else {
            requestToLogin(m_androidId, email, pass, user_type, register_id, v);
        }

    }

    private void requestToLogin(String device_id, String email, String pass, String type, String reg_id, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().login(device_id, email, type, pass, reg_id);
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
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Login Successfull.");

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

                                    Intent intent = new Intent(mContext, AmbulanceHomeActivity.class);
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
}
