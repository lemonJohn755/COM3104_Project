package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusStopAdaptor extends RecyclerView.Adapter<BusStopViewHolder> {

    public BusStopAdaptor(Context context, List<BusStop> busStops) {
        this.context = context;
        this.busStops = busStops;
    }

    Context context;
    List<BusStop> busStops;

    @NonNull
    @Override
    public BusStopViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusStopViewHolder((LayoutInflater.from(context).inflate(R.layout.bus_stop_item ,parent,false)));

    }

    @Override
    public void onBindViewHolder(@NonNull BusStopViewHolder holder, int position) {
        holder.tv_bus_stop.setText(busStops.get(position).getStopName());
        holder.tv_bus_fare.setText("$Fare");

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
