package com.example.administrator.testreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class TestReceiver extends BroadcastReceiver {
    public TestReceiver() {
        super();
        System.out.println("TestReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("onReceive");
    }
}
