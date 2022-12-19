package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.AppointmentNumberAdapter;
import com.ambitious.fghdoctor.Model.Shiift;
import com.ambitious.fghdoctor.Model.Tokens;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class DoctorProfileActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {
    private static final String TAG = "DoctorProfileActivity";
    private Context mContext = this;
    private ImageView iv_Bck, iv_Doctor, iv_Cat, iv_One, iv_Two, iv_Three, iv_Four, iv_Five;
    private TextView tv_Name, tv_Clinicname, tv_Education, tv_Category, tv_Rating, tv_Address, tv_Date, tv_Shift, tv_Notavailable, tv_Time, tv_Workingdays, tv_Aptime, tv_Amount, tv_Gender, tv_Apdate, tv_Refer, tv_Covidsym, tv_Book, tv_Directions, tv_Morning, tv_Evening, tv_Working, tv_Morningtime, tv_Eveningtime, tv_Workdays, tv_Bookaprepresent, tv_Desc, tv_Discriptionhead, tv_Viewreviews, tv_Liveop, tv_Call;
    private EditText et_Pname, et_Age, et_Email, et_Aadhar, et_Pnum, et_Msg, et_Refralname, et_Refralnum;
    private Spinner sp_Shift, sp_Gender, sp_Covid, sp_Refer;
    private RatingBar bar_Rating;
    private CheckBox chk_WalletAmount;
    private RecyclerView rv_Appointments;
    private HorizontalScrollView hsv;
    private RelativeLayout rl_Appoitment, rl_Refralopt;
    private AppointmentNumberAdapter adapter;
    private String obj = "", catimg = "", catname = "", donated = "", wallet = "", doc_id = "", aptime = "", cat_id = "", fee = "", txn_id = "", scharge = "", lat = "", lng = "", clinic_name = "", contact = "";
    public String token_no = "", doctor_shift_id = "", service_charge = "", meeting_to_time = "", meeting_from_time = "";
    private int starthour = 0, endhour = 0, startmin = 0, endmin = 0;
    private RelativeLayout rl_Loader;
    private ArrayList<Shiift> shifts;
    private ArrayList<Tokens> tokens;
    private ArrayList<String> shiftsname;
    private DatePickerDialog.OnDateSetListener date;
    private Calendar myCalendar = Calendar.getInstance();
    private String[] gender = {"Select Gender", "Male", "Female"};
    private String[] covid = {"Covid19 Symptoms", "Yes", "No"};
    private String[] refer = {"Refer by", "User"};
    private ArrayList<String> imagesStrings;
    private ArrayList<String> imagesStrings1;
    private boolean is_paid = false;
    private Dialog cdialog = null;

    private int percentage =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_profile);
        finds();

        if (getIntent().getExtras() != null) {

            obj = getIntent().getStringExtra("obj");
            catimg = getIntent().getStringExtra("catimg");
            catname = getIntent().getStringExtra("catname");
            donated = getIntent().getStringExtra("donated");
            wallet = getIntent().getStringExtra("wallet");

           /* donated = "1";
            wallet = "50";*/

            if (wallet.equalsIgnoreCase("0") || wallet.equalsIgnoreCase("")) {
                wallet = "0";
                // chk_Wallet.setVisibility(View.GONE);
            }
            else if (donated.equalsIgnoreCase("0")) {
              //  wallet = "0";
                chk_WalletAmount.setVisibility(View.GONE);
            }else if (donated.equalsIgnoreCase("") && wallet.equalsIgnoreCase("")) {
                //  wallet = "0";
                chk_WalletAmount.setVisibility(View.GONE);
            } else if (donated.equalsIgnoreCase("1") && wallet.equalsIgnoreCase("")) {
               // wallet = "0";
                chk_WalletAmount.setVisibility(View.GONE);
            } else if (donated.equalsIgnoreCase("1") && wallet.equalsIgnoreCase("0")) {
                //wallet = "0";
                chk_WalletAmount.setVisibility(View.GONE);
            } else {
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    chk_WalletAmount.setVisibility(View.VISIBLE);
                    chk_WalletAmount.setText("Use Wallet Amount(₹" + wallet + ")");

                } else {
                    chk_WalletAmount.setVisibility(View.GONE);
                }
//                chk_WalletAmount.setVisibility(View.GONE);
            }

            try {

                JSONObject result = new JSONObject(obj);

                doc_id = result.optString("user_id");
                String name = result.optString("name");
                String user_image = result.optString("user_image");
                String images = result.optString("images");
                String email = result.optString("email");
                String address = result.optString("clinic_address");
                cat_id = result.optString("category");
                String password = result.optString("password");
                contact = result.optString("mobile");
                String rating = result.optString("rating");
                String user_type = result.optString("user_type");
                String available = result.optString("available");
                String experience = result.optString("experience");
                String expert = result.optString("expert");
                String education = result.optString("education");
                String department = result.optString("department");
                String about = result.optString("about");
                String working_days = result.optString("working_days");
                meeting_from_time = result.optString("meeting_from_time");
                meeting_to_time = result.optString("meeting_to_time");
                clinic_name = result.optString("clinic_name");
                lat = result.optString("lat");
                lng = result.optString("lng");

                if (user_image.contains("png") || user_image.contains("jpg")) {
                    imagesStrings = new ArrayList<>();
                    imagesStrings.add(user_image);
                    Glide.with(mContext).load(user_image).into(iv_Doctor);
                } else {
                    iv_Doctor.setImageResource(R.drawable.profile_new);
                }

                if (images.contains(",")) {

                    hsv.setVisibility(View.VISIBLE);
                    String[] img = images.split(",");
                    imagesStrings1 = new ArrayList<>();

                    if (img.length == 1) {
                        imagesStrings1.add(img[0]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        iv_Two.setVisibility(View.GONE);
                        iv_Three.setVisibility(View.GONE);
                        iv_Four.setVisibility(View.GONE);
                        iv_Five.setVisibility(View.GONE);
                    } else if (img.length == 2) {
                        imagesStrings1.add(img[0]);
                        imagesStrings1.add(img[1]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        Glide.with(mContext).load(img[1]).into(iv_Two);
                        iv_Three.setVisibility(View.GONE);
                        iv_Four.setVisibility(View.GONE);
                        iv_Five.setVisibility(View.GONE);
                    } else if (img.length == 3) {
                        imagesStrings1.add(img[0]);
                        imagesStrings1.add(img[1]);
                        imagesStrings1.add(img[2]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        Glide.with(mContext).load(img[1]).into(iv_Two);
                        Glide.with(mContext).load(img[2]).into(iv_Three);
                        iv_Four.setVisibility(View.GONE);
                        iv_Five.setVisibility(View.GONE);
                    } else if (img.length == 4) {
                        imagesStrings1.add(img[0]);
                        imagesStrings1.add(img[1]);
                        imagesStrings1.add(img[2]);
                        imagesStrings1.add(img[3]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        Glide.with(mContext).load(img[1]).into(iv_Two);
                        Glide.with(mContext).load(img[2]).into(iv_Three);
                        Glide.with(mContext).load(img[3]).into(iv_Four);
                        iv_Five.setVisibility(View.GONE);
                    } else if (img.length == 5) {
                        imagesStrings1.add(img[0]);
                        imagesStrings1.add(img[1]);
                        imagesStrings1.add(img[2]);
                        imagesStrings1.add(img[3]);
                        imagesStrings1.add(img[4]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        Glide.with(mContext).load(img[1]).into(iv_Two);
                        Glide.with(mContext).load(img[2]).into(iv_Three);
                        Glide.with(mContext).load(img[3]).into(iv_Four);
                        Glide.with(mContext).load(img[4]).into(iv_Five);
                    }

                } else if (images.contains("png") || images.contains("jpg")) {
                    hsv.setVisibility(View.VISIBLE);
                    imagesStrings1 = new ArrayList<>();
                    imagesStrings1.add(images);
                    Glide.with(mContext).load(images).into(iv_One);
                    iv_Two.setVisibility(View.GONE);
                    iv_Three.setVisibility(View.GONE);
                    iv_Four.setVisibility(View.GONE);
                    iv_Five.setVisibility(View.GONE);

                } else {

                    hsv.setVisibility(View.GONE);
                    iv_One.setVisibility(View.GONE);
                    iv_Two.setVisibility(View.GONE);
                    iv_Three.setVisibility(View.GONE);
                    iv_Four.setVisibility(View.GONE);
                    iv_Five.setVisibility(View.GONE);

                }

                if (education.equalsIgnoreCase("") && department.equalsIgnoreCase("")) {
                    tv_Education.setVisibility(View.GONE);
                } else {
                    tv_Education.setVisibility(View.VISIBLE);
                }

                if (working_days.equalsIgnoreCase("")) {
                    tv_Working.setVisibility(View.GONE);
                    tv_Workdays.setVisibility(View.GONE);
                } else {
                    tv_Working.setText("Working Days\n\n" + working_days);
                    tv_Workdays.setText(working_days);
                    tv_Working.setVisibility(View.GONE);
                    tv_Workdays.setVisibility(View.VISIBLE);
                }

                Glide.with(mContext).load(catimg).into(iv_Cat);
                if (name.contains("Dr.")) {
                    tv_Name.setText(name);
                } else {
//                    tv_Name.setText("Dr. " + name);
                    tv_Name.setText(name);
                }

                if (about.equalsIgnoreCase("")) {
                    tv_Desc.setVisibility(View.GONE);
                    tv_Discriptionhead.setVisibility(View.GONE);
                } else {
                    tv_Discriptionhead.setVisibility(View.VISIBLE);
                    tv_Desc.setVisibility(View.VISIBLE);
                    tv_Desc.setText(about);
                }

                tv_Rating.setText("(" + rating + ")");
                tv_Address.setText(address);
                tv_Education.setText(education + " - " + department);
                bar_Rating.setRating(Float.parseFloat(rating));
                tv_Category.setText(catname);
                tv_Clinicname.setText(clinic_name);

                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {

                    et_Pname.setText(Utility.getSharedPreferences(mContext, "u_name"));
                    et_Email.setText(Utility.getSharedPreferences(mContext, "u_email"));
                    et_Pnum.setText("+91" + Utility.getSharedPreferences(mContext, "u_mobile"));

                }

                /*Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);

                if (hour >= 12) {
                    tv_Aptime.setText(hour + ":" + minute + " PM");
                } else {
                    tv_Aptime.setText(hour + ":" + minute + " AM");
                }*/

                if (Utility.isNetworkConnected(mContext)) {
                    long date = System.currentTimeMillis();

                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
                    String dateString = sdf.format(date);
                    tv_Date.setText(dateString);
                    tv_Apdate.setText(dateString);
                    if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                        String uid = Utility.getSharedPreferences(mContext, "u_id");
                        getToken(uid, doc_id, dateString, iv_Bck);
                    } else {
                        getToken("", doc_id, dateString, iv_Bck);
                    }
//                    getShifts(doc_id, dateString, iv_Bck);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

//        sp_Shift.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                Log.e("Selected Shift=>", "" + shifts.get(position).getDoctor_shift_id() + "-------->" + shiftsname.get(position));
//                tv_Shift.setText(shiftsname.get(position));
//                String sid = shifts.get(position).getDoctor_shift_id();
//                String date = tv_Date.getText().toString();
//                if (!sid.equalsIgnoreCase("0")) {
//                    if (shifts.get(position).getShift_name().equalsIgnoreCase("Morning")) {
//                        String ft = shifts.get(position).getFrom_time();
//                        String[] frmtm = ft.split(":");
//                        String tt = shifts.get(position).getTo_time();
//                        String[] totm = tt.split(":");
//                        tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " AM  -  " + totm[0] + ":" + totm[1] + " AM");
//                    } else {
//                        String ft = shifts.get(position).getFrom_time();
//                        String[] frmtm = ft.split(":");
//                        String tt = shifts.get(position).getTo_time();
//                        String[] totm = ft.split(":");
//                        tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " PM  -  " + totm[0] + ":" + totm[1] + " PM");
//                    }
//                    tv_Amount.setText("₹" + shifts.get(position).getFees());
//                    tv_Time.setVisibility(View.VISIBLE);
//                    if (Utility.getSharedPreferencesBoolean(mContext, "islogin")) {
//                        String uid = Utility.getSharedPreferences(mContext, "u_id");
//                        getToken(uid, doc_id, date, iv_Bck);
//                    } else {
//                        getToken("", doc_id, date, iv_Bck);
//                    }
//                } else {
//                    tv_Time.setVisibility(View.GONE);
//                    rv_Appointments.setVisibility(View.GONE);
//                    tv_Notavailable.setVisibility(View.VISIBLE);
//                    final float scale = mContext.getResources().getDisplayMetrics().density;
//                    int pixels = (int) (100 * scale + 0.5f);
//                    LinearLayout.LayoutParams layout_description = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixels);
//                    rl_Appoitment.setLayoutParams(layout_description);
//                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Please Select Shift!");
//                }
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//                Log.e("Selected Shift=>", "" + shifts.get(0).getDoctor_shift_id() + "-------->" + shiftsname.get(0));
//            }
//        });

        date = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                  int dayOfMonth) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
                tv_Date.setText(sdf.format(myCalendar.getTime()));
                tv_Apdate.setText(sdf.format(myCalendar.getTime()));
                String dateString = sdf.format(myCalendar.getTime());
                token_no = "";
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    getToken(uid, doc_id, dateString, iv_Bck);
                } else {
                    getToken("", doc_id, dateString, iv_Bck);
                }
//                getShifts(doc_id, dateString, iv_Bck);
            }

        };

        sp_Covid.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_Covidsym.setText(covid[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_Covidsym.setText(covid[0]);
            }
        });

        sp_Gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_Gender.setText(gender[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_Gender.setText(gender[0]);
            }
        });

        sp_Refer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tv_Refer.setText(refer[position]);
                if (position == 0) {
                    rl_Refralopt.setVisibility(View.GONE);
                } else {
                    rl_Refralopt.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                tv_Refer.setText(refer[0]);
            }
        });

