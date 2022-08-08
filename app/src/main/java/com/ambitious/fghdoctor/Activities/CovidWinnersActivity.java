package com.ambitious.fghdoctor.Activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Dialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.ThumbnailUtils;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
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
import android.widget.VideoView;

import com.ambitious.fghdoctor.Adapters.BloodListAdpter;
import com.ambitious.fghdoctor.Adapters.WinnersAdapter;
import com.ambitious.fghdoctor.Model.AmbuDriver;
import com.ambitious.fghdoctor.Model.BloodDonor;
import com.ambitious.fghdoctor.Model.Winners;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.AlertConnection;
import com.ambitious.fghdoctor.Utils.AppConfig;
import com.ambitious.fghdoctor.Utils.CustomSnakbar;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import org.json.JSONArray;
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

public class CovidWinnersActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private LinearLayout ll_Add;
    private RecyclerView rv_Covidwinners;
    private RelativeLayout rl_Loader;
    private TextView tv_Notavailable;
    private EditText et_Search;
    private ArrayList<Winners> winnersArrayList;
    private WinnersAdapter adapter;
    private static final int EXTERNAL_PERMISSION_CODE = 1234;
    private String vpath = "", vurl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_covid_winners);
        finds();

        if (getIntent().getExtras() != null) {
            Uri uri = getIntent().getData();
            vurl = uri.toString().replace("https://fghdoctors.com/fitnessvideo/", "");
            Log.e("From Deep Link==>", "" + vurl);
        }

        if (Utility.isNetworkConnected(mContext)) {
            String uid = Utility.getSharedPreferences(mContext, "u_id");
            getVideos(uid, iv_Bck);
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

                if (winnersArrayList.size() > 0) {
                    filter(s.toString());
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Ambulance is not available.");
                }

            }
        });
    }

    private void filter(String text) {

        ArrayList<Winners> temp = new ArrayList();
        for (Winners d : winnersArrayList) {
            //or use .equal(text) with you want equal match
            //use .toLowerCase() for better matches
            if (d.getName().toLowerCase().contains(text)) {
                temp.add(d);
            } else if (d.getCity().toLowerCase().contains(text)) {
                temp.add(d);
            }
        }
        if (temp.size() > 0) {
            rv_Covidwinners.setVisibility(View.VISIBLE);
            tv_Notavailable.setVisibility(View.GONE);
            adapter.updateList(temp);
        } else {
            rv_Covidwinners.setVisibility(View.GONE);
            tv_Notavailable.setVisibility(View.VISIBLE);
        }

    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        ll_Add = findViewById(R.id.ll_Add);
        rv_Covidwinners = findViewById(R.id.rv_Covidwinners);
        et_Search = findViewById(R.id.et_Search);
        rl_Loader = findViewById(R.id.rl_Loader);
        tv_Notavailable = findViewById(R.id.tv_Notavailable);


        iv_Bck.setOnClickListener(this);
        ll_Add.setOnClickListener(this);
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

            case R.id.ll_Add:
                if (Utility.getSharedPreferencesBoolean(mContext, "islogin", false)) {
                    if (checkPermission()) {
                        Intent intent = new Intent();
                        intent.setType("video/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(intent, "Select Video"), 200);
                    } else {
                        requestStoragePermission();
                    }
                } else {
                    CustomSnakbar.showDarkSnakabar(mContext, v, "Please Login/Register Before Add Video!");
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, LoginActivity.class));
                            Animatoo.animateCard(mContext);
                        }
                    }, 1500);
                }
                break;

        }

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == RESULT_OK) {
            Uri selectedImageUri = data.getData();
            vpath = getPathFromUri(mContext, selectedImageUri);
//            Toast.makeText(getContext(), "VideoPath=>" + vpath, Toast.LENGTH_SHORT).show();
            Log.e("vid_path", "--->" + vpath);
            if (vpath != null) {
                File vFile = new File(vpath);
                if (vFile.exists()) {
                    MediaPlayer mp = MediaPlayer.create(mContext, selectedImageUri);
                    int duration = mp.getDuration();
                    mp.release();
//                String time = String.format("%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(duration), TimeUnit.MILLISECONDS.toSeconds(duration) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(duration)));
//                Toast.makeText(getContext(), "" + time, Toast.LENGTH_SHORT).show();
                    if (duration <= 180000) {
                        Bitmap myBitmap = ThumbnailUtils.createVideoThumbnail(vpath, MediaStore.Video.Thumbnails.MICRO_KIND);

                        final Dialog dialog = new Dialog(mContext, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
                        dialog.setContentView(R.layout.dialog_image_chat);
                        dialog.show();

                        TextView tv_Head = dialog.findViewById(R.id.tv_Head);
                        ImageView iv_Cancel = dialog.findViewById(R.id.iv_Cancel);
                        ImageView iv_Done = dialog.findViewById(R.id.iv_Done);
                        ImageView iv_Image = dialog.findViewById(R.id.iv_Image);
                        ImageView iv_Video = dialog.findViewById(R.id.iv_Video);
                        ImageView iv_Play = dialog.findViewById(R.id.iv_Play);
                        VideoView view_Video = dialog.findViewById(R.id.view_Video);
                        EditText et_Caption = dialog.findViewById(R.id.et_Caption);
                        RelativeLayout rl_Loader = dialog.findViewById(R.id.rl_Loader);

//                        iv_Video.setVisibility(View.VISIBLE);
                        iv_Play.setVisibility(View.VISIBLE);
                        iv_Image.setImageBitmap(myBitmap);
                        tv_Head.setText("Add Video");

                        iv_Cancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                                vpath = "";
                                dialog.dismiss();

                            }
                        });

                        iv_Done.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String title = et_Caption.getText().toString();
                                if (Utility.isNetworkConnected(mContext)) {
                                    String uid = Utility.getSharedPreferences(mContext, "u_id");
                                    RequestBody requestFile = RequestBody.create(MediaType.parse("multipart/form-data"), vFile);
                                    MultipartBody.Part body = MultipartBody.Part.createFormData("video", vFile.getName(), requestFile);
                                    insertuserimage(uid, title, body, v, rl_Loader, dialog);
                                } else {
                                    AlertConnection.showAlertDialog(mContext, "No Internet Connection",
                                            "You don't have internet connection.", false);
                                }
                            }
                        });

                        iv_Play.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                iv_Video.setVisibility(View.GONE);
                                iv_Play.setVisibility(View.GONE);
                                view_Video.setVisibility(View.VISIBLE);
                                view_Video.setVideoURI(selectedImageUri);
                                view_Video.start();
                            }
                        });

                    } else {
                        CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Select Video 3 minute maximum.");
                    }

                } else {
                    Toast.makeText(mContext, "File 1 is not exist.", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(mContext, "Este archivo de video no existe.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void insertuserimage(String uid, String title, MultipartBody.Part body, View v, RelativeLayout rl_Loader, Dialog dialog) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().addvideo(uid, body, title);
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
                        System.out.println("AddVideo" + object);

                        if (status.equalsIgnoreCase("1")) {
                            dialog.dismiss();
                            CustomSnakbar.showSnakabar(mContext, iv_Bck, "Video Uploaded Successfull.");
                            getVideos(uid, iv_Bck);

                        } else {
                            CustomSnakbar.showSnakabar(mContext, iv_Bck, "" + resultmessage);
                        }


                    } else ;

                } catch (IOException | JSONException e) {
                    e.printStackTrace();
                    CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "Profile " + e.getMessage());
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

    private void getVideos(String uid, ImageView view) {

        rl_Loader.setVisibility(View.VISIBLE);
        Call<ResponseBody> call = AppConfig.loadInterface().getVideo("1");
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
                        System.out.println("allVideos" + object);

                        if (status.equalsIgnoreCase("1")) {

                            rv_Covidwinners.setVisibility(View.VISIBLE);
                            tv_Notavailable.setVisibility(View.GONE);

                            JSONArray array = object.optJSONArray("result");

                            winnersArrayList = new ArrayList<>();
                            for (int i = 0; i < array.length(); i++) {

                                JSONObject result = array.getJSONObject(i);

                                String user_id = result.optString("user_id");
                                String name = result.optString("name");
                                String user_image = result.optString("user_image");
                                String video = result.optString("video");
                                String video_title = result.optString("video_title");
                                String city = result.optString("city");
                                String mobile = result.optString("mobile");

//                                if (uid.equalsIgnoreCase(user_id)) {
//                                    ll_Add.setVisibility(View.GONE);
//                                }

                                if (vurl.equalsIgnoreCase("")) {
                                    Winners win = new Winners(user_id, name, mobile, city, user_image, video, video_title);
                                    winnersArrayList.add(win);
                                }else {
                                    if (vurl.equalsIgnoreCase(user_id)){
                                        Winners win = new Winners(user_id, name, mobile, city, user_image, video, video_title);
                                        winnersArrayList.add(win);
                                    }
                                }

                            }

                            adapter = new WinnersAdapter(mContext, winnersArrayList);
                            LinearLayoutManager manager = new LinearLayoutManager(mContext);
                            manager.setOrientation(RecyclerView.VERTICAL);
                            rv_Covidwinners.setLayoutManager(manager);
                            rv_Covidwinners.setAdapter(adapter);


                        } else {
                            rv_Covidwinners.setVisibility(View.GONE);
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

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    private boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        return currentAPIVersion < Build.VERSION_CODES.M || ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    private void requestStoragePermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, EXTERNAL_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == EXTERNAL_PERMISSION_CODE) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Video"), 200);
            } else {
                this.setResult(RESULT_CANCELED);
                CustomSnakbar.showDarkSnakabar(mContext, iv_Bck, "To add video, please grant permissions!");
            }
        }
    }

    public static String getPathFromUri(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {

            // Return the remote address
            if (isGooglePhotosUri(uri))
                return uri.getLastPathSegment();

            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }
}