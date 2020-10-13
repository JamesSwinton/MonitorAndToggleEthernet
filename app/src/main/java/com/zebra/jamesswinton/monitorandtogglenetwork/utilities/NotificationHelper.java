package com.zebra.jamesswinton.monitorandtogglenetwork.utilities;

import android.app.Notification;
import android.app.NotificationChannel;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.zebra.jamesswinton.monitorandtogglenetwork.R;

public class NotificationHelper {

    // Constants
    public static final int NOTIFICATION_ID = 1;

    public static Notification createNotification(Context cx) {
        // Create Variables
        String channelId = "com.zebra.networkmonitor";
        String channelName = "Foreground Service Notification Channel";

        // Create Channel
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId,
                    channelName, android.app.NotificationManager.IMPORTANCE_NONE);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);

            // Set Channel
            android.app.NotificationManager manager = (android.app.NotificationManager)
                    cx.getSystemService(Context.NOTIFICATION_SERVICE);
            if (manager != null) {
                manager.createNotificationChannel(notificationChannel);
            }
        }

        // Build Notification
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(cx,
                channelId);

        // Set Notification Options
        notificationBuilder.setContentTitle("Network Monitoring Active")
                .setSmallIcon(R.drawable.ic_monitor_network)
                .setCategory(Notification.CATEGORY_SERVICE)
                .setOngoing(true);

        // Build & Return Notification
        return notificationBuilder.build();
    }
}
