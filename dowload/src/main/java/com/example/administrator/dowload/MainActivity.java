package com.example.administrator.dowload;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button DownloadTxt = null;
    private Button DownloadMp3 = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTxt = (Button)findViewById(R.id.DownloadTxt);
        DownloadMp3 = (Button)findViewById(R.id.DownloadMp3);


    }

    class DownloadButtonOnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            if (v.getId() == DownloadTxt.getId()){

            }
            else if (v.getId() == DownloadMp3.getId()){

            }
        }
    }
}
