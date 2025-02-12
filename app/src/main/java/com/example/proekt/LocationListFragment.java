package com.example.proekt;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class LocationListFragment extends Fragment {

    private OnLocationSelectedListener listener;

    public interface OnLocationSelectedListener {
        void onLocationSelected(String location);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnLocationSelectedListener) {
            listener = (OnLocationSelectedListener) context;
        } else {
            throw new RuntimeException(context.toString() + " мора да имплементира OnLocationSelectedListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);
        ListView listView = view.findViewById(R.id.listViewLocations);

        final String[] locations = {"Fortress Kale", "The Old Bazaar", "Archeological Museum", "Millenium Cross"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, locations);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener((AdapterView<?> parent, View view1, int position, long id) -> {
            if (listener != null) {
                listener.onLocationSelected(locations[position]);
            }
        });

        return view;
    }
}
