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

import com.ambitious.fghdoctor.Activities.AmbulanceDriverListActivity;
import com.ambitious.fghdoctor.Model.Ambulance;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.ArrayList;
import java.util.Collections;

public class AmbulanceListAdapter extends RecyclerView.Adapter<AmbulanceListAdapter.MyViewHolder> {

    public Context context;
    public ArrayList<Ambulance> ambulances;

    public AmbulanceListAdapter(Context context, ArrayList<Ambulance> ambulances) {
        this.context = context;
        this.ambulances = ambulances;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_ambulance, parent, false);

        return new AmbulanceListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.iv_Bg.setImageResource(ambulances.get(position).getImg());
        holder.tv_Name.setText(ambulances.get(position).getName());

    }

    @Override
    public int getItemCount() {
        return ambulances.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Bg;
        public TextView tv_Name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Bg = itemView.findViewById(R.id.iv_Bg);
            tv_Name = itemView.findViewById(R.id.tv_Name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, AmbulanceDriverListActivity.class)
                            .putExtra("head", "" + ambulances.get(getAdapterPosition()).getName()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }
}
