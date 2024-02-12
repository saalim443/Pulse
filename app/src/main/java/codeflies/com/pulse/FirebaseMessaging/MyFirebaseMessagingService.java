package codeflies.com.pulse.FirebaseMessaging;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import codeflies.com.pulse.Helpers.SharedPreference;
import codeflies.com.pulse.Home.MainActivity;


public class MyFirebaseMessagingService extends FirebaseMessagingService {
    private static final String TAG = MyFirebaseMessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;
    String channelId = "channel-01";


    SharedPreference sharedPreference;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        sharedPreference = new SharedPreference(this);

        Log.e("FIREBASE_NOT_DATA", " Data Title: " + remoteMessage.getData().get("title") + " !! " + remoteMessage.getData().toString());

        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());

            try {
                handleDataMessage(remoteMessage.getData().get("title"), remoteMessage.getData().get("body"));
            } catch (Exception e) {
                Log.e(TAG, "Exception: " + e.getMessage());
            }
        }

    }

    @Override
    public void onNewToken(String token) {
        Log.d(TAG, "Refreshed token: " + token);
    }

    private void handleDataMessage(String title, String desc) {
        try {
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                Intent fullScreenIntent = new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                showNotificationMessage(getApplicationContext(), title,desc, "timestamp", fullScreenIntent);

            }else{
                Intent fullScreenIntent = new Intent(this, MainActivity.class)
                        .setFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK)
                        .addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                showNotificationMessage(getApplicationContext(), title,desc, "timestamp", fullScreenIntent);

            }
        } catch (Exception e) {
            Log.e(TAG, "Json Exception: " + e.getMessage());
        }

    }

    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context, channelId);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}