//        adapter = new AppointmentNumberAdapter(mContext);
//        GridLayoutManager manager = new GridLayoutManager(mContext, 6);
//        manager.setOrientation(RecyclerView.VERTICAL);
//        rv_Appointments.setLayoutManager(manager);
//        rv_Appointments.setAdapter(adapter);
    }

    private void getToken(String uid, String sid, String date, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getToken(sid, uid, date);
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

                            tv_Notavailable.setVisibility(View.GONE);
                            rv_Appointments.setVisibility(View.VISIBLE);
                            final float scale = mContext.getResources().getDisplayMetrics().density;
                            int pixels = (int) (250 * scale + 0.5f);
                            LinearLayout.LayoutParams layout_description = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixels);
                            rl_Appoitment.setLayoutParams(layout_description);
                            JSONObject object1 = object.optJSONObject("result");

                            String doctor_shift_id = object1.optString("doctor_shift_id");
                            String shift_name = object1.optString("shift_name");
                            String morning = object1.optString("morning");
                            String evening = object1.optString("evening");
                            String from_time = object1.optString("from_time");
                            String to_time = object1.optString("to_time");
                            String evening_from_time = object1.optString("evening_from_time");
                            String evening_to_time = object1.optString("evening_to_time");
                            String fees = object1.optString("fees");
                            String attending_token = object1.optString("attending_token");

                            JSONArray array = object1.optJSONArray("tokens");

                            tokens = new ArrayList<>();

                            boolean enable = false;
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String token = result.optString("token");
                                String service_charge = result.optString("service_charge");
                                String token_from_time = result.optString("from_time");
                                String token_to_time = result.optString("to_time");
                                String booked = result.optString("booked");
                                String booked_by_me = result.optString("booked_by_me");

                                Tokens tok = new Tokens();
                                tok.setDoctor_shift_id(doctor_shift_id);
                                tok.setShift_name(shift_name);
                                tok.setToken(token);
                                tok.setService_charge(service_charge);
                                tok.setFrom_time(token_from_time);
                                tok.setTo_time(token_to_time);
                                tok.setBooked(booked);
                                tok.setBooked_by_me(booked_by_me);
                                /*if (i == 0) {
                                    tok.setBooked("1");
                                    booked = "1";
                                } else {
                                    tok.setBooked(booked);
                                }
                                if (i == 1) {
                                    tok.setBooked_by_me("1");
                                    booked_by_me = "1";
                                } else {
                                    tok.setBooked_by_me(booked_by_me);
                                }*/
                                if (i == 0) {
                                    if (!enable) {
                                        if (booked.equalsIgnoreCase("0") && booked_by_me.equalsIgnoreCase("0")) {
                                            tok.setIs_enable(true);
                                            enable = true;
                                        } else if (booked.equalsIgnoreCase("1") && booked_by_me.equalsIgnoreCase("0")) {
                                            tok.setIs_enable(false);
                                            enable = false;
                                        } else if (booked.equalsIgnoreCase("0") && booked_by_me.equalsIgnoreCase("1")) {
                                            tok.setIs_enable(false);
                                            enable = false;
                                        } else if (booked.equalsIgnoreCase("1") && booked_by_me.equalsIgnoreCase("1")) {
                                            tok.setIs_enable(false);
                                            enable = false;
                                        }
                                    } else {
                                        tok.setIs_enable(false);
                                    }
                                } else {
                                    if (!enable) {
                                        if (tokens.get((i - 1)).isIs_enable()) {
                                            tok.setIs_enable(false);
                                        } else {
                                            if (booked.equalsIgnoreCase("0") && booked_by_me.equalsIgnoreCase("0")) {
                                                tok.setIs_enable(true);
                                                enable = true;
                                            } else if (booked.equalsIgnoreCase("1") && booked_by_me.equalsIgnoreCase("0")) {
                                                tok.setIs_enable(false);
                                                enable = false;
                                            } else if (booked.equalsIgnoreCase("0") && booked_by_me.equalsIgnoreCase("1")) {
                                                tok.setIs_enable(false);
                                                enable = false;
                                            } else if (booked.equalsIgnoreCase("1") && booked_by_me.equalsIgnoreCase("1")) {
                                                tok.setIs_enable(false);
                                                enable = false;
                                            }
                                        }
                                    } else {
                                        tok.setIs_enable(false);
                                    }
                                }
                                tokens.add(tok);

                            }

                            /*String[] frmtm = from_time.split(":");
                            String[] totm = to_time.split(":");
                            if (Integer.parseInt(frmtm[0]) > 12 && Integer.parseInt(totm[0]) > 12) {
                                tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " PM  -  " + totm[0] + ":" + totm[1] + " PM");
                            } else if (Integer.parseInt(frmtm[0]) < 12 && Integer.parseInt(totm[0]) > 12) {
                                tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " AM  -  " + totm[0] + ":" + totm[1] + " PM");
                            } else if (Integer.parseInt(frmtm[0]) < 12 && Integer.parseInt(totm[0]) < 12) {
                                tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " AM  -  " + totm[0] + ":" + totm[1] + " AM");
                            } else if (Integer.parseInt(frmtm[0]) > 12 && Integer.parseInt(totm[0]) < 12) {
                                tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " PM  -  " + totm[0] + ":" + totm[1] + " AM");
                            } else {
                                tv_Time.setText(frmtm[0] + ":" + frmtm[1] + " AM  -  " + totm[0] + ":" + totm[1] + " AM");
                            }
                            starthour = Integer.parseInt(frmtm[0]);
                            startmin = Integer.parseInt(frmtm[1]);
                            endhour = Integer.parseInt(totm[0]);
                            endmin = Integer.parseInt(totm[1]);*/
                            if (!morning.equalsIgnoreCase("0")) {
                                tv_Morning.setText("Morning Time\n\n" + from_time + "\n" + to_time);
                                tv_Morningtime.setText(from_time + "\n" + to_time);
                            } else {
                                tv_Morning.setText("Morning Time\n\nNot Available");
                                tv_Morningtime.setText("Not Available");
                            }
                            if (!evening.equalsIgnoreCase("0")) {
                                tv_Evening.setText("Evening Time\n\n" + evening_from_time + "\n" + evening_to_time);
                                tv_Eveningtime.setText(evening_from_time + "\n" + evening_to_time);
                            } else {
                                tv_Evening.setText("Evening Time\n\nNot Available");
                                tv_Eveningtime.setText("Not Available");
                            }
