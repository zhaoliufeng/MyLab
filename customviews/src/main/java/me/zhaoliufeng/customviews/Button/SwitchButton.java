package me.zhaoliufeng.customviews.Button;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import me.zhaoliufeng.customviews.R;

/**
 * Created by We-Smart on 2017/7/11.
 */

public class SwitchButton extends View implements View.OnTouchListener {

    private Paint bgFillPaint;  //背景画笔
    private Paint bgRoundPaint; //背景边框画笔
    private Paint circlePaint;  //圆画笔
    private Paint circleStrokenPaint; //圆边框画笔
    private int mWidth;     //控件宽度
    private int mHeight;     //控件高度
    private int mCircleRadius;  //圆半径
    private String[] texts = {"OFF", "ON"};
    private boolean mStatus = false; //当前状态 1 开 0 关
    private int mStartX; //起始X
    private int mEndX;  //终点X
    private int mCurrX; //当前的X坐标 初始化为起始X
    private int mCirclePadding = 2;  //圆与边界的距离
    private boolean isAimPaying = false;    //标识当前是否正在播放动画

    private OnStateChangedListener mSelectChangeListener;
    public SwitchButton(Context context) {
        super(context);
        initPaint();
    }

    public SwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initPaint();
    }

    private void initPaint(){

        bgFillPaint = new Paint();
        bgFillPaint.setColor(Color.parseColor("#4bd763"));
        bgFillPaint.setStyle(Paint.Style.FILL);
        bgFillPaint.setAntiAlias(true);
        bgFillPaint.setAlpha(0);

        bgRoundPaint = new Paint();
        bgRoundPaint.setColor(Color.parseColor("#cccccc"));
        bgRoundPaint.setStrokeWidth(3);
        bgRoundPaint.setStyle(Paint.Style.STROKE);
        bgRoundPaint.setAntiAlias(true);

        circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setColor(Color.parseColor("#FFFFFF"));
        circlePaint.setStyle(Paint.Style.FILL);

        circleStrokenPaint = new Paint();
        circleStrokenPaint.setAntiAlias(true);
        circleStrokenPaint.setColor(Color.parseColor("#888888"));
        circleStrokenPaint.setStyle(Paint.Style.STROKE);
        circleStrokenPaint.setStrokeWidth(2);

        mCircleRadius = 10;
        setOnTouchListener(this);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), (int) (getMeasuredWidth()/1.5));
        mWidth = getMeasuredWidth();
        mHeight = getMeasuredHeight();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mCircleRadius = (int) (mWidth/3.6f - mCirclePadding); //圆的直径是控件的高度

        mCurrX = mStartX = mCircleRadius + mCirclePadding + 2;
        mEndX = mWidth - mStartX - 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        rectF.left = 2;
        rectF.top = (mHeight - mWidth/1.8f)/2;
        rectF.right = mWidth - 2;
        rectF.bottom = (mHeight - mWidth/1.8f)/2 + mWidth/1.8f;
        canvas.drawRoundRect(rectF, mWidth/3.6f, mWidth/3.6f, bgRoundPaint);

        //画背景
        canvas.drawRoundRect(rectF, mWidth/3.6f, mWidth/3.6f, bgFillPaint);

        //画圆
        canvas.drawCircle(mCurrX, mHeight/2, mCircleRadius, circlePaint);

        if (mStatus)
            circleStrokenPaint.setColor(Color.parseColor("#4bd763"));
        else
            circleStrokenPaint.setColor(Color.parseColor("#cccccc"));
        canvas.drawCircle(mCurrX, mHeight/2, mCircleRadius, circleStrokenPaint);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                changeChecked();

                if (mSelectChangeListener != null){
                    mSelectChangeListener.onChange(mStatus);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
    }

    public void changeChecked(){
        ValueAnimator circleAnimator;
        ValueAnimator colorAnimator;

        if (isAimPaying)return;
        if (!mStatus){
            //圆的移动
            circleAnimator = ValueAnimator.ofInt(mStartX, mEndX).setDuration(300);
            colorAnimator = ValueAnimator.ofInt(0, 255).setDuration(300);
            mStatus = true;
        }else {
            //圆的移动
            circleAnimator = ValueAnimator.ofInt(mEndX, mStartX).setDuration(300);
            colorAnimator = ValueAnimator.ofInt(255, 0).setDuration(300);
            mStatus = false;
        }

        circleAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });

        colorAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                bgFillPaint.setAlpha((int)animation.getAnimatedValue());
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
        colorAnimator.start();
    }

    public void setState(boolean open){
        if (mStatus != open){
            mStatus = open;
            handler.sendEmptyMessageDelayed(0, 300);
        }
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            changeChecked();
        }
    };

    public void setOnStateChangedListener(OnStateChangedListener selectChangeListener){
        mSelectChangeListener = selectChangeListener;
    }

    public interface OnStateChangedListener{
        void onChange(boolean isOpen);
    }
}
