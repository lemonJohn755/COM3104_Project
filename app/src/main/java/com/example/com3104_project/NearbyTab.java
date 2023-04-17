package com.example.com3104_project;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

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
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class NearbyTab extends Fragment implements OnMapReadyCallback, LocationListener{

    private static final int REQUEST_LOCATION_PERMISSION = 1;
    private static final String TAG = "MyLocationService";
    private GoogleMap mMap;
    private EditText et_from;
    private Button bt_getLoc, bt_current, bt_findNearby;
    private Spinner spType;
    private PlacesClient placesClient;
    double latitude, longitude;
    private Location lastKnownLocation;
    
    SupportMapFragment supportMapFragment;
    FusedLocationProviderClient fusedLocationProviderClient;

    Context context;

    String key = "AIzaSyCcD7VS6fWvJq2Awc-pFW5UkRBREFfgip4";

    private static final long MIN_DISTANCE_CHANGE_FOR_UPDATES = 10; // 10 meters // The minimum time between updates in milliseconds
    private static final long MIN_TIME_BW_UPDATES = 200 * 10 * 1; // 2 seconds Location location;
    private static final int REQUEST_CODE = 100;
    protected LocationManager locationManager;
    private Location location;

    ImageButton imgbt_getGPSStart;
    ImageButton imgbt_search;
    Marker hereMarker;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_nearby_tab, container, false);
        context = view.getContext();

        et_from = (EditText) view.findViewById(R.id.et_from);
//        bt_getLoc = (Button) view.findViewById(R.id.bt_getLoc);
//        bt_current = (Button) view.findViewById(R.id.bt_current);
        bt_findNearby = (Button) view.findViewById(R.id.bt_findNearby);
        spType = (Spinner) view.findViewById(R.id.sp_type);
        imgbt_getGPSStart= view.findViewById(R.id.imgbt_getGPSStart);
        imgbt_search = view.findViewById(R.id.imgbt_search);

        String[] placeTypeList = {"atm", "bank", "hospital", "movie_theater", "restaurant"};
        String[] placeNameList = {"ATM", "Bank", "Hospital", "Movie Theater", "Restaurant"};

        spType.setAdapter(new ArrayAdapter<>(context, android.R.layout.simple_spinner_dropdown_item, placeNameList));

        Places.initialize(context, "AIzaSyCcD7VS6fWvJq2Awc-pFW5UkRBREFfgip4");

        refreshMap();

        bt_findNearby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int i = spType.getSelectedItemPosition();
                String url = "https://maps.googleapis.com/maps/api/place/nearbysearch/json"
                        + "?location=" + latitude + "," + longitude + "&radius=5000" + "&types="
                        + placeTypeList[i] + "&sensor=true" + "&key="+key;

                new PlaceTask().execute(url);


//                getNearbyPlaces();
            }
        });

        // Get current loc button with GPS
        imgbt_getGPSStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Log.d("Button", "Get loc button clicked");
                refreshMap();

                Log.d("here", "Start: lan, long => "+latitude+", "+longitude);


                String fromAddr = reverseGeoCode(latitude, longitude);
                et_from.setText(fromAddr);

            }
        });

//        bt_current.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                getLocation();
//            }
//        });

        imgbt_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = et_from.getText().toString();
                Geocoder geocoder = new Geocoder(context);
                try {
                    List<Address> addresses = geocoder.getFromLocationName(location, 1);
                    latitude = addresses.get(0).getLatitude();
                    longitude = addresses.get(0).getLongitude();
                    LatLng latLng = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions()
                            .position(latLng)
                            .title(et_from.getText().toString()));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        // Remove previous markers
        if(hereMarker != null){
            mMap.clear();
        }

        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(false);

        LatLng latLng = new LatLng(latitude, longitude);
        hereMarker = mMap.addMarker(new MarkerOptions()
                .position(latLng)
                .title("Here")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));

        getLocation();
