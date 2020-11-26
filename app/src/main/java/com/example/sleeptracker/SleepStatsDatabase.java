package com.example.sleeptracker;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class SleepStatsDatabase extends SQLiteOpenHelper {
    public static final String SLEEP_STATS_TABLE = "SLEEP_STATS";

    public static final String ID = "ID";
    //Store date/time as a single number (using Calendar)
    public static final String SLEEP_TIME = "SLEEP_TIME";
    public static final String WAKE_TIME = "WAKE_TIME";
    //integer rating of sleep quality from 0 (worst) to 5 (best)
    public static final String RATING = "RATING";

    private static final String SLEEP_STATS_DB_NAME = "SLEEP_STATS.DB";
    private static final int SLEEP_STATS_VERSION = 1;

    private static SleepStatsDatabase instance = null;
    private static Context context = null;

    //this is called the first time a database is accessed
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create = "CREATE TABLE " + SLEEP_STATS_TABLE + " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " + SLEEP_TIME + " INTEGER, " + WAKE_TIME + " INTEGER, " + RATING + " INTEGER)";
        db.execSQL(create);
    }

    //this is called if the database version number changes
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + SLEEP_STATS_TABLE);
        onCreate(db);
    }

    //makes SleepStatsDatabase a singleton class (only one shared instance across the app)
    public static synchronized SleepStatsDatabase getInstance(Context c) {
        if (instance == null) {
            instance = new SleepStatsDatabase(c.getApplicationContext());
        }
        return instance;
    }

    private SleepStatsDatabase(Context c) {
        super(c, SLEEP_STATS_DB_NAME, null, SLEEP_STATS_VERSION);
    }

    public void insert(long sleepTime, long wakeUpTime, int rating) {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues contentValue = new ContentValues();
        contentValue.put(SLEEP_TIME, sleepTime);
        contentValue.put(WAKE_TIME, wakeUpTime);
        contentValue.put(RATING, rating);
        database.insert(SLEEP_STATS_TABLE, null, contentValue);
        database.close();
    }

    public List<SleepStats> getBetweenDates(long start, long end) {
        List<SleepStats> sleepStats = new ArrayList<SleepStats>();
        String query = "SELECT * FROM " + SLEEP_STATS_TABLE + " WHERE " + SLEEP_TIME + " >= " + start + " AND " + SLEEP_TIME + " <= " + end;
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor = database.rawQuery(query, null);
        for(int i = 0; i < cursor.getCount(); i++) {
            cursor.moveToPosition(i);
            sleepStats.add(new SleepStats(cursor.getLong(1), cursor.getLong(2), cursor.getInt(3)));
        }
        return sleepStats;
    }
}
