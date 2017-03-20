package com.foxes.capstone;

/**
 * Created by Foxes on 3/17/2017.
 */

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.NotificationCompat;

public class AlarmNotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context);

        // Mainctivity.lockDisableTime = 14;

        builder.setAutoCancel(true)
                .setDefaults(Notification.DEFAULT_ALL)
                .setWhen(System.currentTimeMillis())
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Test4Time")
                .setContentText(MainActivity.lockDisableTime + " minute(s) remaining!")
                .setDefaults(Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE)
                .setContentInfo("hi :)");
        builder.setPriority(android.support.v4.app.NotificationCompat.PRIORITY_HIGH);



        if (MainActivity.lockDisableTime == 0){

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());

            //this line caused an error:
            // Caused by: java.lang.NullPointerException: Attempt to invoke virtual method 'void android.widget.TextView.setText(java.lang.CharSequence)'
            //likely from something being purged from memory by the OS
            MainActivity.updateUI();

            StopAlarm(context);

            // MainActivity.lockDisableTime = 2;
        }



        if (MainActivity.lockDisableTime > 0) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, builder.build());

            MainActivity.lockDisableTime = MainActivity.lockDisableTime - 1;
        }



    }

    public void StopAlarm(Context c){

        AlarmManager manager = (AlarmManager)c.getSystemService(Context.ALARM_SERVICE);
        Intent myIntent;
        PendingIntent pendingIntent;
        myIntent = new Intent(c.getApplicationContext(),AlarmNotificationReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(c.getApplicationContext(),1,myIntent,PendingIntent.FLAG_UPDATE_CURRENT);


        manager.cancel(pendingIntent);
    }
}