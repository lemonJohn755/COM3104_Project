package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LegsAdaptor extends RecyclerView.Adapter<LegsAdaptor.LegsViewHolder> {

    Context context;
    List<Leg> items;
//    List<LegStop> childItems;

    LegStopsAdaptor legStopsAdaptor;

    RecyclerView rv_child_stops;

    public LegsAdaptor(Context context, List<Leg> items) {
        this.context = context;
        this.items = items;
//        this.childItems = childItems;
    }

    @NonNull
    @Override
    public LegsAdaptor.LegsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LegsAdaptor.LegsViewHolder((LayoutInflater.from(context).inflate(R.layout.suggest_legs_item,parent,false)));

    }

    @Override
    public void onBindViewHolder(@NonNull LegsAdaptor.LegsViewHolder holder, int position) {
        int pos = position;

//        holder.tv_num.setText((pos+1) +".");
        String despcription;
        if (items.get(pos).getTravel_mode().equals("walk")){
            despcription = capitalizeWords(items.get(pos).getTravel_mode()) ;
        }else{
            despcription = capitalizeWords(items.get(pos).getTravel_mode()) +" on " + capitalizeWords(items.get(pos).getVehicle_types());
        }

        holder.tv_travel_mode_vehicle_types.setText( despcription);

//        holder.tv_vehicle_types.setText(items.get(pos).getVehicle_types());

        String brand = items.get(pos).getBrand();
        String name = items.get(pos).getName();

        if(items.get(pos).getTravel_mode().equals("walk")){
            holder.tv_brand_name.setText(name);
        }else{
            holder.tv_brand_name.setText(brand+": "+name);
        }

        String duration_min = Integer.toString(Math.round(items.get(pos).getDuration_seconds()/60));

        holder.tv_duration.setText(duration_min);

        // Nested RecycleView list to display all stops (if available)
        List<LegStop >lenStopList = items.get(pos).getLegStopList();

        if ( lenStopList != null){
            legStopsAdaptor = new LegStopsAdaptor(context, lenStopList);
            RecyclerView rv_child_stops = holder.rv_child_stops;
            rv_child_stops.setLayoutManager((new LinearLayoutManager(context)));
            rv_child_stops.setAdapter(legStopsAdaptor);
        }


    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public static String capitalizeWords(String str) {
        String[] words = str.split("\\s+");
        StringBuilder capitalized = new StringBuilder();
        for (String word : words) {
            if (word.length() > 0) {
                String firstLetter = word.substring(0, 1);
                String restOfWord = word.substring(1);
                capitalized.append(firstLetter.toUpperCase()).append(restOfWord).append(" ");
            }
        }
        return capitalized.toString().trim();
    }


    public class LegsViewHolder extends RecyclerView.ViewHolder {
        TextView tv_num, tv_travel_mode_vehicle_types, tv_duration ,tv_brand_name;
        RecyclerView rv_child_stops;

        public LegsViewHolder(View itemview) {
            super(itemview);
//            tv_num = itemview.findViewById(R.id.tv_num);

            tv_travel_mode_vehicle_types = itemview.findViewById(R.id.tv_travel_mode_vehicle_types);

//            tv_vehicle_types = itemview.findViewById(R.id.tv_vehicle_types);

            tv_brand_name = itemview.findViewById(R.id.tv_brand_name);

            tv_duration = itemview.findViewById(R.id.tv_duration);

            rv_child_stops = itemview.findViewById(R.id.rv_child_stops);

        }
    }

}
