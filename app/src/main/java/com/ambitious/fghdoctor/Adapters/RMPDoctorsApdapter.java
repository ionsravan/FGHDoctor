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

import com.ambitious.fghdoctor.Activities.LabListActivity;
import com.ambitious.fghdoctor.Activities.MedicalShopProfileActivity;
import com.ambitious.fghdoctor.Activities.RMPDoctorListActivity;
import com.ambitious.fghdoctor.Activities.RMPDoctorProfileActivity;
import com.ambitious.fghdoctor.Model.MedicalShop;
import com.ambitious.fghdoctor.Model.RMP;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class RMPDoctorsApdapter extends RecyclerView.Adapter<RMPDoctorsApdapter.MyViewHolder> {

    private Context context;
    private ArrayList<RMP> rmpArrayList;

    public RMPDoctorsApdapter(Context context, ArrayList<RMP> rmpArrayList) {
        this.context = context;
        this.rmpArrayList = rmpArrayList;
        Collections.reverse(this.rmpArrayList);
    }

    @NonNull
    @Override
    public RMPDoctorsApdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_rmp, parent, false);

        return new RMPDoctorsApdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RMPDoctorsApdapter.MyViewHolder holder, int position) {
        RMP rmp = rmpArrayList.get(position);

        if (rmp.getImage().contains("png") || rmp.getImage().contains("jpg")) {
            Glide.with(context).load(rmp.getImage()).into(holder.iv_Image);
        } else {
            holder.iv_Image.setImageResource(R.drawable.profile_new);
        }

        holder.tv_Name.setText(rmp.getName());
        holder.tv_Username.setText("Dr."+rmp.getUser_name());
        holder.tv_Address.setText(rmp.getAddress());
        holder.tv_Rate.setText(rmp.getRating());
        holder.bar_Rating.setRating(Float.parseFloat(rmp.getRating()));

        if (rmp.getAvailable().equalsIgnoreCase("1")) {
            holder.tv_Available.setVisibility(View.VISIBLE);
            holder.tv_Notavailable.setVisibility(View.GONE);
        } else {
            holder.tv_Available.setVisibility(View.GONE);
            holder.tv_Notavailable.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        return rmpArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_Image;
        private TextView tv_Name, tv_Username, tv_Address, tv_Rate, tv_Available, tv_Notavailable;
        private RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            iv_Image = itemView.findViewById(R.id.iv_Image);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Username = itemView.findViewById(R.id.tv_Username);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Rate = itemView.findViewById(R.id.tv_Rate);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);
            tv_Available = itemView.findViewById(R.id.tv_Available);
            tv_Notavailable = itemView.findViewById(R.id.tv_Notavailable);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                  /*  context.startActivity(new Intent(context, RMPDoctorProfileActivity.class)
                            .putExtra("wallet", ((RMPDoctorListActivity) context).wallet)
                            .putExtra("obj", "" + rmpArrayList.get(getAdapterPosition()).getObj()));*/
                    context.startActivity(new Intent(context, MedicalShopProfileActivity.class)
                            .putExtra("wallet", ((RMPDoctorListActivity) context).wallet)
                            .putExtra("donated", ((RMPDoctorListActivity) context).donated)
                            .putExtra("obj", "" + rmpArrayList.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<RMP> list) {
        rmpArrayList = list;
        notifyDataSetChanged();
    }
}
