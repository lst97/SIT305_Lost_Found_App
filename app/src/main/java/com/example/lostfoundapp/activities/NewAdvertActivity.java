package com.example.lostfoundapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.libraries.places.api.Places;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.models.Item;
import com.example.lostfoundapp.repositories.ItemRepository;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class NewAdvertActivity extends AppCompatActivity {
    private RadioGroup radioGroup;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private AutocompleteSupportFragment autocompleteSupportFragment;
    private EditText dateEditText;
    private EditText phoneEditText;
    private Button saveButton;
    private LatLng locationLatLng;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private Button locationButton;
    private String locationName;


    private void initView(){
        radioGroup = findViewById(R.id.post_type_radio_group);
        nameEditText = findViewById(R.id.name_input);
        descriptionEditText = findViewById(R.id.description_input);
        dateEditText = findViewById(R.id.date_input);
        phoneEditText = findViewById(R.id.phone_input);
        saveButton = findViewById(R.id.save_btn);
        autocompleteSupportFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocompleteFragment);
        fusedLocationProviderClient = new FusedLocationProviderClient(this);
        locationButton = findViewById(R.id.current_location_btn);

    }

    private void setPlaceName(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, new Locale("en", "AU"));
        Geocoder.GeocodeListener geocodeListener = new Geocoder.GeocodeListener() {
            @Override
            public void onGeocode(@NonNull List<Address> addresses) {
                addresses.forEach(address -> {
                    locationName = address.getAddressLine(0);
                });
            }

            @Override
            public void onError(@Nullable String errorMessage) {
                Geocoder.GeocodeListener.super.onError(errorMessage);
            }
        };

        geocoder.getFromLocation(latitude, longitude, 1, geocodeListener);

    }
    private void initListener(){

        locationButton.setOnClickListener(v -> {
            if (ContextCompat.checkSelfPermission(NewAdvertActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                fusedLocationProviderClient.getLastLocation().addOnSuccessListener(NewAdvertActivity.this, location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();

                        locationLatLng = new LatLng(latitude, longitude);

                        if (autocompleteSupportFragment != null) {
                            setPlaceName(latitude, longitude);
                            autocompleteSupportFragment.setText(locationName);
                        }
                    } else {
                        Toast.makeText(NewAdvertActivity.this, "Failed to retrieve location", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(NewAdvertActivity.this, "Failed to retrieve location: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
            } else {
                // Request the ACCESS_FINE_LOCATION permission
                ActivityCompat.requestPermissions(NewAdvertActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
            }
        });

        saveButton.setOnClickListener(v -> {

            // data validation
            if (nameEditText.getText().toString().isEmpty()) {
                nameEditText.setError("Name is required");
                return;
            }
            if (descriptionEditText.getText().toString().isEmpty()) {
                descriptionEditText.setError("Description is required");
                return;
            }
            if (autocompleteSupportFragment.getText(autocompleteSupportFragment.getId()).toString().isEmpty()) {
                return;
            }
            if (dateEditText.getText().toString().isEmpty()) {
                dateEditText.setError("Date is required");
                return;
            }
            if (phoneEditText.getText().toString().isEmpty()) {
                phoneEditText.setError("Phone is required");
                return;
            }

            String name = nameEditText.getText().toString();
            String description = descriptionEditText.getText().toString();
            String location = autocompleteSupportFragment.getText(autocompleteSupportFragment.getId()).toString();
            String dateString = dateEditText.getText().toString();
            String phone = phoneEditText.getText().toString();
            String type = radioGroup.getCheckedRadioButtonId() == R.id.lost_radio ? "lost" : "found";

            SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
            Date date = null;
            try {
                date = dateFormat.parse(dateString);
            } catch (Exception e) {
                e.printStackTrace();
            }
            Item item = new Item(0, name, description, location, date, phone, type, locationLatLng.latitude, locationLatLng.longitude);

            ItemRepository itemRepository = ItemRepository.getInstance(new DatabaseHelper(this));
            itemRepository.create(item);

            finish();
        });
    }

    private void initPlaceClient(){
        String PLACE_API_KEY = "AIzaSyCHz4Pnp2P1mSyVCsVPYahBn9jwynns2Qg";
        Places.initialize(getApplicationContext(), PLACE_API_KEY);
        PlacesClient placesClient = Places.createClient(this);


        // Create AutocompleteSupportFragment
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment) getSupportFragmentManager().findFragmentById(R.id.autocompleteFragment);
        assert autocompleteFragment != null;
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.LAT_LNG));

        // Set up PlaceSelectionListener to handle selection
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                // Handle selected place
                NewAdvertActivity.this.locationName = place.getName();
                NewAdvertActivity.this.locationLatLng = place.getLatLng();


                // DEBUG
                Toast.makeText(NewAdvertActivity.this, "Place: " + place.getName() + ", " + locationLatLng.toString(), Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(@NonNull Status status) {
                // Handle error
            }
        });
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_advert);

        initView();
        initPlaceClient();
        initListener();

    }
}
