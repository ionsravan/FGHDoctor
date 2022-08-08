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

import com.ambitious.fghdoctor.Activities.NotificationDetailActivity;
import com.ambitious.fghdoctor.Model.Noti;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Noti> notiArrayList;

    public NotificationAdapter(Context context, ArrayList<Noti> notiArrayList) {
        this.context = context;
        this.notiArrayList = notiArrayList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_noti, parent, false);

        return new NotificationAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if (!notiArrayList.get(position).getImage().equalsIgnoreCase("http://fghdoctors.com/admin/")) {
            holder.iv_Image.setVisibility(View.VISIBLE);
            Glide.with(context).load(notiArrayList.get(position).getImage()).into(holder.iv_Image);
        } else {
            holder.iv_Image.setImageResource(R.drawable.logo);
        }

        holder.tv_Title.setText(notiArrayList.get(position).getTitle());
        holder.tv_Msg.setText(notiArrayList.get(position).getMessage());
        holder.tv_Time.setText(notiArrayList.get(position).getTime_Ago());

    }

    @Override
    public int getItemCount() {
        return notiArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_Image;
        private TextView tv_Title, tv_Time, tv_Msg;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Image = itemView.findViewById(R.id.iv_Image);
            tv_Title = itemView.findViewById(R.id.tv_Title);
            tv_Time = itemView.findViewById(R.id.tv_Time);
            tv_Msg = itemView.findViewById(R.id.tv_Msg);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, NotificationDetailActivity.class)
                            .putExtra("nid", "" + notiArrayList.get(getAdapterPosition()).getId())
                            .putExtra("img", "" + notiArrayList.get(getAdapterPosition()).getImage())
                            .putExtra("title", "" + notiArrayList.get(getAdapterPosition()).getTitle())
                            .putExtra("msg", "" + notiArrayList.get(getAdapterPosition()).getMessage())
                    );
                    Animatoo.animateCard(context);
                }
            });
        }
    }
}
