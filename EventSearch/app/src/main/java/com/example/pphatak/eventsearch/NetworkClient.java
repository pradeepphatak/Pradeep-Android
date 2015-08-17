package com.example.pphatak.eventsearch;

import android.net.Uri;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by pphatak on 8/6/15.
 */
public class NetworkClient {
    private final String TAG = this.getClass().getName();

    public String getContent(String urlStr) {
        try {
            Log.d(TAG, "urlStr = " + urlStr);
            URL url = new URL(Uri.parse(urlStr).toString());
            URLConnection urlConn = url.openConnection();
            InputStream in = urlConn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder sb = new StringBuilder();
            String line = null;

            // Read Server Response
            while((line = reader.readLine()) != null)
            {
                // Append server response in string
                sb.append(line);
            }
            // Append Server Response To Content String
            String content = sb.toString();
            Log.d(TAG, content);
            return content;
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    //TODO: Catching generic Exception
    public static void CopyStream(InputStream is, OutputStream os) {
        final int buffer_size=1024;
        try
        {

            byte[] bytes = new byte[buffer_size];
            for(;;)
            {
                //Read byte from input stream
                int count = is.read(bytes, 0, buffer_size);
                if(count == -1)
                    break;
                //Write byte from output stream
                os.write(bytes, 0, count);
            }
        }
        catch(Exception ex){}
    }
}
