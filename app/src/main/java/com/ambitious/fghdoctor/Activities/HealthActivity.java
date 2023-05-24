package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.RecyclerViewAdapter;
import com.ambitious.fghdoctor.Model.RecyclerData;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.Objects;

public class HealthActivity extends AppCompatActivity implements View.OnClickListener {
    RecyclerView recyclerList;
    private ImageView iv_Bck;
    private TextView tv_Head;
    private ArrayList<RecyclerData> recyclerDataArrayList;

    private String code = "", wallet = "", donated = "", city = "", lat = "", lon = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_health);

        if (getIntent() != null){
            city = getIntent().getStringExtra("city");
            lat = getIntent().getStringExtra("lat");
            wallet = getIntent().getStringExtra("wallet");
            donated = getIntent().getStringExtra("donated");
            lon = getIntent().getStringExtra("lon");
        }

        recyclerList = findViewById(R.id.recyclerList);
        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        iv_Bck.setOnClickListener(this);

        recyclerDataArrayList = new ArrayList<>();
        // added data to array list
        recyclerDataArrayList.add(new RecyclerData("hospitals", R.drawable.hospital));
        recyclerDataArrayList.add(new RecyclerData("reports", R.drawable.medicalreport));
        recyclerDataArrayList.add(new RecyclerData("blood", R.drawable.bloodhome));
        recyclerDataArrayList.add(new RecyclerData("medical shops", R.drawable.medicalhome));
        recyclerDataArrayList.add(new RecyclerData("rmp", R.drawable.rmp));
        recyclerDataArrayList.add(new RecyclerData("veterinary", R.drawable.vetreneri));
        recyclerDataArrayList.add(new RecyclerData("ambulance", R.drawable.ambulancehome));
        recyclerDataArrayList.add(new RecyclerData("lab", R.drawable.lab));
        recyclerDataArrayList.add(new RecyclerData("covid", R.drawable.covidservice));
        // added data from arraylist to adapter class.
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(recyclerDataArrayList, this);
        recyclerList.setAdapter(adapter);
        adapter.setOnItemClickListener((position, v) -> {
            Log.d("TAG", "onItemClick position: " + recyclerDataArrayList.get(position).getTitle());

            if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("hospitals")) {
                startActivity(new Intent(this, NewAppointmentCategoryListActivity.class)
                        .putExtra("head", "Book Appointment")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
            } if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Covid")) {
                startActivity(new Intent(this, CovidServicesActivity.class)
                        .putExtra("city", "" + city)
                        .putExtra("lat", "" + lat)
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                        .putExtra("lon", "" + lon));
                // Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Ambulance")) {
                startActivity(new Intent(this, AmbulanceListActivity.class)
                        .putExtra("head", "Ambulance"));
                // Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Blood")) {
                startActivity(new Intent(this, BloodListActivity.class)
                        .putExtra("head", "Blood")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                //  Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Medical Shops")) {
                startActivity(new Intent(this, MedicalShopListActivity.class)
                        .putExtra("head", "Medical Shop")
                        .putExtra("donated", "" + donated)
                        .putExtra("wallet", "" + wallet)
                );
                // Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("RMP")) {
                startActivity(new Intent(this, RMPDoctorListActivity.class)
                        .putExtra("head", "RMP Doctors")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                // Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Veterinary")) {
                startActivity(new Intent(this, VaterinaryDoctorListActivity.class)
                        .putExtra("head", "Vaterinary Doctors")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated));
                // Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Lab")) {
                startActivity(new Intent(this, LabListActivity.class)
                        .putExtra("head", "Labs")
                        .putExtra("wallet", "" + wallet)
                        .putExtra("donated", "" + donated)
                );
                // Animatoo.animateCard(getContext());
            } else if (recyclerDataArrayList.get(position).getTitle().equalsIgnoreCase("Reports")) {
                startActivity(new Intent(this, ReportsActivity.class)
                        .putExtra("head", "Reports"));
                //  Animatoo.animateCard(getContext());
            }  

        });
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
        //  Animatoo.animateCard(mContext);
    }
}