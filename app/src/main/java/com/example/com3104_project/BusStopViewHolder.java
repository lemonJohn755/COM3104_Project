package com.example.com3104_project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusStopViewHolder extends RecyclerView.ViewHolder{

    TextView tv_bus_stop, tv_bus_fare;

    public BusStopViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_bus_stop = itemView.findViewById(R.id.tv_bus_stop);
        tv_bus_fare = itemView.findViewById(R.id.tv_bus_fare);

    }
}
