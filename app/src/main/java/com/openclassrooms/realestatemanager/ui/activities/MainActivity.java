package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private TextView textViewMain;
    private TextView textViewQuantity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //bug 1: it was activity_second_activity_text_view_main
        this.textViewMain = findViewById(R.id.activity_main_activity_text_view_main);
        this.textViewQuantity = findViewById(R.id.activity_main_activity_text_view_quantity);

        this.configureTextViewMain();
        this.configureTextViewQuantity();

        //new code
        findViewById(R.id.activity_main_enter_app_button).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(MainActivity.this
                                , NavigationActivity.class);
                        startActivity(intent);
                    }
                });

     //   generateFakeDate(12);
    }

    private void generateFakeDate(int n) {
        for (int i = 0; i < n; i++) {
            Repository repository = new Repository(MainActivity.this);

            List<String> poitnOfInterest = new ArrayList<>();
            poitnOfInterest.add("Shopping");
            poitnOfInterest.add("Beach");



            List<String> photos = new ArrayList<>();
            photos.add("https://cdn.vox-cdn.com/thumbor/0__zWQZmmmwHA5OjBTAchz6_sBw=/0x0:3000x2000/1200x800/filters:focal(1260x760:1740x1240)/cdn.vox-cdn.com/uploads/chorus_image/image/62922957/4854_Alonzo_Ave__Encino_FInals_34.0.jpg");
            photos.add("https://cdn.pensador.com/img/authors/dr/ho/dr-house-2-l.jpg");

            RealEstate realEstate = new RealEstate();
            realEstate.setType("flat");
            realEstate.setDatePutInMarket(System.currentTimeMillis());
            realEstate.setNumbOfBedRooms(5);
            realEstate.setNumberOfRooms(6);
            realEstate.setStatus(Constants.Status.AVAILABLE);
            realEstate.setPrice("1000");
            realEstate.setAddress("Lisboa");
            realEstate.setAgentID("asdsa");
            realEstate.setDescription("Description");
            realEstate.setLongDescription("LongDiscription");
            realEstate.setPointsOfInterest(poitnOfInterest);
            realEstate.setPhotos(photos);

            repository.insertListing(realEstate);
        }
    }

    private void configureTextViewMain() {
        this.textViewMain.setTextSize(15);
        this.textViewMain.setText("Le premier bien immobilier enregistré vaut ");
    }

    private void configureTextViewQuantity() {
        int quantity = Utils.convertDollarToEuro(100);
        this.textViewQuantity.setTextSize(20);
        //bug 2: it was quantity instead of String.valueOf(quantity)
        this.textViewQuantity.setText(String.valueOf(quantity));
    }
}
