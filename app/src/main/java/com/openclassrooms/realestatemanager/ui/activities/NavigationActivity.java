package com.openclassrooms.realestatemanager.ui.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaDisplayAdapter;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.adapters.VerticalListAdapter;
import com.openclassrooms.realestatemanager.model.FilterParams;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.openclassrooms.realestatemanager.utils.Constants;
import com.openclassrooms.realestatemanager.utils.Utils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.BUNDLE_CURRENCY_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.BUNDLE_EXTRA;
import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.FILTERED_PARAMS_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.REAL_ESTATE_OBJECT_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.SEARCH_PARAM_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.Currencies.DOLLAR;
import static com.openclassrooms.realestatemanager.utils.Constants.Currencies.EURO;
import static com.openclassrooms.realestatemanager.utils.Constants.TypesList.SEARCH;
import static com.openclassrooms.realestatemanager.utils.Constants.TypesList.TYPE_LIST_KEY;
import static com.openclassrooms.realestatemanager.utils.Utils.formatDate;
import static com.openclassrooms.realestatemanager.utils.Utils.storeCurrency;

public class NavigationActivity extends AppCompatActivity {

    private static final String TAG = "NavigationActivity";
    private FirebaseAuth auth;
    private List<RealEstate> listings;
    private Repository repository;
    private RealEstateAdapter recyclerViewAdapter;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle toggle;
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
    private String currency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);

        setConfigs();
        setViews();
        setListingRecyclerView();
        addDataObservers();
        setToolbar();
        Objects.requireNonNull(getSupportActionBar()).setDisplayShowTitleEnabled(false);
        configureDrawer();
    }

    private void setConfigs() {
        auth = FirebaseAuth.getInstance();
        repository = new Repository(NavigationActivity.this);
        realEstateIndex = 0;

        currency = getIntent().getStringExtra(BUNDLE_CURRENCY_KEY);
        if (currency == null) {
            currency = Utils.getCurrency(NavigationActivity.this);
        }

        extras = getIntent().getBundleExtra(BUNDLE_EXTRA);
        if (extras != null) {
            listType = extras.getInt(Constants.TypesList.TYPE_LIST_KEY, Constants.TypesList.ALL);
        } else {
            listType = Constants.TypesList.ALL;
        }
    }

    private void setToolbar() {
        setSupportActionBar(toolbar);
    }

    private void setMap(RealEstate realEstate) {
        String sb = "https://maps.googleapis.com/maps/api/staticmap?center=" +
                realEstate.getAddress() +
                "&markers=%7Ccolor:0xFFFF00%7Clabel:%7C" +
                realEstate.getAddress() +
                "&zoom=13&size=600x300&maptype=roadmap&key=" +
                getString(R.string.google_api_key);
        Picasso.get().load(sb).into(map);
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
                    userEmailTextView.setText(Objects.requireNonNull(FirebaseAuth.getInstance()
                            .getCurrentUser()).getEmail());
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
        NavigationView navigationView = findViewById(R.id.activity_navigation_nav_view);

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
                                intent.putExtra(Constants.TypesList.TYPE_LIST_KEY, Constants.TypesList.ALL);
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
        final MenuItem currencyItem;
        currencyItem = menu.findItem(R.id.menu_toolbar_change_currency);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setMaxWidth(Integer.MAX_VALUE);
        final MenuItem menuItemEdit = menu.findItem(R.id.menu_toolbar_edit);
        final MenuItem menuItemAdd = menu.findItem(R.id.menu_toolbar_add);
        final MenuItem menuItemDelete = menu.findItem(R.id.menu_toolbar_delete);

        if (currency.equals(EURO)) {
            currencyItem.setTitle(getString(R.string.change_to_d));
        } else if (currency.equals(DOLLAR)) {
            currencyItem.setTitle(getString(R.string.change_to_euro));
        }

        currencyItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if (currency.equals(EURO)) {
                    currency = DOLLAR;
                    storeCurrency(NavigationActivity.this, DOLLAR);
                    currencyItem.setTitle(getString(R.string.change_to_euro));

                } else if (currency.equals(DOLLAR)) {
                    currency = EURO;
                    storeCurrency(NavigationActivity.this, EURO);
                    currencyItem.setTitle(getString(R.string.change_to_d));
                }
                recyclerViewAdapter.setCurrency(currency);
                recyclerViewAdapter.notifyDataSetChanged();
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //search is expanded
                menuItemEdit.setVisible(false);
                menuItemAdd.setVisible(false);
                menuItemDelete.setVisible(false);
                currencyItem.setVisible(false);
                toggle.setDrawerIndicatorEnabled(false);
            }
        });

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                menuItemEdit.setVisible(true);
                menuItemAdd.setVisible(true);
                menuItemDelete.setVisible(true);
                currencyItem.setVisible(true);
                toggle.setDrawerIndicatorEnabled(true);
                return false;
            }
        });
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchTerm(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });

        //It must return true for the menu to be displayed; if you return false it will not be show
        return true;
    }

    private void searchTerm(String term) {
        Intent intent = new Intent(NavigationActivity.this
                , NavigationActivity.class);
        Bundle bundle = new Bundle();
        bundle.putInt(TYPE_LIST_KEY, SEARCH);
        bundle.putString(SEARCH_PARAM_KEY, term);
        intent.putExtra(BUNDLE_EXTRA, bundle);
        startActivity(intent);
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
        intent.putExtra(BUNDLE_CURRENCY_KEY, currency);
        startActivity(intent);

    }

    private void addDataObservers() {
        LiveData<List<RealEstate>> listLiveData = null;
        switch (listType) {
            case Constants.TypesList.ALL:
                listLiveData = repository.getAllListings();
                break;
            case Constants.TypesList.FILTERED:
                listLiveData = getFilteredList();
                break;
            case Constants.TypesList.SEARCH:
                listLiveData = getSearchList();
                break;
        }

        if (listLiveData != null) {
            listLiveData.observe(NavigationActivity.this,
                    new Observer<List<RealEstate>>() {
                        @Override
                        public void onChanged(@Nullable List<RealEstate> realEstates) {
                            if (listings.size() > 0) {
                                listings.clear();
                            }
                            if (realEstates != null) {
                                listings.addAll(realEstates);
                                recyclerViewAdapter.notifyDataSetChanged();
                                displayRealEstateInformation();
                            }
                        }
                    });
        }
    }

    private LiveData<List<RealEstate>> getSearchList() {
        LiveData<List<RealEstate>> listLiveData;
        String term = extras.getString(SEARCH_PARAM_KEY, "");
        listLiveData = repository.geSearchedListings(term);
        return listLiveData;
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
        recyclerViewAdapter = new RealEstateAdapter(NavigationActivity.this,
                listings, currency);
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
        @SuppressLint("WrongConstant")
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
            long soldDateLong = realEstate.getSaleData();
            if (soldDateLong > 0) {
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
