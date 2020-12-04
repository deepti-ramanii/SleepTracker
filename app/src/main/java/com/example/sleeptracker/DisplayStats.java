package com.example.sleeptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import br.com.sapereaude.maskedEditText.MaskedEditText; // https://www.geeksforgeeks.org/how-to-add-mask-to-an-edittext-in-android/ -> tutorial for creating a masked EditText

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

public class DisplayStats extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;
    private MaskedEditText startDate;
    private MaskedEditText endDate;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_stats);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);

        startDate = this.findViewById(R.id.start_date);
        endDate = this.findViewById(R.id.end_date);
    }

    public void getStats(View view) {
        int startDay = Integer.parseInt(startDate.getText().toString().substring(0, 2));
        int startMonth = Integer.parseInt(startDate.getText().toString().substring(2, 4));
        int startYear = Integer.parseInt(startDate.getText().toString().substring(4, 8));

        int endDay = Integer.parseInt(endDate.getText().toString().substring(0, 2));
        int endMonth = Integer.parseInt(endDate.getText().toString().substring(2, 4));
        int endYear = Integer.parseInt(endDate.getText().toString().substring(4, 8));

        List<SleepStats> sleepStats = sleepStatsDatabase.getBetweenDates(SleepStats.getBeginningOfDay(startDay, startMonth, startYear), SleepStats.getEndOfDay(endDay, endMonth, endYear));
        //TODO: do stuff with these stats
    }
}