//        getNearbyPlaces();
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

    private void refreshMap() {
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this::onMapReady);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context);
        getLocation();
    }

    private void getNearbyPlaces() {
        /*
        List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.LAT_LNG);

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Task<Location> locationTask = fusedLocationProviderClient.getLastLocation();
        locationTask.addOnSuccessListener(location -> {
            if (location != null) {
                lastKnownLocation = location;
                FindCurrentPlaceRequest request = FindCurrentPlaceRequest.newInstance(placeFields);
                Task<FindCurrentPlaceResponse> placeResponseTask = placesClient.findCurrentPlace(request);
                placeResponseTask.addOnSuccessListener(response -> {
                    for (PlaceLikelihood placeLikelihood : response.getPlaceLikelihoods()) {
                        Place place = placeLikelihood.getPlace();
                        LatLng latLng = place.getLatLng();
                        mMap.addMarker(new MarkerOptions().position(latLng).title(place.getName()));
                    }
                    LatLng latLng = new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                });
                    placeResponseTask.addOnFailureListener(e -> {
                    Toast.makeText(NearbyActivity.this, "Error finding nearby places", Toast.LENGTH_SHORT).show();
                });
            } else {
                Toast.makeText(NearbyActivity.this, "Unable to get current location", Toast.LENGTH_SHORT).show();
            }
        });
        locationTask.addOnFailureListener(e -> {
            Toast.makeText(NearbyActivity.this, "Error getting location", Toast.LENGTH_SHORT).show();
        });*/

    }

    private void getLocation() {
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
//                getLocation();
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
                    latitude = location.getLatitude();
                    longitude = location.getLongitude();
                    Log.d("location", "GPS Provider location obtained, lat " + latitude + " lon " + longitude);
                    Toast.makeText(context, "GPS Provider location obtained", Toast.LENGTH_SHORT).show();

                }
            }
        }
    }


    @Override
    public void onLocationChanged(@NonNull Location location) {

    }

    @Override
    public void onLocationChanged(@NonNull List<Location> locations) {

    }

    private class PlaceTask extends AsyncTask<String, Integer, String> {

        @Override
        protected String doInBackground(String... strings) {
            String data = null;
            try {
                data = downloadUrl(strings[0]);
            }
            catch(IOException e) {
                e.printStackTrace();
            }
            return data;
        }

        @Override
        protected void onPostExecute(String s) {
            new ParserTask().execute(s);
        }
    }
    private String downloadUrl(String string) throws IOException {
        URL url = new URL(string);
        HttpURLConnection connection = (HttpURLConnection)url.openConnection();
        connection.connect();
        InputStream stream = connection.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
        StringBuilder builder = new StringBuilder();
        String line = "";
        while((line = reader.readLine()) != null) {
            builder.append(line);
        }
        String data = builder.toString();
        reader.close();
        return data;
    }

    private void askPermission() {
        ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_CODE);
        Log.d("location", "asking for permission...");
    }

    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        @Override
        protected List<HashMap<String, String>> doInBackground(String... strings) {
            JParser jparser = new JParser();
            List<HashMap<String, String>> mapList = null;
            try {
                JSONObject object = new JSONObject(strings[0]);
                mapList = jparser.parseResult(object);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }
            return mapList;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> hashMaps) {
            mMap.clear();
            for (int i=0; i<hashMaps.size(); i++) {
                HashMap<String, String> hashMapList = hashMaps.get(i);
                double lat = Double.parseDouble(hashMapList.get("lat"));
                double lng = Double.parseDouble(hashMapList.get("lng"));
                String name = hashMapList.get("name");
                LatLng latLng = new LatLng(lat, lng);
                MarkerOptions options = new MarkerOptions();
                options.position(latLng);
                options.title(name);
                mMap.addMarker(options);

                hereMarker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(latitude, longitude))
                        .title("Here")
                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.gps)));

//                onMapReady(mMap);
            }
            super.onPostExecute(hashMaps);
        }
    }

}