package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class GMBActivity extends AppCompatActivity {

    SearchView sv_search_bus;
    GMBAdaptor gmbAdaptor;
    List<GMB> gmbRouteList = new ArrayList<GMB>();     // ArrayList to store bus route obj

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gmbactivity);

        setTitle("Green Minibus Search");

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        sv_search_bus = findViewById(R.id.sv_search_bus);
//        sv_search_bus.clearFocus();

        gmbRouteList = getGMBRouteList();


        // RecycleView list to display all the bus routes
        gmbAdaptor = new GMBAdaptor(getApplicationContext(), gmbRouteList);
        RecyclerView recyclerView = findViewById(R.id.rv_gmb_list);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(gmbAdaptor);

    }

    private List<GMB> getGMBRouteList() {

        // demo items RecyclerView
        gmbRouteList.add(new GMB("20003337", "HKI", "DEMO 69X"));
        gmbRouteList.add(new GMB("2003042", "HKI", "DEMO 23"));
        gmbRouteList.add(new GMB("2003771", "HKI", "DEMO 55"));
        gmbRouteList.add(new GMB("2002291", "HKI", "DEMO 16X"));
        gmbRouteList.add(new GMB("20003337", "HKI", "DEMO 69X"));
        gmbRouteList.add(new GMB("2003042", "HKI", "DEMO 23"));
        gmbRouteList.add(new GMB("2003771", "HKI", "DEMO 55"));
        gmbRouteList.add(new GMB("2002291", "HKI", "DEMO 16X"));

        return gmbRouteList;
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