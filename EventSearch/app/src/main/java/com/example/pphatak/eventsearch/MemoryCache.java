package com.example.pphatak.eventsearch;

/**
 * Created by pphatak on 8/8/15.
 */
import android.graphics.Bitmap;
import android.util.Log;

import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 *  Implement Memory cache for images downloaded from server.
 */
public class MemoryCache {

    private static final String TAG = "MemoryCache";

    // Last argument true for LRU ordering
    private Map<String, Bitmap> mCache = Collections
            .synchronizedMap(new LinkedHashMap<String, Bitmap>(10, 1.5f, true));

    // Current allocated mSize
    private long mSize = 0;

    // Max memory in bytes
    private long mLimit = 1000000;

    public MemoryCache() {
        // Use 25% of available heap mSize
        setLimit(Runtime.getRuntime().maxMemory() / 4);
    }

    public void setLimit(long new_limit) {
        mLimit = new_limit;
        Log.i(TAG, "MemoryCache will use up to " + mLimit / 1024. / 1024. + "MB");
    }

    public Bitmap get(String id) {
        try {
            if (!mCache.containsKey(id)) {
                return null;
            } else {
                return mCache.get(id);
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public void put(String id, Bitmap bitmap) {
        try {
            if (mCache.containsKey(id)) {
                mSize -= getSizeInBytes(mCache.get(id));
            }
            mCache.put(id, bitmap);
            mSize += getSizeInBytes(bitmap);
            checkSize();
        } catch (Throwable th) {
            th.printStackTrace();
        }
    }

    private void checkSize() {
        Log.i(TAG, "mCache mSize=" + mSize + " length=" + mCache.size());
        if (mSize > mLimit) {
            // Least recently accessed item will be the first one iterated
            Iterator<Entry<String, Bitmap>> iter = mCache.entrySet().iterator();
            while (iter.hasNext()) {
                Entry<String, Bitmap> entry = iter.next();
                mSize -= getSizeInBytes(entry.getValue());
                iter.remove();
                if (mSize <= mLimit)
                    break;
            }
            Log.i(TAG, "Clean mCache. New mSize " + mCache.size());
        }
    }

    public void clear() {
        try {
            mCache.clear();
            mSize = 0;
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }
    }

    long getSizeInBytes(Bitmap bitmap) {
        if (bitmap == null)
            return 0;
        return bitmap.getRowBytes() * bitmap.getHeight();
    }
}
