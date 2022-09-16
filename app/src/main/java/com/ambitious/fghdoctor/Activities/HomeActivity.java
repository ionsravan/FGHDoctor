package com.ambitious.fghdoctor.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.location.Address;
import android.location.Geocoder;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.CustomMenuAdapter;
import com.ambitious.fghdoctor.Adapters.MedicalShopListAdapter;
import com.ambitious.fghdoctor.Fragments.CallSupportFragment;
import com.ambitious.fghdoctor.Fragments.HomeFragment;
import com.ambitious.fghdoctor.Fragments.MembershipFragment;
import com.ambitious.fghdoctor.Fragments.MyAppointmentFragment;
import com.ambitious.fghdoctor.Fragments.SettingsFragment;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CircleImageView;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.GPSTracker;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

import nl.psdcompany.duonavigationdrawer.views.DuoDrawerLayout;
import nl.psdcompany.duonavigationdrawer.views.DuoMenuView;
import nl.psdcompany.duonavigationdrawer.widgets.DuoDrawerToggle;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class HomeActivity extends AppCompatActivity implements DuoMenuView.OnMenuClickListener, View.OnClickListener {

    private Context mContext = this;
    private CustomMenuAdapter mMenuAdapter;
    private ViewHolder mViewHolder;
    private ImageView iv_Menu, iv_Profile, iv_QrCode;
    private TextView tv_Head, tv_Name, tv_NotiCount, tv_City;
    private CircleImageView civ_User;
    private RelativeLayout rl_Loader, rl_Notisel;
    private LinearLayout ll_Notification, ll_Add;
    private ArrayList<String> mTitles = new ArrayList<>();
    private ArrayList<Drawable> mIcons = new ArrayList<>();
    public String user_id = "", code = "", donated = "", wallet = "", city = "";
    public boolean isAgain = false;
    public double latitude = 0.0, longitude = 0.0;

    String[] cities = {"India", "USA", "China", "Japan", "Other"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        finds();

        /*city = Utility.getSharedPreferences(mContext,"u_city");
        tv_City.setText(city);
        tv_City.setOnClickListener(v -> {
            cityListDilog();
        });*/
        getCurrentLocation();
      //  fcmToken();

        Log.d("TAG", "onCreate: "+Utility.getSharedPreferences(this,"regId"));

    }



    private void cityListDilog() {
        Dialog dialog = new Dialog(HomeActivity.this);
        dialog.setContentView(R.layout.city_list);
        dialog.setCancelable(true);
        dialog.setTitle("Select City");
        dialog.show();

        ListView lv = dialog.findViewById(R.id.lv);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, cities);
        lv.setAdapter(adapter);
        lv.setOnItemClickListener((parent, view, position, id) -> {
            tv_City.setText(adapter.getItem(position));
            city = adapter.getItem(position).toString();
            Log.d("TAG", "cityListDilog: " + adapter.getItem(position));
            Utility.setSharedPreference(mContext, "u_city", city);

            dialog.dismiss();
        });


    }

    private void getCurrentLocation() {

        GPSTracker track = new GPSTracker(mContext);
        if (track.canGetLocation()) {
            isAgain = false;
            latitude = track.getLatitude();
            longitude = track.getLongitude();

            Utility.setSharedPreference(mContext, "latitude", String.valueOf(latitude));
            Utility.setSharedPreference(mContext, "longitude", String.valueOf(longitude));

            if (latitude == 0.0 & longitude == 0.0) {
                getCurrentLocation();
            } else {
                getAddress();
            }
        } else {
            isAgain = true;

            Dialog dialog = new Dialog(mContext);
            dialog.setContentView(R.layout.dialog_gps);
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

            TextView tv_Settings = dialog.findViewById(R.id.tv_Settings);
            TextView tv_Cancel = dialog.findViewById(R.id.tv_Cancel);

            tv_Settings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });

            tv_Cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.cancel();
                }
            });


        }
    }

    private void getAddress() {

        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            if (addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
                city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName(); // Only if available else return NULL

                Log.e("City=>", "" + city);
                tv_City.setText(city + ",India");
                if (Utility.isNetworkConnected(mContext)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    getProfile(uid, iv_Menu);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
            } else {
                Log.e("AddressListSize=>", "" + addresses.size());
                getCurrentLocation();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void finds() {
        rl_Notisel = findViewById(R.id.rl_Notisel);
        ll_Notification = findViewById(R.id.ll_Notification);
        ll_Add = findViewById(R.id.ll_Add);
        rl_Loader = findViewById(R.id.rl_Loader);
        iv_Menu = findViewById(R.id.iv_Menu);
        iv_Profile = findViewById(R.id.iv_Profile);
        iv_QrCode = findViewById(R.id.iv_QrCode);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Name = findViewById(R.id.tv_Name);
        tv_NotiCount = findViewById(R.id.tv_NotiCount);
        tv_City = findViewById(R.id.tv_City);
        civ_User = findViewById(R.id.civ_User);

        iv_Menu.setOnClickListener(this);
        iv_Profile.setOnClickListener(this);
        iv_QrCode.setOnClickListener(this);
        rl_Notisel.setOnClickListener(this);
        ll_Add.setOnClickListener(this);
    }

    private void handleMenu() {

        mMenuAdapter = new CustomMenuAdapter(mTitles, mIcons);

        mViewHolder.mDuoMenuView.setOnMenuClickListener(this);
        mViewHolder.mDuoMenuView.setAdapter(mMenuAdapter);

    }

    private void goToFragment(Fragment fragment, boolean addToBackStack) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();

        if (addToBackStack) {
            transaction.addToBackStack(null);
        }

        transaction.add(R.id.container, fragment).commit();
    }

    private void handleDrawer() {
        DuoDrawerToggle duoDrawerToggle = new DuoDrawerToggle(this,
                mViewHolder.mDuoDrawerLayout,
                mViewHolder.mToolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        mViewHolder.mDuoDrawerLayout.setDrawerListener(duoDrawerToggle);
        duoDrawerToggle.syncState();

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onFooterClicked() {

    }

    @Override
    public void onHeaderClicked() {

    }

    @Override
    public void onOptionClicked(int position, Object objectClicked) {
        setTitle(mTitles.get(position));
        Log.d("TAG", "onOptionClicked: " + position);
        // Set the right options selected
        mMenuAdapter.setViewSelected(position, true);
        tv_Head.setText(mTitles.get(position));

        // Navigate to the right fragment
        switch (position) {
            default:
                ll_Notification.setVisibility(View.VISIBLE);
                goToFragment(new HomeFragment(code, wallet, donated, city, "" + latitude, "" + longitude), false);
                break;

            case 0:
                ll_Notification.setVisibility(View.VISIBLE);
                goToFragment(new HomeFragment(code, wallet, donated, city, "" + latitude, "" + longitude), false);
                break;

            case 1:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                        iv_Profile.setVisibility(View.GONE);
                        iv_QrCode.setVisibility(View.GONE);
                        ll_Notification.setVisibility(View.GONE);
                        goToFragment(new MyAppointmentFragment(), false);
                    } else {

                    }
                } else {
                   /* startActivity(new Intent(mContext, AmbulanceListActivity.class)
                            .putExtra("head", "Ambulance"));
                    Animatoo.animateCard(mContext);*/

                    startActivity(new Intent(mContext, LoginActivity.class));
                    Animatoo.animateCard(mContext);
                }

                break;

            case 2:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
//                        startActivity(new Intent(mContext, BloodListActivity.class)
//                                .putExtra("head", "Blood"));
//                        Animatoo.animateCard(mContext);
                        startActivity(new Intent(mContext, PaymentHistoryActivity.class));
                        Animatoo.animateCard(mContext);
                    } else {

                    }
                } else {
                    //Setting Fragment
                    iv_Profile.setVisibility(View.GONE);
                    iv_QrCode.setVisibility(View.GONE);
                    ll_Notification.setVisibility(View.GONE);
                    // goToFragment(new SettingsFragment(), false);
                    startActivity(new Intent(mContext, AmbulanceListActivity.class)
                            .putExtra("head", "Ambulance"));
                    Animatoo.animateCard(mContext);
                }
                break;

            case 3:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                        startActivity(new Intent(mContext, AmbulanceListActivity.class)
                                .putExtra("head", "Ambulance"));
                        Animatoo.animateCard(mContext);

                    } else {

                    }
                } else {
                    startActivity(new Intent(mContext, MedicalShopListActivity.class)
                            .putExtra("donated", "" + donated)
                            .putExtra("head", "Medical Shop"));
                    Animatoo.animateCard(mContext);
                }
                break;

            case 4:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                        startActivity(new Intent(mContext, MedicalShopListActivity.class)
                                .putExtra("donated", "" + donated)
                                .putExtra("head", "Medical Shop"));
                        Animatoo.animateCard(mContext);
                    } else {

                    }
                } else {
//                    startActivity(new Intent(mContext, BloodListActivity.class)
//                            .putExtra("head", "Blood"));
//                    Animatoo.animateCard(mContext);
                    if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                        startActivity(new Intent(mContext, MyWalletActivity.class));
                        Animatoo.animateCard(mContext);
                    } else {
                        startActivity(new Intent(mContext, LoginActivity.class));
                        Animatoo.animateCard(mContext);
                    }
                }
                break;

            case 5:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
