package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GMBActivity extends AppCompatActivity implements GMBListener {

    SearchView sv_search_gmb;
    GMBAdaptor gmbAdaptor;
    List<GMB> gmbRouteList = new ArrayList<>();     // ArrayList to store bus route obj
    RadioGroup rgp_region;
    String region;

    StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmbactivity);

        getGMBRouteList();

        setTitle("Green Minibus Search");

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sv_search_gmb = findViewById(R.id.sv_search_gmb);
        sv_search_gmb.clearFocus();
        sv_search_gmb.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText);
                return false;
            }
        });

        // RecycleView list to display all the bus routes
        gmbAdaptor = new GMBAdaptor(getApplicationContext(), gmbRouteList, this);
        RecyclerView recyclerView = findViewById(R.id.rv_gmb_list);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(gmbAdaptor);


    }

    private void filterList(String text) {
        List<GMB> filteredList = new ArrayList<>();

        for(GMB gmb : gmbRouteList){
            if( gmb.getRoute_code().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
                filteredList.add(gmb);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this, "No route found", Toast.LENGTH_SHORT).show();
        }
        else{
            gmbAdaptor.setFilteredList(filteredList);
        }
    }

    public void getGMBRouteList() {
        String baseURL = "https://data.etagmb.gov.hk/route/";

        // demo items RecyclerView test before API request
//        gmbRouteList.add(new GMB("20003337", "HKI", "DEMO 69X"));
//        gmbRouteList.add(new GMB("2003604", "KLN", "DEMO 13A"));
//        gmbRouteList.add(new GMB("2002547", "NT", "DEMO 9"));

        // API request here
        // RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, baseURL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("volley","Response return:" + response);//display the response on screen

                JSONObject json = null;
                try {
                    json = new JSONObject(response);
                    JSONObject data = json.getJSONObject("data");
                    JSONObject regions = data.getJSONObject("routes");

                    // GMB routes in HK Island:
                    JSONArray routes = regions.getJSONArray("HKI");

                    for (int i = 0; i < routes.length(); i++) {
                        String route = routes.getString(i);
                        gmbRouteList.add(new GMB("","HKI", route));
                        Log.d("Route", route + " "+ "HKI");
                    }

                    // GMB routes in Kowloon:
                    routes = regions.getJSONArray("KLN");
                    for (int i = 0; i < routes.length(); i++) {
                        String route = routes.getString(i);
                        Log.d("Route", route + " "+ "KLN");
                        gmbRouteList.add(new GMB("","KLN", route));
                    }

                    // GMB routes in NT:
                    routes = regions.getJSONArray("NT");
                    for (int i = 0; i < routes.length(); i++) {
                        String route = routes.getString(i);
                        Log.d("Route", route + " "+ "NT");
                        gmbRouteList.add(new GMB("","NT", route));
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                // Update the UI change after API call
                gmbAdaptor.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "Error :" + error.toString());
                Toast.makeText(GMBActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        });

        mRequestQueue.add(mStringRequest);


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


    @Override
    public void onRouteClicked(GMB gmb) {
        Toast.makeText(this, "Route "+gmb.getRoute_code()+ " selected", Toast.LENGTH_SHORT).show();
        Log.d("GMB", "Route "+gmb.getRoute_code()+ " selected");

        // Go to BusStopListActivity
        Intent intent = new Intent(GMBActivity.this, GMBStopActivity.class);
        // start the activity connect to the specified class
        intent.putExtra("gmb", gmb);    // Pass bus obj to BusStopListActivity
        startActivity(intent);
    }
}