package com.example.com3104_project;

import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;

public class BusSearchActivity extends AppCompatActivity {

    EditText et_searchBus;
    ImageButton imgbt_searchBus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTitle("Bus search");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bus_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        et_searchBus = findViewById(R.id.et_searchBus);
//        listAllBusRoute();

//        imgbt_searchBus.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

    }

    public void listAllBusRoute(){

        try {
            // get input stream from the XML file
            InputStream inputStream = getAssets().open("routebus.xml");

            // create a new instance of XmlPullParser
            XmlPullParser parser = Xml.newPullParser();

            // set input stream and encoding
            parser.setInput(inputStream, null);

            // get event type
            int eventType = parser.getEventType();

            // loop through the XML document
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (eventType == XmlPullParser.START_TAG && parser.getName().equals("ROUTE")) {
                    // do something with the <ROUTE> tag
                    String companyCode = parser.getAttributeValue(null, "COMPANY_CODE");
                    String routeNameC = parser.getAttributeValue(null, "ROUTE_NAMEC");
                    String locStartNameC = parser.getAttributeValue(null, "LOC_START_NAMEC");
                    String locStartNames = parser.getAttributeValue(null, "LOC_START_NAMES");
                    String locStartNameE = parser.getAttributeValue(null, "LOC_START_NAMEE");
                    String locEndNameC = parser.getAttributeValue(null, "LOC_END_NAMEC");
                    String locEndNames = parser.getAttributeValue(null, "LOC_END_NAMES");
                    String locEndNameE = parser.getAttributeValue(null, "LOC_END_NAMEE");

                    // do something with the data
                    Log.d("ROUTE", "Company Code: " + companyCode);
                    Log.d("ROUTE", "Route NameC: " + routeNameC);
                    Log.d("ROUTE", "Loc Start NameC: " + locStartNameC);
                    Log.d("ROUTE", "Loc Start Names: " + locStartNames);
                    Log.d("ROUTE", "Loc Start NameE: " + locStartNameE);
                    Log.d("ROUTE", "Loc End NameC: " + locEndNameC);
                    Log.d("ROUTE", "Loc End Names: " + locEndNames);
                    Log.d("ROUTE", "Loc End NameE: " + locEndNameE);
                }
                eventType = parser.next();
            }
        } catch (XmlPullParserException | IOException e) {
            e.printStackTrace();
        }


    }
}