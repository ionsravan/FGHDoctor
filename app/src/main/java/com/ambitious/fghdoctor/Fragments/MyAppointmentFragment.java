package com.ambitious.fghdoctor.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.AmbulanceDriverAdapter;
import com.ambitious.fghdoctor.Adapters.MyAppointmentsAdapter;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.Appointment;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

/**
 * A simple {@link Fragment} subclass.
 */
public class MyAppointmentFragment extends Fragment {

    private View mView;
    private RecyclerView rv_Appointments;
    private MyAppointmentsAdapter adapter;
    private RelativeLayout rl_Loader;
    private TextView tv_Notavailable;
    private ArrayList<Appointment> appointments;

    public MyAppointmentFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_my_appointment, container, false);
        finds();

        if (Utility.isNetworkConnected(getContext())) {
            String uid = Utility.getSharedPreferences(getContext(), "u_id");
            getAppointment(uid, tv_Notavailable);
        } else {
            AlertConnection.showAlertDialog(getContext(), "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        return mView;
    }

    private void getAppointment(String uid, final TextView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getAppointments(uid);
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

                            rv_Appointments.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            appointments = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String appointment_id = result.optString("appointment_id");
                                String token = result.optString("token");
                                String date = result.optString("date");
                                String time = result.optString("time");
                                String category_name = result.optString("category_name");
                                String doctor_name = result.optString("doctor_name");
                                String doctor_image = result.optString("doctor_image");

                                Appointment appoin = new Appointment();
                                appoin.setId(appointment_id);
                                appoin.setToken(token);
                                appoin.setDate(date);
                                appoin.setTime(time);
                                appoin.setCategory_name(category_name);
                                appoin.setDoctor_name(doctor_name);
                                appoin.setDoctor_image(doctor_image);
                                appoin.setObj(result.toString());

                                appointments.add(appoin);

                            }

                            adapter = new MyAppointmentsAdapter(getContext(), appointments);
                            LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Appointments.setLayoutManager(manager);
                            rv_Appointments.setAdapter(adapter);

                        } else {
                            rv_Appointments.setVisibility(View.GONE);
                            tv_Notavailable.setVisibility(View.VISIBLE);
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

    private void finds() {

        rv_Appointments = mView.findViewById(R.id.rv_Appointments);
        tv_Notavailable = mView.findViewById(R.id.tv_Notavailable);
        rl_Loader = mView.findViewById(R.id.rl_Loader);

    }
}
