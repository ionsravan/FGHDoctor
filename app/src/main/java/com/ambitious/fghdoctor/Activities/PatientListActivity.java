package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambitious.fghdoctor.Adapters.PatientAdapter;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class PatientListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Add, iv_Bck;
    private TextView tv_Head, tv_Notavailable;
    private EditText et_Search;
    private RecyclerView rv_Ptients;
    private PatientAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_r_m_p_doctors);
        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));

    }

    private void finds() {

        iv_Add = findViewById(R.id.iv_Add);
        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        et_Search = findViewById(R.id.et_Search);
        rv_Ptients = findViewById(R.id.rv_Ptients);

        adapter = new PatientAdapter(mContext);
        LinearLayoutManager manager = new LinearLayoutManager(mContext);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_Ptients.setLayoutManager(manager);
        rv_Ptients.setAdapter(adapter);

        iv_Bck.setOnClickListener(this);
        iv_Add.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {


            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.iv_Add:
                startActivity(new Intent(mContext, AddPatientActivity.class));
                Animatoo.animateCard(mContext);
                break;
        }

    }
}