package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SuggestDetailActivity extends AppCompatActivity {

    Suggest suggest;
    StartDestLoc startDestLoc;
    LegsAdaptor legsAdaptor;
    LegStopsAdaptor legStopsAdaptor;
    List<Leg> legsList = new ArrayList<>();     // Parent list for legs

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        startDestLoc = (StartDestLoc) getIntent().getSerializableExtra("startDestLoc");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggest_detail);

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Get previous obj
        if(getIntent().getExtras() != null) {
            suggest = (Suggest) getIntent().getSerializableExtra("suggest");
            Log.d("suggest", suggest.getRouteJson());
        }

        setTitle(suggest.getRoute()+" detail");

        getLeg(suggest);

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

//        // Nested RecycleView list to display all stops (if available)
//        legStopsAdaptor = new LegStopsAdaptor(getApplicationContext(), legStopsList);
//        RecyclerView rv_child_stops = findViewById(R.id.rv_child_stops);
//        rv_child_stops.setLayoutManager((new LinearLayoutManager(this)));
//        rv_child_stops.setAdapter(legStopsAdaptor);


    }

    // Get legs from json string in suggest obj
    private void getLeg(Suggest suggest) {
        String jsonStr = suggest.getRouteJson();

        // Add obj to legsList list

        String stopsJson = "";


        // TEST RecyclerView
        legsList.add(new Leg("walk", "600", "", "", "", null));
        legsList.add(new Leg("transit", "3000", "metro", "MTR", "屯馬綫 Tuen Ma Line", childrenStopList(stopsJson)));
        legsList.add(new Leg("transit", "1200", "bus", "KMB", "277E", childrenStopList(stopsJson)));


        // MUST Notify legsList change after receive json response from API
//        legsAdaptor.notifyDataSetChanged();
//        legStopsAdaptor.notifyDataSetChanged();

    }

    public List<LegStop> childrenStopList(String jsonStr){
        List<LegStop> legStopsList = new ArrayList<>();    // Child list for stops (if hv stops in leg obj)

        // DEMO stops display
        legStopsList.add(new LegStop("屯門 Tuen Mun", 22.395268, 113.973088));
        legStopsList.add(new LegStop("兆康 Siu Hong", 22.411856, 113.978843));
        legStopsList.add(new LegStop("天水圍 Tin Shui Wai", 22.44846, 114.004875));
        legStopsList.add(new LegStop("朗屏 Long Ping", 22.447811, 114.025324));
        legStopsList.add(new LegStop("元朗 Yuen Long", 22.446038, 114.035385));
        legStopsList.add(new LegStop("錦上路 Kam Sheung Road", 22.434712, 114.063412));

        //
        // Get value from json here
        //

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