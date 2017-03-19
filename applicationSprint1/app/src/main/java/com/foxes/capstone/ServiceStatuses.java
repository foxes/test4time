package com.foxes.capstone;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Created by alanj_000 on 3/19/2017.
 */

public class ServiceStatuses {
    public static AtomicBoolean isRunningLockingService = new AtomicBoolean(false);
    public static AtomicBoolean needToUpdateWhiteList = new AtomicBoolean(false);
    public static AtomicBoolean needToStopLockingService = new AtomicBoolean(false);

    public ServiceStatuses() {
    }

    /*
    public ServiceStatuses(AtomicBoolean isRunningLockingService, AtomicBoolean needToUpdateWhiteList,
                           AtomicBoolean needToStopLockingService){
        this.isRunningLockingService = isRunningLockingService;
        this.needToUpdateWhiteList = needToUpdateWhiteList;
        this.needToStopLockingService = needToStopLockingService;
    }
*/
}
