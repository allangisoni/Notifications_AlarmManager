package com.practice.gadsapp;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;

;import androidx.core.app.NotificationCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    private static final String NOTIFICATION_CHANNEL_ID = "tweetme";
    CharSequence notificationChannelname= "tweetschannel";
    int channelImportance = NotificationManager.IMPORTANCE_MAX;

    @Override
    public void onReceive(Context context, Intent intent) {




      //  NotificationChannel notificationChannel = mainActivity.createNotificationChannel();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
           @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, notificationChannelname, channelImportance);
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);

        //notificationManager.createNotificationChannel(mainActivity.createNotificationChannel());
        }


       NotificationCompat.Builder builder = new  NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID);
        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.drawable.ic_notify)
                .setTicker("GADS")
                .setContentTitle("SNOOZE ME")
                .setContentText("You have a new action waiting")
                .setStyle(new NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line text that cannot fit one line..."))
                .setContentInfo("Action");

      notificationManager.notify(1, builder.build());
    }
}
