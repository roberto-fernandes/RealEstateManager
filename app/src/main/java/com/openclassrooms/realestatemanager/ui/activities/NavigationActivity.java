package com.openclassrooms.realestatemanager.ui.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private FirebaseAuth auth;
    private List<RealEstate> listings;
    private Repository repository;
    private RealEstateAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        auth = FirebaseAuth.getInstance();
        repository = new Repository(NavigationActivity.this);

        debugStuff();
        setRecyclerView();
        addDataObservers();
     //   generateFakeList();
    }

    private void addDataObservers() {
        repository.getAllListings().observe(NavigationActivity.this,
                new Observer<List<RealEstate>>() {
            @Override
            public void onChanged(@Nullable List<RealEstate> realEstates) {
                if (listings.size()>0) {
                    listings.clear();
                }
                listings.addAll(realEstates);
                recyclerViewAdapter.notifyDataSetChanged();
            }
        });
    }

    private void debugStuff() {
        Button btn = findViewById(R.id.activity_navigation_button);

        btn.setText("Sign Out " + FirebaseAuth.getInstance().getCurrentUser().getEmail());

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Log.d(TAG, "onClick: user signOut");
                Intent intent = new Intent(NavigationActivity.this
                        , LogInActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
    }

    private void setRecyclerView() {
        listings = new ArrayList<>();
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false);
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.activity_navigation_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        recyclerViewAdapter = new RealEstateAdapter(NavigationActivity.this, listings);
        recyclerViewAdapter.setOnSelectionItem(new RealEstateAdapter.OnItemSelectedListener() {
            @Override
            public void onSelection(int position) {
                displayRealEstateInformation(position);
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void displayRealEstateInformation(int position) {
        String positionString = String.valueOf(position);
        Toast.makeText(NavigationActivity.this, positionString, Toast.LENGTH_SHORT)
                .show();
    }

    private void generateFakeList() {
        RealEstate realEstate = new RealEstate();
        realEstate.setDescription("House near the river From DB");
        realEstate.setType("Flat");
        realEstate.setPriceInDollars(2000);
        List<String> photos = new ArrayList<>();
        photos.add("https://pmcvariety.files.wordpress.com/2018/07/" +
                "bradybunchhouse_sc11.jpg?w=1000&h=563&crop=1");
        realEstate.setPhotos(photos);
        realEstate.setAddress("some address");
        realEstate.setAgentID("21");
        realEstate.setDatePutInMarket(2311456L);
        realEstate.setNumberOfRooms(5);
        List<String> pointsOfInterest = new ArrayList<>();
        pointsOfInterest.add("1 point of interest");
        pointsOfInterest.add("2 point of interest");
        realEstate.setPointsOfInterest(pointsOfInterest);
        realEstate.setDatePutInMarket(321456L);
        realEstate.setPriceInDollars(324);

        for (int i = 0; i < 51; i++) repository.insertListing(realEstate);
    }

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser==null) goToLogInActivity();
    }

    private void goToLogInActivity() {
        Intent intent = new Intent(NavigationActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
