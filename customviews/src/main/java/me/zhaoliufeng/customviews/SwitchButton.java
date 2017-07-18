package me.zhaoliufeng.customviews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by We-Smart on 2017/7/11.
 */

public class SwitchButton extends View implements View.OnTouchListener {

    private Paint bgPaint;  //背景画笔
    private Paint circlePaint;  //圆画笔
    private Paint textOnPaint;    //ON文字画笔
    private Paint textOffPaint;    //OFF文字画笔
    private int mWidth;     //控件宽度
    private int mHeight;     //控件高度
    private int mCircleRadius;  //圆半径
    private String[] texts = {"OFF", "ON"};
    private int mStatus = 0; //当前状态 1 开 0 关
    private int mStartX; //起始X
    private int mEndX;  //终点X
    private int mCurrX; //当前的X坐标 初始化为起始X
    private int mCirclePadding = 3;  //圆与边界的距离
    private boolean isAimPaying = false;    //标识当前是否正在播放动画

    private SelectChangeListener mSelectChangeListener;
    public SwitchButton(Context context) {
        super(context);
        initPaint();
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint(){
        bgPaint = new Paint();
        bgPaint.setColor(Color.parseColor("#8CB216"));
        bgPaint.setStyle(Paint.Style.FILL);
        bgPaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#FFFFFF"));
        circlePaint.setStyle(Paint.Style.FILL);

        textOnPaint = new Paint();
        textOnPaint.setColor(Color.parseColor("#FFFFFF"));
        textOnPaint.setAntiAlias(true);
        textOnPaint.setTextSize(25);
        textOnPaint.setAlpha(0);

        textOffPaint = new Paint();
        textOffPaint.setColor(Color.parseColor("#FFFFFF"));
        textOffPaint.setAntiAlias(true);
        textOffPaint.setTextSize(25);

        mCircleRadius = 10;
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mCircleRadius = mWidth/5 - mCirclePadding; //圆的直径是控件的高度

        mCurrX = mStartX = mCircleRadius + mCirclePadding;
        mEndX = mWidth - mStartX;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画背景
        RectF rectF = new RectF();
        rectF.left = 0;
        rectF.top = (mHeight - mWidth/2.5f)/2;
        rectF.right = mWidth;
        rectF.bottom = (mHeight - mWidth/2.5f)/2 + mWidth/2.5f;
        canvas.drawRoundRect(rectF, mWidth/5, mWidth/5, bgPaint);

        //画圆
        canvas.drawCircle(mCurrX, mHeight/2, mCircleRadius, circlePaint);

        //画字OFF
        Rect rect = new Rect();
        textOffPaint.getTextBounds(texts[0], 0, texts[0].length(), rect);
        int w = rect.width();
        int h = rect.height();
        canvas.drawText(texts[0], mWidth - w - mCircleRadius, (mHeight + h)/2, textOffPaint);

        //画字ON
        textOffPaint.getTextBounds(texts[1], 0, texts[1].length(), rect);
        h = rect.height();
        canvas.drawText(texts[1], mCircleRadius, (mHeight + h)/2, textOnPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                changeButtonState();
                if (mSelectChangeListener != null){
                    mSelectChangeListener.onChange(mStatus == 1);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }

    private void changeButtonState(){
        ValueAnimator circleAnimator;
        ValueAnimator textOffAnimator;
        ValueAnimator textOnAnimator;

        if (isAimPaying)return;

        if (mStatus == 0){
            //圆的移动
            circleAnimator = ValueAnimator.ofInt(mStartX, mEndX).setDuration(500);

            //文字的淡入淡出
            textOffAnimator = ValueAnimator.ofInt(255, 0).setDuration(500);
            textOnAnimator = ValueAnimator.ofInt(0, 255).setDuration(500);
            mStatus = 1;
        }else {
            //圆的移动
            circleAnimator = ValueAnimator.ofInt(mEndX, mStartX).setDuration(500);

            //文字的淡入淡出
            textOffAnimator = ValueAnimator.ofInt(0, 255).setDuration(500);
            textOnAnimator = ValueAnimator.ofInt(255, 0).setDuration(500);
            mStatus = 0;
        }

        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        textOffAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textOffPaint.setAlpha((int) animation.getAnimatedValue());
                postInvalidate();
            }
        });

        textOnAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                textOnPaint.setAlpha((int) animation.getAnimatedValue());
                postInvalidate();
            }
        });
        circleAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                isAimPaying = true;
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                isAimPaying = false;
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });

        circleAnimator.start();
        textOffAnimator.start();
        textOnAnimator.start();
    }

    public void setSelectChangeListener(SelectChangeListener selectChangeListener){
        mSelectChangeListener = selectChangeListener;
    }

    public interface SelectChangeListener{
        void onChange(boolean isOpen);
    }
}
