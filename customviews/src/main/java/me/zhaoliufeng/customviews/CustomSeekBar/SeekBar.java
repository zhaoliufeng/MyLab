package me.zhaoliufeng.customviews.CustomSeekBar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewTreeObserver;

import me.zhaoliufeng.customviews.R;

/**
 * Created by We-Smart on 2017/7/14.
 */

public class SeekBar extends View {

    private Bitmap mNormalBg;
    private Bitmap mSelectBg;
    private Bitmap mSliderBitmap;   //滑块图片
    private Bitmap mLeftBitmap;
    private Bitmap mRightBitmap;
    private Bitmap mMiddleBitmap;

    private Paint mPaint;

    private float mStartX;
    private float mEndX;
    private float mCurrX;

    private int mWidth;
    private int mHeight;
    private int space = 3;
    private OnPositionChangedListener mOnPositionChangedListener;

    public SeekBar(Context context) {
        super(context);
    }

    public SeekBar(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mNormalBg = BitmapFactory.decodeResource(getResources(), R.drawable.adjuster_rail_normal);
        mSelectBg = BitmapFactory.decodeResource(getResources(), R.drawable.adjuster_rail_selected);
        mSliderBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.adjuster_seek_slider);
        mLeftBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.room_icon_switch_on);
        mRightBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.room_icon_switch_off);
        mMiddleBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.adjuster_middle_line);

        mSliderBitmap = zoomImg(mSliderBitmap, 70, 0);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        setMeasuredDimension(MeasureSpec.getSize(widthMeasureSpec), mSliderBitmap.getHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;
        // 得到新的图片
        mNormalBg = zoomImg(mNormalBg, mWidth - 40, 0);
        mSelectBg = zoomImg(mSelectBg, mWidth - 40, 0);
        mLeftBitmap = zoomImg(mLeftBitmap, 0, mNormalBg.getHeight());
        mRightBitmap = zoomImg(mRightBitmap, 0, mNormalBg.getHeight());
        mMiddleBitmap = zoomImg(mMiddleBitmap, 0, (int) (mHeight * 0.1264f));

        mStartX = mSliderBitmap.getWidth()/2;
        mCurrX = mStartX;
        mEndX = mWidth - mSliderBitmap.getWidth();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                if (event.getX() > mEndX + mSliderBitmap.getWidth()/2) {
                    mCurrX = mEndX + mSliderBitmap.getWidth()/2;
                    break;
                }else if (event.getX() < mStartX ){
                    mCurrX = mStartX ;
                    break;
                }
                mCurrX = event.getX();
                //Log.i("X", (int)(100*(mCurrX/mEndX) - 6) + "----" );
                if (mOnPositionChangedListener != null)
                    mOnPositionChangedListener.onNewPosition((int)(100*(mCurrX/mEndX)));
                postInvalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        float top = mSliderBitmap.getHeight() * 0.3716f;
        //画左边原点
        canvas.drawBitmap(mLeftBitmap, 0, top, mPaint);
        //画中间线
        canvas.drawBitmap(mNormalBg, mLeftBitmap.getWidth() + space, top, mPaint);
        //画右边原点
        canvas.drawBitmap(mRightBitmap,mLeftBitmap.getWidth() +  mNormalBg.getWidth() + space *2, top, mPaint);
        //画经过线
        canvas.save();
        canvas.clipRect(0, 0,  mCurrX - mSliderBitmap.getWidth()/2, mHeight);
        canvas.drawBitmap(mSelectBg, mLeftBitmap.getWidth() + space, top, mPaint);
        canvas.restore();
        //画中间竖线
        canvas.drawBitmap(mMiddleBitmap, mWidth/2 - mMiddleBitmap.getWidth()/2, 0, mPaint);
        //画滑动块
        canvas.drawBitmap(mSliderBitmap, mCurrX - mSliderBitmap.getWidth()/2, 0, mPaint);
    }


    /**
     * 设置位置
     * @param x 位置 0 - 100
     */
    public void setPosition(final int x){
        if (x < 100 && x > 0) {
            this.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    mCurrX = x/100f * mEndX;
                    postInvalidate();
                }
            });
        }else {
            Log.e("SeekBar", "传入的位置参数范围为 0 - 100");
        }
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

    public void setOnPositionChangedListener(OnPositionChangedListener onPositionChangedListener){
        this.mOnPositionChangedListener = onPositionChangedListener;
    }

    public interface OnPositionChangedListener{
        void onNewPosition(int position);
    }
}
