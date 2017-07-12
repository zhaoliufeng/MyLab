package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
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

    private Paint mPaint;    //背景颜色画笔
    private Paint mCirclePaint;  //指示圆画笔
    private Point mCirclePoint;  //指示圆位置
    private int mCircleRadius;  //指示圆半径
    private Shader mSweepGradient; // 梯度渲染

    private ColorChangeListener mColorChangeListener;   //颜色改变监听

    int[] colors =  new int[] {  0xFFFF0000, 0xFFFFFF00,
                                 0xFF00FF00, 0xFF00FFFF,
                                 0xFF0000FF, 0xFFFF00FF,
                                 0xFFFF0000}; //色环颜色值

    public ColorPicker(Context context) {
        super(context);
    }

    public ColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mCircleRadius = 15;
        initPaint();
        setOnTouchListener(this);
    }

    private void initPaint() {
        mPaint = new Paint();

        mCirclePaint = new Paint();
        mCirclePaint.setAlpha(50);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setAntiAlias(true);

        mCirclePoint = new Point(0, 0);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mSweepGradient = new SweepGradient(w/2, h/2, colors, null);
        mCirclePoint.set(w/2, h/2);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // 绘制梯度渐变
        mPaint.setShader(mSweepGradient);
        canvas.drawRect(0, 0, getWidth(), getHeight(), mPaint);

        //绘制指示圆
       drawCircle(canvas);
    }


    /**
     * 画圆
     * @param canvas 画布
     */
    public void drawCircle(Canvas canvas){
        canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mCircleRadius, mCirclePaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        float x = event.getX() - getWidth() / 2;
        float y = event.getY() - getHeight() / 2;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                mCirclePoint.set((int)event.getX(), (int) event.getY());
                postInvalidate();
                break;
            case MotionEvent.ACTION_MOVE:
                float angle = (float) Math.atan2(y, x); //获取角度 0 - 1 [0 - 360]
                float unit = (float) (angle/ (2*Math.PI));
                if (unit < 0) {
                    unit += 1;
                }

                mCirclePoint.set((int)event.getX(), (int) event.getY());
                postInvalidate();
                if (mColorChangeListener != null)
                    mColorChangeListener.colorChange(interpRectColor(colors, unit));
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    /**
     * 设置颜色确定圆点位置
     * @param a 透明度
     * @param r 红
     * @param g 绿
     * @param b 蓝
     */
    public void setColor(final int a, final int r, final int g, final int b){
        Log.e("COLOR", Integer.toHexString(Color.argb(a, r, g, b)) + "");
        if (r == 0){
            float p = Math.abs((g - 255)/255);
            float unit = p/(colors.length-1);
            Log.e("COLOR", "角度" + unit + "");
        }else if (g == 0){
            float p = Math.abs((r - 255)/255);
            float unit = p/(colors.length-1);
            Log.e("COLOR", "角度" + unit + "");
        }else if (b == 0){
            float p = Math.abs((g - 255)/255);
            float unit = p/(colors.length-1);
            Log.e("COLOR", "角度" + unit + "");
        }
    }

    private int interpRectColor(int colors[], float unit){
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

    private int ave(int c0, int c1, float p){
        return c0 + Math.round(p * (c1-c0));
    }

    public void setColorChangeListener(ColorChangeListener colorChangeListener){
        mColorChangeListener = colorChangeListener;
    }

    public interface ColorChangeListener{
        void colorChange(int rgbColor);
    }

}
