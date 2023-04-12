package com.example.com3104_project;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class BusSearchActivity extends AppCompatActivity {

    EditText et_searchBus;
    ImageButton imgbt_searchBus;


    List<Bus> items = new ArrayList<Bus>();

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

        et_searchBus = findViewById(R.id.et_searchBus);

        // RecycleView list to display all the bus routes
        RecyclerView recyclerView = findViewById(R.id.rv_allBus);
        recyclerView.setLayoutManager((new LinearLayoutManager(this)));
        recyclerView.setAdapter(new BusAdaptor(getApplicationContext(), items));


        imgbt_searchBus = findViewById(R.id.imgbt_searchBus);
        imgbt_searchBus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String search = et_searchBus.getText().toString();

                // Call API to search route

            }
        });



    }

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

                    items.add(new Bus(busRoute, fromLoc, endLoc, company));
                    Log.d("bus route", busRoute + ", From: "+ fromLoc+ " To: "+ endLoc);

                }
            }//end of for loop

        } catch (Exception e) {
            Log.d("xml", e.toString());
        }

    }

    private static String getValue(String tag, Element element) {
        NodeList nodeList = element.getElementsByTagName(tag).item(0).getChildNodes();
        Node node = (Node) nodeList.item(0);
        return node.getNodeValue();
    }

}
