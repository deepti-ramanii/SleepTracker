package com.example.sleeptracker;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.content.Intent;
import androidx.annotation.Nullable;

import android.view.View;
import android.widget.Button;

import java.util.Calendar;

public class GetSleepInfo extends AppCompatActivity implements RatingDialog.RatingDialogListener {
    private SleepStatsDatabase sleepStatsDatabase;
    private Button sleepWakeButton;
    private boolean hasStoredSleepTime;
    private long storedSleepTime;
    private long storedWakeTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_get_sleep_info);
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
            setRating();
            storedWakeTime = currTime;
            sleepWakeButton.setText("Sleep");
        } else {
            storedSleepTime = currTime;
            sleepWakeButton.setText("Wake Up");
        }
        hasStoredSleepTime = !hasStoredSleepTime;
    }

    private void setRating() {
        RatingDialog ratingDialog = new RatingDialog();
        ratingDialog.show(getSupportFragmentManager(), "submit rating");
    }

    @Override
    public void applyRating(int rating) {
        sleepStatsDatabase.insert(storedSleepTime, storedWakeTime, rating);
    }

    public void getSleepInfoToDisplayStats(View view) {
        Intent activitySwitchIntent = new Intent(GetSleepInfo.this, DisplayCustomStats.class);
        startActivity(activitySwitchIntent);
    }

    public void getSleepInfoToRecommendations(View view) {
        Intent activitySwitchIntent = new Intent(GetSleepInfo.this, Recommendations.class);
        startActivity(activitySwitchIntent);
    }
}