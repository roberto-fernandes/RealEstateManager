package com.openclassrooms.realestatemanager.ui.activities;

import android.os.Bundle;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.text.NumberFormat;
import java.util.Objects;

public class LoanSimulationActivity extends AppCompatActivity {

    private SeekBar amountSeekBar;
    private SeekBar timeSeekBar;
    private TextView amountTextView;
    private TextView timeTextView;
    private TextView interestTextView;
    private int months;
    private int amount;
    private TextView monthPaymentTextView;
    private TextView totalPaymentTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loan_simulation);

        setViews();
        setListeners();
        setToolbar();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.loan_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setViews() {
        amountSeekBar = findViewById(R.id.amount_seek_bar);
        timeSeekBar = findViewById(R.id.time_seek_bar);
        amountTextView = findViewById(R.id.amount_text_view);
        timeTextView = findViewById(R.id.time_text_view);
        interestTextView = findViewById(R.id.interest_text_view);
        totalPaymentTextView = findViewById(R.id.payment_total_text_view);
        monthPaymentTextView = findViewById(R.id.month_payment_text_view);
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
        amountTextView.setText(NumberFormat.getInstance().format(amount));
        timeTextView.setText(String.valueOf(months));

        double interestRate = getInterestRate(months);
        double totalPayment = getTotalPayment(interestRate, amount);
        double monthlyPayment = getMonthlyPayment(totalPayment, months);

        interestTextView.setText(Utils.formatDoubleToString(interestRate));
        totalPaymentTextView.setText(Utils.formatDoubleToString(totalPayment));
        monthPaymentTextView.setText(Utils.formatDoubleToString(monthlyPayment));
    }

    public static double getInterestRate(int months) {
        double factor = 9D;
        if (months < 1) {
            return 0;
        }
        return Math.max((months / factor), 3d);
    }

    public static double getTotalPayment(double interestRate, int amount) {
        return interestRate / 100 * amount + amount;
    }

    public static double getMonthlyPayment(double totalPayment, int months) {
        return totalPayment / months;
    }
}
