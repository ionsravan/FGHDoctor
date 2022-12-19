package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Model.Noti;
import com.ambitious.fghdoctor.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ViewPager2Adapter extends RecyclerView.Adapter<ViewPager2Adapter.ViewHolder> {

    // Array of images
    // Adding images from drawable folder
    private ArrayList<Noti> notiArrayList ;
    private Context ctx;

    // Constructor of our ViewPager2Adapter class
    public ViewPager2Adapter(Context ctx, ArrayList<Noti> notiArrayList) {
        this.ctx = ctx;
        this.notiArrayList = notiArrayList;
    }

    // This method returns our layout
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(ctx).inflate(R.layout.viewpager_holder, parent, false);
        return new ViewHolder(view);
    }

    // This method binds the screen with the view
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // This will set the images in imageview
        if (!notiArrayList.get(position).getImage().equalsIgnoreCase("http://fghdoctors.com/admin/")) {
            holder.images.setVisibility(View.VISIBLE);
            Glide.with(ctx).load(notiArrayList.get(position).getImage()).into(holder.images);
        } else {
            holder.images.setImageResource(R.drawable.logo);
        }
        holder.txtTitle.setText(notiArrayList.get(position).getTitle());
        holder.txtMsg.setText(notiArrayList.get(position).getMessage());
        holder.txtTime.setText(notiArrayList.get(position).getTime_Ago());

    }

    // This Method returns the size of the Array
    @Override
    public int getItemCount() {
        return notiArrayList.size();
    }

    // The ViewHolder class holds the view
    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView images;
        TextView txtTitle,txtMsg,txtTime;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            images = itemView.findViewById(R.id.iv_Image);
            txtTitle = itemView.findViewById(R.id.tv_Title);
            txtMsg = itemView.findViewById(R.id.tv_Msg);
            txtTime = itemView.findViewById(R.id.tv_Time);
        }
    }
}
