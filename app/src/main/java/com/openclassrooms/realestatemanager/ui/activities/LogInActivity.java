package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;

public class LogInActivity extends AppCompatActivity {

    private TextView registerTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        setViews();
        setListeners();
    }

    private void setViews() {
        registerTextView = findViewById(R.id.activity_log_in_register_text_view);
    }

    private void setListeners() {
        registerTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LogInActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }
}
