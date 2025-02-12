package com.example.proekt;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LocationDetailFragment extends Fragment {
    private ImageView imageView;
    private TextView textView;

    public LocationDetailFragment() {}

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_detail, container, false);
        imageView = view.findViewById(R.id.imgLocation);
        textView = view.findViewById(R.id.txtDescription);
        return view;
    }

    public void updateDetails(int imageResId, String description) {
        if (imageView != null && textView != null) {
            imageView.setImageResource(imageResId);
            textView.setText(description);
        }
    }
}
