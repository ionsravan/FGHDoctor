package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.AmbulanceDriverProfileActivity;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class AmbulanceDriverAdapter extends RecyclerView.Adapter<AmbulanceDriverAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<AmbuDriver> drivers;

    public AmbulanceDriverAdapter(Context context, ArrayList<AmbuDriver> drivers) {
        this.context = context;
        this.drivers = drivers;
        Collections.reverse(this.drivers);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_ambulancedriver, parent, false);

        return new AmbulanceDriverAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        AmbuDriver driver = drivers.get(position);

        if (driver.getUser_image().contains("png") || driver.getUser_image().contains("jpg")) {
            Glide.with(context).load(driver.getUser_image()).into(holder.iv_Driver);
        } else {
            holder.iv_Driver.setImageResource(R.drawable.profile_new);
        }

        if (driver.getAvailable().equalsIgnoreCase("1")) {
            holder.tv_Available.setVisibility(View.VISIBLE);
            holder.tv_Notavailable.setVisibility(View.GONE);
        } else {
            holder.tv_Available.setVisibility(View.GONE);
            holder.tv_Notavailable.setVisibility(View.VISIBLE);
        }

        holder.tv_Name.setText(driver.getName());
        holder.tv_Rating.setText("(" + driver.getRating() + ")");
        holder.tv_Address.setText(driver.getAddress());
        holder.bar_Rating.setRating(Float.parseFloat(driver.getRating()));

    }

    @Override
    public int getItemCount() {
        return drivers.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Driver;
        public TextView tv_Name, tv_Rating, tv_Address, tv_Available, tv_Notavailable;
        public RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Driver = itemView.findViewById(R.id.iv_Driver);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Rating = itemView.findViewById(R.id.tv_Rating);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);
            tv_Available = itemView.findViewById(R.id.tv_Available);
            tv_Notavailable = itemView.findViewById(R.id.tv_Notavailable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, AmbulanceDriverProfileActivity.class)
                            .putExtra("obj", "" + drivers.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<AmbuDriver> list){
        drivers = list;
        notifyDataSetChanged();
    }
}
