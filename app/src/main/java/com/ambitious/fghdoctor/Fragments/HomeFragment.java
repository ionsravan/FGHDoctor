package com.ambitious.fghdoctor.Fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Activities.AmbulanceListActivity;
import com.ambitious.fghdoctor.Activities.BloodListActivity;
import com.ambitious.fghdoctor.Activities.CovidServicesActivity;
import com.ambitious.fghdoctor.Activities.CovidWinnersActivity;
import com.ambitious.fghdoctor.Activities.LabListActivity;
import com.ambitious.fghdoctor.Activities.LoginActivity;
import com.ambitious.fghdoctor.Activities.MarketPricesActivity;
import com.ambitious.fghdoctor.Activities.MedicalShopListActivity;
import com.ambitious.fghdoctor.Activities.NewAppointmentCategoryListActivity;
import com.ambitious.fghdoctor.Activities.RMPDoctorListActivity;
import com.ambitious.fghdoctor.Activities.ReferActivity;
import com.ambitious.fghdoctor.Activities.ReportsActivity;
import com.ambitious.fghdoctor.Activities.VaterinaryDoctorListActivity;
import com.ambitious.fghdoctor.Activities.VehiclesActivity;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AViewFlipper;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.Date;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View mView;
    private RelativeLayout rl_Ambulance, rl_Blood, rl_Medical, rl_Rmp, rl_Veternary, rl_Labs, rl_Reports, rl_Telemedicine, rl_Vaccine, rl_Covidservice, rl_MarketPrices, rl_Refer, rl_Fitness, rl_Vehicles, rl_Loader;
    private TextView tv_Bookappoinment;
    private AViewFlipper vf;
    private String code = "", wallet = "", donated = "", city = "", lat = "", lon = "";

    public HomeFragment(String codee, String wallets, String donate, String city, String lat, String lon) {
        this.code = codee;
        this.wallet = wallets;
        this.donated = donate;
        this.city = city;
        this.lat = lat;
        this.lon = lon;
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        finds();
        if (Utility.isNetworkConnected(getContext())) {
            String m_androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.e("m_androidId", "" + m_androidId);
            Log.e("city", "" + city);
            Log.e("lat", "" + lat);
            Log.e("lon", "" + lon);
            String register_id = FirebaseInstanceId.getInstance().getToken();
            updateRegisteId(m_androidId, register_id, city, lat, lon);
        } else {
            AlertConnection.showAlertDialog(getContext(), "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        return mView;
    }

    private void getBanner(String uid, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getBanner();
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

                            JSONArray array = object.optJSONArray("result");

                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String banner_image = result.optString("banner_image");

                                ImageView image = new ImageView(getContext());
                                image.setScaleType(ImageView.ScaleType.FIT_XY);
                                Glide.with(getContext()).load(banner_image).into(image);
                                vf.addView(image);

                            }

                        } else {
                            CustomSnakbar.showDarkSnakabar(getContext(), view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(getContext(), view, "Profile " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateRegisteId(String Device_id, String register_id, String city, String lat, String lng) {

        Call<ResponseBody> call = AppConfig.loadInterface().updateRegisteId(Device_id, register_id, city, lat, lng);
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
                        System.out.println("updateRegisteId" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");

                            Log.e("updateRegisteId=>", "Successful");
                            String uid = Utility.getSharedPreferences(getContext(), "u_id");
                            Utility.setSharedPreference(getContext(), "noti_u_id", user_id);
                            getBanner(uid, mView);

                        } else {
                            CustomSnakbar.showDarkSnakabar(getContext(), mView, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(getContext(), mView, "updateRegisteId " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Toast.makeText(getContext(), "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void finds() {

        rl_Ambulance = mView.findViewById(R.id.rl_Ambulance);
        rl_Blood = mView.findViewById(R.id.rl_Blood);
        rl_Medical = mView.findViewById(R.id.rl_Medical);
        rl_Rmp = mView.findViewById(R.id.rl_Rmp);
        rl_Veternary = mView.findViewById(R.id.rl_Veternary);
        rl_Labs = mView.findViewById(R.id.rl_Labs);
        rl_Reports = mView.findViewById(R.id.rl_Reports);
        rl_Telemedicine = mView.findViewById(R.id.rl_Telemedicine);
        rl_Vaccine = mView.findViewById(R.id.rl_Vaccine);
        rl_Covidservice = mView.findViewById(R.id.rl_Covidservice);
        rl_MarketPrices = mView.findViewById(R.id.rl_MarketPrices);
        rl_Refer = mView.findViewById(R.id.rl_Refer);
        rl_Fitness = mView.findViewById(R.id.rl_Fitness);
        rl_Vehicles = mView.findViewById(R.id.rl_Vehicles);
        rl_Loader = mView.findViewById(R.id.rl_Loader);
        tv_Bookappoinment = mView.findViewById(R.id.tv_Bookappoinment);
        vf = mView.findViewById(R.id.view_flipper);


        rl_Ambulance.setOnClickListener(this);
        rl_Blood.setOnClickListener(this);
        rl_Medical.setOnClickListener(this);
        rl_Rmp.setOnClickListener(this);
        rl_Veternary.setOnClickListener(this);
        rl_Labs.setOnClickListener(this);
        rl_Reports.setOnClickListener(this);
        rl_Telemedicine.setOnClickListener(this);
        rl_Vaccine.setOnClickListener(this);
        rl_Covidservice.setOnClickListener(this);
        rl_MarketPrices.setOnClickListener(this);
        rl_Refer.setOnClickListener(this);
        rl_Fitness.setOnClickListener(this);
        rl_Vehicles.setOnClickListener(this);
        tv_Bookappoinment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.rl_Ambulance:
                startActivity(new Intent(getContext(), AmbulanceListActivity.class)
                        .putExtra("head", "Ambulance"));
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Blood:
                startActivity(new Intent(getContext(), BloodListActivity.class)
                        .putExtra("head", "Blood")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Medical:
                startActivity(new Intent(getContext(), MedicalShopListActivity.class)
                        .putExtra("head", "Medical Shop")
                        .putExtra("donated", ""+donated)
                        .putExtra("wallet", "" + wallet)
                );
                Animatoo.animateCard(getContext());
                break;

            case R.id.tv_Bookappoinment:
//                startActivity(new Intent(getContext(), AppointmentCategoryListActivity.class)
                startActivity(new Intent(getContext(), NewAppointmentCategoryListActivity.class)
                        .putExtra("head", "Book Appointment")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Rmp:
                startActivity(new Intent(getContext(), RMPDoctorListActivity.class)
                        .putExtra("head", "RMP Doctors")
                        .putExtra("wallet", "" + wallet)
                );
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Veternary:
                startActivity(new Intent(getContext(), VaterinaryDoctorListActivity.class)
                        .putExtra("head", "Vaterinary Doctors"));
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Reports:
                startActivity(new Intent(getContext(), ReportsActivity.class)
                        .putExtra("head", "Reports"));
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Telemedicine:
                CustomSnakbar.showDarkSnakabar(getContext(), v, "Coming Soon..");
                break;


            case R.id.rl_Covidservice:
                startActivity(new Intent(getContext(), CovidServicesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_MarketPrices:
                startActivity(new Intent(getContext(), MarketPricesActivity.class)
                        .putExtra("head", "Market Prices")
                        .putExtra("donated", ""+donated)
                        .putExtra("wallet", "" + wallet)
                );
                break;

            case R.id.rl_Refer:
                if (Utility.getSharedPreferencesBoolean(getContext(), "islogin", false)) {
                    startActivity(new Intent(getContext(), ReferActivity.class)
                            .putExtra("head", "Refer & Earn")
                            .putExtra("code", "" + code));
                    Animatoo.animateCard(getContext());
                } else {
                    CustomSnakbar.showDarkSnakabar(getContext(), v, "Please Login/Register Before Refer & Earn!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getContext(), LoginActivity.class)
                                    .putExtra("head", "Blood"));
                            Animatoo.animateCard(getContext());
                        }
                    }, 1500);
                }
                break;

            case R.id.rl_Labs:
                startActivity(new Intent(getContext(), LabListActivity.class)
                        .putExtra("head", "Labs")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Fitness:
                startActivity(new Intent(getContext(), CovidWinnersActivity.class));
                Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Vehicles:
                startActivity(new Intent(getContext(), VehiclesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(getContext());
                break;

        }

    }
}
