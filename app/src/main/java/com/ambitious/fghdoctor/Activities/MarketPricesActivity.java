package com.ambitious.fghdoctor.Activities;

import static androidx.core.content.PermissionChecker.checkPermission;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.MarketPricesListAdapter;
import com.ambitious.fghdoctor.Adapters.MarketProductAdapter;
import com.ambitious.fghdoctor.Model.MarketPriceList;
import com.ambitious.fghdoctor.Model.MarketProduct;
import com.ambitious.fghdoctor.Model.MedicalShop;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class MarketPricesActivity extends AppCompatActivity implements View.OnClickListener, MarketPricesListAdapter.ItemClickListener {
    private static final int EXTERNAL_PERMISSION_CODE = 1234;

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Head, tv_Notavailable;
    private RecyclerView rv_Medicalshp;
    private MarketPricesListAdapter adapter;
    private LinearLayout ll_Login;
    private EditText et_Search;
    public String type = "", wallet = "", donated = "";
    private RelativeLayout rl_Loader;
    private ArrayList<MarketPriceList> marketPriceLists = new ArrayList<>();
    private String img="",title="",msgg="",mId="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_prices);
        finds();

        tv_Head.setText(getIntent().getStringExtra("head"));
        wallet = getIntent().getStringExtra("wallet");
        donated = getIntent().getStringExtra("donated");

        type = "market";

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            String latitude = Utility.getSharedPreferences(mContext, "latitude");
            String longitude = Utility.getSharedPreferences(mContext, "longitude");

            getUsers(uid, type, iv_Bck,latitude,longitude);
        } else {
            AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                    "You don't have internet connection.", false);
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

                if (marketPriceLists.size() > 0) {
                    filter(s.toString());
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Medical shop is not available.");
                }

            }
        });

    }

    private void filter(String text) {

        ArrayList<MarketPriceList> temp = new ArrayList();
        for (MarketPriceList d : marketPriceLists) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getProductName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getShopName().toLowerCase().contains(text)) {
                temp.add(d);
            }else if (d.getAddress().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Medicalshp.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adapter.updateList(temp);
        } else {
            rv_Medicalshp.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void getUsers(String uid, final String type, final ImageView view, String latitude, String longitude) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getUsers(type,latitude,longitude);
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

                            et_Search.setVisibility(View.VISIBLE);
                            rv_Medicalshp.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            marketPriceLists = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String user_image = result.optString("user_image");
                                String email = result.optString("email");
                                String address = result.optString("address");
                                String password = result.optString("password");
                                String mobile = result.optString("mobile");
                                String rating = result.optString("rating");
                                String user_type = result.optString("user_type");
                                String available = result.optString("available");
                                String active_status = result.optString("status");
                                String images = result.optString("images");
                                String product_price = result.optString("product_price");
                                String product_name = result.optString("product_name");
                                String product_weight = result.optString("weight");

                                String[] productImage = images.split(",");
                                String[] productName = product_name.split(",");
                                String[] productPrice = product_price.split(",");
                                String[] productWeight = product_weight.split(",");

                                Log.d("TAG", "onResponse: "+ Arrays.toString(productName));

                                ArrayList<String> pNames = new ArrayList<>(Arrays.asList(productName));
                                ArrayList<String> pImages = new ArrayList<>(Arrays.asList(productImage));


                               // for (int j = 0; j < productImage.length; j++) {

                                    for (int k = pNames.size()-1; k >=0; k--) {
                                        MarketPriceList shop = new MarketPriceList();
                                        shop.setId(user_id);
                                        shop.setShopName(name);
                                        shop.setAddress(address);
                                        shop.setRating(rating);
                                        shop.setImage(productImage[k]);
                                        shop.setProductName(productName[k]);
                                        shop.setPrice(productPrice[k]);
                                        shop.setWeight(productWeight[k]);
                                        shop.setObj(result.toString());
                                        marketPriceLists.add(shop);
                                    }





                            //    }

                            }


                            if (marketPriceLists.size() > 0) {

                                adapter = new MarketPricesListAdapter(mContext, marketPriceLists, MarketPricesActivity.this::onClick,marketPriceLists);
                                LinearLayoutManager manager = new LinearLayoutManager(mContext);
                                manager.setOrientation(RecyclerView.VERTICAL);
                                rv_Medicalshp.setLayoutManager(manager);
                                rv_Medicalshp.setAdapter(adapter);

                            } else {
                                et_Search.setVisibility(View.GONE);
                                rv_Medicalshp.setVisibility(View.GONE);
                                tv_Notavailable.setVisibility(View.VISIBLE);
                            }

                        } else {
                            et_Search.setVisibility(View.GONE);
                            rv_Medicalshp.setVisibility(View.GONE);
                            tv_Notavailable.setVisibility(View.VISIBLE);
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

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Head = findViewById(R.id.tv_Head);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);
        rv_Medicalshp = findViewById(R.id.rv_Medicalshp);
        ll_Login = findViewById(R.id.ll_Login);
        et_Search = findViewById(R.id.et_Search);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);
        ll_Login.setOnClickListener(this);

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
      //  Animatoo.animateCard(mContext);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.ll_Login:
                startActivity(new Intent(mContext, MarketPricesLoginActivity.class));
                Animatoo.animateCard(mContext);
                break;
        }
    }

    @Override
    public void onClick(int position, MarketPriceList marketProduct) {
        mId = marketProduct.getId();
        img = marketProduct.getImage();
        title = marketProduct.getShopName();
        msgg = marketProduct.getProductName();
        if (img.equalsIgnoreCase("http://fghdoctors.com/admin/")) {
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.launcher);
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES) + "/Share.png";
            OutputStream out = null;
            File file = new File(path);
            try {
                out = new FileOutputStream(file);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
                out.flush();
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            path = file.getPath();
            Uri bmpUri = Uri.parse("file://" + path);
            String msg = "*" + title + "*\n" + msgg + "\n\nhttps://fghdoctors.com/liveupdates/" + mId + "\n\nFor more such live updates and online Doctor Booking, download the FGHDoctor app.\n\nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor";
            Intent shareIntent = new Intent();
            shareIntent.setAction(Intent.ACTION_SEND);
            shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
            shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
            shareIntent.setType("image/png");
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            startActivity(Intent.createChooser(shareIntent, "send"));
        } else {
            if (Utility.isNetworkConnected(mContext)) {
                if (checkPermission()) {
                    new DownloadTask().execute(stringToURL(marketProduct.getImage()));
                } else {
                    requestStoragePermission();
                }
            } else {
                AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                        "You don't have internet connection.", false);
            }
        }


    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(mContext, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            } else {
                setResult(RESULT_CANCELED);
                CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "To Share, please grant permissions!");
            }
        }
    }

    private class DownloadTask extends AsyncTask<URL, Void, Bitmap> {

        ProgressDialog mProgressDialog = null;

        protected void onPreExecute() {
            mProgressDialog = new ProgressDialog(mContext);
            mProgressDialog.setTitle("Saving Image");
            mProgressDialog.setMessage("Downloading...");
            mProgressDialog.setIndeterminate(false);
            mProgressDialog.show();
        }

        protected Bitmap doInBackground(URL... urls) {
            URL url = urls[0];
            HttpURLConnection connection = null;
            try {
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream inputStream = connection.getInputStream();
                BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
                return BitmapFactory.decodeStream(bufferedInputStream);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        // When all async task done
        @SuppressLint("WrongThread")
        protected void onPostExecute(Bitmap result) {
            // Hide the progress dialog
            mProgressDialog.dismiss();
            if (result != null) {
                String root = Environment.getExternalStorageDirectory().toString();
                File myDir = new File(root + "/FGH_Share");
                myDir.mkdirs();
                Random generator = new Random();
                int n = 10000;
                n = generator.nextInt(n);
                String fname = "FGHImage-" + n + ".jpg";
                File file = new File(myDir, fname);
                Log.e("SaveIMG", "" + file);
                if (file.exists())
                    file.delete();
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                try {
                    FileOutputStream out = new FileOutputStream(file);
                    result.compress(Bitmap.CompressFormat.JPEG, 90, out);
                    out.flush();
                    out.close();
//                    Toast.makeText(mContext, "foto guardada en la galería", Toast.LENGTH_SHORT).show();
                    MediaScannerConnection.scanFile(mContext, new String[]{file.toString()}, null,
                            new MediaScannerConnection.OnScanCompletedListener() {
                                public void onScanCompleted(String path, Uri uri) {
                                    Log.i("ExternalStorage", "Scanned " + path + ":");
                                    Log.i("ExternalStorage", "-> uri=" + uri);
                                    String msg = "*" + title + "*\n" + msgg + "\n\nhttps://fghdoctors.com/liveupdates/" + mId + "\n\nFor more such live updates and online Doctor Booking, download the FGHDoctor app.\n\nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor";
                                    Intent shareIntent = new Intent();
                                    shareIntent.setAction(Intent.ACTION_SEND);
                                    shareIntent.putExtra(Intent.EXTRA_TEXT, msg);
                                    shareIntent.putExtra(Intent.EXTRA_STREAM, uri);
                                    shareIntent.setType("image/png");
                                    shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                                    startActivity(Intent.createChooser(shareIntent, "send"));
                                }
                            });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                // Notify user that an error occurred while downloading image
                Toast.makeText(mContext, "Error", Toast.LENGTH_SHORT).show();
            }
        }
    }

    protected URL stringToURL(String img) {
        try {
            URL url = new URL(img);
            return url;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return null;
    }
}