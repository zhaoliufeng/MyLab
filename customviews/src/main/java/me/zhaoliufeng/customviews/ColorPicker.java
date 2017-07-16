package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ImageFormat;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import static android.content.ContentValues.TAG;

/**
 * Created by We-Smart on 2017/7/11.
 */

public class ColorPicker extends View implements View.OnTouchListener {

    private Paint mPaint;    //背景颜色画笔
    private Paint mCirclePaint;  //指示圆画笔
    private Point mCirclePoint;  //指示圆位置
    private int mCircleRadius;  //指示圆半径
    private Shader mSweepGradient; // 梯度渲染
    private boolean drawArc = false;
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

        if (drawArc){
            RectF oval = new RectF( 0, 0, getWidth(), getHeight());
            canvas.drawArc(oval, 180, (int)(360 * 0.7f),true,  mCirclePaint);
        }

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
                Log.e("COLOR", unit + " ");
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
        // FF XX 00
        if (r == 255 && b == 0){
            Log.i("COLOR", "3. setColor: " + g/255f + " i " + 0);
        }
        // XX FF 00
        if (g == 255 && b == 0){
            Log.i("COLOR", "2. setColor: " + r/255f + " i " + 1);
        }
        // 00 FF XX
        if (r == 0 && g == 255){
            Log.i("COLOR", "4. setColor: " + b/255f + " i " + 2);
        }
        // XX 00 FF
        if (g == 0 && b == 255){
            Log.i("COLOR", "5. setColor: " + r/255f + " i " + 3);
        }
        // FF 00 XX
        if (r == 255 && g == 0){
            float p = b/255f;
            Log.i("COLOR", "6. setColor: " + p + " i " + 4);

            float unit = (p + 4)/6;

            unit = (float) Math.toRadians(unit * 360);
            float x = (float) Math.sin(unit) * getHeight();
            float y = (float) Math.cos(unit) * getHeight();
            Log.i("COLOR", "unit " + x + " " + y);
            drawArc = true;

        }
        //FF XX 00
        if (r == 255 && b == 0){
            Log.i("COLOR", "7. setColor: " + g/255f + " i " + 5);
        }
    }

    /**
     * @param colors  颜色数组
     * @param unit  移动的角度 可以理解成在色带中 0 - 1 之间位置 获取对应位置的中间色
     * @return 颜色
     */
    private int interpRectColor(int colors[], float unit){
        //如果是小于0 意外的值 就使用第一个颜色
        if (unit < 0)
            return colors[0];
        //如果是大于1 意外的值 就使用最后一个颜色
        if (unit >= 1)
            return colors[colors.length - 1];

        //length - 1是值 长度为length的colors数组有length -1 个区间颜色
        float p = unit * (colors.length - 1);
        int i = (int)p;
        //此时p就是一个颜色区间的 0 - 1 的范围 而 i 就是 两个颜色区间的下标 i i+1
        p -=  i;

        int c0 = colors[i];
        int c1 = colors[i + 1];
        int a = ave(Color.alpha(c0), Color.alpha(c1), p);
        int r = ave(Color.red(c0), Color.red(c1), p);
        int g = ave(Color.green(c0), Color.green(c1), p);
        int b = ave(Color.blue(c0), Color.blue(c1), p);

        Log.i("COLOR", a + " " + r + " " + g + " " + b);
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
