package com.openclassrooms.realestatemanager.ui.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaDisplayAdapter;
import com.openclassrooms.realestatemanager.adapters.PointsOfInterestAdapter;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.openclassrooms.realestatemanager.adapters.PointsOfInterestAdapter.*;
import static com.openclassrooms.realestatemanager.utils.Utils.Status.AVAILABLE;

public class UpdateAndAddActivity extends AppCompatActivity implements View.OnClickListener {

    private RealEstate realEstate;
    private EditText shortDescription;
    private EditText longDescription;
    private EditText media;
    private EditText type;
    private EditText numOfRooms;
    private EditText location;
    private EditText surface;
    private EditText price;
    private EditText numOfBedrooms;
    private ImageView mediaAdd;
    private ImageView pointsOfIntAdd;
    private RecyclerView mediaRecyclerView;
    private MediaDisplayAdapter mediaDisplayAdapter;
    private Button submitBtn;
    private PointsOfInterestAdapter pointsOfInterestAdapter;
    private RecyclerView pointsOfInterestRecyclerView;
    private EditText pointsOfInterestEditText;
    private Repository repository;
    private boolean updating;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_add);

        setToolbar();
        realEstate = getIntent().getParcelableExtra(Utils.BundleKeys.REAL_ESTATE_OBJECT_KEY);
        repository = new Repository(UpdateAndAddActivity.this);
        setViews();
        setParams();
        setDebugData();
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.update_and_add_toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void setDebugData() {
        media.setText("https://cdn.pixabay.com/photo/2016/10/02/00/53/a-1708752_960_720.png");
        shortDescription.setText("short desc");
        longDescription.setText("long desc");
        type.setText("flat");
        numOfRooms.setText("4");
        surface.setText("12");
        location.setText("Coimbra");
        pointsOfInterestEditText.setText("Shopping Center");
    }

    private void setViews() {
        pointsOfIntAdd = findViewById(R.id.activity_update_and_add_points_of_interest_add_icon);
        shortDescription = findViewById(R.id.activity_update_and_add_short_description_edit_text);
        longDescription = findViewById(R.id.activity_update_and_add_long_description_edit_text);
        mediaAdd = findViewById(R.id.activity_update_and_add_media_add_icon);
        mediaRecyclerView = findViewById(R.id.activity_update_and_add_media_recycler_view);
        media = findViewById(R.id.activity_update_and_add_media_edit_text);
        type = findViewById(R.id.activity_update_and_add_type_edit_text);
        numOfRooms = findViewById(R.id.activity_update_and_add_num_of_rooms_edit_text);
        surface = findViewById(R.id.activity_update_and_add_surface_edit_text);
        location = findViewById(R.id.activity_update_and_add_location);
        submitBtn = findViewById(R.id.activity_update_and_add_submit_btn);
        pointsOfInterestRecyclerView
                = findViewById(R.id.activity_update_and_add_points_of_interest_recycler_view);
        pointsOfInterestEditText = findViewById(R.id.activity_update_and_add_v_edit_text);
        price = findViewById(R.id.activity_update_and_add_price);
        numOfBedrooms = findViewById(R.id.activity_update_and_add_num_of_bedrooms);

        mediaAdd.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        pointsOfIntAdd.setOnClickListener(this);
    }

    private void setParams() {
        if (realEstate != null) {
            updating = true;
            shortDescription.setText(realEstate.getDescription());
            longDescription.setText(realEstate.getLongDescription());
            numOfRooms.setText(String.valueOf(realEstate.getNumberOfRooms()));
            type.setText(realEstate.getType());
            surface.setText(String.valueOf(realEstate.getSurfaceArea()));
        } else {
            updating = false;
            realEstate = new RealEstate();
            realEstate.setPhotos(new ArrayList<String>());
            realEstate.setPointsOfInterest(new ArrayList<String>());
            realEstate.setPointsOfInterest(new ArrayList<String>());
        }
        setMediaRecyclerView();
        setPointsOfInterestRecyclerView();
    }

    private void setMediaRecyclerView() {
        mediaDisplayAdapter = new MediaDisplayAdapter(realEstate.getPhotos(), true
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

    private void setPointsOfInterestRecyclerView() {
        pointsOfInterestAdapter = new PointsOfInterestAdapter(realEstate.getPointsOfInterest());
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                UpdateAndAddActivity.this);
        pointsOfInterestRecyclerView.setLayoutManager(layoutManager);
        pointsOfInterestAdapter.setDeleteItemListener(new DeleteItemListener() {
            @Override
            public void onDeleteIconPress(int position) {
                realEstate.getPointsOfInterest().remove(position);
                pointsOfInterestAdapter.notifyDataSetChanged();
            }
        });
        pointsOfInterestRecyclerView.setAdapter(pointsOfInterestAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.activity_update_and_add_media_add_icon:
                addMedia();
                break;
            case R.id.activity_update_and_add_submit_btn:
                submitRealEstate();
                break;
            case R.id.activity_update_and_add_points_of_interest_add_icon:
                addPointsOfInterest();
                break;
        }
    }

    private void addMedia() {
        String url = media.getText().toString();
        if (url.isEmpty()) {
            Toast.makeText(this, "You must add an url", Toast.LENGTH_SHORT).show();
        } else {
            realEstate.getPhotos().add(url);
            mediaDisplayAdapter.notifyDataSetChanged();
            Objects.requireNonNull(mediaRecyclerView.getLayoutManager())
                    .scrollToPosition(realEstate.getPhotos().size() - 1);
            media.setText("");
        }
    }

    private void addPointsOfInterest() {
        String pointOfInterest = pointsOfInterestEditText.getText().toString();
        if (pointOfInterest.isEmpty()) {
            Toast.makeText(this, "You must add a point of intensest"
                    , Toast.LENGTH_SHORT).show();
        } else {
            realEstate.getPointsOfInterest().add(pointOfInterest);
            pointsOfInterestAdapter.notifyDataSetChanged();
            pointsOfInterestEditText.setText("");
        }
    }

    private void submitRealEstate() {
        if (type.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add a type"
                    , Toast.LENGTH_SHORT).show();
        } else if (realEstate.getPhotos().size() < 1) {
            Toast.makeText(this, "You must add at least one photo"
                    , Toast.LENGTH_SHORT).show();
        } else if (shortDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add a short description"
                    , Toast.LENGTH_SHORT).show();
        } else if (longDescription.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add a long description"
                    , Toast.LENGTH_SHORT).show();
        } else if (surface.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add the surface area"
                    , Toast.LENGTH_SHORT).show();
        } else if (numOfRooms.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add the number of rooms"
                    , Toast.LENGTH_SHORT).show();
        } else if (location.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add the location"
                    , Toast.LENGTH_SHORT).show();
        } else if (realEstate.getPointsOfInterest().size() < 1) {
            Toast.makeText(this, "You must add at least one point of interest"
                    , Toast.LENGTH_SHORT).show();
        } else if (price.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add the price"
                    , Toast.LENGTH_SHORT).show();
        } else if (numOfBedrooms.getText().toString().isEmpty()) {
            Toast.makeText(this, "You must add at least one point of interest"
                    , Toast.LENGTH_SHORT).show();
        } else {
            realEstate.setStatus(AVAILABLE);
            realEstate.setPrice(price.getText().toString());
            realEstate.setNumberOfBedrooms(numOfBedrooms.getText().toString());
            realEstate.setType(type.getText().toString());
            realEstate.setDescription(shortDescription.getText().toString());
            realEstate.setLongDescription(longDescription.getText().toString());
            int surfaceInt = Integer.valueOf(surface.getText().toString());
            int numOfRoomsInt = Integer.valueOf(numOfRooms.getText().toString());
            realEstate.setSurfaceArea(surfaceInt);
            realEstate.setNumberOfRooms(numOfRoomsInt);
            realEstate.setAddress(location.getText().toString());
            if (updating) {
                repository.updateListing(realEstate);
            } else {
                repository.insertListing(realEstate);
            }
            goToNavigationActivity();
        }
    }

    private void goToNavigationActivity() {
        Intent intent = new Intent(UpdateAndAddActivity.this
                , NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
