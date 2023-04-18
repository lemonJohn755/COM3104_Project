package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class TransitSuggestActivity extends AppCompatActivity implements OnMapReadyCallback {

    StartDestLoc startDestLoc;
    String fromAddr, toAddr;
    double fromLat, fromLon, toLat, toLon;
    float ZOOM = 10;

    EditText et_from, et_to;

    private GoogleMap mMap;

    Marker startMarker;     // Start marker on map
    Marker destMarker;      // Destination market on map

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_suggest);

        setTitle("Transit suggestion");

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get obj for start & dest loc info resp.
        getStartDest();

        et_from = findViewById(R.id.et_from);
        et_to = findViewById(R.id.et_to);

        et_from.setText(fromAddr);
        et_to.setText(toAddr);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);



        getSuggestion(fromLat, fromLon, toLat, toLon);


    }

    private void getSuggestion(double fromLat, double fromLon, double toLat, double toLon) {
//        String start = fromLat




    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if(mMap != null){
            mMap.clear();
        }
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        LatLng loc = new LatLng(fromLat, fromLon);
        Log.d("location", "showing map now " + fromLat + "::" + fromLon);

        mMap.addMarker(new MarkerOptions().position(loc).title("Start"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM));

//        mMap.getUiSettings().setZoomControlsEnabled(false);


        loc = new LatLng(toLat, toLon);
        mMap.addMarker(new MarkerOptions().position(loc).title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chequered_flag)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(loc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, ZOOM));
        Log.d("location", "showing map now " + toLat + "::" + toLon);

    }

    private void getStartDest() {
        // Retrieve start & dest locations info from the obj.
        startDestLoc = (StartDestLoc) getIntent().getSerializableExtra("startDestLoc");
        fromAddr = startDestLoc.getFromAddr();
        toAddr = startDestLoc.getToAddr();

        fromLat = startDestLoc.getFromLat();
        fromLon = startDestLoc.getFromLon();

        toLat = startDestLoc.getToLat();
        toLon = startDestLoc.getToLon();

        Log.d("startDestLoc", "From:"+fromAddr + ", To:"+ toAddr +
                "\nfrom(lan,lon): "+fromLat+ " ,"+ fromLon +
                "\nto(lan, lon):" + toLat+" ,"+ toLon);
    }

    // Back button at title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}