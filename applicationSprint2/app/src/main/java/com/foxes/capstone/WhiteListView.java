package com.foxes.capstone;


import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by alanj_000 on 3/18/2017.
 */


public class WhiteListView extends Activity {

    final Context context = this;
    //private static ArrayList<ApplicationInfo> appsInfo;
    private static final String WHITE_LIST = "whiteList.txt"; //file name
    MyCustomAdapter dataAdapter = null;
    private ArrayList<String> whiteList;
    private ArrayList<PackageObj> packageList;
    private File appPath;
    private ServiceStatuses serviceStatuses;
    //private boolean alertResult;
    PackageObj packageTemp;
    CheckBox cb;
    private ArrayList<PackageObj> notListed;


    private PackageManager mPackageManager = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list_list_view);
        //alertResult = false;

        whiteList = new ArrayList<String>();
        notListed = new ArrayList<PackageObj>();
        packageList = new ArrayList<PackageObj>();
        appPath = getApplicationContext().getFilesDir();
        serviceStatuses = new ServiceStatuses();
        //Generate list View from ArrayList
        displayListView();

    }
    @Override
    public void onBackPressed() {
        saveWhiteList();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            saveWhiteList();
            Intent intent = new Intent(this, MainActivity.class);
            this.startActivity(intent);
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /*
        public void onListItemClick(ListView l, View v, int position, long id) {
            //super.onListItemClick(l, v, position, id);
            //Get related checkbox and change flag status..
            CheckBox cb = (CheckBox)v.findViewById(R.id.checkBox1);
            cb.setChecked(!cb.isChecked());
            //Toast.makeText(getActivity(), "Click item", Toast.LENGTH_SHORT).show();
        }
        public void updateCheckBoxStatus(View v){
            Integer taggedPosition = (Integer) v.getTag();
            Log.d("qwe", Integer.toString(taggedPosition));
            int index = taggedPosition.intValue();
            dataAdapter.getItem(index).setIsnotBlocked(((CheckBox)v).isSelected());
        }
        */
    public void returnToMainActivity(View v) {
        saveWhiteList();
        Intent intent = new Intent(this, MainActivity.class);
        this.startActivity(intent);
    }

    private void saveWhiteList() {

        ArrayList<PackageObj> packageListTemp = dataAdapter.packageList;
        whiteList.clear();
        for (int i = 0; i < packageListTemp.size(); i++) {
            PackageObj packageTemp = packageListTemp.get(i);
            Log.d("qwe", "packageTemp Name:  " + packageTemp.getName() + "isNotBLocked:  " + packageTemp.getIsNotBlocked());
            if (packageTemp.getIsNotBlocked()) {
                whiteList.add(packageTemp.getPackageName());
            }
        }

        for (int i = 0; i < notListed.size(); i++) {
            PackageObj packageTemp = notListed.get(i);
            Log.d("qwe", "packageTemp Name:  " + packageTemp.getName() + "isNotBLocked:  " + packageTemp.getIsNotBlocked());
            if (packageTemp.getIsNotBlocked()) {
                whiteList.add(packageTemp.getPackageName());
            }
        }
        Log.d("qwe", "whiteList:  " + whiteList.toString());

        try {
            File whiteListFile = new File(appPath + "/" + WHITE_LIST);

            FileOutputStream fos = new FileOutputStream(whiteListFile, false);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(whiteList);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*need to have LockingService update its whitelist*/
        if (isMyServiceRunning(LockingService.class)) {
            //Intent intent = new Intent(this, LockingService.class);
            //stopService(intent);
            serviceStatuses.needToUpdateWhiteList.set(true);
        }
    }

    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }
        return false;
    }

    private void loadAppsInfo() {


        PackageManager packageManager = getPackageManager();
        Intent mainIntent = new Intent(Intent.ACTION_MAIN, null);
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER);

        List<ResolveInfo> appList = packageManager.queryIntentActivities(mainIntent, 0);
        Collections.sort(appList, new ResolveInfo.DisplayNameComparator(packageManager));
        //List<PackageInfo> packs = packageManager.getInstalledPackages(0);
        List<PackageInfo> packs = packageManager.getInstalledPackages(0);

        // mApplications = checkForLaunchIntent(mPackageManager.getInstalledApplications(PackageManager.GET_META_DATA));

        File f = new File(appPath + "/" + WHITE_LIST);
        boolean creatingFile = !f.exists();
        if (f.exists() && !f.isDirectory()) {
            try {
                //Log.d("qwe", "before read whiteList:  " + whiteList.toString());

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f)); //new FileInputStream(String<>) /*ByteArrayInputStream(baos.toByteArray())*/);
                whiteList = (ArrayList<String>) ois.readObject();
                ois.close();
                //Toast toast = Toast.makeText(this, whiteList.toString(), Toast.LENGTH_SHORT);
                //toast.show();
                //Log.d("qwe", "Reading whiteList:  " + whiteList.toString());

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        for (int i = 0; i < packs.size(); i++) {
            PackageInfo p = packs.get(i);
            ApplicationInfo a = p.applicationInfo;
            boolean isSystemApp = false;
            // skip system apps if they shall not be included


            if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                isSystemApp = true;
                //continue;
            }


            Log.d("LockingService", "PackageName: " + p.packageName);
            try {
                ApplicationInfo app = this.getPackageManager().getApplicationInfo(p.packageName, 0);
                String appLabel = "" + packageManager.getApplicationLabel(app);
                String appLabelLower = appLabel.toLowerCase();
                boolean hasKeyWords = false;

                //This has a bare bones list, which can improved later.
                //This could be reworked to only include certain system apps in the list
                //           instead of only excluding certain system apps.
                if(isSystemApp){
                    if(appLabelLower.contains("android")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("contacts")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("calender")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("mobile")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("phone")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("system")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("google") && !appLabelLower.contains("play")){
                        hasKeyWords = true;
                    }
                    if(appLabelLower.contains("clock")){
                        hasKeyWords = true;
                    }
                    //add more later
                    //if(appLabelLower.contains("")){
                    //    hasKeyWords = true;
                    //}
                }
                if (!MainActivity.class.getPackage().getName().equals(p.packageName)
                        && !hasKeyWords  && !appLabelLower.contains("fitbit")) {


                    boolean isNotBlocked = false;
                    if (whiteList.contains(p.packageName) || (isSystemApp && creatingFile)) {
                        isNotBlocked = true;
                    }
                    /*
                    if ((a.flags & ApplicationInfo.FLAG_SYSTEM) == 1) {
                         continue;
                    }
                    */
                    //ApplicationInfo app = this.getPackageManager().getApplicationInfo(p.packageName, 0);
                    packageList.add(new PackageObj(app,
                            appLabel,
                            packageManager.getApplicationIcon(app),
                            p.packageName,
                            isNotBlocked, isSystemApp));


                }
                else{
                    notListed.add(new PackageObj(app,
                            appLabel,
                            packageManager.getApplicationIcon(app),
                            p.packageName,
                            true, isSystemApp));
                }
            } catch (PackageManager.NameNotFoundException e) {

            }
        }
        Collections.sort(packageList);



    }

    private void displayListView() {
        loadAppsInfo();
        //create an ArrayAdaptar from the String Array
        dataAdapter = new MyCustomAdapter(this,
                R.layout.white_list_item, packageList);
        ListView listView = (ListView) findViewById(R.id.listView1);
        // Assign adapter to ListView
        listView.setAdapter(dataAdapter);


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // When clicked, show a toast with the TextView text
                packageTemp = (PackageObj) parent.getItemAtPosition(position);
                cb = (CheckBox) view.findViewById(R.id.checkBox1);
                String title, message, positive, negative;
                if (packageTemp.getIsSystemApp()) {
                    if (cb.isChecked()) {
                        title = "Warning";
                        message = "This is a system app, disabling it may disrupt normal use of the device.";
                        positive = "Disable";
                        negative = "Keep Enabled";
                    } else {
                        title = "Warning";
                        message = "This is a system app, we recomend you enable it unless you are certain you want it blocked.";
                        positive = "Enable";
                        negative = "Keep Disabled";
                    }

                    AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                            context);
                    // set title
                    alertDialogBuilder.setTitle(title);
                    // set dialog message

                    alertDialogBuilder
                            .setMessage(message)
                            //.setCancelable(false)
                            .setPositiveButton(positive, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    cb.setChecked(!cb.isChecked());
                                    packageTemp.setIsnotBlocked(!packageTemp.getIsNotBlocked());
                                }
                            })
                            .setNegativeButton(negative, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    // if this button is clicked, just close
                                    // the dialog box and do nothing

                                    dialog.cancel();
                                }
                            });


                    // create alert dialog
                    AlertDialog alertDialog = alertDialogBuilder.create();

                    // show it
                    alertDialog.show();

                    Button positiveButton = (Button) alertDialog.findViewById(android.R.id.button1);
                    Button negativeButton = (Button) alertDialog.findViewById(android.R.id.button2);
                    // then get their parent ViewGroup
                    ViewGroup buttonPanelContainer = (ViewGroup) positiveButton.getParent();
                    int positiveButtonIndex = buttonPanelContainer.indexOfChild(positiveButton);
                    int negativeButtonIndex = buttonPanelContainer.indexOfChild(negativeButton);
                    if (positiveButtonIndex > negativeButtonIndex) {
                        // prepare exchange their index in ViewGroup
                        buttonPanelContainer.removeView(negativeButton);
                        buttonPanelContainer.removeView(positiveButton);
                        buttonPanelContainer.addView(positiveButton, negativeButtonIndex);
                        buttonPanelContainer.addView(negativeButton, positiveButtonIndex);
                    }

                } else {
                    cb.setChecked(!cb.isChecked());
                    packageTemp.setIsnotBlocked(!packageTemp.getIsNotBlocked());

                }
                //if(alertResult){
                //cb.setChecked(!cb.isChecked());
                //packageTemp.setIsnotBlocked(!packageTemp.getIsNotBlocked());

                //Toast.makeText(getApplicationContext(),
                //        "Clicked on Row: " + packageTemp.getName() + "bool: " + packageTemp.getIsNotBlocked(),
                //        Toast.LENGTH_LONG).show();
            }
        });
    }


    private class MyCustomAdapter extends ArrayAdapter<PackageObj> {

        private ArrayList<PackageObj> packageList;

        public MyCustomAdapter(Context context, int textViewResourceId,
                               ArrayList<PackageObj> packageList) {
            super(context, textViewResourceId, packageList);
            this.packageList = new ArrayList<PackageObj>();
            this.packageList.addAll(packageList);


        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHolder holder = null;
            Log.v("ConvertView", String.valueOf(position));

            if (convertView == null) {
                LayoutInflater vi = (LayoutInflater) getSystemService(
                        Context.LAYOUT_INFLATER_SERVICE);
                convertView = vi.inflate(R.layout.white_list_item, parent, false);

                holder = new ViewHolder();
                holder.checkBox = (CheckBox) convertView.findViewById(R.id.checkBox1);
                holder.icon = (ImageView) convertView.findViewById(R.id.icon1);

                holder.name = (TextView) convertView.findViewById(R.id.name1);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            PackageObj packageTemp = packageList.get(position);
            holder.checkBox.setChecked(packageTemp.getIsNotBlocked());
            holder.icon.setImageDrawable(packageTemp.getIcon());
            holder.name.setText(packageTemp.getName());


            return convertView;

        }

        private class ViewHolder {
            CheckBox checkBox;
            ImageView icon;
            TextView name;

        }

    }

}