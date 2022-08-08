package com.ambitious.fghdoctor.Adapters;

import static androidx.core.content.PermissionChecker.checkPermission;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.SortedList;

import com.ambitious.fghdoctor.Activities.MarketPricesActivity;
import com.ambitious.fghdoctor.Activities.MarketPricesProfileActivity;
import com.ambitious.fghdoctor.Model.MarketPriceList;
import com.ambitious.fghdoctor.Model.MarketProduct;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MarketPricesListAdapter extends RecyclerView.Adapter<MarketPricesListAdapter.MyViewHolder> {
    ItemClickListener itemClickListener;
    private Context context;
    private ArrayList<MarketPriceList> marketPriceLists;
    private SortedList<MarketPriceList> mPersons;

    public MarketPricesListAdapter(Context context, ArrayList<MarketPriceList> marketPriceLists, ItemClickListener itemClickListener, List<MarketPriceList> persons) {
        this.context = context;
        this.marketPriceLists = marketPriceLists;
        this.itemClickListener = itemClickListener;
      /*  mPersons = new SortedList<>(MarketPriceList.class, new PersonListCallback());
        mPersons.addAll(persons);*/
        Collections.reverse(this.marketPriceLists);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_market_prices, parent, false);

        return new MarketPricesListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        MarketPriceList shop = marketPriceLists.get(position);

        if (shop.getImage().contains("png") || shop.getImage().contains("jpg")) {
            Glide.with(context).load(shop.getImage()).into(holder.iv_Image);
        } else {
            holder.iv_Image.setImageResource(R.drawable.profile_new);
        }

        holder.txtProductName.setText(shop.getProductName());
        holder.txtShopName.setText(shop.getShopName());
        holder.txtProductPriceWeight.setText("\u20b9"+shop.getPrice()+"/"+shop.getWeight());
        holder.tv_Address.setText(shop.getAddress());
        holder.tv_Rate.setText("("+shop.getRating()+")");
        holder.bar_Rating.setRating(Float.parseFloat(shop.getRating()));
        holder.imgShare.setOnClickListener(v -> {
            itemClickListener.onClick(holder.getAdapterPosition(), shop);

        });


    }

    @Override
    public int getItemCount() {
        return marketPriceLists.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private ImageView iv_Image,imgShare;
        private TextView txtProductName, txtShopName,txtProductPriceWeight,tv_Address, tv_Rate, tv_Available, tv_Notavailable;
        private RatingBar bar_Rating;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Image = itemView.findViewById(R.id.iv_Image);
            imgShare = itemView.findViewById(R.id.imgShare);
            txtProductName = itemView.findViewById(R.id.txtProductName);
            txtShopName = itemView.findViewById(R.id.txtShopName);
            txtProductPriceWeight = itemView.findViewById(R.id.txtProductPriceWeight);
            tv_Address = itemView.findViewById(R.id.tv_Address);
            tv_Rate = itemView.findViewById(R.id.tv_Rate);
            bar_Rating = itemView.findViewById(R.id.bar_Rating);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    context.startActivity(new Intent(context, MarketPricesProfileActivity.class)
                            .putExtra("wallet", ((MarketPricesActivity) context).wallet)
                            .putExtra("donated", ((MarketPricesActivity) context).donated)
                            .putExtra("obj", "" + marketPriceLists.get(getAdapterPosition()).getObj())
                    );
                    Animatoo.animateCard(context);
                }
            });
        }
    }

    public void updateList(ArrayList<MarketPriceList> list) {
        marketPriceLists = list;
        notifyDataSetChanged();
    }

    public interface ItemClickListener {
        void onClick(int position, MarketPriceList marketProduct);
    }

    /**
     * Implementation of callback for getting updates on person list changes.
     */
    private class PersonListCallback extends SortedList.Callback<MarketPriceList> {

        @Override
        public int compare(MarketPriceList p1, MarketPriceList p2) {
            String[] rank1 = p1.getPrice().split(",");
            String[] rank2 = p2.getPrice().split(",");
            int diff = Integer.parseInt(rank1[0]) - Integer.parseInt(rank2[0]);
            return (diff == 0) ? p1.getProductName().compareTo(p2.getProductName()) : diff;
        }

        @Override
        public void onInserted(int position, int count) {
            notifyItemInserted(position);
        }

        @Override
        public void onRemoved(int position, int count) {
            notifyItemRemoved(position);
        }

        @Override
        public void onMoved(int fromPosition, int toPosition) {
        }

        @Override
        public void onChanged(int position, int count) {
        }

        @Override
        public boolean areContentsTheSame(MarketPriceList oldItem, MarketPriceList newItem) {
            return false;
        }

        @Override
        public boolean areItemsTheSame(MarketPriceList item1, MarketPriceList item2) {
            return false;
        }

    }
}
