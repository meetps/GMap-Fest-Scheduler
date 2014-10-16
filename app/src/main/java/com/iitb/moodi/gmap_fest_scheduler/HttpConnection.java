package com.iitb.moodi.gmap_fest_scheduler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import au.com.bytecode.opencsv.CSVReader;

public class HttpConnection {

    public String readUrl(String mapsApiDirectionsUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(mapsApiDirectionsUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            BufferedReader br = new BufferedReader(new InputStreamReader(
                    iStream));
            StringBuffer sb = new StringBuffer();
            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            data = sb.toString();
            br.close();
        } catch (Exception e) {
            Log.d("Exception while reading url", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    public List<String[]> getCSV(String csvUrl) throws IOException {

        List<String[]> data=new ArrayList<String[]>();
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(csvUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.connect();
            iStream = urlConnection.getInputStream();
            CSVReader csv = new CSVReader(new InputStreamReader(
                    iStream));

            String[] next = {};

            while ((next = csv.readNext()) != null) {
                data.add(next);
            }
        } catch (Exception e) {
            Log.d("Exception while reading url", e.getMessage());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }

    return data;
    }
}
