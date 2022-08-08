package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
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

import com.ambitious.fghdoctor.Adapters.BloodListAdpter;
import com.ambitious.fghdoctor.Model.BloodDonor;
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

public class BloodListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Head, tv_Donor, tv_Banks, tv_Notavailable;
    private RecyclerView rv_Blood;
    private BloodListAdpter adpter;
    private LinearLayout ll_Login;
    private EditText et_Search;
    private RelativeLayout rl_Loader;
    private ArrayList<BloodDonor> bloodDonors;
    public String type, wallet = "", donated = "";
    private String latitude="",longitude="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blood_list);
        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));
//        setData("donor");
        type = "donor";
        wallet = getIntent().getStringExtra("wallet");
        donated = getIntent().getStringExtra("donated");

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            if (type.equalsIgnoreCase("donor")) {
                tv_Donor.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Banks.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Donor.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Banks.setTextColor(Color.parseColor("#666666"));
            } else {
                tv_Banks.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Donor.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Banks.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Donor.setTextColor(Color.parseColor("#666666"));
            }
            getUsers(uid, type, iv_Bck, latitude, longitude);
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

                if (bloodDonors.size() > 0) {
                    if (type.equalsIgnoreCase("donor")) {
                        filter(s.toString());
                    } else {
                        filter1(s.toString());
                    }
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, type + " is not available.");
                }

            }
        });
    }

    private void filter1(String text) {

        ArrayList<BloodDonor> temp = new ArrayList();
        for (BloodDonor d : bloodDonors) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        //update recyclerview
        if (temp.size() > 0) {
            rv_Blood.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adpter.updateList(temp);
        } else {
            rv_Blood.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void filter(String text) {

        ArrayList<BloodDonor> temp = new ArrayList();
        for (BloodDonor d : bloodDonors) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getGroup().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Blood.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adpter.updateList(temp);
        } else {
            rv_Blood.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void getUsers(String uid, final String type, final ImageView view, String latitude, String longitude) {

        latitude = Utility.getSharedPreferences(mContext, "latitude");
        longitude = Utility.getSharedPreferences(mContext, "longitude");

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
                            rv_Blood.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            bloodDonors = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String user_image = result.optString("user_image");
                                String email = result.optString("email");
                                String address = result.optString("address");
                                String password = result.optString("password");
                                String mobile = result.optString("mobile");
                                String rating = result.optString("rating");
                                String user_type = result.optString("user_type");
                                String available = result.optString("available");
                                String active_status = result.optString("status");
                                String blood_group = result.optString("blood_group");


                                BloodDonor donor = new BloodDonor();
                                donor.setId(user_id);
                                donor.setName(name);
                                donor.setAddress(address);
                                donor.setRating(rating);
                                donor.setUser_image(user_image);
                                donor.setAvailable(available);
                                donor.setObj(result.toString());
                                donor.setGroup(blood_group);

                                if (active_status.equalsIgnoreCase("1")) {
                                    bloodDonors.add(donor);
                                }

                            }

                            if (bloodDonors.size() > 0) {
                                adpter = new BloodListAdpter(mContext, type, bloodDonors);
                                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                                manager.setOrientation(RecyclerView.VERTICAL);
                                rv_Blood.setLayoutManager(manager);
                                rv_Blood.setAdapter(adpter);
                            } else {
                                et_Search.setVisibility(View.GONE);
                                rv_Blood.setVisibility(View.GONE);
                                tv_Notavailable.setVisibility(View.VISIBLE);
                            }


                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Blood.setVisibility(View.GONE);
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
        tv_Donor = findViewById(R.id.tv_Donor);
        tv_Banks = findViewById(R.id.tv_Banks);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rv_Blood = findViewById(R.id.rv_Blood);
        ll_Login = findViewById(R.id.ll_Login);
        et_Search = findViewById(R.id.et_Search);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        tv_Donor.setOnClickListener(this);
        tv_Banks.setOnClickListener(this);
        ll_Login.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.tv_Banks:
                type = "bank";
                tv_Banks.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Donor.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Banks.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Donor.setTextColor(Color.parseColor("#666666"));
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");

                    getUsers(uid, type, iv_Bck,latitude,longitude);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.tv_Donor:
                type = "donor";
                tv_Donor.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Banks.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Donor.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Banks.setTextColor(Color.parseColor("#666666"));
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    getUsers(uid, type, iv_Bck, latitude, longitude);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.ll_Login:
                if (type.equalsIgnoreCase("donor")) {
                    startActivity(new Intent(mContext, DonorLoginActivity.class));
                    Animatoo.animateCard(mContext);
                } else {
                    startActivity(new Intent(mContext, BloodBankLoginActivity.class));
                    Animatoo.animateCard(mContext);
                }
                break;
        }

    }

    private void setData(String type) {

        if (type.equalsIgnoreCase("Banks")) {
            tv_Banks.setBackgroundColor(Color.parseColor("#0D96DC"));
            tv_Donor.setBackgroundColor(Color.parseColor("#E2E5E7"));
            tv_Banks.setTextColor(Color.parseColor("#FFFFFF"));
            tv_Donor.setTextColor(Color.parseColor("#666666"));

            adpter = new BloodListAdpter(mContext, type);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            rv_Blood.setLayoutManager(manager);
            rv_Blood.setAdapter(adpter);

        } else {
            tv_Donor.setBackgroundColor(Color.parseColor("#0D96DC"));
            tv_Banks.setBackgroundColor(Color.parseColor("#E2E5E7"));
            tv_Donor.setTextColor(Color.parseColor("#FFFFFF"));
            tv_Banks.setTextColor(Color.parseColor("#666666"));

            adpter = new BloodListAdpter(mContext, type);
            LinearLayoutManager manager = new LinearLayoutManager(mContext);
            manager.setOrientation(RecyclerView.VERTICAL);
            rv_Blood.setLayoutManager(manager);
            rv_Blood.setAdapter(adpter);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}
