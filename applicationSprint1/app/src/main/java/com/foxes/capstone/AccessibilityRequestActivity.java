package com.foxes.capstone;

import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

/**
 * Foxes 3/19/17
 */

public class AccessibilityRequestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.fragment_accessibility_required);




        Button give_permission_button = (Button)this.findViewById(R.id.accessibility_permission_button);
        give_permission_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openPermissionSettings();
            }
        });

    }



    public void openPermissionSettings(){
        Intent settings = new Intent("android.settings.USAGE_ACCESS_SETTINGS");//Settings.ACTION_USAGE_ACCESS_SETTINGS);
        //settings.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        //startActivity(settings);

        startActivityForResult( settings /*new Intent(android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS)*/, 101);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        Log.d("ARA", "intent result: "+ requestCode + " " + resultCode);
        if (!needPermissionForBlocking(this)) {
            Intent mainActivity = new Intent(this, MainActivity.class);
            startActivity(mainActivity);
        }
        /*
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
                // The user picked a contact.
                // The Intent's data Uri identifies which contact was selected.

                // Do something with the contact here (bigger example below)
                Intent mainActivity = new Intent(this, MainActivity.class);
                startActivity(mainActivity);
            }
        }
        */
    }
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

}
