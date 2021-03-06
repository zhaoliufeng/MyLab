package me.zhaoliufeng.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RadialGradient;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by We-Smart on 2017/8/9 16:42.
 */

public class RingProgressBar extends View {

    //渐变色圆
    private int[] doughnutColors = {
            0x00DEDEDE, 0xAA07A7CD, 0xff00CEFC
    };

    private RectF mRectFArc;    //圆环边界矩形
    private float mStrokeWidth = 12;     //描边粗细

    private int mStartAngle = 270; // 起始角度
    private int mSweepAngle = 0; // 绘制角度
    private Paint mPaint;
    private int mMaxVal = 100; //默认100
    private Shader mShader;     //渐变渲染
    private Paint mCirclePaint;
    private Paint mBgCirclePaint; //背景圆画笔
    private Point mStartPoint;
    private Paint mLightCirclePaint;    //光效圆
    private RadialGradient radialShader;    //光效渲染

    public RingProgressBar(Context context) {
        super(context);
    }

    public RingProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setAnimation();
//            }
//        }, 3000);
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.parseColor("#00CEFC"));
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(mStrokeWidth);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setColor(Color.parseColor("#BDECFF"));

        mBgCirclePaint = new Paint();
        mBgCirclePaint.setAntiAlias(true);
        mBgCirclePaint.setStrokeWidth(mStrokeWidth);
        mBgCirclePaint.setStyle(Paint.Style.STROKE);
        mBgCirclePaint.setColor(Color.parseColor("#1D2C39"));

        mRectFArc = new RectF();

        mStartPoint = new Point();

        mLightCirclePaint = new Paint();
        mLightCirclePaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRectFArc.set(
                0 + mStrokeWidth/2 + 8,
                0 + mStrokeWidth/2 + 8,
                getMeasuredWidth() - mStrokeWidth/2 - 8,
                getMeasuredHeight() - mStrokeWidth/2 - 8
        );
        //设置渐变图层
        mShader =  new SweepGradient(getMeasuredWidth()/2, getMeasuredHeight()/2, doughnutColors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(-92, getMeasuredWidth()/2, getMeasuredHeight()/2);
        mShader.setLocalMatrix(matrix);
        mPaint.setShader(mShader);
        mStartPoint.set(getMeasuredWidth()/2, (int) (8 + mStrokeWidth/2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectFArc, 0, 360, false, mBgCirclePaint);

        canvas.drawArc(mRectFArc, mStartAngle, mSweepAngle, false, mPaint);

        canvas.save();
        canvas.rotate(mSweepAngle, getWidth()/2, getHeight()/2);
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 8, mCirclePaint);
        radialShader = new RadialGradient(mStartPoint.x, mStartPoint.y, 16, new int[]{Color.parseColor("#44ffffff"), Color.parseColor("#22ffffff"), Color.parseColor("#00ffffff")}, null, Shader.TileMode.CLAMP);
        mLightCirclePaint.setShader(radialShader);
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 16, mLightCirclePaint);
        canvas.restore();
    }

    public void setProgress(int progress){
        mSweepAngle = (360 * progress/mMaxVal);
        Log.i("RingProgress", "setProgress: " + progress + " Angle " + mSweepAngle);
        postInvalidate();
    }

    public void setMax(int maxVal){
        mMaxVal = maxVal;
    }

    public void setAnimation(){
        ValueAnimator animator = ValueAnimator.ofInt(0, 100).setDuration(5000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((Integer) animation.getAnimatedValue());
            }
        });
        animator.start();
    }
}
