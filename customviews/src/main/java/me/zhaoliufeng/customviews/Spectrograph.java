package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.Locale;
import java.util.Random;

/**
 * Created by zhaol on 2017/11/1.
 */

public class Spectrograph extends View implements View.OnClickListener {
    private byte[] mData;
    private int mHeight, mWidth;
    private float mSingeWidth;
    private int mSkipStep = 5;
    private final static float MAX_VALUE = 255f;
    private Paint mPaint;
    private boolean stopRecord = false;
    private int[] mRgbwc = new int[5];

    public Spectrograph(Context context) {
        super(context);
        init();
    }

    public Spectrograph(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        this.setOnClickListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.mHeight = h;
        this.mWidth = w;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制mData.length个矩形
        if (mData != null) {
            for (int i = 0, j = 0; i < mData.length; i += mSkipStep, j++) {
                Log.i("music", "data : " + mData[i]);
                if (i < mData.length)
                    canvas.drawRect(mSingeWidth * j, ((mData[i] + 128) / MAX_VALUE) * mHeight,
                            mSingeWidth * j + mSingeWidth, mHeight, mPaint);
            }
        }
    }

    public void setData(byte[] data) {
        this.mData = data;
        int index = 0;
        mRgbwc = new int[5];
        long sum = 0;
//        for (int i = 0; i < mData.length; i += mSkipStep) {
//            if (i < mData.length) {
//                index = i / (data.length / 4);
////                data[i] += 90;
//                if (data[i] > 0)
//                    mRgbwc[index] += data[i];
//            }
//        }
        Log.i("rgbwc", "data : " + mRgbwc[0] + "  " + mRgbwc[1] + "  " + mRgbwc[2] + "  " + mRgbwc[3] + "  " + mRgbwc[4]);
        this.mSingeWidth = mWidth / (float) data.length * mSkipStep;
        if (!stopRecord)
            postInvalidate();
    }

    @Override
    public void onClick(View v) {
        stopRecord = !stopRecord;
    }
}