//                            tv_Time.setText("Morning Time : " + from_time + " - " + to_time + "\nEvening Time : " + evening_from_time + " - " + evening_to_time);
                            fee = fees;
                            tv_Amount.setText("₹" + fees);
                            if (!attending_token.equalsIgnoreCase("null")) {
                                if (attending_token.length() == 1) {
                                    tv_Liveop.setText("0" + attending_token);
                                } else {
                                    tv_Liveop.setText(attending_token);
                                }
                            } else {
                                tv_Liveop.setText("--");
                            }
//                            tv_Time.setVisibility(View.VISIBLE);

                            adapter = new AppointmentNumberAdapter(mContext, tokens);
                            adapter.setHasStableIds(true);
                            GridLayoutManager manager = new GridLayoutManager(mContext, 6);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Appointments.setLayoutManager(manager);
                            rv_Appointments.setAdapter(adapter);


                        } else {
                            tv_Time.setVisibility(View.GONE);
                            tv_Notavailable.setVisibility(View.GONE);
                            rv_Appointments.setVisibility(View.VISIBLE);
                            final float scale = mContext.getResources().getDisplayMetrics().density;
                            int pixels = (int) (100 * scale + 0.5f);
                            LinearLayout.LayoutParams layout_description = new LinearLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, pixels);
                            rl_Appoitment.setLayoutParams(layout_description);
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

    private void getShifts(String doc_id, String dateString, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getDoctorshift(doc_id, dateString);
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

                            shifts = new ArrayList<>();
                            shiftsname = new ArrayList<>();
                            Shiift shift1 = new Shiift();
                            shift1.setDoctor_shift_id("0");
                            shift1.setShift_name("Select Shift");
                            shift1.setFrom_time("0");
                            shift1.setTo_time("0");
                            shift1.setNo_of_token("0");
                            shift1.setTime_per_token("0");
                            shift1.setFees("0");
                            shifts.add(shift1);
                            shiftsname.add("Select Shift");
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String doctor_shift_id = result.optString("doctor_shift_id");
                                String shift_name = result.optString("shift_name");
                                String from_time = result.optString("from_time");
                                String to_time = result.optString("to_time");
                                String no_of_token = result.optString("no_of_token");
                                String time_per_token = result.optString("time_per_token");
                                String fees = result.optString("fees");

                                Shiift shift = new Shiift();
                                shift.setDoctor_shift_id(doctor_shift_id);
                                shift.setShift_name(shift_name);
                                shift.setFrom_time(from_time);
                                shift.setTo_time(to_time);
                                shift.setNo_of_token(no_of_token);
                                shift.setTime_per_token(time_per_token);
                                shift.setFees(fees);

                                shifts.add(shift);
                                shiftsname.add(shift_name);

                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, shiftsname);
                            sp_Shift.setAdapter(adapter);

                        } else {
                            tv_Shift.setHint("Select Shift");
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
        iv_Doctor = findViewById(R.id.iv_Doctor);
        iv_Cat = findViewById(R.id.iv_Cat);
        iv_One = findViewById(R.id.iv_One);
        iv_Two = findViewById(R.id.iv_Two);
        iv_Three = findViewById(R.id.iv_Three);
        iv_Four = findViewById(R.id.iv_Four);
        iv_Five = findViewById(R.id.iv_Five);
        tv_Date = findViewById(R.id.tv_Date);
        tv_Aptime = findViewById(R.id.tv_Aptime);
//        tv_Shift = findViewById(R.id.tv_Shift);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        tv_Time = findViewById(R.id.tv_Time);
        tv_Workingdays = findViewById(R.id.tv_Workingdays);
        tv_Amount = findViewById(R.id.tv_Amount);
        tv_Gender = findViewById(R.id.tv_Gender);
        tv_Apdate = findViewById(R.id.tv_Apdate);
        tv_Refer = findViewById(R.id.tv_Refer);
        tv_Covidsym = findViewById(R.id.tv_Covidsym);
        tv_Book = findViewById(R.id.tv_Book);
        tv_Directions = findViewById(R.id.tv_Directions);
        tv_Morning = findViewById(R.id.tv_Morning);
        tv_Evening = findViewById(R.id.tv_Evening);
        tv_Working = findViewById(R.id.tv_Working);
        tv_Morningtime = findViewById(R.id.tv_Morningtime);
        tv_Eveningtime = findViewById(R.id.tv_Eveningtime);
        tv_Workdays = findViewById(R.id.tv_Workdays);
        tv_Bookaprepresent = findViewById(R.id.tv_Bookaprepresent);
        tv_Desc = findViewById(R.id.tv_Desc);
        tv_Discriptionhead = findViewById(R.id.tv_Discriptionhead);
        tv_Viewreviews = findViewById(R.id.tv_Viewreviews);
        tv_Liveop = findViewById(R.id.tv_Liveop);
        tv_Call = findViewById(R.id.tv_Call);
        et_Pname = findViewById(R.id.et_Pname);
        et_Age = findViewById(R.id.et_Age);
        et_Email = findViewById(R.id.et_Email);
        et_Aadhar = findViewById(R.id.et_Aadhar);
        et_Pnum = findViewById(R.id.et_Pnum);
        et_Msg = findViewById(R.id.et_Msg);
        et_Refralname = findViewById(R.id.et_Refralname);
        et_Refralnum = findViewById(R.id.et_Refralnum);
        sp_Shift = findViewById(R.id.sp_Shift);
        sp_Gender = findViewById(R.id.sp_Gender);
        sp_Covid = findViewById(R.id.sp_Covid);
        sp_Refer = findViewById(R.id.sp_Refer);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Clinicname = findViewById(R.id.tv_Clinicname);
        tv_Education = findViewById(R.id.tv_Education);
        tv_Category = findViewById(R.id.tv_Category);
        tv_Rating = findViewById(R.id.tv_Rating);
        tv_Address = findViewById(R.id.tv_Address);
        bar_Rating = findViewById(R.id.bar_Rating);
        chk_WalletAmount = findViewById(R.id.chk_WalletAmount);
        rv_Appointments = findViewById(R.id.rv_Appointments);
        hsv = findViewById(R.id.hsv);
        rl_Appoitment = findViewById(R.id.rl_Appoitment);
        rl_Refralopt = findViewById(R.id.rl_Refralopt);
        rl_Loader = findViewById(R.id.rl_Loader);

        ArrayAdapter<String> gadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, gender);
        sp_Gender.setAdapter(gadapter);

        ArrayAdapter<String> cadapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, covid);
        sp_Covid.setAdapter(cadapter);

        ArrayAdapter<String> radapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_list_item_1, refer);
        sp_Refer.setAdapter(radapter);

        iv_Bck.setOnClickListener(this);
        tv_Date.setOnClickListener(this);
