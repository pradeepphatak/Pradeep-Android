package com.example.pphatak.eventsearch;

/**
 * Created by pphatak on 8/8/15.
 */
import android.content.Context;

import java.io.File;

/**
 *
 *  Implement File cache for images downloaded from server.
 *  Third party attributions:
 *  Original source- http://www.androidbegin.com/tutorial/android-json-parse-images-and-texts-tutorial/
 *  Modified as required.
 *
 */
public class FileCache {

    private File mCacheDir;

    public FileCache(Context context) {
        // Find the dir to save cached images
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            mCacheDir = new File(
                    android.os.Environment.getExternalStorageDirectory(),
                    "EventSearch");
        } else {
            mCacheDir = context.getCacheDir();
        }
        if (!mCacheDir.exists()) {
            mCacheDir.mkdirs();
        }
    }

    public File getFile(String url) {
        String filename = String.valueOf(url.hashCode());
        File f = new File(mCacheDir, filename);
        return f;
    }

    public void clear() {
        File[] files = mCacheDir.listFiles();
        if (files == null) {
            return;
        }
        for (File f : files) {
            f.delete();
        }
    }

}