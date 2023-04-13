package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class BusAdaptor extends RecyclerView.Adapter<BusViewHolder> {

    public BusAdaptor(Context context, List<Bus> items) {
        this.context = context;
        this.items = items;
//        this.busRouteListener = busRouteListener;
    }

//    public BusAdaptor(List<Bus> items) {
//        this.items = items;
//    }

    private Context context;
    private List<Bus> items;
//    private BusRouteListener busRouteListener;

    public void setFilteredList(List<Bus> filteredList) {
        this.items = filteredList;
        notifyDataSetChanged();
    }

//    public void setFilteredList(List<Bus> filteredList){
//        this.items = filteredList;
//        notifyDataSetChanged();
//    }

    @NonNull
    @Override
    public BusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BusViewHolder((LayoutInflater.from(context).inflate(R.layout.bus_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull BusViewHolder holder, int position) {
        holder.tv_bus_route.setText(items.get(position).getRoute());
        holder.tv_busFrom.setText(items.get(position).getFromLoc());
        holder.tv_busTo.setText(items.get(position).getToLoc());
        holder.tv_bus_company.setText(items.get(position).getCompany());
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
