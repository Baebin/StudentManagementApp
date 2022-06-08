package com.techtown.studentmanagementapp.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.google.firebase.messaging.RemoteMessage;
import com.techtown.studentmanagementapp.MainActivity;
import com.techtown.studentmanagementapp.R;

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService {
    public static String TAG = "FirebaseMessagingService";

    private static final String CHANNEL_ID = "SMA Channel";
    private static final String CHANNEL_NAME = "SMA";

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

        Intent intent_main = new Intent(this, MainActivity.class);
        PendingIntent intent_pending = PendingIntent.getActivity(
                this, 0, intent_main, PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap rog = BitmapFactory.decodeResource(getResources(), R.drawable.rog);

        NotificationCompat.Builder builder =
                new NotificationCompat.Builder(this, "")
                        //.setSmallIcon(R.drawable.ic_launcher_background)
                        .setLargeIcon(rog)
                        .setSmallIcon(R.drawable.rog)
                        .setContentTitle(title_)
                        .setContentText(message_)
                        .setAutoCancel(true)
                        .setContentIntent(intent_pending)
                        .setDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);

        NotificationManager manager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID, CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription("급식 알리미");
            channel.enableLights(true);
            channel.enableVibration(true);
            channel.setShowBadge(false);
            channel.setVibrationPattern(new long[]{1000, 1000});

            manager.createNotificationChannel(channel);
            builder.setChannelId(CHANNEL_ID);
        }

        manager.notify(2022, builder.build());
    }
}
