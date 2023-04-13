package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusAdaptor extends RecyclerView.Adapter<BusViewHolder> {
    Context context;
    List<Bus> items;
    BusRouteListener busRouteListener;

    public BusAdaptor(Context context, List<Bus> items, BusRouteListener busRouteListener) {
        this.context = context;
        this.items = items;
        this.busRouteListener = busRouteListener;
    }

    public void setFilteredList(List<Bus> filteredList) {
        this.items = filteredList;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusViewHolder((LayoutInflater.from(context).inflate(R.layout.bus_routes_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder,int position) {
        int pos = position;

        holder.tv_bus_route.setText(items.get(position).getRoute());
        holder.tv_busFrom.setText(items.get(position).getFromLoc());
        holder.tv_busTo.setText(items.get(position).getToLoc());
        holder.tv_bus_company.setText(items.get(position).getCompany());

        holder.card_bus_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                busRouteListener.onRouteClicked(items.get(pos));
            }
        });


    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
