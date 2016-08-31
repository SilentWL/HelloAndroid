package com.ble.Animation;

import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class ConnectingAnimation{
    private ImageView scaleImageView = null;
    AnimationSet animationSet = new AnimationSet(true);

    public ConnectingAnimation(float fromX, float toX, float fromY, float toY, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, ImageView imageView) {
        ScaleAnimation scaleanimation1 = new ScaleAnimation(fromX, toX, fromY, toY, pivotXType, pivotXValue, pivotYType, pivotYValue);
        ScaleAnimation scaleAnimation2 = new ScaleAnimation(toX, fromX, toY, fromX, pivotXType, pivotXValue, pivotYType, pivotYValue);

        scaleanimation1.setRepeatCount(-1);
        scaleanimation1.setDuration(1000);
        scaleanimation1.setInterpolator(new LinearInterpolator());
        scaleAnimation2.setRepeatCount(-1);
        scaleAnimation2.setDuration(1000);
        scaleAnimation2.setInterpolator(new LinearInterpolator());
        animationSet.addAnimation(scaleanimation1);
        animationSet.addAnimation(scaleAnimation2);

        this.scaleImageView = imageView;
    }

    public void startAnimation(){
        if (scaleImageView != null) {
            scaleImageView.startAnimation(animationSet);
        }
    }

    public void stopAnimation(){
        if (scaleImageView != null) {
            scaleImageView.clearAnimation();
        }
    }
}
