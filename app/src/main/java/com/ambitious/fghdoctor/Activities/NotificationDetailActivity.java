package com.ambitious.fghdoctor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.Adapters.NotificationAdapter;
import com.ambitious.fghdoctor.Model.Noti;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.bumptech.glide.Glide;

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
import java.util.Random;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;

public class NotificationDetailActivity extends AppCompatActivity implements View.OnClickListener {


    private Context mContext = this;
    private ImageView iv_Bck, iv_Image;
    private TextView tv_Title, tv_Msg;
    private LinearLayout ll_Share;
    private String img = "", title = "", msgg = "", nid = "";
    private RelativeLayout rl_Loader;
    private static final int EXTERNAL_PERMISSION_CODE = 1234;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification_detail);
        finds();

        if (getIntent().getExtras() != null) {

            nid = getIntent().getStringExtra("nid");
            img = getIntent().getStringExtra("img");
            title = getIntent().getStringExtra("title");
            msgg = getIntent().getStringExtra("msg");

            if (img == null) {

                Uri uri = getIntent().getData();
                String vurl = uri.toString().replace("https://fghdoctors.com/liveupdates/", "");
                Log.e("From Deep Link==>", "" + vurl);

                getNotification(vurl, iv_Bck);

            } else if (img.equalsIgnoreCase("")) {
                String notification_id = getIntent().getStringExtra("notification_id");
                getNotification(notification_id, iv_Bck);

            } else {

                iv_Image.setVisibility(View.VISIBLE);
                tv_Title.setVisibility(View.VISIBLE);
                tv_Msg.setVisibility(View.VISIBLE);
                ll_Share.setVisibility(View.VISIBLE);

                if (!img.equalsIgnoreCase("")) {
                    iv_Image.setVisibility(View.VISIBLE);
                    Glide.with(mContext).load(img).into(iv_Image);
                } else {
                    iv_Image.setVisibility(View.GONE);
                }

                tv_Title.setText(title);
                tv_Msg.setText(msgg);

            }

        } else {
            iv_Image.setVisibility(View.GONE);
            tv_Title.setVisibility(View.GONE);
            tv_Msg.setVisibility(View.GONE);
            ll_Share.setVisibility(View.GONE);
        }
    }

    private void getNotification(String id, ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getNotification(id);
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
                        System.out.println("SingleNotification" + object);

                        if (status.equalsIgnoreCase("1")) {

                            iv_Image.setVisibility(View.VISIBLE);
                            tv_Title.setVisibility(View.VISIBLE);
                            tv_Msg.setVisibility(View.VISIBLE);
                            ll_Share.setVisibility(View.VISIBLE);
                            JSONArray arr = object.getJSONArray("result");
                            JSONObject result = arr.getJSONObject(0);
                            nid = result.optString("notification_id");
                            title = result.optString("title");
                            msgg = result.optString("message");
                            img = result.optString("image");

                            if (!img.equalsIgnoreCase("")) {
                                iv_Image.setVisibility(View.VISIBLE);
                                Glide.with(mContext).load(img).into(iv_Image);
                            } else {
                                iv_Image.setVisibility(View.GONE);
                            }

                            tv_Title.setText(title);
                            tv_Msg.setText(msgg);

                        } else {
                            iv_Image.setVisibility(View.GONE);
                            tv_Title.setVisibility(View.GONE);
                            tv_Msg.setVisibility(View.GONE);
                            ll_Share.setVisibility(View.GONE);
                            CustomSnakbar.showDarkSnakabar(mContext, view, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, view, "SingleNotification " + e.getMessage());
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

        rl_Loader = findViewById(R.id.rl_Loader);
        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Image = findViewById(R.id.iv_Image);
        tv_Title = findViewById(R.id.tv_Title);
        tv_Msg = findViewById(R.id.tv_Msg);
        ll_Share = findViewById(R.id.ll_Share);

        iv_Bck.setOnClickListener(this);
        ll_Share.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.ll_Share:
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
                    String msg = "*" + title + "*\n" + msgg + "\n\nhttps://fghdoctors.com/liveupdates/" + nid + "\n\nFor more such live updates and online Doctor Booking, download the FGHDoctor app.\n\nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor";
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
                            new DownloadTask().execute(stringToURL(img));
                        } else {
                            requestStoragePermission();
                        }
                    } else {
                        AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                                "You don't have internet connection.", false);
                    }
                }
                break;

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
                                    String msg = "*" + title + "*\n" + msgg + "\n\nhttps://fghdoctors.com/liveupdates/" + nid + "\n\nFor more such live updates and online Doctor Booking, download the FGHDoctor app.\n\nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor";
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

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}