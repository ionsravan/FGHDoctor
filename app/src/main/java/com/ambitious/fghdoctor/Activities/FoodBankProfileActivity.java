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
import android.graphics.Color;
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

import com.ambitious.fghdoctor.Adapters.BloodDonorHistryAdapter;
import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class FoodBankProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Driver, iv_One, iv_Two, iv_Three;
    private TextView tv_Detail, tv_Histry, tv_Name, tv_Rating, tv_Address, tv_Description;
    private RelativeLayout rl_Whatsapp, rl_Call;
    private RecyclerView rv_Histry;
    private LinearLayout ll_Imgs;
    private BloodDonorHistryAdapter adapter;
    private RatingBar bar_Rating;
    private String contact = "";
    private ArrayList<String> imagesStringsProfile;
    private ArrayList<String> imagesStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_bank_profile);

        finds();
        setData("detail");

        if (getIntent().getExtras() != null) {

            try {

                String obj = getIntent().getStringExtra("obj");

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
                String village = object.optString("village");
                String city = object.optString("city");
                String distric = object.optString("distric");
                String blood_group = object.optString("blood_group");
                String aadhar = object.optString("aadhar");
                String vehicle_no = object.optString("vehicle_no");
                String user_image = object.optString("user_image");
                String images = object.optString("images");
                String available = object.optString("available");
                String rating = object.optString("rating");

                if (user_image.contains("png") || user_image.contains("jpg")) {
                    imagesStringsProfile = new ArrayList<>();
                    imagesStringsProfile.add(user_image);
                    Glide.with(mContext).load(user_image).into(iv_Driver);
                } else {
                    iv_Driver.setImageResource(R.drawable.profile_new);
                }

                tv_Name.setText(name);
                tv_Rating.setText("(" + rating + ")");
                tv_Address.setText(address + "," + city);
                bar_Rating.setRating(Float.parseFloat(rating));
                tv_Description.setText(about);

                contact = "+91" + mobile.trim();

                if (images.contains(",")) {

                    String[] img = images.split(",");

                    imagesStrings = new ArrayList<>();

                    if (img.length == 1) {
                        imagesStrings.add(img[0]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        iv_Two.setVisibility(View.GONE);
                        iv_Three.setVisibility(View.GONE);
                    } else if (img.length == 2) {
                        imagesStrings.add(img[0]);
                        imagesStrings.add(img[1]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        Glide.with(mContext).load(img[1]).into(iv_Two);
                        iv_Three.setVisibility(View.GONE);
                    } else if (img.length == 3) {
                        imagesStrings.add(img[0]);
                        imagesStrings.add(img[1]);
                        imagesStrings.add(img[2]);
                        Glide.with(mContext).load(img[0]).into(iv_One);
                        Glide.with(mContext).load(img[1]).into(iv_Two);
                        Glide.with(mContext).load(img[2]).into(iv_Three);
                    }

                } else if (images.contains("png") || images.contains("jpg")) {
                    imagesStrings = new ArrayList<>();
                    imagesStrings.add(images);
                    Glide.with(mContext).load(images).into(iv_One);
                    iv_Two.setVisibility(View.GONE);
                    iv_Three.setVisibility(View.GONE);

                } else {

                    iv_One.setVisibility(View.GONE);
                    iv_Two.setVisibility(View.GONE);
                    iv_Three.setVisibility(View.GONE);

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    private void setData(String type) {
        if (type.equalsIgnoreCase("detail")) {
            tv_Detail.setBackgroundColor(Color.parseColor("#0D96DC"));
            tv_Histry.setBackgroundColor(Color.parseColor("#E2E5E7"));
            tv_Detail.setTextColor(Color.parseColor("#FFFFFF"));
            tv_Histry.setTextColor(Color.parseColor("#666666"));

            rv_Histry.setVisibility(View.GONE);
            ll_Imgs.setVisibility(View.GONE);
            tv_Description.setVisibility(View.VISIBLE);
            rl_Whatsapp.setVisibility(View.VISIBLE);
            rl_Call.setVisibility(View.VISIBLE);

        } else {
            tv_Histry.setBackgroundColor(Color.parseColor("#0D96DC"));
            tv_Detail.setBackgroundColor(Color.parseColor("#E2E5E7"));
            tv_Histry.setTextColor(Color.parseColor("#FFFFFF"));
            tv_Detail.setTextColor(Color.parseColor("#666666"));

            ll_Imgs.setVisibility(View.VISIBLE);
            rv_Histry.setVisibility(View.GONE);
            tv_Description.setVisibility(View.GONE);
            rl_Whatsapp.setVisibility(View.GONE);
            rl_Call.setVisibility(View.GONE);
        }
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Driver = findViewById(R.id.iv_Driver);
        tv_Detail = findViewById(R.id.tv_Detail);
        tv_Histry = findViewById(R.id.tv_Histry);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Rating = findViewById(R.id.tv_Rating);
        tv_Address = findViewById(R.id.tv_Address);
        tv_Description = findViewById(R.id.tv_Description);
        bar_Rating = findViewById(R.id.bar_Rating);
        rl_Whatsapp = findViewById(R.id.rl_Whatsapp);
        rl_Call = findViewById(R.id.rl_Call);
        ll_Imgs = findViewById(R.id.ll_Imgs);
        rv_Histry = findViewById(R.id.rv_Histry);
        iv_One = findViewById(R.id.iv_One);
        iv_Two = findViewById(R.id.iv_Two);
        iv_Three = findViewById(R.id.iv_Three);

        tv_Detail.setOnClickListener(this);
        tv_Histry.setOnClickListener(this);
        iv_Bck.setOnClickListener(this);
        rl_Whatsapp.setOnClickListener(this);
        rl_Call.setOnClickListener(this);
        iv_Driver.setOnClickListener(this);
        iv_One.setOnClickListener(this);
        iv_Two.setOnClickListener(this);
        iv_Three.setOnClickListener(this);

        adapter = new BloodDonorHistryAdapter(mContext);
        GridLayoutManager manager = new GridLayoutManager(mContext, 3);
        manager.setOrientation(RecyclerView.VERTICAL);
        rv_Histry.setLayoutManager(manager);
        rv_Histry.setAdapter(adapter);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.tv_Detail:
                setData("detail");
                break;

            case R.id.tv_Histry:
                setData("histry");
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

            case R.id.rl_Call:
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:" + contact));
                startActivity(intent);
                break;

            case R.id.iv_Driver:
                if (imagesStringsProfile != null) {
                    openImage(0, imagesStringsProfile);
                }
                break;

            case R.id.iv_One:
                if (imagesStrings != null) {
                    openImage(0, imagesStrings);
                }
                break;

            case R.id.iv_Two:
                if (imagesStrings != null) {
                    openImage(1, imagesStrings);
                }
                break;

            case R.id.iv_Three:
                if (imagesStrings != null) {
                    openImage(2, imagesStrings);
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