//        tv_Shift.setOnClickListener(this);
        tv_Gender.setOnClickListener(this);
        tv_Covidsym.setOnClickListener(this);
        tv_Book.setOnClickListener(this);
        tv_Refer.setOnClickListener(this);
        tv_Aptime.setOnClickListener(this);
        tv_Apdate.setOnClickListener(this);
        tv_Call.setOnClickListener(this);
        iv_Doctor.setOnClickListener(this);
        tv_Directions.setOnClickListener(this);
        tv_Bookaprepresent.setOnClickListener(this);
        iv_One.setOnClickListener(this);
        iv_Two.setOnClickListener(this);
        iv_Three.setOnClickListener(this);
        iv_Four.setOnClickListener(this);
        iv_Five.setOnClickListener(this);
        tv_Viewreviews.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(final View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.tv_Apdate:
            case R.id.tv_Date:
                DatePickerDialog dialog = new DatePickerDialog(mContext, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                dialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dialog.show();
                break;

           /* case R.id.tv_Shift:
                if (tv_Shift.getText().toString().equalsIgnoreCase("")) {
                    CustomSnakbar.showDarkSnakabar(mContext, v, "Shift is not available on " + tv_Date.getText().toString());
                } else {
                    sp_Shift.performClick();
                }
                break;*/

            case R.id.tv_Gender:
                sp_Gender.performClick();
                break;

            case R.id.tv_Covidsym:
                sp_Covid.performClick();
                break;

            case R.id.tv_Refer:
                sp_Refer.performClick();
                break;

            case R.id.tv_Book:
                if (token_no.equalsIgnoreCase("")) {
                    CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Token First!");
                } else {
                    validate(v);
                }
                break;


            case R.id.tv_Aptime:
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker = new TimePickerDialog(mContext, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        /*if (selectedHour > 12) {
                            if ((selectedHour - 12) < starthour) {
                                aptime = "";
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                            } else if ((selectedHour - 12) == starthour) {
                                if (selectedMinute > startmin) {
                                    aptime = selectedHour + ":" + selectedMinute + ":00";
                                    if (selectedHour >= 12) {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                                    } else {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                                    }
                                } else {
                                    aptime = "";
                                    CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                                }
                            } else if (endhour > (selectedHour - 12)) {
                                aptime = "";
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                            } else if ((selectedHour - 12) == endhour) {
                                if (selectedMinute < endmin) {
                                    aptime = selectedHour + ":" + selectedMinute + ":00";
                                    if (selectedHour >= 12) {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                                    } else {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                                    }
                                } else {
                                    aptime = "";
                                    CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                                }
                            } else {
                                aptime = selectedHour + ":" + selectedMinute + ":00";
                                if (selectedHour >= 12) {
                                    tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                                } else {
                                    tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                                }
                            }
                        } else {
                            if (selectedHour < starthour) {
                                aptime = "";
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                            } else if (selectedHour == starthour) {
                                if (selectedMinute > startmin) {
                                    aptime = selectedHour + ":" + selectedMinute + ":00";
                                    if (selectedHour >= 12) {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                                    } else {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                                    }
                                } else {
                                    aptime = "";
                                    CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                                }
                            } else if (selectedHour > endhour) {
                                aptime = "";
                                CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                            } else if (selectedHour == endhour) {
                                if (selectedMinute < endmin) {
                                    aptime = selectedHour + ":" + selectedMinute + ":00";
                                    if (selectedHour >= 12) {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                                    } else {
                                        tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                                    }
                                } else {
                                    aptime = "";
                                    CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Time Between => " + tv_Time.getText().toString());
                                }
                            } else {
                                aptime = selectedHour + ":" + selectedMinute + ":00";
                                if (selectedHour >= 12) {
                                    tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                                } else {
                                    tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                                }
                            }
                        }*/
                        if (selectedHour >= 12) {
                            tv_Aptime.setText(selectedHour + ":" + selectedMinute + " PM");
                        } else {
                            tv_Aptime.setText(selectedHour + ":" + selectedMinute + " AM");
                        }
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
                break;

            case R.id.iv_Doctor:
                if (imagesStrings != null) {
                    openImage(0, imagesStrings);
                }
                break;

            case R.id.tv_Directions:
                if (lat.equalsIgnoreCase("") && lng.equalsIgnoreCase("")) {
                    CustomSnakbar.showDarkSnakabar(mContext, v, "Doctor not added thier location!");
                } else {
                    String strUri = "http://maps.google.com/maps?q=loc:" + lat + "," + lng + " (" + clinic_name + ")";
                    Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(strUri));
                    intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                    startActivity(intent);
                }
                break;

            case R.id.tv_Bookaprepresent:
                startActivity(new Intent(mContext, RepresentativeBookingActivity.class)
                        .putExtra("time", "" + meeting_from_time + "\n" + meeting_to_time)
                        .putExtra("doc_id", doc_id));
                Animatoo.animateCard(mContext);
                break;

            case R.id.iv_One:
                if (imagesStrings1 != null) {
                    openImage(0, imagesStrings1);
                }
                break;

            case R.id.iv_Two:
                if (imagesStrings1 != null) {
                    openImage(1, imagesStrings1);
                }
                break;

            case R.id.iv_Three:
                if (imagesStrings1 != null) {
                    openImage(2, imagesStrings1);
                }
                break;

            case R.id.iv_Four:
                if (imagesStrings1 != null) {
                    openImage(3, imagesStrings1);
                }
                break;

            case R.id.iv_Five:
                if (imagesStrings1 != null) {
                    openImage(4, imagesStrings1);
                }
                break;

            case R.id.tv_Viewreviews:
                startActivity(new Intent(mContext, ViewReviewsActivity.class)
                        .putExtra("doc_id", "" + doc_id));
                Animatoo.animateCard(mContext);
                break;

            case R.id.tv_Call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact));
                startActivity(intent);
                break;
        }

    }

    private void validate(View v) {

        String uid = Utility.getSharedPreferences(mContext, "u_id");
        String name = et_Pname.getText().toString();
        String age = et_Age.getText().toString();
        String gender = tv_Gender.getText().toString();
        String apdate = tv_Apdate.getText().toString();
        String time = tv_Aptime.getText().toString();
        String email = et_Email.getText().toString();
        String adharno = et_Aadhar.getText().toString();
        String number = et_Pnum.getText().toString();
        String covid = tv_Covidsym.getText().toString();
        String referby = tv_Refer.getText().toString();
        String refername = et_Refralname.getText().toString();
        String refernum = et_Refralnum.getText().toString();
        String amount = tv_Amount.getText().toString();
        String msg = et_Msg.getText().toString();


        if (name.equalsIgnoreCase("")) {
            et_Pname.setError("Can't be empty!");
            et_Pname.requestFocus();
        } else if (age.equalsIgnoreCase("")) {
            et_Age.setError("Can't be empty!");
            et_Age.requestFocus();
        } else if (gender.equalsIgnoreCase("Select Gender")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Gender!");
        } else if (apdate.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Appointment Date!");
        } else if (time.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Appointment Time!");
        } else if (number.equalsIgnoreCase("")) {
            et_Pnum.setError("Can't be empty!");
            et_Pnum.requestFocus();
        } else if (covid.equalsIgnoreCase("Covid19 Symptoms")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Covid19 Symptoms yes or no!");
        } else if (!referby.equalsIgnoreCase("Refer by") && refername.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Enter Referel Name!");
            et_Refralname.setError("Can't be empty!");
            et_Refralname.requestFocus();
        } else if (!referby.equalsIgnoreCase("Refer by") && refernum.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Enter Referel Number!");
            et_Refralnum.setError("Can't be empty!");
            et_Refralnum.requestFocus();
        } else if (amount.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "OP Amount is Empty!");
        } else {

            if (!referby.equalsIgnoreCase("Refer by")) {
//                referby = referby;
            } else {
                referby = "";
            }

            if (Utility.isNetworkConnected(mContext)) {

                if (is_paid) {
                    String Amnt = "";
                    if (chk_WalletAmount.isChecked()) {
                        if (Integer.parseInt(fee) + Integer.parseInt(service_charge) <= Integer.parseInt(wallet)) {
                            Amnt = "" + (Integer.parseInt(fee) + Integer.parseInt(service_charge));
                            wallet = Amnt;
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment Successfull.");
                            Amnt = "" + (Integer.parseInt(fee) + Integer.parseInt(service_charge));
                        }
                    } else {
                        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment Successfull.");
                        Amnt = "" + (Integer.parseInt(fee) + Integer.parseInt(service_charge));
                        wallet = "0";
                    }
//                    Toast.makeText(mContext, "Amount=>" + Amnt + "\nWallet=>" + wallet, Toast.LENGTH_LONG).show();
                    bookAppointment(uid, doc_id, doctor_shift_id, cat_id, apdate, token_no, name, number, email, gender, aptime, age, adharno, referby, refername, refernum, covid, Amnt, wallet, txn_id, service_charge, "Pending", msg, v);
                } else {
                    if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                        bookAppointmentDialog(uid, doc_id, doctor_shift_id, cat_id, apdate, token_no, name, number, email, gender, aptime, age, adharno, referby, refernum, covid, amount, "Pending", msg, v);
                    } else {
                        bookAppointmentDialog("", doc_id, doctor_shift_id, cat_id, apdate, token_no, name, number, email, gender, aptime, age, adharno, referby, refernum, covid, amount, "Pending", msg, v);
                    }
                }

            } else {
                AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                        "You don't have internet connection.", false);
            }

        }
    }

    private void bookAppointmentDialog(final String uid, final String doc_id, final String doctor_shift_id, final String cat_id, final String apdate, final String token_no, final String name, final String number, final String email, final String gender, final String aptime, final String age, final String adharno, final String referby, final String refnum, final String covid, final String amount, final String stts, final String msg, View view) {

        final Dialog dialog = new Dialog(mContext);
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.setContentView(R.layout.dialog_book);

        TextView tv_Dname = dialog.findViewById(R.id.tv_Dname);
        TextView tv_Apdate = dialog.findViewById(R.id.tv_Apdate);
        TextView tv_time = dialog.findViewById(R.id.tv_Aptime);
        TextView tv_Tokenno = dialog.findViewById(R.id.tv_Tokenno);
        TextView tv_OPAmount = dialog.findViewById(R.id.tv_OPAmount);
        TextView tv_Servicecharge = dialog.findViewById(R.id.tv_Servicecharge);
        TextView tv_Totalamount = dialog.findViewById(R.id.tv_Totalamount);
        TextView tv_Bookap = dialog.findViewById(R.id.tv_Bookap);
        TextView tv_Walletamount = dialog.findViewById(R.id.tv_Walletamount);
        RelativeLayout rl_Wallet = dialog.findViewById(R.id.rl_Wallet);

        tv_Dname.setText(tv_Name.getText().toString());
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(apdate);
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);
            SimpleDateFormat outFormat1 = new SimpleDateFormat("dd MMM yyyy");
            String datee = outFormat1.format(date);
            tv_Apdate.setText(goal + "," + datee);
        } catch (Exception e) {
            e.printStackTrace();
        }
        tv_time.setText(tv_Aptime.getText().toString());
        tv_Tokenno.setText(token_no);
        tv_OPAmount.setText("₹" + fee);
        tv_Servicecharge.setText("₹" + service_charge);

        if (chk_WalletAmount.isChecked()) {
            rl_Wallet.setVisibility(View.VISIBLE);
            if (Integer.parseInt(fee) + Integer.parseInt(service_charge) <= Integer.parseInt(wallet)) {
                int amt = Integer.parseInt(fee) + Integer.parseInt(service_charge);
                int remaining = Integer.parseInt(wallet) - amt;
                tv_Walletamount.setText("₹" + amt + " (" + remaining + ")");
                tv_Totalamount.setText("₹" + (Integer.parseInt(fee) + Integer.parseInt(service_charge) - amt));
                tv_Bookap.setText("Book");
            } else {
                tv_Walletamount.setText("₹" + wallet);
                tv_Totalamount.setText("₹" + (Integer.parseInt(fee) + Integer.parseInt(service_charge) - Integer.parseInt(wallet)));
                tv_Bookap.setText("Pay & Book");
            }
        } else {
            rl_Wallet.setVisibility(View.GONE);
            tv_Totalamount.setText("₹" + (Integer.parseInt(fee) + Integer.parseInt(service_charge)));
        }

        dialog.show();

        tv_Bookap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cdialog = dialog;
                String Amnt = "";
                if (chk_WalletAmount.isChecked()) {
                    Amnt = "" + (Integer.parseInt(fee) + Integer.parseInt(service_charge));
                    if (Integer.parseInt(fee) + Integer.parseInt(service_charge) <= Integer.parseInt(wallet)) {
                        is_paid = true;
                     //   tv_Book.performClick();
                        wallet = Amnt;
                        String refername = et_Refralname.getText().toString();
                        String refernum = et_Refralnum.getText().toString();

                        bookAppointment(uid, doc_id, doctor_shift_id, cat_id, apdate, token_no, name, number, email, gender, aptime, age, adharno, referby, refername, refernum, covid, Amnt, wallet, txn_id, service_charge, "Pending", msg, v);

                    } else {
                        getOrderId(v, name, Amnt, email, number);
//                        startPayment("", name, Amnt, email, number);
                    }
                } else {
                    Amnt = "" + (Integer.parseInt(fee) + Integer.parseInt(service_charge));
                    getOrderId(v, name, Amnt, email, number);
//                    startPayment("", name, Amnt, email, number);
                }

                dialog.dismiss();

                Log.d(TAG, "onClick: "+Amnt);
