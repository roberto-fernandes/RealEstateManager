package com.openclassrooms.realestatemanager.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.openclassrooms.realestatemanager.R;
import com.openclassrooms.realestatemanager.adapters.MediaDisplayAdapter;
import com.openclassrooms.realestatemanager.adapters.PointsOfInterestAdapter;
import com.openclassrooms.realestatemanager.model.RealEstate;
import com.openclassrooms.realestatemanager.repository.Repository;
import com.openclassrooms.realestatemanager.ui.fragments.DatePickerFragment;
import com.openclassrooms.realestatemanager.utils.Constants;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Objects;
import java.util.UUID;

import static com.openclassrooms.realestatemanager.adapters.PointsOfInterestAdapter.DeleteItemListener;
import static com.openclassrooms.realestatemanager.utils.Constants.Status.AVAILABLE;
import static com.openclassrooms.realestatemanager.utils.Constants.Status.SOLD;
import static com.openclassrooms.realestatemanager.utils.Utils.formatDate;

public class UpdateAndAddActivity extends AppCompatActivity
        implements View.OnClickListener, DatePickerDialog.OnDateSetListener {

    private static final int PICK_IMAGE_REQUEST = 1;

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
    private RecyclerView mediaRecyclerView;
    private MediaDisplayAdapter mediaDisplayAdapter;
    private PointsOfInterestAdapter pointsOfInterestAdapter;
    private RecyclerView pointsOfInterestRecyclerView;
    private EditText pointsOfInterestEditText;
    private Repository repository;
    private View dateContainer;
    private TextView soldDateTextView;
    private long soldDate;
    private boolean updating;
    private RadioButton soldRadio;
    private RadioButton availableRadio;
    private Uri filePath;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_and_add);

        FirebaseStorage storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        setToolbar();
        realEstate = getIntent().getParcelableExtra(Constants.BundleKeys.REAL_ESTATE_OBJECT_KEY);
        repository = new Repository(UpdateAndAddActivity.this);
        setViews();
        setParams();
        setClickListneres();
    }

    private void setClickListneres() {
        soldRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateContainer.setVisibility(View.VISIBLE);
                    soldDateTextView.setText(formatDate(System.currentTimeMillis()));
                } else {
                    dateContainer.setVisibility(View.GONE);
                }
            }
        });

        soldDateTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "date picker");
            }
        });
    }

    private void setToolbar() {
        Toolbar toolbar = findViewById(R.id.update_and_add_toolbar);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void setViews() {
        ImageView pointsOfIntAdd = findViewById(R.id.activity_update_and_add_points_of_interest_add_icon);
        shortDescription = findViewById(R.id.activity_update_and_add_short_description_edit_text);
        longDescription = findViewById(R.id.activity_update_and_add_long_description_edit_text);
        ImageView mediaAdd = findViewById(R.id.activity_update_and_add_media_add_icon);
        mediaRecyclerView = findViewById(R.id.activity_update_and_add_media_recycler_view);
        media = findViewById(R.id.activity_update_and_add_media_edit_text);
        type = findViewById(R.id.activity_update_and_add_type_edit_text);
        numOfRooms = findViewById(R.id.activity_update_and_add_num_of_rooms_edit_text);
        surface = findViewById(R.id.activity_update_and_add_surface_edit_text);
        location = findViewById(R.id.activity_update_and_add_location);
        Button submitBtn = findViewById(R.id.activity_update_and_add_submit_btn);
        pointsOfInterestRecyclerView
                = findViewById(R.id.activity_update_and_add_points_of_interest_recycler_view);
        pointsOfInterestEditText = findViewById(R.id.activity_update_and_add_v_edit_text);
        price = findViewById(R.id.activity_update_and_add_price);
        numOfBedrooms = findViewById(R.id.activity_update_and_add_num_of_bedrooms);
        soldRadio = findViewById(R.id.activity_update_and_add_sold_radio);
        availableRadio = findViewById(R.id.activity_update_and_add_available_radio);
        soldDateTextView = findViewById(R.id.activity_update_and_add_sold_date);
        dateContainer = findViewById(R.id.activity_update_and_add_sold_date_container);
        Button selectPicFromInternalStorageBtn = findViewById(R.id
                .activity_update_and_add_picture_from_storage);

        mediaAdd.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        pointsOfIntAdd.setOnClickListener(this);
        selectPicFromInternalStorageBtn.setOnClickListener(this);
    }

    private void setParams() {
        if (realEstate != null) {
            updating = true;
            shortDescription.setText(realEstate.getDescription());
            longDescription.setText(realEstate.getLongDescription());
            numOfRooms.setText(String.valueOf(realEstate.getNumberOfRooms()));
            type.setText(realEstate.getType());
            surface.setText(String.valueOf(realEstate.getSurfaceArea()));
            price.setText(realEstate.getPrice());
            location.setText(realEstate.getAddress());
            numOfBedrooms.setText(String.valueOf(realEstate.getNumbOfBedRooms()));
            if (realEstate.getStatus().equals(SOLD)) {
                soldRadio.setChecked(true);
                availableRadio.setChecked(false);
                dateContainer.setVisibility(View.VISIBLE);
                soldDateTextView.setText(formatDate(realEstate.getSaleData()));
            }
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
                addMedia(media.getText().toString());
                break;
            case R.id.activity_update_and_add_submit_btn:
                submitRealEstate();
                break;
            case R.id.activity_update_and_add_points_of_interest_add_icon:
                addPointsOfInterest();
                break;
            case R.id.activity_update_and_add_picture_from_storage:
                chooseImage();
                break;
        }
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            filePath = data.getData();
            uploadImage();
        }
    }

    private void addMedia(String url) {
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

    private void uploadImage() {
        if (filePath != null) {
            final StorageReference ref = storageReference.child("images/" + UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            ref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    addMedia(uri.toString());
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), "Failed " + e.getMessage()
                                    , Toast.LENGTH_SHORT).show();
                        }
                    });

        }
    }

    private void addPointsOfInterest() {
        String pointOfInterest = pointsOfInterestEditText.getText().toString();
        if (pointOfInterest.isEmpty()) {
            Toast.makeText(this, "You must add a point of interest"
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
            realEstate.setDatePutInMarket(System.currentTimeMillis());
            if (soldRadio.isChecked()) {
                realEstate.setSaleData(soldDate);
                realEstate.setStatus(SOLD);
            } else {
                realEstate.setStatus(AVAILABLE);
            }
            realEstate.setPrice(price.getText().toString());
            realEstate.setDepreciatedVal2(numOfBedrooms.getText().toString());
            realEstate.setType(type.getText().toString());
            realEstate.setDescription(shortDescription.getText().toString());
            realEstate.setLongDescription(longDescription.getText().toString());
            int surfaceInt = Integer.valueOf(surface.getText().toString());
            int numOfRoomsInt = Integer.valueOf(numOfRooms.getText().toString());
            int numOfBedroomsInt = Integer.valueOf(numOfBedrooms.getText().toString());
            realEstate.setNumbOfBedRooms(numOfBedroomsInt);
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

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String currentDateString = formatDate(c);
        soldDate = c.getTimeInMillis();
        soldDateTextView.setText(currentDateString);
    }
}
