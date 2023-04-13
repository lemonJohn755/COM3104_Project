package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

public class BusStopListActivity extends AppCompatActivity {

    private Bus bus;
    private String ROUTE;
    private TabItem tb_inbound;
    private TabLayout tabLayout;
    private String IN_DEST = "inbound";     // Inbound destination
    private String OUT_DEST = "outbound";     // Outbound destination

//    private String DEST = bus.getToLoc();    // Destination

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_stop_list);

        if(getIntent().getExtras() != null) {
            bus = (Bus) getIntent().getSerializableExtra("bus");
        }

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        ROUTE = bus.getRoute();
        // Top tittle bar set text
        setTitle(ROUTE + " ("+bus.getCompany()+") Stops");

        // Set tab item title for inbound & outbound destinations
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.getTabAt(0).setText(IN_DEST);
        tabLayout.getTabAt(1).setText(OUT_DEST);




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