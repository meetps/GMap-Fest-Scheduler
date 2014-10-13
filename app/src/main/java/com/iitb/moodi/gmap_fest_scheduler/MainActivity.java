package com.iitb.moodi.gmap_fest_scheduler;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioGroup;


public class MainActivity extends Activity {
    // 19.137559, 72.914470 hostel

    // 19.130739, 72.917208 lec hall
    RadioGroup list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        list= (RadioGroup) findViewById(R.id.event_list);
    }

    public void onClick(View v){
        int radioButtonID = list.getCheckedRadioButtonId();
        String latlang="";

        switch(radioButtonID){
            case R.id.convo:
                latlang="19.132069,72.914452";
                break;
            case R.id.lec_hall:
                latlang="19.130739,72.917208";
                break;
            case R.id.vmcc:
                latlang="19.132479,72.917279";
                break;
        }

        Intent intent = new Intent();
        intent.putExtra("url","https://maps.googleapis.com/maps/api/directions/json?destination="+latlang);
        intent.setClass(this, PathDisplayActivity.class);
        startActivity(intent);
    }
}
