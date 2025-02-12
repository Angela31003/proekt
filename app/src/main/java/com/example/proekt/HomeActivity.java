
package com.example.proekt;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.HashMap;
import java.util.Map;

public class HomeActivity extends AppCompatActivity implements LocationListFragment.OnLocationSelectedListener {

    private ImageView imgPlace;
    private RadioGroup radioGroupPlaces;
    private final Map<Integer, Integer> placeImages = new HashMap<>();
    private final Map<Integer, String> placeDescriptions = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Проверка дали е landscape или portrait
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            setContentView(R.layout.activity_home);
            setupFragments();
        } else {
            setContentView(R.layout.activity_home);
            setupPortraitMode();
        }
    }

    private void setupPortraitMode() {
        imgPlace = findViewById(R.id.img_place);
        radioGroupPlaces = findViewById(R.id.radioGroupPlaces);
        Button btnEnterLocation = findViewById(R.id.btn_enter_location);
        Button btnMyTravels = findViewById(R.id.btn_my_travels);

        // Додавање на слики и описи
        placeImages.put(R.id.radio_kale, R.drawable.kale);
        placeImages.put(R.id.radio_carsija, R.drawable.carsija);
        placeImages.put(R.id.radio_museum, R.drawable.muzej);
        placeImages.put(R.id.radio_millennum, R.drawable.vodno);

        placeDescriptions.put(R.id.radio_kale, "As the highest point in the center of the city, this location has always had a strategic role. The land has been inhabited since Neolithic times, and the first fortress at this location was built in the 6th century. Destroyed and rebuilt by many conquerors, the fortress Kale offers countless stories from the Byzantine and Ottoman times, as well as a beautiful view of the city.");
        placeDescriptions.put(R.id.radio_carsija, "Pass through the Stone Bridge and enter a different world. Located on the eastern bank of the Vardar River, the Old Skopje Bazaar reflects centuries-old culture influenced by the Ottoman Empire, and abounds with small shops and artisan workshops. It’s easy to get lost in the intertwined streets paved with cobblestones. There is something for everyone in the Bazaar, and it is also the best place to buy souvenirs from Skopje.");
        placeDescriptions.put(R.id.radio_museum, "Located on the left bank of the Vardar River, the museum is a sight to behold from both the outside and the inside. Built in 2012, the museum is home to around 7,000 archaeological artifacts found on the territory of North Macedonia, dating from the Stone Age to the Middle Ages. A large number of the items that are part of this institution are exclusive on a global scale. For all museum fans, this museum is a must-visit attraction.");
        placeDescriptions.put(R.id.radio_millennum, "This 66-meter-high giant lives on the top of Vodno Mountain. As the name suggests, it was built in honor of the second millennium that we celebrated about 20 years ago, and to this day it is the 6th largest cross in the world. Go to the top of Vodno and climb up to the cross itself with an elevator to see Skopje from a perspective you haven’t seen before");

        radioGroupPlaces.setOnCheckedChangeListener((group, checkedId) -> {
            if (placeImages.containsKey(checkedId)) {
                imgPlace.setImageResource(placeImages.get(checkedId));
            }
        });

        imgPlace.setOnClickListener(v -> {
            int selectedId = radioGroupPlaces.getCheckedRadioButtonId();
            if (placeDescriptions.containsKey(selectedId)) {
                Intent intent = new Intent(HomeActivity.this, PlaceDetailActivity.class);
                intent.putExtra("place_description", placeDescriptions.get(selectedId));
                intent.putExtra("place_image", placeImages.get(selectedId));
                HomeActivity.this.startActivity(intent);
            } else {
                Toast.makeText(HomeActivity.this, " Select a place to see details!", Toast.LENGTH_SHORT).show();
            }
        });

        btnEnterLocation.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, Map_Travel.class);
            HomeActivity.this.startActivity(intent);
        });


        btnMyTravels.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, TravelsActivity.class);
            HomeActivity.this.startActivity(intent);
        });

    }

    private void setupFragments() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        LocationListFragment listFragment = new LocationListFragment();
        transaction.replace(R.id.fragment_list, listFragment);

        LocationDetailFragment detailFragment = new LocationDetailFragment();
        transaction.replace(R.id.fragment_detail, detailFragment);

        transaction.commit();
    }

    @Override
    public void onLocationSelected(String location) {
        int imageResId;
        String description;

        switch (location) {
            case "Fortress Kale":
                imageResId = R.drawable.kale;
                description ="As the highest point in the center of the city, this location has always had a strategic role. The land has been inhabited since Neolithic times, and the first fortress at this location was built in the 6th century.";
                break;
            case "The Old Bazaar":
                imageResId = R.drawable.carsija;
                description = "Located on the eastern bank of the Vardar River,the Old Skopje Bazaar reflects centuries-old culture influenced by the Ottoman Empire,and abounds with small shops and artisan workshops.";
                break;
            case "Archeological Museum":
                imageResId = R.drawable.muzej;
                description = " the museum is home to around 7,000 archaeological artifacts found on the territory of North Macedonia, dating from the Stone Age to the Middle Ages.";
                break;
            case "Millenium Cross":
                imageResId = R.drawable.vodno;
                description = "This 66-meter-high giant lives on the top of Vodno Mountain. It was built in honor of the second millennium that we celebrated about 20 years ago,and to this day it is the 6th largest cross in the world.";
                break;
            default:
                imageResId = R.drawable.placeholder;
                description = "Изберете локација за детали.";
        }

        LocationDetailFragment detailFragment = (LocationDetailFragment) getSupportFragmentManager()
                .findFragmentById(R.id.fragment_detail);

        if (detailFragment != null) {
            detailFragment.updateDetails(imageResId, description);
        }
    }
}

