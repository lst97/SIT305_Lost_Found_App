package com.example.lostfoundapp.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.lostfoundapp.R;
import com.example.lostfoundapp.helpers.DatabaseHelper;
import com.example.lostfoundapp.models.Item;
import com.example.lostfoundapp.repositories.ItemRepository;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class ShowOnMapActivity extends AppCompatActivity {

    private List<LatLng> latLngs = new java.util.ArrayList<>();
    private MapView mapView;

    private void initView(){
        mapView = findViewById(R.id.mapView);
    }

    private void initMap(Bundle savedInstanceState){

        mapView.onCreate(savedInstanceState);
        mapView.onResume();

        ItemRepository itemRepository = ItemRepository.getInstance(new DatabaseHelper(this));
        List<Item> items = itemRepository.read();

        for (Item item : items) {
            latLngs.add(new LatLng(item.getLatitude(), item.getLongitude()));
        }

        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                for (LatLng latLng : ShowOnMapActivity.this.latLngs) {
                    googleMap.addMarker(new MarkerOptions().position(latLng));
                }

                // melbourne
                LatLng initialLocation = new LatLng(-37.8136, 144.9631);
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(initialLocation, 10));
            }
        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_on_map);

        initView();
        initMap(savedInstanceState);
    }
}
