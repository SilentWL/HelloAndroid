package com.example.administrator.wifi;

import android.content.Context;
import android.net.wifi.WifiManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    //private WifiManager wm = (WifiManager)MainActivity.this.getSystemService(MainActivity.this.WIFI_SERVICE);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button StartBtn = (Button) findViewById(R.id.Start);
        Button StopBtn = (Button)findViewById(R.id.Stop);
        Button CheckBtn = (Button)findViewById(R.id.Check);

        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wm = (WifiManager)MainActivity.this.getSystemService(MainActivity.this.WIFI_SERVICE);
                wm.setWifiEnabled(true);
                System.out.println("WIFI state---->" + wm.getWifiState());
                Toast.makeText(MainActivity.this, "当前WIFI状态为" + wm.getWifiState(), Toast.LENGTH_SHORT).show();
            }
        });

        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wm = (WifiManager)MainActivity.this.getSystemService(MainActivity.this.WIFI_SERVICE);
                wm.setWifiEnabled(false);
                System.out.println("WIFI state---->" + wm.getWifiState());
                Toast.makeText(MainActivity.this, "当前WIFI状态为" + wm.getWifiState(), Toast.LENGTH_SHORT).show();

            }
        });

        CheckBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                WifiManager wm = (WifiManager)MainActivity.this.getSystemService(MainActivity.this.WIFI_SERVICE);
                System.out.println("WIFI state---->" + wm.getWifiState());
                Toast.makeText(MainActivity.this, "当前WIFI状态为" + wm.getWifiState(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
