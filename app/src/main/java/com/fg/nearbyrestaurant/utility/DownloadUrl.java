package com.fg.nearbyrestaurant.utility;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by fred on 3/30/2017.
 */

public class DownloadUrl {
    private String DOWNLOADURL = "downloadUrl";

    public String readUrl(String strUrl) throws IOException {
        String data = "";
        InputStream inputStream = null;
        HttpURLConnection urlConnection = null;

        try {
            URL url = new URL(strUrl); // initialize the URL
            urlConnection = (HttpURLConnection) url.openConnection();// open connection
            urlConnection.connect(); // connect

            inputStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(inputStream));

            StringBuffer sb = new StringBuffer();
            String line = "";
            while((line = br.readLine()) != null){
                sb.append(line);
            }
            data = sb.toString();
            Log.d(DOWNLOADURL, data.toString());
            br.close();

        } catch (MalformedURLException e){
            e.printStackTrace();
        } finally {
            inputStream.close();
            urlConnection.disconnect();
        }
        return data;
    }
}
