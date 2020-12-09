package com.example.sleeptracker;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText; // https://www.geeksforgeeks.org/how-to-add-mask-to-an-edittext-in-android/ -> tutorial for creating a masked EditText

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.Date;
import java.util.List;

//displays sleep stats between a set of days, including rating, number of hours slept, etc.
public class DisplayCustomStats extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;

    private MaskedEditText startDate, endDate;

    private GraphView sleepQualityGraph;
    private TextView avgSleepTime, avgWakeTime, avgHoursSleep, avgSleepQuality;

    private double totalHoursSlept = 0.0;
    private double totalRating = 0.0;
    private long totalSleepTime = 0;
    private long totalWakeTime = 0;
    private int numStats;
    private boolean hasDates;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_custom_stats);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);

        startDate = this.findViewById(R.id.start_date);
        endDate = this.findViewById(R.id.end_date);

        sleepQualityGraph = this.findViewById(R.id.sleep_quality_graph);
        avgSleepTime = this.findViewById(R.id.average_sleep_time);
        avgWakeTime = this.findViewById(R.id.average_wake_time);
        avgHoursSleep = this.findViewById(R.id.average_hours_sleep);
        avgSleepQuality = this.findViewById(R.id.average_sleep_quality);
        hasDates = false;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getStats(View view) {
        int startDay = Integer.parseInt(startDate.getText().toString().substring(0, 2));
        int startMonth = Integer.parseInt(startDate.getText().toString().substring(3, 5));
        int startYear = Integer.parseInt(startDate.getText().toString().substring(6, 10));

        int endDay = Integer.parseInt(endDate.getText().toString().substring(0, 2));
        int endMonth = Integer.parseInt(endDate.getText().toString().substring(3, 5));
        int endYear = Integer.parseInt(endDate.getText().toString().substring(6, 10));

        List<SleepStats> sleepStats = sleepStatsDatabase.getBetweenDates(SleepStats.getBeginningOfDay(startDay, startMonth, startYear), SleepStats.getEndOfDay(endDay, endMonth, endYear));
        resetPreviousStats();

        DataPoint[] sleepQualityData = new DataPoint[sleepStats.size()];
        for(SleepStats stats : sleepStats) {
            sleepQualityData[numStats] = new DataPoint(numStats, stats.getRating());

            totalHoursSlept += stats.getHoursSlept();
            totalRating += stats.getRating();
            totalSleepTime = Long.sum(totalSleepTime, stats.getSleepTime());
            totalWakeTime = Long.sum(totalWakeTime, stats.getWakeTime());

            numStats++;
        }
        LineGraphSeries<DataPoint> sleepQuality = new LineGraphSeries<DataPoint>(sleepQualityData);
        sleepQualityGraph.addSeries(sleepQuality);
        avgSleepTime.setText(averageTime(totalSleepTime, numStats));
        avgWakeTime.setText(averageTime(totalWakeTime, numStats));
        avgHoursSleep.setText(roundToTwoDecimals(totalHoursSlept / numStats));
        avgSleepQuality.setText(roundToTwoDecimals(totalRating / numStats) + "/10");
        this.findViewById(R.id.average_sleep_wake_times_labels).setVisibility(View.VISIBLE);
        this.findViewById(R.id.average_sleep_stats_labels).setVisibility(View.VISIBLE);
        hasDates = true;
    }

    private String roundToTwoDecimals(double round) {
        return String.format("%.2f", round);
    }

    private String averageTime(long totalTime, int numTimes) {
        Date date = SleepStats.getDateFromLong(totalTime / numTimes);
        String time = (date.getHours() % 12) + ":" + date.getMinutes() + ":" + date.getSeconds();
        if (date.getHours() >= 12) {
            return time + " PM";
        }
        return time + " AM";
    }

    private void resetPreviousStats() {
        numStats = 0;
        totalHoursSlept = 0;
        totalRating = 0;
        totalSleepTime = 0;
        totalWakeTime = 0;
        sleepQualityGraph.removeAllSeries();
        this.findViewById(R.id.average_sleep_wake_times_labels).setVisibility(View.INVISIBLE);
        this.findViewById(R.id.average_sleep_stats_labels).setVisibility(View.INVISIBLE);
    }

    public void goToGetSleepInfo(View view) {
        Intent activitySwitchIntent = new Intent(DisplayCustomStats.this, GetSleepInfo.class);
        startActivity(activitySwitchIntent);
    }

    public void goToRecommendations(View view) {
        if (hasDates) {
            // we want to send avg hours and sleep/wakeup time
            Intent activitySwitchIntent = new Intent(DisplayCustomStats.this, Recommendations.class);
            activitySwitchIntent.putExtra(Recommendations.AVG_HOURS, (totalHoursSlept / numStats));
            startActivity(activitySwitchIntent);
        }
    }
}
