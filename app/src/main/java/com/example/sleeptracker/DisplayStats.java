package com.example.sleeptracker;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import br.com.sapereaude.maskedEditText.MaskedEditText;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class DisplayStats extends AppCompatActivity {
    private MaskedEditText startDate;
    private MaskedEditText endDate;
    private Button getDateBounds;

    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_display_stats);
        startDate = this.findViewById(R.id.start_date);
        endDate = this.findViewById(R.id.end_date);
        getDateBounds = this.findViewById(R.id.get_date_bounds);
    }

    public void getStats(View view) {

    }
}
