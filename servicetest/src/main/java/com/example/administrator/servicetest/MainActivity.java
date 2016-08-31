package com.example.administrator.servicetest;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button StartBtn = (Button)findViewById(R.id.StartService);
        StartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, FirstService.class);
                startService(it);
                System.out.println("Start Service Button ----->Thread Id:" + Thread.currentThread().getId());
            }
        });

        Button StopBtn = (Button)findViewById(R.id.StopService);
        StopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent();
                it.setClass(MainActivity.this, FirstService.class);
                stopService(it);
            }
        });
    }
}
