package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.AmbulanceDriverProfileActivity;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.MarketProduct;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collections;

public class MarketProductAdapter extends RecyclerView.Adapter<MarketProductAdapter.MyViewHolder> {
    ItemClickListener itemClickListener;
    private Context context;
    //  private ArrayList<String> drivers;
    private ArrayList<MarketProduct> marketProductArrayList;
    private boolean atleastone = false;
    private View view;

    public MarketProductAdapter(Context context, ArrayList<MarketProduct> marketProductArrayList, ItemClickListener itemClickListener) {
        this.context = context;
        this.marketProductArrayList = marketProductArrayList;
        this.itemClickListener = itemClickListener;
        Collections.reverse(this.marketProductArrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = LayoutInflater.from(context).inflate(R.layout.item_product, parent, false);

        return new MarketProductAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        MarketProduct marketProduct = marketProductArrayList.get(position);

        if (marketProduct.getImg() != null) {
            holder.imageView.setImageBitmap(marketProduct.getImg());
            if (!marketProduct.getpName().isEmpty() && !marketProduct.getPrice().isEmpty() && !marketProduct.getWeight().isEmpty() && marketProduct.getImg() != null) {
                holder.checkBox.setChecked(true);
                marketProduct.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
                marketProduct.setChecked(false);
            }


        } else {

            if (!marketProduct.getpName().isEmpty() && !marketProduct.getPrice().isEmpty() && !marketProduct.getWeight().isEmpty() && !marketProduct.getImgs().isEmpty()) {
                holder.checkBox.setChecked(true);
                marketProduct.setChecked(true);
            } else {
                holder.checkBox.setChecked(false);
                marketProduct.setChecked(false);

            }

            if (marketProduct.getImgs().contains("png") || marketProduct.getImgs().contains("jpg")) {
                Glide.with(context).load(marketProduct.getImgs()).into(holder.imageView);
            } else {
                holder.imageView.setImageResource(R.drawable.ic_add_box_blue);
            }

        }


        holder.etProductName.setText(marketProduct.getpName());
        holder.etPrice.setText(marketProduct.getPrice());
        holder.etWeight.setText(marketProduct.getWeight());


        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                if (marketProduct.getImg() == null || marketProduct.getImgs().isEmpty()) {
                    holder.checkBox.setChecked(false);
                   // CustomSnakbar.showSnakabar(context, view.findViewById(android.R.id.content), "Product Image Can't be Empty");

                    Toast.makeText(context, "Product Image Can't be Empty", Toast.LENGTH_SHORT).show();
                } else if (holder.etProductName.getText().toString().equalsIgnoreCase("")) {
                    holder.checkBox.setChecked(false);
                    Toast.makeText(context, "Please Enter Amount!", Toast.LENGTH_SHORT).show();
                   // CustomSnakbar.showDarkSnakabar(context, view.findViewById(android.R.id.content), "Please Enter Amount!");
                    holder.etProductName.setError("Can't be Empty!");
                    holder.etProductName.requestFocus();


                } else if (holder.etPrice.getText().toString().equalsIgnoreCase("")) {
                    holder.etPrice.setError("Can't be Empty!");
                    holder.etPrice.requestFocus();
                    holder.checkBox.setChecked(false);

                } else if (holder.etWeight.getText().toString().equalsIgnoreCase("")) {
                    holder.checkBox.setChecked(false);
                    holder.etWeight.setError("Can't be Empty!");
                    holder.etWeight.requestFocus();

                } else {
                    atleastone = true;
                    holder.checkBox.setChecked(true);

                    marketProduct.setpName(holder.etProductName.getText().toString());
                    marketProduct.setPrice(holder.etPrice.getText().toString());
                    marketProduct.setWeight(holder.etWeight.getText().toString());
                    marketProduct.setChecked(true);
                }
            } else {
                marketProduct.setpName(holder.etProductName.getText().toString());
                marketProduct.setPrice(holder.etPrice.getText().toString());
                marketProduct.setWeight(holder.etWeight.getText().toString());
                marketProduct.setChecked(false);
            }
        });

        holder.etProductName.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.etProductName.length() == 0) {
                    holder.checkBox.setChecked(false);
                }else {
                    marketProduct.setpName(s.toString());
                }
            }
        });

        holder.etPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.etPrice.length() == 0) {
                    holder.checkBox.setChecked(false);
                }else {
                    marketProduct.setPrice(s.toString());
                }
            }
        });

        holder.etWeight.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (holder.etWeight.length() == 0) {
                    holder.checkBox.setChecked(false);
                }else {
                    marketProduct.setWeight(s.toString());
                }
            }
        });

        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                itemClickListener.onClick(holder.getAdapterPosition(), marketProduct);
            }
        });


    }

    @Override
    public int getItemCount() {
        return marketProductArrayList.size();
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CheckBox checkBox;
        public ImageView imageView;
        public EditText etProductName, etPrice, etWeight;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.image);
            etProductName = itemView.findViewById(R.id.etProduct);
            etPrice = itemView.findViewById(R.id.etProductPrice);
            etWeight = itemView.findViewById(R.id.etProductWeight);
            checkBox = itemView.findViewById(R.id.checkbox);

        }
    }


    public void addItem(MarketProduct model) {
        marketProductArrayList.add(model);
        notifyDataSetChanged(); // This is to notify the adapter that the data in the recyclerview has been modified.
    }

    public interface ItemClickListener {
        void onClick(int position, MarketProduct marketProduct);
    }
}
