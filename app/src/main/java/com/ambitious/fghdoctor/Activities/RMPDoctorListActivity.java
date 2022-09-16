package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.MedicalShopListAdapter;
import com.ambitious.fghdoctor.Adapters.RMPDoctorsApdapter;
import com.ambitious.fghdoctor.Model.MedicalShop;
import com.ambitious.fghdoctor.Model.RMP;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class RMPDoctorListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Head, tv_Notavailable;
    private RecyclerView rv_Rmpdoctor;
    private LinearLayout ll_Login;
    private EditText et_Search;
    public String type = "", wallet = "", donated = "";
    private RelativeLayout rl_Loader;
    private ArrayList<RMP> rmpArrayList;
    private RMPDoctorsApdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_m_p_doctor_list);
        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));
        wallet = getIntent().getStringExtra("wallet");
        donated = getIntent().getStringExtra("donated");

        type = "rmp";

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getUsers(uid, type, iv_Bck);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                if (rmpArrayList.size() > 0) {
                    filter(s.toString());
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Medical shop is not available.");
                }

            }
        });
    }

    private void filter(String text) {

        ArrayList<RMP> temp = new ArrayList();
        for (RMP d : rmpArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getUser_name().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Rmpdoctor.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adapter.updateList(temp);
        } else {
            rv_Rmpdoctor.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void getUsers(String uid, final String type, final ImageView view) {
        String latitude = Utility.getSharedPreferences(mContext, "latitude");
        String longitude = Utility.getSharedPreferences(mContext, "longitude");

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getUsers(type,latitude,longitude);
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

                            et_Search.setVisibility(View.VISIBLE);
                            rv_Rmpdoctor.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            rmpArrayList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String username = result.optString("username");
                                String user_image = result.optString("user_image");
                                String email = result.optString("email");
                                String address = result.optString("address");
                                String password = result.optString("password");
                                String mobile = result.optString("mobile");
                                String rating = result.optString("rating");
                                String user_type = result.optString("user_type");
                                String active_status = result.optString("status");
                                String available = result.optString("available");
                                String images = result.optString("images");

                                String img = "";

                                if (images.contains(",")) {

                                    String[] imgs = images.split(",");
                                    img = imgs[0];

                                } else {

                                    img = images;
                                }


                                RMP rmp = new RMP();
                                rmp.setId(user_id);
                                rmp.setName(name);
                                rmp.setUser_name(username);
                                rmp.setAddress(address);
                                rmp.setRating(rating);
                                rmp.setImage(img);
                                rmp.setAvailable(available);
                                rmp.setObj(result.toString());

                                if (active_status.equalsIgnoreCase("1")) {
                                    rmpArrayList.add(rmp);
                                }


                            }
                            if (rmpArrayList.size() > 0) {
                                adapter = new RMPDoctorsApdapter(mContext, rmpArrayList);
                                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                                manager.setOrientation(RecyclerView.VERTICAL);
                                rv_Rmpdoctor.setLayoutManager(manager);
                                rv_Rmpdoctor.setAdapter(adapter);
                            } else {
                                et_Search.setVisibility(View.GONE);
                                rv_Rmpdoctor.setVisibility(View.GONE);
                                tv_Notavailable.setVisibility(View.VISIBLE);
                            }

                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Rmpdoctor.setVisibility(View.GONE);
                            tv_Notavailable.setVisibility(View.VISIBLE);
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

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rv_Rmpdoctor = findViewById(R.id.rv_Rmpdoctor);
        ll_Login = findViewById(R.id.ll_Login);
        et_Search = findViewById(R.id.et_Search);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        ll_Login.setOnClickListener(this);

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

            case R.id.ll_Login:
                startActivity(new Intent(mContext, RMPLoginActivity.class));
                Animatoo.animateCard(mContext);
                break;
        }
    }
}