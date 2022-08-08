package com.ambitious.fghdoctor.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Adapters.MyAppointmentsAdapter;
import com.ambitious.fghdoctor.Model.Appointment;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MembershipFragment extends Fragment {

    private View mView;
    private ImageView iv_User;
    private RelativeLayout rl_Loader;
    private TextView tv_Name, tv_Dob, tv_Code, tv_Gender, tv_ActivationDate, tv_ExpiryDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = inflater.inflate(R.layout.frag_membership, container, false);
        finds();

        if (Utility.isNetworkConnected(getContext())) {
            String uid = Utility.getSharedPreferences(getContext(), "u_id");
            getProfile(uid, tv_Name);
        } else {
            AlertConnection.showAlertDialog(getContext(), "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        return mView;
    }

    private void getProfile(String uid, TextView view) {

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
                        System.out.println("MembershipCard=>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String code = result.optString("code");
                            String dob = result.optString("dob");
                            String sex = result.optString("sex");
                            String activation_date = result.optString("activation_date");
                            String expiry_date = result.optString("expiry_date");

                            Glide.with(getContext()).load(user_image).into(iv_User);
                            tv_Name.setText(name);
                            tv_Dob.setText(parseDateToddMMyyyy(dob));
                            tv_Code.setText(code);
                            tv_Gender.setText(sex);
                            tv_ActivationDate.setText(parseDateToddMMyyyy(activation_date));
                            tv_ExpiryDate.setText(parseDateToddMMyyyy(expiry_date));

                        } else {
                            CustomSnakbar.showSnakabar(getContext(), view, "" + resultmessage);
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

        iv_User = mView.findViewById(R.id.iv_User);
        tv_Name = mView.findViewById(R.id.tv_Name);
        tv_Dob = mView.findViewById(R.id.tv_Dob);
        tv_Code = mView.findViewById(R.id.tv_Code);
        tv_Gender = mView.findViewById(R.id.tv_Gender);
        tv_ActivationDate = mView.findViewById(R.id.tv_ActivationDate);
        tv_ExpiryDate = mView.findViewById(R.id.tv_ExpiryDate);
        rl_Loader = mView.findViewById(R.id.rl_Loader);

    }

    public String parseDateToddMMyyyy(String time) {
        String inputPattern = "yyyy-MM-dd";
        String outputPattern = "dd/MM/yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }
}
