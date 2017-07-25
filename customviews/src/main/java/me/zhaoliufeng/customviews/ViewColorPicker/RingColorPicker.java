package me.zhaoliufeng.customviews.ViewColorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
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

    public enum MODE{
        FIVE_CHANNEL_MODE,  //五路灯
        THREE_CHANNEL_MODE,  //三路彩光灯
        TWO_CHANNEL_MODE,   //两路冷暖灯
        ONE_CHANNEL_MODE    //一路灯
    }
    private MODE currMode = MODE.TWO_CHANNEL_MODE;   //当前模式 默认五路灯
    //渐变色圆
    private int[] doughnutColors = { 0xffffb61a, 0xffffdc92, 0xfffffefc };
    private Paint mRingPaint;
    private Paint mCirclePaint;     //中心圆画笔
    private Point mCirclePoint;  //指示圆位置

    private RectF mRectFArc;    //圆环边界矩形
    private float mStrokeWidth = 150;     //描边粗细
    private int mStartAngle = 130; // 起始角度
    private int mSweepAngle = 280; // 绘制角度 30 + 180 + 30
    private int mWidth;
    private int mSmallRadius;   //内圆半径
    private int mBigRadius;     //外圆半径
    private int mCurrRingRadius = 18;    //指示圆环半径
    private int mPaddingVal = 15; //内推距离
    private Shader mShader;     //渐变渲染

    private boolean switchState = false; //开关状态
    private Bitmap mOffBitmap;
    private Bitmap mOnBitmap;

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
        mRingPaint.setColor(Color.parseColor("#FF00CEFC"));

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);
        mCirclePaint.setStyle(Paint.Style.STROKE);
        mCirclePaint.setStrokeWidth(5);
        mCirclePaint.setColor(Color.parseColor("#FFEEEEEE"));
        mRectFArc = new RectF();

        mOffBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_off);
        mOnBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.switch_on);

        mCirclePoint = new Point();
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

        float x = (float) Math.sin(Math.toRadians(40.0)) * ( mBigRadius - mStrokeWidth/2 );
        float y = (float) Math.cos(Math.toRadians(40.0)) * ( mBigRadius - mStrokeWidth/2 );
        x = getWidth()/2 - x;
        y = getHeight()/2 + y;

        mCirclePoint.set((int)x, (int)y);
        //设置渐变图层
        mShader =  new SweepGradient(getMeasuredWidth()/2, getMeasuredHeight()/2, doughnutColors, null);
        Matrix matrix = new Matrix();
        matrix.setRotate(90, getMeasuredWidth()/2, getMeasuredHeight()/2);
        mShader.setLocalMatrix(matrix);
        mRingPaint.setShader(mShader);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth = w;
        //设置图片大小
        mOffBitmap = zoomImg(mOffBitmap, (int) (mWidth - 2 * mStrokeWidth -  mPaddingVal * 2), 0);
        mOnBitmap = zoomImg(mOnBitmap, (int) (mWidth - 2 * mStrokeWidth - mPaddingVal * 2), 0);

        mSmallRadius = mOnBitmap.getWidth()/2;
        mBigRadius = w/2;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawArc(mRectFArc, mStartAngle, mSweepAngle, false, mRingPaint);
        if (switchState){
            canvas.drawBitmap(mOnBitmap, mStrokeWidth + mPaddingVal, mStrokeWidth + mPaddingVal , null);
        }else {
            canvas.drawBitmap(mOffBitmap, mStrokeWidth + mPaddingVal, mStrokeWidth + mPaddingVal , null);
        }

        if(currMode != MODE.ONE_CHANNEL_MODE)
            canvas.drawCircle(mCirclePoint.x, mCirclePoint.y, mCurrRingRadius, mCirclePaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getX() - getWidth() / 2;
        float y = event.getY() - getHeight() / 2;
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                if (touchOnCenterCircle(getWidth()/2, getHeight()/2, mSmallRadius, event.getX(), event.getY())){
                    switchState = !switchState;
                    postInvalidate();
                }

                break;
            case MotionEvent.ACTION_MOVE:
            case MotionEvent.ACTION_UP:
                if (touchOnRingCircle(getWidth()/2, getHeight()/2, mBigRadius, mSmallRadius, event.getX(), event.getY()) && touchOnFan(y, x)
                        ||touchOnLeftStrokeCircle(event.getX(), event.getY()) || touchOnRightStrokeCircle(event.getX(), event.getY()) ){
                    // TODO: 2017/7/25 在圆环上
                    mCirclePoint.set((int) event.getX(), (int) event.getY());
                    Log.i("COLOR", String.valueOf(getPercentage(x, y)));
                    if (currMode == MODE.TWO_CHANNEL_MODE){

                    }
                }else{
                    // TODO: 2017/7/25 不在圆环上
                   //mCirclePoint = getBorderPoint(new Point(getWidth()/2, getHeight()/2), new Point((int)event.getX(), (int)event.getY()), mBigRadius, 10);
                }

                break;
        }
        postInvalidate();
        return true;
    }

    /**
     * 改变模式 色温双路灯 ---- 单路灯
     */
    public void changeMode(MODE mode){
        currMode = mode;
        switch(currMode){
            case FIVE_CHANNEL_MODE:

                break;
            case THREE_CHANNEL_MODE:

                break;
            case TWO_CHANNEL_MODE:
                mRingPaint.setShader(mShader);
                getAngle();
                break;
            case ONE_CHANNEL_MODE:
                mRingPaint.setShader(null);
                break;
        }
        postInvalidate();
    }

    public MODE getMode(){
        return currMode;
    }
    /**
     * @return 扇区面积
     */
    private float getFanArea(){
        float fanArea = (float) ((Math.toRadians(280) * Math.PI * Math.pow(mBigRadius, 2)) / 360.0f + Math.PI * Math.pow(mStrokeWidth/2, 2));
        Log.i("RING", fanArea + " ");
        return fanArea;
    }
    /**
     * 判断是否触摸在中心开关上
     * @param centerX 中心点的X坐标
     * @param centerY 中心点的Y坐标
     * @param radius 中心圆的半径
     * @param touchX   触摸的X坐标
     * @param touchY    触摸的Y坐标
     * @return  是否触摸在开关上
     */
    private boolean touchOnCenterCircle(int centerX, int centerY, int radius, float touchX, float touchY){
        double distance = Math.sqrt(Math.pow(Math.abs(touchX - centerX), 2) + Math.pow(Math.abs(touchY - centerY), 2));
        return distance < radius;
    }

    /**
     * 获取是否触摸在扇形区域 起始 130 扇区角度280
     * @param x 触摸坐标的X
     * @param y 触摸坐标的Y
     * @return 是否触摸在扇区上
     */
    private boolean touchOnFan(float x, float y){
        float angle = (float) Math.atan2(y, x); //获取角度 0 - 1 [0 - 360]
        float unit = (float) (angle/ (2*Math.PI));
        if (unit < 0) {
            unit += 1;
        }

        return unit <= 1 - 40/360f && unit >= 40/360f;
    }

    /**
     * @param x 移动点的X坐标
     * @param y 移动点的Y坐标
     * @return 移动距离百分比 0 - 1 f
     */
    private float getPercentage(float x, float y){
        //当前坐标点的角度
        float angle = (float) Math.atan2(y, x);
        float unit = (float) (angle/ (2*Math.PI));
        if (unit < 0) {
            unit += 1;
        }
        //起始角度 扇形起始角度 - 中心点与线圆角切线角度
        float ang1 = (float) (Math.toRadians(130 - getAngle()));
        float u1 = (float) (ang1/ (2*Math.PI));
        if (u1 < 0) {
            u1 += 1;
        }
        //结束角度 扇形截至角度（130 + 320 - 90） + 中心点与线圆角切线角度
        float ang2 = (float) (Math.toRadians(50 + getAngle()));
        float u2 = (float) (ang2/ (2*Math.PI));
        if (u2 < 1) {
            u2 += 1;
        }
        //当前角度百分比 0 - 1
        if (unit < 1 && unit < u1)
            unit +=1;
        float b = (unit - u1)/ (u2 - u1);
        return 1 - b;
    }

    private float getAngle(){
        return (float) (180 * Math.asin((mStrokeWidth/2 - mCurrRingRadius) / (mStrokeWidth/2 + mSmallRadius))/Math.PI);
    }

    /**
     * 判断是否触摸在左边的圆上
     * @param tx 触摸坐标的x
     * @param ty 触摸坐标的y
     * @return 是否触摸在圆上
     */
    private boolean touchOnLeftStrokeCircle(float tx, float ty){
        float x = (float) Math.sin(Math.toRadians(40.0)) * ( mBigRadius - mStrokeWidth/2 );
        float y = (float) Math.cos(Math.toRadians(40.0)) * ( mBigRadius - mStrokeWidth/2 );
        x = getWidth()/2 - x;
        y = getHeight()/2 + y;
        double distance = Math.sqrt(Math.pow(Math.abs(tx - x), 2) + Math.pow(Math.abs(ty - y), 2));
        return distance < mStrokeWidth/2 -  mCurrRingRadius/2 - 2;
    }


    /**
     * 判断是否触摸在右边的圆上
     * @param tx 触摸坐标的x
     * @param ty 触摸坐标的y
     * @return 是否触摸在圆上
     */
    private boolean touchOnRightStrokeCircle(float tx, float ty){
        float x = (float) Math.sin(Math.toRadians(40.0)) * ( mBigRadius - mStrokeWidth/2 );
        float y = (float) Math.cos(Math.toRadians(40.0)) * ( mBigRadius - mStrokeWidth/2 );
        x = getWidth()/2 + x;
        y = getHeight()/2 + y;
        double distance = Math.sqrt(Math.pow(Math.abs(tx - x), 2) + Math.pow(Math.abs(ty - y), 2));
        return distance < mStrokeWidth/2 - mCurrRingRadius/2 - 2;
    }

    /**
     * 判断是否触摸在边缘色盘上
     * @param centerX  中心点的X坐标
     * @param centerY 中心点的Y坐标
     * @param ringRadius 边缘色盘圆的半径
     * @param centerRadius 中心圆的半径
     * @param touchX 触摸的X坐标
     * @param touchY 触摸的Y坐标
     * @return 是否触摸在边缘色盘上
     */
    private boolean touchOnRingCircle(int centerX, int centerY, int ringRadius, int centerRadius, float touchX, float touchY){
        double ringDistance = Math.sqrt(Math.pow(Math.abs(touchX - centerX), 2) + Math.pow(Math.abs(touchY - centerY), 2));
        return ringDistance < ringRadius - mCurrRingRadius && !touchOnCenterCircle(centerX, centerY, centerRadius + mCurrRingRadius + mPaddingVal, touchX, touchY);
    }
    /**
     * 获取边境坐标点
     * @param a 中心点
     * @param b 触摸位置的点
     * @param cutRadius 大圆坐标
     * @param offsetRadius 内偏移坐标
     * @return 边境坐标点
     */
    public static Point getBorderPoint(Point a, Point b, int cutRadius, int offsetRadius) {
        double radian = getRadian(a, b);
        Log.i("POINT", radian + "");
        return new Point(a.x + (int) ((cutRadius - offsetRadius * 2) * Math.cos(radian)), a.x
                + (int) ((cutRadius - offsetRadius * 2) * Math.sin(radian)));
    }

    /**
     * 获取角度 PI 的比值
     * @param a 中心点
     * @param b 触摸位置的点
     * @return 角度 PI 的比值
     */
    public static double getRadian(Point a, Point b) {
        float lenA = b.x - a.x;
        float lenB = b.y - a.y;
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        double ang = Math.acos(lenA / lenC);
        ang = ang * (b.y < a.y ? -1 : 1);
//        if (ang > 5/18*Math.PI && ang < Math.PI/2)
//            ang = 5/18f*Math.PI;
//        else if (ang >= Math.PI/2 && ang < 13/18f*Math.PI)
//            ang = 13/18f*Math.PI;
        return ang;
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
