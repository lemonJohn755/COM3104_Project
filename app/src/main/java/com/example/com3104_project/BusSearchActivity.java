package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BusSearchActivity extends AppCompatActivity implements BusRouteListener{

    EditText et_searchBus;
    ImageButton imgbt_searchBus;
    SearchView sv_search_bus;

    List<Bus> bus_routes = new ArrayList<Bus>();     // ArrayList to store bus route obj
    BusAdaptor busAdaptor;

    String company = "";

    RadioGroup rgp_busCompany;
    RadioButton rbt_kmb, rbt_ctb, rbt_nwfb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Bus search");

        // Load all the bus routes
        listAllBusRoute();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search);

        // Back button on title bar
        assert getSupportActionBar() != null;   //null check
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        et_searchBus = findViewById(R.id.et_searchBus);

        sv_search_bus = findViewById(R.id.sv_search_bus);
        sv_search_bus.clearFocus();
        sv_search_bus.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterList(newText,company);
                return false;
            }
        });

        rgp_busCompany = (RadioGroup) findViewById(R.id.rgp_busCompany);
        rbt_kmb = findViewById(R.id.rbt_kmb);
        rbt_ctb = findViewById(R.id.rbt_ctb);
        rbt_nwfb = findViewById(R.id.rbt_nwfb);

        rgp_busCompany.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (rbt_kmb.isChecked()){
                    company = "kmb";
                    filterList(sv_search_bus.getQuery().toString(), company);
                }
                else if (rbt_ctb.isChecked()){
                    company = "ctb";
                    filterList(sv_search_bus.getQuery().toString(), company);
                }
                else if (rbt_nwfb.isChecked()){
                    company = "nwfb";
                    filterList(sv_search_bus.getQuery().toString(), company);
                }
                filterList(sv_search_bus.getQuery().toString(), company);
            }
        });

        // RecycleView list to display all the bus routes
        busAdaptor = new BusAdaptor(getApplicationContext(), bus_routes, this);
        RecyclerView recyclerView = findViewById(R.id.rv_allBus);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(busAdaptor);

    }

    public void filterList(String bus_code, String company) {
        List<Bus> filteredList = new ArrayList<>();

        for(Bus bus : bus_routes){
            if( bus.getRoute().toLowerCase(Locale.ROOT).contains(bus_code.toLowerCase(Locale.ROOT))
                    && !bus.getCompany().equals("")){

                if ( bus.getCompany().toLowerCase().contains(company.toLowerCase())){
                    filteredList.add(bus);
                }

            }else if (bus.getRoute().toLowerCase(Locale.ROOT).contains(bus_code.toLowerCase(Locale.ROOT))){
                filteredList.add(bus);
            }
        }

        if (filteredList.isEmpty()){
            Toast.makeText(this, "No route found", Toast.LENGTH_SHORT).show();
        }
        else{
            busAdaptor.setFilteredList(filteredList);
        }
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

    // Load all the bus routes from assets/ROUTE_BUS.xml
    public void listAllBusRoute(){
        try {
            InputStream is = getAssets().open("routebus.xml");

            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(is);

            Element element=doc.getDocumentElement();
            element.normalize();

            NodeList nList = doc.getElementsByTagName("ROUTE");
            for (int i=0; i<nList.getLength(); i++) {

                Node node = nList.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element2 = (Element) node;

                    String busRoute = getValue("ROUTE_NAMEE", element2);
                    String company = getValue("COMPANY_CODE", element2);
                    String fromLoc = getValue("LOC_START_NAMEE", element2);
                    String endLoc = getValue("LOC_END_NAMEE", element2);

                    bus_routes.add(new Bus(busRoute, fromLoc, endLoc, company));
//                    Log.d("bus route", busRoute + ", From: "+ fromLoc+ " To: "+ endLoc);

                }
            }//end of for loop
            Log.d("bus route", nList.getLength()+" routes read from xml");


        } catch (Exception e) {
            Log.d("xml", e.toString());
        }

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }


    @Override
    public void onRouteClicked(Bus bus) {
//        Toast.makeText(this, "Route "+bus.getRoute()+ "\nProvider: "+ bus.getCompany()+ "\nFrom: "+bus.getFromLoc()+"\nTo: "+ bus.getToLoc(), Toast.LENGTH_SHORT).show();
        Log.d("rv", "selected item\nRoute "+bus.getRoute()+ ", Provider: "+ bus.getCompany()+ ", From: "+bus.getFromLoc()+" ,To: "+ bus.getToLoc());

        // Go to BusStopListActivity
        Intent intent = new Intent(BusSearchActivity.this, BusStopListActivity.class);
        // start the activity connect to the specified class
        intent.putExtra("bus", bus);    // Pass bus obj to BusStopListActivity
        startActivity(intent);



    }
}
