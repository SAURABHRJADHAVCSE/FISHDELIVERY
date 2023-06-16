package com.example.fishdelivery.Admin.Orders;

import static androidx.constraintlayout.helper.widget.MotionEffect.TAG;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.fishdelivery.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class AdminTrackUser extends AppCompatActivity implements OnMapReadyCallback {

    private GoogleMap googleMap;
    private MapView mapView;

    private double latitude;
    private double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_track_user);

        // Get the address information from the intent
        Intent intent = getIntent();
        String latitudeString = intent.getStringExtra("latitude");
        String longitudeString = intent.getStringExtra("longitude");

        // Convert latitude and longitude strings to double
        latitude = Double.parseDouble(latitudeString);
        longitude = Double.parseDouble(longitudeString);

        Log.d("AdminTrackUser", "Latitude: " + latitude + ", Longitude: " + longitude);

        mapView = findViewById(R.id.mapView);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync((OnMapReadyCallback) this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        this.googleMap = googleMap;

        // Set up the marker with the user's location
        LatLng location = new LatLng(latitude, longitude);


        googleMap.addMarker(new MarkerOptions().position(location).title("User Location"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(location, 12f));
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