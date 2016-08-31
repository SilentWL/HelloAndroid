package com.ble.Animation;

import android.content.Context;
import android.util.AttributeSet;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

/**
 * Created by Administrator on 2016/5/10 0010.
 */
public class RefreshingAnimation extends RotateAnimation {
    private ImageView refreshingImageView = null;
    public RefreshingAnimation(float fromDegrees, float toDegrees, int pivotXType, float pivotXValue, int pivotYType, float pivotYValue, ImageView refreshingImageView) {
        super(fromDegrees, toDegrees, pivotXType, pivotXValue, pivotYType, pivotYValue);
        this.setInterpolator(new LinearInterpolator());
        this.setRepeatCount(-1);
        this.setDuration(500);
        this.refreshingImageView = refreshingImageView;
    }

    public void startAnimation(){
        if (refreshingImageView != null) {
            refreshingImageView.startAnimation(this);
        }
    }

    public void stopAnimation(){
        if (refreshingImageView != null) {
            refreshingImageView.clearAnimation();
        }
    }
}
