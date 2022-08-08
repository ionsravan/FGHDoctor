package com.ambitious.fghdoctor.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ambitious.fghdoctor.Activities.ReportListActivity;
import com.ambitious.fghdoctor.Activities.WebviewActivity;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.MyReport;
import com.ambitious.fghdoctor.Model.ReportsFiles;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class MyReportAdapter extends RecyclerView.Adapter<MyReportAdapter.MyViewHolder> {

    private Context context;
    private ArrayList<MyReport> myReports;

    public MyReportAdapter(Context context, ArrayList<MyReport> myReports) {
        this.context = context;
        this.myReports = myReports;
    }

    @NonNull
    @Override
    public MyReportAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View mView = LayoutInflater.from(context).inflate(R.layout.item_myreport, parent, false);

        return new MyReportAdapter.MyViewHolder(mView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyReportAdapter.MyViewHolder holder, int position) {

        MyReport rep = myReports.get(position);

        try {
            JSONArray files = new JSONArray(rep.getFilesarr());
            ArrayList<ReportsFiles> myfiles = new ArrayList<>();
            for (int i = 0; i < files.length(); i++) {


                JSONObject object = files.getJSONObject(i);
                String file_type = object.optString("file_type");
                String file = object.optString("file");

                ReportsFiles myfile = new ReportsFiles();
                myfile.setFile_type(file_type);
                myfile.setFile(file);

                myfiles.add(myfile);

            }

            ReportsListAdpater adpater = new ReportsListAdpater(context, myfiles);
            GridLayoutManager manager = new GridLayoutManager(context, 3);
            manager.setOrientation(RecyclerView.VERTICAL);
            holder.rv_Reports.setLayoutManager(manager);
            holder.rv_Reports.setAdapter(adpater);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        holder.tv_Name.setText("Name : " + rep.getName());
        holder.tv_Number.setText("Mobile : " + rep.getMobile());

        if (rep.getDate().equalsIgnoreCase("")) {
            holder.tv_Date.setVisibility(View.GONE);
        } else {
            holder.tv_Date.setVisibility(View.VISIBLE);
            holder.tv_Date.setText("Date : " + rep.getDate());
        }

        if (rep.getHospital_name().equalsIgnoreCase("")) {
            holder.tv_Hospital.setVisibility(View.GONE);
        } else {
            holder.tv_Hospital.setVisibility(View.VISIBLE);
            holder.tv_Hospital.setText("Hospital Name : " + rep.getHospital_name());
        }

        if (rep.getDescription().equalsIgnoreCase("")) {
            holder.tv_Description.setVisibility(View.GONE);
        } else {
            holder.tv_Description.setVisibility(View.VISIBLE);
            holder.tv_Description.setText("Description : " + rep.getDescription());
        }

    }

    @Override
    public int getItemCount() {
        return myReports.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tv_Date, tv_Name, tv_Number, tv_Hospital, tv_Description;
        private RecyclerView rv_Reports;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_Date = itemView.findViewById(R.id.tv_Date);
            tv_Name = itemView.findViewById(R.id.tv_Name);
            tv_Number = itemView.findViewById(R.id.tv_Number);
            tv_Hospital = itemView.findViewById(R.id.tv_Hospital);
            tv_Description = itemView.findViewById(R.id.tv_Description);
            rv_Reports = itemView.findViewById(R.id.rv_Reports);
        }
    }

    public void updateList(ArrayList<MyReport> list) {
        myReports = list;
        notifyDataSetChanged();
    }
}
