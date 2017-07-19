package me.zhaoliufeng.customviews.ViewColorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import me.zhaoliufeng.customviews.R;

/**
 * Created by We-Smart on 2017/7/19.
 */

public class RingColorPicker extends View{

    //渐变色圆
    private int[] doughnutColors = { 0xffffb61a, 0xffffdc92, 0xfffffefc };
    private Paint mRingPaint;
    private Paint mCirclePaint;

    private RectF mRectFArc;    //圆环边界矩形
    private float mStrokeWidth = 150;     //描边粗细
    private int mStartAngle = 130; // 起始角度
    private int mSweepAngle = 280; // 绘制角度 30 + 180 + 30

    private Bitmap mCenterBitmap;

    public RingColorPicker(Context context) {
        super(context);
        init();
    }

    public RingColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init(){
        //初始化圆环画笔
        mRingPaint = new Paint();
        mRingPaint.setAntiAlias(true);
        mRingPaint.setStrokeCap(Paint.Cap.ROUND);
        mRingPaint.setStrokeWidth(mStrokeWidth);
        mRingPaint.setStyle(Paint.Style.STROKE);
        mRingPaint.setColor(Color.YELLOW);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setAlpha(50);
        mCirclePaint.setColor(Color.WHITE);
        mRectFArc = new RectF();

        mCenterBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_off);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mRectFArc.set(
                0 + mStrokeWidth/2,
                0 + mStrokeWidth/2,
                getMeasuredWidth() - mStrokeWidth/2,
                getMeasuredHeight() - mStrokeWidth/2
        );

        //设置渐变图层
        Shader shader =  new SweepGradient(getMeasuredWidth()/2, getMeasuredHeight()/2, doughnutColors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(90, getMeasuredWidth()/2, getMeasuredHeight()/2);
        shader.setLocalMatrix(matrix);
        mRingPaint.setShader(shader);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        //设置图片大小
        mCenterBitmap = zoomImg(mCenterBitmap, (int) (w - 2 * mStrokeWidth - 30), 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectFArc, mStartAngle, mSweepAngle, false, mRingPaint);
        canvas.drawBitmap(mCenterBitmap, mStrokeWidth + 15, mStrokeWidth + 15 , null);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                touchOnCenterCircle(getWidth()/2, getHeight()/2, mCenterBitmap.getHeight()/2, event.getX(), event.getY());
                break;
        }
        return true;
    }

    private boolean touchOnCenterCircle(int centerX, int centerY, int radius, float touchX, float touchY){
        double distance = Math.pow(Math.sqrt(Math.abs(touchX - centerX)) + Math.sqrt(Math.abs(touchY - centerY)), 0.5);
        Log.i("DISTANCE", distance + "");
        return distance < radius;
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
}
