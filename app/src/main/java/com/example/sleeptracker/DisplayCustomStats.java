package com.example.sleeptracker;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText; // https://www.geeksforgeeks.org/how-to-add-mask-to-an-edittext-in-android/ -> tutorial for creating a masked EditText

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
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
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getStats(View view) {
        boolean hasValidInput = true;
        if(startDate.getText().toString().equals("DD/MM/YYYY")) {
            startDate.setError("Please enter a valid date.");
            hasValidInput = false;
        }
        if(startDate.getText().toString().equals("DD/MM/YYYY")) {
            endDate.setError("Please enter a valid date.");
            hasValidInput = false;
        }
        if(hasValidInput) {
            int startDay = Integer.parseInt(startDate.getText().toString().substring(0, 2));
            int startMonth = Integer.parseInt(startDate.getText().toString().substring(3, 5));
            int startYear = Integer.parseInt(startDate.getText().toString().substring(6, 10));
            if (!isValidDate(startDay, startMonth, startYear)) {
                startDate.setError("Please enter a valid date.");
                hasValidInput = false;
            }
            int endDay = Integer.parseInt(endDate.getText().toString().substring(0, 2));
            int endMonth = Integer.parseInt(endDate.getText().toString().substring(3, 5));
            int endYear = Integer.parseInt(endDate.getText().toString().substring(6, 10));
            if (!isValidDate(endDay, endMonth, endYear)) {
                endDate.setError("Please enter a valid date.");
                hasValidInput = false;
            }

            if(hasValidInput) {
                resetPreviousStats();
                List<SleepStats> sleepStats = sleepStatsDatabase.getBetweenDates(SleepStats.getBeginningOfDay(startDay, startMonth, startYear), SleepStats.getEndOfDay(endDay, endMonth, endYear));
                LineGraphSeries<DataPoint> sleepQualityData = new LineGraphSeries<DataPoint>();
                int numRatings = 0;
                for (SleepStats stats : sleepStats) {
                    int rating = stats.getRating();
                    if(rating != -1) {
                        totalRating += stats.getRating();
                        sleepQualityData.appendData(new DataPoint(numRatings, stats.getRating()), true, sleepStats.size());
                        numRatings++;
                    }

                    totalHoursSlept += stats.getHoursSlept();
                    totalSleepTime = Long.sum(totalSleepTime, stats.getSleepTime());
                    totalWakeTime = Long.sum(totalWakeTime, stats.getWakeTime());
                }
                sleepQualityGraph.addSeries(sleepQualityData);
                avgSleepTime.setText(averageTime(totalSleepTime, sleepStats.size()));
                avgWakeTime.setText(averageTime(totalWakeTime, sleepStats.size()));
                avgHoursSleep.setText(averageStatsAndRound(totalHoursSlept, sleepStats.size()));
                avgSleepQuality.setText(averageStatsAndRound(totalRating, numRatings) + "/10");
                this.findViewById(R.id.display_stat_averages).setVisibility(View.VISIBLE);
            }
        }
    }

    private String averageStatsAndRound(double total, int count) {
        if(count <= 0) {
            return "0.00";
        }
        return String.format("%.2f", total / count);
    }

    private String averageTime(long totalTime, int numTimes) {
        if(numTimes <= 0) {
            return "No data.";
        }
        Date date = SleepStats.getDateFromLong(totalTime / numTimes);
        String time = (date.getHours() % 12) + ":" + date.getMinutes() + ":" + date.getSeconds();
        if (date.getHours() >= 12) {
            return time + " PM";
        }
        return time + " AM";
    }

    private void resetPreviousStats() {
        totalHoursSlept = 0;
        totalRating = 0;
        totalSleepTime = 0;
        totalWakeTime = 0;
        sleepQualityGraph.removeAllSeries();
        this.findViewById(R.id.display_stat_averages).setVisibility(View.INVISIBLE);
    }

    private boolean isValidDate(int day, int month, int year) {
        int numDays = -1;
        if(month == 1 || month == 3 || month == 5 || month == 7 || month == 8 || month == 10 || month == 12) {
            numDays = 31;
        } else if(month == 4 || month == 6 || month == 9 || month == 11) {
            numDays = 30;
        } else if(month == 2) {
            if(year % 4 == 0) {
                numDays = 29;
            } else {
                numDays = 29;
            }
        }
        return (month <= 12 && day <= numDays);
    }

    public void goToGetSleepInfo(View view) {
        Intent activitySwitchIntent = new Intent(DisplayCustomStats.this, GetSleepInfo.class);
        startActivity(activitySwitchIntent);
    }
}
