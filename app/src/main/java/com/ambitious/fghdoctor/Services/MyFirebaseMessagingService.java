package com.ambitious.fghdoctor.Services;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import com.ambitious.fghdoctor.Activities.HomeActivity;
import com.ambitious.fghdoctor.Activities.NotificationDetailActivity;
import com.ambitious.fghdoctor.Activities.SplashActivity;
import com.ambitious.fghdoctor.R;
import com.ambitious.fghdoctor.Utils.Utility;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import org.json.JSONException;
import org.json.JSONObject;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();
    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
//        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);
//        showNotificationMessage(getApplicationContext(), "Nice", "New Post Added.", "Timestamp", resultIntent);
        System.out.println("------------------------In MyFirebaseMessagingService---------------------------------");
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                JSONObject json = new JSONObject(remoteMessage.getData().toString());
                handleDataMessage(json);
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }
    }

    private void handleNotification(String message) {
        Intent resultIntent = new Intent(getApplicationContext(), HomeActivity.class);

        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent("pushNotification");
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" + message, "Timestamp", resultIntent);
        } else {
            showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), "" + message, "Timestamp", resultIntent);
            // If the app is in background, firebase itself handles the notification
        }
    }


    private void handleDataMessage(JSONObject json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            JSONObject data = json.getJSONObject("message");
            String lang = Utility.getSharedPreferences(getApplicationContext(), "def_lang");
            String title = "";
            String message = "";
            String key = "";
            String notification_id = "";
            if (lang.equalsIgnoreCase("ar")) {
                message = data.getString("message_ar");
                key = data.getString("key_ar");
            } else {
                title = data.getString("title");
                message = data.getString("message");
                key = data.getString("key");
                notification_id = data.getString("notification_id");
            }

            Intent resultIntent = new Intent(getApplicationContext(), NotificationDetailActivity.class)
                    .putExtra("img", "")
                    .putExtra("notification_id", "" + notification_id);

            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Intent pushNotification = new Intent("pushNotification");
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
//                showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), ""+key+"\n"+message, "Timestamp", resultIntent);
                if (Utility.getSharedPreferencesBoolean(getApplicationContext(), "allownotifications", true)) {
                    showNotificationMessage(getApplicationContext(), "" + title, "" + message, "Timestamp", resultIntent);
                }
            } else {
//                showNotificationMessage(getApplicationContext(), getResources().getString(R.string.app_name), ""+key+"\n"+message, "Timestamp", resultIntent);
                if (Utility.getSharedPreferencesBoolean(getApplicationContext(), "allownotifications", true)) {
                    showNotificationMessage(getApplicationContext(), "" + title, "" + message, "Timestamp", resultIntent);
                }
                // If the app is in background, firebase itself handles the notification
            }

            if (key.equalsIgnoreCase("You have a new message")) {

                String c = Utility.getSharedPreferences(getApplicationContext(), "count");
                if (c.equalsIgnoreCase("")) {
                    int i = 1;
                    Utility.setSharedPreference(getApplicationContext(), "count", "" + i);
                } else {
                    int i = Integer.parseInt(c);
                    int i1 = i++;
                    Utility.setSharedPreference(getApplicationContext(), "count", "" + i);
                }

            } else {

                String c1 = Utility.getSharedPreferences(getApplicationContext(), "noti_count");
                if (c1.equalsIgnoreCase("")) {
                    int i = 1;
                    Utility.setSharedPreference(getApplicationContext(), "noti_count", "" + i);
                } else {
                    int i = Integer.parseInt(c1);
                    int i1 = i++;
                    Utility.setSharedPreference(getApplicationContext(), "noti_count", "" + i);
                }

            }

        } catch (JSONException e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent);
    }

    /*
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, intent, imageUrl);
    }
}