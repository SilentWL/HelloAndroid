package com.ble.activity;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ble.Animation.ConnectingAnimation;
import com.ble.service.BluetoothLeService;
import com.example.administrator.carble.R;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class ControlCarActivity extends Activity{
    private ConnectingAnimation controlCarAnimation = null;
    private BroadcastReceiver mGattUpdateReceiver = null;

    private OrientationControlButton orientationControlButtonLeft = null;
    private OrientationControlButton orientationControlButtonRight = null;
    private OrientationControlButton orientationControlButtonUp = null;
    private OrientationControlButton orientationControlButtonDown = null;

    private BLESendDataHander mBLESendDatahandler = null;
    private HandlerThread mBLESendDataThread = null;
    private BTContext mBTContext = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.control_car);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        controlCarAnimation = new ConnectingAnimation(0.5f, 2.3f, 0.5f, 2.3f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, (ImageView)findViewById(R.id.DeviceControlAnimationImg));
        orientationControlButtonLeft = (OrientationControlButton)findViewById(R.id.Left);
        orientationControlButtonRight = (OrientationControlButton)findViewById(R.id.Right);
        orientationControlButtonUp = (OrientationControlButton)findViewById(R.id.Up);
        orientationControlButtonDown = (OrientationControlButton)findViewById(R.id.Down);

        orientationControlButtonLeft.setOnTouchListener(new OrientationControlButtonOnTouchListenser());
        orientationControlButtonRight.setOnTouchListener(new OrientationControlButtonOnTouchListenser());
        orientationControlButtonUp.setOnTouchListener(new OrientationControlButtonOnTouchListenser());
        orientationControlButtonDown.setOnTouchListener(new OrientationControlButtonOnTouchListenser());

        mBLESendDataThread = new HandlerThread("BLE Send Data Thread");
        mBLESendDataThread.start();
        mBLESendDatahandler = new BLESendDataHander(mBLESendDataThread.getLooper());

        mBTContext = (BTContext)getApplication();

        if (mGattUpdateReceiver == null) {
            mGattUpdateReceiver = new BroadcastReceiver() {
                @Override
                public void onReceive(Context context, Intent intent) {
                    final String action = intent.getAction();
                    if  (BluetoothLeService.ACTION_GATT_DISCONNECTED
                            .equals(action)) {
                        finish();

                    } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED
                            .equals(action)) {
                        // Show all the supported services and characteristics on the
                        // user interface.

                    } else if (BluetoothLeService.ACTION_DATA_AVAILABLE.equals(action)) {

                    }
                }
            };
        }
    }
    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();

        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_AVAILABLE);
        return intentFilter;
    }
    @Override
    protected void onResume() {
        super.onResume();

        if (!mBTContext.ismConnected()){
            finish();
        }

        if (controlCarAnimation != null) {
            controlCarAnimation.startAnimation();
        }

        if (mGattUpdateReceiver != null){
            registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (controlCarAnimation != null) {
            controlCarAnimation.stopAnimation();
        }

        if (mGattUpdateReceiver != null){
            unregisterReceiver(mGattUpdateReceiver);
        }

        if (mBLESendDataThread != null) {
            mBLESendDataThread.quit();
        }
        if (mBTContext.getmBluetoothLeService() != null){
            mBTContext.getmBluetoothLeService().disconnect();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    class OrientationControlButtonOnTouchListenser implements View.OnTouchListener{
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            byte sendData[]={(byte)0xaa, (byte)0x58, (byte)0x00, (byte)0x00,(byte)0x00,(byte)0x00,(byte)0x00, (byte)0x00, (byte)0xff};
            boolean isSendData = true;
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (v.getId() == R.id.Left) {
                    sendData[2] = (byte)0x81;
                } else if (v.getId() == R.id.Right) {
                    sendData[2] = (byte)0x01;
                } else if (v.getId() == R.id.Up) {
                    sendData[3] = (byte)0x81;
                } else if (v.getId() == R.id.Down) {
                    sendData[3] = (byte)0x01;
                }else {
                    isSendData = false;
                }
            }
            else if (event.getAction() == MotionEvent.ACTION_UP){
                if (v.getId() == R.id.Left || v.getId() == R.id.Right) {
                    sendData[2] = (byte) 0x00;
                }else if (v.getId() == R.id.Up || v.getId() == R.id.Down) {
                    sendData[3] = (byte) 0x00;
                }else {
                    isSendData = false;
                }
            }
            else {
                isSendData = false;
            }
            if (isSendData == true){
                sendData[7] = (byte)((sendData[0] + sendData[1] + sendData[2] + sendData[3] + sendData[4] + sendData[5] + sendData[6]) * 0xff);
                mBTContext.getmBluetoothLeService().writeControlData(mBTContext.getmBluetoothLeService().getSupportedGattServices(), mBTContext.getmGattCharacteristics(), sendData);
            }
            return false;
        }
    }
    class BLESendDataHander extends Handler{

        public BLESendDataHander(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {

        }
    }
}
