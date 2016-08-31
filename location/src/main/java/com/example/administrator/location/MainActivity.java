package com.example.administrator.location;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private Button bondingBtn = null;
    private Button validProviderBtn = null;
    private Button allProviderBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bondingBtn = (Button) findViewById(R.id.BondingBtn);
        validProviderBtn = (Button)findViewById(R.id.ValidProviderBtn);
        allProviderBtn = (Button)findViewById(R.id.AllProviderBtn);

        validProviderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                Criteria criteria = new Criteria();
                criteria.setAccuracy(Criteria.ACCURACY_FINE);
                criteria.setPowerRequirement(Criteria.POWER_LOW);
                criteria.setAltitudeRequired(false);
                criteria.setSpeedRequired(false);
                criteria.setCostAllowed(false);
                String provider = locationManager.getBestProvider(criteria, false);
                Log.w("LocationListener", "best provider---->" + provider);

            }
        });

        allProviderBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                List<String> providers = locationManager.getAllProviders();

                for (Iterator iterator = providers.iterator(); iterator.hasNext();){
                    String provider = (String)iterator.next();
                    Log.w("LocationListener", "provider---->" + provider);


                }
            }
        });

        bondingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    Log.w("LocationListener", "No Permission");
                    return;
                }
                else {
                    Log.w("LocationListener", "Permission");
                    locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, new TestLocationListener());
                }
            }
        });
    }

    private class  TestLocationListener implements LocationListener{
        @Override
        public void onLocationChanged(Location location) {
            Log.w("LocationListener", "location.getLongitude=" +  location.getLongitude());
            Log.w("LocationListener", "location.getLatitude=" +  location.getLatitude());
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.w("LocationListener", "onProviderDisabled");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.w("LocationListener", "onProviderEnabled");
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.w("LocationListener", "onStatusChanged--->status=" + status);
        }
    }
}

