package com.foxes.capstone;

import android.util.Log;

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
}
