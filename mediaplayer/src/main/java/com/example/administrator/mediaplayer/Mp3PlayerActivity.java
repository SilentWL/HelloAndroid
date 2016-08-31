package com.example.administrator.mediaplayer;

import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.administrator.mediaplayer.service.Mp3PlayService;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Queue;

import LrcProcessor.LrcProcessor;
import mp3download.FileUtils;
import mp3parser.Mp3Info;

/**
 * Created by Administrator on 2016/4/8 0008.
 */
public class Mp3PlayerActivity extends AppCompatActivity {
    private ImageButton playBtn = null;
    private ImageButton pauseBtn = null;
    private ImageButton stopBtn = null;
    private TextView lrcTextView = null;

    private Intent intent = null;
    private Mp3Info mp3Info = null;

    private ArrayList<Queue> queue = null;
    private UpdateTimeCallback updateTimeCallback = null;
    private Handler handler = null;

    private Long begin = 0L;
    private Long currentTimeMill = 0L;
    private Long nextTimeMill = 0L;
    private Long pauseTimeMills = 0L;

    private String lrcString = null;
    private boolean isPlaying = false;
    private boolean isReleased = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        Intent tempIntent = getIntent();
        intent = new Intent();
        mp3Info = (Mp3Info)tempIntent.getSerializableExtra("mp3info");
        intent.putExtra("mp3info", mp3Info);

        playBtn = (ImageButton)findViewById(R.id.play);
        pauseBtn = (ImageButton)findViewById(R.id.pause);
        stopBtn = (ImageButton)findViewById(R.id.stop);
        lrcTextView = (TextView)findViewById(R.id.lrc);

        lrcTextView.setMovementMethod(ScrollingMovementMethod.getInstance());
        handler = new Handler();

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Msg", AppConstant.PlayerMsg.PLAY_MSG);
                intent.setClass(Mp3PlayerActivity.this, Mp3PlayService.class);
                startService(intent);
                if (isPlaying == false) {
                    prepareLrc(mp3Info.getLrcName());
                    begin = System.currentTimeMillis();
                    handler.postDelayed(updateTimeCallback, 5);
                    isPlaying = true;
                }
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Msg", AppConstant.PlayerMsg.PAUSE_MSG);
                intent.setClass(Mp3PlayerActivity.this, Mp3PlayService.class);
                startService(intent);
                if (!isReleased) {
                    if (isPlaying) {
                        handler.removeCallbacks(updateTimeCallback);
                        pauseTimeMills = System.currentTimeMillis();
                    } else {
                        handler.postDelayed(updateTimeCallback, 5);
                        begin = System.currentTimeMillis() - pauseTimeMills + begin;
                    }
                    isPlaying = !isPlaying;
                }
            }

        });

        stopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                intent.putExtra("Msg", AppConstant.PlayerMsg.STOP_MSG);
                intent.setClass(Mp3PlayerActivity.this, Mp3PlayService.class);
                startService(intent);
                isPlaying = false;

                if (!isReleased) {
                    handler.removeCallbacks(updateTimeCallback);
                    isReleased = true;
                    lrcTextView.setText("");
                }
            }
        });
    }

    private void prepareLrc(String lrcName){
        try{
            InputStream inputStream = new FileInputStream(new FileUtils().GetSDPath() + File.separator + "mp3" + File.separator + mp3Info.getLrcName());
            LrcProcessor lrcProcessor = new LrcProcessor();
            queue = lrcProcessor.process(inputStream);
            updateTimeCallback = new UpdateTimeCallback(queue);
            begin = 0L;
            currentTimeMill = 0L;
            nextTimeMill = 0L;

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    class UpdateTimeCallback implements Runnable{
        private ArrayList<Queue> queues = null;
        private Queue times = null;
        private Queue lrcInfos = null;
        public UpdateTimeCallback(ArrayList<Queue> queues){
            this.queues = queues;
            times = queues.get(0);
            lrcInfos = queues.get(1);
        }
        @Override
        public void run() {
            Long offset = System.currentTimeMillis() - begin;
            if (currentTimeMill == 0){
                nextTimeMill = (Long)times.poll();
                lrcString = (String)lrcInfos.poll();
            }
            if (nextTimeMill != null && offset >= nextTimeMill){
                if (lrcString != null) {
                    lrcTextView.setText(lrcString);
                    refreshLrcTextView(lrcTextView);
                }

                if (lrcInfos != null) {
                    lrcString = (String) lrcInfos.poll();
                    nextTimeMill = (Long) times.poll();
                }
            }
            currentTimeMill = currentTimeMill + 10;
            handler.postDelayed(updateTimeCallback, 10);
        }
    }
    private void refreshLrcTextView(TextView textView){
        int offset=textView.getLineCount()*textView.getLineHeight();
        if (offset > textView.getHeight()){
            textView.scrollTo(0, offset - textView.getHeight() + textView.getLineHeight());
        }
    }
}
