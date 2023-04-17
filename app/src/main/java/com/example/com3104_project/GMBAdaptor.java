package com.example.com3104_project;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class GMBAdaptor extends RecyclerView.Adapter<GMBAdaptor.GMBViewHolder> {

    Context context;
    List<GMB> items;
    GMBListener gmbListener;


    public GMBAdaptor(Context context, List<GMB> items, GMBListener gmbListener) {
        this.context = context;
        this.items = items;
        this.gmbListener = gmbListener;
    }

    @NonNull
    @Override
    public GMBViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new GMBViewHolder((LayoutInflater.from(context).inflate(R.layout.gmb_route_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull GMBAdaptor.GMBViewHolder holder, int position) {
        int pos = position;

        String route_id = items.get(pos).getRoute_code();
        String region = items.get(pos).getRegion();

        if (region.equals("HKI")){
            region = "Hong Kong Island";
        }
        else if (region.equals("KLN")){
            region = "Kowloon";
        }
        else if (region.equals("NT")){
            region = "New Territories";
        }

        holder.tv_gmb_route.setText(route_id);

        holder.tv_gmb_region.setText(region);

        holder.card_gmb_route.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gmbListener.onRouteClicked(items.get(pos));
            }
        });
    }

    public void update(List<GMB> items){
        this.items = items;
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void setFilteredList(List<GMB> filteredList) {
        this.items = filteredList;
        notifyDataSetChanged();
    }

    public class GMBViewHolder extends RecyclerView.ViewHolder{
        TextView tv_gmb_route, tv_gmb_out_dest, tv_gmb_in_dest, tv_gmb_region;
        CardView card_gmb_route;

        public GMBViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_gmb_route = itemView.findViewById(R.id.tv_gmb_route);

//            tv_gmb_out_dest = itemView.findViewById(R.id.tv_gmb_out_dest);
//
//            tv_gmb_in_dest = itemView.findViewById(R.id.tv_gmb_in_dest);

            tv_gmb_region = itemView.findViewById(R.id.tv_gmb_region);

            card_gmb_route = itemView.findViewById(R.id.card_gmb_route);

        }
    }
}

