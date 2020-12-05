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

public class DisplayStats extends AppCompatActivity {
    private SleepStatsDatabase sleepStatsDatabase;
    private TableLayout table;
    private MaskedEditText startDate;
    private MaskedEditText endDate;

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

        TextView testText = (TextView)findViewById(R.id.tableText);
        if(sleepStats.isEmpty()) {
            testText.setText("No sleep data found.");
        }
        else {
            testText.setText(Integer.toString(sleepStats.size()));
            if (sleepStats.size() > 10) {
                sleepStats = sleepStats.subList(0, 11);
            }
            for (SleepStats stat : sleepStats) {
                createRow(stat);
            }
        }
    }
}
