package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.openclassrooms.realestatemanager.R;

import static com.openclassrooms.realestatemanager.utils.Utils.isInternetAvailable;

public class MainActivity extends AppCompatActivity {

    //views from starter code with bug
/*    private TextView textViewMain;
    private TextView textViewQuantity;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bug 1 fix
/*        //bug 1: it was activity_second_activity_text_view_main
        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();*/

        init();
    }

    private void init() {
        findViewById(R.id.activity_main_login_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this
                                , LogInActivity.class);
                        startActivity(intent);
                    }
                });
        findViewById(R.id.activity_main_signup_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this
                                , RegisterActivity.class);
                        startActivity(intent);
                    }
                });
    }


//bug 2 fix
/*
    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

   private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        //bug 2: it was quantity instead of String.valueOf(quantity)
        this.textViewQuantity.setText(String.valueOf(quantity));
    }*/
}
