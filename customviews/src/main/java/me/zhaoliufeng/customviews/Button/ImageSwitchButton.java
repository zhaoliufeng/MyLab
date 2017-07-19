package me.zhaoliufeng.customviews.Button;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.zhaoliufeng.customviews.R;

/**
 * Created by We-Smart on 2017/7/12.
 */

public class ImageSwitchButton extends View implements View.OnTouchListener {

    private Bitmap mBgBitmap;
    private Bitmap mStickBitmap;
    private Paint mOnPaint; //画On
    private boolean mStatus = false; //当前状态 1 开 0 关
    private String[] texts = {"OFF", "ON"};
    private int mStartX; //起始X
    private int mEndX;  //终点X
    private int mCurrX; //当前的X坐标 初始化为起始X
    private SelectChangeListener mSelectChangeListener;

    private int mHeight;
    private int mWidth;

    public ImageSwitchButton(Context context) {
        super(context);
        init();
    }

    public ImageSwitchButton(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        mBgBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bg_switch_slider);
        mStickBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.room_switch_slider);

        mOnPaint = new Paint();
        mOnPaint.setAntiAlias(true);
        mOnPaint.setColor(Color.WHITE);
        mOnPaint.setTextSize(25);
        setOnTouchListener(this);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        mHeight = h;

        mBgBitmap = zoomImg(mBgBitmap, w, 0);
        mStickBitmap = zoomImg(mStickBitmap, mBgBitmap.getHeight(), 0);

        mStartX = 0;
        mCurrX = mStartX;
        mEndX = mStartX + (mBgBitmap.getWidth() - mStickBitmap.getWidth()) + 1;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawColor(Color.parseColor("#474747"));
        //画ON
        Rect rect = new Rect();
        mOnPaint.getTextBounds(texts[0], 0, texts[0].length(), rect);
        int w = rect.width();
        int h = rect.height();
        canvas.drawText(texts[0], 0, mBgBitmap.getHeight()/2, mOnPaint);
        canvas.drawBitmap(mBgBitmap, w, 0, null);
        canvas.drawBitmap(mStickBitmap, w + mCurrX, 0, null);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                changeButtonState();
                mStatus = !mStatus;
                if (mSelectChangeListener != null){
                    mSelectChangeListener.onChange(mStatus);
                }
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return false;
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

    private void changeButtonState() {
        ValueAnimator sliderAnim;
        if (mStatus){
            sliderAnim = ValueAnimator.ofInt(mEndX, mStartX).setDuration(500);
        }else {
            sliderAnim = ValueAnimator.ofInt(mStartX, mEndX).setDuration(500);
        }

        sliderAnim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mCurrX = (int) animation.getAnimatedValue();
                postInvalidate();
            }
        });
        sliderAnim.start();
    }


    public void setSelectChangeListener(SelectChangeListener selectChangeListener){
        mSelectChangeListener = selectChangeListener;
    }

    public interface SelectChangeListener{
        void onChange(boolean isOPen);
    }
}
