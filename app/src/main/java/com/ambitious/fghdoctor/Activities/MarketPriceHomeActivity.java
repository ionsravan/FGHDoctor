package com.ambitious.fghdoctor.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.MarketProductAdapter;
import com.ambitious.fghdoctor.Model.MarketPriceList;
import com.ambitious.fghdoctor.Model.MarketProduct;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;
import com.google.firebase.iid.FirebaseInstanceId;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MarketPriceHomeActivity extends AppCompatActivity implements View.OnClickListener, MarketProductAdapter.ItemClickListener {
    private static final String TAG = "MarketPriceHomeActivity";
    private Context mContext = this;
    private ImageView iv_Profile,imgQrCode;
    private TextView tv_Login, tv_Available, tv_Head, txtAddList;
    EditText etFirstName, etLastName, etAccountNumber, etIfscCode, etUpiId, etPaymentMobile;
    private EditText btnAddBankDetails, et_name, et_Mobile, et_Email, et_Address, et_Cityname, et_Districtname, et_Password, et_Repassword, etLatitude,etLongitude,et_Aposprice, et_Anegprice, et_Bposprice, et_Bnegprice, et_ABposprice, et_ABnegprice, et_Oposprice, et_Onegprice;
    private CheckBox chk_Apositive, chk_Anegitive, chk_Bpositive, chk_Bnegitive, chk_ABpositive, chk_ABnegitive, chk_Opositive, chk_Onegitive;
    private Button btn_Login;
    private String path = "", firstName, lastName, accountNumber, ifscCode, upiId, paymentMobile;
    private MultipartBody.Part body;
    private boolean atleastone = false;
    private RelativeLayout rl_Loader;
    private LinearLayout ll_Logout;
    private Switch switch_Available;
    private String bgroup = "", prc = "", city = "", district = "";
    private boolean is_Getting = false;
    private RecyclerView recyclerList;
    private MarketProductAdapter marketProductAdapter;
    private ArrayList<MarketProduct> arrayList = new ArrayList<>();
    private ArrayList<String> productImagesArray = new ArrayList<>();
    private ArrayList<String> productNameArray = new ArrayList<>();
    private ArrayList<String> productPriceArray = new ArrayList<>();
    private ArrayList<String> productWeightArray = new ArrayList<>();
    ArrayList<MultipartBody.Part> parts = new ArrayList<>();

    MarketProduct marketProduct1;
    int adapterPosition;
    private String uid, name, number, address, pass, email;
    String user_type = "market";
    String productImage = "";
    String productPrice = "";
    String productName = "";
    String productWeight = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_price_home);
        finds();

        if (Utility.isNetworkConnected(mContext)) {
            uid = Utility.getSharedPreferences(mContext, "u_id");
            getProfile(uid, iv_Profile);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
        }

        switch_Available.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                uid = Utility.getSharedPreferences(mContext, "u_id");
                if (isChecked) {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "1", iv_Profile);
                } else {
                    tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                    switch_Available.setChecked(isChecked);
                    requesToChangeAvailablity(uid, "0", iv_Profile);
                }
            }
        });


    }

    private void requesToChangeAvailablity(String uid, String stts, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().cahngeAvailablity(uid, stts);
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");
                            String available = result.optString("available");

                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
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

    private void finds() {

        tv_Available = findViewById(R.id.tv_Available);
        tv_Head = findViewById(R.id.tv_Head);
        iv_Profile = findViewById(R.id.iv_Profile);
        imgQrCode = findViewById(R.id.imgQrCode);
        tv_Login = findViewById(R.id.tv_Login);
        txtAddList = findViewById(R.id.txtAddList);
        btnAddBankDetails = findViewById(R.id.btnAddBankDetails);
        et_name = findViewById(R.id.et_name);
        et_Mobile = findViewById(R.id.et_Mobile);
        et_Email = findViewById(R.id.et_Email);
        et_Address = findViewById(R.id.et_Address);
        et_Cityname = findViewById(R.id.et_Cityname);
        et_Districtname = findViewById(R.id.et_Districtname);
        et_Password = findViewById(R.id.et_Password);
        et_Repassword = findViewById(R.id.et_Repassword);
        etFirstName = findViewById(R.id.etFirstName);
        etLastName = findViewById(R.id.etLastName);
        etAccountNumber = findViewById(R.id.etAccountNumber);
        etIfscCode = findViewById(R.id.etIfscCode);
        etUpiId = findViewById(R.id.etUpiId);
        etPaymentMobile = findViewById(R.id.etPaymentMobile);
        btn_Login = findViewById(R.id.btn_Login);
        ll_Logout = findViewById(R.id.ll_Logout);
        switch_Available = findViewById(R.id.switch_Available);
        rl_Loader = findViewById(R.id.rl_Loader);
        recyclerList = findViewById(R.id.recyclerList);
        etLatitude = findViewById(R.id.et_Latitude);
        etLongitude = findViewById(R.id.et_Longitude);

        iv_Profile.setOnClickListener(this);
        btn_Login.setOnClickListener(this);
        ll_Logout.setOnClickListener(this);
        btnAddBankDetails.setOnClickListener(this);
        txtAddList.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Profile:
                Intent intent4 = new Intent(mContext, ImageSelectActivity.class);
                intent4.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent4.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent4.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent4, 1314);
                break;

            case R.id.txtAddList:

                MarketProduct marketProduct = new MarketProduct(null, "", "", "", "");
                arrayList.add(arrayList.size(), marketProduct);
                marketProductAdapter.notifyItemInserted(arrayList.size());
                marketProductAdapter.notifyDataSetChanged();

                break;

            case R.id.btnAddBankDetails:

                Intent intent = new Intent(mContext, BankDetailsActivity.class);
                startActivityForResult(intent, 100);

                break;

            case R.id.btn_Login:
                if (Utility.isNetworkConnected(mContext)) {
                    Validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;

            case R.id.ll_Logout:
                new AlertDialog.Builder(mContext)
                        .setTitle(getResources().getString(R.string.logout))
                        .setMessage(getResources().getString(R.string.logoutmsg))
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                rl_Loader.setVisibility(View.VISIBLE);
                                Utility.setSharedPreference(mContext, "u_id", "");
                                Utility.setSharedPreference(mContext, "u_name", "");
                                Utility.setSharedPreference(mContext, "u_img", "");
                                Utility.setSharedPreference(mContext, "u_email", "");
                                Utility.setSharedPreference(mContext, "location", "");
                                Utility.setSharedPreference(mContext, "user_type", "");
                                Utility.setSharedPreferenceBoolean(mContext, "islogin", false);
                                new Handler().postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        rl_Loader.setVisibility(View.GONE);
                                        Intent intent = new Intent(mContext, MarketPricesLoginActivity.class);
                                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        Animatoo.animateSlideLeft(mContext);
                                        startActivity(intent);
                                        finish();
                                    }
                                }, 3000);

                            }
                        })

                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        })
                        .show();
                break;

        }

    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1314 && resultCode == Activity.RESULT_OK) {
            path = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
//                Toast.makeText(getContext(), "Image Path =>"+path1, Toast.LENGTH_SHORT).show();
            File imgFile = new File(path);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                iv_Profile.setImageBitmap(myBitmap);

            } else {
                Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(iv_Profile);
            }

        }

        if (requestCode == 100 && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                firstName = data.getStringExtra("firstName");
                lastName = data.getStringExtra("lastName");
                accountNumber = data.getStringExtra("accountNumber");
                ifscCode = data.getStringExtra("ifscCode");
                upiId = data.getStringExtra("upiId");
                paymentMobile = data.getStringExtra("paymentMobile");

            }

        }

        if (requestCode == adapterPosition && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);

            Log.d(TAG, "onActivityResult: " + path);
            File imgFile = new File(path);
            if (imgFile.exists()) {

                Log.d(TAG, "onActivityResult: " + adapterPosition);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                // iv_Profile.setImageBitmap(myBitmap);
                MarketProduct marketProduct = new MarketProduct(myBitmap, marketProduct1.getpName(), marketProduct1.getPrice(), marketProduct1.getWeight(), path);
                arrayList.set(adapterPosition, marketProduct);
                marketProductAdapter.notifyItemChanged(adapterPosition);

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("image", imgFile.getName(), requestFile);
                parts.add(body);

                UpdateImage(uid, String.valueOf(adapterPosition), body, iv_Profile);

            } else {
                Toast.makeText(mContext, "File is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(iv_Profile);
            }

        }
    }

    private void getProfile(String uid, final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getProfile(uid);
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String blood_group = result.optString("blood_group");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            String available = result.optString("available");
                            String product_name = result.optString("product_name");
                            String product_price = result.optString("product_price");
                            String product_weight = result.optString("weight");
                            String account_first_name = result.optString("account_first_name");
                            String account_last_name = result.optString("account_last_name");
                            String account_no = result.optString("account_no");
                            String ifsc_code = result.optString("ifsc_code");
                            String upi_id = result.optString("upi_id");
                            String payment_mobile = result.optString("payment_mobile");
                            String latitude = result.optString("lat");
                            String longitude = result.optString("lng");
                            String qrCode = result.optString("qrcode");

                            etLatitude.setText(latitude);
                            etLongitude.setText(longitude);
                            Glide.with(mContext).load(qrCode).into(imgQrCode);
                            Log.d(TAG, "onResponse: "+name);

                            imgQrCode.setOnClickListener(v -> {
                                Intent intent = new Intent(getApplicationContext(),QrCodeActivity.class);
                                intent.putExtra("qrCode",qrCode);
                                intent.putExtra("title",name);
                                Log.d("TAG", "onResponse: "+name);
                                //   intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            });


                            is_Getting = true;

                            String[] productImage = images.split(",");
                            String[] productName = product_name.split(",");
                            String[] productPrice = product_price.split(",");
                            String[] productWeight = product_weight.split(",");

                            ArrayList<String> pNames = new ArrayList<>(Arrays.asList(productName));
                            ArrayList<String> pImages = new ArrayList<>(Arrays.asList(productImage));


                            // for (int j = 0; j < productImage.length; j++) {

                            for (int k = pNames.size() - 1; k >= 0; k--) {
                                MarketProduct group = new MarketProduct();
                                group.setImg(null);
                                group.setpName(productName[k]);
                                group.setPrice(productPrice[k]);
                                group.setWeight(productWeight[k]);
                                group.setImgs(productImage[k]);
                                arrayList.add(group);
                            }

                           /* Log.d(TAG, "onResponse: "+ Arrays.toString(productName));
                            //  arrayList = new ArrayList<>();
                            for (int i = 0; i < productImage.length; i++) {

                                MarketProduct group = new MarketProduct();
                                group.setImg(null);
                                group.setpName(productName[i]);
                                group.setPrice(productPrice[i]);
                                group.setWeight(productWeight[i]);
                                group.setImgs(productImage[i]);
                                arrayList.add(group);

                            }*/

                            marketProductAdapter = new MarketProductAdapter(MarketPriceHomeActivity.this, arrayList, MarketPriceHomeActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MarketPriceHomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            recyclerList.setLayoutManager(linearLayoutManager);
                            recyclerList.setAdapter(marketProductAdapter);


                            Glide.with(mContext).load(user_image).into(iv_Profile);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);
                            et_Cityname.setText(city);
                            et_Districtname.setText(distric);
                            etLatitude.setText(latitude);
                            etLongitude.setText(longitude);

                            etFirstName.setText(account_first_name);
                            etLastName.setText(account_last_name);
                            etAccountNumber.setText(account_no);
                            etIfscCode.setText(ifsc_code);
                            etPaymentMobile.setText(payment_mobile);
                            etUpiId.setText(upi_id);

                            tv_Head.setText(name);

                            is_Getting = false;

                            if (available.equalsIgnoreCase("1")) {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                switch_Available.setChecked(true);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                tv_Available.setText("Available");
                            } else {
                                CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                switch_Available.setChecked(false);
                                tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                tv_Available.setText("Not Available");
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

    private void Validate(View v) {

        uid = Utility.getSharedPreferences(mContext, "u_id");
        name = et_name.getText().toString();
        number = et_Mobile.getText().toString();
        email = et_Email.getText().toString();
        address = et_Address.getText().toString();
        city = et_Cityname.getText().toString();
        district = et_Districtname.getText().toString();
        pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();
        String register_id = FirebaseInstanceId.getInstance().getToken();

        /*if (path.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Profile image.");
        } else*/
        if (name.equalsIgnoreCase("")) {
            et_name.setError("Can,t be empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can,t be empty!");
            et_Mobile.requestFocus();
        } else if (address.equalsIgnoreCase("")) {
            et_Address.setError("Can,t be empty!");
            et_Address.requestFocus();
        } else if (city.equalsIgnoreCase("")) {
            et_Cityname.setError("Can,t be empty!");
            et_Cityname.requestFocus();
        } else if (district.equalsIgnoreCase("")) {
            et_Districtname.setError("Can,t be empty!");
            et_Districtname.requestFocus();
        } else if (latitude.equalsIgnoreCase("")) {
            etLatitude.setError("Can,t be empty!");
            etLatitude.requestFocus();
        } else if (longitude.equalsIgnoreCase("")) {
            etLongitude.setError("Can,t be empty!");
            etLongitude.requestFocus();
        } else if (pass.equalsIgnoreCase("")) {
            et_Password.setError("Can,t be empty!");
            et_Password.requestFocus();
        } else if (repass.equalsIgnoreCase("")) {
            et_Repassword.setError("Can,t be empty!");
            et_Repassword.requestFocus();
        } else if (!pass.equalsIgnoreCase(repass)) {
            et_Repassword.setError("Re Enter!");
            et_Repassword.requestFocus();
            CustomSnakbar.showDarkSnakabar(mContext, v, "Password does not matched!");
        } else if (etFirstName.getText().toString().isEmpty()) {
            etFirstName.setError("Enter First Name");
            etFirstName.requestFocus();
        } else if (etLastName.getText().toString().isEmpty()) {
            etLastName.setError("Enter Last Name");
            etLastName.requestFocus();
        } else if (etAccountNumber.getText().toString().isEmpty()) {
            etAccountNumber.setError("Enter Account Number");
            etAccountNumber.requestFocus();
        } else if (etIfscCode.getText().toString().isEmpty()) {
            etIfscCode.setError("Enter IFSC Code");
            etIfscCode.requestFocus();
        } else if (etPaymentMobile.getText().toString().isEmpty()) {
            etPaymentMobile.setError("Enter Payment Mobile");
            etPaymentMobile.requestFocus();
        } /*else if (!atleastone) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please select Product ");
        }*/ else {


            if (!path.equalsIgnoreCase("")) {
                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);
            }


            for (MarketProduct marketProduct : arrayList) {

                if (marketProduct.getChecked()) {
                    marketProduct.setChecked(true);
                    atleastone = marketProduct.getChecked();

                    // product image
                    if (productImage.equalsIgnoreCase("")) {
                        productImage = marketProduct.getImgs();
                    } else {
                        productImage = productImage + "," + marketProduct.getImgs();
                    }

                    // product name
                    if (productName.equalsIgnoreCase("")) {
                        productName = marketProduct.getpName();
                    } else {
                        productName = productName + "," + marketProduct.getpName();
                    }

                    // product price
                    if (productPrice.equalsIgnoreCase("")) {
                        productPrice = marketProduct.getPrice();
                    } else {
                        productPrice = productPrice + "," + marketProduct.getPrice();
                    }


                    // product weight
                    if (productWeight.equalsIgnoreCase("")) {
                        productWeight = marketProduct.getWeight();
                    } else {
                        productWeight = productWeight + "," + marketProduct.getWeight();
                    }

                    Log.d(TAG, "onClick: " + productName + "\n" + productPrice + "\n" + productWeight + "\n" + productImage);


                } else {
//                    atleastone = false;

                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                    removeImage(uid, marketProduct.getPos(), v);

                }
            }

            if (!atleastone) {
                CustomSnakbar.showDarkSnakabar(mContext, v, "Please select Product ");
            } else {
                requestToUpdate(uid, name, number, email, address,city,district, pass, productName, productPrice, productWeight, user_type, body, parts, v,latitude,longitude);
            }

        }


    }

    private void UpdateImage(String uid, String pos, MultipartBody.Part body,
                             final ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = AppConfig.loadInterface().updateImage(uid, pos, body);
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            CustomSnakbar.showDarkSnakabar(mContext, view, "Image Update Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String department = result.optString("department");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            final String available = result.optString("available");

                            Log.d(TAG, "onResponse: " + Arrays.toString(images.split(",")));
                           /* String[] productImage = images.split(",");

                            //  arrayList = new ArrayList<>();
                            for (int i = 0; i < productImage.length; i++) {

                                MarketProduct group = new MarketProduct(null, productName[i], productPrice[i], productWeight[i], "http://fghdoctors.com/admin/uploads/user/49841654317000IMG_1654316981272.png");
                                arrayList.add(group);

                            }*/


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


    private void requestToUpdate(String uid, String name, String number, String email, String address, String city, String district, String pass, String productName, String productPrice, String productWeight, String user_type, MultipartBody.Part body, ArrayList<MultipartBody.Part> parts, final View view, String latitude, String longitude) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call;
        if (path.equalsIgnoreCase("")) {
            call = AppConfig.loadInterface().updateMarketPrice(uid, name, number, email, address, city, district, productName, productPrice, productWeight, user_type, pass, etFirstName.getText().toString(), etLastName.getText().toString(),
                    etAccountNumber.getText().toString(), etIfscCode.getText().toString(), etUpiId.getText().toString(), etPaymentMobile.getText().toString(),latitude,longitude);
        } else {
            call = AppConfig.loadInterface().updateMarketPriceWithImage(uid, name, number, email, address, city, district, productName, productPrice, productWeight, user_type, pass, etFirstName.getText().toString(), etLastName.getText().toString(),
                    etAccountNumber.getText().toString(), etIfscCode.getText().toString(), etUpiId.getText().toString(), etPaymentMobile.getText().toString(),latitude,longitude, body, parts);
        }
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Profile Update Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String blood_group = result.optString("blood_group");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            final String available = result.optString("available");
                            String product_name = result.optString("product_name");
                            String product_price = result.optString("product_price");
                            String product_weight = result.optString("weight");

                            Utility.setSharedPreference(mContext, "u_id", user_id);
                            Utility.setSharedPreference(mContext, "u_name", name);
                            Utility.setSharedPreference(mContext, "u_img", user_image);
                            Utility.setSharedPreference(mContext, "u_email", email);
                            Utility.setSharedPreference(mContext, "location", address);
                            Utility.setSharedPreference(mContext, "user_type", user_type);
                            Utility.setSharedPreferenceBoolean(mContext, "islogin", true);

                            is_Getting = true;

                          /*  String[] productImage = images.split(",");
                            String[] productName = product_name.split(",");
                            String[] productPrice = product_price.split(",");
                            String[] productWeight = product_weight.split(",");

                         //   arrayList = new ArrayList<>();
                            for (int i = 0; i < productName.length; i++) {

                                MarketProduct group = new MarketProduct(null,productName[i], productPrice[i],productWeight[i],productImage[i]);
                                arrayList.add(group);

                            }

                            Log.d(TAG, "onResponse: " + arrayList.size());

                            marketProductAdapter = new MarketProductAdapter(MarketPriceHomeActivity.this, arrayList, MarketPriceHomeActivity.this);
                            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MarketPriceHomeActivity.this, LinearLayoutManager.VERTICAL, false);
                            recyclerList.setLayoutManager(linearLayoutManager);
                            recyclerList.setAdapter(marketProductAdapter);*/

                            Glide.with(mContext).load(user_image).into(iv_Profile);
                            et_name.setText(name);
                            et_Email.setText(email);
                            et_Address.setText(address);
                            et_Password.setText(password);
                            et_Repassword.setText(password);
                            et_Mobile.setText(mobile);

                            tv_Head.setText(name);

                            is_Getting = false;

                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {
                                    if (available.equalsIgnoreCase("1")) {
                                        CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Available.");
                                        switch_Available.setChecked(true);
                                        tv_Available.setTextColor(getResources().getColor(R.color.colorWhite));
                                        tv_Available.setText("Available");
                                    } else {
                                        CustomSnakbar.showDarkSnakabar(mContext, view, "You are now Not Available.");
                                        switch_Available.setChecked(false);
                                        tv_Available.setTextColor(getResources().getColor(R.color.colorGrayLight));
                                        tv_Available.setText("Not Available");
                                    }
                                }
                            }, 1500);


                            Intent intent = new Intent(MarketPriceHomeActivity.this, MarketPriceHomeActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

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
                Log.d(TAG, "onFailure: " + t.getMessage());
                Toast.makeText(mContext, "Failed server or network connection, please try again", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onClick(int position, MarketProduct marketProduct) {
        marketProduct1 = arrayList.get(position);
        adapterPosition = position;
        Log.d(TAG, "onClick: " + adapterPosition);
        /*Intent intent1 = new Intent(mContext, ImageSelectActivity.class);
        intent1.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent1.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent1.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent1, 200);*/

        openSelectionDialog(String.valueOf(adapterPosition), adapterPosition, marketProduct.getImgs());
    }

    private void openSelectionDialog(final String pos, final int i, String str) {
        Log.d(TAG, "openSelectionDialog: " + i);
        final Dialog dialog = new Dialog(mContext);
        dialog.setContentView(R.layout.dialog_imageoperation);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        TextView tv_Changeimg = dialog.findViewById(R.id.tv_Changeimg);
        TextView tv_Removeimg = dialog.findViewById(R.id.tv_Removeimg);
        View view = dialog.findViewById(R.id.view);

        if (str.equalsIgnoreCase("")) {
            tv_Changeimg.setText("Choose Image");
            tv_Removeimg.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        } else if (str.contains("png") || str.contains("jpg")) {

        } else {
            tv_Removeimg.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
        }

        dialog.show();

        tv_Changeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent intent = new Intent(mContext, ImageSelectActivity.class);
                intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent, i);

                Log.d(TAG, "onClick: " + i);

            }
        });

        tv_Removeimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                String uid = Utility.getSharedPreferences(mContext, "u_id");
                removeImage(uid, pos, v);
            }
        });

    }

    private void removeImage(String uid, String pos, final View view) {

        rl_Loader.setVisibility(View.VISIBLE);

        Call<ResponseBody> call = AppConfig.loadInterface().removeImage(uid, pos);
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
                        System.out.println("Login" + object);

                        if (status.equalsIgnoreCase("1")) {

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String username = result.optString("username");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String department = result.optString("department");
                            String password = result.optString("password");
                            String mobile = result.optString("mobile");
                            String user_type = result.optString("user_type");
                            String village = result.optString("village");
                            String city = result.optString("city");
                            String distric = result.optString("distric");
                            String aadhar = result.optString("aadhar");
                            String vehicle_no = result.optString("vehicle_no");
                            String images = result.optString("images");
                            final String available = result.optString("available");

                            CustomSnakbar.showDarkSnakabar(mContext, view, "Image Remove Successfull.");


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
}