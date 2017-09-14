package me.zhaoliufeng.customviews;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.view.animation.LinearInterpolator;

/**
 * OTA环形进度条
 */

public class OTAProgressBar extends View {

    private Paint mCirclePaint;//中心圆画笔
    private Paint mProgressPaint;//进度条画笔
    private Paint mProgressBgPaint; //进度条背景画笔
    private Paint mTextPaint;//文字画笔

    private int mStrokeWidth;   //进度条线宽
    private float mSweepAngle = 0;//旋转过的角度
    private RectF mRectFArc;    //中心圆的绘制bound
    private int mMaxVal = 100;  //最大值
    private int mPaddingVal;    //圆与进度条的间隔
    private String mShowText;   //显示的文字

    public OTAProgressBar(Context context) {
        super(context);
        init();
    }

    public OTAProgressBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
        //测试用
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                setAnimation();
//            }
//        }, 3000);
    }

    private void init(){
        mStrokeWidth = dp2px(7);
        mPaddingVal = dp2px(7);
        mShowText = "0%";

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setAntiAlias(true);

        mProgressPaint = new Paint();
        mProgressPaint.setColor(Color.parseColor("#FFF6C161")); //橙黄
        mProgressPaint.setAntiAlias(true);
        mProgressPaint.setStrokeCap(Paint.Cap.ROUND);
        mProgressPaint.setStrokeWidth(mStrokeWidth);
        mProgressPaint.setStyle(Paint.Style.STROKE);

        mProgressBgPaint = new Paint();
        mProgressBgPaint.setColor(Color.parseColor("#FF101E2A"));
        mProgressBgPaint.setAntiAlias(true);
        mProgressBgPaint.setStrokeWidth(mStrokeWidth);
        mProgressBgPaint.setStyle(Paint.Style.STROKE);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.WHITE);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextSize(mStrokeWidth * 4);

        mRectFArc = new RectF();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        Shader mShader = new LinearGradient(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, h,
                new int[]{0xFF203D5A, 0xFF14222F}, null, Shader.TileMode.CLAMP);
        mCirclePaint.setShader(mShader);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mStrokeWidth = (int) (getMeasuredWidth() * 3.5f/100f);
        mProgressPaint.setStrokeWidth(mStrokeWidth);
        mProgressBgPaint.setStrokeWidth(mStrokeWidth);
        mPaddingVal = mStrokeWidth;

        mRectFArc.set(
                mStrokeWidth/2,
                mStrokeWidth/2,
                getMeasuredWidth() - mStrokeWidth/2,
                getMeasuredWidth() - mStrokeWidth/2
        );

        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2 - mStrokeWidth - mPaddingVal, mCirclePaint);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getWidth()/2 - mStrokeWidth/2, mProgressBgPaint);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        float top = fontMetrics.top;//为基线到字体上边框的距离,即上图中的top
        float bottom = fontMetrics.bottom;//为基线到字体下边框的距离,即上图中的bottom
        int baseLineY = (int) (getHeight()/2 - top/2 - bottom/2);//基线中间点的y轴计算公式
        canvas.drawText(mShowText, getWidth()/2 -
                mTextPaint.measureText(mShowText)/2, baseLineY, mTextPaint);

        //弧形起始绘制角度 270
        canvas.drawArc(mRectFArc, 270, mSweepAngle, false, mProgressPaint);
    }

    /**
     * 设置进度
     * @param progress 进度值
     */
    public void setProgress(int progress){
        mShowText = progress + "%";
        mSweepAngle = (360 * progress/mMaxVal);
        postInvalidate();
    }

    /**
     * 设置进度最大值
     * @param maxVal 最大值
     */
    public void setMaxVal(int maxVal){
        mMaxVal = maxVal;
    }

    /**
     * 设置进度条宽
     * @param dp 设置进度条宽
     */
    public void setProgressWidth(int dp){
        mStrokeWidth = dp2px(dp);
    }

    /**
     * 设置当前显示的文本
     * @param showStr 需要显示的文本 “成功” “失败”
     * @param color 文本的颜色
     */
    public void setText(String showStr, int color){
        mShowText = showStr;
        mTextPaint.setColor(color);
        mProgressPaint.setColor(color);
        postInvalidate();
    }

    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    //测试用
    private void setAnimation(){
        ValueAnimator animator = ValueAnimator.ofInt(0, 100).setDuration(8000);
        animator.setInterpolator(new LinearInterpolator());
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((Integer) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setText("成功", Color.parseColor("#22BB11"));
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        animator.start();
    }
}
