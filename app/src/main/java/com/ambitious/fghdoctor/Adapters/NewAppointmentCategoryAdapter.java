package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Model.Category;
import com.ambitious.fghdoctor.R;
import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class NewAppointmentCategoryAdapter extends RecyclerView.Adapter<NewAppointmentCategoryAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Category> categories;
    private CLick cLick;

    public NewAppointmentCategoryAdapter(Context context, ArrayList<Category> categories, CLick cLick) {
        this.context = context;
        this.categories = categories;
        this.cLick = cLick;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(context).inflate(R.layout.item_cat, parent, false);

        return new NewAppointmentCategoryAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Category cat = categories.get(position);

        if (!cat.getImg().equalsIgnoreCase("")) {
            if (cat.getImg().contains("png") || cat.getImg().contains("jpg")) {
                if (!cat.getImg().contains("http")) {
                    Glide.with(context).load("http://webuddys.in//FGH//" + cat.getImg()).into(holder.iv_Cat);
                } else {
                    Glide.with(context).load(cat.getImg()).into(holder.iv_Cat);
                }
            } else {
                holder.iv_Cat.setImageResource(R.drawable.dentist);
            }
        } else {
            holder.iv_Cat.setImageResource(R.drawable.launcher);
        }

        if (cat.isIs_Sel()) {
            holder.tv_Name.setTextColor(context.getResources().getColor(R.color.colorBase));
            holder.tv_Name.setTypeface(Typeface.DEFAULT_BOLD);
        } else {
            holder.tv_Name.setTextColor(context.getResources().getColor(R.color.colorGray));
            holder.tv_Name.setTypeface(Typeface.DEFAULT);
        }

        holder.tv_Name.setText(cat.getName());

    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public ImageView iv_Cat;
        public TextView tv_Name;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            iv_Cat = itemView.findViewById(R.id.iv_Cat);
            tv_Name = itemView.findViewById(R.id.tv_Name);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    cLick.onSelect(getAdapterPosition());
                    for (int i = 0; i < categories.size(); i++) {

                        if (i == getAdapterPosition()) {
                            categories.get(getAdapterPosition()).setIs_Sel(true);
                        } else {
                            categories.get(i).setIs_Sel(false);
                        }

                    }
                    notifyDataSetChanged();
                }
            });

        }
    }

    public void updateList(ArrayList<Category> list) {
        categories = list;
        notifyDataSetChanged();
    }

    public interface CLick {

        void onSelect(int pos);

    }
}
