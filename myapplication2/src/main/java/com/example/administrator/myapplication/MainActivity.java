package com.example.administrator.myapplication;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        System.out.println("Main--->" + Thread.currentThread().getId());
        HandlerThread MyHandlerThread = new HandlerThread("Test_Handler");
        MyHandlerThread.start();

        MyHandler MyHd = new MyHandler(MyHandlerThread.getLooper());
        Message Msg = MyHd.obtainMessage();

        Bundle b = new Bundle();
        b.putInt("a", 10);
        b.putString("b", "abcdefg");
        Msg.setData(b);
        Msg.sendToTarget();
    }

    class MyHandler extends Handler{
        MyHandler(Looper looper){
            super(looper);
        }

        @Override
        public void handleMessage(Message msg) {
            Bundle b = msg.getData();

            System.out.println("Handler--->" + Thread.currentThread().getId());
            System.out.println("Handler--->Msg Data \"a:\"" + b.getInt("a") + "  \"b:\"" + b.getString("b"));
            super.handleMessage(msg);
        }


    }
}
