package com.example.sleeptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Calendar;

public class GetUserInput extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;
    private Button sleepWakeButton;
    private boolean hasStoredSleepTime;
    private long storedSleepTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_input);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);
        sleepWakeButton = this.findViewById(R.id.sleep_wake_button);
        hasStoredSleepTime = false;
        storedSleepTime = 0;
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("LogTimeButtonText", sleepWakeButton.getText().toString());
        savedInstanceState.putBoolean("HasStoredSleepTime", hasStoredSleepTime);
        savedInstanceState.putLong("StoredSleepTime", storedSleepTime);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        sleepWakeButton.setText(savedInstanceState.getString("LogTimeButtonText"));
        hasStoredSleepTime = savedInstanceState.getBoolean("HasStoredSleepTime");
        storedSleepTime = savedInstanceState.getLong("StoredSleepTime");
    }

    public void setTimes(View view) {
        long currTime = Calendar.getInstance().getTimeInMillis();
        if(hasStoredSleepTime) {
            //if we already have a stored sleep time, log a wake up time
            sleepStatsDatabase.insert(storedSleepTime, currTime, 0);
            sleepWakeButton.setText("Sleep");
        } else {
            //otherwise log a sleep time
            storedSleepTime = currTime;
            sleepWakeButton.setText("Wake Up");
        }
        hasStoredSleepTime = !hasStoredSleepTime;
    }
}