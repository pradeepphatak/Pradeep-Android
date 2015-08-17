package com.example.pphatak.eventsearch;

import java.util.List;

/**
 * Created by pphatak on 8/7/15.
 */
public class PaginatedResponse {
    int mObjectCount;
    int mPageNumber;
    int mPageSize;
    int mPageCount;
    List<Event> events;
}
