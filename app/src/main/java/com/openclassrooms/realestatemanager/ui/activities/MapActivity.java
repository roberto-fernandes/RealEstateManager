package com.openclassrooms.realestatemanager.ui.activities;

import android.Manifest;
import android.app.Dialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;
import com.google.maps.android.clustering.ClusterManager;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.model.ClusterMarker;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.openclassrooms.realestatemanager.utils.ClusterManagerRenderer;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.utils.Constants.MapsCodes.ERROR_DIALOG_REQUEST;
import static com.openclassrooms.realestatemanager.utils.Constants.MapsCodes.MAPVIEW_BUNDLE_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.MapsCodes.PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION;
import static com.openclassrooms.realestatemanager.utils.Constants.MapsCodes.PERMISSIONS_REQUEST_ENABLE_GPS;
import static com.openclassrooms.realestatemanager.utils.Utils.getAddressClassFromString;
import static com.openclassrooms.realestatemanager.utils.Utils.isInternetAvailable;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {

    private boolean mLocationPermissionGranted = false;
    private static final String TAG = MapActivity.class.getSimpleName();
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private GeoPoint mUserPosition;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final double MAP_SCOPE = 0.07D;
    private ClusterManager<ClusterMarker> mClusterManager;
    private ClusterManagerRenderer mClusterManagerRenderer;
    private List<Bitmap> bitmapList;
    private LiveData<List<RealEstate>> allListings;
    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    private ArrayList<ClusterMarker> mClusterMarkers = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        setToolbar();

        if (isInternetAvailable(MapActivity.this)) {
            initMap(savedInstanceState);
        } else {
            Toast.makeText(getBaseContext(), "No wifi internet connection",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void initMap(Bundle savedInstanceState) {
        Repository repository = new Repository(getBaseContext());
        allListings = repository.getAllListings();
        allListings.observe(this, new Observer<List<RealEstate>>() {
            @Override
            public void onChanged(@Nullable List<RealEstate> realEstates) {
                List<String> photosUrls = new ArrayList<>();

                if (realEstates != null) {
                    for (RealEstate realEstate : realEstates) {
                        photosUrls.add(realEstate.getPhotos().get(0));
                    }
                }

                Utils.bitmapsFromUrl(photosUrls, new Utils.OnReceivingBitmapFromUrl() {
                    @Override
                    public void onSucess(List<Bitmap> bitmaps) {
                        bitmapList = bitmaps;
                        addMapMarkers();
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                }, MapActivity.this);
            }
        });
        mLocationPermissionGranted = checkMapServices();
        mMapView = findViewById(R.id.activity_map_map_view);
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        getLastKnownLocation();
        initGoogleMap(savedInstanceState);
    }

    private void addMapMarkers() {

        if (mGoogleMap != null) {

            if (mClusterManager == null) {
                mClusterManager = new ClusterManager<>(getApplicationContext(), mGoogleMap);
            }
            if (mClusterManagerRenderer == null) {
                mClusterManagerRenderer = new ClusterManagerRenderer(
                        this,
                        mGoogleMap,
                        mClusterManager
                );
                mClusterManager.setRenderer(mClusterManagerRenderer);
            }

            for (int i = 0; i < allListings.getValue().size(); i++) {
                try {
                    RealEstate realEstate = allListings.getValue().get(i);
                    Address address = getAddressClassFromString(realEstate.getAddress(), getBaseContext());
                    Bitmap avatar = null;
                    try {
                        avatar = bitmapList.get(i);
                    } catch (NumberFormatException ignored) {
                    }
                    ClusterMarker newClusterMarker = new ClusterMarker(
                            new LatLng(
                                    address.getLatitude(),
                                    address.getLongitude()
                            ),

                            realEstate.getType(),
                            realEstate.getDescription(),
                            avatar
                    );
                    mClusterManager.addItem(newClusterMarker);
                    mClusterMarkers.add(newClusterMarker);

                } catch (NullPointerException e) {
                    Log.e(TAG, "addMapMarkers: NullPointerException: " + e.getMessage());
                }
            }
            mClusterManager.cluster();
        }
    }


    private void getLastKnownLocation() {
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mFusedLocationClient.getLastLocation().addOnCompleteListener(new OnCompleteListener<Location>() {
            @Override
            public void onComplete(@NonNull Task<Location> task) {
                if (task.isSuccessful()) {
                    Location location = task.getResult();
                    if (location != null) {
                        mUserPosition = new GeoPoint(location.getLatitude(), location.getLongitude());
                    }
                    if (mGoogleMap != null) {
                        setCameraView();
                    }
                }
            }
        });
    }

    private void setCameraView() {
        // Set a boundary to start
        double bottomBoundary = mUserPosition.getLatitude() - MAP_SCOPE;
        double leftBoundary = mUserPosition.getLongitude() - MAP_SCOPE;
        double topBoundary = mUserPosition.getLatitude() + MAP_SCOPE;
        double rightBoundary = mUserPosition.getLongitude() + MAP_SCOPE;

        LatLngBounds mMapBoundary = new LatLngBounds(
                new LatLng(bottomBoundary, leftBoundary),
                new LatLng(topBoundary, rightBoundary)
        );
        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(mMapBoundary, 0));
    }


    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.map_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        Bundle mapViewBundle = outState.getBundle(MAPVIEW_BUNDLE_KEY);
        if (mapViewBundle == null) {
            mapViewBundle = new Bundle();
            outState.putBundle(MAPVIEW_BUNDLE_KEY, mapViewBundle);
        }

        mMapView.onSaveInstanceState(mapViewBundle);
    }


    private void initGoogleMap(Bundle savedInstanceState) {
        Bundle mapViewBundle = null;
        if (savedInstanceState != null) {
            mapViewBundle = savedInstanceState.getBundle(MAPVIEW_BUNDLE_KEY);
        }

        mMapView.onCreate(mapViewBundle);

        mMapView.getMapAsync(this);
    }

    private boolean checkMapServices() {
        if (isServicesOK()) {
            return isMapsEnabled();
        }
        return false;
    }

    private void buildAlertMessageNoGps() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("This application requires GPS to work properly" +
                ", do you want to enable it?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("unused") final DialogInterface dialog,
                                        @SuppressWarnings("unused") final int id) {
                        Intent enableGpsIntent = new Intent(
                                android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivityForResult(enableGpsIntent, PERMISSIONS_REQUEST_ENABLE_GPS);
                    }
                });
        final AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean isMapsEnabled() {
        final LocationManager manager = (LocationManager)
                getSystemService(Context.LOCATION_SERVICE);

        if (!manager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            buildAlertMessageNoGps();
            return false;
        }
        return true;
    }

    private void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this.getApplicationContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
            showMap();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    private void showMap() {
        Toast.makeText(getBaseContext(), "Show Map", Toast.LENGTH_SHORT).show();
    }

    public boolean isServicesOK() {
        int available = GoogleApiAvailability.getInstance()
                .isGooglePlayServicesAvailable(getBaseContext());

        if (available == ConnectionResult.SUCCESS) {
            return true;
        } else if (GoogleApiAvailability.getInstance().isUserResolvableError(available)) {
            Dialog dialog = GoogleApiAvailability.getInstance()
                    .getErrorDialog(MapActivity.this, available,
                            ERROR_DIALOG_REQUEST);
            dialog.show();
        }
        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                mLocationPermissionGranted = true;
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PERMISSIONS_REQUEST_ENABLE_GPS) {
            if (mLocationPermissionGranted) {
                showMap();
            } else {
                getLocationPermission();
            }
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        if (mMapView != null)
            mMapView.onResume();
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mMapView != null)
            mMapView.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mMapView != null)
            mMapView.onStop();
    }

    @Override
    public void onMapReady(GoogleMap map) {
        if (ActivityCompat.checkSelfPermission(MapActivity.this
                , Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(MapActivity.this
                , Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        map.setMyLocationEnabled(true);
        mGoogleMap = map;
        if (mUserPosition != null) {
            setCameraView();
        }
    }

    @Override
    public void onPause() {
        if (mMapView != null)
            mMapView.onPause();
        super.onPause();
    }

    @Override
    public void onDestroy() {
        if (mMapView != null)
            mMapView.onDestroy();
        super.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mMapView != null)
            mMapView.onLowMemory();
    }
}

