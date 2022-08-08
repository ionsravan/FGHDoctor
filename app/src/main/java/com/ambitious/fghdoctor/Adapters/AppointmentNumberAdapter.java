package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.DoctorProfileActivity;
import com.ambitious.fghdoctor.Model.Tokens;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;

import java.util.ArrayList;
import java.util.Collections;

public class AppointmentNumberAdapter extends RecyclerView.Adapter<AppointmentNumberAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Tokens> tokens;

    public AppointmentNumberAdapter(Context context, ArrayList<Tokens> tokens) {
        this.context = context;
        this.tokens = tokens;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_appointmentnum, parent, false);

        return new AppointmentNumberAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Tokens toke = tokens.get(position);

        int tok = Integer.parseInt(toke.getToken());

        if (tok >= 0 && tok <= 9) {
            holder.tv_Count.setText("0" + (tok));
        } else {
            holder.tv_Count.setText("" + (tok));
        }

        if (toke.getBooked().equalsIgnoreCase("0") && toke.getBooked_by_me().equalsIgnoreCase("0")) {
            if (toke.isIs_sel()) {
                holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_base));
                holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorBase));
            } else {
                if (toke.isIs_enable()) {
                    holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle));
                    holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorBlack));
                    holder.tv_Mytoken.setVisibility(View.INVISIBLE);
                } else {
                    holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_gray));
                    holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorGray));
                    holder.tv_Mytoken.setVisibility(View.INVISIBLE);
                }
            }
        } else if (toke.getBooked().equalsIgnoreCase("1") && toke.getBooked_by_me().equalsIgnoreCase("0")) {
            holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_sel));
            holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.tv_Mytoken.setVisibility(View.INVISIBLE);
        } else if (toke.getBooked().equalsIgnoreCase("0") && toke.getBooked_by_me().equalsIgnoreCase("1")) {
            holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_sel));
            holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.tv_Mytoken.setVisibility(View.VISIBLE);
            holder.ll_Contain.setBackground(context.getResources().getDrawable(R.drawable.bg_box_mybook));
        } else if (toke.getBooked().equalsIgnoreCase("1") && toke.getBooked_by_me().equalsIgnoreCase("1")) {
            holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_sel));
            holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorWhite));
            holder.tv_Mytoken.setVisibility(View.VISIBLE);
            holder.ll_Contain.setBackground(context.getResources().getDrawable(R.drawable.bg_box_mybook));
        } else {
            if (toke.isIs_sel()) {
                holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_base));
                holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorBase));
            } else {
                holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle));
                holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorBlack));
                holder.tv_Mytoken.setVisibility(View.INVISIBLE);
            }
        }

        if (toke.getBooked_by_me().equalsIgnoreCase("0")) {
            holder.tv_Mytoken.setVisibility(View.INVISIBLE);
        } else {
            holder.tv_Mytoken.setVisibility(View.VISIBLE);
        }


        /*if (position >= 0 && position <= 6) {

            holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle_sel));
            holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorWhite));

        } else {
            holder.tv_Count.setBackground(context.getResources().getDrawable(R.drawable.bg_circle));
            holder.tv_Count.setTextColor(context.getResources().getColor(R.color.colorBlack));
        }*/

    }

    @Override
    public int getItemCount() {
        return tokens.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Count, tv_Mytoken;
        private LinearLayout ll_Contain;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Count = itemView.findViewById(R.id.tv_Count);
            tv_Mytoken = itemView.findViewById(R.id.tv_Mytoken);
            ll_Contain = itemView.findViewById(R.id.ll_Contain);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (tokens.get(getAdapterPosition()).getBooked().equalsIgnoreCase("1") && tokens.get(getAdapterPosition()).getBooked_by_me().equalsIgnoreCase("0")) {
                        CustomSnakbar.showDarkSnakabar(context, v, "This Token is already booked.");
                    } else if (tokens.get(getAdapterPosition()).getBooked().equalsIgnoreCase("0") && tokens.get(getAdapterPosition()).getBooked_by_me().equalsIgnoreCase("1")) {
                        CustomSnakbar.showDarkSnakabar(context, v, "This Token is already booked by you.");
                    } else if (tokens.get(getAdapterPosition()).getBooked().equalsIgnoreCase("1") && tokens.get(getAdapterPosition()).getBooked_by_me().equalsIgnoreCase("1")) {
                        CustomSnakbar.showDarkSnakabar(context, v, "This Token is already booked by you.");
                    } else {

                        for (int i = 0; i < tokens.size(); i++) {

                            if (i == getAdapterPosition()) {
                                if (tokens.get(getAdapterPosition()).isIs_enable()) {
                                    tokens.get(getAdapterPosition()).setIs_sel(true);
                                    ((DoctorProfileActivity) context).token_no = tokens.get(getAdapterPosition()).getToken();
                                    ((DoctorProfileActivity) context).doctor_shift_id = tokens.get(getAdapterPosition()).getDoctor_shift_id();
                                    ((DoctorProfileActivity) context).service_charge = tokens.get(getAdapterPosition()).getService_charge();
                                } else {
                                    ((DoctorProfileActivity) context).token_no = "";
                                    ((DoctorProfileActivity) context).doctor_shift_id = "";
                                    ((DoctorProfileActivity) context).service_charge = "";
                                    CustomSnakbar.showDarkSnakabar(context, v, "Previous Token is Empty!\nPlease Select Previous one First!");
                                }
                                /*if (i == 0) {
                                    tokens.get(getAdapterPosition()).setIs_sel(true);
                                } else {
                                    if (tokens.get((i - 1)).getBooked().equalsIgnoreCase("1")) {
                                        tokens.get(getAdapterPosition()).setIs_sel(true);
                                    } else if (tokens.get((i - 1)).getBooked_by_me().equalsIgnoreCase("1")) {
                                        tokens.get(getAdapterPosition()).setIs_sel(true);
                                    } else {
                                        CustomSnakbar.showDarkSnakabar(context, v, "Previous Token is Empty!\nPlease Select Previous one First!");
                                    }
                                }*/
                            } else {
                                tokens.get(i).setIs_sel(false);
                            }
                        }

                        notifyDataSetChanged();
                    }

                }
            });
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }


}
