package com.ambitious.fghdoctor.Fragments;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Adapters.DoctorListAdapter;
import com.ambitious.fghdoctor.Adapters.NewAppointmentCategoryAdapter;
import com.ambitious.fghdoctor.Model.Category;
import com.ambitious.fghdoctor.Model.Doctors;
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

public class TopDoctorListFragment extends Fragment {

    private View mView;
    private EditText et_Search;
    private RecyclerView rv_Categories, rv_Doctors;
    private TextView tv_Notavailable;
    private RelativeLayout rl_Loader;
    private ArrayList<Category> categories;
    private NewAppointmentCategoryAdapter adapter;
    private ArrayList<Doctors> doctors;
    private DoctorListAdapter dadapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mView = LayoutInflater.from(getContext()).inflate(R.layout.fragment_top, container, false);
        finds();

        if (Utility.isNetworkConnected(getContext())) {
            String uid = Utility.getSharedPreferences(getContext(), "u_id");
            getCategories(mView);
        } else {
            AlertConnection.showAlertDialog(getContext(), "No Internet Connection",
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

                if (doctors != null) {

                    if (doctors.size() > 0) {
                        filter12(s);
                    } else {
                        CustomSnakbar.showDarkSnakabar(getContext(), et_Search, "catagories is not available.");
                    }
                }

            }
        });
        return mView;
    }

    private void filter12(Editable text) {

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
            rv_Doctors.setVisibility(View.VISIBLE);
            dadapter.updateList(temp);
        } else {
            rv_Doctors.setVisibility(View.GONE);
        }

    }

    private void finds() {

        et_Search = mView.findViewById(R.id.et_Search);
        rv_Categories = mView.findViewById(R.id.rv_Categories);
        rv_Doctors = mView.findViewById(R.id.rv_Doctors);
        tv_Notavailable = mView.findViewById(R.id.tv_Notavailable);
        rl_Loader = mView.findViewById(R.id.rl_Loader);

    }

    private void getCategories(final View view) {

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
                                category.setIs_Sel(false);

                                categories.add(category);


                            }

                            adapter = new NewAppointmentCategoryAdapter(getContext(), categories, new OnClick());
                            LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            manager.setOrientation(RecyclerView.HORIZONTAL);
                            rv_Categories.setLayoutManager(manager);
                            rv_Categories.setAdapter(adapter);

                            getDoctors("", "", "", mView);
                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Categories.setVisibility(View.GONE);
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

    public class OnClick implements NewAppointmentCategoryAdapter.CLick {

        @Override
        public void onSelect(int pos) {
//            Toast.makeText(getContext(), "" + categories.get(pos).getName(), Toast.LENGTH_SHORT).show();
            String cat_id = categories.get(pos).getId();
            String catimg = categories.get(pos).getImg();
            String cat_name = categories.get(pos).getName();
            getDoctors(cat_id, catimg, cat_name, mView);
        }
    }

    private void getDoctors(String cat_id, String catimg, String cat_name, View view) {

        String latitude = Utility.getSharedPreferences(requireContext(), "latitude");
        String longitude = Utility.getSharedPreferences(requireContext(), "longitude");

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        if (!cat_id.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().getDoctors(cat_id, "1",latitude,longitude);
        } else {
            call = AppConfig.loadInterface().getAllDoctors("1",latitude,longitude);
        }
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
                            rv_Doctors.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            doctors = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String user_image = result.optString("user_image");
                                String email = result.optString("email");
                                String clinic_name = result.optString("clinic_name");
                                String address = result.optString("clinic_address");
                                String password = result.optString("password");
                                String mobile = result.optString("mobile");
                                String rating = result.optString("rating");
                                String user_type = result.optString("user_type");
                                String available = result.optString("available");
                                String experience = result.optString("experience");
                                String expert = result.optString("expert");


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
                                doctor.setClinicname(clinic_name);
                                doctor.setObj(result.toString());

                                doctors.add(doctor);


                            }

                            dadapter = new DoctorListAdapter(getContext(), doctors);
                            LinearLayoutManager manager = new LinearLayoutManager(getContext());
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Doctors.setLayoutManager(manager);
                            rv_Doctors.setAdapter(dadapter);


                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Doctors.setVisibility(View.GONE);
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
}
