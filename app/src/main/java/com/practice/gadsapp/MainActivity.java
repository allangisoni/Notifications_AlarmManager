package com.practice.gadsapp;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

import static android.app.Notification.EXTRA_NOTIFICATION_ID;

public class MainActivity extends AppCompatActivity {

    Button notifymebtn;
    Button updatemebtn;
    Button nextalarmbtn;
    ToggleButton toggleButton;
    private static final String NOTIFICATION_CHANNEL_ID = "tweetme";
    CharSequence notificationChannelname= "tweetschannel";
    int channelImportance = NotificationManager.IMPORTANCE_MAX;
    NotificationManager notificationManager ;

    private MyBroadcastDeleteReceiver mReceiver = new MyBroadcastDeleteReceiver();
    private static final String ACTION_DELETE_NOTIFICATION = "com.practice.gadsapp.ACTION_DELETE_NOTIFICATION";


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intializeViews();
        createNotificationChannel();

        registerReceiver(mReceiver, new IntentFilter(ACTION_DELETE_NOTIFICATION));
        setNotificationButtonState(true, false);

        Intent notifyIntent = new Intent(this, AlarmReceiver.class);
        final PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(this, 1, notifyIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        boolean alarmUp = (PendingIntent.getBroadcast(this, 1,
                notifyIntent, PendingIntent.FLAG_NO_CREATE) != null);
        toggleButton.setChecked(alarmUp);

        final AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notifymebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });
        updatemebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             updateNotification();
            }
        });
        toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                String toastMessage = "";
                if(isChecked){
                    toastMessage = "Stand Up alarm is on";
                    long repeatInterval = 6000L;
                    long triggerTime = SystemClock.elapsedRealtime()
                            + repeatInterval;

                    if(alarmManager != null){
                        alarmManager.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, triggerTime, repeatInterval, alarmPendingIntent);
                    }


                    //sendNotification();
                } else{
                    toastMessage = "Stand Up alarm is off";
                    if(alarmManager != null){
                        alarmManager.cancel(alarmPendingIntent);
                    }
                    notificationManager.cancel(1);
                }

                Toast.makeText(MainActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
            }
        });

        nextalarmbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Long nextAlarm = alarmManager.getNextAlarmClock().getTriggerTime();
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                cal.setTimeInMillis(nextAlarm);
                String date = DateFormat.format("dd-MM-yyyy hh:mm:ss", cal).toString();
                Toast.makeText(MainActivity.this, date, Toast.LENGTH_LONG ).show();
            }
        });



    }

    private void intializeViews(){
        notifymebtn = findViewById(R.id.btn_notifyme);
        updatemebtn = findViewById(R.id.btn_updateme);
        nextalarmbtn = findViewById(R.id.btn_nextAlarm);
        toggleButton = findViewById(R.id.alarmToggle);
    }

    public void createNotificationChannel(){
       notificationManager =  (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            @SuppressLint("WrongConstant") NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL_ID, notificationChannelname, channelImportance);
            notificationChannel.setDescription("Channel Description");
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.BLUE);
            notificationChannel.setVibrationPattern(new long[]{0, 1000, 500, 1000});
            notificationChannel.enableVibration(true);
            notificationManager.createNotificationChannel(notificationChannel);
        }


    }


    public void updateNotification(){
        Bitmap androidImage = BitmapFactory.decodeResource(getResources(), R.drawable.mascot_1);
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
       // notifyBuilder.setStyle(new NotificationCompat.BigPictureStyle().bigPicture(androidImage).setBigContentTitle("Notificationupdated"));
        CharSequence sequence ="Drum beartssss";
        CharSequence sequence1 ="Drum beartssss it is";
        notifyBuilder.setStyle(new NotificationCompat.InboxStyle().addLine(sequence).addLine(sequence1));
        notificationManager.notify(1, notifyBuilder.build());
        setNotificationButtonState(true, false);
    }

  public  NotificationCompat.Builder getNotificationBuilder(){
      Intent intent = new Intent(this, MainActivity.class);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
      PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, 0);

      Intent snoozeIntent = new Intent(this, MyBroadcastReceiver.class);
      //snoozeIntent.setAction(ACT);
      snoozeIntent.putExtra(EXTRA_NOTIFICATION_ID, 0);
      PendingIntent pendingSnoozeIntent = PendingIntent.getBroadcast(this, 1, snoozeIntent, PendingIntent.FLAG_ONE_SHOT);





      NotificationCompat.Builder builder = new  NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID);
      builder.setAutoCancel(true)
              .setDefaults(Notification.DEFAULT_ALL)
              .setWhen(System.currentTimeMillis())
              .setSmallIcon(R.mipmap.ic_launcher)
              .setTicker("GADS")
              .setContentTitle("NOTIFY ME")
              .setContentText("You have a new action waiting")
              .setStyle(new NotificationCompat.BigTextStyle().bigText("Much longer text that cannot fit one line text that cannot fit one line..."))
              .setContentInfo("Action")
              .setContentIntent(pendingIntent)
              .addAction(R.drawable.ic_launcher_background,"SNOOZE",pendingSnoozeIntent);


      Handler handler = new Handler();
      long waittime = 5000;
      handler.postDelayed(new Runnable() {
          @Override
          public void run() {
              notificationManager.cancel(2);
          }
      }, waittime);

      return builder;
    }

    public void sendNotification(){
        NotificationCompat.Builder notifyBuilder = getNotificationBuilder();
        Intent deleteIntent = new Intent(ACTION_DELETE_NOTIFICATION);
        PendingIntent pendingDeleteIntent = PendingIntent.getBroadcast(this, 1, deleteIntent, PendingIntent.FLAG_ONE_SHOT);
        notifyBuilder.setDeleteIntent(pendingDeleteIntent);
        notificationManager.notify(1, notifyBuilder.build());
        setNotificationButtonState(false, true);
    }


    public void setNotificationButtonState(Boolean isNotifyEnabled,
                                           Boolean isUpdateEnabled){
        notifymebtn.setEnabled(isNotifyEnabled);
        updatemebtn.setEnabled(isUpdateEnabled);

    }





    @Override
    protected void onDestroy() {
        unregisterReceiver(mReceiver);
        super.onDestroy();


    }

    public  class MyBroadcastDeleteReceiver extends BroadcastReceiver{
        public  MyBroadcastDeleteReceiver(){

        }

        @Override
        public void onReceive(Context context, Intent intent) {
             setNotificationButtonState(true, false);
        }
    }


}