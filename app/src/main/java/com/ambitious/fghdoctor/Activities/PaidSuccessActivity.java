package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class PaidSuccessActivity extends AppCompatActivity {
 TextView txtDone,txtDate,txtPayment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paid_success);

        txtDone = findViewById(R.id.txtDone);
        txtDate = findViewById(R.id.txtDate);
        txtPayment = findViewById(R.id.txtPayment);

        if (getIntent() != null){
            String title = getIntent().getStringExtra("title");
            String amount = getIntent().getStringExtra("amount");

            String date = new SimpleDateFormat("yyyy-MMM-dd HH:mm a", Locale.getDefault()).format(new Date());
            txtDate.setText(date);
            txtPayment.setText("Payment of Rs."+amount+" To "+title+" Successful");

        }

        txtDone.setOnClickListener(v -> {
            Intent intent = new Intent(this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            Animatoo.animateSlideLeft(this);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        playSound();
    }

    private void playSound() {

        MediaPlayer mPlayer2 = MediaPlayer.create(this, R.raw.success_notification);
        mPlayer2.start();
    }
}