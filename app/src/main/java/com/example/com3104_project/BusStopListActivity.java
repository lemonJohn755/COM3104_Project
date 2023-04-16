package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;


public class BusStopListActivity extends AppCompatActivity {

    static Bus bus;
    BusStop busStop;
    String ROUTE;
    TabItem tb_inbound;
    TabLayout tabLayout;
    String IN_DEST = "inbound";     // Inbound destination
    String OUT_DEST = "outbound";     // Outbound destination
    String COMPANY;
    Handler mHandler;
    String jsonStr;
    ListView lv_stopList;
    RecyclerView rv_bus_stops_list;
    ViewPager2 vp_bound_stop_list;
    ViewPagerAdaptorextendsBound viewPagerAdaptorextendsBound;

//    List<BusStop> busStops = new ArrayList<BusStop>();     // store bus route obj

    BusStopAdaptor busStopAdaptor;

    //    private String DEST = bus.getToLoc();    // Destination
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_list);

        if(getIntent().getExtras() != null) {
            bus = (Bus) getIntent().getSerializableExtra("bus");
        }

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ROUTE = bus.getRoute();
        COMPANY = bus.getCompany();
        IN_DEST = bus.getFromLoc();
        OUT_DEST = bus.getToLoc();

        // Top tittle bar set text
        setTitle(ROUTE + " ("+bus.getCompany()+") Route");

        // Set tab item title for inbound & outbound destinations
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.getTabAt(0).setText(OUT_DEST);
        tabLayout.getTabAt(1).setText(IN_DEST);

        vp_bound_stop_list = findViewById(R.id.vp_bound_stop_list);
        viewPagerAdaptorextendsBound = new ViewPagerAdaptorextendsBound(this);
        vp_bound_stop_list.setAdapter(viewPagerAdaptorextendsBound);

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

    // Back button at title bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; go home
                Intent intent = new Intent(this, BusSearchActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}

