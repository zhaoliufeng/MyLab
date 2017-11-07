package me.zhaoliufeng.customviews.CustomSeekBar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import me.zhaoliufeng.customviews.R;


/**
 * Created by We-Smart on 2017/7/27.
 */

public class SunSeekBar extends View {

    private Paint mBitmapPaint; //图片画笔
    private Paint mCirclePaint; //圆画笔
    private Paint mRectSelectPaint; //拖过路径画笔
    private Paint mRectNormalPaint; //背景路径画笔
    private float mRectHeight;
    private Paint mFrameCirclePaint; //边框圆

    private Bitmap mBitmapLight;

    private float mStartX;
    private float mEndX;
    private float mCurrX;

    private int mCircleRadius = 30; //滑块圆半径

    private int mMarginVal = 10;
    private OnPositionChangedListener mOnPositionChangedListener;

    public SunSeekBar(Context context) {
        super(context);
        init();
    }

    public SunSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mRectNormalPaint = new Paint();
        mRectNormalPaint.setAntiAlias(true);
        mRectNormalPaint.setColor(Color.WHITE);
        mRectNormalPaint.setAlpha(80);

        mRectSelectPaint = new Paint();
        mRectSelectPaint.setAntiAlias(true);
        mRectSelectPaint.setColor(Color.WHITE);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setColor(Color.WHITE);

        mFrameCirclePaint = new Paint();
        mFrameCirclePaint.setColor(Color.GRAY);
        mFrameCirclePaint.setAntiAlias(true);
        mFrameCirclePaint.setStyle(Paint.Style.STROKE);
        mFrameCirclePaint.setStrokeWidth(1);

        mBitmapLight = BitmapFactory.decodeResource(getResources(), R.drawable.icon_sun);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        mBitmapLight = zoomImg(mBitmapLight, 0, getMeasuredHeight());
        mCircleRadius = mBitmapLight.getHeight() / 2;

        mRectHeight = mBitmapLight.getHeight() * 0.206f;
        mStartX = mCircleRadius + mMarginVal;
        mCurrX = mStartX;
        mEndX = getMeasuredWidth() - mBitmapLight.getWidth() - mCircleRadius - mMarginVal;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        RectF rectF = new RectF();
        rectF.left = mMarginVal;
        rectF.top = (int) (getHeight() / 2f - mRectHeight / 2f);
        rectF.right = getMeasuredWidth() - mBitmapLight.getWidth() - mMarginVal;
        rectF.bottom = (int) (getHeight() / 2f + mRectHeight / 2f);
        //画中间背景
        canvas.drawRect(rectF, mRectNormalPaint);

        //画太阳
        canvas.drawBitmap(mBitmapLight, getWidth() - mBitmapLight.getWidth(), (int) (getHeight() / 2f - mBitmapLight.getHeight() / 2f), mBitmapPaint);

        rectF.right = rectF.left + mCurrX - mCircleRadius;
        //画滑过背景
        canvas.drawRect(rectF, mRectSelectPaint);

        //画圆
        canvas.drawCircle(mCurrX, getHeight() / 2, mCircleRadius, mCirclePaint);
        canvas.drawCircle(mCurrX, getHeight() / 2, mCircleRadius, mFrameCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (event.getX() > mEndX) {
                    mCurrX = mEndX;
                    break;
                } else if (event.getX() < mStartX) {
                    mCurrX = mStartX;
                    break;
                }
                mCurrX = event.getX();
                if (mOnPositionChangedListener != null)
                    mOnPositionChangedListener.onNewPosition((int) (101 * ((mCurrX - mStartX) / (mEndX - mStartX))), event.getAction() == MotionEvent.ACTION_UP);
                postInvalidate();
                break;
        }
        return true;
    }

    /**
     * 处理图片
     *
     * @param bm        所要转换的bitmap
     * @param newWidth  新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
        float scaleHeight;
        float scaleWidth;
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        if (newWidth == 0) {
            scaleHeight = ((float) newHeight) / height;
            scaleWidth = scaleHeight;
        } else if (newHeight == 0) {
            scaleWidth = ((float) newWidth) / width;
            scaleHeight = scaleWidth;
        } else {
            scaleHeight = ((float) newHeight) / height;
            scaleWidth = ((float) newWidth) / width;
        }

        // 取得想要缩放的matrix参数
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片
        Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);
        return newbm;
    }

    public void setPosition(final int x) {
        if (x < 100 && x >= 0) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mCurrX = x / 100f * (mEndX - mStartX) + mStartX;
                    postInvalidate();
                }
            });
        } else {
            Log.e("SeekBar", "传入的位置参数范围为 0 - 100");
        }
    }

    public void reSetPosition(final int x){
        mCurrX = x / 100f * (mEndX - mStartX) + mStartX;
        postInvalidate();
    }

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener) {
        this.mOnPositionChangedListener = onPositionChangedListener;
    }

    public interface OnPositionChangedListener {
        void onNewPosition(int position, boolean isUp);
    }
}
