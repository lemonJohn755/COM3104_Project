package com.example.com3104_project;

import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.com3104_project.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.Marker;


public class MainActivity extends AppCompatActivity {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 200 * 10 * 1; // 2 seconds Location location;
    private static final int REQUEST_CODE = 100;
    protected LocationManager locationManager;
    private Location location;

    ImageButton imgbt_getGPSStart;
    ImageButton imgbt_searchLocStart;
    ImageButton imgbt_setDest;

    Button bt_go;
    Button bt_swap;
    Button bt_bus;
    Button bt_gmb;

    EditText et_from;
    EditText et_to;

    float ZOOM = 15;


    SupportMapFragment mapFragment;

    Marker startMarker;     // Start marker on map
    Marker destMarker;      // Destination market on map

    double fromLat = 0;
    double fromLon = 0;

    double toLat = 0;
    double toLon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Hide title bar
//        requestWindowFeature(Window.FEATURE_NO_TITLE);
//        getSupportActionBar().hide();


        // Initial layout display
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new TransitTab());

        binding.navBar.setOnItemSelectedListener(items ->{
            switch (items.getItemId()){

                case R.id.item_transit:
                    replaceFragment(new TransitTab());
                    break;
                case R.id.item_nearby:
                    replaceFragment(new NearbyTab());
                    break;

            }
            return true;
        });


    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.framelayout, fragment);
        fragmentTransaction.commit();

    }


}