package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.DoctorProfileActivity;
import com.ambitious.fghdoctor.Model.Category;
import com.ambitious.fghdoctor.Model.Doctors;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class ExpertDoctorListAdapter extends RecyclerView.Adapter<ExpertDoctorListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Doctors> doctors;

    public ExpertDoctorListAdapter(Context context, ArrayList<Doctors> doctors) {
        this.context = context;
        this.doctors = doctors;
        Collections.reverse(this.doctors);
    }

    @NonNull
    @Override
    public ExpertDoctorListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor_horizontal, parent, false);

        return new ExpertDoctorListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExpertDoctorListAdapter.MyViewHolder holder, int position) {

        Doctors doct = doctors.get(position);

        if (doct.getUser_image().contains("png") || doct.getUser_image().contains("jpg")) {
            Glide.with(context).load(doct.getUser_image()).into(holder.iv_Doctor);
        } else {
            holder.iv_Doctor.setImageResource(R.drawable.profile_new);
        }

        if (doct.getUser_image().contains("png") || doct.getUser_image().contains("jpg")) {
            Glide.with(context).load(doct.getCatimg()).into(holder.iv_Cat);
        } else {
            holder.iv_Cat.setImageResource(R.drawable.dentist);
        }


        holder.tv_Name.setText(doct.getName());
        holder.tv_Education.setText(doct.getEducation());
        holder.tv_Category.setText(doct.getCatname());
        holder.tv_Address.setText(doct.getAddress());
        holder.tv_Clinicname.setText(doct.getClinicname());

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Doctor, iv_Cat;
        private TextView tv_Name, tv_Education, tv_Category, tv_Bookappoinment, tv_Address, tv_Clinicname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);


            iv_Doctor = itemView.findViewById(R.id.iv_Doctor);
            iv_Cat = itemView.findViewById(R.id.iv_Cat);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Education = itemView.findViewById(R.id.tv_Education);
            tv_Category = itemView.findViewById(R.id.tv_Category);
            tv_Bookappoinment = itemView.findViewById(R.id.tv_Bookappoinment);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Clinicname = itemView.findViewById(R.id.tv_Clinicname);


            tv_Bookappoinment.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DoctorProfileActivity.class)
                            .putExtra("obj", "" + doctors.get(getAdapterPosition()).getObj())
                            .putExtra("catname", "" + doctors.get(getAdapterPosition()).getCatname())
                            .putExtra("catimg", "" + doctors.get(getAdapterPosition()).getCatimg())
                    );
                    Animatoo.animateCard(context);
                }
            });

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DoctorProfileActivity.class)
                            .putExtra("obj", "" + doctors.get(getAdapterPosition()).getObj())
                            .putExtra("catname", "" + doctors.get(getAdapterPosition()).getCatname())
                            .putExtra("catimg", "" + doctors.get(getAdapterPosition()).getCatimg())
                    );
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<Doctors> list) {
        doctors = list;
        notifyDataSetChanged();
    }
}
