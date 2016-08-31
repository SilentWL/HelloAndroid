package com.ble.activity;

import android.content.Context;
import android.graphics.ColorMatrixColorFilter;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Administrator on 2016/5/11 0011.
 */
public class OrientationControlButton extends ImageButton{
    private final static float[] BUTTON_PRESSED = new float[] {
            2.0f, 0, 0, 0, -50,
            0, 2.0f, 0, 0, -50,
            0, 0, 2.0f, 0, -50,
            0, 0, 0, 5, 0 };

            /**
             11
             * 按钮恢复原状
             12
             */
    private final static float[] BUTTON_RELEASED = new float[] {
            1, 0, 0, 0, 0,
            0, 1, 0, 0, 0,
            0, 0, 1, 0, 0,
            0, 0, 0, 1, 0 };

    public OrientationControlButton(Context context) {
        super(context);
    }

    public OrientationControlButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(event.getAction() == MotionEvent.ACTION_DOWN) {
            this.setAlpha(0.8f);
            //this.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_PRESSED));
            //this.setBackgroundDrawable(this.getBackground());
        }else if(event.getAction() == MotionEvent.ACTION_UP) {
            this.setAlpha(1f);
            //this.getBackground().setColorFilter(new ColorMatrixColorFilter(BUTTON_RELEASED));
            //this.setBackgroundDrawable(this.getBackground());
        }
        return super.onTouchEvent(event);
    }
}
