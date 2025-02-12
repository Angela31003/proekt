package com.example.proekt;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class PlaceDetailActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_detail);

        TextView txtDescription = findViewById(R.id.txt_place_description);
        ImageView imgPlace = findViewById(R.id.img_place);


        String description = getIntent().getStringExtra("place_description");
        int imageResource = getIntent().getIntExtra("place_image", R.drawable.kale);


        txtDescription.setText(description);
        imgPlace.setImageResource(imageResource);
    }
}
