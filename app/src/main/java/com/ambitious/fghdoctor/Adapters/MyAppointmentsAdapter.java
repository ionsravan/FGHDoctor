package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.AppointmentDetailActivity;
import com.ambitious.fghdoctor.Model.Appointment;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CircleImageView;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class MyAppointmentsAdapter extends RecyclerView.Adapter<MyAppointmentsAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<Appointment> appointments;

    public MyAppointmentsAdapter(Context context, ArrayList<Appointment> appointments) {
        this.context = context;
        this.appointments = appointments;
    }

    @NonNull
    @Override
    public MyAppointmentsAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.item_appointment, parent, false);

        return new MyAppointmentsAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyAppointmentsAdapter.MyViewHolder holder, int position) {


        Appointment app = appointments.get(position);

        if (app.getDoctor_image().contains("png") || app.getDoctor_image().contains("jpg")) {
            Glide.with(context).load(app.getDoctor_image()).into(holder.civ_Doctor);
        } else {
            holder.civ_Doctor.setImageResource(R.drawable.profile_new);
        }

        if (app.getDoctor_name().contains("Dr.")) {
            holder.tv_Name.setText(app.getDoctor_name() + " (" + app.getCategory_name() + ")");
        } else {
            holder.tv_Name.setText("Dr." + app.getDoctor_name() + " (" + app.getCategory_name() + ")");
        }
        holder.tv_Token.setText("Token Number : " + app.getToken());

        /*String t[] = app.getTime().split(":");
        if (Integer.parseInt(t[0]) < 12) {
            holder.tv_Time.setText(t[0] + ":" + t[1] + " AM");
        } else {
            holder.tv_Time.setText(t[0] + ":" + t[1] + " PM");
        }*/
        holder.tv_Time.setText(app.getTime());
        try {
            SimpleDateFormat inFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = inFormat.parse(app.getDate());
            SimpleDateFormat outFormat = new SimpleDateFormat("EEEE");
            String goal = outFormat.format(date);
            holder.tv_Day.setText(goal);

            SimpleDateFormat outFormat1 = new SimpleDateFormat("dd MMM yyyy");
            String datee = outFormat1.format(date);
            holder.tv_Date.setText(datee);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return appointments.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        public CircleImageView civ_Doctor;
        public TextView tv_Name, tv_Token, tv_Day, tv_Time, tv_Date;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            civ_Doctor = itemView.findViewById(R.id.civ_Doctor);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Token = itemView.findViewById(R.id.tv_Token);
            tv_Day = itemView.findViewById(R.id.tv_Day);
            tv_Time = itemView.findViewById(R.id.tv_Time);
            tv_Date = itemView.findViewById(R.id.tv_Date);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    context.startActivity(new Intent(context, AppointmentDetailActivity.class)
                            .putExtra("obj", "" + appointments.get(getAdapterPosition()).getObj()));
                    Animatoo.animateCard(context);

                }
            });
        }
    }
}
