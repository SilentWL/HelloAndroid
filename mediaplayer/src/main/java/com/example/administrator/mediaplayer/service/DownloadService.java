package com.example.administrator.mediaplayer.service;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import mp3download.HttpDownload;
import mp3parser.Mp3Info;

/**
 * Created by Administrator on 2016/4/6 0006.
 */
public class DownloadService extends Service{
    public DownloadService() {
        super();
        Log.i("DownloadService","mp3Info:" + "------>DownloadService");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("DownloadService","mp3Info:" + "------>onCreate");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3Info");
        Log.i("DownloadService","mp3Info:" + "------>onStartCommand : " + mp3Info.toString());
        DownloadThread downloadThread = new DownloadThread(mp3Info);
        new Thread(downloadThread).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i("DownloadService","mp3Info:" + "------>onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.i("DownloadService","mp3Info:" + "------>onBind");
        return null;
    }

    class DownloadThread implements Runnable{
        private Mp3Info mp3Info = null;
        public DownloadThread(Mp3Info mp3Info){
            this.mp3Info = mp3Info;
        }
        @Override
        public void run() {
            HttpDownload httpDownload = new HttpDownload();
            String mp3Url = "http://192.168.199.18:8080/mp3/" + mp3Info.getName();

            //String lrcUrl = "http://192.168.199.18:8080/mp3/" + mp3Info.getLrcName();
            int result = httpDownload.DownloadFile(mp3Url, "mp3/", mp3Info.getName());
            //httpDownload.DownloadFile(lrcUrl, "mp3/", mp3Info.getLrcName());
            String resultMsg = null;

            if (result == 1){
                resultMsg = "文件已存在";
            }else if (result == -1){
                resultMsg = "下载失败";
            }else if (result == 0){
                resultMsg = "下载成功";
            }
            String lrcUrl = "http://192.168.199.18:8080/mp3/" + mp3Info.getLrcName();
            //String lrcUrl = "http://192.168.199.18:8080/mp3/" + mp3Info.getLrcName();
            httpDownload.DownloadFile(lrcUrl, "mp3/", mp3Info.getLrcName());
            initNotificationNew(resultMsg);
        }
    }

    private void initNotificationNew(String notificationStr) {


        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_sync).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setOngoing(false).setContentText (notificationStr).setContentTitle("Media Player")
                .setTicker("abcd").setPriority(Notification.PRIORITY_DEFAULT);

        NotificationManager nm = (NotificationManager) this.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notif = builder.build();
        notif.flags|=Notification.FLAG_INSISTENT | Notification.FLAG_AUTO_CANCEL;
        notif.defaults=Notification.DEFAULT_ALL;

        nm.notify(1, createNotification(notificationStr, true));

    }
    private Notification createNotification(String notificationStr, boolean makeHeadsUpNotification) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                .setSmallIcon(android.R.drawable.stat_notify_sync).setWhen(System.currentTimeMillis())
                .setAutoCancel(true).setOngoing(false).setContentText (notificationStr).setContentTitle("Media Player")
                .setPriority(Notification.PRIORITY_DEFAULT);
        if (makeHeadsUpNotification) {
            Intent push = new Intent();
            push.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            push.setClass(this, this.getClass());

            PendingIntent fullScreenPendingIntent = PendingIntent.getActivity(this, 0,
                    push, PendingIntent.FLAG_ONE_SHOT);
            builder
                    .setFullScreenIntent(fullScreenPendingIntent, false);
        }
        Notification notif = builder.build();

        notif.defaults=Notification.DEFAULT_ALL;
        return notif;
    }
}

