package me.zhaoliufeng.customviews;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * Created by We-Smart on 2017/8/9 16:42.
 */

public class RingProgressBar extends View {

    //渐变色圆
    private int[] doughnutColors = { 0x001D2C39, 0xAA07A7CD, 0xff00CEFC };

    private RectF mRectFArc;    //圆环边界矩形
    private float mStrokeWidth = 5;     //描边粗细

    private int mStartAngle = 270; // 起始角度
    private int mSweepAngle = 0; // 绘制角度
    private Paint mPaint;
    private int mMaxVal = 100; //默认100
    private Shader mShader;     //渐变渲染
    private Paint mCirclePaint;
    private Point mStartPoint;

    public RingProgressBar(Context context) {
        super(context);
    }

    public RingProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                setAnimation();
            }
        }, 3000);
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
        mCirclePaint.setColor(Color.WHITE);

        mRectFArc = new RectF();

        mStartPoint = new Point();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRectFArc.set(
                0 + mStrokeWidth/2 + 6,
                0 + mStrokeWidth/2 + 6,
                getMeasuredWidth() - mStrokeWidth/2 - 6,
                getMeasuredHeight() - mStrokeWidth/2 - 6
        );
        //设置渐变图层
        mShader =  new SweepGradient(getMeasuredWidth()/2, getMeasuredHeight()/2, doughnutColors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(-92, getMeasuredWidth()/2, getMeasuredHeight()/2);
        mShader.setLocalMatrix(matrix);
        mPaint.setShader(mShader);
        mStartPoint.set(getMeasuredWidth()/2, (int) (6 + mStrokeWidth/2));
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectFArc, mStartAngle, mSweepAngle, false, mPaint);

        canvas.save();
        canvas.rotate(mSweepAngle, getWidth()/2, getHeight()/2);
        canvas.drawCircle(mStartPoint.x, mStartPoint.y, 6, mCirclePaint);
        canvas.restore();
    }

    public void setProgress(int progress){
        mSweepAngle = (360 * progress/mMaxVal);
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
