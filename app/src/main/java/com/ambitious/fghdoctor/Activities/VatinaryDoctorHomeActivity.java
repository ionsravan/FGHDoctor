package com.ambitious.fghdoctor.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Adapters.PatientAdapter;
import com.ambitious.fghdoctor.Model.Patient;
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

public class VatinaryDoctorHomeActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private TextView tv_Head, tv_Addpatient, tv_Notavailable, tv_Available;
    private EditText et_Search;
    private RecyclerView rv_Ptients;
    private ImageView iv_More;
    private RelativeLayout rl_Loader;
    private PatientAdapter adapter;
    private Switch switch_Available;
    private ArrayList<Patient> patientArrayList;
    private boolean is_updated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vatinary_doctor_home);
        finds();

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getProfile(uid, iv_More);
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

                if (patientArrayList.size() > 0) {
                    filter(s.toString());
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_More, "Patient is not available.");
                }

            }
        });

        switch_Available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                if (isChecked) {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "1", iv_More);
                } else {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "0", iv_More);
                }
            }
        });
    }

    private void finds() {

        tv_Head = findViewById(R.id.tv_Head);
        tv_Addpatient = findViewById(R.id.tv_Addpatient);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        tv_Available = findViewById(R.id.tv_Available);
        et_Search = findViewById(R.id.et_Search);
        rv_Ptients = findViewById(R.id.rv_Ptients);
        iv_More = findViewById(R.id.iv_More);
        rl_Loader = findViewById(R.id.rl_Loader);
        switch_Available = findViewById(R.id.switch_Available);

        tv_Addpatient.setOnClickListener(this);
        iv_More.setOnClickListener(this);

    }

    private void filter(String text) {

        ArrayList<Patient> temp = new ArrayList();
        for (Patient d : patientArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getMobile().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Ptients.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adapter.updateList(temp);
        } else {
            rv_Ptients.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void requesToChangeAvailablity(String uid, String stts, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().cahngeAvailablity(uid, stts);
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
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");
                            String available = result.optString("available");

                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
                            }

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

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.tv_Addpatient:
                startActivity(new Intent(mContext, AddPatientActivity.class)
                        .putExtra("type", "veterinary"));
                Animatoo.animateCard(mContext);
                break;

            case R.id.iv_More:
                showPopupMenu(v);
                break;

        }

    }

    private void showPopupMenu(View button) {

        PopupMenu popup = new PopupMenu(mContext, button);
        //Inflating the Popup using xml file
        popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());

        //registering popup with OnMenuItemClickListener
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()) {

                    case R.id.update:
                        is_updated = true;
                        startActivity(new Intent(mContext, VaterinaryUpdateActivity.class));
                        Animatoo.animateCard(mContext);
                        break;

                    case R.id.logout:
                        logout();
                        break;

                }
//                Toast.makeText(mContext, "You Clicked : " + item.getTitle(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });

        popup.show();

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
                                Intent intent = new Intent(mContext, VaterinaryLoginActivity.class);
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
                    }
                })
                .show();

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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String department = result.optString("department");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            String available = result.optString("available");

                            tv_Head.setText(name);
                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
                            }

                            getPatients(uid, "veterinarypatient", view);

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

    private void getPatients(String uid, String type, ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getPatients(uid, type);
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
                            patientArrayList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String user_type = result.optString("user_type");
                                String parent_id = result.optString("parent_id");
                                String name = result.optString("name");
                                String mobile = result.optString("mobile");
                                String medical_history = result.optString("medical_history");
                                String fees = result.optString("fees");
                                String service_charge = result.optString("service_charge");
                                String entrydt = result.optString("entrydt");

                                Patient pat = new Patient();

                                pat.setUser_id(user_id);
                                pat.setUser_type(user_type);
                                pat.setParent_id(parent_id);
                                pat.setName(name);
                                pat.setMobile(mobile);
                                pat.setMedical_history(medical_history);
                                pat.setFees(fees);
                                pat.setService_charge(service_charge);
                                pat.setObj(result.toString());
                                String[] dt = entrydt.split(" ");
                                pat.setDate(dt[0]);
                                patientArrayList.add(pat);

                            }

                            adapter = new PatientAdapter(mContext, patientArrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Ptients.setLayoutManager(manager);
                            rv_Ptients.setAdapter(adapter);

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
    protected void onResume() {
        super.onResume();
        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            if (is_updated) {
                getPatients(uid, "rmppatient", iv_More);
            }
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }
    }
}