package com.example.sleeptracker;

import android.content.Context;

import java.util.Date;
import java.util.Random;

public class AddingFakeData {
    public static void addDataIntoDatabase(Context context) {
        SleepStatsDatabase sleepStatsDatabase = SleepStatsDatabase.getInstance(context);
        Random myRandom = new Random();
        for(int i = 1; i <= 9; i++) {
            Date fakeSleepTime = new Date(2020, 12, i, myRandom.nextInt(4) + 20, myRandom.nextInt(60), myRandom.nextInt(60));
            Date fakeWakeUpTime = new Date(2020, 12, i + 1, myRandom.nextInt(6) + 5, myRandom.nextInt(60), myRandom.nextInt(60));
            int rating = myRandom.nextInt(11);
            sleepStatsDatabase.insert(fakeSleepTime.getTime(), fakeWakeUpTime.getTime(), rating);
        }
    }
}
