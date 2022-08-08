package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class WebviewActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck;
    private TextView tv_Noitem;
    private WebView view_Web;
    private RelativeLayout rl_Loader;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);
        finds();

        view_Web.getSettings().setJavaScriptEnabled(true); // enable javascript

        view_Web.getSettings().setLoadWithOverviewMode(false);
        view_Web.getSettings().setUseWideViewPort(false);
        view_Web.getSettings().setBuiltInZoomControls(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            view_Web.getSettings().setSafeBrowsingEnabled(false);
        }

        String url = getIntent().getStringExtra("url");

        view_Web.loadUrl("https://drive.google.com/viewerng/viewer?embedded=true&url=" + url);

        view_Web.setWebViewClient(new WebViewClient() {
            public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
                rl_Loader.setVisibility(View.GONE);
                view_Web.setVisibility(View.GONE);
                tv_Noitem.setVisibility(View.VISIBLE);
                tv_Noitem.setText(description);
                Toast.makeText(mContext, description, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                rl_Loader.setVisibility(View.VISIBLE);
            }


            @Override
            public void onPageFinished(WebView view, String url) {
                rl_Loader.setVisibility(View.GONE);
                tv_Noitem.setVisibility(View.GONE);
                view_Web.setVisibility(View.VISIBLE);
            }

        });
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        tv_Noitem = findViewById(R.id.tv_Noitem);
        view_Web = findViewById(R.id.view_Web);
        rl_Loader = findViewById(R.id.rl_Loader);

        iv_Bck.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}