package com.example.android.myspendingtracker;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

/**
 * Created by Lydia on 19-Mar-18.
 */

public class NotificationHelper extends ContextWrapper {

    public static final String Channel1_ID = "Channel1_ID";
    public static final String Channel1_Name = "Channel1";
    private static int REQUEST_CODE = 1;
    private NotificationManager mManager;

    public NotificationHelper(Context base) {
        super(base);
        REQUEST_CODE++;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    public void createChannel(){
        NotificationChannel channel1 = new NotificationChannel(Channel1_ID, Channel1_Name, NotificationManager.IMPORTANCE_HIGH);
        channel1.enableLights(true);
        channel1.enableVibration(true);
        channel1.setLightColor(R.color.colorPrimary);
        channel1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
        //channel1.setSound();   //find out how to set sound

        getManager().createNotificationChannel(channel1);
    }

    public NotificationManager getManager(){
       if(mManager == null){
           mManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
       }
        return mManager;
    }

    public NotificationCompat.Builder getChannel1Notification(String message){

        Intent confirmIntent = new Intent(this, AlertReceiver.class);
        confirmIntent.setAction("LATER");
        confirmIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pConfirmIntent = PendingIntent.getBroadcast(this, REQUEST_CODE, confirmIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Intent openAppIntent = new Intent(this, MainActivity.class); //can create an activity here that simply says "Take med"
        openAppIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(getApplicationContext(), Channel1_ID)
                .setContentTitle("Enter data")
                .setContentText(message)
                .setContentIntent(PendingIntent.getActivity(this, REQUEST_CODE, openAppIntent, PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.drawable.ic_launcher_background) //setLargeIcon as a picture of a pill
                .addAction(new NotificationCompat.Action(R.drawable.ic_launcher_background, "Remind me later", pConfirmIntent))
                .setCategory(NotificationCompat.CATEGORY_REMINDER)
                .setAutoCancel(true)
                .setTimeoutAfter(60000);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.O){
            notificationBuilder.setPriority(NotificationCompat.PRIORITY_HIGH);
        }

        return notificationBuilder;
    }



}
