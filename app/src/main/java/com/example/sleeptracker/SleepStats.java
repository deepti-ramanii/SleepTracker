package com.example.sleeptracker;

public class SleepStats {
    private long sleepTime;
    private long wakeTime;
    private int rating;
    private double hoursSlept;

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
}