//                bookAppointment(uid, doc_id, doctor_shift_id, cat_id, apdate, token_no, name, number, email, gender, aptime, age, adharno, referby, covid, amount, stts, msg, v);
            }
        });

    }

    private void getOrderId(View view, String name, String amnt, String email, String number) {
        rl_Loader.setVisibility(View.VISIBLE);
        int amount = Integer.parseInt(amnt) * 100;

        Log.d(TAG, "getOrderId: "+amount);
        Call<ResponseBody> call = AppConfig.loadInterface().getOrderID(amount);
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
                        System.out.println("Genrate Order ID =>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            startPayment(resultmessage, name, amnt, email, number);
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Order ID " + e.getMessage());
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

    private void bookAppointment(String uid, String doc_id, String doctor_shift_id, String cat_id, String apdate, String token_no, String name, String number, String email, String gender, String aptime, String age, String adharno, String referby, String refname, String refnum, String covid, String amount, String wallet_amnt, String txn_id, String service_charge, String stts, String msg, final View view) {

        if (cdialog != null) {
            cdialog.dismiss();
        }

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().book(uid, doc_id, doctor_shift_id, cat_id, apdate, token_no, number, name, email, gender, aptime, age, adharno, referby, refname, refnum,
                covid, amount, wallet_amnt, txn_id, service_charge, stts, msg);
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
                        System.out.println("Booking=>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Appointment Book Successfully");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                   /* Intent intent = new Intent(mContext, HomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();*/

                                    Intent intent = new Intent(mContext, PaidSuccessActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    intent.putExtra("title",tv_Name.getText().toString());
                                    intent.putExtra("amount",amount);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();

                                }
                            }, 1500);

                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Booking " + e.getMessage());
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

    private void openImage(int pos, ArrayList<String> imagesStrings) {
        Dialog dialog = new Dialog(mContext, R.style.MyDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagedialog_view);
        ViewPager viewpager_a = (ViewPager) dialog.findViewById(R.id.viewpager_a);

        ViewPagerAdapter1 pagerAdapter = new ViewPagerAdapter1(mContext, imagesStrings, pos);
        viewpager_a.setAdapter(pagerAdapter);
        viewpager_a.setCurrentItem(pos);

        dialog.show();
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("onPaymentSuccess", "---->" + s);
        txn_id = s;
        is_paid = true;
        tv_Book.performClick();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("onPaymentError", i + "----->" + s);
        txn_id = "";
        is_paid = false;
        if (cdialog != null) {
            cdialog.dismiss();
        }
        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment Failed\nBooking Appointment Failed.");
    }

    public void startPayment(String orderid, String name, String amount, String email, String mobile) {
        try {

            final Activity activity = this;
            final Checkout co = new Checkout();

            try {
                JSONObject options = new JSONObject();
                options.put("name", name);
                options.put("description", "Appointment Charges");
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("order_id", orderid);
                options.put("currency", "INR");
                options.put("amount", (Integer.parseInt(amount) * 100));
//                options.put("amount", 100);

                JSONObject preFill = new JSONObject();
                preFill.put("email", email);
                preFill.put("contact", mobile);

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //************** view pager Adapter for image swiping *************//

    private class ViewPagerAdapter1 extends PagerAdapter {
        // Declare Variables
        Context context;
        ViewPagerAdapter1.ViewHolder holder;
        Bitmap bitmap;
        int pos;
        ArrayList<String> ImagesArray;

        ArrayList<String> list = new ArrayList<>();
        LayoutInflater inflater;

        private ViewPagerAdapter1(Context context, ArrayList<String> ImagesArray, int position) {
            this.context = context;
            this.pos = position;
            this.ImagesArray = ImagesArray;
        }

        @Override
        public void notifyDataSetChanged() {
            // TODO Auto-generated method stub
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            //return 1;
            return ImagesArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View convertView = null;
            if (convertView == null) {
                holder = new ViewPagerAdapter1.ViewHolder();
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.offer_grid_item, container, false);
                holder.imageView = (ImageView) convertView.findViewById(R.id.off_img);
                holder.iv_Paly = (ImageView) convertView.findViewById(R.id.iv_Paly);
                container.addView(convertView);
                convertView.setTag(holder);

            } else {

                holder = (ViewPagerAdapter1.ViewHolder) convertView.getTag();
            }
            if (ImagesArray.get(position).contains("mp4")) {
                holder.iv_Paly.setVisibility(View.VISIBLE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                mediaMetadataRetriever.setDataSource(ImagesArray.get(position), new HashMap<String, String>());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(100); //unit in microsecond
                holder.imageView.setImageBitmap(bmFrame);
            } else {
                holder.iv_Paly.setVisibility(View.GONE);
                Glide.with(context).load(ImagesArray.get(position)).into(holder.imageView);
            }
            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);
        }

        private class ViewHolder {
            ImageView imageView, iv_Paly;
        }
    }
}
