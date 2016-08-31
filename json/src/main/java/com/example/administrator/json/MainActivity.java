package com.example.administrator.json;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.JsonReader;
import android.view.View;
import android.widget.Button;

import java.util.LinkedList;

import json.Jsonutils;
import json.User;

public class MainActivity extends AppCompatActivity {
    private String jsonData = "[{\"name\":\"zhangsan\", \"age\":30}, {\"name\":\"lisi\", \"age\":35}]";
    private Button button = null;
    private Button button1 = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button)findViewById(R.id.button);
        button1 = (Button)findViewById(R.id.button1);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jsonutils jsonutils = new Jsonutils();
                jsonutils.ParseJson(jsonData);
            }
        });

        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Jsonutils jsonutils = new Jsonutils();
                jsonutils.ParseUserJson(jsonData);


            }
        });
    }
}
