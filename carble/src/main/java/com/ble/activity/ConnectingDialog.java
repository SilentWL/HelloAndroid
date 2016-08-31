package com.ble.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.view.KeyEvent;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;

import com.ble.Animation.ConnectingAnimation;
import com.example.administrator.carble.R;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class ConnectingDialog extends Activity{
    private ConnectingAnimation connectingAnimation = null;
    private HandlerThread handlerThread = null;
    private Handler handler= null;
    private static final int CONNECT_STATUS_MSG = 100;
    private BTContext mBTContext = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.connecting_dialog);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            //透明状态栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        }
        connectingAnimation = new ConnectingAnimation(0.5f, 2.5f, 0.5f, 2.5f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f, (ImageView)findViewById(R.id.ConnectingDialogImg));

        handlerThread = new HandlerThread("CheckConnectStatus");
        handlerThread.start();

        mBTContext = (BTContext)getApplication();

        handler = new Handler(handlerThread.getLooper()){
            int handleMessageTimes = 0;

            @Override
            public void handleMessage(Message msg) {
                handleMessageTimes++;
                if (msg.what == CONNECT_STATUS_MSG) {
                    if (mBTContext.ismConnected() && mBTContext.ismServiceDiscovered() == true) {
                        Intent intent = new Intent().setClass(ConnectingDialog.this, ControlCarActivity.class);
                        startActivity(intent);
                        handler.removeMessages(CONNECT_STATUS_MSG);
                        handlerThread.quit();
                        finish();
                    } else {
                        if ((!mBTContext.ismConnected() && !mBTContext.ismConnecting()) || handleMessageTimes >= 50) {
                            handlerThread.quit();

                            if (mBTContext.getmBluetoothLeService() != null){
                                mBTContext.getmBluetoothLeService().disconnect();
                                mBTContext.getmBluetoothLeService().close();
                            }
                            finish();
                        }
                    }
                    if (handler != null) {
                        handler.sendEmptyMessageDelayed(CONNECT_STATUS_MSG, 100);
                    }
                }
            }
        };
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (connectingAnimation != null){
            connectingAnimation.startAnimation();
        }
        if (handler != null) {
            handler.sendEmptyMessageDelayed(CONNECT_STATUS_MSG, 500);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (connectingAnimation != null){
            connectingAnimation.stopAnimation();
        }
        if (handlerThread != null){
            handlerThread.quit();
        }
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (KeyEvent.KEYCODE_BACK == keyCode) {
            return true;

        }else {
            return super.onKeyDown(keyCode, event);
        }
    }
}
