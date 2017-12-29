package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by zhaol on 2017/11/20.
 */

public class ScanView extends View {

    private Paint mPaint;
    private Bitmap mBgBitmap;
    private Bitmap mFrontBitmap;
    private boolean mIsStop = true;

    private int mWidth;
    private int mSweepAngle = 0; // 绘制角度

    public ScanView(Context context) {
        super(context);
        init();
    }

    public ScanView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setDither(true);

        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_under);
        mFrontBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.scan_top);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mBgBitmap = zoomImg(mBgBitmap, w, 0);
        mFrontBitmap = zoomImg(mFrontBitmap, w, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawBitmap(mBgBitmap, 0, 0, mPaint);
        canvas.save();
        canvas.setDrawFilter(new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG|Paint.FILTER_BITMAP_FLAG));
        canvas.rotate(mSweepAngle, mWidth/2, mWidth/2);
        canvas.drawBitmap(mFrontBitmap, 0, 0, mPaint);
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mIsStop)return;
                if (mSweepAngle >= 360){
                    mSweepAngle = 0;
                }else {
                    mSweepAngle+=2;
                }
                postInvalidate();
            }
        }, 0);
    }

    public void startScan(){
        mIsStop = false;
        postInvalidate();
    }

    public void stopScan(){
        mIsStop = true;
        postInvalidate();
    }

    public boolean getScanStatus(){
        return mIsStop;
    }

    Handler mHandler = new Handler();

    private Bitmap zoomImg(Bitmap bm, int newWidth , int newHeight){
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
}
