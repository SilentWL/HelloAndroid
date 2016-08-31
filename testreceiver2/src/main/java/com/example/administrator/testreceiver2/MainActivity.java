package com.example.administrator.testreceiver2;

import android.Manifest;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    SMSReceiver smsReceiver = new SMSReceiver();
    private final static String SMS_ACTION = "android.provider.Telephony.SMS_RECEIVED";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button Btn1 = (Button) findViewById(R.id.RegisterBtn);
        Button Btn2 = (Button)findViewById(R.id.UnregisterBtn);
        Btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                insertDummyContactWrapper();
                IntentFilter filter = new IntentFilter();
                filter.addAction(SMS_ACTION);
                MainActivity.this.registerReceiver(smsReceiver, filter);

            }
        });
        Btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MainActivity.this.unregisterReceiver(smsReceiver);
            }
        });

    }
    final private int REQUEST_CODE_ASK_PERMISSIONS = 123;
    private void insertDummyContactWrapper() {
        int hasWriteContactsPermission = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.RECEIVE_SMS);
        if (hasWriteContactsPermission != PackageManager.PERMISSION_GRANTED) {


            return;
        }

    }
}
