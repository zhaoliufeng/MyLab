package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by Zhao Liufeng on 2017/7/18.
 */

public class LightSeekBar extends View {

    private Bitmap mSunPic; //太阳图片
    private Paint mCirclePaint; //圆画笔
    private Paint mNormalRectPaint;  //背景矩形
    private Paint mSelectedRectPaint;   //选中矩形
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
    }
}
