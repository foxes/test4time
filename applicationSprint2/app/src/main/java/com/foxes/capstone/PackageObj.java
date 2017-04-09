package com.foxes.capstone;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by alanj_000 on 3/18/2017.
 */


public class PackageObj implements Comparable<PackageObj>{



    //String code = null;
    private String name = null;
    private boolean isNotBlocked = false;
    private Drawable icon = null;
    private String packageName = null;
    private ApplicationInfo info = null;
    private boolean isSystemApp = false;



    public PackageObj(ApplicationInfo info, String name, Drawable icon,
                      String packageName, boolean isNotBlocked, boolean isSystemApp) {
        super();

        this.info = info;
        this.name = name;
        this.isNotBlocked = isNotBlocked;
        this.icon = icon;
        this.packageName = packageName;
        this.isSystemApp = isSystemApp;

    }



    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public boolean getIsNotBlocked() {
        return isNotBlocked;
    }
    public void setIsnotBlocked(boolean selected) {
        this.isNotBlocked = selected;
    }

    public Drawable getIcon() {
        return icon;
    }
    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }
    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public ApplicationInfo getInfo() {
        return info;
    }

    public void setInfo(ApplicationInfo info) {
        this.info = info;
    }

    public boolean getIsSystemApp() {
        return isSystemApp;
    }

    public void setisSystemApp(boolean systemApp) {
        isSystemApp = systemApp;
    }

    //public static final Comparator<PackageObj> packageObjComparator = new Comparator<PackageObj>()
    //{
        @Override
    public int compareTo(PackageObj b) {
        int result = Boolean.compare(this.isSystemApp , b.isSystemApp);
        return result == 0 ? this.name.toLowerCase().compareTo(b.name.toLowerCase()) : result;
        }
//    };
    //  return (boolean) ? (int) : (int);

/*
    public int compareTo (PackageObj a, PackageObj b){
        //int dateComparison = a.date.compareTo(b.date);
        return 1;
        //return a.getPackageName== 0 ? a.value.compareTo(b.value) : dateComparison;
    }
    */
}