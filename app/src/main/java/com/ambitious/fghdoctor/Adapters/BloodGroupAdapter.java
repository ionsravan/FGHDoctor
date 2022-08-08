package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Model.BloodGroup;
import com.ambitious.fghdoctor.R;

import java.util.ArrayList;

public class BloodGroupAdapter extends RecyclerView.Adapter<BloodGroupAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<BloodGroup> bloodGroups;

    public BloodGroupAdapter(Context context, ArrayList<BloodGroup> bloodGroups) {
        this.context = context;
        this.bloodGroups = bloodGroups;
    }

    @NonNull
    @Override
    public BloodGroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_blood_grp, parent, false);

        return new BloodGroupAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BloodGroupAdapter.MyViewHolder holder, int position) {

        BloodGroup grp = bloodGroups.get(position);

        if (grp.getName().contains("+")) {

            String bg = grp.getName();

//            String nm[] = bg.split("/+");

            holder.tv_Grpname.setText(bg);
            holder.tv_Grpsign.setText("+");
        } else {
            String bg = grp.getName();

//            String nm[] = bg.split("-");

            holder.tv_Grpname.setText(bg);
            holder.tv_Grpsign.setText("-");
        }
        holder.tv_Prc.setText("Rs." + grp.getPrice() + "/- 200ml");

    }

    @Override
    public int getItemCount() {
        return bloodGroups.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Grpname, tv_Grpsign, tv_Prc;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Grpname = itemView.findViewById(R.id.tv_Grpname);
            tv_Grpsign = itemView.findViewById(R.id.tv_Grpsign);
            tv_Prc = itemView.findViewById(R.id.tv_Prc);
        }
    }
}
