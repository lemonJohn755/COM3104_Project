package com.example.com3104_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

///**
// * A simple {@link Fragment} subclass.
//// * Use the {@link GMBInbound_Tab#newInstance} factory method to
// * create an instance of this fragment.
// */
public class GMBInbound_Tab extends Fragment {

    String BOUND = "2";     // 1 for outbound; 2 for inbound for GMB API
    String route_id;
    String route_code;
    String region;
    ListView lv_stopList;
    GMB gmb;

    ArrayList stopList = new ArrayList();


    private RequestQueue mRequestQueue;
    private StringRequest mStringRequest;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        gmb = GMBStopActivity.gmb;


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gmb_inoutbound__tab, container, false);

        route_code = gmb.getRoute_code();
        region = gmb.getRegion();

        getRouteID(region, route_code);

        lv_stopList = view.findViewById(R.id.lv_stopList);

        return view;
    }

    public void getRouteID(String region, String route_code) {
        String url = "https://data.etagmb.gov.hk/route/"+region+"/"+route_code;
        Log.d("volley", "url "+url);

        //RequestQueue initialized
        mRequestQueue = Volley.newRequestQueue(GMBInbound_Tab.this.getContext());

        //String Request initialized
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getContext(),"Response :" + response, Toast.LENGTH_LONG).show();//display the response on screen
                Log.d("volley", response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    JSONObject dataObject = dataArray.getJSONObject(0);
                    route_id = dataObject.getString("route_id");
                    gmb.setRoute_id(route_id);
                    Log.d("volley", route_id);

                    getStopList(route_id, BOUND);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley","Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);

    }

    public void getStopList(String route_id, String bound){
        String url = "https://data.etagmb.gov.hk/route-stop/"+route_id+"/"+bound;


        mRequestQueue = Volley.newRequestQueue(GMBInbound_Tab.this.getContext());
        mStringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

//                Toast.makeText(getContext(),"Response :" + response, Toast.LENGTH_LONG).show();//display the response on screen
                Log.d("volley", response);

                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String stopName;

                    JSONArray routeStops = jsonObject.getJSONObject("data").getJSONArray("route_stops");
                    for (int i = 0; i < routeStops.length(); i++) {
                        JSONObject stop = routeStops.getJSONObject(i);
                        stopName = stop.getString("name_en");
                        Log.d("stops", (i+1) +". " +stopName);
                        stopList.add((i+1) +". " +stopName);
                    }


                } catch (JSONException e) {
                    e.printStackTrace();
                }

                ArrayAdapter adapter = new ArrayAdapter(getContext(), android.R.layout.simple_list_item_1, stopList);
                lv_stopList.setAdapter(adapter);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("volley","Error :" + error.toString());
            }
        });

        mRequestQueue.add(mStringRequest);


    }
}