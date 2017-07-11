package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by We-Smart on 2017/7/11.
 */

public class ColorPicker extends View implements View.OnTouchListener {

    Paint mPaint = null;
    // 梯度渲染
    Shader mSweepGradient = null;

    int[] colors =  new int[] {0xFFFF0000, 0xFFFF00FF,0xFF0000FF,0xFF00FFFF, 0xFF00FF00, 0xFFFFFF00, 0xFFFF0000};

    public ColorPicker(Context context) {
        super(context);
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSweepGradient = new SweepGradient(w/2, h/2, colors, null);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制梯度渐变
        mPaint.setShader(mSweepGradient);

        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                float angle = (float) Math.atan2(event.getY(), event.getX());
                float unit = (float) (angle/ (2*Math.PI));

                Log.e("RGB", Integer.toHexString(interpCircleColor(colors, unit)) + "");
                break;
        }
        return false;
    }

    private int interpCircleColor(int colors[], float unit){
        if (unit < 0)
            return colors[0];
        if (unit >= 1)
            return colors[colors.length - 1];
        float p = unit * (colors.length - 1);
        int i = (int)p;
        p -=  i;

        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        return Color.argb(a, r, g, b);
    }

    private int ave(int s, int d, float p){
        return s + Math.round(p * (d-s));
    }
}
