package com.example.fishdelivery.Admin.Orders;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.fishdelivery.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class AdminTrackUser extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;

    private double latitude;
    private double longitude;

    private String address;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_track_user);

        // Get the address information from the intent
        Intent intent = getIntent();
        String latitudeString = intent.getStringExtra("latitude");
        String longitudeString = intent.getStringExtra("longitude");

        address = intent.getStringExtra("address");

        // Log the address
        Log.d("AdminTrackUser", "Address: " + address);

        if (latitudeString != null && longitudeString != null) {
            latitude = Double.parseDouble(latitudeString);
            longitude = Double.parseDouble(longitudeString);
        } else {
            Toast.makeText(this, "Hello", Toast.LENGTH_SHORT).show();
        }
        Log.d("AdminTrackUser", "Latitude: " + latitude + ", Longitude: " + longitude);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        googleMap.clear(); // Clear previous markers
        // Set up the marker with the user's location
        LatLng location = new LatLng(latitude, longitude);


        googleMap.addMarker(new MarkerOptions().position(location).title("User Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));


        if (address != null && !address.isEmpty()) {
            searchLocation(address);
        } else {
            Toast.makeText(this, "Invalid address", Toast.LENGTH_SHORT).show();
        }
    }

    private void searchLocation(String query) {
        googleMap.clear(); // Clear previous markers

        // Use Geocoder to get the location from the address
        Geocoder geocoder = new Geocoder(this);
        try {
            List<Address> addresses = geocoder.getFromLocationName(query, 1);
            if (!addresses.isEmpty()) {
                Address address = addresses.get(0);
                LatLng location = new LatLng(address.getLatitude(), address.getLongitude());
                googleMap.addMarker(new MarkerOptions().position(location).title("Search Location"));
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
            } else {
                Toast.makeText(this, "Location not found", Toast.LENGTH_SHORT).show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
}