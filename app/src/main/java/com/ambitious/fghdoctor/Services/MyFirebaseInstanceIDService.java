package com.ambitious.fghdoctor.Services;


import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ambitious.fghdoctor.Utils.Utility;



/**
 * Created by ritesh on 22/9/17.
 */

public class MyFirebaseInstanceIDService
       // extends FirebaseInstanceIdService
{
    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

  /*  @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        System.out.println("-----------------MyFirebaseInstanceIDService-------------");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        // sending reg id to your server
        sendRegistrationToServer(refreshedToken);

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        Intent registrationComplete = new Intent("registrationComplete");
        registrationComplete.putExtra("token", refreshedToken);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void sendRegistrationToServer(final String token) {
        // sending gcm token to server
        Log.e(TAG, "sendRegistrationToServer: " + token);
    }

    private void storeRegIdInPref(String token) {
//        SharedPreferences pref = getApplicationContext().getSharedPreferences("delex", 0);
//        SharedPreferences.Editor editor = pref.edit();
//        editor.putString("regId", token);
//        editor.commit();
        System.out.println("----------token---------- "+token);
        Utility.setSharedPreference(getApplicationContext(),"regId",token);
    }*/
}