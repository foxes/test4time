package com.foxes.capstone;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by alanj_000 on 4/15/2017.
 */



public class AutoStart extends BroadcastReceiver
{
    public void onReceive(Context context, Intent arg1)
    {
        Intent intent = new Intent(context,LockingService.class);
         context.startService(intent);
        Log.i("Autostart", "started");
    }
}
