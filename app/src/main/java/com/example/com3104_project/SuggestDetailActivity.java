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
    List<Leg> legsList = new ArrayList<>();     // Parent list for legs
    List<Leg> stopsList = new ArrayList<>();    // Child list for stops (if hv stops in leg obj)

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



        // RecycleView list to display all the bus routes
        legsAdaptor = new LegsAdaptor(getApplicationContext(), legsList);
        RecyclerView recyclerView = findViewById(R.id.rv_parent_leg);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(legsAdaptor);


    }

    // Get legs from json string in suggest obj
    private void getLeg(Suggest suggest) {
        String jsonStr = suggest.getRouteJson();


        // Add obj to legsList list

        // TEST RecyclerView
        legsList.add(new Leg("walk", "600", "", "", "", ""));
        legsList.add(new Leg("transit", "3000", "metro", "MTR", "屯馬綫 Tuen Ma Line", "stopsJson"));
        legsList.add(new Leg("transit", "1200", "bus", "KMB", "277E", "stopsJson"));
        legsList.add(new Leg("transit", "1200", "bus", "KMB", "277E", "stopsJson"));
        legsList.add(new Leg("transit", "1200", "bus", "KMB", "277E", "stopsJson"));
        legsList.add(new Leg("transit", "1200", "bus", "KMB", "277E", "stopsJson"));



        // Notify legsList change after receive json response from API
//        legsAdaptor.notifyDataSetChanged();



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