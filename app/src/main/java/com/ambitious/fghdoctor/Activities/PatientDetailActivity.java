package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONException;
import org.json.JSONObject;

public class PatientDetailActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private TextView tv_Head, et_name, et_Mobile, et_Purposevisit, et_Fees, et_Arialfees;
    private ImageView iv_Bck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_detail);
        finds();

        if (getIntent().getExtras() != null) {

            String obj = getIntent().getStringExtra("obj");

            try {

                JSONObject result = new JSONObject(obj);

                String user_id = result.optString("user_id");
                String user_type = result.optString("user_type");
                String parent_id = result.optString("parent_id");
                String name = result.optString("name");
                String mobile = result.optString("mobile");
                String medical_history = result.optString("medical_history");
                String fees = result.optString("fees");
                String service_charge = result.optString("service_charge");
                String entrydt = result.optString("entrydt");

                tv_Head.setText(name);
                et_name.setText(name);
                et_Mobile.setText(mobile);
                et_Purposevisit.setText(medical_history);
                et_Fees.setText(fees);
                et_Arialfees.setText(service_charge);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Purposevisit = findViewById(R.id.et_Purposevisit);
        et_Fees = findViewById(R.id.et_Fees);
        et_Arialfees = findViewById(R.id.et_Arialfees);

        iv_Bck.setOnClickListener(this);

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

        }

    }
}