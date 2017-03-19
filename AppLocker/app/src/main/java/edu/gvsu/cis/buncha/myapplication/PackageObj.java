package edu.gvsu.cis.buncha.myapplication;

import android.content.pm.ApplicationInfo;
import android.graphics.drawable.Drawable;

/**
 * Created by alanj_000 on 3/18/2017.
 */


public class PackageObj{



    //String code = null;
    String name = null;
    boolean isNotBlocked = false;
    Drawable icon = null;
    String packageName = null;
    ApplicationInfo info = null;



    public PackageObj(ApplicationInfo info, String name, Drawable icon, String packageName, boolean isNotBlocked) {
        super();

        this.info = info;
        this.name = name;
        this.isNotBlocked = isNotBlocked;
        this.icon = icon;
        this.packageName = packageName;

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

}