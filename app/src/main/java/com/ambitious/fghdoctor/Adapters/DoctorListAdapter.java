package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.DoctorProfileActivity;
import com.ambitious.fghdoctor.Activities.NewAppointmentCategoryListActivity;
import com.ambitious.fghdoctor.Model.Category;
import com.ambitious.fghdoctor.Model.Doctors;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class DoctorListAdapter extends RecyclerView.Adapter<DoctorListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Doctors> doctors;

    public DoctorListAdapter(Context context, ArrayList<Doctors> doctors) {
        this.context = context;
        this.doctors = doctors;
        Collections.reverse(this.doctors);
    }

    public DoctorListAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_doctor, parent, false);

        return new DoctorListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Doctors doct = doctors.get(position);

        if (doct.getUser_image().contains("png") || doct.getUser_image().contains("jpg")) {
            Glide.with(context).load(doct.getUser_image()).into(holder.iv_Profile);
        } else {
            holder.iv_Profile.setImageResource(R.drawable.profile_new);
        }

        if (doct.getExperience().equalsIgnoreCase("")) {
            holder.tv_Experience.setVisibility(View.GONE);
        } else {
            holder.tv_Experience.setVisibility(View.VISIBLE);
        }

        holder.tv_Name.setText(doct.getName());
        holder.tv_Rating.setText("(" + doct.getRating() + ")");
        holder.tv_Address.setText(doct.getAddress());
        holder.tv_Clinicname.setText(doct.getClinicname());
        holder.tv_Experience.setText(doct.getExperience() + " year experience overall");
        holder.bar_Rating.setRating(Float.parseFloat(doct.getRating()));

    }

    @Override
    public int getItemCount() {
        return doctors.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Profile;
        public TextView tv_Name, tv_Rating, tv_Address, tv_Experience, tv_Clinicname, tv_Book;
        public RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Profile = itemView.findViewById(R.id.iv_Profile);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Rating = itemView.findViewById(R.id.tv_Rating);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Experience = itemView.findViewById(R.id.tv_Experience);
            tv_Clinicname = itemView.findViewById(R.id.tv_Clinicname);
            tv_Book = itemView.findViewById(R.id.tv_Book);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);

            tv_Book.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    Toast.makeText(context, "Donated=>" + ((NewAppointmentCategoryListActivity) context).donated + "\nWallet=>" + ((NewAppointmentCategoryListActivity) context).wallet, Toast.LENGTH_SHORT).show();
                    context.startActivity(new Intent(context, DoctorProfileActivity.class)
                            .putExtra("obj", "" + doctors.get(getAdapterPosition()).getObj())
                            .putExtra("catname", "" + doctors.get(getAdapterPosition()).getCatname())
                            .putExtra("catimg", "" + doctors.get(getAdapterPosition()).getCatimg())
                            .putExtra("donated", "" + ((NewAppointmentCategoryListActivity) context).donated)
                            .putExtra("wallet", "" + ((NewAppointmentCategoryListActivity) context).wallet)
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
                            .putExtra("donated", "" + ((NewAppointmentCategoryListActivity) context).donated)
                            .putExtra("wallet", "" + ((NewAppointmentCategoryListActivity) context).wallet)
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
