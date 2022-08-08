package com.ambitious.fghdoctor.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.google.firebase.messaging.FirebaseMessaging;


public class BroadcastNotiReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("registrationComplete")) {
            // gcm successfully registered
            // now subscribe to `global` topic to receive app wide notifications
            FirebaseMessaging.getInstance().subscribeToTopic("global");

            displayFirebaseRegId();

        } else if (intent.getAction().equals("pushNotification")) {
            // new push notification is received

            String message = intent.getStringExtra("message");

//            Toast.makeText(AppController.getInstance(), "Push notification: " + message, Toast.LENGTH_LONG).show();

//                    txtMessage.setText(message);
        }
        displayFirebaseRegId();
    }
    public void displayFirebaseRegId() {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences(Config.SHARED_PREF, 0);
//        String regId = pref.getString("regId", null);
//        String regId = getData(AppController.getInstance(), "regId", null);
//        Log.e("REG_ID", "Firebase reg id: " + regId);

        /*if (!TextUtils.isEmpty(regId)) {
            System.out.println("Firebase Reg Id: " + regId);
            saveData(AppController.getInstance(),"regId",regId);
        }
//            txtRegId.setText("Firebase Reg Id: " + regId);
        else {
            System.out.println("Firebase Reg Id is not received yet!");
        }*/
//            txtRegId.setText("Firebase Reg Id is not received yet!");
    }
}
