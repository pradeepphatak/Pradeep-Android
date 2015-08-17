package com.example.pphatak.eventsearch;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.Collections;
import java.util.List;

/**
 * Created by pphatak on 8/7/15.
 */
public class EventAdapter extends RecyclerView.Adapter<EventViewHolder> {
    private LayoutInflater mInflater;
    List<Event> mEvents = Collections.emptyList();
    ImageLoader mImageLoader;
    VenueLoader mVenueLoader;

    public EventAdapter(Context context, List<Event> events) {
        mInflater = LayoutInflater.from(context);
        mEvents = events;
        mImageLoader = new ImageLoader(context);
        mVenueLoader = new VenueLoader(context);
    }

    @Override
    public EventViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.event_row, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(EventViewHolder holder, int position) {
        Event event = mEvents.get(position);
        if (event != null) {
            holder.eventName.setText(event.mEventName);
            holder.eventStartTime.setText(event.mEventStartTime);
            ImageView imageView = holder.eventImg;
            if (!TextUtils.isEmpty(event.mImgUrl)) {
                mImageLoader.displayImage(event.mImgUrl, imageView);
            }

            if (!TextUtils.isEmpty(event.mEventVenueUrl)) {
                mVenueLoader.displayVenue(event.mEventVenueUrl, holder.eventVenue);
            }
        }
    }

    @Override
    public int getItemCount() {
        return mEvents.size();
    }
}
