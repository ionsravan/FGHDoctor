package com.ambitious.fghdoctor.Activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.lang.UCharacter;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.MarketProductAdapter;
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

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import in.mayanknagwanshi.imagepicker.ImageSelectActivity;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MarketPricesSignupActivity extends AppCompatActivity implements View.OnClickListener, MarketProductAdapter.ItemClickListener {
    private static final String TAG = "MarketPricesSignupActivity";
    private Context mContext = this;
    private ImageView iv_Bck, iv_Profile;
    private TextView tv_Login, txtAddList;
    EditText etFirstName,etLastName,etAccountNumber,etIfscCode,etUpiId,etPaymentMobile;
    private EditText btnAddBankDetails, et_name, et_Mobile, et_Email, et_Address,et_Cityname, et_Districtname, et_Password, et_Repassword, etLatitude,etLongitude,etProductName, etProductPrice, etProductWeight;
    private CheckBox chk_Apositive;
    private Button btn_Login;
    private String path = "", firstName, lastName, accountNumber, ifscCode, upiId, paymentMobile;
    private MultipartBody.Part body;
    private boolean atleastone = false;
    private RelativeLayout rl_Loader;
    private RecyclerView recyclerList;
    private MarketProductAdapter marketProductAdapter;
    private ArrayList<MarketProduct> arrayList = new ArrayList<>();
    private ArrayList<String> emptyList = new ArrayList<>();
    ArrayList<MultipartBody.Part> parts = new ArrayList<>();
    MarketProduct marketProduct1;
    int adapterPosition;
    // counter to track number of clicks
    int numberOfClicks = 3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_prices_signup);
        finds();


    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Profile = findViewById(R.id.iv_Profile);
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
        etLatitude = findViewById(R.id.et_Latitude);
        etLongitude = findViewById(R.id.et_Longitude);

        btn_Login = findViewById(R.id.btn_Login);
        rl_Loader = findViewById(R.id.rl_Loader);
        recyclerList = findViewById(R.id.recyclerList);

        arrayList.add(new MarketProduct(null, "", "", "",""));
        arrayList.add(new MarketProduct(null, "", "", "",""));
        arrayList.add(new MarketProduct(null, "", "", "",""));
        arrayList.add(new MarketProduct(null, "", "", "",""));
        arrayList.add(new MarketProduct(null, "", "", "",""));



        marketProductAdapter = new MarketProductAdapter(this, arrayList, this::onClick);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        recyclerList.setLayoutManager(linearLayoutManager);
        recyclerList.setAdapter(marketProductAdapter);

        iv_Bck.setOnClickListener(this);
        tv_Login.setOnClickListener(this);
        txtAddList.setOnClickListener(this);
        iv_Profile.setOnClickListener(this);
        btn_Login.setOnClickListener(this);
        btnAddBankDetails.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
            case R.id.tv_Login:
                onBackPressed();
                break;

            case R.id.txtAddList:


                MarketProduct marketProduct = new MarketProduct(null, "", "", "","");
                arrayList.add(arrayList.size(), marketProduct);
                marketProductAdapter.notifyItemInserted(arrayList.size());

                break;

            case R.id.btnAddBankDetails:

                Intent intent = new Intent(mContext, BankDetailsActivity.class);
                startActivityForResult(intent, 100);

                break;

            case R.id.iv_Profile:
                Intent intent1 = new Intent(mContext, ImageSelectActivity.class);
                intent1.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
                intent1.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
                intent1.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
                startActivityForResult(intent1, 1314);
                break;

            case R.id.btn_Login:
                if (Utility.isNetworkConnected(mContext)) {
                    Validate(v);
                } else {
                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                            "You don't have internet connection.", false);
                }
                break;
        }
    }

    private void Validate(View v) {

        String name = et_name.getText().toString();
        String number = et_Mobile.getText().toString();
        String email = et_Email.getText().toString();
        String address = et_Address.getText().toString();
        String city = et_Cityname.getText().toString();
        String district = et_Districtname.getText().toString();
        String pass = et_Password.getText().toString();
        String repass = et_Repassword.getText().toString();
        String latitude = etLatitude.getText().toString();
        String longitude = etLongitude.getText().toString();
        String user_type = "market";
        String productPrice = "";
        String productName = "";
        String productWeight = "";
        String register_id = FirebaseInstanceId.getInstance().getToken();

        if (path.equalsIgnoreCase("")) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please Select Profile image.");
        } else if (name.equalsIgnoreCase("")) {
            et_name.setError("Can,t be empty!");
            et_name.requestFocus();
        } else if (number.equalsIgnoreCase("")) {
            et_Mobile.setError("Can,t be empty!");
            et_Mobile.requestFocus();
        } else if (address.equalsIgnoreCase("")) {
            et_Address.setError("Can,t be empty!");
            et_Address.requestFocus();
        }else if (city.equalsIgnoreCase("")) {
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
        }  else if (pass.equalsIgnoreCase("")) {
            et_Password.setError("Can,t be empty!");
            et_Password.requestFocus();
        } else if (repass.equalsIgnoreCase("")) {
            et_Repassword.setError("Can,t be empty!");
            et_Repassword.requestFocus();
        } else if (!pass.equalsIgnoreCase(repass)) {
            et_Repassword.setError("Re Enter!");
            et_Repassword.requestFocus();
            CustomSnakbar.showDarkSnakabar(mContext, v, "Password does not matched!");
        }
        else if (etFirstName.getText().toString().isEmpty()){
            etFirstName.setError("Enter First Name");
            etFirstName.requestFocus();
        }

        else if (etLastName.getText().toString().isEmpty()){
            etLastName.setError("Enter Last Name");
            etLastName.requestFocus();
        }

        else if (etAccountNumber.getText().toString().isEmpty()){
            etAccountNumber.setError("Enter Account Number");
            etAccountNumber.requestFocus();
        }

        else if (etIfscCode.getText().toString().isEmpty()){
            etIfscCode.setError("Enter IFSC Code");
            etIfscCode.requestFocus();
        }

        else if (etPaymentMobile.getText().toString().isEmpty()){
            etPaymentMobile.setError("Enter Payment Mobile");
            etPaymentMobile.requestFocus();
        }/*else if (!atleastone) {
            CustomSnakbar.showDarkSnakabar(mContext, v, "Please select Product ");
        }*/ else {


            if (!path.equalsIgnoreCase("")) {
                File file = new File(path);
                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), file);
                body = MultipartBody.Part.createFormData("user_image", file.getName(), requestFile);
            }


            for (MarketProduct marketProduct : arrayList) {

                if (marketProduct.getChecked()) {
                    atleastone = true;

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
                    Log.d(TAG, "onClick: " + productName + "\n" + productPrice + "\n" + productWeight);

                }
            }

            if (!atleastone) {
                CustomSnakbar.showDarkSnakabar(mContext, v, "Please select Product ");
            } else {
                requestToRegister(name, number, email, address, city,district,pass, productName, productPrice, productWeight,user_type, register_id, body,parts, v,latitude,longitude);

            }

        }

    }

    private void requestToRegister(String name, String number, String email, String address, String city, String district, String pass, String productName, String productPrice, String productWeight, String user_type, String reg_id, MultipartBody.Part body, ArrayList<MultipartBody.Part> parts, final View view, String latitude, String longitude) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().signupMarketPrice(name, number, email, address, city,district,productName, productPrice, productWeight,user_type, pass, reg_id,etFirstName.getText().toString(),etLastName.getText().toString(),
                etAccountNumber.getText().toString(),etIfscCode.getText().toString(),etUpiId.getText().toString(),etPaymentMobile.getText().toString(),latitude,longitude,body,parts);
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
                            CustomSnakbar.showDarkSnakabar(mContext, view, "Registration Successfull.");

                            JSONObject result = object.optJSONObject("result");
                            String user_id = result.optString("user_id");
                            String name = result.optString("name");
                            String user_image = result.optString("user_image");
                            String email = result.optString("email");
                            String address = result.optString("address");
                            String user_type = result.optString("user_type");

                            Utility.setSharedPreference(mContext, "u_id", user_id);
                            Utility.setSharedPreference(mContext, "u_name", name);
                            Utility.setSharedPreference(mContext, "u_img", user_image);
                            Utility.setSharedPreference(mContext, "u_email", email);
                            Utility.setSharedPreference(mContext, "location", address);
                            Utility.setSharedPreference(mContext, "user_type", user_type);
                            Utility.setSharedPreferenceBoolean(mContext, "islogin", true);


                            new Handler().postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    Intent intent = new Intent(mContext, MarketPriceHomeActivity.class);
                                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    Animatoo.animateSlideLeft(mContext);
                                    startActivity(intent);
                                    finish();

                                }
                            }, 1500);

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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1314 && resultCode == Activity.RESULT_OK) {
            path = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
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

        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            String path = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);


            File imgFile = new File(path);
            if (imgFile.exists()) {

                Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                // iv_Profile.setImageBitmap(myBitmap);
                MarketProduct marketProduct = new MarketProduct(myBitmap, marketProduct1.getpName(), marketProduct1.getPrice(), marketProduct1.getWeight(),path);
                arrayList.set(adapterPosition, marketProduct);
                marketProductAdapter.notifyItemChanged(adapterPosition);

                RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), imgFile);
                MultipartBody.Part body = MultipartBody.Part.createFormData("images[]", imgFile.getName(), requestFile);
                parts.add(body);

            } else {
                Toast.makeText(mContext, "File  is not exist.", Toast.LENGTH_SHORT).show();
                Glide.with(mContext).load(path).into(iv_Profile);
            }

        }
    }

    @Override
    public void onClick(int position, MarketProduct marketProduct) {
        marketProduct1 = arrayList.get(position);
        adapterPosition = position;
        Intent intent1 = new Intent(mContext, ImageSelectActivity.class);
        intent1.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent1.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent1.putExtra(ImageSelectActivity.FLAG_GALLERY, true);//default is true
        startActivityForResult(intent1, 200);
    }
}