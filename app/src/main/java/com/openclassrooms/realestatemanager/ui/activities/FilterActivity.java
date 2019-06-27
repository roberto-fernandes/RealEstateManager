package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.FilterParams;

import java.util.Objects;

import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.BUNDLE_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.FILTERED_PARAMS_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.TypesList.FILTERED;
import static com.openclassrooms.realestatemanager.utils.Constants.TypesList.TYPE_LIST_KEY;

public class FilterActivity extends AppCompatActivity {

    private Button filterBtn;
    private FilterParams filterParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        filterParams = new FilterParams();
        setToolbar();
        setViews();
        setListeners();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.filter_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setViews() {
        filterBtn = findViewById(R.id.filter_btn);
    }

    private void setListeners() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateParams();
                Intent intent = new Intent(FilterActivity.this
                        , NavigationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_LIST_KEY, FILTERED);
                bundle.putParcelable(FILTERED_PARAMS_KEY, filterParams);
                intent.putExtra(BUNDLE_EXTRA, bundle);
                startActivity(intent);
            }
        });
    }

    private void updateParams() {
        CheckBox available = findViewById(R.id.activity_filter_available);
        CheckBox sold = findViewById(R.id.activity_filter_sold);
        EditText startNumOfRooms = findViewById(R.id.activity_filter_start_number_of_rooms);
        EditText endNumOfRooms = findViewById(R.id.activity_filter_end_number_of_rooms);
        EditText startNumOfBedRooms = findViewById(R.id.activity_filter_start_number_of_bedrooms);
        EditText endNumOfBedRooms = findViewById(R.id.activity_filter_end_number_of_bedrooms);
        EditText startSurfaceArea = findViewById(R.id.activity_filter_start_surface);
        EditText endSurfaceArea = findViewById(R.id.activity_filter_end_surface);

        filterParams.setAvailable(available.isChecked());
        filterParams.setSold(sold.isChecked());
        if (!startNumOfRooms.getText().toString().isEmpty()) {
            filterParams.setMinNumOfRooms(startNumOfRooms.getText().toString());
        }
        if (!endNumOfRooms.getText().toString().isEmpty()) {
            filterParams.setMaxNumOfRooms(endNumOfRooms.getText().toString());
        }
        if (!startNumOfBedRooms.getText().toString().isEmpty()) {
            filterParams.setMinNumOfBedRooms(startNumOfBedRooms.getText().toString());
        }
        if (!endNumOfBedRooms.getText().toString().isEmpty()) {
            filterParams.setMaxNumOfBedRooms(endNumOfBedRooms.getText().toString());
        }
        if (!startSurfaceArea.getText().toString().isEmpty()) {
            filterParams.setMinSurfaceArea(startSurfaceArea.getText().toString());
        }
        if (!endSurfaceArea.getText().toString().isEmpty()) {
            filterParams.setMaxSurfaceArea(endSurfaceArea.getText().toString());
        }
    }
}
