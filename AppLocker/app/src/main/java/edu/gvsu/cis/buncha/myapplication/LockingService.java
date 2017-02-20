package edu.gvsu.cis.buncha.myapplication;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.AppOpsManager;
import android.app.IntentService;
import android.app.PendingIntent;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.SystemClock;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * Created by alanj_000 on 2/19/2017.
 * Some Elements of the code may be borrowed or inspired from: https://github.com/xcarsons/TEST4TIME
 * and from: http://stackoverflow.com/questions/19852069/blocking-android-apps-programatically/19852713#19852713
 */

/*The background service the handle blocking apps.
* This service extends IntentService, this may change to
* extending Service later since it may better to use that to spawn
* new tasks instead of infinite looping and sleeping this one
*/
public class LockingService extends IntentService {
    private static final String TAG = "com.test4time.test4time";
    private List<String> blockApps;

    public LockingService() {
        super("LockingService");
    }

    // USED IN API 21
    // Check if service has access to usage Data
    //
    private static boolean needPermissionForBlocking(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) context.getSystemService(Context.APP_OPS_SERVICE);
            int mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS, applicationInfo.uid, applicationInfo.packageName);
            return (mode != AppOpsManager.MODE_ALLOWED);
        } catch (PackageManager.NameNotFoundException e) {
            return true;
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        populateBlockApps();
        Log.d("LockingService", "service launched");
        return START_STICKY;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        // USES API 21
        // open settings to let user grant access to usage data

        if (needPermissionForBlocking(this)) {
            Intent settings = new Intent("android.settings.USAGE_ACCESS_SETTINGS");//Settings.ACTION_USAGE_ACCESS_SETTINGS);
            settings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(settings);
        }

        Log.d("LockingService", "service handling intent");
        /*An infinite loop to keep the service running. It sleeps for 2 seconds after running
         * in order to lower battery usage etc.
         */
        while (true) {
            if (blockApps.contains(getForegroundApp())) {
                Intent mainIntent;
                mainIntent = new Intent(getApplicationContext(), MainActivity.class);
                 mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                startActivity(mainIntent);
            }else{
                Log.d("LockingService", "App doesn't match blocked list");
            }
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onTaskRemoved(Intent rootIntent) {
        Log.d("LockingService", "service removed");
        Intent restartServiceIntent = new Intent(getApplicationContext(), this.getClass());
        restartServiceIntent.setPackage(getPackageName());

        PendingIntent restartServicePendingIntent = PendingIntent.getService(getApplicationContext(), 1, restartServiceIntent, PendingIntent.FLAG_ONE_SHOT);
        AlarmManager alarmService = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmService.set(
                AlarmManager.ELAPSED_REALTIME,
                SystemClock.elapsedRealtime() + 1000,
                restartServicePendingIntent);
        super.onTaskRemoved(rootIntent);
    }

    /*packageManager finds all the packages, all non packages except this app and
     *   system apps are added to the blocked list.
     *   This may be changed to a whitelist instead of a block list in the future.
     */
    private void populateBlockApps() {
        blockApps = new ArrayList <String>();
        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            // skip system apps if they shall not be included
            if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                continue;
            }
            Log.d("LockingService", "PackageName: " + p.packageName);
            if (!MainActivity.class.getPackage().getName().equals(p.packageName)) {
                blockApps.add(p.packageName);
            }
        }
        //Log.d("LockingService", "BlockedApps: " + blockApps.toString());
    }

    /*Finds the app that is currently running*/
    public String getForegroundApp() {
        String currentApp = "NULL";
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getApplication().getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    currentApp = mySortedMap.get(mySortedMap.lastKey()).getPackageName();
                }
            }
        } else {
            ActivityManager am = (ActivityManager) this.getApplication().getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            currentApp = tasks.get(0).processName;
        }

        return currentApp;
    }
}
