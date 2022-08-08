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

import com.ambitious.fghdoctor.Activities.DoctorListActivity;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.Category;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class AppointmentCategoryAdapter extends RecyclerView.Adapter<AppointmentCategoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Category> categories;

    public AppointmentCategoryAdapter(Context context, ArrayList<Category> categories) {
        this.context = context;
        this.categories = categories;
    }

    @NonNull
    @Override
    public AppointmentCategoryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_category, parent, false);

        return new AppointmentCategoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AppointmentCategoryAdapter.MyViewHolder holder, int position) {

        Category cat = categories.get(position);

        if (!cat.getImg().equalsIgnoreCase("")) {
            if (cat.getImg().contains("png") || cat.getImg().contains("jpg")) {
                if (!cat.getImg().contains("http")) {
                    Glide.with(context).load("http://webuddys.in//FGH//" + cat.getImg()).into(holder.iv_Catimg);
                } else {
                    Glide.with(context).load(cat.getImg()).into(holder.iv_Catimg);
                }
            } else {
                holder.iv_Catimg.setImageResource(R.drawable.dentist);
            }
        } else {
            holder.iv_Catimg.setImageResource(R.drawable.dentist);
        }
        holder.tv_Catname.setText(cat.getName());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Catimg, iv_Next;
        public TextView tv_Catname;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Catimg = itemView.findViewById(R.id.iv_Catimg);
            iv_Next = itemView.findViewById(R.id.iv_Next);
            tv_Catname = itemView.findViewById(R.id.tv_Catname);

            iv_Next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, DoctorListActivity.class)
                            .putExtra("head", "" + categories.get(getAdapterPosition()).getName())
                            .putExtra("catimg", "" + categories.get(getAdapterPosition()).getImg())
                            .putExtra("catid", "" + categories.get(getAdapterPosition()).getId()));
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<Category> list) {
        categories = list;
        notifyDataSetChanged();
    }
}
