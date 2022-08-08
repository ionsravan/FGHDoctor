package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
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

import com.ambitious.fghdoctor.Adapters.AmbulanceDriverAdapter;
import com.ambitious.fghdoctor.Adapters.AppointmentCategoryAdapter;
import com.ambitious.fghdoctor.Adapters.DoctorListAdapter;
import com.ambitious.fghdoctor.Adapters.ExpertDoctorListAdapter;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.Category;
import com.ambitious.fghdoctor.Model.Doctors;
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
import java.util.Objects;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class AppointmentCategoryListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Head, tv_Notavailable, tv_Expert, tv_Cat;
    private EditText et_Search;
    private RecyclerView rv_Categories, rv_Expdoctors;
    private AppointmentCategoryAdapter adapter;
    private ExpertDoctorListAdapter docadapter;
    private ArrayList<Category> categories;
    private ArrayList<Doctors> doctors;
    private RelativeLayout rl_Loader;
    private LinearLayout ll_Doctors;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_appointment_category_list);
        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getExpertDoctors(iv_Bck);
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

                if (categories != null) {

                    if (categories.size() > 0) {
                        filter(s.toString());
                    } else {
                        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "catagories is not available.");
                    }

                }

            }
        });

//        setData();
    }

    private void getExpertDoctors(final ImageView view) {
        String latitude = Utility.getSharedPreferences(mContext, "latitude");
        String longitude = Utility.getSharedPreferences(mContext, "longitude");

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getExpertDoctors("1",latitude,longitude);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                //rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            ll_Doctors.setVisibility(View.VISIBLE);

                            JSONArray array = object.optJSONArray("result");

                            doctors = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String user_image = result.optString("user_image");
                                String email = result.optString("email");
                                String address = result.optString("clinic_address");
                                String password = result.optString("password");
                                String mobile = result.optString("mobile");
                                String rating = result.optString("rating");
                                String user_type = result.optString("user_type");
                                String available = result.optString("available");
                                String experience = result.optString("experience");
                                String expert = result.optString("expert");
                                String catimg = result.optString("category_image");
                                String cat_name = result.optString("category_name");
                                String education = result.optString("education");
                                String clinic_name = result.optString("clinic_name");


                                Doctors doctor = new Doctors();
                                doctor.setId(user_id);
                                doctor.setName(name);
                                doctor.setAddress(address);
                                doctor.setRating(rating);
                                doctor.setUser_image(user_image);
                                doctor.setExpert(expert);
                                doctor.setExperience(experience);
                                doctor.setCatimg(catimg);
                                doctor.setCatname(cat_name);
                                doctor.setEducation(education);
                                doctor.setClinicname(clinic_name);
                                doctor.setObj(result.toString());

                                doctors.add(doctor);


                            }

                            docadapter = new ExpertDoctorListAdapter(mContext, doctors);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.HORIZONTAL);
                            rv_Expdoctors.setLayoutManager(manager);
                            rv_Expdoctors.setAdapter(docadapter);

                            getCategories(iv_Bck);


                        } else {
                            ll_Doctors.setVisibility(View.GONE);
                            getCategories(iv_Bck);
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

    private void filter(String text) {

        ArrayList<Category> temp = new ArrayList();
        for (Category d : categories) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Categories.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adapter.updateList(temp);
        } else {
            rv_Categories.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

        if (doctors != null) {

            if (doctors.size() > 0) {
                filter1(text, temp);
            } else {
                CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "catagories is not available.");
            }
        }

    }

    private void filter1(String text, ArrayList<Category> ctemp) {

        ArrayList<Doctors> temp = new ArrayList();
        for (Doctors d : doctors) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getClinicname().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            tv_Expert.setVisibility(View.VISIBLE);
            rv_Expdoctors.setVisibility(View.VISIBLE);
            docadapter.updateList(temp);
        } else {
            tv_Expert.setVisibility(View.GONE);
            rv_Expdoctors.setVisibility(View.GONE);
        }

        if (ctemp.size() > 0) {
            rv_Categories.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            tv_Cat.setVisibility(View.GONE);
            adapter.updateList(ctemp);
        } else {
            rv_Categories.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
            tv_Cat.setVisibility(View.VISIBLE);
        }

    }

    private void getCategories(final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getCatagories();
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
                            rv_Categories.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            categories = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String category_id = result.optString("category_id");
                                String category_name = result.optString("category_name");
                                String category_image = result.optString("category_image");


                                Category category = new Category();
                                category.setId(category_id);
                                category.setName(category_name);
                                category.setImg(category_image);

                                categories.add(category);


                            }

                            adapter = new AppointmentCategoryAdapter(mContext, categories);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Categories.setLayoutManager(manager);
                            rv_Categories.setAdapter(adapter);

                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Categories.setVisibility(View.GONE);
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

    private void setData() {

        categories = new ArrayList<>();

        Category category = new Category("Dentist", R.drawable.dentist);
        categories.add(category);

        category = new Category("Urology", R.drawable.urology);
        categories.add(category);

        category = new Category("Neurology", R.drawable.neuro);
        categories.add(category);

        category = new Category("Orthopadic", R.drawable.ortho);
        categories.add(category);

        category = new Category("Cardiologist", R.drawable.cardio);
        categories.add(category);

        adapter = new AppointmentCategoryAdapter(mContext, categories);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_Categories.setLayoutManager(manager);
        rv_Categories.setAdapter(adapter);
    }

    private void finds() {

        rl_Loader = findViewById(R.id.rl_Loader);
        ll_Doctors = findViewById(R.id.ll_Doctors);
        et_Search = findViewById(R.id.et_Search);
        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        tv_Expert = findViewById(R.id.tv_Expert);
        tv_Cat = findViewById(R.id.tv_Cat);
        rv_Categories = findViewById(R.id.rv_Categories);
        rv_Expdoctors = findViewById(R.id.rv_Expdoctors);

        iv_Bck.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}
