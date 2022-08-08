package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Model.BloodGroup;
import com.ambitious.fghdoctor.Model.MarketProduct;
import com.ambitious.fghdoctor.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MarketGroupAdapter extends RecyclerView.Adapter<MarketGroupAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MarketProduct> bloodGroups;

    public MarketGroupAdapter(Context context, ArrayList<MarketProduct> bloodGroups) {
        this.context = context;
        this.bloodGroups = bloodGroups;
    }

    @NonNull
    @Override
    public MarketGroupAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_market_grp, parent, false);

        return new MarketGroupAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MarketGroupAdapter.MyViewHolder holder, int position) {

        MarketProduct grp = bloodGroups.get(position);

        if (grp.getImgs().contains("png") || grp.getImgs().contains("jpg")) {
            Glide.with(context).load(grp.getImgs()).into(holder.imageView);
        } else {
            holder.imageView.setImageResource(R.drawable.profile_new);
        }

        holder.txtProductName.setText(grp.getpName());
        holder.txtProductPriceWeight.setText("\u20b9" + grp.getPrice() + "/-"+grp.getWeight());

    }

    @Override
    public int getItemCount() {
        return bloodGroups.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtProductName, txtProductPriceWeight;
        private ImageView imageView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtProductPriceWeight = itemView.findViewById(R.id.txtProductPriceWeight);
            imageView = itemView.findViewById(R.id.imgView);
        }
    }
}
