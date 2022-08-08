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
import com.ambitious.fghdoctor.Adapters.OxygenbankListAdpter;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.OxygenBank;
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

public class OxygenBankListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private LinearLayout ll_Login;
    private TextView tv_Free, tv_Paid, tv_Notavailable;
    private EditText et_Search;
    private RelativeLayout rl_Loader;
    private RecyclerView rv_Oxygenbanks;
    private OxygenbankListAdpter adpter;
    private ArrayList<OxygenBank> oxygenBankArrayList;
    private String type;
    private String city = "", lat = "", lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxygen_bank_list);
        finds();

        city = getIntent().getStringExtra("city");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lon");

        type = "oxygen free";

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            if (type.equalsIgnoreCase("oxygen free")) {
                tv_Free.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Paid.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Free.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Paid.setTextColor(Color.parseColor("#666666"));
            } else {
                tv_Free.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Paid.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Free.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Paid.setTextColor(Color.parseColor("#666666"));
            }
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

                if (oxygenBankArrayList.size() > 0) {
                    if (type.equalsIgnoreCase("donor")) {
                        filter(s.toString());
                    } else {
                        filter1(s.toString());
                    }
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Not available.");
                }

            }
        });
    }

    private void filter1(String text) {

        ArrayList<OxygenBank> temp = new ArrayList();
        for (OxygenBank d : oxygenBankArrayList) {
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
            rv_Oxygenbanks.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adpter.updateList(temp);
        } else {
            rv_Oxygenbanks.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void filter(String text) {

        ArrayList<OxygenBank> temp = new ArrayList();
        for (OxygenBank d : oxygenBankArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Oxygenbanks.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adpter.updateList(temp);
        } else {
            rv_Oxygenbanks.setVisibility(View.GONE);
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
                        System.out.println("OxygenBanks" + object);

                        if (status.equalsIgnoreCase("1")) {

                            et_Search.setVisibility(View.VISIBLE);
                            rv_Oxygenbanks.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            oxygenBankArrayList = new ArrayList<>();
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
                                String blood_group = result.optString("blood_group");


                                OxygenBank donor = new OxygenBank();
                                donor.setId(user_id);
                                donor.setName(name);
                                donor.setAddress(address);
                                donor.setRating(rating);
                                donor.setUser_image(user_image);
                                donor.setAvailable(available);
                                donor.setObj(result.toString());

                                oxygenBankArrayList.add(donor);


                            }


                            adpter = new OxygenbankListAdpter(mContext, oxygenBankArrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Oxygenbanks.setLayoutManager(manager);
                            rv_Oxygenbanks.setAdapter(adpter);


                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Oxygenbanks.setVisibility(View.GONE);
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
        ll_Login = findViewById(R.id.ll_Login);
        tv_Free = findViewById(R.id.tv_Free);
        tv_Paid = findViewById(R.id.tv_Paid);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        et_Search = findViewById(R.id.et_Search);
        rv_Oxygenbanks = findViewById(R.id.rv_Oxygenbanks);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        tv_Paid.setOnClickListener(this);
        tv_Free.setOnClickListener(this);
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
                startActivity(new Intent(mContext, OxygenBankLoginActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.tv_Free:
                type = "oxygen free";
                tv_Free.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Paid.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Free.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Paid.setTextColor(Color.parseColor("#666666"));
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    getUsers(uid, type, iv_Bck);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.tv_Paid:
                type = "oxygen paid";
                tv_Paid.setBackgroundColor(Color.parseColor("#0D96DC"));
                tv_Free.setBackgroundColor(Color.parseColor("#E2E5E7"));
                tv_Paid.setTextColor(Color.parseColor("#FFFFFF"));
                tv_Free.setTextColor(Color.parseColor("#666666"));
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    getUsers(uid, type, iv_Bck);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

        }
    }
}