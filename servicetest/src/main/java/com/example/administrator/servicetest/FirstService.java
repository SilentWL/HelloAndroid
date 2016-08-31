package com.example.administrator.servicetest;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

/**
 * Created by Administrator on 2016/3/31 0031.
 */
public class FirstService extends Service{

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Service onBind");
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("flags---->" + flags);
        System.out.println("startId---->" + startId);
        System.out.println("Service onStartCommand");
        System.out.println("Service onStartCommand ----->Thread Id:" + Thread.currentThread().getId());
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        System.out.println("Service onDestroy");
        super.onDestroy();
    }
}
