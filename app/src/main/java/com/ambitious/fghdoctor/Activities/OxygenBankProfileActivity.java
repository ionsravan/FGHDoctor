package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.BloodGroupAdapter;
import com.ambitious.fghdoctor.Model.BloodGroup;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class OxygenBankProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_User;
    private TextView tv_Head, tv_Name, tv_Rating, tv_Address, tv_Description, tv_Priceoxcy, tv_Priceoxcon;
    private RatingBar bar_Rating;
    private RelativeLayout rl_Call, rl_Whatsapp;
    private LinearLayout ll_Oxcy, ll_Oxcon;
    private String contact = "";
    private ArrayList<String> imagesStringsProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_oxygen_bank_profile);
        finds();

        if (getIntent().getExtras() != null) {

            String obj = getIntent().getStringExtra("obj");

            try {
                JSONObject object = new JSONObject(obj);

                String user_id = object.optString("user_id");
                String user_type = object.optString("user_type");
                String name = object.optString("name");
                String mobile = object.optString("mobile");
                String email = object.optString("email");
                String username = object.optString("username");
                String password = object.optString("password");
                String address = object.optString("address");
                String about = object.optString("about");
                String city = object.optString("city");
                String distric = object.optString("distric");
                String blood_group = object.optString("blood_group");
                String aadhar = object.optString("aadhar");
                String vehicle_no = object.optString("vehicle_no");
                String user_image = object.optString("user_image");
                String images = object.optString("images");
                String available = object.optString("available");
                String rating = object.optString("rating");
                String product_name = object.optString("product_name");
                String product_price = object.optString("product_price");


                tv_Head.setText(name);
                if (user_image.contains("png") || user_image.contains("jpg")) {
                    imagesStringsProfile = new ArrayList<>();
                    imagesStringsProfile.add(user_image);
                    Glide.with(mContext).load(user_image).into(iv_User);
                } else {
                    iv_User.setImageResource(R.drawable.profile_new);
                }

                tv_Name.setText(name);
                tv_Address.setText(address + "," + city);
                if (!about.equalsIgnoreCase("")) {
                    tv_Description.setText("Description : " + about);
                } else {
                    tv_Description.setVisibility(View.GONE);
                }
                tv_Rating.setText("(" + rating + ")");
                bar_Rating.setRating(Float.parseFloat(rating));

                contact = "+91" + mobile.trim();

                if (product_name.contains("OxygenCylinder")) {
                    ll_Oxcy.setVisibility(View.VISIBLE);
                    if (user_type.equalsIgnoreCase("oxygen free")) {
                        tv_Priceoxcy.setText("Free");
                    } else {
                        if (product_price.contains(",")) {
                            String[] prc = product_price.split(",");
                            tv_Priceoxcy.setText("₹" + prc[0] + " /Day");
                        } else {
                            tv_Priceoxcy.setText("₹" + product_price + " /Day");
                        }
                    }

                }

                if (product_name.contains("OxygenConcentrator")) {
                    ll_Oxcon.setVisibility(View.VISIBLE);
                    if (user_type.equalsIgnoreCase("oxygen free")) {
                        tv_Priceoxcon.setText("Free");
                    } else {
                        if (product_price.contains(",")) {
                            String[] prc = product_price.split(",");
                            tv_Priceoxcon.setText("₹" + prc[1] + " /Day");
                        } else {
                            tv_Priceoxcon.setText("₹" + product_price + " /Day");
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_User = findViewById(R.id.iv_User);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Rating = findViewById(R.id.tv_Rating);
        tv_Address = findViewById(R.id.tv_Address);
        tv_Description = findViewById(R.id.tv_Description);
        tv_Priceoxcy = findViewById(R.id.tv_Priceoxcy);
        tv_Priceoxcon = findViewById(R.id.tv_Priceoxcon);
        bar_Rating = findViewById(R.id.bar_Rating);
        rl_Call = findViewById(R.id.rl_Call);
        ll_Oxcy = findViewById(R.id.ll_Oxcy);
        ll_Oxcon = findViewById(R.id.ll_Oxcon);
        rl_Whatsapp = findViewById(R.id.rl_Whatsapp);

        iv_Bck.setOnClickListener(this);
        rl_Call.setOnClickListener(this);
        rl_Whatsapp.setOnClickListener(this);
        iv_User.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.rl_Call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact));
                startActivity(intent);
                break;

            case R.id.rl_Whatsapp:
                String url = "https://api.whatsapp.com/send?phone=" + contact;
                try {
                    PackageManager pm = mContext.getPackageManager();
                    pm.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES);
                    Intent i = new Intent(Intent.ACTION_VIEW);
                    i.setData(Uri.parse(url));
                    startActivity(i);
                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(mContext, "Whatsapp app not installed in your phone", Toast.LENGTH_SHORT).show();
                    e.printStackTrace();
                }
                break;

            case R.id.iv_User:
                if (imagesStringsProfile != null) {
                    openImage(0, imagesStringsProfile);
                }
                break;

        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    private void openImage(int pos, ArrayList<String> imagesStrings) {
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