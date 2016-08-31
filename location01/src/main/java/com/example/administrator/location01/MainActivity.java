package com.example.administrator.location01;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button geocodingBtn = null;
    private Button reversegeocodingBtn = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        geocodingBtn = (Button)findViewById(R.id.geocodingBtn);
        reversegeocodingBtn = (Button)findViewById(R.id.reversegeocodingBtn);

        geocodingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new GeocodingTask().execute();

            }
        });

        reversegeocodingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Geocoder reversegeocoder = new Geocoder(MainActivity.this, Locale.CHINA);

                    List<Address> addresses = reversegeocoder.getFromLocation(40.061191, 116.579344, 10);
                    Log.w("GeocodingTask", "SFO---->" + addresses.size());
                }catch(Exception e){
                    e.printStackTrace();
                }

            }
        });
    }

    private class GeocodingTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... params) {
            try{
                Geocoder geocoder = new Geocoder(MainActivity.this);
                List<Address> addresses = geocoder.getFromLocationName("北京首都机场", 10);
                Log.w("GeocodingTask", "SFO---->" + addresses.size());

            }catch (Exception e){
                e.printStackTrace();
            }
            return null;
        }

    }
}
