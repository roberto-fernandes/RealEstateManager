package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.FilterParams;

import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.BUNDLE_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.FILTERED_PARAMS_KEY;
import static com.openclassrooms.realestatemanager.utils.Utils.TypesList.FILTERED;
import static com.openclassrooms.realestatemanager.utils.Utils.TypesList.TYPE_LIST_KEY;

public class FilterActivity extends AppCompatActivity {

    private Button filterBtn;
    private FilterParams filterParams;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filter);

        setToolbar();
        setViews();
        setListeners();
        filterParams = new FilterParams();
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
                bundle.putParcelable(FILTERED_PARAMS_KEY, filterParams);
                intent.putExtra(BUNDLE_EXTRA, bundle);
                startActivity(intent);
            }
        });
    }
}
