package com.example.proekt;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class TravelsActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    private TravelAdapter travelAdapter;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_travels2);

        recyclerView = findViewById(R.id.recyclerViewTravels);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        dbHelper = new DatabaseHelper(this);
        String currentUser = getCurrentUser();

        if (currentUser == null) {
            Toast.makeText(this, "Error: You are not logged in!", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }


        List<String> travelList = dbHelper.getUserTravels(currentUser);
        travelAdapter = new TravelAdapter(travelList);
        recyclerView.setAdapter(travelAdapter);

    }

    private String getCurrentUser() {
        SharedPreferences prefs = getSharedPreferences("UserSession", MODE_PRIVATE);
        return prefs.getString("username", "Unknown user");
    }
}
