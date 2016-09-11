package com.example.pphatak.eventsearch;

import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Implements API to get Event information from the server or from the stored JSON data.
 * Created by pphatak on 8/8/15.
 */
public class EventManager implements IEventManager{
    private Handler mHandler;
    private static final int EVENT_LIST_RECEIVED = 1;
    private static final int EVENT_LIST_NOT_RECEIVED = 2;
    private final String TAG = getClass().getName();
    private static final String EVENTBRITE_API_EVENT_SEARCH = "https://www.eventbriteapi.com/v3/events/search/";
    private static final String KEYWORD_PARAM = "q";
    private static final String LOCATION_PARAM = "location.address";
    public static final String TOKEN_PARAM = "token";
    public static final String TOKEN_VALUE = "TQV3XOU5XAZT5QYDJCAX";
    public static final String EVENTBRITE_API_VENUE_SEARCH = "https://www.eventbriteapi.com/v3/venues/";
    private String mEventsJson;
    private WeakReference<IEventNotify> mEventNotify;

    public EventManager(IEventNotify eventNotify) {
        mEventNotify = new WeakReference<>(eventNotify);
        mHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                List<Event> events = (List<Event>)msg.obj;
                IEventNotify evtNotify= (IEventNotify) mEventNotify.get();
                if (evtNotify == null) {
                    Log.e(TAG, "Main Activity is null");
                    return;
                }
                switch(msg.what) {
                    case EVENT_LIST_RECEIVED:
                        Log.d(TAG, "Event list updated");
                        evtNotify.onEventsReceived(events);
                        break;
                    case EVENT_LIST_NOT_RECEIVED:
                        Log.d(TAG, "Event list not updated");
                        evtNotify.onEventsReceived(null);
                        break;
                }
            }

        };
    }

    @Override
    /**
     * {@inheritDoc}
     */
    public void getEvents(final String keyword, final String location) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                NetworkClient client = new NetworkClient();
                String url = createSearchUrlString(keyword, location);
                String jsonStr = client.getContent(url);
                if (!TextUtils.isEmpty(jsonStr)) {
                    mEventsJson = jsonStr;
                    EventParser parser = new EventParser();
                    parser.parse(jsonStr);
                    List<Event> events = parser.getEvents();
                    Message eventsRcdMsg = mHandler.obtainMessage();
                    eventsRcdMsg.what = EVENT_LIST_RECEIVED;
                    eventsRcdMsg.obj = events;
                    Log.d(TAG, "Send Message: Event list updated");
                    mHandler.sendMessage(eventsRcdMsg);
                } else {
                    Message eventsRcdMsg = mHandler.obtainMessage();
                    eventsRcdMsg.what = EVENT_LIST_NOT_RECEIVED;
                    mHandler.sendMessage(eventsRcdMsg);
                }
            }
        }).start();
    }

    /**
     * Get Last JSON data received from server
     * @return
     */
    public String getEventJsonData() {
        return mEventsJson;
    }

    /**
     * Get events list for the given JSON string
     * @param eventJson
     * @return
     * List containing Event objects
     */
    public List<Event> getEventsFromJson(String eventJson) {
        if (TextUtils.isEmpty(eventJson)) {
            return null;
        }
        mEventsJson = eventJson;
        EventParser parser = new EventParser();
        parser.parse(eventJson);
        List<Event> events = parser.getEvents();
        return events;
    }

    /**
     * Create URL string for API call
     * @param keyword
     * @param location
     * @return
     * URL string
     */
    private String createSearchUrlString(String keyword, String location) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.eventbriteapi.com")
                .appendPath("v3")
                .appendPath("events")
                .appendPath("search")
                .appendQueryParameter(KEYWORD_PARAM, keyword)
                .appendQueryParameter(LOCATION_PARAM, location)
                .appendQueryParameter(TOKEN_PARAM, TOKEN_VALUE);

        String urlStr = builder.build().toString();
        return urlStr;
    }

    public static String createVenueUrlString(String venueId) {
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("www.eventbriteapi.com")
                .appendPath("v3")
                .appendPath("venues")
                .appendPath(venueId)
                .appendQueryParameter(TOKEN_PARAM, TOKEN_VALUE);

        String urlStr = builder.build().toString();
        return urlStr;
    }
}
