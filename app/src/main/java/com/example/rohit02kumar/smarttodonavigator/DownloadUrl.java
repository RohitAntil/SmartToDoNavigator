package com.example.rohit02kumar.smarttodonavigator;

/**
 * Created by rohit02.kumar on 3/9/2017.
 */
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class
DownloadUrl {
String key="AIzaSyATuUiZUkEc_UgHuqsBJa1oqaODI-3mLs0";
    public String readUrl(String strUrl) throws IOException {
        String data = dowloadJson(strUrl);
           if (data.contains("error_message")) {
               Log.d("downloadUrl", "Trying again");
               strUrl = updateNearbyUrl(strUrl);
               data=dowloadJson(strUrl);
           }
        return data;
    }

    private String updateNearbyUrl(String url) {
        int index = url.indexOf("&key=");
        url=url.substring(0,index)+"&key="+key;
        return (url.toString());
    }

    String dowloadJson(String strUrl) throws IOException
    {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);
            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();
            // Connecting to url
            urlConnection.connect();
            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();

            Log.d("downloadUrl", data.toString());
            br.close();

        } catch (Exception e) {
            Log.d("Exception", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}