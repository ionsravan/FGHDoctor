package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ambitious.fghdoctor.Adapters.AmbulanceListAdapter;
import com.ambitious.fghdoctor.Model.Ambulance;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

public class AmbulanceListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Head;
    private RecyclerView rv_Ambulance;
    private AmbulanceListAdapter adapter;
    private ArrayList<Ambulance> ambulanceArrayList;
    private LinearLayout ll_Login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ambulance_list);
        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));
        setData();
    }

    private void setData() {

        ambulanceArrayList = new ArrayList<>();

        Ambulance ambulance = new Ambulance("Book 108", R.drawable.one);
        ambulanceArrayList.add(ambulance);

        ambulance = new Ambulance("Private Ambulance", R.drawable.two);
        ambulanceArrayList.add(ambulance);

        ambulance = new Ambulance("Team Ambulance", R.drawable.three);
        ambulanceArrayList.add(ambulance);

        adapter = new AmbulanceListAdapter(mContext, ambulanceArrayList);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_Ambulance.setLayoutManager(manager);
        rv_Ambulance.setAdapter(adapter);
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        rv_Ambulance = findViewById(R.id.rv_Ambulance);
        ll_Login = findViewById(R.id.ll_Login);

        iv_Bck.setOnClickListener(this);
        ll_Login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.ll_Login:
                startActivity(new Intent(mContext, AmbulanceLoginActivity.class));
                Animatoo.animateCard(mContext);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  Animatoo.animateCard(mContext);
    }
}
