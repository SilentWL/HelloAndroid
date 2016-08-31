package com.example.administrator.bluetooth01;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class MainActivity extends AppCompatActivity {
    private Button scanBtn = null;
    private Button BondedDevicesBtn = null;
    private Button SetVisibleBtn = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        scanBtn = (Button)findViewById(R.id.Scan);
        BondedDevicesBtn = (Button)findViewById(R.id.BondedDevices);
        SetVisibleBtn = (Button)findViewById(R.id.Visible);

        IntentFilter intentFilter = new IntentFilter(BluetoothDevice.ACTION_FOUND);
        BluetoothReceiver bluetoothReceiver = new BluetoothReceiver();
        registerReceiver(bluetoothReceiver, intentFilter);

        BondedDevicesBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

                if (bluetoothAdapter!=null){
                    if (bluetoothAdapter.isEnabled()){
                        Intent intent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                        startActivity(intent);
                    }
                }
                Set<BluetoothDevice> devices = bluetoothAdapter.getBondedDevices();
                Log.w("BluetoothAdapter", "devices.size()=" + devices.size());

                if (devices.size() > 0){
                    for (Iterator iterator = devices.iterator(); iterator.hasNext();){
                        BluetoothDevice bluetoothDevice = (BluetoothDevice)iterator.next();
                        Log.w("BluetoothAdapter", "bluetoothDevice.getAddress()=" + bluetoothDevice.getAddress());
                    }
                }
            }
        });

        scanBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BluetoothAdapter bluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
                bluetoothAdapter.startDiscovery();
            }
        });

        SetVisibleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent setVisibleIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_DISCOVERABLE);
                setVisibleIntent.putExtra(BluetoothAdapter.EXTRA_DISCOVERABLE_DURATION, 500);
                startActivity(setVisibleIntent);
            }
        });
    }

    private class BluetoothReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)){
                BluetoothDevice bluetoothDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                Log.w("BluetoothAdapter", "BluetoothReceiver ---> bluetoothDevice=" + bluetoothDevice.getAddress());
            }
        }
    }
}
