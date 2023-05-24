package com.ambitious.fghdoctor.Fragments;

import static android.content.Context.MODE_PRIVATE;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Activities.AmbulanceListActivity;
import com.ambitious.fghdoctor.Activities.BloodListActivity;
import com.ambitious.fghdoctor.Activities.CovidServicesActivity;
import com.ambitious.fghdoctor.Activities.CovidWinnersActivity;
import com.ambitious.fghdoctor.Activities.DeliveryBoyActivity;
import com.ambitious.fghdoctor.Activities.HealthActivity;
import com.ambitious.fghdoctor.Activities.HomeActivity;
import com.ambitious.fghdoctor.Activities.LabListActivity;
import com.ambitious.fghdoctor.Activities.LoginActivity;
import com.ambitious.fghdoctor.Activities.MarketPricesActivity;
import com.ambitious.fghdoctor.Activities.MedicalShopListActivity;
import com.ambitious.fghdoctor.Activities.NewAppointmentCategoryListActivity;
import com.ambitious.fghdoctor.Activities.NotificationActivity;
import com.ambitious.fghdoctor.Activities.RMPDoctorListActivity;
import com.ambitious.fghdoctor.Activities.RechargeActivity;
import com.ambitious.fghdoctor.Activities.ReferActivity;
import com.ambitious.fghdoctor.Activities.ReportsActivity;
import com.ambitious.fghdoctor.Activities.SignupActivity;
import com.ambitious.fghdoctor.Activities.VaterinaryDoctorListActivity;
import com.ambitious.fghdoctor.Activities.VehiclesActivity;
import com.ambitious.fghdoctor.Activities.WithDrawActivity;
import com.ambitious.fghdoctor.Adapters.RecyclerViewAdapter;
import com.ambitious.fghdoctor.Model.RecyclerData;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AViewFlipper;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.goodiebag.pinview.Pinview;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment implements View.OnClickListener {

    private View mView;
    RecyclerView recyclerList;
    private ArrayList<RecyclerData> recyclerDataArrayList;
    NestedScrollView scrollView;
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
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //  RateItDialogFragment.show(getContext(), getFragmentManager());

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);
        finds();
        SharedPreferences sharedPreferences = Objects.requireNonNull(getContext()).getSharedPreferences("isDialog", MODE_PRIVATE);
        // boolean isDialog = sharedPreferences.getBoolean("isDialog", true);
        Long duration = sharedPreferences.getLong("duration", System.currentTimeMillis() - TimeUnit.DAYS.toMillis(111111111));
        if (System.currentTimeMillis() - duration > TimeUnit.DAYS.toMillis(100000000)) {
            // inflateDialog is a function containing the functionality of popping up the dialog
            fullScreenDialog();
        }


        // created new array list..
        recyclerDataArrayList = new ArrayList<>();

        // added data to array list
        recyclerDataArrayList.add(new RecyclerData("media city", R.drawable.mediacity));
        recyclerDataArrayList.add(new RecyclerData("upi & recharge", R.drawable.upi_recharge));
        recyclerDataArrayList.add(new RecyclerData("Market Prices", R.drawable.markert_prices));

       /* recyclerDataArrayList.add(new RecyclerData("Recharges", R.drawable.recharge_icon));
        recyclerDataArrayList.add(new RecyclerData("Market Prices", R.drawable.markert_prices));
        recyclerDataArrayList.add(new RecyclerData("Delivery Boys", R.drawable.delivery_boys_icon));
*/

        recyclerDataArrayList.add(new RecyclerData("health", R.drawable.hospital));
        recyclerDataArrayList.add(new RecyclerData("sharing vehicles", R.drawable.carblue));
        recyclerDataArrayList.add(new RecyclerData("delivery items", R.drawable.delivery_items));

       /* recyclerDataArrayList.add(new RecyclerData("Short News", R.drawable.short_news));
        recyclerDataArrayList.add(new RecyclerData("Vehicles", R.drawable.carblue));
        recyclerDataArrayList.add(new RecyclerData("Blood", R.drawable.bloodhome));*/

        recyclerDataArrayList.add(new RecyclerData("earnings", R.drawable.earnings));
        recyclerDataArrayList.add(new RecyclerData("lot", R.drawable.lot));
        recyclerDataArrayList.add(new RecyclerData("Saadi", R.drawable.saadi));

       /* recyclerDataArrayList.add(new RecyclerData("Medical Shop", R.drawable.medicalhome));
        recyclerDataArrayList.add(new RecyclerData("RMP", R.drawable.rmp));
        recyclerDataArrayList.add(new RecyclerData("Veterinary", R.drawable.vetreneri));
*/
        recyclerDataArrayList.add(new RecyclerData("agriculture", R.drawable.agriculture));
        recyclerDataArrayList.add(new RecyclerData("education", R.drawable.education));
        recyclerDataArrayList.add(new RecyclerData("make faction", R.drawable.make_faction));

       /* recyclerDataArrayList.add(new RecyclerData("Ambulance", R.drawable.ambulancehome));
        recyclerDataArrayList.add(new RecyclerData("Reports", R.drawable.hospital));
        //  recyclerDataArrayList.add(new RecyclerData("Fitness", R.drawable.fitness));
*/

        recyclerDataArrayList.add(new RecyclerData("youth festivals", R.drawable.youth_festival));
        recyclerDataArrayList.add(new RecyclerData("jobs", R.drawable.jobs));
        recyclerDataArrayList.add(new RecyclerData("fgh foundation", R.drawable.fgh_foundation));


       /* recyclerDataArrayList.add(new RecyclerData("Labs", R.drawable.lab));
        recyclerDataArrayList.add(new RecyclerData("Covid Services", R.drawable.covidservice));
        recyclerDataArrayList.add(new RecyclerData("Courses Hub", R.drawable.courses_hub));
        recyclerDataArrayList.add(new RecyclerData("Jobs", R.drawable.jobs));
*/
        // recyclerDataArrayList.add(new RecyclerData("Refer & Earn", R.drawable.sharing));


        // added data from arraylist to adapter class.
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(recyclerDataArrayList, requireContext());

        /*// setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 3);

        // at last set adapter to recycler view.
        recyclerList.setLayoutManager(layoutManager);*/
        recyclerList.setAdapter(adapter);
        adapter.setOnItemClickListener((position, v) -> {
            Log.d("TAG", "onItemClick position: " + recyclerDataArrayList.get(position).getTitle());

            if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("upi & recharge")) {
               /* if (Utility.getSharedPreferencesBoolean(Objects.requireNonNull(getContext()), "islogin", false)) {
                    if (Utility.getSharedPreferences(getContext(), "user_type").equalsIgnoreCase("user")) {
                        startActivity(new Intent(getContext(), RechargeActivity.class)
                                .putExtra("donated", "" + donated)
                                .putExtra("wallet", wallet)
                                .putExtra("FROM", "fragment"));
                    }
                }*/

                CustomSnakbar.showSnakabar(Objects.requireNonNull(getContext()), v, "Coming Soon...");

            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("health")) {

                startActivity(new Intent(getContext(), HealthActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));

            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Short News")) {
                startActivity(new Intent(getContext(), NotificationActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));

            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Market Prices")) {

                startActivity(new Intent(getContext(), MarketPricesActivity.class)
                        .putExtra("head", "Market Prices")
                        .putExtra("donated", "" + donated)
                        .putExtra("wallet", "" + wallet)
                );
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Fitness")) {
                startActivity(new Intent(getContext(), CovidWinnersActivity.class));
                //  Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Vehicles")) {
                startActivity(new Intent(getContext(), VehiclesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));
                //  Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Delivery items")) {
                startActivity(new Intent(getContext(), DeliveryBoyActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated));
                // Animatoo.animateCard(mContext);
            }else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("sharing vehicles")) {
                startActivity(new Intent(getContext(), VehiclesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated));
                // Animatoo.animateCard(mContext);
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Courses Hub")) {
                Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Jobs")) {
                Toast.makeText(getContext(), "Coming Soon...", Toast.LENGTH_SHORT).show();
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Refer & Earn")) {
                if (Utility.getSharedPreferencesBoolean(getContext(), "islogin", false)) {
                    startActivity(new Intent(getContext(), ReferActivity.class)
                            .putExtra("head", "Refer & Earn")
                            .putExtra("code", "" + code));
                    //  Animatoo.animateCard(getContext());
                } else {
                    CustomSnakbar.showDarkSnakabar(getContext(), v, "Please Login/Register Before Refer & Earn!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getContext(), LoginActivity.class)
                                    .putExtra("head", "Blood"));
                            //  Animatoo.animateCard(getContext());
                        }
                    }, 1500);
                }
            }

        });

        if (Utility.isNetworkConnected(getContext())) {
            String m_androidId = Settings.Secure.getString(getContext().getContentResolver(), Settings.Secure.ANDROID_ID);
            Log.e("m_androidId", "" + m_androidId);
            Log.e("city", "" + city);
            Log.e("lat", "" + lat);
            Log.e("lon", "" + lon);
            // String register_id = FirebaseInstanceId.getInstance().getToken();
            String register_id = Utility.getSharedPreferences(requireContext(), "regId");
            updateRegisteId(m_androidId, register_id, city, lat, lon);
        } else {
            AlertConnection.showAlertDialog(getContext(), "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        return mView;
    }


    private void fullScreenDialog() {
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(requireContext(), android.R.style.Theme_Translucent_NoTitleBar);
        View view = getLayoutInflater().inflate(R.layout.image_alert, null);

        mBuilder.setView(view);
        AlertDialog dialog = mBuilder.create();
        dialog.show();

        ImageView btnClose = view.findViewById(R.id.btnClose);
        btnClose.setOnClickListener(v -> {
            dialog.dismiss();

            isDialog();
        });

    }

    private void isDialog() {
        // Storing data into SharedPreferences


        SharedPreferences sharedPreferences = getContext().getSharedPreferences("isDialog", MODE_PRIVATE);
        SharedPreferences.Editor myEdit = sharedPreferences.edit();
        // myEdit.putBoolean("isDialog", false);
        myEdit.putLong("duration", System.currentTimeMillis());
        myEdit.apply();
    }


    private void getBanner(String uid, final View view) {

        // rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getBanner();
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                // rl_Loader.setVisibility(View.GONE);
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
                                vf.setFlipInterval(4000);
                                vf.setAutoStart(true);
                               /* vf.setInAnimation(requireContext(), android.R.anim.slide_in_left);
                                vf.setOutAnimation(requireContext(), android.R.anim.slide_out_right);*/

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
                //  rl_Loader.setVisibility(View.GONE);
                Toast.makeText(getContext(), "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateRegisteId(String Device_id, String register_id, String city, String lat, String lng) {

        Call<ResponseBody> call = AppConfig.loadInterface().updateRegisteId(Device_id, register_id, city, lat, lng);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                //  rl_Loader.setVisibility(View.GONE);
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
       /* rl_Ambulance = mView.findViewById(R.id.rl_Ambulance);
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
        rl_Vehicles = mView.findViewById(R.id.rl_Vehicles);*/
        //   rl_Loader = mView.findViewById(R.id.rl_Loader);
        recyclerList = mView.findViewById(R.id.recyclerList);
        tv_Bookappoinment = mView.findViewById(R.id.tv_Bookappoinment);
        vf = mView.findViewById(R.id.view_flipper);



/*        rl_Ambulance.setOnClickListener(this);
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
        rl_Vehicles.setOnClickListener(this);*/
        tv_Bookappoinment.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

           /* case R.id.rl_Ambulance:
                startActivity(new Intent(getContext(), AmbulanceListActivity.class)
                        .putExtra("head", "Ambulance"));
               // Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Blood:
                startActivity(new Intent(getContext(), BloodListActivity.class)
                        .putExtra("head", "Blood")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
               // Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Medical:
                startActivity(new Intent(getContext(), MedicalShopListActivity.class)
                        .putExtra("head", "Medical Shop")
                        .putExtra("donated", ""+donated)
                        .putExtra("wallet", "" + wallet)
                );
              //  Animatoo.animateCard(getContext());
                break;*/

            case R.id.tv_Bookappoinment:
//                startActivity(new Intent(getContext(), AppointmentCategoryListActivity.class)
                startActivity(new Intent(getContext(), NewAppointmentCategoryListActivity.class)
                        .putExtra("head", "Book Appointment")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                // Animatoo.animateCard(getContext());
                break;

           /* case R.id.rl_Rmp:
                startActivity(new Intent(getContext(), RMPDoctorListActivity.class)
                        .putExtra("head", "RMP Doctors")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
               // Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Veternary:
                startActivity(new Intent(getContext(), VaterinaryDoctorListActivity.class)
                        .putExtra("head", "Vaterinary Doctors")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated));
               // Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Reports:
                startActivity(new Intent(getContext(), ReportsActivity.class)
                        .putExtra("head", "Reports"));
               // Animatoo.animateCard(getContext());
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
               // Animatoo.animateCard(getContext());
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
                  //  Animatoo.animateCard(getContext());
                } else {
                    CustomSnakbar.showDarkSnakabar(getContext(), v, "Please Login/Register Before Refer & Earn!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(getContext(), LoginActivity.class)
                                    .putExtra("head", "Blood"));
                          //  Animatoo.animateCard(getContext());
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
               // Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Fitness:
                startActivity(new Intent(getContext(), CovidWinnersActivity.class));
               // Animatoo.animateCard(getContext());
                break;

            case R.id.rl_Vehicles:
                startActivity(new Intent(getContext(), VehiclesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));
               // Animatoo.animateCard(getContext());
                break;*/

        }

    }
}
