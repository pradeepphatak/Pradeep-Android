package com.example.pphatak.eventsearch;

import java.util.List;

/**
 * Created by pphatak on 8/8/15.
 */
public interface IEventManager {
    /**
     * Asynchronous method to get events using EventBrite REST API.
     *
     * @param keyword
     * @param location
     */
    void getEvents(String keyword, String location);

    /**
     * Get events from JSON string
     * @param eventJson
     * @return
     */
    public List<Event> getEventsFromJson(String eventJson);
}
