package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.R;

public class BloodDonorHistryAdapter extends RecyclerView.Adapter<BloodDonorHistryAdapter.MyViewHolder> {

    private Context context;

    public BloodDonorHistryAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public BloodDonorHistryAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_image_histry,parent,false);
        return new BloodDonorHistryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodDonorHistryAdapter.MyViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 9;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
