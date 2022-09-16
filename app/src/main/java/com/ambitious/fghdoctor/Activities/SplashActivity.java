package com.ambitious.fghdoctor.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.Utility;
import com.blogspot.atifsoftwares.animatoolib.Animatoo;
import com.google.firebase.messaging.FirebaseMessaging;

public class SplashActivity extends AppCompatActivity {

    private Context mContext = this;
    private String[] perm = {"android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION"};
    private int permstats = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        AccessPremissions();

        fcmToken();
    }

    private void fcmToken(){
        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful()) {
                        System.out.println("Fetching FCM registration token failed");
                        return;
                    }

                    // Get new FCM registration token
                    String token = task.getResult();
                    Utility.setSharedPreference(getApplicationContext(),"regId",token);

                    // Log and toast
                    System.out.println(token);
                   /* Toast.makeText(SplashActivity.this, "Your device registration token is" + token
                            , Toast.LENGTH_SHORT).show();
*/

                });

    }


    private void AccessPremissions() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(perm, permstats);
        } else {
            ProceedAfterPermisson();
        }
    }

    private void ProceedAfterPermisson() {

                if (Utility.getSharedPreferences(mContext, "user_type").contains("ambulance")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, AmbulanceHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("user")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, HomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("donor")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, BloodDonorHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("bank")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, BloodBankHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("medical")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, MedicalHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("rmp")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, RMPDoctorHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("vaterinary")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, VatinaryDoctorHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("labs")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, LabUpdateActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("oxygen paid")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, OxygenBankHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("oxygen free")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, OxygenBankHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("Helping")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, HelpingSoldierHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("Sanitization")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, SanitizationHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("FoodBank")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, FoodBankHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("Vehicle")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, VehicleHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("Delivery")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, DeliveryBoyHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }else if (Utility.getSharedPreferences(mContext, "user_type").equalsIgnoreCase("market")) {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, MarketPriceHomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                } else {
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startActivity(new Intent(mContext, HomeActivity.class));
                            Animatoo.animateCard(mContext);
                            finish();
                        }
                    }, 3000);
                }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == permstats) {
            boolean acess = false;
            for (int i = 0; i < grantResults.length; i++) {
                acess = grantResults[i] == PackageManager.PERMISSION_GRANTED;
//                if (!acess) {
//                    AccessPremissions();
//                    break;
//                }
            }
            if (acess) {
                ProceedAfterPermisson();
            }
        }
    }
}
