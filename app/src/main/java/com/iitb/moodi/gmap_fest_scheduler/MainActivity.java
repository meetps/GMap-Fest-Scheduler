package com.iitb.moodi.gmap_fest_scheduler;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity {
    // 19.137559, 72.914470 hostel

    // 19.130739, 72.917208 lec hall
    RadioGroup list;
    List<String[]> data;

    LatLng myLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        list = (RadioGroup) findViewById(R.id.event_list);

        // Acquire a reference to the system Location Manager
        LocationManager locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);

        Location myLoc = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        myLocation = new LatLng(myLoc.getLatitude(),myLoc.getLongitude());

// Define a listener that responds to location updates
        LocationListener locationListener = new LocationListener() {
            public void onLocationChanged(Location location) {
                // Called when a new location is found by the network location provider.
                myLocation=new LatLng(location.getLatitude(),location.getLongitude());
            }

            public void onStatusChanged(String provider, int status, Bundle extras) {}

            public void onProviderEnabled(String provider) {}

            public void onProviderDisabled(String provider) {}
        };

// Register the listener with the Location Manager to receive location updates
        locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, locationListener);

        new ReadTask().execute("https://drive.google.com/uc?execute=download&id=0B_6rvZNWXShpV0t0ZWxlNkQ5UE0");
    }

    public void onClick(View v){
        int radioButtonID = list.getCheckedRadioButtonId();
        LatLng destination=getLatLang(data.get(radioButtonID)[4]);

        Intent intent = new Intent();
        intent.putExtra("destination",destination);
        intent.putExtra("myLocation",myLocation);
        intent.setClass(this, PathDisplayActivity.class);
        startActivity(intent);
    }

    private LatLng getLatLang(String place){
        if(place.equals("LCH"))
            return new LatLng(19.130739,72.917208);
        else if(place.equals("SAC"))
            return new LatLng(19.135369,72.913769);
        else if(place.equals("Footer Field"))
            return new LatLng(19.134354,72.912133);
        else if(place.equals("VMCC"))
            return new LatLng(19.132506,72.917260);
        else if(place.equals("FCK"))
            return new LatLng(19.130484,72.915719);
        else if(place.equals("H8 Road"))
            return new LatLng(19.133731,72.911319);
        else if(place.equals("KV Grounds"))
            return new LatLng(19.129144,72.918190);
        else
            return new LatLng(19,72);
    }

    private class ReadTask extends AsyncTask<String, Integer, List<String[]>> {
        @Override
        protected List<String[]> doInBackground(String... url) {
            List<String[]> data = new ArrayList<String[]>();
            try {
                HttpConnection http = new HttpConnection();
                data = http.getCSV(url[0]);
                //Log.d("Data",http.readUrl(url[0]));
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            Log.d("Download Debug","progress:"+progress);
        }

        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);
            data=result;
            new Timer().scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    list.post(new Runnable() {
                        @Override
                        public void run() {
                            populateList();
                        }
                    });
                }
            }, 0, 5000);
            Log.d("Download Debug","finished executing");
        }
    }

    private void populateList(){
        list= (RadioGroup) findViewById(R.id.event_list);
        list.removeAllViews();

        int system_time=(int)(System.currentTimeMillis()%86400000)/1000+19800;
        int time_range_start=system_time-60;
        int time_range_end=system_time+60;

        Log.d("Timer Debug", "sys time : "+system_time);

        for(int i=0; i<data.size(); i++){
            String[] event = data.get(i);
            String[] hhmm=event[3].split(":");
            int event_time = Integer.parseInt(hhmm[0])*3600+Integer.parseInt(hhmm[1])*60;

            Log.d("Timer Debug","event time : "+event_time);

            if(event_time<time_range_start || event_time>time_range_end) continue;

            RadioButton rb = new RadioButton(this);

            rb.setText(event[0]+" @ "+event[4]);
            rb.setId(i);
            if(i==0) rb.setSelected(true);

            list.addView(rb);
        }
    }
}
