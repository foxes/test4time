package com.foxes.capstone;

import io.realm.RealmObject;


public class Application extends RealmObject {

    private String name, packageName, processName;

    public Application(){
        name = "";
        packageName = "";
        processName = "";
    }

    public Application(String name, String packageName, String processName) {
        this.name = name;
        this.packageName = packageName;
        this.processName = processName;
    }

    public String getName() {
        return name;
    }

    public String getPackageName() {
        return packageName;
    }

    public String getProcessName() {
        return processName;
    }
}
