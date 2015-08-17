package com.example.pphatak.eventsearch;

/**
 * Created by pphatak on 8/7/15.
 */
public class Event {
    String mEventName;
    //String mEventDescr;
    String mImgUrl;
    String mEventStartTime;
    String mEventVenueUrl;

    public Event(String eventName, String imgUrl, String eventStartTime, String eventVenueUrl) {
        mEventName = eventName;
        mImgUrl = imgUrl;
        mEventStartTime = eventStartTime;
        mEventVenueUrl = eventVenueUrl;
    }

}
