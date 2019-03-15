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

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.ListingAdapter;
import com.openclassrooms.realestatemanager.model.RealEstateListing;
import com.openclassrooms.realestatemanager.repository.Repository;

import java.util.ArrayList;
import java.util.List;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private FirebaseAuth auth;
    private List<RealEstateListing> listings;
    private Repository repository;
    private ListingAdapter recyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        auth = FirebaseAuth.getInstance();

        debugStuff();
        setRecyclerView();
        addDataObservers();

    }

    private void addDataObservers() {
        repository = new Repository(NavigationActivity.this);
        repository.getAllListings().observe(NavigationActivity.this,
                new Observer<List<RealEstateListing>>() {
            @Override
            public void onChanged(@Nullable List<RealEstateListing> realEstateListings) {
                if (listings.size()>0) {
                    listings.clear();
                }
                listings.addAll(realEstateListings);
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
        recyclerViewAdapter = new ListingAdapter(listings);
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private List<RealEstateListing> generateFakeList() {
        List<RealEstateListing> realEstateListingsList = new ArrayList<>();
        RealEstateListing realEstateListing = new RealEstateListing();
        realEstateListing.setDescription("House near the river");
        realEstateListing.setType("Flat");
        realEstateListing.setPriceInDollars(2000);
        List<String> photos = new ArrayList<>();
        photos.add("https://pmcvariety.files.wordpress.com/2018/07/" +
                "bradybunchhouse_sc11.jpg?w=1000&h=563&crop=1");
        realEstateListing.setPhotos(photos);
        for (int i = 0; i < 51; i++) realEstateListingsList.add(realEstateListing);
        return realEstateListingsList;
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
