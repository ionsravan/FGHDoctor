package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.ReportListActivity;
import com.ambitious.fghdoctor.Activities.WebviewActivity;
import com.ambitious.fghdoctor.Model.ReportsFiles;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class ReportsListAdpater extends RecyclerView.Adapter<ReportsListAdpater.MyViewHolder> {

    private Context context;
    private ArrayList<ReportsFiles> files;
    public OnClick click;

    public ReportsListAdpater(Context context, ArrayList<ReportsFiles> files, OnClick onClick) {
        this.context = context;
        this.files = files;
        this.click = onClick;
    }

    public ReportsListAdpater(Context context, ArrayList<ReportsFiles> files) {
        this.context = context;
        this.files = files;
    }

    @NonNull
    @Override
    public ReportsListAdpater.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_reports, parent, false);

        return new ReportsListAdpater.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportsListAdpater.MyViewHolder holder, int position) {

        ReportsFiles file = files.get(position);

        if (file.getFile_type().contains("pdf")) {
            holder.iv.setImageResource(R.drawable.ic_file);
        } else {
            Glide.with(context).load(file.getFile()).into(holder.iv);
        }

    }

    @Override
    public int getItemCount() {
        return files.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv = itemView.findViewById(R.id.iv);

            iv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    click.onImageclick(getAdapterPosition());
                    if (files.get(getAdapterPosition()).getFile_type().contains("pdf")) {
                        String url = files.get(getAdapterPosition()).getFile();
                        context.startActivity(new Intent(context, WebviewActivity.class)
                                .putExtra("url", "" + url));
                        Animatoo.animateCard(context);

                    } else {
                        ArrayList<String> imagesStrings = new ArrayList<>();
                        imagesStrings.add(files.get(getAdapterPosition()).getFile());
                        ((ReportListActivity) context).openImage(0, imagesStrings);
                    }
                }
            });
        }
    }

    public interface OnClick {

        void onImageclick(int pos);

    }
}
