package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GMBAdaptor extends RecyclerView.Adapter<GMBViewHolder> {

    Context context;
    List<GMB> items;

    public GMBAdaptor(Context context, List<GMB> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public GMBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GMBViewHolder((LayoutInflater.from(context).inflate(R.layout.gmb_route_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull GMBViewHolder holder, int position) {
        int pos = position;

        String route_id = items.get(pos).getRoute_code();

        holder.tv_gmb_route.setText(route_id);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }
}
