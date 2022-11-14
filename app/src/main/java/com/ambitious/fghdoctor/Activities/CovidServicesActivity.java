package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

public class CovidServicesActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private EditText et_Search;
    private RelativeLayout rl_Covidwinners, rl_Oxygenbank, rl_Helpinghand, rl_Sanitizationservice, rl_Epass, rl_Foodbank, rl_Vehicles, rl_Deliveryboys, rl_Vaccine,
            rl_Telemedicine, rl_Isolationkit, rl_Quarantinecenter, rl_Beds, rl_Vaccinetoken;
    private TextView tv_Covidwinners, tv_Oxygenbank, tv_Helpinghand, tv_Sanitizationservice, tv_Epass, tv_Foodbank, tv_Vehicles, tv_Deliveryboys, tv_Vaccine,
            tv_Telemedicine, tv_Isolationkit, tv_Quarantinecenter, tv_Beds, tv_Vaccinetoken;
    private String city = "", lat = "", lon = "", wallet = "", donated = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_services);
        finds();

        city = getIntent().getStringExtra("city");
        lat = getIntent().getStringExtra("lat");
        lon = getIntent().getStringExtra("lon");
        wallet = getIntent().getStringExtra("wallet");
        donated = getIntent().getStringExtra("donated");

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                filter(s.toString());

            }
        });
    }

    private void filter(String text) {

        if (tv_Covidwinners.getText().toString().toLowerCase().contains(text)) {
            rl_Covidwinners.setVisibility(View.VISIBLE);
        } else {
            rl_Covidwinners.setVisibility(View.GONE);
        }

        if (tv_Oxygenbank.getText().toString().toLowerCase().contains(text)) {
            rl_Oxygenbank.setVisibility(View.VISIBLE);
        } else {
            rl_Oxygenbank.setVisibility(View.GONE);
        }

        if (tv_Helpinghand.getText().toString().toLowerCase().contains(text)) {
            rl_Helpinghand.setVisibility(View.VISIBLE);
        } else {
            rl_Helpinghand.setVisibility(View.GONE);
        }

        if (tv_Sanitizationservice.getText().toString().toLowerCase().contains(text)) {
            rl_Sanitizationservice.setVisibility(View.VISIBLE);
        } else {
            rl_Sanitizationservice.setVisibility(View.GONE);
        }

        if (tv_Foodbank.getText().toString().toLowerCase().contains(text)) {
            rl_Foodbank.setVisibility(View.VISIBLE);
        } else {
            rl_Foodbank.setVisibility(View.GONE);
        }

        if (tv_Vehicles.getText().toString().toLowerCase().contains(text)) {
            rl_Vehicles.setVisibility(View.VISIBLE);
        } else {
            rl_Vehicles.setVisibility(View.GONE);
        }

        if (tv_Deliveryboys.getText().toString().toLowerCase().contains(text)) {
            rl_Deliveryboys.setVisibility(View.VISIBLE);
        } else {
            rl_Deliveryboys.setVisibility(View.GONE);
        }

        if (tv_Epass.getText().toString().toLowerCase().contains(text)) {
            rl_Epass.setVisibility(View.VISIBLE);
        } else {
            rl_Epass.setVisibility(View.GONE);
        }

        if (tv_Vaccine.getText().toString().toLowerCase().contains(text)) {
            rl_Vaccine.setVisibility(View.VISIBLE);
        } else {
            rl_Vaccine.setVisibility(View.GONE);
        }

        if (tv_Telemedicine.getText().toString().toLowerCase().contains(text)) {
            rl_Telemedicine.setVisibility(View.VISIBLE);
        } else {
            rl_Telemedicine.setVisibility(View.GONE);
        }

        if (tv_Isolationkit.getText().toString().toLowerCase().contains(text)) {
            rl_Isolationkit.setVisibility(View.VISIBLE);
        } else {
            rl_Isolationkit.setVisibility(View.GONE);
        }

        if (tv_Quarantinecenter.getText().toString().toLowerCase().contains(text)) {
            rl_Quarantinecenter.setVisibility(View.VISIBLE);
        } else {
            rl_Quarantinecenter.setVisibility(View.GONE);
        }

        if (tv_Beds.getText().toString().toLowerCase().contains(text)) {
            rl_Beds.setVisibility(View.VISIBLE);
        } else {
            rl_Beds.setVisibility(View.GONE);
        }

        if (tv_Vaccinetoken.getText().toString().toLowerCase().contains(text)) {
            rl_Vaccinetoken.setVisibility(View.VISIBLE);
        } else {
            rl_Vaccinetoken.setVisibility(View.GONE);
        }

    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        et_Search = findViewById(R.id.et_Search);
        rl_Covidwinners = findViewById(R.id.rl_Covidwinners);
        rl_Oxygenbank = findViewById(R.id.rl_Oxygenbank);
        rl_Helpinghand = findViewById(R.id.rl_Helpinghand);
        rl_Sanitizationservice = findViewById(R.id.rl_Sanitizationservice);
        rl_Epass = findViewById(R.id.rl_Epass);
        rl_Foodbank = findViewById(R.id.rl_Foodbank);
        rl_Vehicles = findViewById(R.id.rl_Vehicles);
        rl_Deliveryboys = findViewById(R.id.rl_Deliveryboys);
        rl_Vaccine = findViewById(R.id.rl_Vaccine);
        rl_Telemedicine = findViewById(R.id.rl_Telemedicine);
        rl_Isolationkit = findViewById(R.id.rl_Isolationkit);
        rl_Quarantinecenter = findViewById(R.id.rl_Quarantinecenter);
        rl_Beds = findViewById(R.id.rl_Beds);
        rl_Vaccinetoken = findViewById(R.id.rl_Vaccinetoken);
        tv_Covidwinners = findViewById(R.id.tv_Covidwinners);
        tv_Oxygenbank = findViewById(R.id.tv_Oxygenbank);
        tv_Helpinghand = findViewById(R.id.tv_Helpinghand);
        tv_Sanitizationservice = findViewById(R.id.tv_Sanitizationservice);
        tv_Epass = findViewById(R.id.tv_Epass);
        tv_Foodbank = findViewById(R.id.tv_Foodbank);
        tv_Vehicles = findViewById(R.id.tv_Vehicles);
        tv_Deliveryboys = findViewById(R.id.tv_Deliveryboys);
        tv_Vaccine = findViewById(R.id.tv_Vaccine);
        tv_Telemedicine = findViewById(R.id.tv_Telemedicine);
        tv_Isolationkit = findViewById(R.id.tv_Isolationkit);
        tv_Quarantinecenter = findViewById(R.id.tv_Quarantinecenter);
        tv_Beds = findViewById(R.id.tv_Beds);
        tv_Vaccinetoken = findViewById(R.id.tv_Vaccinetoken);

        iv_Bck.setOnClickListener(this);
        rl_Covidwinners.setOnClickListener(this);
        rl_Oxygenbank.setOnClickListener(this);
        rl_Helpinghand.setOnClickListener(this);
        rl_Sanitizationservice.setOnClickListener(this);
        rl_Epass.setOnClickListener(this);
        rl_Foodbank.setOnClickListener(this);
        rl_Vehicles.setOnClickListener(this);
        rl_Deliveryboys.setOnClickListener(this);
        rl_Vaccine.setOnClickListener(this);
        rl_Telemedicine.setOnClickListener(this);
        rl_Isolationkit.setOnClickListener(this);
        rl_Quarantinecenter.setOnClickListener(this);
        rl_Beds.setOnClickListener(this);
        rl_Vaccinetoken.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.rl_Covidwinners:
                startActivity(new Intent(mContext, CovidWinnersActivity.class));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Oxygenbank:
                startActivity(new Intent(mContext, OxygenBankListActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Helpinghand:
                startActivity(new Intent(mContext, HelpingSoldierActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Sanitizationservice:
                startActivity(new Intent(mContext, SanitizationServiceActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Foodbank:
                startActivity(new Intent(mContext, FoodBankActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Vehicles:
                startActivity(new Intent(mContext, VehiclesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Deliveryboys:
                startActivity(new Intent(mContext, DeliveryBoyActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("lon", "" + lon));
                Animatoo.animateCard(mContext);
                break;

            case R.id.rl_Vaccine:
                String url = "https://www.cowin.gov.in/home";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
                break;

            case R.id.rl_Epass:
                openSelectionDialog(v);
                break;

            case R.id.rl_Telemedicine:
            case R.id.rl_Isolationkit:
            case R.id.rl_Quarantinecenter:
            case R.id.rl_Beds:
            case R.id.rl_Vaccinetoken:
                CustomSnakbar.showDarkSnakabar(mContext, v, "Coming Soon");
                break;

        }

    }

    private void openSelectionDialog(View v) {

        Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_imageoperation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_Changeimg = dialog.findViewById(R.id.tv_Changeimg);
        TextView tv_Removeimg = dialog.findViewById(R.id.tv_Removeimg);

        tv_Changeimg.setText("Telangana");
        tv_Removeimg.setText("Andhra pradesh");

        dialog.show();

        tv_Changeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url = "https://policeportal.tspolice.gov.in/";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        tv_Removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String url = "https://citizen.appolice.gov.in/jsp/homePage.do?method=getHomePageElements#";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
       // Animatoo.animateCard(mContext);
    }
}