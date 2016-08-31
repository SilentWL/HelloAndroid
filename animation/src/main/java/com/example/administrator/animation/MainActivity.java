package com.example.administrator.animation;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {
    private Button AlphaBtn = null;
    private Button TranslateBtn = null;
    private Button RotateBtn = null;
    private Button ScaleBtn = null;
    private ImageView Image = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AlphaBtn = (Button)findViewById(R.id.Alpha);
        TranslateBtn = (Button)findViewById(R.id.Translate);
        RotateBtn = (Button)findViewById(R.id.Rotate);
        ScaleBtn = (Button)findViewById(R.id.Scale);
        Image = (ImageView)findViewById(R.id.ImageViewId);

        AlphaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                AnimationSet animationSet = new AnimationSet(true);
//                Animation animation = new AlphaAnimation(1, 0);
//                animation.setDuration(5000);
//                animationSet.addAnimation(animation);
//                Image.startAnimation(animationSet);
                Animation animation = AnimationUtils.loadAnimation(MainActivity.this, R.anim.animation);
                Image.startAnimation(animation);
            }
        });

        TranslateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);
                Animation translateAnimation = new TranslateAnimation(Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 0f, Animation.RELATIVE_TO_SELF, 1f);
                translateAnimation.setDuration(5000);
                animationSet.addAnimation(translateAnimation);
                Image.startAnimation(animationSet);
            }
        });

        RotateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);
                Animation rotateAnimation = new RotateAnimation(0, 360, Animation.RELATIVE_TO_PARENT, 0f, Animation.RELATIVE_TO_PARENT, 0f);
                rotateAnimation.setDuration(5000);
                animationSet.addAnimation(rotateAnimation);
                Image.startAnimation(animationSet);
            }
        });

        ScaleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AnimationSet animationSet = new AnimationSet(true);

                Animation ScaleAnimation = new ScaleAnimation(1, 0.1f, 1, 0.1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                ScaleAnimation.setDuration(5000);
                animationSet.addAnimation(ScaleAnimation);
                Image.startAnimation(animationSet);
            }
        });
    }
}
