package com.example.sleeptracker;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class SleepStats {
    private long sleepTime;
    private long wakeTime;
    private int rating;
    private double hoursSlept;

    /*
    private double averageHoursSlept;
    private double averageRating;
    public averageHours() {

    }
    public averageRating() {

    }
    */

    public SleepStats(long sleepTime, long wakeTime, int rating) {
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.rating = rating;
        this.hoursSlept = (double)((wakeTime - sleepTime) / 3600000);
    }

    public long getSleepTime() {
        return this.sleepTime;
    }

    public long getWakeTime() {
        return this.wakeTime;
    }

    public int getRating() {
        return this.rating;
    }

    public double getHoursSlept() {
        return this.hoursSlept;
    }

    public static String getDate(long datetimeInMilliseconds) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss.SSS");
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(datetimeInMilliseconds);
        return formatter.format(calendar.getTime());
    }
}
