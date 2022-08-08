package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.PatientDetailActivity;
import com.ambitious.fghdoctor.Model.Patient;
import com.ambitious.fghdoctor.Model.Vaterinary;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;

public class PatientAdapter extends RecyclerView.Adapter<PatientAdapter.MyViewHolder> {

    public Context context;
    public ArrayList<Patient> patients;

    public PatientAdapter(Context context) {
        this.context = context;
    }

    public PatientAdapter(Context context, ArrayList<Patient> patients) {
        this.context = context;
        this.patients = patients;
    }

    @NonNull
    @Override
    public PatientAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(context).inflate(R.layout.item_patient, parent, false);

        return new PatientAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull PatientAdapter.MyViewHolder holder, int position) {

        Patient pat = patients.get(position);

        holder.tv_Name.setText(pat.getName());
        holder.tv_Number.setText("Mobile Number :- " + pat.getMobile());
        holder.tv_date.setText("Date\n" + pat.getDate());

    }

    @Override
    public int getItemCount() {
        return patients.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Name, tv_Number, tv_date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Number = itemView.findViewById(R.id.tv_Number);
            tv_date = itemView.findViewById(R.id.tv_date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, PatientDetailActivity.class)
                            .putExtra("obj", "" + patients.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<Patient> list) {
        patients = list;
        notifyDataSetChanged();
    }
}
