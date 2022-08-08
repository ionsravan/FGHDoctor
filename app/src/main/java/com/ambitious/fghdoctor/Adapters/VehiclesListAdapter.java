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
import com.ambitious.fghdoctor.Activities.MedicalShopListActivity;
import com.ambitious.fghdoctor.Activities.VehicleProfileActivity;
import com.ambitious.fghdoctor.Activities.VehiclesActivity;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.Vehicle;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class VehiclesListAdapter extends RecyclerView.Adapter<VehiclesListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Vehicle> vehicles;

    public VehiclesListAdapter(Context context, ArrayList<Vehicle> vehicles) {
        this.context = context;
        this.vehicles = vehicles;
        Collections.reverse(this.vehicles);
    }

    @NonNull
    @Override
    public VehiclesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ambulancedriver, parent, false);

        return new VehiclesListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull VehiclesListAdapter.MyViewHolder holder, int position) {
        Vehicle driver = vehicles.get(position);

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
        return vehicles.size();
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
                    context.startActivity(new Intent(context, VehicleProfileActivity.class)
                            .putExtra("wallet", "" + ((VehiclesActivity) context).wallet)
                            .putExtra("donated", ((VehiclesActivity) context).donated)
                            .putExtra("obj", "" + vehicles.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<Vehicle> list) {
        vehicles = list;
        notifyDataSetChanged();
    }

}
