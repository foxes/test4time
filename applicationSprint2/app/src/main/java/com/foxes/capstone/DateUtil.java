package com.foxes.capstone;

import java.util.Calendar;

/**
 * Created by fletcher on 4/6/17.
 */

public class DateUtil {

    Calendar cal = Calendar.getInstance();

    public int getYear() {

        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    public int getMonth(){

        int month = cal.get(Calendar.MONTH);
        return month;
    }

    public int getDay(){

        int day = cal.get(Calendar.DATE);
        return day;
    }

    public String getTimeStamp(){

        Long tmStamp = System.currentTimeMillis() / 1000;
        String ts = tmStamp.toString();
        System.out.println("tmStamp: " + ts);
        return ts;
    }
}

