package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class LegStopsAdaptor extends RecyclerView.Adapter<LegStopsAdaptor.LegsStopsViewHolder>{

    Context context;
    List<LegStop> childItems;

    public LegStopsAdaptor(Context context, List<LegStop> childItems) {
        this.context = context;
        this.childItems = childItems;
    }

    @NonNull
    @Override
    public LegsStopsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new LegStopsAdaptor.LegsStopsViewHolder((LayoutInflater.from(context)
                .inflate(R.layout.suggest_legs_stops_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull LegsStopsViewHolder holder, int position) {
        int pos = position;

        String stopName = childItems.get(pos).getName();
        holder.tv_stop_name.setText(stopName);

    }

    @Override
    public int getItemCount() {
        return childItems.size();
    }

    public class LegsStopsViewHolder extends RecyclerView.ViewHolder{
        TextView tv_stop_name;
        public LegsStopsViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_stop_name = itemView.findViewById(R.id.tv_stop_name);

        }
    }
}

