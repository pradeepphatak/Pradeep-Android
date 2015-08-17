package com.example.pphatak.eventsearch;

import android.content.Context;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Implement thread pool executor service to download venue information from server.
 * Uses NetworkClient to download data.
 * Created by pphatak on 8/9/15.
 */
public class VenueLoader {
    ExecutorService mExecutorService;
    Handler mHandler;
    private final String TAG = getClass().getName();
    Map<String, String> mVenueMap;

    public VenueLoader(Context context) {
        mHandler = new Handler();
        mExecutorService = Executors.newFixedThreadPool(5);
        mVenueMap = Collections.synchronizedMap(new HashMap<String, String>());
    }

    public void displayVenue(String url, TextView textView) {
        String venueName = mVenueMap.get(url);
        if (venueName != null) {
            Log.d(TAG, "venue name in hashmap : " + venueName);
            textView.setText(venueName);
        } else {
            queueVenue(url, textView);
        }
    }

    private void queueVenue(String url, TextView imageView) {
        Log.d(TAG, "queueVenue: url = " + url);
        VenueToLoad p = new VenueToLoad(url, imageView);
        mExecutorService.submit(new VenueNameLoader(p));
    }

    private String getVenue(String url) {
        NetworkClient networkClient = new NetworkClient();
        String venueStr = networkClient.getContent(url);
        if (!TextUtils.isEmpty(venueStr)) {
            String venueName = EventParser.parseVenue(venueStr);
            Log.d(TAG, "venueName = " + venueName);
            return venueName;
        }
        return null;
    }

    // Task for the queue
    private class VenueToLoad {
        public String url;
        public TextView textView;

        public VenueToLoad(String u, TextView t) {
            url = u;
            textView = t;
        }
    }

    class VenueNameLoader implements Runnable {
        VenueToLoad venueToLoad;

        VenueNameLoader(VenueToLoad venueToLoad) {
            this.venueToLoad = venueToLoad;
        }

        @Override
        public void run() {
            try {
                String venue = getVenue(venueToLoad.url);
                mVenueMap.put(venueToLoad.url, venue);
                VenueDisplayer vd = new VenueDisplayer(venue, venueToLoad);
                mHandler.post(vd);
            } catch (Throwable th) {
                th.printStackTrace();
            }
        }
    }

    class VenueDisplayer implements Runnable {
        String mVenue;
        VenueToLoad mVenueToLoad;
        VenueDisplayer(String venue, VenueToLoad venueToLoad) {
            mVenue = venue;
            mVenueToLoad = venueToLoad;
        }

        public void run() {
            mVenueToLoad.textView.setText(mVenue);
        }

    }
}