//                        startActivity(new Intent(mContext, BloodListActivity.class)
//                                .putExtra("head", "Blood"));
//                        Animatoo.animateCard(mContext);
                        startActivity(new Intent(mContext, MyWalletActivity.class));
                        Animatoo.animateCard(mContext);
                    } else {

                    }
                } else {
                    //Setting Fragment
                    iv_Profile.setVisibility(View.GONE);
                    iv_QrCode.setVisibility(View.GONE);
                    ll_Notification.setVisibility(View.GONE);
                    goToFragment(new SettingsFragment(), false);
                }
                break;

            case 6:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                        iv_Profile.setVisibility(View.GONE);
                        iv_QrCode.setVisibility(View.GONE);
                        ll_Notification.setVisibility(View.GONE);
                        goToFragment(new SettingsFragment(), false);
                    } else {
                        Toast.makeText(mContext, "SettingLogin", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class)
                            .putExtra("head", "Blood"));
                    Animatoo.animateCard(mContext);
                }
                break;

            case 7:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                        if (donated.equalsIgnoreCase("0")) {
                            iv_Profile.setVisibility(View.GONE);
                            iv_QrCode.setVisibility(View.GONE);
                            ll_Notification.setVisibility(View.GONE);
                            logout();
                        } else {
                            goToFragment(new MembershipFragment(), false);
                        }
                    } else {

                    }
                } else {
                    iv_Profile.setVisibility(View.GONE);
                    iv_QrCode.setVisibility(View.GONE);
                    ll_Notification.setVisibility(View.GONE);
                    goToFragment(new CallSupportFragment(), false);
                }
                break;

            case 8:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                        if (donated.equalsIgnoreCase("0")) {
                            iv_Profile.setVisibility(View.GONE);
                            iv_QrCode.setVisibility(View.GONE);
                            ll_Notification.setVisibility(View.GONE);
                            goToFragment(new CallSupportFragment(), false);
                        } else {
                            iv_Profile.setVisibility(View.GONE);
                            iv_QrCode.setVisibility(View.GONE);
                            ll_Notification.setVisibility(View.GONE);
                            logout();
                        }
                    } else {

                    }
                } else {
                    iv_Profile.setVisibility(View.GONE);
                    iv_QrCode.setVisibility(View.GONE);
                    ll_Notification.setVisibility(View.GONE);
                    goToFragment(new CallSupportFragment(), false);
                }
                break;
            case 9:
                iv_Profile.setVisibility(View.GONE);
                iv_QrCode.setVisibility(View.GONE);
                ll_Notification.setVisibility(View.GONE);
                goToFragment(new CallSupportFragment(), false);
                break;


        }

        // Close the drawer
        mViewHolder.mDuoDrawerLayout.closeDrawer();
    }

    private void logout() {

        new AlertDialog.Builder(mContext)
                .setTitle(getResources().getString(R.string.logout))
                .setMessage(getResources().getString(R.string.logoutmsg))
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        rl_Loader.setVisibility(View.VISIBLE);
                        Utility.setSharedPreference(mContext, "u_id", "");
                        Utility.setSharedPreference(mContext, "u_name", "");
                        Utility.setSharedPreference(mContext, "u_img", "");
                        Utility.setSharedPreference(mContext, "u_email", "");
                        Utility.setSharedPreference(mContext, "location", "");
                        Utility.setSharedPreference(mContext, "user_type", "");
                        Utility.setSharedPreferenceBoolean(mContext, "islogin", false);
                        new Handler().postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                rl_Loader.setVisibility(View.GONE);
                                Intent intent = new Intent(mContext, LoginActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                Animatoo.animateSlideLeft(mContext);
                                startActivity(intent);
                                finish();
                            }
                        }, 3000);

                    }
                })

                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        mMenuAdapter.setViewSelected(0, true);
                        setTitle(mTitles.get(0));
                        tv_Head.setText(mTitles.get(0));
                    }
                })
                .show();

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Menu:
                if (mViewHolder.mDuoDrawerLayout.isDrawerOpen()) {
                    mViewHolder.mDuoDrawerLayout.closeDrawer();
                } else {
                    mViewHolder.mDuoDrawerLayout.openDrawer();
                }
                break;

            case R.id.iv_Profile:
                startActivity(new Intent(mContext, UserProfileActivity.class)
                        .putExtra("head", "Blood"));
                Animatoo.animateCard(mContext);
                break;

            case R.id.iv_QrCode:
                // which is the class of QR library
                IntentIntegrator intentIntegrator = new IntentIntegrator(this);
                intentIntegrator.setPrompt("Scan a barcode or QR Code");
                intentIntegrator.setOrientationLocked(true);
                intentIntegrator.initiateScan();

                break;

            case R.id.rl_Notisel:
                Utility.setSharedPreference(mContext, "noti_count", "");
                startActivity(new Intent(mContext, NotificationActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + latitude)
                        .putExtra("lon", "" + longitude));
                Animatoo.animateCard(mContext);
                break;

            case R.id.ll_Add:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    startActivity(new Intent(mContext, AddNotificationActivity.class)
                            .putExtra("uid", "" + user_id)
                            .putExtra("city", "" + city)
                            .putExtra("lat", "" + latitude)
                            .putExtra("lon", "" + longitude));
                    Animatoo.animateCard(mContext);
                } else {
                    startActivity(new Intent(mContext, LoginActivity.class)
                            .putExtra("head", "Blood"));
                    Animatoo.animateCard(mContext);
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        // if the intentResult is null then
        // toast a message as "cancelled"
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Toast.makeText(getBaseContext(), "Cancelled", Toast.LENGTH_SHORT).show();
            } else {
                // if the intentResult is not null we'll set
                // the content and format of scan message
                Log.d("TAG", "onActivityResult: " + intentResult.getContents());

                rl_Loader.setVisibility(View.VISIBLE);
                Call<ResponseBody> call = AppConfig.loadInterface().getProfile(intentResult.getContents());
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

                                    JSONObject result = object.optJSONObject("result");
                                    String user_type = result.optString("user_type");
                                    String name = result.optString("name");
                                   /* String wallet = result.optString("wallet");
                                    String donated = result.optString("donated");*/

                                    if (user_type.equalsIgnoreCase("market")) {

                                        startActivity(new Intent(HomeActivity.this, MarketPricesProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("medical")){
                                        startActivity(new Intent(HomeActivity.this, MedicalShopProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("bank")){

                                        startActivity(new Intent(HomeActivity.this, BloodBankProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("rmp")){

                                        startActivity(new Intent(HomeActivity.this, MedicalShopProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                               // .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("labs")){

                                        startActivity(new Intent(HomeActivity.this, MedicalShopProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                // .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("vaterinary")){

                                        startActivity(new Intent(HomeActivity.this, MedicalShopProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                // .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("Vehicle")){

                                        startActivity(new Intent(HomeActivity.this, VehicleProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                // .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (
                                            user_type.equalsIgnoreCase("Book 108")  ||
                                            user_type.equalsIgnoreCase("Private Ambulance")  ||
                                            user_type.equalsIgnoreCase("Team Ambulance")
                                    ){

                                        startActivity(new Intent(HomeActivity.this, AmbulanceDriverProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                // .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }else if (user_type.equalsIgnoreCase("donor")){

                                        startActivity(new Intent(HomeActivity.this, BloodDonorProfileActivity.class)
                                                .putExtra("wallet", wallet)
                                                .putExtra("donated", donated)
                                                // .putExtra("head", name)
                                                .putExtra("obj", "" +result)
                                        );
                                        Animatoo.animateCard(HomeActivity.this);
                                    }

                                }


                            } else ;

                        } catch (IOException | JSONException e) {
                            e.printStackTrace();
                            //  CustomSnakbar.showDarkSnakabar(mContext, view, "Profile " + e.getMessage());
                            Toast.makeText(mContext, "Profile " + e.getMessage(), Toast.LENGTH_SHORT).show();


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
        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }


    private class ViewHolder {
        private DuoDrawerLayout mDuoDrawerLayout;
        private DuoMenuView mDuoMenuView;
        private Toolbar mToolbar;

        ViewHolder() {
            mDuoDrawerLayout = (DuoDrawerLayout) findViewById(R.id.drawer);
            mDuoMenuView = (DuoMenuView) mDuoDrawerLayout.getMenuView();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isAgain) {
            getCurrentLocation();
        } else {
            getCurrentLocation();
            String noti_count = Utility.getSharedPreferences(mContext, "noti_count");

            if (noti_count.equalsIgnoreCase("")) {
                tv_NotiCount.setVisibility(View.GONE);
            } else {
                tv_NotiCount.setVisibility(View.VISIBLE);
                tv_NotiCount.setText(noti_count);
            }
            if (mMenuAdapter != null) {
                mMenuAdapter.setViewSelected(0, true);
                setTitle(mTitles.get(0));
                tv_Head.setText(mTitles.get(0));
                ll_Notification.setVisibility(View.VISIBLE);
                goToFragment(new HomeFragment(code, wallet, donated, city, "" + latitude, "" + longitude), false);
            }
        }
    }

    private void getProfile(String uid, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getProfile(uid);
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
                        System.out.println("UserProfile=>" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String active_status = result.optString("status");
                            code = result.optString("code");
                            donated = result.optString("donated");
                            wallet = result.optString("wallet");

                            if (active_status.equalsIgnoreCase("0")) {

                                Toast.makeText(mContext, "Your Account is Deactivated by FGH Team.", Toast.LENGTH_SHORT).show();
                                rl_Loader.setVisibility(View.VISIBLE);
                                Utility.setSharedPreference(mContext, "u_id", "");
                                Utility.setSharedPreference(mContext, "u_name", "");
                                Utility.setSharedPreference(mContext, "u_img", "");
                                Utility.setSharedPreference(mContext, "u_email", "");
                                Utility.setSharedPreference(mContext, "location", "");
                                Utility.setSharedPreference(mContext, "user_type", "");
                                Utility.setSharedPreferenceBoolean(mContext, "islogin", false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_Loader.setVisibility(View.GONE);
                                        Intent intent = new Intent(mContext, LoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Animatoo.animateSlideLeft(mContext);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 3000);

                            } else {

                                Utility.setSharedPreference(mContext, "code", code);

                                mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
                                mIcons = new ArrayList<>();
                                mIcons.add(0, getResources().getDrawable(R.drawable.home));
                                mIcons.add(1, getResources().getDrawable(R.drawable.ic_date));
                                mIcons.add(2, getResources().getDrawable(R.drawable.ic_payment));
                                mIcons.add(3, getResources().getDrawable(R.drawable.ic_ambulance));
                                mIcons.add(4, getResources().getDrawable(R.drawable.ic_cart));
//                            mIcons.add(4, getResources().getDrawable(R.drawable.ic_invert));
                                mIcons.add(5, getResources().getDrawable(R.drawable.ic_wallet));
//        mIcons.add(5, getResources().getDrawable(R.drawable.ic_person));
//        mIcons.add(6, getResources().getDrawable(R.drawable.ic_record));
//        mIcons.add(7, getResources().getDrawable(R.drawable.ic_group));
                                mIcons.add(6, getResources().getDrawable(R.drawable.ic_settings));
                                mIcons.add(7, getResources().getDrawable(R.drawable.ic_membership));
                                mIcons.add(8, getResources().getDrawable(R.drawable.ic_account));
                                mIcons.add(9, getResources().getDrawable(R.drawable.ic_exit));
                                mIcons.add(10, getResources().getDrawable(R.drawable.ic_call));

                                if (donated.equalsIgnoreCase("0")) {
                                    mTitles.remove("Health Id Card");
                                    mIcons.remove(6);
                                }
//        mIcons.add(9, getResources().getDrawable(R.drawable.ic_stars));

                                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                                    if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                                        mTitles.remove("Signup/Login");
                                        mIcons.remove(6);
                                        iv_Profile.setVisibility(View.VISIBLE);
                                        iv_QrCode.setVisibility(View.VISIBLE);
                                        civ_User.setVisibility(View.VISIBLE);
                                        tv_Name.setVisibility(View.VISIBLE);
                                        tv_City.setVisibility(View.VISIBLE);
                                        ll_Add.setVisibility(View.VISIBLE);
                                        tv_Name.setText(Utility.getSharedPreferences(mContext, "u_name"));
                                        String img = Utility.getSharedPreferences(mContext, "u_img");
                                        Glide.with(mContext).load(img).into(civ_User);
                                    } else {
                                        mTitles.remove("Logout");
                                        mIcons.remove(7);
                                    }
                                } else {
                                    civ_User.setVisibility(View.INVISIBLE);
                                    tv_Name.setVisibility(View.INVISIBLE);
                                    tv_City.setVisibility(View.INVISIBLE);
//                                ll_Add.setVisibility(View.GONE);
                                    iv_Profile.setVisibility(View.GONE);
                                    iv_QrCode.setVisibility(View.GONE);
                                    mTitles.remove("Logout");
                                    mTitles.remove("Appointments");
                                    mTitles.remove("Health Id Card");
                                    mIcons.remove(7);
                                    mIcons.remove(1);
                                    mIcons.remove(6);
                                }

                                // Initialize the views
                                mViewHolder = new ViewHolder();

                                // Handle menu actions
                                handleMenu();

                                // Handle drawer actions
                                handleDrawer();

                                // Show main fragment in container
                                ll_Notification.setVisibility(View.VISIBLE);
                                goToFragment(new HomeFragment(code, wallet, donated, city, "" + latitude, "" + longitude), false);
                                mMenuAdapter.setViewSelected(0, true);
                                setTitle(mTitles.get(0));

                            }

                        } else {
                            Log.e("UserProfile=>", "" + resultmessage);
//                            CustomSnakbar.showSnakabar(mContext, view, "" + resultmessage);
                            mTitles = new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.menuOptions)));
                            mIcons = new ArrayList<>();
                            mIcons.add(0, getResources().getDrawable(R.drawable.home));
                            mIcons.add(1, getResources().getDrawable(R.drawable.ic_date));
                            mIcons.add(2, getResources().getDrawable(R.drawable.ic_payment));
                            mIcons.add(3, getResources().getDrawable(R.drawable.ic_ambulance));
                            mIcons.add(4, getResources().getDrawable(R.drawable.ic_cart));
//                            mIcons.add(4, getResources().getDrawable(R.drawable.ic_invert));
                            mIcons.add(5, getResources().getDrawable(R.drawable.ic_wallet));
//        mIcons.add(5, getResources().getDrawable(R.drawable.ic_person));
//        mIcons.add(6, getResources().getDrawable(R.drawable.ic_record));
//        mIcons.add(7, getResources().getDrawable(R.drawable.ic_group));
                            mIcons.add(6, getResources().getDrawable(R.drawable.ic_settings));
                            mIcons.add(7, getResources().getDrawable(R.drawable.ic_membership));
                            mIcons.add(8, getResources().getDrawable(R.drawable.ic_account));
                            mIcons.add(9, getResources().getDrawable(R.drawable.ic_exit));
                            mIcons.add(10, getResources().getDrawable(R.drawable.ic_call));

//        mIcons.add(9, getResources().getDrawable(R.drawable.ic_stars));

                            if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                                if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                                    mTitles.remove("Signup/Login");
                                    mIcons.remove(6);
                                    iv_Profile.setVisibility(View.VISIBLE);
                                    iv_QrCode.setVisibility(View.VISIBLE);
                                    civ_User.setVisibility(View.VISIBLE);
                                    tv_Name.setVisibility(View.VISIBLE);
                                    tv_City.setVisibility(View.VISIBLE);
                                    ll_Add.setVisibility(View.VISIBLE);
                                    tv_Name.setText(Utility.getSharedPreferences(mContext, "u_name"));
                                    String img = Utility.getSharedPreferences(mContext, "u_img");
                                    Glide.with(mContext).load(img).into(civ_User);
                                } else {
                                    mTitles.remove("Logout");
                                    mIcons.remove(7);
                                }
                            } else {
                                civ_User.setVisibility(View.INVISIBLE);
                                tv_Name.setVisibility(View.INVISIBLE);
                                iv_Profile.setVisibility(View.GONE);
                                iv_QrCode.setVisibility(View.GONE);
                                tv_City.setVisibility(View.INVISIBLE);
//                                ll_Add.setVisibility(View.GONE);
                                mTitles.remove("Logout");
                                mTitles.remove("Appointments");
                                mTitles.remove("Health Id Card");
                                mIcons.remove(7);
                                mIcons.remove(1);
                                mIcons.remove(6);
                            }

                            // Initialize the views
                            mViewHolder = new ViewHolder();

                            // Handle menu actions
                            handleMenu();

                            // Handle drawer actions
                            handleDrawer();

                            // Show main fragment in container
                            ll_Notification.setVisibility(View.VISIBLE);
                            goToFragment(new HomeFragment(code, wallet, donated, city, "" + latitude, "" + longitude), false);
                            mMenuAdapter.setViewSelected(0, true);
                            setTitle(mTitles.get(0));
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
