package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class GMBStopActivity extends AppCompatActivity {

    static GMB gmb;
    ViewPager2 vp_bound_stop_list;
    TabLayout tabLayout;
    ViewPagerAdaptorextendsGMBBound viewPagerAdaptorextendsGMBBound;
    Map<String, String> orgDest = new HashMap<String, String>();

    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmbstop);

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        // Get gmb obj that stores the route name
        if(getIntent().getExtras() != null) {
            gmb = (GMB) getIntent().getSerializableExtra("gmb");
        }

        setTitle(gmb.getRoute_code() + " Stops");

        orgDest = getOrgDest(gmb.getRegion(), gmb.getRoute_code());

        // Set tab item title for inbound & outbound destinations
        tabLayout = findViewById(R.id.tabLayout);

        vp_bound_stop_list = findViewById(R.id.vp_bound_stop_list);
        viewPagerAdaptorextendsGMBBound = new ViewPagerAdaptorextendsGMBBound(this);
        vp_bound_stop_list.setAdapter(viewPagerAdaptorextendsGMBBound);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp_bound_stop_list.setCurrentItem(tab.getPosition());
                Log.d("tab", "Clicked tab "+ tab.getPosition());

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {
                vp_bound_stop_list.setCurrentItem(tab.getPosition());
            }
        });

        vp_bound_stop_list.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                tabLayout.getTabAt(position).select();
            }
        });



    }

    private Map<String, String> getOrgDest(String region, String route_code) {
        String url = "https://data.etagmb.gov.hk/route/"+region+"/"+route_code;
        Log.d("volley","Request "+ url);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(GMBStopActivity.this.getApplicationContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getContext(),"Response :" + response, Toast.LENGTH_LONG).show();//display the response on screen
                Log.d("volley", response);

                JSONObject jsonObject = null;
                try {
                    JSONObject json = new JSONObject(response);
                    JSONArray directions = json.getJSONArray("data").getJSONObject(0).getJSONArray("directions");
                    String destEn = directions.getJSONObject(0).getString("dest_en");
                    String orig_en = directions.getJSONObject(0).getString("orig_en");

                    orgDest.put("outbound", destEn);
                    orgDest.put("inbound", orig_en);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                tabLayout.getTabAt(0).setText(orgDest.get("outbound"));    // Outbound route
                tabLayout.getTabAt(1).setText(orgDest.get("inbound"));     // Inbound route

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley","Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);

        return orgDest;
    }

    // Back button at title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, GMBActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}