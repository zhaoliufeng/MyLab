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
    private final static float MAX_VALUE = 255f;
    private Paint mPaint;
    private boolean stopRecord = false;

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
            int[] rgbwc = sysData(mData);
            Log.i("MusicService",rgbwc[0]/4 +"  --  " + rgbwc[1]/4 + "  --  " +  rgbwc[2]/4);
            Random random = new Random();

            int s = random.nextInt(2)%(2+1);
            rgbwc[s] = 0;
            mPaint.setARGB(255, (int) (rgbwc[0]/3.5f), (int) (rgbwc[1]/3.5f), (int) (rgbwc[2]/3.5f));
            for (int i = 0; i < mData.length; i++) {
                canvas.drawRect(mSingeWidth * i, ((mData[i] + 128) / MAX_VALUE) * mHeight,
                        mSingeWidth * i + mSingeWidth, mHeight, mPaint);
            }
        }
    }

    public int[] sysData(byte[] data) {
        int[] Val = new int[3];
        for (int j = 0; j < 3; j++) {
            for (int i = j * data.length / 3; i < (j + 1) * (data.length / 3); i++) {
                Val[j] = Val[j] + (data[i] + 128);
            }
            Val[j] = Val[j] - 25435;
        }
        return Val;
    }

    public void setData(byte[] data) {
        this.mData = data;
        this.mSingeWidth = mWidth / (float) data.length;
        if (!stopRecord)
            postInvalidate();
    }

    @Override
    public void onClick(View v) {
        stopRecord = !stopRecord;
    }
}
