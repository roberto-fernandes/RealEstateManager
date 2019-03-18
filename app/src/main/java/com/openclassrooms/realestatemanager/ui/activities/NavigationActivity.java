package com.openclassrooms.realestatemanager.ui.activities;

import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private FirebaseAuth auth;
    private List<RealEstate> listings;
    private Repository repository;
    private RealEstateAdapter recyclerViewAdapter;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        auth = FirebaseAuth.getInstance();
        repository = new Repository(NavigationActivity.this);

        setViews();
        setRecyclerView();
        addDataObservers();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        configureDrawer();
        //   generateFakeList();
    }

    private void configureDrawer() {
        configureDrawerLayout();
        configureNavigationView();
    }

    private void configureDrawerLayout() {
        this.drawerLayout = findViewById(R.id.activity_navigation_drawer_layout);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar
                , R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        drawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View view, float v) {

            }

            @Override
            public void onDrawerOpened(@NonNull View view) {
                // blurBackground();
                TextView userEmailTextView = findViewById(R.id.drawer_header_user_email);
                try {
                    userEmailTextView.setText(FirebaseAuth.getInstance().getCurrentUser().getEmail());
                } catch (Exception e) {
                    Log.e(TAG, "configureDrawerLayout: " + e.getMessage());
                }
            }

            @Override
            public void onDrawerClosed(@NonNull View view) {

            }

            @Override
            public void onDrawerStateChanged(int i) {

            }
        });
    }

    private void signOutUser() {
        FirebaseAuth.getInstance().signOut();
        Log.d(TAG, "onClick: user signOut");
        Intent intent = new Intent(NavigationActivity.this
                , LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void configureNavigationView() {
        this.navigationView = findViewById(R.id.activity_navigation_nav_view);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                int id = menuItem.getItemId();
                Intent intent = null;

                switch (id) {
                    case R.id.menu_drawer_all:
                        Toast.makeText(getApplicationContext(), "all", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_drawer_filter:
                        Toast.makeText(getApplicationContext(), "filter", Toast.LENGTH_SHORT).show();
                        break;
                    case R.id.menu_drawer_sing_out:
                        signOutUser();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    @Override
    public void onBackPressed() {
        if (this.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            this.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void setViews() {
        toolbar = findViewById(R.id.navigation_activity_toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.toolbar_menu, menu);
        MenuItem item = menu.findItem(R.id.menu_toolbar_search);

        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        final MenuItem menuItemEdit = menu.findItem(R.id.menu_toolbar_edit);
        final MenuItem menuItemAdd = menu.findItem(R.id.menu_toolbar_add);
        final MenuItem menuItemDelete = menu.findItem(R.id.menu_toolbar_delete);

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search is expanded
                Log.d(TAG, "onClick: ");
                menuItemEdit.setVisible(false);
                menuItemAdd.setVisible(false);
                menuItemDelete.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                Log.d(TAG, "onClose: ");
                menuItemEdit.setVisible(true);
                menuItemAdd.setVisible(true);
                menuItemDelete.setVisible(true);
                toggle.setDrawerIndicatorEnabled(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                Log.d(TAG, "onQueryTextSubmit: ");
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                Log.d(TAG, "onQueryTextChange: ");
                return false;
            }
        });

        //It must return true for the menu to be displayed; if you return false it will not be show
        return true;
    }

    private void addDataObservers() {
        repository.getAllListings().observe(NavigationActivity.this,
                new Observer<List<RealEstate>>() {
                    @Override
                    public void onChanged(@Nullable List<RealEstate> realEstates) {
                        if (listings.size() > 0) {
                            listings.clear();
                        }
                        listings.addAll(realEstates);
                        recyclerViewAdapter.notifyDataSetChanged();
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
        if (currentUser == null) goToLogInActivity();
    }

    private void goToLogInActivity() {
        Intent intent = new Intent(NavigationActivity.this, LogInActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
