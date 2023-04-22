package com.example.com3104_project;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.example.com3104_project.databinding.ActivityMainBinding;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class TransitTab extends Fragment implements OnMapReadyCallback, LocationListener{

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

    View view;
    Context context;

    String fromAddr = "";
    String toAddr = "";

    private static final int EARTH_RADIUS = 6371000; // in meters

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_transit_tab, container, false);
        context = view.getContext();

        getLastLocation();
        refreshMap();

        imgbt_getGPSStart = view.findViewById(R.id.imgbt_getGPSStart);
        imgbt_searchLocStart = view.findViewById(R.id.imgbt_searchLocStart);
        imgbt_setDest = view.findViewById(R.id.imgbt_setDest);

        et_from = view.findViewById(R.id.et_from);

        fromAddr = reverseGeoCode(fromLat, fromLon);
        et_from.setText(fromAddr);

        et_to = view.findViewById(R.id.et_to);

        bt_bus = view.findViewById(R.id.bt_bus);

        bt_gmb = view.findViewById(R.id.bt_gmb);

        bt_go = view.findViewById(R.id.bt_go);

        // Get current loc button with GPS
        imgbt_getGPSStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("Button", "Get loc button clicked");
                refreshMap();
                String fromAddr = reverseGeoCode(fromLat, fromLon);
                et_from.setText(fromAddr);
                Log.d("start", "Start loc: "+fromAddr);
                Log.d("from_to", "Start: lan, long => "+fromLat+", "+fromLon);
            }
        });

        imgbt_searchLocStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String startLoc = et_from.getText().toString();     // Start location
                Log.d("Button", "Search loc start button clicked, inputed: "+ startLoc);

                // Get lan lon from startLoc
                Map coordinates = Geocoder(startLoc);
                fromLat = (double) coordinates.get("lat");
                fromLon = (double) coordinates.get("lon");

                // Put a marker for start loc
                LatLng loc = new LatLng(fromLat, fromLon);


                if ( startMarker != null){
                    startMarker.remove();
                }

                startMarker = mMap.addMarker(new MarkerOptions().position(loc).title("Start"));

                mMap.animateCamera(CameraUpdateFactory.newLatLng(loc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM));

                // Display destination address into et_from edittext
                fromAddr = reverseGeoCode(fromLat, fromLon);
                et_from.setText(fromAddr);

                Log.d("from_to", "Start: lan, long => "+fromLat+", "+fromLon);

            }
        });

        imgbt_setDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = et_to.getText().toString();
                Log.d("Button", "Set destination button clicked, you inputed: \""+ search+"\"" );

                // Get lan lon from startLoc
                Map coordinates = Geocoder(search);
                toLat = (double) coordinates.get("lat");
                toLon = (double) coordinates.get("lon");

                // Put a marker for start loc
                LatLng toloc = new LatLng(toLat, toLon);

                if(destMarker != null){
                    destMarker.remove();
                }

                destMarker = mMap.addMarker(new MarkerOptions().position(toloc).title("Destination")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.chequered_flag)));
                mMap.animateCamera(CameraUpdateFactory.newLatLng(toloc));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toloc, ZOOM));

                // Display destination address into et_from edittext
                toAddr = reverseGeoCode(toLat, toLon);
                et_to.setText(toAddr);

                Log.d("from_to", "Destination: lan, long => "+toLat+", "+toLon);
            }
        });

        bt_bus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, BusSearchActivity.class);

                // start the activity connect to the specified class
                startActivity(intent);
            }
        });

        bt_gmb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, GMBActivity.class);

                // start the activity connect to the specified class
                startActivity(intent);
            }
        });

        bt_go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (fromAddr.equals("") || toAddr.equals("")) {     // if start & dest fields are blank
                        Toast.makeText(context, "Please input the start and destination", Toast.LENGTH_SHORT).show();
                    }else if ( getDistance(fromLat, fromLon, toLat, toLon) < 10){ // if start & dest fields are too short, less than 10m
                        Toast.makeText(context, "Distance too short between two points \nMinimum distance is 10m",Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Log.d("startDestLoc", "From:" + fromAddr + ", To:" + toAddr +
                                "\nfrom(lan,lon): " + fromLat + " ," + fromLon +
                                "\nto(lan, lon):" + toLat + " ," + toLon);

                        // Save the info of start & dest loc into startDestLoc obj
                        StartDestLoc startDestLoc = new StartDestLoc(fromLat, fromLon, toLat, toLon, fromAddr, toAddr);

                        // Switch to TransitSuggestActivity
                        Intent intent = new Intent(view.getContext(), TransitSuggestActivity.class);
                        intent.putExtra("startDestLoc", startDestLoc);
                        startActivity(intent);

                    }
                }catch (Exception e){
                    Toast.makeText(context, "Please input the start and destination", Toast.LENGTH_SHORT).show();
                }
            }
        });



        return view;
    }

    private double getDistance(double startLat, double startLon, double endLat, double endLon) {

        double dLat = Math.toRadians(endLat - startLat);
        double dLon = Math.toRadians(endLon - startLon);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(startLat)) * Math.cos(Math.toRadians(endLat)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    private void refreshMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        getLastLocation();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == REQUEST_CODE) {
            if ((grantResults.length > 0) && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                getLastLocation();
            }
        } else {
            Toast.makeText(context, "permission not granted", Toast.LENGTH_LONG).show();
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void getLastLocation() {
        locationManager = (LocationManager) context.getSystemService(context.LOCATION_SERVICE);
        boolean isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        boolean isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (!isGPSEnabled && !isNetworkEnabled) {
            // no network provider is enabled
            Log.d("Network-GPS", "Disable");
            Toast.makeText(context, "No provider enabled", Toast.LENGTH_SHORT).show();
        } else {
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                askPermission();
//                getLastLocation();
                return;
            }
            Log.d("Network-GPS", "isNetworkEnabled: " + isNetworkEnabled);
            if (isNetworkEnabled) {

                // get loc update
                if (locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)){
                    locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            (LocationListener) this);
                    location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
                    Toast.makeText(getContext(), "Get location from network provider", Toast.LENGTH_SHORT).show();
                }else{
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
                            MIN_TIME_BW_UPDATES,
                            MIN_DISTANCE_CHANGE_FOR_UPDATES,
                            (LocationListener) this);
                    location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                    Toast.makeText(getContext(), "Get location from GPS provider", Toast.LENGTH_SHORT).show();
                }

                if (location != null) {
                    fromLat = location.getLatitude();
                    fromLon = location.getLongitude();
                    Log.d("location", "location obtained, lat " + fromLat + " lon " + fromLon);
//                    Toast.makeText(context, "GPS Provider location obtained", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
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
        if(mMap != null){
            mMap.clear();
        }
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(fromLat, fromLon);
        Log.d("location", "showing map now " + fromLat + "::" + fromLon);

        fromAddr = reverseGeoCode(fromLat, fromLon);         // Convert lan lon to address

//        startMarker = mMap.addMarker(new MarkerOptions().position(myLocation).title("Start"));
        mMap.addMarker(new MarkerOptions().position(loc).title("You're here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM));

        mMap.getUiSettings().setZoomControlsEnabled(false);
    }

    private String reverseGeoCode(double lat, double lon) {
        String addressText = "";

        try {
            Geocoder geocoder = new Geocoder(context, Locale.getDefault());
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

    // Convert destination address into lan lon with geocoder
    public Map Geocoder(String location) {
        Map<String, Double> coordinates = new HashMap<>();

        try {
            if (location != null || !location.equals("")) {
                List<Address> addressList = null;

                Geocoder geocoder = new Geocoder(context);
                try {
                    addressList = geocoder.getFromLocationName(location, 1);

                } catch (IOException e) {
                    e.printStackTrace();
                }

                Address address = addressList.get(0);
//            tolatLng = new LatLng(address.getLatitude(), address.getLongitude());

//            mMap.addMarker(new MarkerOptions().position(tolatLng).title("End").icon(BitmapDescriptorFactory.fromResource(R.drawable.chequered_flag)));
//            mMap.animateCamera(CameraUpdateFactory.newLatLng(tolatLng));
//            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(tolatLng, ZOOM));

//            toLat = address.getLatitude();
//            toLon = address.getLongitude();
                coordinates.put("lat", address.getLatitude());
                coordinates.put("lon", address.getLongitude());

                Toast.makeText(context, "Destination found", Toast.LENGTH_LONG).show();
                Log.d("Geocode", "Address: "+address.toString()+"\nlat: " + address.getLatitude() + " lon: " + address.getLongitude());

                return coordinates;
            }
        }catch (Exception e){
            Toast.makeText(context, "Please enter a destination address, current address is used", Toast.LENGTH_LONG).show();
            Log.d("Geocode", e.toString());
        }
        getLastLocation();
        coordinates.put("lat", fromLat);
        coordinates.put("lon", fromLon);
        Log.d("Geocode", "null address");
        Toast.makeText(context, "Please enter a destination address.\n" +
                "Current address is used", Toast.LENGTH_LONG).show();
        return coordinates;
    }

    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

}