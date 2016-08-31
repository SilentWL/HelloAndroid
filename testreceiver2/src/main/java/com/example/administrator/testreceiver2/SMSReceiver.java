package com.example.administrator.testreceiver2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2016/3/30 0030.
 */
public class SMSReceiver extends BroadcastReceiver{
    public SMSReceiver() {
        super();
        System.out.println("SMSReceiver----->SMSReceiver");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        System.out.println("SMSReceiver----->onReceive");
    }
}
