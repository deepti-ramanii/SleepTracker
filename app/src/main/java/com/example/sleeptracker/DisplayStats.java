package com.example.sleeptracker;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import br.com.sapereaude.maskedEditText.MaskedEditText; // https://www.geeksforgeeks.org/how-to-add-mask-to-an-edittext-in-android/ -> tutorial for creating a masked EditText

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;
import java.math.RoundingMode;
import java.text.DecimalFormat;

public class DisplayStats extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;
    private TableLayout table;
    private MaskedEditText startDate;
    private MaskedEditText endDate;
    private double totalHoursSlept;
    private double totalRating;
    private double divideCounter = 0;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_stats);
        table = findViewById(R.id.table_main);
        sleepStatsDatabase = SleepStatsDatabase.getInstance(this);
        startDate = this.findViewById(R.id.start_date);
        endDate = this.findViewById(R.id.end_date);
    }

    private void createRow(SleepStats stat) {
        TableRow tableRow = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(stat.toString());
        tableRow.addView(tv0);
        table.addView(tableRow);
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
            divideCounter++;
        }
    }

    public String roundMethod(double round) {
        DecimalFormat df = new DecimalFormat("0.00");
        df.setRoundingMode(RoundingMode.UP);
        return df.format(round);
    }

    public String averageHours() {
        return roundMethod(totalHoursSlept / divideCounter);
    }

    public String averageRating() {
        return roundMethod(totalRating / divideCounter);
    }

}
