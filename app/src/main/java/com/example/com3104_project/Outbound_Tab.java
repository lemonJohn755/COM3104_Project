package com.example.com3104_project;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

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


public class Outbound_Tab extends Fragment {

    ListView lv_stopList;
    Handler mHandler;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_outbound__tab, container, false);

        getStopList(BusStopListActivity.bus, "outbound");

        lv_stopList = view.findViewById(R.id.lv_stopList);

        return view;
    }

    private void getStopList(Bus bus, String bound) {
//        final String[] json_return = {""};
        String url;
        String company = bus.getCompany();
        String route = bus.getRoute();


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
                        String stopName;
                        double stopLat = 0;
                        double stopLon = 0;

                        for (int i=0; i<stopArr.length(); i++){
                            JSONObject stop = stopArr.getJSONObject(i);
                            stopID = stop.getString("stop");

                            Log.d("stop", "stopID: "+ stopID);

                            Map stopInfo = getKMBStopName(stopID);
                            stopName = stopInfo.get("stopName").toString();
                            stopLat = (Double) stopInfo.get("lat");
                            stopLon = (Double) stopInfo.get("lon");

//                            Thread thread2 = new Thread(new ThreadAPI("https://data.etabus.gov.hk/v1/transport/kmb/stop/"+stopID));
//                            thread.start();
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
//                            busStops.add(new BusStop(route, bound, (i+1), stopID, stopName, stopLat, stopLon));
//                            busStops.add((i+1)+". stopID: "+stopID +" "+ getKMBStopName(stopID).get("stopName"));
                            busStops.add((i+1)+". "+getKMBStopName(stopID).get("stopName"));

                        }

                    }
                    catch (Exception e){
                        e.printStackTrace();
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, busStops);
                    lv_stopList.setAdapter(adapter);


                }
            };

        }
        else{
            Toast.makeText(getContext() , "Not KMB bus", Toast.LENGTH_SHORT).show();
        }


    }

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

    // Get JSON return from request
    class ThreadAPI implements Runnable {
        String uri_string;

        public ThreadAPI(String str) {
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

            try {
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

            } catch (Exception e) {
                e.printStackTrace();
            }

            //[7] // get a Message object from recycled pool
            Message message = new Message();
            Log.d("msg", "get msg obj");
            //[8] // let the Message object carry the bookJSONString
            message.obj = jsonStr;
            Log.d("msg", "msg got string\n" + jsonStr);

            //[9] // send out the message to the handler in another thread
            mHandler.sendMessage(message);

        }

    }

}