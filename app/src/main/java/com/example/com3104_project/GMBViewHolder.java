package com.example.com3104_project;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class GMBViewHolder extends RecyclerView.ViewHolder{
    TextView tv_gmb_route, tv_gmb_out_dest, tv_gmb_in_dest;
    CardView card_gmb_route;

    public GMBViewHolder(@NonNull View itemView) {
        super(itemView);

        tv_gmb_route = itemView.findViewById(R.id.tv_gmb_route);

        tv_gmb_out_dest = itemView.findViewById(R.id.tv_gmb_out_dest);

        tv_gmb_in_dest = itemView.findViewById(R.id.tv_gmb_in_dest);

        card_gmb_route = itemView.findViewById(R.id.card_gmb_route);

    }
}
