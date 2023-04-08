package com.example.com3104_project;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import com.example.com3104_project.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class MainActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private ActivityMainBinding binding;
    FusedLocationProviderClient fusedLocationProviderClient;

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 200 * 10 * 1; // 2 seconds Location location;
    private static final int REQUEST_CODE = 100;
    protected LocationManager locationManager;
    private Location location;

    Button bt_getLocStart;
    Button bt_setDest;
    Button bt_go;
    Button bt_swap;
    EditText et_from;
    EditText et_to;


    SupportMapFragment mapFragment;

    double fromLat = 0;
    double fromLon = 0;

    double toLat = 0;
    double toLon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Initial layout display
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        refreshMap();

        bt_getLocStart = findViewById(R.id.bt_getLocStart);
        bt_setDest = findViewById(R.id.bt_setDest);
        bt_swap = findViewById(R.id.bt_swap);

        et_from = findViewById(R.id.et_from);
        et_to = findViewById(R.id.et_to);

        // Button actions
        bt_getLocStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Button", "Get loc. button clicked");
                refreshMap();
                String fromAddr = reverseGeoCode(fromLat, fromLon);
                et_from.setText(fromAddr);
                Log.d("start", "Start loc: "+fromAddr);
                Log.d("from_to", "Start: lan"+fromLat+" lon"+fromLon);
            }
        });

        bt_setDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = et_to.getText().toString();
                Log.d("Button", "Set destination button clicked, you inputed: \""+ search+"\"" );
                geocoder(search);

                String toAddr = reverseGeoCode(toLat, toLon);
                et_to.setText(toAddr);
                Log.d("from_to", "Destination: lan"+toLat+" lon"+toLon);
            }
        });

        // Swap two point lan lon & its address
        bt_swap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });


    }

    private void refreshMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(MainActivity.this);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        getLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation();
            }
        } else {
            Toast.makeText(MainActivity.this, "permission not granted", Toast.LENGTH_LONG).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getLastLocation() {
        locationManager = (LocationManager) MainActivity.this.getSystemService(LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Log.d("Network-GPS", "Disable");
            Toast.makeText(MainActivity.this, "No provider enabled", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                askPermission();
                getLastLocation();
                return;
            }
            Log.d("Network-GPS", "isNetworkEnabled: " + isNetworkEnabled);
            if (isNetworkEnabled) {
                // get loc update
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                        MIN_TIME_BW_UPDATES,
                        MIN_DISTANCE_CHANGE_FOR_UPDATES,
                        (LocationListener) this);
                location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (location != null) {
                    fromLat = location.getLatitude();
                    fromLon = location.getLongitude();
                    Log.d("location", "GPS Provider location obtained, lat " + fromLat + " lon " + fromLon);
                    Toast.makeText(MainActivity.this, "GPS Provider location obtained", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        Log.d("location", "asking for permission...");
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng myLocation = new LatLng(fromLat, fromLon);
        Log.d("location", "showing map now " + fromLat + "::" + fromLon);

        String addr = reverseGeoCode(fromLat, fromLon);         // Convert lan lon to address

        mMap.addMarker(new MarkerOptions().position(myLocation).title("Start"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(myLocation));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 15));
    }

    private String reverseGeoCode(double lat, double lon) {
        String addressText = "";

        try {
            Geocoder geocoder = new Geocoder(this, Locale.getDefault());
            List<Address> addresses = geocoder.getFromLocation(lat, lon, 1);
            if (addresses.size() > 0) {
                Address address = addresses.get(0);
                addressText = address.getAddressLine(0);
                Log.d("reverseGeoCode", "Address found:\n" + addressText);
                return addressText;
            } else {
                Log.d("reverseGeoCode", "Address not found, lan=" + lat + " lon" + lon);
                return null;
//                return "Address not found, lan=" + lat + " lon" + lon;
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("reverseGeoCode", e.toString());
        }
//        return "Address not found, lan=" + lat + " lon" + lon;
        return null;
    }

    public void geocoder(String location) {
        try {
            if (location != null || !location.equals("")) {
                List<Address> addressList = null;

                Geocoder geocoder = new Geocoder(this);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addressList.get(0);
                LatLng latLng = new LatLng(address.getLatitude(), address.getLongitude());
                mMap.addMarker(new MarkerOptions().position(latLng).title("End"));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(latLng));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                toLat = address.getLatitude();
                toLon = address.getLongitude();

                Toast.makeText(MainActivity.this, "Destination found", Toast.LENGTH_LONG).show();
                Log.d("Geocode", "Address: "+address.toString()+"\nlat: " + address.getLatitude() + " lon: " + address.getLongitude());

            }
        }catch (Exception e){
            Toast.makeText(MainActivity.this, "Please enter a destination address", Toast.LENGTH_LONG).show();
            Log.d("Geocode", e.toString());
        }
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }
}