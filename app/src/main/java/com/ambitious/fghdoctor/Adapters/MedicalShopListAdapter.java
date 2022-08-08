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

import com.ambitious.fghdoctor.Activities.MedicalShopListActivity;
import com.ambitious.fghdoctor.Activities.MedicalShopProfileActivity;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.MedicalShop;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class MedicalShopListAdapter extends RecyclerView.Adapter<MedicalShopListAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MedicalShop> medicalShops;

    public MedicalShopListAdapter(Context context, ArrayList<MedicalShop> medicalShops) {
        this.context = context;
        this.medicalShops = medicalShops;
        Collections.reverse(this.medicalShops);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_medicalshop, parent, false);

        return new MedicalShopListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MedicalShop shop = medicalShops.get(position);

        if (shop.getImage().contains("png") || shop.getImage().contains("jpg")) {
            Glide.with(context).load(shop.getImage()).into(holder.iv_Image);
        } else {
            holder.iv_Image.setImageResource(R.drawable.profile_new);
        }

        holder.tv_Name.setText(shop.getName());
        holder.tv_Address.setText(shop.getAddress());
        holder.tv_Rate.setText(shop.getRating());
        holder.bar_Rating.setRating(Float.parseFloat(shop.getRating()));

        if (shop.getAvailable().equalsIgnoreCase("1")) {
            holder.tv_Available.setVisibility(View.VISIBLE);
            holder.tv_Notavailable.setVisibility(View.GONE);
        } else {
            holder.tv_Available.setVisibility(View.GONE);
            holder.tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public int getItemCount() {
        return medicalShops.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_Image;
        private TextView tv_Name, tv_Address, tv_Rate, tv_Available, tv_Notavailable;
        private RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Image = itemView.findViewById(R.id.iv_Image);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Rate = itemView.findViewById(R.id.tv_Rate);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);
            tv_Available = itemView.findViewById(R.id.tv_Available);
            tv_Notavailable = itemView.findViewById(R.id.tv_Notavailable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MedicalShopProfileActivity.class)
                            .putExtra("wallet", ((MedicalShopListActivity) context).wallet)
                            .putExtra("donated", ((MedicalShopListActivity) context).donated)
                            .putExtra("obj", "" + medicalShops.get(getAdapterPosition()).getObj())
                    );
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<MedicalShop> list) {
        medicalShops = list;
        notifyDataSetChanged();
    }

}
