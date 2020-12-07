package com.example.sleeptracker;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import br.com.sapereaude.maskedEditText.MaskedEditText; // https://www.geeksforgeeks.org/how-to-add-mask-to-an-edittext-in-android/ -> tutorial for creating a masked EditText

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DisplayStats extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;
    private MaskedEditText startDate;
    private MaskedEditText endDate;
    private double totalHoursSlept;
    private double totalRating;
    private double divide;
    private long totalWhenSleep = 0;
    private long totalWhenWake = 0;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_stats);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);

        startDate = this.findViewById(R.id.start_date);
        endDate = this.findViewById(R.id.end_date);
    }

    public void getStats(View view) {
        int startDay = Integer.parseInt(startDate.getText().toString().substring(0, 2));
        int startMonth = Integer.parseInt(startDate.getText().toString().substring(3, 5));
        int startYear = Integer.parseInt(startDate.getText().toString().substring(6, 10));

        int endDay = Integer.parseInt(endDate.getText().toString().substring(0, 2));
        int endMonth = Integer.parseInt(endDate.getText().toString().substring(3, 5));
        int endYear = Integer.parseInt(endDate.getText().toString().substring(6, 10));

        List<SleepStats> sleepStats = sleepStatsDatabase.getBetweenDates(SleepStats.getBeginningOfDay(startDay, startMonth, startYear), SleepStats.getEndOfDay(endDay, endMonth, endYear));
        //TODO: do stuff with these stats
        for(SleepStats stats : sleepStats) {
            totalHoursSlept += stats.getHoursSlept();
            totalRating += stats.getRating();
            totalWhenSleep = Long.sum(totalWhenSleep, stats.getSleepTime());
            totalWhenWake = Long.sum(totalWhenWake, stats.getWakeTime());
        }
        divide = sleepStats.size();
    }

    public String roundMethod(double round) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        return df.format(round);
    }

    public String averageHours() {
        return roundMethod(totalHoursSlept / divide);
    }

    public String averageRating() {
        return roundMethod(totalRating / divide);
    }

    public String averageSleepTime() {
        double result = ((totalWhenSleep / divide) / 3600000);
        if (result > 12) {
            result = result - 12;
            String finalString = roundMethod(result);
            return finalString + "PM";
        } else {
            return roundMethod(result) + "AM";
        }
    }

    public String averageWakeTime() {
        double result = ((totalWhenWake / divide) / 3600000);
        if (result > 12) {
            result = result - 12;
            String finalString = roundMethod(result);
            return finalString + "PM";
        } else {
            return roundMethod(result) + "AM";
        }
    }

}
