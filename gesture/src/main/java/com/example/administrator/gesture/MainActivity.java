package com.example.administrator.gesture;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.gesture.Gesture;
import android.gesture.GestureLibraries;
import android.gesture.GestureLibrary;
import android.gesture.GestureOverlayView;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private GestureOverlayView gestureOverlayView = null;
    //private EditText editText = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // 使用文本编辑器
        //editText = (EditText) findViewById(R.id.gesture_name);
        // 获取手势编辑视图
        gestureOverlayView = (GestureOverlayView) findViewById(R.id.gesture);
        // 设置手势绘图的颜色
        gestureOverlayView.setGestureColor(Color.RED);
        // 设置手势的绘制宽度
        gestureOverlayView.setGestureStrokeWidth(4);
        // 为gesture的手势完成事件绑定事件监听器
        gestureOverlayView.addOnGesturePerformedListener(new GestureOverlayView.OnGesturePerformedListener() {
            @Override
            public void onGesturePerformed(GestureOverlayView overlay, final Gesture gesture) {
                // 加载save.xml界面布局代表的视图
                View saveDialog = getLayoutInflater().inflate(R.layout.save, null);
                // 获取saveDialog里的show组件
                ImageView imageView = (ImageView) saveDialog.findViewById(R.id.show);
                // 获取saveDialog的gesture_name组件
                final EditText gestureEditText = (EditText) saveDialog.findViewById(R.id.gesture_name);
                // 根据Gesture包含的手势创建一个位图
                Bitmap bitmap = gesture.toBitmap(128, 128, 10, 0xffff0000);
                imageView.setImageBitmap(bitmap);
                // 使用对话框显示saveDialog组件
                new AlertDialog.Builder(MainActivity.this).setView(saveDialog).setPositiveButton("save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // 获取制定文件对应的手势库
                        GestureLibrary gestureLibrary = GestureLibraries.fromFile(Environment.getExternalStorageDirectory().getPath() + "/gesture");
                        // 添加手势
                        gestureLibrary.addGesture(gestureEditText.getText().toString(), gesture);
                        gestureLibrary.save();

                    }
                }).setNegativeButton("cancle", null).show();
            }
        });
    }
}
