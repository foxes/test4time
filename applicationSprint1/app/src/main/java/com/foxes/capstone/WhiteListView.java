package com.foxes.capstone;


import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
    //private static ArrayList<ApplicationInfo> appsInfo;
    private static final String WHITE_LIST = "whiteList.txt"; //file name
    MyCustomAdapter dataAdapter = null;
    private ArrayList<String> whiteList;
    private ArrayList<PackageObj> packageList;
    private File appPath;
    private ServiceStatuses serviceStatuses;


    private PackageManager mPackageManager = null;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.white_list_list_view);

        whiteList = new ArrayList<String>();
        packageList = new ArrayList<PackageObj>();
        appPath = getApplicationContext().getFilesDir();
        serviceStatuses = new ServiceStatuses();
        //Generate list View from ArrayList
        displayListView();

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
        Log.d("qwe", "whiteList:  " + whiteList.toString());

        try {
            File whiteListFile = new File( appPath + "/" + WHITE_LIST);

            FileOutputStream fos = new FileOutputStream(whiteListFile, false );
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(whiteList);
            oos.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /*need to have LockingService update its whitelist*/
        if(isMyServiceRunning(LockingService.class)){
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

        File f = new File(appPath+"/"+WHITE_LIST);
        if(f.exists() && !f.isDirectory()) {
            try {
                Log.d("qwe", "before read whiteList:  " + whiteList.toString());

                ObjectInputStream ois = new ObjectInputStream(new FileInputStream(f)); //new FileInputStream(String<>) /*ByteArrayInputStream(baos.toByteArray())*/);
                whiteList = (ArrayList<String>) ois.readObject();
                ois.close();
                //Toast toast = Toast.makeText(this, whiteList.toString(), Toast.LENGTH_SHORT);
                //toast.show();
                Log.d("qwe", "Reading whiteList:  " + whiteList.toString());

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
            // skip system apps if they shall not be included

            Log.d("LockingService", "PackageName: " + p.packageName);
            if (!MainActivity.class.getPackage().getName().equals(p.packageName)) {


                try {
                    boolean isNotBlocked = false;
                    if (whiteList.contains(p.packageName)) {
                        isNotBlocked = true;
                    }
                    ApplicationInfo app = this.getPackageManager().getApplicationInfo(p.packageName, 0);
                    packageList.add(new PackageObj(app,
                            "" + packageManager.getApplicationLabel(app),
                            packageManager.getApplicationIcon(app),
                            p.packageName,
                            isNotBlocked));


                } catch (PackageManager.NameNotFoundException e) {

                }
            }
        }
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
                PackageObj packageTemp = (PackageObj) parent.getItemAtPosition(position);
                CheckBox cb = (CheckBox) view.findViewById(R.id.checkBox1);
                cb.setChecked(!cb.isChecked());
                packageTemp.setIsnotBlocked(!packageTemp.getIsNotBlocked());
                Toast.makeText(getApplicationContext(),
                        "Clicked on Row: " + packageTemp.getName() + "bool: " + packageTemp.getIsNotBlocked(),
                        Toast.LENGTH_LONG).show();
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