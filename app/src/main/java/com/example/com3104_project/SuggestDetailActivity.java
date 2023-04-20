package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SuggestDetailActivity extends AppCompatActivity {

    Suggest suggest;
    StartDestLoc startDestLoc;
    LegsAdaptor legsAdaptor;
    LegStopsAdaptor legStopsAdaptor;
    List<Leg> legsList = new ArrayList<>();     // Parent list for legs

    TextView tv_duration_min;
    SlidingUpPanelLayout sliding_layout;

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

        setTitle(suggest.getRoute()+" detail");

        tv_duration_min = findViewById(R.id.tv_duration_min);

        int min = Math.round(suggest.getDurationSec()/60);

        tv_duration_min.setText(min+" min");

//        Toast.makeText(SuggestDetailActivity.this, suggest.getRouteJson(), Toast.LENGTH_LONG).show();

        getLeg(suggest.getRouteJson());

        //
        //
        // Google Map display & put markers
        //
        //

        // RecycleView list to display all legs
        legsAdaptor = new LegsAdaptor(getApplicationContext(), legsList);
        RecyclerView recyclerView = findViewById(R.id.rv_parent_leg);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(legsAdaptor);

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

                travel_mode = legsArray.getJSONObject(i).getString("travel_mode");

                if (travel_mode.equals("walk")){        // for walk
//                    legsList.add(new Leg(travel_mode, duration_secconds, "", "",
//                            "", null));
                    leg = new Leg(travel_mode, duration_secconds, "", "", "", null);
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

            Log.d("StopsArray", jsonArr.toString());

            String name;
            double codLat, codLon;

            for(int i=0; i<jsonArr.length(); i++){

                name = jsonArr.getJSONObject(i).getString("name");

                codLat = jsonArr.getJSONObject(i).getJSONObject("coordinates").getDouble("lat");

                codLon = jsonArr.getJSONObject(i).getJSONObject("coordinates").getDouble("lon");

                Log.d("StopList","name: "+ name + "| lat, lon: "+ codLat + ", "+ codLon);

                // add to return list
                legStopsList.add(new LegStop(name, codLat, codLon));

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
}