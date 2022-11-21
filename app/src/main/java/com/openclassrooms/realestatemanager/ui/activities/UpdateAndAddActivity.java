package com.openclassrooms.realestatemanager.ui.activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.openclassrooms.realestatemanager.utils.Utils;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import static com.openclassrooms.realestatemanager.adapters.PointsOfInterestAdapter.DeleteItemListener;
import static com.openclassrooms.realestatemanager.utils.Constants.BundleKeys.BUNDLE_CURRENCY_KEY;
import static com.openclassrooms.realestatemanager.utils.Constants.Status.AVAILABLE;
import static com.openclassrooms.realestatemanager.utils.Constants.Status.SOLD;
import static com.openclassrooms.realestatemanager.utils.Utils.convertDollarToEuro;
import static com.openclassrooms.realestatemanager.utils.Utils.convertEuroToDollar;
import static com.openclassrooms.realestatemanager.utils.Utils.formatDate;
import static com.openclassrooms.realestatemanager.utils.Utils.getTodayDate;

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
    private EditText agenteResposible;
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
    private TextView priceTextView;
    private String currency;

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
        setCurrency();
        setParams();
        setClickListneres();
    }

    private void setViews() {
        priceTextView = findViewById(R.id.activity_update_and_add_price_text_view);
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
        agenteResposible = findViewById(R.id.activity_update_and_add_agent_responsible);
        mediaAdd.setOnClickListener(this);
        submitBtn.setOnClickListener(this);
        pointsOfIntAdd.setOnClickListener(this);
        selectPicFromInternalStorageBtn.setOnClickListener(this);
    }

    private void setCurrency() {
        currency = getIntent().getStringExtra(BUNDLE_CURRENCY_KEY);
        if (currency == null) {
            currency = Utils.getCurrency(UpdateAndAddActivity.this);
        }
        if (currency.equals(Constants.Currencies.EURO)) {
            priceTextView.setText(getString(R.string.price_in_euros));
        }
    }

    private void setClickListneres() {
        soldRadio.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    dateContainer.setVisibility(View.VISIBLE);
                    soldDateTextView.setText(getTodayDate());
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

    private void setParams() {
        if (realEstate != null) {
            updating = true;
            shortDescription.setText(realEstate.getDescription());
            longDescription.setText(realEstate.getLongDescription());
            int numberOfRooms = realEstate.getNumberOfRooms();
            if (numberOfRooms > 0) {
                numOfRooms.setText(String.valueOf(numberOfRooms));
            }

            type.setText(realEstate.getType());
            int surfaceArea = realEstate.getSurfaceArea();
            if (surfaceArea > 0) {
                surface.setText(String.valueOf(surfaceArea));
            }
            try {
                String priceString = realEstate.getPrice();
                int priceInt = Integer.valueOf(priceString);
                if (priceInt > 0) {
                    if (currency.equals(Constants.Currencies.EURO)) {
                        priceString = String.valueOf(convertDollarToEuro(priceInt));
                    }
                    price.setText(priceString);
                }
            } catch (Exception ignored) {
            }

            agenteResposible.setText(realEstate.getAgent());
            location.setText(realEstate.getAddress());
            int numbOfBedRooms = realEstate.getNumbOfBedRooms();
            if (numbOfBedRooms > 0) {
                numOfBedrooms.setText(String.valueOf(numbOfBedRooms));
            }
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
            Toast.makeText(this, getString(R.string.you_must_add_a_point_of_int)
                    , Toast.LENGTH_SHORT).show();
        } else {
            realEstate.getPointsOfInterest().add(pointOfInterest);
            pointsOfInterestAdapter.notifyDataSetChanged();
            pointsOfInterestEditText.setText("");
        }
    }

    private void submitRealEstate() {
        List<String> photos = realEstate.getPhotos();
        if (photos == null || photos.size() < 1) {
            Toast.makeText(getBaseContext(), getString(R.string.you_must_add_at_least)
                    , Toast.LENGTH_SHORT).show();
        } else {
            try {
                realEstate.setDatePutInMarket(System.currentTimeMillis());
                if (soldRadio.isChecked()) {
                    realEstate.setSaleData(soldDate);
                    realEstate.setStatus(SOLD);
                } else {
                    realEstate.setStatus(AVAILABLE);
                }

                String priceString = price.getText().toString();
                if (currency.equals(Constants.Currencies.EURO)) {
                    try {
                        priceString = String.valueOf(convertEuroToDollar(Integer.valueOf(priceString)));
                    } catch (Exception e) {
                        priceString = "";
                    }
                }
                realEstate.setPrice(priceString);
                Integer numOfBedInt;
                try {
                    numOfBedInt = Integer.valueOf(numOfBedrooms.getText().toString());
                } catch (Exception ignored) {
                    numOfBedInt = -1;
                }
                realEstate.setNumbOfBedRooms(numOfBedInt);
                realEstate.setType(type.getText().toString());
                realEstate.setDescription(shortDescription.getText().toString());
                realEstate.setLongDescription(longDescription.getText().toString());
                int surfaceInt;
                try {
                    surfaceInt = Integer.valueOf(surface.getText().toString());
                } catch (Exception e) {
                    surfaceInt = -1;
                }
                int numOfRoomsInt;
                try {
                    numOfRoomsInt = Integer.valueOf(numOfRooms.getText().toString());
                } catch (Exception e) {
                    numOfRoomsInt = -1;
                }
                int numOfBedroomsInt;
                try {
                    numOfBedroomsInt = numOfBedInt;
                } catch (Exception e) {
                    numOfBedroomsInt = -1;
                }
                realEstate.setNumbOfBedRooms(numOfBedroomsInt);
                realEstate.setSurfaceArea(surfaceInt);
                realEstate.setNumberOfRooms(numOfRoomsInt);
                realEstate.setAgent(agenteResposible.getText().toString());
                realEstate.setAddress(location.getText().toString());
                if (updating) {
                    String title = realEstate.getDescription() + " " + getString(R.string.updated);
                    String message = getString(R.string.roomie_updated);
                    Utils.createNotification(getApplicationContext(), title, message);
                    repository.updateListing(realEstate);
                } else {
                    String title = realEstate.getDescription() + " " + getString(R.string.added);
                    String message = getString(R.string.roomie_added);
                    Utils.createNotification(getApplicationContext(), title, message);
                    repository.insertListing(realEstate);
                }
                goToNavigationActivity();
            } catch (Exception e) {
                Toast.makeText(UpdateAndAddActivity.this, getString(R.string.error)
                        , Toast.LENGTH_SHORT).show();
            }
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
