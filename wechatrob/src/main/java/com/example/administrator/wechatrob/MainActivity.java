package com.example.administrator.wechatrob;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.OpenRobBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openServiceSetting();
            }
        });
    }
    private void openServiceSetting() {
        try {
            Intent intent = new Intent(
                    android.provider.Settings.ACTION_ACCESSIBILITY_SETTINGS);
            startActivity(intent);
            Toast.makeText(this, "找到抢红包服务，开启即可", Toast.LENGTH_LONG).show();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
