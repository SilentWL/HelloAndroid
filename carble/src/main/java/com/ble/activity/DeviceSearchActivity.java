package com.ble.activity;

import android.annotation.TargetApi;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.ble.Animation.RefreshingAnimation;
import com.ble.service.BluetoothLeService;
import com.ble.systembar.SystemBarTintManager;
import com.example.administrator.carble.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class DeviceSearchActivity extends Activity{
    private static final int STATUS_BAR_COLOR = 0X70000000;
    private static final long SCAN_PERIOD = 10000;

    private RefreshingAnimation refreshingImageRotateAnimation = null;

    private ListView deviceListView = null;


    private BluetoothAdapter mBluetoothAdapter;
    private Handler mHandler;
    private boolean mScanning;
    private int mConnectingDeviceIndex = -1;

    private ArrayList<HashMap<String, String>> deviceListArrayShow = new ArrayList<HashMap<String, String>>();
    private ArrayList<BluetoothDevice> deviceListArray = new ArrayList<BluetoothDevice>();
    private SimpleAdapter simpleAdapter = null;

    private final String LIST_NAME = "NAME";
    private final String LIST_UUID = "UUID";
    private BroadcastReceiver mGattUpdateReceiver = null;

    private BTContext mBTContext = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStatusBarColor(STATUS_BAR_COLOR);
        setContentView(R.layout.device_list);

        if (!checkTargetBLESupport()){
            finish();
        }
        mHandler = new Handler();
        refreshingImageRotateAnimation = new RefreshingAnimation(0f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, (ImageView)findViewById(R.id.Refreshing));
        deviceListView = (ListView)findViewById(R.id.AvaliableDeviceList);
        mBTContext = (BTContext)getApplication();
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (refreshingImageRotateAnimation != null){
            refreshingImageRotateAnimation.startAnimation();
        }

        // Ensures Bluetooth is enabled on the device.  If Bluetooth is not currently enabled,
        // fire an intent to display a dialog asking the user to grant permission to enable it.
        if (!mBluetoothAdapter.isEnabled()) {
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, 1);
            }
        }
        mConnectingDeviceIndex = -1;
        deviceListArray.clear();
        SearchBLEDevice(true);
        showDeviceListView(deviceListArray, deviceListArrayShow);
        mBTContext.setmServiceDiscovered(false);
        mBTContext.setmConnected(false);
        mBTContext.setmConnecting(false);


        if (mBTContext.getmServiceConnection() != null){
            unbindService(mBTContext.getmServiceConnection());
            mBTContext.setmServiceConnection(null);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();

        if (refreshingImageRotateAnimation != null){
            refreshingImageRotateAnimation.stopAnimation();
        }


        SearchBLEDevice(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mGattUpdateReceiver != null){
            unregisterReceiver(mGattUpdateReceiver);
        }

        if (mBTContext.getGattServiceIntent() != null){
            stopService(mBTContext.getGattServiceIntent());
            mBTContext.setGattServiceIntent(null);
        }
        mBTContext.clear();
    }

    private void setStatusBarColor(int color){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
            SystemBarTintManager tintManager = new SystemBarTintManager(this);
            tintManager.setStatusBarTintEnabled(true);
            tintManager.setStatusBarTintColor(color);//通知栏所需颜色
        }
    }
    @TargetApi(19)
    private void setTranslucentStatus(boolean on) {
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    private void showDeviceListView(ArrayList<BluetoothDevice> ListArray, ArrayList<HashMap<String, String>> ListArrayShow){
        if (deviceListArray != null) {
            ListArrayShow.clear();
            for (int i = 0; i < ListArray.size(); i++) {
                HashMap<String, String> deviceInfo = new HashMap<String, String>();

                deviceInfo.put("ItemDeviceName", (i + 1) + ".    " + ListArray.get(i).getName());
                deviceInfo.put("ItemDeviceAddress", ListArray.get(i).getAddress());
                ListArrayShow.add(deviceInfo);
            }

            if (simpleAdapter == null) {
                simpleAdapter = new SimpleAdapter(this, ListArrayShow, R.layout.device_list_item, new String[]{"ItemDeviceName", "ItemDeviceAddress"}, new int[]{R.id.ItemDeviceName, R.id.ItemDeviceAddress});
                deviceListView.setAdapter(simpleAdapter);
                deviceListView.setOnItemClickListener(new DeviceInfoOnClickListenser());
            }
            else {
                simpleAdapter.notifyDataSetChanged();
            }
        }
    }
    class DeviceInfoOnClickListenser implements AdapterView.OnItemClickListener{
        @Override
        public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {

            if (mConnectingDeviceIndex == -1) {
                mConnectingDeviceIndex = position;
                connectSelectDevice(position);
            }
        }
    }

    private void connectSelectDevice(final int Index){
        if (mBTContext.getmServiceConnection() == null) {
            mBTContext.setmServiceConnection(new ServiceConnection() {
                @Override
                public void onServiceConnected(ComponentName componentName,
                                               IBinder service) {
                    mBTContext.setmBluetoothLeService(((BluetoothLeService.LocalBinder) service).getService());

                    if (mBTContext.getmBluetoothLeService() != null && !mBTContext.getmBluetoothLeService().initialize()) {
                        finish();
                    }
                    // Automatically connects to the device upon successful start-up
                    // initialization.
                    if (mConnectingDeviceIndex != -1) {
                        mBTContext.getmBluetoothLeService().connect(deviceListArrayShow.get(mConnectingDeviceIndex).get("ItemDeviceAddress"));
                        Intent connecntDialogActivityIntent = new Intent();
                        connecntDialogActivityIntent.setClass(DeviceSearchActivity.this, ConnectingDialog.class);
                        startActivity(connecntDialogActivityIntent);
                        mBTContext.setmConnecting(true);
                    }
                }

                @Override
                public void onServiceDisconnected(ComponentName componentName) {
                    mBTContext.setmBluetoothLeService(null);
                    mConnectingDeviceIndex = -1;
                    mBTContext.setmConnected(false);
                    mBTContext.setmConnecting(false);
                    mBTContext.setmServiceDiscovered(false);
                }
            });
            mBTContext.setGattServiceIntent(new Intent(DeviceSearchActivity.this, BluetoothLeService.class));
            bindService(mBTContext.getGattServiceIntent(), mBTContext.getmServiceConnection(), BIND_AUTO_CREATE);
        }
        if (mGattUpdateReceiver == null) {
                mGattUpdateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();
                    if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                        mBTContext.setmConnected(true);
                        mBTContext.setmConnecting(false);
                        mBTContext.setmServiceDiscovered(false);
                        if (deviceListArray.contains(mConnectingDeviceIndex))
                        {
                            deviceListArray.remove(mConnectingDeviceIndex);
                        }
                        mConnectingDeviceIndex = -1;

                    } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED
                            .equals(action)) {
                        mBTContext.setmConnected(false);
                        mBTContext.setmConnecting(false);
                        mBTContext.setmServiceDiscovered(false);
                    } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                            .equals(action)) {
                        // Show all the supported services and characteristics on the
                        // user interface.
                        mBTContext.setmServiceDiscovered(true);
                        displayGattServices(mBTContext.getmBluetoothLeService().getSupportedGattServices());
                    } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                    }
                }
            };
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }

        if (mBTContext.getmBluetoothLeService() != null) {
            //final boolean result = mBTContext.getmBluetoothLeService().connect(deviceListArrayShow.get(Index).get("ItemDeviceAddress"));
        }


    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter
                .addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    private boolean checkTargetBLESupport(){
        // Use this check to determine whether BLE is supported on the device.  Then you can
        // selectively disable BLE-related features.
        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
            return false;
        }

        // Initializes a Bluetooth adapter.  For API level 18 and above, get a reference to
        // BluetoothAdapter through BluetoothManager.
        final BluetoothManager bluetoothManager =
                (BluetoothManager) getSystemService(Context.BLUETOOTH_SERVICE);
        mBluetoothAdapter = bluetoothManager.getAdapter();

        // Checks if Bluetooth is supported on the device.
        if (mBluetoothAdapter == null) {
            Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
            finish();
            return false;
        }
        return true;
    }

    private void SearchBLEDevice(final boolean enable) {
        if (enable) {
            // Stops scanning after a pre-defined scan period.
/*
            mHandler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    mScanning = false;
                    mBluetoothAdapter.stopLeScan(mLeScanCallback);
                }
            }, SCAN_PERIOD);
*/
            mScanning = true;
            mBluetoothAdapter.startLeScan(mLeScanCallback);
        } else {
            mScanning = false;
            mBluetoothAdapter.stopLeScan(mLeScanCallback);
        }

    }

    private BluetoothAdapter.LeScanCallback mLeScanCallback =
            new BluetoothAdapter.LeScanCallback() {

                @Override
                public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!deviceListArray.contains(device)){
                                deviceListArray.add(device);
                                showDeviceListView(deviceListArray, deviceListArrayShow);
                            }
                        }
                    });
                }
            };

    private void displayGattServices(List<BluetoothGattService> gattServices) {
        if (gattServices == null)
            return;
        String uuid = null;
        String unknownServiceString = getResources().getString(
                R.string.unknown_service);
        String unknownCharaString = getResources().getString(
                R.string.unknown_characteristic);
        ArrayList<HashMap<String, String>> gattServiceData = new ArrayList<HashMap<String, String>>();
        ArrayList<ArrayList<HashMap<String, String>>> gattCharacteristicData = new ArrayList<ArrayList<HashMap<String, String>>>();
        ArrayList<ArrayList<BluetoothGattCharacteristic>> GattCharacteristics = null;

        if (mBTContext.getmGattCharacteristics() == null) {
            mBTContext.setmGattCharacteristics(new ArrayList<ArrayList<BluetoothGattCharacteristic>>());
        }
        GattCharacteristics = mBTContext.getmGattCharacteristics();
        GattCharacteristics.clear();

        // Loops through available GATT Services.
        for (BluetoothGattService gattService : gattServices) {
            HashMap<String, String> currentServiceData = new HashMap<String, String>();
            uuid = gattService.getUuid().toString();
            currentServiceData.put(LIST_NAME,
                    GattAttributes.lookup(uuid, unknownServiceString));
            currentServiceData.put(LIST_UUID, uuid);
            gattServiceData.add(currentServiceData);

            ArrayList<HashMap<String, String>> gattCharacteristicGroupData = new ArrayList<HashMap<String, String>>();
            List<BluetoothGattCharacteristic> gattCharacteristics = gattService
                    .getCharacteristics();
            ArrayList<BluetoothGattCharacteristic> charas = new ArrayList<BluetoothGattCharacteristic>();

            // Loops through available Characteristics.
            for (BluetoothGattCharacteristic gattCharacteristic : gattCharacteristics) {
                charas.add(gattCharacteristic);
                HashMap<String, String> currentCharaData = new HashMap<String, String>();
                uuid = gattCharacteristic.getUuid().toString();
                currentCharaData.put(LIST_NAME,
                        GattAttributes.lookup(uuid, unknownCharaString));
                currentCharaData.put(LIST_UUID, uuid);
                gattCharacteristicGroupData.add(currentCharaData);
            }
            GattCharacteristics.add(charas);
            gattCharacteristicData.add(gattCharacteristicGroupData);
        }
    }
}
