package com.example.com3104_project;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class SuggestionAdaptor extends RecyclerView.Adapter<SuggestionAdaptor.SuggestionViewHolder> {

    Context context;
    List<Suggest> items;
    SuggestionListener suggestionListener;


    public SuggestionAdaptor(Context context, List<Suggest> items, SuggestionListener suggestionListener) {
        this.context = context;
        this.items = items;
        this.suggestionListener = suggestionListener;
    }

    @NonNull
    @Override
    public SuggestionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new SuggestionViewHolder((LayoutInflater.from(context).inflate(R.layout.suggest_item,parent,false)));
    }

    @Override
    public void onBindViewHolder(@NonNull SuggestionAdaptor.SuggestionViewHolder holder, int position) {
        int pos = position;

        holder.tv_route.setText(items.get(pos).getRoute());

        int durationMin = Math.round(items.get(pos).getDurationSec()/60);

        holder.tv_duration.setText(Integer.toString(durationMin));   // no. in min

        holder.card_suggest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                suggestionListener.onRouteClicked(items.get(pos));
                Log.d("select", "select: "+items.get(pos).getRouteJson());
            }
        });
    }

    @Override
    public int getItemCount() {
        return items.size();
    }


    public class SuggestionViewHolder extends RecyclerView.ViewHolder{
        TextView tv_route, tv_duration, tv_brand_name;
        CardView card_suggest;

        public SuggestionViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_route = itemView.findViewById(R.id.tv_route);

            tv_duration = itemView.findViewById(R.id.tv_duration);

            card_suggest = itemView.findViewById(R.id.card_suggest);


        }
    }
}

