package com.ble.activity;

import android.app.Application;
import android.bluetooth.BluetoothGattCharacteristic;
import android.content.Intent;
import android.content.ServiceConnection;

import com.ble.service.BluetoothLeService;

import java.util.ArrayList;

/**
 * Created by Administrator on 2016/5/12 0012.
 */
public class BTContext extends Application{
    private boolean mConnected = false;
    private boolean mConnecting = false;
    private boolean mServiceDiscovered = false;
    private ServiceConnection mServiceConnection = null;
    private Intent gattServiceIntent = null;

    private ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics = null;
    private BluetoothLeService mBluetoothLeService = null;

    public void setmBluetoothLeService(BluetoothLeService mBluetoothLeService) {
        this.mBluetoothLeService = mBluetoothLeService;
    }

    public BluetoothLeService getmBluetoothLeService() {
        return mBluetoothLeService;
    }

    public boolean ismConnected() {
        return mConnected;
    }
    public void setmConnected(boolean mConnected) {
        this.mConnected = mConnected;
    }
    public ArrayList<ArrayList<BluetoothGattCharacteristic>> getmGattCharacteristics() {
        return mGattCharacteristics;
    }
    public void setmGattCharacteristics(ArrayList<ArrayList<BluetoothGattCharacteristic>> mGattCharacteristics) {
        this.mGattCharacteristics = mGattCharacteristics;
    }

    public boolean ismConnecting() {
        return mConnecting;
    }

    public void setmConnecting(boolean mConnecting) {
        this.mConnecting = mConnecting;
    }

    public boolean ismServiceDiscovered() {
        return mServiceDiscovered;
    }

    public void setmServiceDiscovered(boolean mServiceDiscovered) {
        this.mServiceDiscovered = mServiceDiscovered;
    }

    public ServiceConnection getmServiceConnection() {
        return mServiceConnection;
    }

    public void setmServiceConnection(ServiceConnection mServiceConnection) {
        this.mServiceConnection = mServiceConnection;
    }

    public void clear(){
        mConnected = false;
        mConnecting = false;
        mServiceDiscovered = false;
        mGattCharacteristics.clear();
        mBluetoothLeService = null;
        mServiceConnection = null;
        gattServiceIntent = null;
    }

    public Intent getGattServiceIntent() {
        return gattServiceIntent;
    }

    public void setGattServiceIntent(Intent gattServiceIntent) {
        this.gattServiceIntent = gattServiceIntent;
    }
}
