package com.example.com3104_project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class BusViewHolder extends RecyclerView.ViewHolder {

    TextView tv_bus_route, tv_busTo, tv_busFrom, tv_bus_company;

    public BusViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_bus_route = itemView.findViewById(R.id.tv_bus_route);

        tv_busTo = itemView.findViewById(R.id.tv_busTo);

        tv_busFrom = itemView.findViewById(R.id.tv_busFrom);

        tv_bus_company = itemView.findViewById(R.id.tv_bus_company);

    }
}
