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

import com.ambitious.fghdoctor.Activities.FoodBankProfileActivity;
import com.ambitious.fghdoctor.Activities.HelpingSoldierProfileActivity;
import com.ambitious.fghdoctor.Model.FoodBank;
import com.ambitious.fghdoctor.Model.Helping;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class FoodBankListAdpter extends RecyclerView.Adapter<FoodBankListAdpter.MyViewHolder> {

    private Context context;
    private ArrayList<FoodBank> foodBanks;

    public FoodBankListAdpter(Context context, ArrayList<FoodBank> foodBanks) {
        this.context = context;
        this.foodBanks = foodBanks;
        Collections.reverse(this.foodBanks);
    }

    @NonNull
    @Override
    public FoodBankListAdpter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_blood, parent, false);

        return new FoodBankListAdpter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodBankListAdpter.MyViewHolder holder, int position) {
        FoodBank donor = foodBanks.get(position);

        if (donor.getUser_image().contains("png") || donor.getUser_image().contains("jpg")) {
            Glide.with(context).load(donor.getUser_image()).into(holder.iv_User);
        } else {
            holder.iv_User.setImageResource(R.drawable.profile_new);
        }

        holder.tv_Name.setText(donor.getName());
        holder.tv_Address.setText(donor.getAddress());
        holder.tv_Rating.setText("(" + donor.getRating() + ")");
        holder.bar_Rating.setRating(Float.parseFloat(donor.getRating()));

        if (donor.getAvailable().equalsIgnoreCase("1")) {
            holder.tv_Available.setVisibility(View.VISIBLE);
            holder.tv_Notavailable.setVisibility(View.GONE);
        } else {
            holder.tv_Available.setVisibility(View.GONE);
            holder.tv_Notavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return foodBanks.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_User;
        public TextView tv_Name, tv_Rating, tv_Address, tv_Available, tv_Notavailable, tv_Bgroup;
        public RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_User = itemView.findViewById(R.id.iv_User);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Rating = itemView.findViewById(R.id.tv_Rating);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Available = itemView.findViewById(R.id.tv_Available);
            tv_Notavailable = itemView.findViewById(R.id.tv_Notavailable);
            tv_Bgroup = itemView.findViewById(R.id.tv_Bgroup);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, FoodBankProfileActivity.class)
                            .putExtra("head", tv_Name.getText().toString())
                            .putExtra("obj", foodBanks.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<FoodBank> list) {
        foodBanks = list;
        notifyDataSetChanged();
    }
}
