package com.example.com3104_project;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.StrictMode;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BusStopListActivity extends AppCompatActivity {

    Bus bus;
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
        setTitle(ROUTE + " ("+bus.getCompany()+") Stops");

        // Set tab item title for inbound & outbound destinations
        tabLayout = findViewById(R.id.tabLayout);
        tabLayout.getTabAt(0).setText(IN_DEST);
        tabLayout.getTabAt(1).setText(OUT_DEST);

        getStopList(ROUTE, COMPANY,"inbound");  // inbound stops list
        Log.d("stop", ROUTE+" "+ COMPANY);

        lv_stopList = findViewById(R.id.lv_stopList);

//        busStopAdaptor = new BusStopAdaptor(getApplicationContext(), busStops);
//        RecyclerView rv_bus_stops_list = findViewById(R.id.rv_bus_stops_list);
//        rv_bus_stops_list.setLayoutManager((new LinearLayoutManager(BusStopListActivity.this)));
//        rv_bus_stops_list.setAdapter(busStopAdaptor);


    }

    private void getStopList(String route, String company, String bound) {
//        final String[] json_return = {""};
        String url;

        if (company.equals("KMB"))  {
            url = "https://data.etabus.gov.hk/v1/transport/kmb/route-stop/"+route+"/"+bound+"/1";
            Uri builtURI = Uri.parse(url).buildUpon().build();
            String uri_string = builtURI.toString();

            Thread thread = new Thread(new ThreadAPI(uri_string));
            thread.start();

            mHandler = new Handler(Looper.getMainLooper()) {
                public void handleMessage(@NonNull Message msg) {
                    String jsonStr = msg.obj.toString();
                    Log.d("msg", jsonStr);

                    ArrayList busStops = new ArrayList();;

                    final JSONObject[] jsonObj = {null};

                    try{
                        jsonObj[0] = new JSONObject(jsonStr);
                        JSONArray stopArr = jsonObj[0].getJSONArray("data");

                        String stopID = "";
                        final String[] stopName = {""};
                        double stopLat = 0;
                        double stopLon = 0;

                        for (int i=0; i<stopArr.length(); i++){
                            JSONObject stop = stopArr.getJSONObject(i);
                            stopID = stop.getString("stop");

                            Log.d("stop", "stopID: "+ stopID);

//                            Map stopInfo = getKMBStopName(stopID);
//                            stopName = (String) stopInfo.get("stopName");
//                            stopLat = (Double) stopInfo.get("lat");
//                            stopLon = (Double) stopInfo.get("lon");

//                            Thread thread2 = new Thread(new ThreadAPI("https://data.etabus.gov.hk/v1/transport/kmb/stop/"+stopID));
//                            thread2.start();
//                            mHandler = new Handler(Looper.getMainLooper()) {
//                                public void handleMessage(@NonNull Message msg) {
////                                    JSONObject jsonObj = null;
//                                    try {
//
//                                        JSONObject json = new JSONObject(jsonStr);
//                                        String stopName = json.getJSONObject("data").getString("name_en");
//
//                                        Log.d("stopName is", stopName);
//
//                                    } catch (JSONException e) {
//                                        e.printStackTrace();
//                                    }
//                                }};
//                            busStops.add(new BusStop(ROUTE, bound, (i+1), stopID, stopName, stopLat, stopLon));\
//                            busStops.add((i+1)+". stopID: "+stopID +" "+ getKMBStopName(stopID).get("stopName"));
                            busStops.add((i+1)+". "+getKMBStopName(stopID).get("stopName"));


                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(BusStopListActivity.this, android.R.layout.simple_list_item_1, busStops);
                    lv_stopList.setAdapter(adapter);


                }
            };

        }
        else{
            Toast.makeText(BusStopListActivity.this, "Not KMB bus", Toast.LENGTH_SHORT).show();
        }


    }

    // Get stop name from its stopID
    private Map<String, Object> getKMBStopName(String stopID) {
        Map<String, Object> stopInfo = new HashMap<>();
        String stopName;
        double lat;
        double lon;

        try{
            URL url = new URL("https://data.etabus.gov.hk/v1/transport/kmb/stop/"+stopID);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");

            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }

            in.close();
            con.disconnect();

            String jsonStr = content.toString();
            Log.d("stop json", jsonStr);

            // Assuming the JSON string is stored in a variable called jsonString
            JSONObject jsonObject = new JSONObject(jsonStr);

            stopName = jsonObject.getJSONObject("data").getString("name_en");
            lat = jsonObject.getJSONObject("data").getDouble("lat");
            lon = jsonObject.getJSONObject("data").getDouble("long");

            stopInfo.put("stopID", stopID);
            stopInfo.put("stopName", stopName);
            stopInfo.put("lat", lat);
            stopInfo.put("lon", lon);

            Log.d("stop info","stopID: "+stopID+" stopName: "+ stopName+" lat: "+lat+" lon: "+lon);
        }
        catch (Exception e){
            Log.d("stop", "Cannot get stop name\n"+e.toString());
        }

        return stopInfo;
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

    // Get JSON return from request
    class ThreadAPI implements Runnable{
        String uri_string;
        public ThreadAPI(String str){
            uri_string = str;
        }

        @Override
        public void run() {
            URL url = null;

            // a null HttpURLConnection object
            HttpURLConnection urlConnection = null;

            // a null BufferedReader
            BufferedReader reader = null;

            String jsonStr = null;

            try{
                // Init the URL object from a string
                url = new URL(uri_string);

                Log.d("Http: ", "url string is " + url);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                Log.d("Http: ", "response got");

                // get the response from the HttpURLConnection object
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    Log.d("AILEEN:", "returned");
                    return;
                }

                reader = new BufferedReader(new InputStreamReader(inputStream));
                String line;
                while ((line = reader.readLine()) != null) {
                    buffer.append(line + "\n");
                }
                if (buffer.length() == 0) {
                    return;
                }

                jsonStr = buffer.toString();

            }
            catch (Exception e){
                e.printStackTrace();
            }

            //[7] // get a Message object from recycled pool
            Message message = new Message();
            Log.d("msg", "get msg obj");
            //[8] // let the Message object carry the bookJSONString
            message.obj = jsonStr;
            Log.d("msg", "msg got string\n"+jsonStr);

            //[9] // send out the message to the handler in another thread
            mHandler.sendMessage(message);

        }
    }

}

