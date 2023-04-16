package com.example.com3104_project;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

///**
// * A simple {@link Fragment} subclass.
//// * Use the {@link GMBInbound_Tab#newInstance} factory method to
// * create an instance of this fragment.
// */
public class GMBInbound_Tab extends Fragment {

    String BOUND = "inbound";
    ListView lv_stopList;
    GMB gmb;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_gmb_inoutbound__tab, container, false);

        getStopList(GMBStopActivity.gmb, BOUND);

        lv_stopList = view.findViewById(R.id.lv_stopList);

        Log.d("GMB STOP", gmb.getRoute_code() + " " +gmb.getRoute_id());



        return view;
    }

    public void getStopList(GMB gmb, String bound){

    }
}