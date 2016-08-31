package com.example.administrator.mediaplayer.service;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.example.administrator.mediaplayer.AppConstant;

import java.io.File;
import java.util.ArrayList;
import java.util.Queue;

import mp3download.FileUtils;
import mp3parser.Mp3Info;

/**
 * Created by Administrator on 2016/4/9 0009.
 */
public class Mp3PlayService extends Service{
    private Mp3Info mp3Info = null;
    private MediaPlayer mediaPlayer = null;

    private boolean isPlaying = false;
    private boolean isPause = false;
    private boolean isReleased = false;

    public Mp3PlayService() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Mp3Info mp3Info = (Mp3Info) intent.getSerializableExtra("mp3info");
        int Msg = intent.getIntExtra("Msg", 0);

        if (mp3Info != null){
            switch (Msg){
                case AppConstant.PlayerMsg.PLAY_MSG:
                    playMp3(mp3Info);
                    break;
                case AppConstant.PlayerMsg.PAUSE_MSG:
                    pauseMp3(mp3Info);
                    break;
                case AppConstant.PlayerMsg.STOP_MSG:
                    stopMp3(mp3Info);
                    break;
            }
        }

        return super.onStartCommand(intent, flags, startId);
    }
    private void playMp3(Mp3Info mp3Info){
        if (isPlaying == false){
            String path = getMp3Path(mp3Info);
            mediaPlayer = MediaPlayer.create(this, Uri.parse("file://" + path));
            mediaPlayer.setLooping(false);
            mediaPlayer.start();
            isPlaying = true;
            isReleased = false;
            isPause = false;
        }
    }

    private void pauseMp3(Mp3Info mp3Info){
        if (mediaPlayer != null){
            if (isReleased == false) {
                if (isPause == false) {
                    mediaPlayer.pause();
                    isPause = true;
                    isPlaying = false;
                }else {
                    mediaPlayer.start();
                    isPause = false;
                    isPlaying = true;
                }
            }
        }
    }
    private void stopMp3(Mp3Info mp3Info){
        if (mediaPlayer != null){
            if (isReleased == false){
                mediaPlayer.stop();
                mediaPlayer.release();
                isReleased = true;
                isPlaying = false;
            }
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    private String getMp3Path(Mp3Info mp3Info){
        FileUtils fileUtils = new FileUtils();
        String path = fileUtils.GetSDPath() + File.separator + "mp3" + File.separator + mp3Info.getName();
        return path;
    }
}
