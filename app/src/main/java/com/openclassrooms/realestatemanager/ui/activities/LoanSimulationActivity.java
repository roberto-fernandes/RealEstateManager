package com.openclassrooms.realestatemanager.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

public class LoanSimulationActivity extends AppCompatActivity {

    private SeekBar amountSeekBar;
    private SeekBar timeSeekBar;
    private TextView amountTextView;
    private TextView timeTextView;
    private TextView interestTextView;
    private int months;
    private int amount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_simulation);

        setViews();
        setListeners();
    }

    private void setViews() {
        amountSeekBar = findViewById(R.id.amount_seek_bar);
        timeSeekBar = findViewById(R.id.time_seek_bar);
        amountTextView = findViewById(R.id.amount_text_view);
        timeTextView = findViewById(R.id.time_text_view);
        interestTextView = findViewById(R.id.interest_text_view);
    }

    private void setListeners() {
        amountSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                amount = progress;
                updateValues();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        timeSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                months = progress;
                updateValues();
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private void updateValues() {
        amountTextView.setText(String.valueOf(amount));
        timeTextView.setText(String.valueOf(months));

        double interestRate = getInterestRate();

        String interestString = String.format("%,.2f", interestRate);
        interestTextView.setText(interestString);
    }

    private double getInterestRate() {
        double factor = 9D;
        if (months < 1) {
            return 0;
        }
        return Math.max( (months / factor), 3d);
    }

}
