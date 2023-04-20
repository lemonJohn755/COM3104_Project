package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggestDetailActivity extends AppCompatActivity implements OnMapReadyCallback {

    Suggest suggest;
    StartDestLoc startDestLoc;
    LegsAdaptor legsAdaptor;
    LegStopsAdaptor legStopsAdaptor;
    List<Leg> legsList = new ArrayList<>();     // Parent list for legs
    List<LegStop> MarkersList = new ArrayList<>();


    TextView tv_duration_min, tv_summary;
    SlidingUpPanelLayout sliding_layout;

    private GoogleMap mMap;

    LatLng fromloc, toloc;

    float ZOOM = 12;

    double fromLat = 0;
    double fromLon = 0;
    double toLat = 0;
    double toLon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_detail);

        startDestLoc = (StartDestLoc) getIntent().getSerializableExtra("startDestLoc");

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get previous obj
        if(getIntent().getExtras() != null) {
            suggest = (Suggest) getIntent().getSerializableExtra("suggest");
            Log.d("suggestDetail", suggest.getRouteJson());
        }

        getStartDest();

        setTitle(suggest.getRoute()+" detail");

        // Google Map display & put markers
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        tv_duration_min = findViewById(R.id.tv_duration_min);

        int min = Math.round(suggest.getDurationSec()/60);

        tv_duration_min.setText(min+" min");

        getLeg(suggest.getRouteJson());

        tv_summary = findViewById(R.id.tv_summary);
        tv_summary.setText(getSummary(suggest.getRouteJson()));

        // RecycleView list to display all legs
        legsAdaptor = new LegsAdaptor(getApplicationContext(), legsList);
        RecyclerView recyclerView = findViewById(R.id.rv_parent_leg);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(legsAdaptor);

    }

    private String getSummary(String jsonStr) {
        String summary = "";

        String travel_mode = "";
        int duration_secconds = 0;
        String vehicle_types = "";

        // Get legs[i]
        // Assuming the JSON code is stored in a String variable called jsonStr
        JSONObject legJsonObj = null;
        try {
            legJsonObj = new JSONObject(jsonStr);

            JSONArray legsArray = legJsonObj.getJSONArray("legs");

            for (int i=0; i<legsArray.length(); i++){

                Log.d("leg", "legsArray: " + legsArray.getString(i));

                duration_secconds = legsArray.getJSONObject(i).getInt("duration_seconds");

                Log.d("leg", "dration sec: "+ duration_secconds);

                travel_mode = legsArray.getJSONObject(i).getString("travel_mode");

                if (travel_mode.equals("walk")){        // for walk
                    summary = summary + " "+ "walk";
                }
                else if (travel_mode.equals("transit")) {      // for transit

                    Log.d("leg", legsArray.getJSONObject(i).getJSONArray("services").toString());

                    if (legsArray.getJSONObject(i).getJSONArray("services").length() > 0) {

                        JSONObject json = new JSONObject(jsonStr); // jsonString is your JSON string
                        JSONArray vehicleTypes = legsArray.getJSONObject(i).getJSONArray("services")
                                .getJSONObject(0)
                                .getJSONArray("vehicle_types");

                        vehicle_types = vehicleTypes.getString(0);
                        Log.d("leg", "vehicle_types: "+vehicle_types);

                        summary = summary + " "+ vehicle_types;

                    }
                }
                Log.d("legSummary", "summary: "+ summary);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return capitalizeWords(summary);
    }

    // Get legs from json string in suggest obj
    private void getLeg(String jsonStr) {

        String travel_mode = "";
        int duration_secconds = 0;
        String vehicle_types = "";
        String brand = "";
        String name = "";
        List<LegStop> legStopList = new ArrayList<>();

        Log.d("route>leg", "route>leg: "+ jsonStr);

        // Get legs[i]
        // Assuming the JSON code is stored in a String variable called jsonStr
        JSONObject legJsonObj = null;
        try {
            legJsonObj = new JSONObject(jsonStr);

            JSONArray legsArray = legJsonObj.getJSONArray("legs");

            Leg leg = new Leg(travel_mode, duration_secconds, "", "", "", null);

            for (int i=0; i<legsArray.length(); i++){

                Log.d("leg", "legsArray: " + legsArray.getString(i));

                duration_secconds = legsArray.getJSONObject(i).getInt("duration_seconds");

                Log.d("leg", "dration sec: "+ duration_secconds);

                travel_mode = legsArray.getJSONObject(i).getString("travel_mode");

                if (travel_mode.equals("walk")){        // for walk

                    if ( (i+1) == legsArray.length()){
                        leg = new Leg(travel_mode, duration_secconds, "", "", "walk to destination", null);
                    }
                    else if (i==0) {
                        leg = new Leg(travel_mode, duration_secconds, "", "", "walk to station/stop", null);
                    }
                    else if (legsArray.getJSONObject(i+1).getString("travel_mode").equals("transit")){
                        leg = new Leg(travel_mode, duration_secconds, "", "", "switch bus route/ metro line/ other transport", null);
                    }
                    else{
                        leg = new Leg(travel_mode, duration_secconds, "", "", "", null);
                    }
//                    leg = new Leg(travel_mode, duration_secconds, "", "", "", null);
                }
                else if (travel_mode.equals("transit")) {      // for transit

                    Log.d("leg", legsArray.getJSONObject(i).getJSONArray("services").toString());

                    if (legsArray.getJSONObject(i).getJSONArray("services").length() > 0) {

                        JSONObject json = new JSONObject(jsonStr); // jsonString is your JSON string
                        JSONArray vehicleTypes = legsArray.getJSONObject(i).getJSONArray("services")
                                .getJSONObject(0)
                                .getJSONArray("vehicle_types");

                        vehicle_types = vehicleTypes.getString(0);
                        Log.d("leg", "vehicle_types: "+vehicle_types);

//                        brand = legsArray.getJSONObject(i).getJSONArray("services")
//                                .getJSONObject(0).getJSONArray("brand").getJSONObject(0);
                        brand = legsArray.getJSONObject(i).getJSONArray("services")
                                .getJSONObject(0)
                                .getJSONObject("brand")
                                .getString("name");

                        name = legsArray.getJSONObject(i).getJSONArray("services").getJSONObject(0).getString("name");

                    }else{
                        vehicle_types = "";
                        brand = "";
                        name = "";
                    }

                    leg = new Leg(travel_mode, duration_secconds, vehicle_types, brand, name, childrenStopList(jsonStr, i));
                }

                Log.d("leg", "leg "+i+") "+travel_mode+": vehicle_types: "+ vehicle_types+ " brand: "+brand+ " name: "+name);

                legsList.add(leg);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    // Get stop[i]
    public List<LegStop> childrenStopList(String stopJson, int legPos){

        List<LegStop> legStopsList = new ArrayList<>();    // Child list for stops (if hv stops in leg obj)

        // DEMO stops display
//        legStopsList.add(new LegStop("屯門 Tuen Mun", 22.395268, 113.973088));
//        legStopsList.add(new LegStop("兆康 Siu Hong", 22.411856, 113.978843));
//        legStopsList.add(new LegStop("天水圍 Tin Shui Wai", 22.44846, 114.004875));
//        legStopsList.add(new LegStop("朗屏 Long Ping", 22.447811, 114.025324));
//        legStopsList.add(new LegStop("元朗 Yuen Long", 22.446038, 114.035385));
//        legStopsList.add(new LegStop("錦上路 Kam Sheung Road", 22.434712, 114.063412));

        // Get stop list from json
        try {
            JSONObject jsonObj =  new JSONObject(stopJson);
            JSONArray jsonArr = jsonObj.getJSONArray("legs").getJSONObject(legPos).getJSONArray("stops");

            Log.d("StopsArray", jsonArr.length()+ " "+jsonArr.toString());

            String name;
            double codLat, codLon;

            for(int i=0; i<jsonArr.length(); i++){

                name = jsonArr.getJSONObject(i).getString("name");

                codLat = jsonArr.getJSONObject(i).getJSONObject("coordinates").getDouble("lat");

                codLon = jsonArr.getJSONObject(i).getJSONObject("coordinates").getDouble("lon");

                Log.d("StopList","name: "+ name + "| lat, lon: "+ codLat + ", "+ codLon);

                // add to return list
                legStopsList.add(new LegStop(name, codLat, codLon));
                MarkersList.add(new LegStop(name, codLat, codLon));
                Log.d("StopsArray", "legStopsList size: "+legStopsList.size());

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return legStopsList;
    }

    // Back button at title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(SuggestDetailActivity.this, TransitSuggestActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.putExtra("startDestLoc", startDestLoc);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public static String capitalizeWords(String str) {
        String[] words = str.split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                String firstLetter = word.substring(0, 1);
                String restOfWord = word.substring(1);
                capitalized.append(firstLetter.toUpperCase()).append(restOfWord).append(" ");
            }
        }
        return capitalized.toString().trim();
    }

    private void getStartDest() {
        // Retrieve start & dest locations info from the obj.
        startDestLoc = (StartDestLoc) getIntent().getSerializableExtra("startDestLoc");

        fromLat = startDestLoc.getFromLat();
        fromLon = startDestLoc.getFromLon();

        toLat = startDestLoc.getToLat();
        toLon = startDestLoc.getToLon();

    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {

        startDestLoc = (StartDestLoc) getIntent().getSerializableExtra("startDestLoc");

        if (mMap != null) {
            mMap.clear();
        }

        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        fromloc = new LatLng(fromLat, fromLon);
        Log.d("location", "showing map now " + fromLat + "::" + fromLon);
        mMap.addMarker(new MarkerOptions().position(fromloc).title("Start")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fromloc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromloc, ZOOM));

        toloc = new LatLng(toLat, toLon);
        mMap.addMarker(new MarkerOptions().position(toloc).title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chequered_flag)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(toloc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toloc, ZOOM));
        Log.d("location", "showing map now " + toLat + "::" + toLon);

        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(fromloc);
        builder.include(toloc);
        LatLngBounds bounds = builder.build();
//        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,400,800,0), 2000, null);

        String name;
        double codLat, codLon;
        List<LatLng> stopCods = new ArrayList<>();
        stopCods.add(fromloc);
        stopCods.add(toloc);

        // Assign markers for all stops

        for(LegStop i: MarkersList){
            name = i.getName();
            codLat = i.getCodLat();
            codLon = i.getCodLon();
            Log.d("stopPin",name +" " + codLat+ ","+ codLon);
            mMap.addMarker(new MarkerOptions().position(new LatLng(codLat, codLon)).title(name));
            builder.include(new LatLng(codLat, codLon));
        }

        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds,900,900,20), 2000, null);


    }
}