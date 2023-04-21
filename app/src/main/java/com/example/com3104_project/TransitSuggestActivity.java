package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class TransitSuggestActivity extends AppCompatActivity implements OnMapReadyCallback, SuggestionListener {

    StartDestLoc startDestLoc;
    String fromAddr, toAddr;
    double fromLat, fromLon, toLat, toLon;
    float ZOOM = 13;

    EditText et_from, et_to;

    private GoogleMap mMap;

    StringRequest mStringRequest;
    private RequestQueue mRequestQueue;

    LatLng fromloc, toloc;

    Marker startMarker;     // Start marker on map
    Marker destMarker;      // Destination market on map

    SuggestionAdaptor suggestionAdaptor;
    List<Suggest> suggestList = new ArrayList<>();     // ArrayList to store bus route obj
    Suggest suggest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transit_suggest);

        setTitle("Transit suggestion");

        // Get obj for start & dest loc info resp.
        getStartDest();

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        et_from = findViewById(R.id.et_from);
        et_to = findViewById(R.id.et_to);

        et_from.setText(fromAddr);
        et_to.setText(toAddr);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        getSuggestion(fromLat, fromLon, toLat, toLon);

        // RecycleView list to display all the bus routes
        suggestionAdaptor = new SuggestionAdaptor(getApplicationContext(), suggestList, this);
        RecyclerView recyclerView = findViewById(R.id.rv_suggestion);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(suggestionAdaptor);

    }

    private void getSuggestion(double fromLat, double fromLon, double toLat, double toLon) {

        String url = "https://api.external.citymapper.com/api/1/directions/transit?" +
                "start=" + Double.toString(fromLat) + "," + Double.toString(fromLon) +
                "&end=" + Double.toString(toLat) + "," + Double.toString(toLon);

        Log.d("volley", "Response url:" + url);//display the response on screen

        mRequestQueue = Volley.newRequestQueue(this);

        // String Request initialized
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {

//                response = toUTF8(response);

                Log.d("volley", "Response return:" + response);//display the response on screen

                    // Get value from json response
                try {
                    // Parse the JSON string
//                    JsonObject jsonObject = new Gson().fromJson(response, JsonObject.class);
                    JSONObject jsonObject = response;

                    // Extract the "routes" array
//                    JsonArray routesArray = jsonObject.getAsJsonArray("routes");
                    JSONArray routesArray = jsonObject.getJSONArray("routes");

//                    JsonObject routeObject;
                    JSONObject routeObject;
                    int durationSec = 0;
                    double distanceMeter = 0;
                    for (int i=0; i < routesArray.length(); i++){
//                        routeObject = routesArray.get(i).getAsJsonObject();
                        routeObject = routesArray.getJSONObject(i);
//                        durationSec = routeObject.get("duration_seconds").getAsInt();
                        durationSec = routeObject.getInt("duration_seconds");

                        Log.d("vRoute","Route "+ i+": "+routeObject.toString());

//                        suggest = new Suggest("Route "+(i+1), durationSec, routeObject.toString());
                        suggestList.add(new Suggest("Route "+(i+1), durationSec, routeObject.toString()));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

                // Update the UI change after API call
                suggestionAdaptor.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley", "Error :" + error.toString());
                Toast.makeText(TransitSuggestActivity.this, error.toString(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Citymapper-Partner-Key", getString(R.string.citymapper_key));
                params.put("Content-Type", "application/json; charset=utf-8");
                return params;
            }
        };

//        mStringRequest.setRetryPolicy(new DefaultRetryPolicy(
//                0,
//                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
//                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT)
//        );

        mRequestQueue.add(jsonObjectRequest);

    }

    private String toUTF8(String response) {

        // Create Gson object
        Gson gson = new Gson();

// Create object to be serialized
//        MyObject myObject = new MyObject();

// Serialize object to JSON string
        String jsonString = gson.toJson(response);

// Get bytes of UTF-8 encoding
        byte[] utf8Bytes = new byte[0];
        String utf8String = "";

        try {
            utf8Bytes = jsonString.getBytes("UTF-8");
            utf8String = new String(utf8Bytes, "UTF-8");

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Log.d("utf8Str", utf8String);

        return utf8String;
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        if (mMap != null) {
            mMap.clear();
        }
        mMap = googleMap;
        // Add a marker in Sydney and move the camera
        fromloc = new LatLng(fromLat, fromLon);
        Log.d("location", "showing map now " + fromLat + "::" + fromLon);

        mMap.addMarker(new MarkerOptions().position(fromloc).title("Start")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.direction2)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(fromloc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(fromloc, ZOOM));

//        mMap.getUiSettings().setZoomControlsEnabled(false);


        toloc = new LatLng(toLat, toLon);
        mMap.addMarker(new MarkerOptions().position(toloc).title("Destination")
                .icon(BitmapDescriptorFactory.fromResource(R.drawable.chequered_flag)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(toloc));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(toloc, ZOOM));
        Log.d("location", "showing map now " + toLat + "::" + toLon);

        // Move the camera to fit all LatLng obj
        LatLngBounds.Builder builder = new LatLngBounds.Builder();
        builder.include(fromloc);
        builder.include(toloc);
        LatLngBounds bounds = builder.build();
        mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds, 900,900,120), 1000, null);

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

        Log.d("startDestLoc", "From:" + fromAddr + ", To:" + toAddr +
                "\nfrom(lan,lon): " + fromLat + " ," + fromLon +
                "\nto(lan, lon):" + toLat + " ," + toLon);

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
    public void onRouteClicked(Suggest suggest) {
        Log.d("Route", "Route "+suggest.getRoute()+ " selected");

        // Go to SuggestDetailActivity
        Intent intent = new Intent(TransitSuggestActivity.this, SuggestDetailActivity.class);
        // start the activity connect to the specified class
        intent.putExtra("suggest", suggest);
        intent.putExtra("startDestLoc", startDestLoc);

        startActivity(intent);
    }

    private class MyObject {
    }
}