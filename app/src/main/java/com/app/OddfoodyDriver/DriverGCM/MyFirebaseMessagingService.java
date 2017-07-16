package com.app.OddfoodyDriver.DriverGCM;

/**
 * Created by Invenzo on 03-10-2016.
 */

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.app.OddfoodyDriver.DriverActivities.NotificationListActivity;
import com.app.OddfoodyDriver.DriverActivities.SplashScreenActivity;
import com.app.OddfoodyDriver.DriverUtils.LoginPrefManager;
import com.app.OddfoodyDriver.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;


public class MyFirebaseMessagingService extends FirebaseMessagingService {

    private LoginPrefManager loginPrefManager;

    private static final String TAG = "MyFirebaseMsgService";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        Log.e(TAG, "NotificationItem data id: " + remoteMessage.getData().get("id"));
        Log.e(TAG, "NotificationItem: " + remoteMessage.getData().toString());


        // Calling method to generate notification
        loginPrefManager = new LoginPrefManager(getBaseContext());

        if (loginPrefManager.getUserLoginDriverId().length() == 0) {
            sendSplashScreenNotification("" + remoteMessage.getData().get("message"), "" + remoteMessage.getData().get("title"));
        } else {
            sendOrderDetailNotification("" + remoteMessage.getData().get("message"), "" + remoteMessage.getData().get("title"), "" + remoteMessage.getData().get("id"));
        }

    }

    private void sendSplashScreenNotification(String message, String noti_title) {

        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.oddfoody_notifi_samll).setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle("" + noti_title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }


    private void sendOrderDetailNotification(String message, String noti_title, String order_id) {

        loginPrefManager.setStringValue("order_id", "" + order_id);

        Intent intent = new Intent(this, NotificationListActivity.class);
        intent.putExtra("notif_conf_dialog", "1");
        intent.setFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent, 0);

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.oddfoody_notifi_samll).setColor(getResources().getColor(R.color.colorAccent))
                .setContentTitle("" + noti_title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());

    }




}