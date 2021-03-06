package com.example.administrator.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposePathEffect;
import android.graphics.CornerPathEffect;
import android.graphics.DashPathEffect;
import android.graphics.DiscretePathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathDashPathEffect;
import android.graphics.PathEffect;
import android.graphics.SumPathEffect;
import android.support.v4.view.ViewCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        RelativeLayout layout = (RelativeLayout)findViewById(R.id.root);
        DrawView drawView = new DrawView(this);
        drawView.setMinimumHeight(800);
        drawView.setMinimumWidth(600);
        //drawView.invalidate();
        layout.addView(drawView);

        Window window = getWindow();

        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(Color.BLACK);
        window.setNavigationBarColor(Color.BLACK);


    }
    public class DrawView extends View{

        public DrawView(Context context) {
            super(context);
        }

        @Override
        protected void onDraw(Canvas canvas) {
            super.onDraw(canvas);


            Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(10);

            PathEffect[] pathEffects = new PathEffect[7];
//不设置效果，默认转角处带有毛刺
            pathEffects[0] = null;
/**
 * 用平滑的圆角代替尖角
 * radius 转角处的圆滑程度
 */
            pathEffects[1] = new CornerPathEffect(100);
/**
 * 创建一个虚线的轮廓(短横线或者小圆点)
 * intervals[] 数组中第一个参数定义第一个实线的长度，第二个参数为虚线长度，以此类推或者重复
 * phase 偏移值，动态改变其值会让路径产生动画的效果，就像进度条往前走一样
 */
            pathEffects[2] = new DashPathEffect(new float[]{50, 20, 5, 10}, 0);
/**
 * 添加了随机性，类似生锈铁丝的效果
 * segmentLength 指定突出杂点的密度，值越小杂点越密集
 * deviation 指定杂点突出的大小，值越大突出的距离越大
 */
            pathEffects[3] = new DiscretePathEffect(5f, 5f);
/**
 * 和DashPathEffect类似，只不过可以自定义路径虚线的样式
 * shape 自定义的路径样式，这里定义为方形
 * advance 每个shape间的距离
 * phase 偏移值，动态改变其值会让路径产生动画的效果，就像进度条往前走一样
 * style 设置连接处的样式，取值如下：
 *      ROTATE 线段连接处的shape进行适当角度旋转连接
 *      MORPH 线段连接处的shape以拉伸或者压缩形变连接
 *      TRANSLATE 线段连接处的shape平行平移连接
 */
            Path pathShape = new Path();
            pathShape.addRect(0, 0, 10, 10, Path.Direction.CCW);
            pathEffects[4] = new PathDashPathEffect(pathShape, 20, 0, PathDashPathEffect.Style.ROTATE);
/**
 * 组合两种路径效果，先将路径变成innerpe的效果，再去复合outerpe的路径效果
 * outerpe PathEffect效果A
 * innerpe PathEffect效果B
 */
            pathEffects[5] = new ComposePathEffect(pathEffects[3], pathEffects[4]);
/**
 * 组合两种路径效果，把两种路径效果加起来再作用于路径
 * first PathEffect效果A
 * second PathEffect效果B
 */
            pathEffects[6] = new SumPathEffect(pathEffects[2], pathEffects[4]);

            Path path = new Path();
            path.moveTo(0, 0);
            for (int index=1; index<20; index++){
                path.lineTo(index*40, (float)Math.random()*150);
            }

            for (int index=0; index<pathEffects.length; index++){
                paint.setPathEffect(pathEffects[index]);
                canvas.drawPath(path, paint);
                canvas.translate(0, 100);
            }


        }
    }
}
