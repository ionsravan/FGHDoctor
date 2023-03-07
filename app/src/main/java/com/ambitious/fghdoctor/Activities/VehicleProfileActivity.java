package com.ambitious.fghdoctor.Activities;

import static com.ambitious.fghdoctor.Utils.AppConfig.amountOfPercentage;
import static com.ambitious.fghdoctor.Utils.AppConfig.tax;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class VehicleProfileActivity extends AppCompatActivity implements View.OnClickListener, PaymentResultListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Driver, iv_One, iv_Two, iv_Three;
    private TextView tv_Name, tv_Rating, tv_Address, tv_Desc, tv_Pay, txtIfscCode, txtAccountNo;
    private RelativeLayout rl_Whatsapp, rl_Call;
    private CheckBox chk_Wallet;
    private EditText et_Amount, et_Pname, et_Email, et_Pnum;
    private RatingBar bar_Rating;
    private RelativeLayout rl_Loader;
    LinearLayout bankLayout;
    private String contact = "", wallet = "", user_id = "", n_Wallet = "", Amnt = "", donated = "";
    private ArrayList<String> imagesStringsProfile;
    private ArrayList<String> imagesStrings;
    private int percentage =0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle_profile);
        finds();

        if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {

            et_Pname.setText(Utility.getSharedPreferences(mContext, "u_name"));
            et_Email.setText(Utility.getSharedPreferences(mContext, "u_email"));
            et_Pnum.setText("+91" + Utility.getSharedPreferences(mContext, "u_mobile"));

        }

        if (getIntent().getExtras() != null) {

            String obj = getIntent().getStringExtra("obj");
            wallet = getIntent().getStringExtra("wallet");
            donated = getIntent().getStringExtra("donated");

//            if (wallet.equalsIgnoreCase("0")) {
//                chk_Wallet.setVisibility(View.GONE);
//            } else {
//                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
//                    chk_Wallet.setVisibility(View.VISIBLE);
//                    chk_Wallet.setText("Use Wallet Amount (₹" + wallet + ")");
//                } else {
//                    chk_Wallet.setVisibility(View.GONE);
//                }
//            }

            if (wallet.equalsIgnoreCase("0") || wallet.equalsIgnoreCase("") || Integer.parseInt(wallet) <= 0) {
                wallet = "0";
                // chk_Wallet.setVisibility(View.GONE);
            }
            else if (donated.equalsIgnoreCase("0") || Integer.parseInt(wallet) <= 0) {
                wallet = "0";
                chk_Wallet.setVisibility(View.GONE);
            } else if (donated.equalsIgnoreCase("1") && (wallet.equalsIgnoreCase("") || Integer.parseInt(wallet) <= 0)) {
                wallet = "0";
                chk_Wallet.setVisibility(View.GONE);
            } else if (donated.equalsIgnoreCase("1") && (wallet.equalsIgnoreCase("0")|| Integer.parseInt(wallet) <= 0)) {
                wallet = "0";
                chk_Wallet.setVisibility(View.GONE);
            } else {
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    chk_Wallet.setVisibility(View.VISIBLE);
                    chk_Wallet.setText("Use Wallet Amount(₹" + wallet + ")");
                } else {
                    chk_Wallet.setVisibility(View.GONE);
                }

            }

            try {

                JSONObject object = new JSONObject(obj);

                user_id = object.optString("user_id");
                String user_type = object.optString("user_type");
                String name = object.optString("name");
                String mobile = object.optString("mobile");
                String email = object.optString("email");
                String username = object.optString("username");
                String password = object.optString("password");
                String address = object.optString("address");
                String department = object.optString("department");
                String village = object.optString("village");
                String city = object.optString("city");
                String distric = object.optString("distric");
                String aadhar = object.optString("aadhar");
                String vehicle_no = object.optString("vehicle_no");
                String user_image = object.optString("user_image");
                String images = object.optString("images");
                String available = object.optString("available");
                String ifsc_code = object.optString("ifsc_code");
                String account_no = object.optString("account_no");
                String rating = object.optString("rating");

                if (!ifsc_code.equalsIgnoreCase("") || !account_no.equalsIgnoreCase("")) {
                    txtIfscCode.setText(ifsc_code);
                    if (!account_no.equalsIgnoreCase("")) {
                        txtAccountNo.setText("XXXXX" + account_no.charAt(5) + account_no.charAt(6) + account_no.charAt(7) + account_no.charAt(8) + account_no.charAt(9) + account_no.charAt(10));
                    }
                    bankLayout.setVisibility(View.VISIBLE);
                }else {
                    bankLayout.setVisibility(View.GONE);
                }

                if (user_image.contains("png") || user_image.contains("jpg")) {
                    imagesStringsProfile = new ArrayList<>();
                    imagesStringsProfile.add(user_image);
                    Glide.with(mContext).load(user_image).into(iv_Driver);
                } else {
                    iv_Driver.setImageResource(R.drawable.profile_new);
                }

                tv_Name.setText(name);
                tv_Rating.setText("(" + rating + ")");
                tv_Address.setText(address);
                tv_Desc.setText(department);
                bar_Rating.setRating(Float.parseFloat(rating));

                contact = "+91 " + mobile;

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

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Driver = findViewById(R.id.iv_Driver);
        iv_One = findViewById(R.id.iv_One);
        iv_Two = findViewById(R.id.iv_Two);
        iv_Three = findViewById(R.id.iv_Three);
        tv_Name = findViewById(R.id.tv_Name);
        tv_Rating = findViewById(R.id.tv_Rating);
        tv_Address = findViewById(R.id.tv_Address);
        tv_Desc = findViewById(R.id.tv_Desc);
        tv_Pay = findViewById(R.id.tv_Pay);
        rl_Whatsapp = findViewById(R.id.rl_Whatsapp);
        rl_Call = findViewById(R.id.rl_Call);
        chk_Wallet = findViewById(R.id.chk_Wallet);
        et_Amount = findViewById(R.id.et_Amount);
        et_Pname = findViewById(R.id.et_Pname);
        et_Email = findViewById(R.id.et_Email);
        et_Pnum = findViewById(R.id.et_Pnum);
        bar_Rating = findViewById(R.id.bar_Rating);
        rl_Loader = findViewById(R.id.rl_Loader);
        txtAccountNo = findViewById(R.id.txtAccountNo);
        txtIfscCode = findViewById(R.id.txtIfscCode);
        bankLayout = findViewById(R.id.bankLayout);

        iv_Bck.setOnClickListener(this);
        rl_Whatsapp.setOnClickListener(this);
        rl_Call.setOnClickListener(this);
        iv_Driver.setOnClickListener(this);
        iv_One.setOnClickListener(this);
        iv_Two.setOnClickListener(this);
        iv_Three.setOnClickListener(this);
        tv_Pay.setOnClickListener(this);
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

            case R.id.tv_Pay:
                String fee = et_Amount.getText().toString();
                String name = et_Pname.getText().toString();
                String email = et_Email.getText().toString();
                String number = et_Pnum.getText().toString();
                if (name.equalsIgnoreCase("")) {
                    et_Pname.setError("Can't be empty!");
                    et_Pname.requestFocus();
                } else if (email.equalsIgnoreCase("")) {
                    et_Email.setError("Can't be empty!");
                    et_Email.requestFocus();
                } else if (number.equalsIgnoreCase("")) {
                    et_Pnum.setError("Can't be empty!");
                    et_Pnum.requestFocus();
                } else if (fee.equalsIgnoreCase("")) {
                    et_Amount.setError("Can't be empty!");
                    et_Amount.requestFocus();
                } else {
                    Amnt = "";
                    n_Wallet = "";
                    if (chk_Wallet.isChecked()) {
                        if (Integer.parseInt(fee) <= Integer.parseInt(wallet)) {
                           // Amnt = "" + (Integer.parseInt(fee));
                             percentage = ((amountOfPercentage * Integer.parseInt(fee)) / 100);
                            int finalAmount = Integer.parseInt(fee) +percentage+tax;
                            Amnt = String.valueOf(finalAmount);

                            Log.d("TAG", "onClick: "+Amnt);

                            n_Wallet = String.valueOf(Integer.parseInt(wallet) - Integer.parseInt(Amnt));
                            String uid = Utility.getSharedPreferences(mContext, "u_id");

                            confirmationDialog(uid, user_id, Amnt, n_Wallet, "wallet", v);

                           // makePayment(uid, user_id, Amnt, n_Wallet, "wallet", v);
                        } else {
                            Amnt = String.valueOf((Integer.parseInt(fee) - Integer.parseInt(wallet)));
                            n_Wallet = "0";
//                            startPayment(name, Amnt, email, number);
                           // getOrderId(v, name, Amnt, email, number);
                            CustomSnakbar.showDarkSnakabar(mContext, v, "Your wallet haven't sufficient amount");

                        }
                    } else {

                        if (Integer.parseInt(wallet) <= 0) {
                            n_Wallet = "0";
                            // getOrderId(v, name, Amnt, email, number);
                            CustomSnakbar.showDarkSnakabar(mContext, v, "Your wallet haven't sufficient amount");

                        } else {

                            Amnt = "" + (Integer.parseInt(fee));
                            n_Wallet = "";
                            // getOrderId(v, name, Amnt, email, number);
                            CustomSnakbar.showDarkSnakabar(mContext, v, "Please use Wallet Amount");

//                        startPayment(name, Amnt, email, number);
                        }
                    }
//                    Toast.makeText(mContext, "Amnt=>" + Amnt + "\nWallet=>" + n_Wallet, Toast.LENGTH_SHORT).show();
                }
                break;
        }

    }

    private void confirmationDialog(String uid, String user_id, String amnt, String n_Wallet, String wallet, View v){
        AlertDialog.Builder builder = new AlertDialog.Builder(VehicleProfileActivity.this);
        ViewGroup viewGroup = findViewById(android.R.id.content);
        View dialogView = LayoutInflater.from(v.getContext()).inflate(R.layout.customview, viewGroup, false);
        builder.setView(dialogView);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        TextView txtTitle = dialogView.findViewById(R.id.txtTitle);
        TextView txtPrice = dialogView.findViewById(R.id.txtPrice);
        Button btnContinue = dialogView.findViewById(R.id.btnContinue);
        Button btnCancel = dialogView.findViewById(R.id.btnCancel);

       /* // add 1Rupee to total amount
        Amnt = String.valueOf(Integer.parseInt(Amnt) + 1);*/
        txtTitle.setText(tv_Name.getText().toString());
        txtPrice.setText("\u20b9 "+Amnt);

        btnContinue.setOnClickListener(v1 -> {
            alertDialog.dismiss();
            // pay to vendor
            int vendorAmount = (Integer.parseInt(Amnt) - percentage) - tax;

            Log.d("TAG", "confirmationDialog: "+vendorAmount);
            makePayment(uid, user_id, String.valueOf(vendorAmount), n_Wallet, "wallet", v);

        });

        btnCancel.setOnClickListener(v1 -> {
            alertDialog.dismiss();
        });
    }


    private void getOrderId(View view, String name, String amnt, String email, String number) {
        rl_Loader.setVisibility(View.VISIBLE);
        int amount = Integer.parseInt(amnt) * 100;
        Call<ResponseBody> call = AppConfig.loadInterface().getOrderID(amount);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("Genrate Order ID =>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            startPayment(resultmessage, name, amnt, email, number);
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Order ID " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    public void startPayment(String orderid, String name, String amount, String email, String mobile) {
        try {

            final Activity activity = this;
            final Checkout co = new Checkout();

            try {
                JSONObject options = new JSONObject();
                options.put("name", name);
                options.put("description", "" + tv_Name.getText().toString());
                //You can omit the image option to fetch the image from dashboard
                options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                options.put("order_id", orderid);
                options.put("currency", "INR");
                options.put("amount", (Integer.parseInt(amount) * 100));
//                options.put("amount", 100);

                JSONObject preFill = new JSONObject();
                preFill.put("email", email);
                preFill.put("contact", mobile);

                options.put("prefill", preFill);

                co.open(activity, options);
            } catch (Exception e) {
                Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
                e.printStackTrace();
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String s) {
        Log.e("onPaymentSuccess", "---->" + s);
        String uid = Utility.getSharedPreferences(mContext, "u_id");
        makePayment(uid, user_id, Amnt, n_Wallet, s, iv_Bck);
    }

    @Override
    public void onPaymentError(int i, String s) {
        Log.e("onPaymentError", i + "----->" + s);
        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Payment Failed\nMedical Shop Payment Failed.");
    }

    private void makePayment(String uid, String vendor_id, String amnt, String n_wallet, String txn_id, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().makePayment(uid, vendor_id, amnt, txn_id);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("MakePayment=>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Paid Successfully.");
                            if (!n_wallet.equalsIgnoreCase("")) {
                                updateWallet(uid, n_wallet, view);
                            } else {
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {

                                        Intent intent = new Intent(mContext, HomeActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Animatoo.animateSlideLeft(mContext);
                                        startActivity(intent);
                                        finish();

                                    }
                                }, 1500);
                            }
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Profile " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateWallet(String uid, String n_wallet, View view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().updateWallet(uid, n_wallet);
        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                rl_Loader.setVisibility(View.GONE);
                try {
                    if (response.isSuccessful()) {
                        String responseData = response.body().string();
                        JSONObject object = new JSONObject(responseData);
                        String status = object.getString("status");
                        String message = object.getString("message");
                        String resultmessage = object.getString("result");
                        System.out.println("UpdateWallet=>" + object);

                        if (status.equalsIgnoreCase("1")) {
                            Intent intent = new Intent(mContext, PaidSuccessActivity.class);
                            intent.putExtra("title",tv_Name.getText().toString());
                            intent.putExtra("amount",Amnt);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            Animatoo.animateSlideLeft(mContext);
                            startActivity(intent);
                            finish();
                        } else {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "Profile " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                rl_Loader.setVisibility(View.GONE);
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

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