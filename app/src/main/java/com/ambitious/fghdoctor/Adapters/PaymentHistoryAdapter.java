package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
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
import com.ambitious.fghdoctor.Model.HistoryModel;
import com.ambitious.fghdoctor.Model.MarketPriceList;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PaymentHistoryAdapter extends RecyclerView.Adapter<PaymentHistoryAdapter.MyViewHolder> {
    private Context context;
    private ArrayList<HistoryModel> historyModelArrayList;

    public PaymentHistoryAdapter(Context context, ArrayList<HistoryModel> historyModelArrayList) {
        this.context = context;
        this.historyModelArrayList = historyModelArrayList;
        Collections.reverse(this.historyModelArrayList);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_payment_history, parent, false);

        return new PaymentHistoryAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        HistoryModel historyModel = historyModelArrayList.get(position);

        holder.txtId.setText("Transaction Id : "+historyModel.getWallet_txn_id());
        holder.txtPrice.setText("\u20b9"+historyModel.getAmount());
        holder.txtRemark.setText(historyModel.getRemark());

        DateTimeFormatter formatter = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
            LocalDateTime dateTime = LocalDateTime.parse(historyModel.getEntrydt(), formatter);
            DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("dd MMM yyyy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm a");
            System.out.println(dateTime.format(formatter2));
            holder.txtDate.setText("Date : "+dateTime.format(formatter2) +"    Time : "+dateTime.format(timeFormatter));

        }



    }

    @Override
    public int getItemCount() {
        return historyModelArrayList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView txtId, txtRemark,txtDate,txtPrice;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            txtId = itemView.findViewById(R.id.txtId);
            txtRemark = itemView.findViewById(R.id.txtRemark);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtPrice = itemView.findViewById(R.id.txtPrice);

        }
    }



}
