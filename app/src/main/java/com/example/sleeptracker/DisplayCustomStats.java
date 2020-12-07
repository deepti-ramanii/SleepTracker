package com.example.sleeptracker;
import android.os.Build;
import android.os.Bundle;
import android.view.View;

import br.com.sapereaude.maskedEditText.MaskedEditText; // https://www.geeksforgeeks.org/how-to-add-mask-to-an-edittext-in-android/ -> tutorial for creating a masked EditText

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.List;
import java.math.RoundingMode;
import java.text.DecimalFormat;

//displays sleep stats between a set of days, including rating, number of hours slept, etc.
public class DisplayCustomStats extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;

    private MaskedEditText startDate;
    private MaskedEditText endDate;
    private GraphView sleepQualityGraph;

    private double totalHoursSlept = 0.0;
    private double totalRating = 0.0;
    private int numStats = 0;
    private long totalWhenSleep = 0;
    private long totalWhenWake = 0;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_custom_stats);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);

        startDate = this.findViewById(R.id.start_date);
        endDate = this.findViewById(R.id.end_date);
        sleepQualityGraph = this.findViewById(R.id.sleep_quality_graph);
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

        DataPoint[] sleepQualityData = new DataPoint[sleepStats.size()];
        for(SleepStats stats : sleepStats) {
            sleepQualityData[numStats] = new DataPoint(numStats, stats.getRating());

            totalHoursSlept += stats.getHoursSlept();
            totalRating += stats.getRating();
            totalWhenSleep = Long.sum(totalWhenSleep, stats.getSleepTime());
            totalWhenWake = Long.sum(totalWhenWake, stats.getWakeTime());

            numStats++;
        }
        LineGraphSeries<DataPoint> sleepQuality = new LineGraphSeries<DataPoint>(sleepQualityData);
        sleepQualityGraph.addSeries(sleepQuality);
    }

    public String roundMethod(double round) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        return df.format(round);
    }

    public String averageTime(long totalTime, int numTimes) {
        double result = ((totalTime / numTimes) / 3600000.0);
        if (result > 12) {
            result = result - 12;
            String finalString = roundMethod(result);
            return finalString + "PM";
        } else {
            return roundMethod(result) + "AM";
        }
    }

}
