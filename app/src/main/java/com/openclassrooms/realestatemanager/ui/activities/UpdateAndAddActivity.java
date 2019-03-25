package com.openclassrooms.realestatemanager.ui.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.EditText;
import android.widget.ImageView;

import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaDisplayAdapter;
import com.openclassrooms.realestatemanager.adapters.RealEstateAdapter;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;

public class UpdateAndAddActivity extends AppCompatActivity {

    private RealEstate realEstate;
    private EditText shortDescription;
    private EditText longDescription;
    private EditText media;
    private EditText type;
    private EditText numOfRooms;
    private EditText surface;
    private ImageView mediaAdd;
    private RecyclerView mediaRecyclerView;
    private MediaDisplayAdapter mediaDisplayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_add);

        realEstate = getIntent().getParcelableExtra(Utils.BundleKeys.REAL_ESTATE_OBJECT_KEY);
        setViews();
        setParams();
    }

    private void setParams() {
        if (realEstate!=null) {
            shortDescription.setText(realEstate.getDescription());
            longDescription.setText(realEstate.getLongDescription());
            numOfRooms.setText(String.valueOf(realEstate.getNumberOfRooms()));
            type.setText(realEstate.getType());
            surface.setText(String.valueOf(realEstate.getSurfaceArea()));

            setMediaRecyclerView();
        }
    }

    private void setMediaRecyclerView() {
        mediaDisplayAdapter = new MediaDisplayAdapter(realEstate.getPhotos(), true
                , getApplicationContext());
        LinearLayoutManager layoutManager = new LinearLayoutManager(this
                , LinearLayoutManager.HORIZONTAL, false);
        mediaRecyclerView.setLayoutManager(layoutManager);
        mediaRecyclerView.setAdapter(mediaDisplayAdapter);
    }

    private void setViews() {
        shortDescription = findViewById(R.id.activity_update_and_add_location);
        longDescription = findViewById(R.id.activity_update_and_add_long_description_edit_text);
        mediaAdd = findViewById(R.id.activity_update_and_add_media_add_icon);
        mediaRecyclerView = findViewById(R.id.activity_update_and_add_media_recycler_view);
        media = findViewById(R.id.activity_update_and_add_media_edit_text);
        type = findViewById(R.id.activity_update_and_add_type_edit_text);
        numOfRooms = findViewById(R.id.activity_update_and_add_num_of_rooms_edit_text);
        surface = findViewById(R.id.activity_update_and_add_surface_edit_text);
    }
}
