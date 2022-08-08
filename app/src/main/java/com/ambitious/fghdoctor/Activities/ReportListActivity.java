package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ambitious.fghdoctor.Adapters.MyReportAdapter;
import com.ambitious.fghdoctor.Adapters.ReportsListAdpater;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.MyReport;
import com.ambitious.fghdoctor.Model.ReportsFiles;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class ReportListActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private EditText et_Search;
    private TextView tv_Notavailable;
    private RecyclerView rv_Myreports;
    private ArrayList<MyReport> myReports;
    private MyReportAdapter adpater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_list);
        finds();

        if (getIntent().getExtras() != null) {

            String arr = getIntent().getStringExtra("arr");

            try {

                JSONArray array = new JSONArray(arr);
                myReports = new ArrayList<>();
                for (int i = 0; i < array.length(); i++) {

                    JSONObject result = array.getJSONObject(i);
                    Log.d("TAG", "onCreate: "+result);
                    String lab_id = result.optString("lab_id");
                    String name = result.optString("name");
                    String mobile = result.optString("mobile");
                    String date = result.optString("date");
                    String hospital_name = result.optString("hospital_name");
                    String description = result.optString("description");
                    JSONArray files = result.optJSONArray("files");

                    MyReport rep = new MyReport();

                    rep.setLab_id(lab_id);
                    rep.setName(name);
                    rep.setMobile(mobile);
                    rep.setDate(date);
                    rep.setDate(date);
                    rep.setHospital_name(hospital_name);
                    rep.setDescription(description);
                    rep.setFilesarr(files.toString());

                    myReports.add(rep);
                }

                adpater = new MyReportAdapter(mContext, myReports);
                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                manager.setOrientation(RecyclerView.VERTICAL);
                rv_Myreports.setLayoutManager(manager);
                rv_Myreports.setAdapter(adpater);

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }

        et_Search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (myReports.size() > 0) {
                    filter(s.toString());
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Any Reports is not available.");
                }
            }
        });
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        et_Search = findViewById(R.id.et_Search);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rv_Myreports = findViewById(R.id.rv_Myreports);

        iv_Bck.setOnClickListener(this);

    }

    private void filter(String text) {

        ArrayList<MyReport> temp = new ArrayList();
        for (MyReport d : myReports) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getHospital_name().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getHospital_name().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Myreports.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adpater.updateList(temp);
        } else {
            rv_Myreports.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

        }

    }

    public void openImage(int pos, ArrayList<String> imagesStrings) {

        Dialog dialog = new Dialog(mContext, R.style.MyDialogTheme);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.imagedialog_view);
        ViewPager viewpager_a = (ViewPager) dialog.findViewById(R.id.viewpager_a);

        ViewPagerAdapter1 pagerAdapter = new ViewPagerAdapter1(mContext, imagesStrings, pos);
        viewpager_a.setAdapter(pagerAdapter);
        viewpager_a.setCurrentItem(pos);

        dialog.show();
    }

    //************** view pager Adapter for image swiping *************//

    private class ViewPagerAdapter1 extends PagerAdapter {
        Context context;
        ViewPagerAdapter1.ViewHolder holder;
        Bitmap bitmap;
        int pos;
        ArrayList<String> ImagesArray;

        ArrayList<String> list = new ArrayList<>();
        LayoutInflater inflater;

        private ViewPagerAdapter1(Context context, ArrayList<String> ImagesArray, int position) {
            this.context = context;
            this.pos = position;
            this.ImagesArray = ImagesArray;
        }

        @Override
        public void notifyDataSetChanged() {
            // TODO Auto-generated method stub
            super.notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            //return 1;
            return ImagesArray.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == ((RelativeLayout) object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            View convertView = null;
            if (convertView == null) {
                holder = new ViewPagerAdapter1.ViewHolder();
                inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.offer_grid_item, container, false);
                holder.imageView = (ImageView) convertView.findViewById(R.id.off_img);
                holder.iv_Paly = (ImageView) convertView.findViewById(R.id.iv_Paly);
                container.addView(convertView);
                convertView.setTag(holder);

            } else {

                holder = (ViewPagerAdapter1.ViewHolder) convertView.getTag();
            }
            if (ImagesArray.get(position).contains("mp4")) {
                holder.iv_Paly.setVisibility(View.VISIBLE);
                MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();

                mediaMetadataRetriever.setDataSource(ImagesArray.get(position), new HashMap<String, String>());
                Bitmap bmFrame = mediaMetadataRetriever.getFrameAtTime(100); //unit in microsecond
                holder.imageView.setImageBitmap(bmFrame);
            } else {
                holder.iv_Paly.setVisibility(View.GONE);
                Glide.with(context).load(ImagesArray.get(position)).into(holder.imageView);
            }
            return convertView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((RelativeLayout) object);
        }

        private class ViewHolder {
            ImageView imageView, iv_Paly;
        }
    }
}