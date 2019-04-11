package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.openclassrooms.realestatemanager.utils.Utils;

import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.BUNDLE_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.MAX_SURFACE;
import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.MIN_SURFACE;
import static com.openclassrooms.realestatemanager.utils.Utils.TypesList.FILTERED;
import static com.openclassrooms.realestatemanager.utils.Utils.TypesList.TYPE_LIST_KEY;

public class FilterActivity extends AppCompatActivity {

    private Button filterBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        setToolbar();
        setViews();
        setListeners();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.filter_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setViews() {
        filterBtn = findViewById(R.id.filter_btn);
    }

    private void setListeners() {
        filterBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FilterActivity.this
                        , NavigationActivity.class);
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE_LIST_KEY, FILTERED);
                bundle.putString(MIN_SURFACE, "20");
                bundle.putString(MAX_SURFACE, "100");
                intent.putExtra(BUNDLE_EXTRA, bundle);
                startActivity(intent);
            }
        });
    }
}
