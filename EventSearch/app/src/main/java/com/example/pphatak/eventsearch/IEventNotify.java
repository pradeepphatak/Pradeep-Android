package com.example.pphatak.eventsearch;

import java.util.List;

/**
 * Created by pphatak on 8/8/15.
 */
public interface IEventNotify {
    void onEventsReceived(List<Event> events);
}
