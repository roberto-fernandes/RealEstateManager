package com.openclassrooms.realestatemanager.ui.activities;

import android.arch.lifecycle.LiveData;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaDisplayAdapter;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.adapters.VerticalListAdapter;
import com.openclassrooms.realestatemanager.model.FilterParams;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.BUNDLE_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.FILTERED_PARAMS_KEY;
import static com.openclassrooms.realestatemanager.utils.Utils.BundleKeys.REAL_ESTATE_OBJECT_KEY;
import static com.openclassrooms.realestatemanager.utils.Utils.TypesList;
import static com.openclassrooms.realestatemanager.utils.Utils.formatDate;

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
    private TextView itemDescription;
    private TextView noEntries;
    private TextView surface;
    private TextView numOfRooms;
    private TextView numOfBedrooms;
    private TextView type;
    private TextView shortDescription;
    private TextView status;
    private TextView addedDate;
    private TextView soldDate;
    private RecyclerView mediaRecyclerView;
    private TextView location;
    private int realEstateIndex;
    private int listType;
    private ImageView map;
    private Bundle extras;
    private MediaDisplayAdapter mediaDisplayAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        auth = FirebaseAuth.getInstance();
        repository = new Repository(NavigationActivity.this);
        realEstateIndex = 0;

        extras = getIntent().getBundleExtra(BUNDLE_EXTRA);
        if (extras != null) {
            listType = extras.getInt(TypesList.TYPE_LIST_KEY, TypesList.ALL);
        } else {
            listType = TypesList.ALL;
        }

        setViews();
        setListingRecyclerView();
        addDataObservers();
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        configureDrawer();
        // generateFakeList();
    }

    private void setMap(RealEstate realEstate) {
        StringBuilder sb = new StringBuilder("https://maps.googleapis.com/maps/api/staticmap?center=");
        sb.append(realEstate.getAddress());
        sb.append("&markers=%7Ccolor:0xFFFF00%7Clabel:%7C");
        sb.append(realEstate.getAddress());
        sb.append("&zoom=13&size=600x300&maptype=roadmap&key=");
        sb.append(getString(R.string.google_api_key));

        Picasso.get().load(sb.toString()).into(map);
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

        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        int id = menuItem.getItemId();
                        Intent intent = null;

                        switch (id) {
                            case R.id.menu_drawer_all:
                                intent = new Intent(NavigationActivity.this
                                        , NavigationActivity.class);
                                intent.putExtra(TypesList.TYPE_LIST_KEY, TypesList.ALL);
                                break;
                            case R.id.menu_drawer_map:
                                intent = new Intent(NavigationActivity.this
                                        , MapActivity.class);
                                break;
                            case R.id.menu_drawer_filter:
                                intent = new Intent(NavigationActivity.this
                                        , FilterActivity.class);
                                break;
                            case R.id.menu_drawer_sing_out:
                                signOutUser();
                                break;
                            case R.id.menu_drawer_loan:
                                intent = new Intent(NavigationActivity.this
                                        , LoanSimulationActivity.class);
                                break;
                        }
                        drawerLayout.closeDrawer(GravityCompat.START);
                        if (intent != null) {
                            startActivity(intent);
                        }
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
        itemDescription = findViewById(R.id.navigation_activity_description);
        map = findViewById(R.id.map);
        noEntries = findViewById(R.id.no_entries);
        surface = findViewById(R.id.navigation_activity_surface);
        numOfRooms = findViewById(R.id.navigation_activity_num_of_rooms);
        numOfBedrooms = findViewById(R.id.navigation_activity_num_of_bedrooms);
        location = findViewById(R.id.navigation_activity_location);
        mediaRecyclerView = findViewById(R.id.activity_navigation_media_recycler_view);
        type = findViewById(R.id.navigation_activity_type);
        shortDescription = findViewById(R.id.navigation_activity_short_description);
        status = findViewById(R.id.navigation_activity_status);
        addedDate = findViewById(R.id.navigation_activity_added_date);
        soldDate = findViewById(R.id.navigation_activity_sold_date);
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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_toolbar_add:
                goToUpdateAndAddActivity(null);
                break;
            case R.id.menu_toolbar_edit:
                try {
                    goToUpdateAndAddActivity(listings.get(realEstateIndex));
                } catch (Exception e) {
                    Toast.makeText(this, "It is not possible to edit"
                            , Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.menu_toolbar_delete:
                try {
                    repository.deleteListing(listings.get(realEstateIndex));
                    realEstateIndex = 0;

                    if (listings.size() < 2) {
                        restartActivity();
                    }
                } catch (Exception e) {
                    Toast.makeText(this, "It is not possible to delete"
                            , Toast.LENGTH_SHORT).show();
                }
                break;
        }

        return false;
    }

    private void goToUpdateAndAddActivity(RealEstate realEstate) {
        Intent intent = new Intent(NavigationActivity.this
                , UpdateAndAddActivity.class);
        intent.putExtra(REAL_ESTATE_OBJECT_KEY, realEstate);
        startActivity(intent);

    }

    private void addDataObservers() {
        LiveData<List<RealEstate>> listLiveData = null;
        switch (listType) {
            case TypesList.ALL:
                Toast.makeText(getApplicationContext(), "All"
                        , Toast.LENGTH_SHORT).show();
                listLiveData = repository.getAllListings();
                break;
            case TypesList.FILTERED:
                Toast.makeText(getApplicationContext(), "Filtered"
                        , Toast.LENGTH_SHORT).show();
                listLiveData = getFilteredList();
                break;
        }

        listLiveData.observe(NavigationActivity.this,
                new Observer<List<RealEstate>>() {
                    @Override
                    public void onChanged(@Nullable List<RealEstate> realEstates) {
                        if (listings.size() > 0) {
                            listings.clear();
                        }
                        listings.addAll(realEstates);
                        recyclerViewAdapter.notifyDataSetChanged();
                        displayRealEstateInformation();
                        //delete all
                        //if (listings.size() > 0) { repository.deleteListing(listings.get(0)); }
                    }
                });
    }

    private LiveData<List<RealEstate>> getFilteredList() {
        LiveData<List<RealEstate>> listLiveData;
        FilterParams filterParams = extras.getParcelable(FILTERED_PARAMS_KEY);
        listLiveData = repository.filterList(filterParams);
        return listLiveData;
    }

    private void setListingRecyclerView() {
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
                realEstateIndex = position;
                displayRealEstateInformation();
            }
        });
        recyclerView.setAdapter(recyclerViewAdapter);
    }

    private void setPointsOfInterestRecyclerView() {
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.VERTICAL, false);
        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.points_of_interest_recycler_view);
        recyclerView.setLayoutManager(layoutManager);
        VerticalListAdapter verticalListAdapter = new VerticalListAdapter(
                listings.get(realEstateIndex).getPointsOfInterest());
        recyclerView.setAdapter(verticalListAdapter);
    }

    private void displayRealEstateInformation() {
        if (listings.size() > 0) {
            RealEstate realEstate = listings.get(realEstateIndex);
            itemDescription.setText(realEstate.getLongDescription());
            setPointsOfInterestRecyclerView();
            surface.setText(String.valueOf(realEstate.getSurfaceArea()));
            numOfRooms.setText(String.valueOf(realEstate.getNumberOfRooms()));
            numOfBedrooms.setText(String.valueOf(realEstate.getNumbOfBedRooms()));
            location.setText(realEstate.getAddress());
            type.setText(realEstate.getType());
            setMediaRecyclerView(realEstate);
            setMap(realEstate);
            shortDescription.setText(realEstate.getDescription());
            status.setText(realEstate.getStatus());
            addedDate.setText(formatDate(realEstate.getDatePutInMarket()));
            long soldDateLong =realEstate.getSaleData();
            if (soldDateLong>0) {
                soldDate.setText(formatDate(soldDateLong));
            }

            noEntries.setVisibility(View.GONE);
        } else {
            noEntries.setVisibility(View.VISIBLE);
        }
    }

    private void setMediaRecyclerView(final RealEstate realEstate) {
        mediaDisplayAdapter = new MediaDisplayAdapter(realEstate.getPhotos(), false
                , getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false);
        mediaRecyclerView.setLayoutManager(layoutManager);

        mediaDisplayAdapter.setOnDeleteIconListener(new MediaDisplayAdapter.ItemDeleteListener() {
            @Override
            public void deleteIconClicked(int position) {
                realEstate.getPhotos().remove(position);
                mediaDisplayAdapter.notifyDataSetChanged();
            }
        });

        mediaRecyclerView.setAdapter(mediaDisplayAdapter);
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

    private void restartActivity() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
}
