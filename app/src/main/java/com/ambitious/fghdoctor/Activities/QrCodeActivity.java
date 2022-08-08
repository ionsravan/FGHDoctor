package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.ImageView;
import android.widget.TextView;

import com.ambitious.fghdoctor.R;
import com.bumptech.glide.Glide;

import java.io.File;

public class QrCodeActivity extends AppCompatActivity {

    String qrCodeUrl ="";
    String title ="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qr_code);

        if (getIntent() != null){
            qrCodeUrl = getIntent().getStringExtra("qrCode");
            title = getIntent().getStringExtra("title");
        }

        ImageView iv_Bck = findViewById(R.id.iv_Bck);
        ImageView imgQrCode = findViewById(R.id.imgQrCode);
        TextView txtShare = findViewById(R.id.txtShare);
        TextView txtDownload = findViewById(R.id.txtDownload);
        TextView txtTitle = findViewById(R.id.txtTitle);
        txtTitle.setText(title);

        Glide.with(this).load(qrCodeUrl).into(imgQrCode);


        iv_Bck.setOnClickListener(v -> {
            onBackPressed();
           // finish();


        });
        txtDownload.setOnClickListener(v -> {
            downloadFile(qrCodeUrl);
        });

        txtShare.setOnClickListener(v -> {
            Drawable mDrawable = imgQrCode.getDrawable();
            Bitmap mBitmap = ((BitmapDrawable) mDrawable).getBitmap();

            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mBitmap, "Image Description", null);
            Uri uri = Uri.parse(path);

            Intent intent = new Intent(Intent.ACTION_SEND);
            intent.setType("image/jpeg");
            intent.putExtra(Intent.EXTRA_STREAM, uri);
            startActivity(Intent.createChooser(intent, "Share QR Code"));
        });

    }

    @Override
    public void onBackPressed() {
       // super.onBackPressed();

        Intent intent = new Intent(getApplicationContext(),SplashActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void downloadFile(String uRl) {
        File direct = new File(Environment.getExternalStorageDirectory()
                + "/fgh");

        if (!direct.exists()) {
            direct.mkdirs();
        }

        DownloadManager mgr = (DownloadManager)getApplicationContext().getSystemService(Context.DOWNLOAD_SERVICE);

        Uri downloadUri = Uri.parse(uRl);
        DownloadManager.Request request = new DownloadManager.Request(
                downloadUri);

        request.setAllowedNetworkTypes(
                        DownloadManager.Request.NETWORK_WIFI
                                | DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false).setTitle("Demo")
                .setDescription("Something useful. No, really.")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,"parag.jpeg");

        // .setDestinationInExternalPublicDir("/Downloads", "fileName.jpg");

        mgr.enqueue(request);

    }
}