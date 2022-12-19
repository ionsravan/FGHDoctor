package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.BloodListAdpter;
import com.ambitious.fghdoctor.Adapters.NotificationAdapter;
import com.ambitious.fghdoctor.Adapters.ViewPager2Adapter;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.Noti;
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

public class NotificationActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Notavailable;
    private RelativeLayout rl_Loader;
    private RecyclerView rv_Notifications;
    private ArrayList<Noti> notis;
    private NotificationAdapter adapter;
    // Create object of ViewPager2
    private ViewPager2 viewPager2;
    private String city = "", lat = "", lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        finds();

        if (getIntent().getExtras() != null) {

            city = getIntent().getStringExtra("city");
            lat = getIntent().getStringExtra("lat");
            lon = getIntent().getStringExtra("lon");

            if (Utility.isNetworkConnected(mContext)) {
                String uid = Utility.getSharedPreferences(mContext, "noti_u_id");



                getNotifications(uid, city, lat, lon, iv_Bck);
            } else {
                AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                        "You don't have internet connection.", false);
            }

        }


    }

    private void getNotifications(String uid, String city, String lat, String lon, ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getNotifications(uid, city, lat, lon);
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
                        System.out.println("Notifications" + object);

                        if (status.equalsIgnoreCase("1")) {

                            tv_Notavailable.setVisibility(View.GONE);
                            JSONArray array = object.optJSONArray("result");
                            notis = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String id = result.optString("notification_id");
                                String title = result.optString("title");
                                String msg = result.optString("message");
                                String image = result.optString("image");
                                String time_Ago = result.optString("time_ago");


                                Noti noti = new Noti(id, title, msg, image, time_Ago);
                                notis.add(noti);

                            }


                           /* adapter = new NotificationAdapter(mContext, notis);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Notifications.setLayoutManager(manager);
                            rv_Notifications.setAdapter(adapter);*/

                            ViewPager2Adapter viewPager2Adapter = new ViewPager2Adapter(mContext,notis);

                            // adding the adapter to viewPager2
                            // to show the views in recyclerview
                            viewPager2.setAdapter(viewPager2Adapter);

                            // To get swipe event of viewpager2
                            viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
                                @Override
                                // This method is triggered when there is any scrolling activity for the current page
                                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                                    super.onPageScrolled(position, positionOffset, positionOffsetPixels);
                                }

                                // triggered when you select a new page
                                @Override
                                public void onPageSelected(int position) {
                                    super.onPageSelected(position);
                                }

                                // triggered when there is
                                // scroll state will be changed
                                @Override
                                public void onPageScrollStateChanged(int state) {
                                    super.onPageScrollStateChanged(state);
                                }
                            });


                        } else {
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
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rl_Loader = findViewById(R.id.rl_Loader);
        //rv_Notifications = findViewById(R.id.rv_Notifications);
        viewPager2 = findViewById(R.id.viewpager);

        iv_Bck.setOnClickListener(this);

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

        }

    }
}