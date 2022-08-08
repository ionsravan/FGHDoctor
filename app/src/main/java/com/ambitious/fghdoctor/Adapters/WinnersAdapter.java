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

import com.ambitious.fghdoctor.Activities.VideoViewActivity;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.Winners;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CircleImageView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class WinnersAdapter extends RecyclerView.Adapter<WinnersAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Winners> winners;

    public WinnersAdapter(Context context, ArrayList<Winners> winners) {
        this.context = context;
        this.winners = winners;
//        Collections.reverse(this.winners);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_winners, parent, false);

        return new WinnersAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Winners win = winners.get(position);

        Glide.with(context).load(win.getUser_image()).into(holder.iv_User);
        Glide.with(context).load(win.getVideo()).into(holder.iv_Video);
        holder.tv_Name.setText("Name : " + win.getName());
        holder.tv_Mobile.setText("Mobile : " + win.getMobile());
        holder.tv_City.setText("City : " + win.getCity());

        if (!win.getVideo_title().equalsIgnoreCase("")) {
            holder.tv_Title.setVisibility(View.VISIBLE);
            holder.tv_Title.setText(win.getVideo_title());
        }

    }

    @Override
    public int getItemCount() {
        return winners.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Video, iv_Play, iv_Share;
        public TextView tv_Name, tv_Mobile, tv_City, tv_Title;
        public CircleImageView iv_User;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_User = itemView.findViewById(R.id.iv_User);
            iv_Share = itemView.findViewById(R.id.iv_Share);
            iv_Video = itemView.findViewById(R.id.iv_Video);
            iv_Play = itemView.findViewById(R.id.iv_Play);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Mobile = itemView.findViewById(R.id.tv_Mobile);
            tv_City = itemView.findViewById(R.id.tv_City);
            tv_Title = itemView.findViewById(R.id.tv_Title);

            iv_Play.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, VideoViewActivity.class)
                            .putExtra("url", "" + winners.get(getAdapterPosition()).getVideo()));
                    Animatoo.animateCard(context);
                }
            });

            iv_Share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String msg = "*" + winners.get(getAdapterPosition()).getVideo_title() + "*\n" + "Watch Covid Winners Video " + "\n\nhttps://fghdoctors.com/fitnessvideo/" + winners.get(getAdapterPosition()).getUid() + "\n\nFor more such live updates and online Doctor Booking, download the FGHDoctor app.\n\nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor";
                    Intent shareIntent = new Intent();
                    shareIntent.setAction(Intent.ACTION_SEND);
                    shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                    shareIntent.setType("text/plain");
                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    context.startActivity(Intent.createChooser(shareIntent, "send"));
                }
            });
        }
    }

    public void updateList(ArrayList<Winners> list) {
        winners = list;
        notifyDataSetChanged();
    }
}
