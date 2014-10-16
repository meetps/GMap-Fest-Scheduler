package com.iitb.moodi.gmap_fest_scheduler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {
    // 19.137559, 72.914470 hostel

    // 19.130739, 72.917208 lec hall
    RadioGroup list;
    List<String[]> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        new ReadTask().execute("http://auv-iitb.org/Web/fonts/mi_events.csv");
    }

    public void onClick(View v){
        int radioButtonID = list.getCheckedRadioButtonId();
        String latlang=getLatLang(data.get(radioButtonID)[4]);

        Intent intent = new Intent(Intent.ACTION_VIEW,
                Uri.parse("http://maps.google.com/maps?daddr="+latlang));
        startActivity(intent);
    }

    private String getLatLang(String place){
        if(place.equals("LCH"))
            return "19.130739,72.917208";
        else if(place.equals("SAC"))
            return "19.135369,72.913769";
        else if(place.equals("Footer Field"))
            return "19.134354,72.912133";
        else if(place.equals("VMCC"))
            return "19.132506,72.917260";
        else if(place.equals("FCK"))
            return "19.130484,72.915719";
        else if(place.equals("H8 Road"))
            return "19.133731,72.911319";
        else if(place.equals("KV Grounds"))
            return "19.129144,72.918190";
        else
            return "19,72";
    }

    private class ReadTask extends AsyncTask<String, Integer, List<String[]>> {
        @Override
        protected List<String[]> doInBackground(String... url) {
            List<String[]> data = new ArrayList<String[]>();
            try {
                HttpConnection http = new HttpConnection();
                data = http.getCSV(url[0]);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            if(progress[0]%10!=0) return;

            Toast t = new Toast(getApplicationContext());
            t.setText("Downloaded "+ progress[0]+"%");
            t.setDuration(Toast.LENGTH_SHORT);
        }

        @Override
        protected void onPostExecute(List<String[]> result) {
            super.onPostExecute(result);
            data=result;
            populateList();
        }
    }

    private void populateList(){
        list= (RadioGroup) findViewById(R.id.event_list);

        for(int i=0; i<data.size(); i++){
            String[] event = data.get(i);
            RadioButton rb = new RadioButton(this);

            rb.setText(event[0]+" @ "+event[4]);
            rb.setId(i);
            if(i==0) rb.setSelected(true);

            list.addView(rb);
        }
    }
}
