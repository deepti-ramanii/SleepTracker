package com.example.sleeptracker;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class Recommendations extends AppCompatActivity {
    public static String AVG_HOURS = "avgHours";
    private static final int MAXHOURS = 9;
    private static final int MINHOURS = 7;

    private TextView hourText;
    private int avgHoursSleep;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_recommendations);
        avgHoursSleep = 0;
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            avgHoursSleep = extras.getInt(AVG_HOURS);
        }

        hourText = this.findViewById(R.id.hourText);
        getHourRec();
    }

    private void getHourRec() {
        if (avgHoursSleep < MINHOURS) {
            hourText.setText(String.format("You should sleep %s more hours each night", (MINHOURS - avgHoursSleep)));
        }
        else if (avgHoursSleep > MAXHOURS) {
            hourText.setText(String.format("You should sleep %s less hours each night", (avgHoursSleep - MAXHOURS)));
        }
        else {
            hourText.setText("You sleep the recommended amount each night");
        }
    }

    public void goToDisplayStats(View view) {
        Intent activitySwitchIntent = new Intent(Recommendations.this, DisplayCustomStats.class);
        startActivity(activitySwitchIntent);
    }

    public void goToGetSleepInfo(View view) {
        Intent activitySwitchIntent = new Intent(Recommendations.this, GetSleepInfo.class);
        startActivity(activitySwitchIntent);
    }
}
