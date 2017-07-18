package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * Created by Zhao Liufeng on 2017/7/18.
 */

public class LightSeekBar extends View {

    private Bitmap mSunPic; //太阳图片
    private Paint mCirclePaint; //圆画笔
    private Paint mFrameCirclePaint; //边框圆
    private Paint mNormalRectPaint;  //背景矩形
    private Paint mSelectedRectPaint;   //选中矩形
    private int mWidth;
    private int mHeight;

    private int mRadius;
    private float mStartY;
    private float mEndY;
    private float mCurrY;
    private OnPositionChangedListener mOnPositionChangedListener;

    public LightSeekBar(Context context) {
        super(context);
        init();
    }

    public LightSeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mSunPic = BitmapFactory.decodeResource(getResources(), R.drawable.icon_sun);

        mCirclePaint = new Paint();
        mCirclePaint.setColor(Color.WHITE);
        mCirclePaint.setStyle(Paint.Style.FILL);
        mCirclePaint.setStrokeWidth(3);
        mCirclePaint.setAntiAlias(true);

        mNormalRectPaint = new Paint();
        mNormalRectPaint.setColor(Color.WHITE);
        mNormalRectPaint.setAlpha(80);
        mNormalRectPaint.setAntiAlias(true);

        mSelectedRectPaint = new Paint();
        mSelectedRectPaint.setColor(Color.WHITE);
        mSelectedRectPaint.setAntiAlias(true);

        mFrameCirclePaint = new Paint();
        mFrameCirclePaint.setColor(Color.GRAY);
        mFrameCirclePaint.setAntiAlias(true);
        mFrameCirclePaint.setStyle(Paint.Style.STROKE);
        mFrameCirclePaint.setStrokeWidth(1);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        mSunPic = zoomImg(mSunPic, w, 0);

        mCurrY = mStartY = h - mRadius - 20;
        mEndY = mSunPic.getHeight() + mRadius + 20;
        mRadius = mWidth/2 - 2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //画太阳
        canvas.drawBitmap(mSunPic, 0, 0, null);
        //画背景矩形
        canvas.drawRect(10, mSunPic.getHeight() + 10, mWidth - 10, mHeight - 10, mNormalRectPaint);
        //画选中矩形
        canvas.drawRect(10, mCurrY, mWidth - 10, mHeight - 10, mSelectedRectPaint);
        //画选中圆
        canvas.drawCircle(mWidth/2, mCurrY, mRadius, mCirclePaint);
        canvas.drawCircle(mWidth/2, mCurrY, mRadius, mFrameCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getY() < mEndY) {
                    mCurrY = mEndY;
                }else if (event.getY() > mStartY - mRadius){
                    mCurrY = mStartY;
                }else {
                    mCurrY = event.getY();
                }
                Log.e("SeekBar", " " + Math.round(100 * (mStartY - mCurrY)/(mStartY - mEndY)));
                if (mOnPositionChangedListener != null)
                    mOnPositionChangedListener.onNewPosition(Math.round(100 * (mStartY - mCurrY)/(mStartY - mEndY)));
                postInvalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }

    /**
     *  处理图片
     * @param bm 所要转换的bitmap
     * @param newWidth 新的宽
     * @param newHeight 新的高
     * @return 指定宽高的bitmap
     */
    public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){
        float scaleHeight;
        float scaleWidth;
        // 获得图片的宽高
        int width = bm.getWidth();
        int height = bm.getHeight();
        // 计算缩放比例
        if (newWidth == 0){
            scaleHeight = ((float) newHeight) / height;
            scaleWidth = scaleHeight;
        }else if (newHeight == 0){
            scaleWidth = ((float) newWidth) / width;
            scaleHeight = scaleWidth;
        }else{
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

    /**
     * 设置位置
     * @param y 位置 0 - 100
     */
    public void setPosition(final int y){
        if (y < 100 && y > 0) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mCurrY = mStartY - (y/100f) * (mStartY - mEndY);
                    postInvalidate();
                }
            });
        }else {
            Log.e("SeekBar", "传入的位置参数范围为 0 - 100");
        }
    }

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener){
        this.mOnPositionChangedListener = onPositionChangedListener;
    }

    public interface OnPositionChangedListener{
        void onNewPosition(int position);
    }
}
