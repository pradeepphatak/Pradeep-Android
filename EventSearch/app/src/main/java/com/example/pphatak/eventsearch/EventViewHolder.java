package com.example.pphatak.eventsearch;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by pphatak on 8/7/15.
 */
public class EventViewHolder extends RecyclerView.ViewHolder {
    TextView eventName;
    TextView eventStartTime;
    TextView eventVenue;
    ImageView eventImg;

    public EventViewHolder(View itemView) {
        super(itemView);
        eventName = (TextView)itemView.findViewById(R.id.eventName);
        eventStartTime = (TextView)itemView.findViewById(R.id.eventStartTime);
        eventVenue = (TextView)itemView.findViewById(R.id.eventVenue);
        eventImg = (ImageView)itemView.findViewById(R.id.eventImg);
    }
}
