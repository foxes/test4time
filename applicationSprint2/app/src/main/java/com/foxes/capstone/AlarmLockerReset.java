package com.foxes.capstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import static android.content.Context.MODE_MULTI_PROCESS;

/**
 * Created by alanj_000 on 4/15/2017.
 */



public class AlarmLockerReset extends BroadcastReceiver
{
    @Override
    public void onReceive(Context context, Intent intent)
    {
        try
        {
            Bundle bundle = intent.getExtras();
            //String message = bundle.getString("alarm_message");
           // Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
            SharedPreferences.Editor editor;
            SharedPreferences preferences;
            preferences = context.getSharedPreferences("myPrefs", MODE_MULTI_PROCESS); //.getDefaultSharedPreferences(this);
            editor = preferences.edit();
            editor.putBoolean("isRunningLockingService", true);
            editor.putBoolean("isTemporarilyUnlocked", false);
            editor.putBoolean("needToStopLockingService", false);
            editor.putBoolean("needToUpdateWhiteList", false);
            editor.commit();

            context.startService(new Intent(context, LockingService.class));
        }
        catch (Exception e)
        {
            Toast.makeText(context, "There was an error somewhere, but we still received an alarm", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }
}