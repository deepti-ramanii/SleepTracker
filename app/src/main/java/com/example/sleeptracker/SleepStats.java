package com.example.sleeptracker;

import java.util.Date;

public class SleepStats {
    private static final int MILLISECONDS_PER_HOUR = 3600000;
    private long sleepTime;
    private long wakeTime;
    private int rating;
    private double hoursSlept;

    public SleepStats(long sleepTime, long wakeTime, int rating) {
        this.sleepTime = sleepTime;
        this.wakeTime = wakeTime;
        this.rating = rating;
        this.hoursSlept = (double)((wakeTime - sleepTime) / MILLISECONDS_PER_HOUR);
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

    public static Date getDateFromLong(long dateInMilliseconds) {
        return new Date(dateInMilliseconds);
    }

    public static long getBeginningOfDay(int day, int month, int year) {
        Date date = new Date(year - 1900, month - 1, day, 0, 0, 0);
        return date.getTime();
    }

    public static long getEndOfDay(int day, int month, int year) {
        Date date = new Date(year - 1900, month - 1, day, 23, 59, 59);
        return date.getTime();
    }
}
