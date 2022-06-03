package com.techtown.studentmanagementapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.techtown.studentmanagementapp.MainActivity;
import com.techtown.studentmanagementapp.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static String TAG = "FirebaseMessagingService";

    @Override
    public void onNewToken(@NonNull String token) {
        super.onNewToken(token);
        Log.d(TAG, "onNewToken(" + token + ")");
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage rm) {
        super.onMessageReceived(rm);

        String title_ = "급식 알림";
        String grade_ = rm.getData().get("grade");
        String class_ = rm.getData().get("class");

        String message_ = grade_ + "학년 " + class_ + "반, 내려와주세요!";

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent intent_pending = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String channel = "SMA Channel";
            String channel_nm = "SMA";

            NotificationManager notichannel = (android.app.NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            NotificationChannel channelMessage = new NotificationChannel(channel, channel_nm,
                    android.app.NotificationManager.IMPORTANCE_DEFAULT);
            channelMessage.setDescription("채널에 대한 설명.");
            channelMessage.enableLights(true);
            channelMessage.enableVibration(true);
            channelMessage.setShowBadge(false);
            channelMessage.setVibrationPattern(new long[]{1000, 1000});
            notichannel.createNotificationChannel(channelMessage);

            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, channel)
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title_)
                            .setContentText(message_)
                            .setChannelId(channel)
                            .setAutoCancel(true)
                            .setContentIntent(intent_pending)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
            NotificationManager manager_noti =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            manager_noti.notify(2022, notificationBuilder.build());
        } else {
            NotificationCompat.Builder notificationBuilder =
                    new NotificationCompat.Builder(this, "")
                            .setSmallIcon(R.drawable.ic_launcher_background)
                            .setContentTitle(title_)
                            .setContentText(message_)
                            .setAutoCancel(true)
                            .setContentIntent(intent_pending)
                            .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

            NotificationManager manager_noti =
                    (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            manager_noti.notify(2022, notificationBuilder.build());
        }
    }
}
