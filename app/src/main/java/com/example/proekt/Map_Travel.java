package com.example.proekt;


import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Map_Travel extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Marker startMarker, destinationMarker;
    private Button btnAddToTravels;
    private DatabaseHelper databaseHelper;

    private Polyline routePolyline;
    private FusedLocationProviderClient fusedLocationProviderClient;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_travel);

        databaseHelper = new DatabaseHelper(this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        btnAddToTravels = findViewById(R.id.btnAddToTravels);
        btnAddToTravels.setVisibility(Button.GONE); //pocetno sokrieno po postavuvanjeto na krajnata lokacija kje se pojavi

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        if (mapFragment != null) {
            mapFragment.getMapAsync(this);
        }

        // kopce za zacuvuvanje vo moi patuvanja
        btnAddToTravels.setOnClickListener(v -> saveTravelDestination());
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // proverka i baranje dozvola za lokacija
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        } else {
            enableUserLocation();
        }

        // dodavanje marker za krajna destinacija so klik na mapata
        mMap.setOnMapClickListener(latLng -> {
            if (destinationMarker != null) {
                destinationMarker.remove();
            }
            destinationMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Final destination"));
            btnAddToTravels.setVisibility(Button.VISIBLE); // Prikazi go kopceto za dodavanje vo moi patuvanja
            drawRoute(startMarker.getPosition(), destinationMarker.getPosition());
        });
    }

    private void drawRoute(LatLng start, LatLng end) {

        if (routePolyline != null) {
            routePolyline.remove();
        }

        // iscrtuvanje linija pomegju dvete tocki
        routePolyline = mMap.addPolyline(new PolylineOptions()
                .add(start, end)
                .width(10)
                .color(Color.BLUE)
                .geodesic(true));
    }

    private void enableUserLocation() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    LatLng userLocation = new LatLng(location.getLatitude(), location.getLongitude());
                    if (startMarker != null) {
                        startMarker.remove();
                    }
                    startMarker = mMap.addMarker(new MarkerOptions().position(userLocation).title("Current location"));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(userLocation, 15));


                    if (destinationMarker != null) {
                        drawRoute(startMarker.getPosition(), destinationMarker.getPosition());
                    }
                }
            });
        }
    }

    private void saveTravelDestination() {
        if (destinationMarker == null) {
            Toast.makeText(this, "Set a final destination!", Toast.LENGTH_SHORT).show();
            return;
        }

        double destLat = destinationMarker.getPosition().latitude;
        double destLng = destinationMarker.getPosition().longitude;

        String currentUser = getCurrentUser();

        String placeName = databaseHelper.getNearestPlaceName(destLat, destLng);
        if (placeName == null) {
            placeName = "Unknown location";
        }

        boolean saved = databaseHelper.saveTravel(currentUser, placeName, destLat, destLng);
        if (saved) {
            Toast.makeText(this, "The trip has been saved:" + placeName, Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Error while saving!", Toast.LENGTH_SHORT).show();
        }
    }

    private String getCurrentUser() {
        return getSharedPreferences("UserSession", MODE_PRIVATE).getString("username", "Unknown user");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                enableUserLocation();
            } else {
                Toast.makeText(this, "Location permission denied!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
