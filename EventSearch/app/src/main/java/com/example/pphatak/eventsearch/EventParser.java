package com.example.pphatak.eventsearch;

import android.text.TextUtils;
import android.util.Log;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by pphatak on 8/7/15.
 */
public class EventParser {
    private final String TAG = getClass().getName();
    private List<Event> mEvents = Collections.emptyList();

    public void parse(String jsonStr) {
        if (TextUtils.isEmpty(jsonStr)) {
            return;
        }
        JsonElement jelement = new JsonParser().parse(jsonStr);
        JsonObject root = jelement.getAsJsonObject();
        JsonObject pagination = root.getAsJsonObject("pagination");

        int objectCount = pagination.get("object_count").getAsInt();
        int pageNumber = pagination.get("page_number").getAsInt();
        int pageSize = pagination.get("page_size").getAsInt();
        int pageCount = pagination.get("page_count").getAsInt();

        JsonArray events = root.getAsJsonArray("events");
        mEvents = new ArrayList<Event>();
        for (int i = 0; i < events.size(); i++) {
            JsonObject eventObj = events.get(i).getAsJsonObject();
            JsonObject eventName = eventObj.get("name").getAsJsonObject();
            String eventNameTxt = eventName.get("text").getAsString();
            Log.d(TAG, "Event name = " + eventNameTxt);
            String eventVenueId = eventObj.get("venue_id").getAsString();
            Log.d(TAG, "Venue id = " + eventVenueId);
            JsonObject startTimeObj = eventObj.get("start").getAsJsonObject();
            String startTime = startTimeObj.get("local").getAsString();
            Log.d(TAG, "Start Time = " + startTime);

            String imgUrl = null;
            JsonElement imgElem = eventObj.get("logo");
            if (imgElem != null) {
                if (!imgElem.isJsonNull() && imgElem.isJsonObject()) {
                    JsonObject imgObj = imgElem.getAsJsonObject();
                    imgUrl = imgObj.get("url").getAsString();
                    if (!TextUtils.isEmpty(imgUrl)) {
                        Log.d(TAG, "Event logo URL = " + imgUrl);
                    } else {
                        Log.d(TAG, "No event logo");
                    }
                }
            } else {
                Log.d(TAG, "No event logo");
            }

            String venueUrl = EventManager.createVenueUrlString(eventVenueId);
            Event event = new Event(eventNameTxt, imgUrl, startTime, venueUrl);
            mEvents.add(event);
        }
    }

    public List<Event> getEvents() {
        return mEvents;
    }

    public static String parseVenue(String venueJson) {
        JsonElement jelement = new JsonParser().parse(venueJson);
        JsonObject venueObj = jelement.getAsJsonObject();
        JsonElement elem = venueObj.get("name");
        if (!elem.isJsonNull() && elem.isJsonPrimitive()) {
            String venue = elem.getAsString();
            return venue;
        }
        return "";
    }
}
