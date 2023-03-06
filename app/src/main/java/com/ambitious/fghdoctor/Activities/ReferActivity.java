package com.ambitious.fghdoctor.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;

import java.util.List;

public class ReferActivity extends AppCompatActivity implements View.OnClickListener {

    private Context mContext = this;
    private ImageView iv_Bck, iv_Fb, iv_Fbmess, iv_More;
    private LinearLayout ll_Wallet;
    private RelativeLayout rl_Watsapp;
    private String referel_code = "";
    private String referral_content ="\n\nOur Services :\n" +
            "\n" +
            "Health Releted Services , Nearby shops Grocery Products & Prices With Delivery Boy Services , Short News , All Vehicles Rental Services , Share & Earning Money Every month\n" +
            "\n" +
            "(Coming Soon ) UPI & Recharges , Entertainment , Courses With Jobs , Agriculture Releted Services , Education Releted Services  , Lot , Affiliate Earn money , Videos, Photos Posts on earning money, Manufacturer Services And more....\n" +
            "\n" +
            "FGH యాప్ సర్వీసులను వినియోగించుకోండి షేర్ చేయండి  ప్రతీనెల సంపాదించండి";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refer);
        if (getIntent().getExtras() != null) {
            referel_code = getIntent().getStringExtra("code");
        }
        finds();
    }

    private void finds() {

        iv_Bck = findViewById(R.id.iv_Bck);
        iv_Fb = findViewById(R.id.iv_Fb);
        iv_Fbmess = findViewById(R.id.iv_Fbmess);
        iv_More = findViewById(R.id.iv_More);
        ll_Wallet = findViewById(R.id.ll_Wallet);
        rl_Watsapp = findViewById(R.id.rl_Watsapp);

        iv_Bck.setOnClickListener(this);
        iv_Fb.setOnClickListener(this);
        iv_Fbmess.setOnClickListener(this);
        iv_More.setOnClickListener(this);
        ll_Wallet.setOnClickListener(this);
        rl_Watsapp.setOnClickListener(this);
        ll_Wallet.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.iv_Bck:
                onBackPressed();
                break;

            case R.id.ll_Wallet:
                startActivity(new Intent(mContext, MyWalletActivity.class));
                Animatoo.animateCard(mContext);
                break;

            case R.id.iv_Fb:
                String urlToShare = "Please Install FGH Tech Solutions \nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor \n& Earn Above ₹10000 every month Using this Referral Code - " + referel_code +referral_content;
                Intent fintent = new Intent(Intent.ACTION_SEND);
                fintent.setType("text/plain");
                fintent.putExtra(Intent.EXTRA_TEXT, urlToShare);
                boolean facebookAppFound = false;
                List<ResolveInfo> matches = mContext.getPackageManager().queryIntentActivities(fintent, 0);
                for (ResolveInfo info : matches) {
                    if (info.activityInfo.packageName.toLowerCase().startsWith("com.facebook.katana")) {
                        fintent.setPackage(info.activityInfo.packageName);
                        facebookAppFound = true;
                        break;
                    }
                }
                if (!facebookAppFound) {
                    String sharerUrl = "https://www.facebook.com/sharer/sharer.php?u=" + urlToShare;
                    Intent intent1 = new Intent(Intent.ACTION_VIEW, Uri.parse(sharerUrl));
                }
                startActivity(fintent);
                break;

            case R.id.iv_Fbmess:
                String textt = "Please Install FGH Tech Solutions \nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor \n& Earn Above ₹10000 every month Using this Referral Code -  " + referel_code+referral_content;
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_SEND);
                intent.putExtra(Intent.EXTRA_TEXT, textt);
                intent.setType("text/plain");
                intent.setPackage("com.facebook.orca");
                try {
                    startActivity(intent);
                } catch (android.content.ActivityNotFoundException ex) {
                    Toast.makeText(mContext, "Please Install Facebook Messenger", Toast.LENGTH_LONG).show();
                }
                break;

            case R.id.rl_Watsapp:
                PackageManager pm = mContext.getPackageManager();
                try {

                    Intent waIntent = new Intent(Intent.ACTION_SEND);
                    waIntent.setType("text/plain");
                    String text = "Please Install FGH Tech Solutions \nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor \n& Earn Above ₹10000 every month Using this Referral Code - " + referel_code+referral_content;

                    PackageInfo info = pm.getPackageInfo("com.whatsapp", PackageManager.GET_META_DATA);
                    //Check if package exists or not. If not then code
                    //in catch block will be called
                    waIntent.setPackage("com.whatsapp");

                    waIntent.putExtra(Intent.EXTRA_TEXT, text);
                    startActivity(Intent.createChooser(waIntent, "Share with"));

                } catch (PackageManager.NameNotFoundException e) {
                    Toast.makeText(mContext, "WhatsApp not Installed", Toast.LENGTH_SHORT).show();
                }
                break;

            case R.id.iv_More:
                String txts = "Please Install FGH Tech Solutions \nhttps://play.google.com/store/apps/details?id=com.ambitious.fghdoctor \n& Earn Above ₹10000 every month Using this Referral Code - " + referel_code+referral_content;
                Intent sendIntents = new Intent();
                sendIntents.setAction(Intent.ACTION_SEND);
                sendIntents.putExtra(Intent.EXTRA_TEXT, txts);
                sendIntents.setType("text/plain");
                startActivity(sendIntents);
                break;

        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Animatoo.animateCard(mContext);
    }
}