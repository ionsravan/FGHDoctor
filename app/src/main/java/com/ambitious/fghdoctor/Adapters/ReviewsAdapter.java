package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Model.Reviews;
import com.ambitious.fghdoctor.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Reviews> reviews;

    public ReviewsAdapter(Context context, ArrayList<Reviews> reviews) {
        this.context = context;
        this.reviews = reviews;
        Collections.reverse(this.reviews);
    }

    @NonNull
    @Override
    public ReviewsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_review, parent, false);

        return new ReviewsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsAdapter.MyViewHolder holder, int position) {


        Reviews rev = reviews.get(position);

        if (rev.getUser_image().contains("png") || rev.getUser_image().contains("jpg")) {
            Glide.with(context).load(rev.getUser_image()).into(holder.iv_Profile);
        } else {
            holder.iv_Profile.setImageResource(R.drawable.profile_new);
        }

        if (rev.getReview().equalsIgnoreCase("")) {
            holder.tv_Review.setVisibility(View.GONE);
        } else {
            holder.tv_Review.setVisibility(View.VISIBLE);
        }

        holder.tv_Name.setText(rev.getName());
        holder.tv_Review.setText(rev.getReview());
        holder.tv_Timeago.setText(rev.getTime_ago());
        holder.tv_Rating.setText("(" + rev.getRating() + ")");
        holder.bar_Rating.setRating(Float.parseFloat(rev.getRating()));

    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Profile;
        public TextView tv_Name, tv_Review, tv_Rating, tv_Timeago;
        public RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Profile = itemView.findViewById(R.id.iv_Profile);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Review = itemView.findViewById(R.id.tv_Review);
            tv_Rating = itemView.findViewById(R.id.tv_Rating);
            tv_Timeago = itemView.findViewById(R.id.tv_Timeago);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);
        }
    }
}
