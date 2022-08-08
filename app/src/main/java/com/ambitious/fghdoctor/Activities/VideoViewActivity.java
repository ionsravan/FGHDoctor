package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.ProgressBar;
import android.widget.VideoView;

import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

public class VideoViewActivity extends AppCompatActivity implements View.OnClickListener{

    private Context mContext = this;
    private ImageView iv_Cancel;
    private VideoView view_Video;
    private ProgressBar progress_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_video_view);
        finds();
        view_Video.setVideoPath(getIntent().getStringExtra("url"));
        MediaController mediaController = new
                MediaController(this);
        mediaController.setAnchorView(view_Video);
        view_Video.setMediaController(mediaController);

        view_Video.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                progress_Dialog.setVisibility(View.GONE);
            }
        });
        view_Video.start();

        finds();
    }

    private void finds() {

        iv_Cancel = findViewById(R.id.iv_Cancel);
        view_Video = findViewById(R.id.view_Video);
        progress_Dialog = findViewById(R.id.progress_Dialog);

        iv_Cancel.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.iv_Cancel:
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