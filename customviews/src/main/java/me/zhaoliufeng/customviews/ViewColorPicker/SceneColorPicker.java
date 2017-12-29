package me.zhaoliufeng.customviews.ViewColorPicker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.Shader;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import me.zhaoliufeng.customviews.ColorPickView;
import me.zhaoliufeng.customviews.R;
import me.zhaoliufeng.toolslib.MathUtil;

/**
 * Created by zhaol on 2017/11/23.
 */

public class SceneColorPicker extends View {

    private Paint mBitmapPaint; //图片画笔
    private Paint mCirclePaint;  //圆形画笔
    private Paint mCurrRingPaint;   //指示圆画笔
    private Bitmap mBitmapBg;
    private Point mCurrRingPoint;   //指示圆位置
    private int mCurrRingRadius = 18;    //指示圆环半径
    private Shader mShader; //冷暖渲染
    private int[] doughnutColors = {0xfffffefc, 0xffffdc92, 0xffffb61a};
    private int mWidth; //半径
    public static int RGB_MODE = 0;
    public static int WC_MODE = 1;
    public static int WARM_MODE = 2;
    public static int COLD_MODE = 3;
    private int mMode = WC_MODE;
    private OnColorChangedListener listener; // 小球移动的监听
    private int length; // 小球到中心位置的距离
    private double angel = 0.0 /* 0 - 360.0 */,
            linePercent = 1.0 /* 0 - 1.0 */,
            radio = 1.0 /* 0 - 1.0 */;
    private float[] mHSB = new float[3];

    public SceneColorPicker(Context context) {
        super(context);
        init();
    }

    public SceneColorPicker(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mBitmapPaint = new Paint();
        mBitmapPaint.setAntiAlias(true);

        mCirclePaint = new Paint();
        mCirclePaint.setAntiAlias(true);

        mCurrRingPaint = new Paint();
        mCurrRingPaint.setAntiAlias(true);
        mCurrRingPaint.setAntiAlias(true);
        mCurrRingPaint.setStyle(Paint.Style.STROKE);
        mCurrRingPaint.setStrokeWidth(5);
        mCurrRingPaint.setColor(Color.parseColor("#FFEEEEEE"));

        mCurrRingPoint = new Point();

        mBitmapBg = BitmapFactory.decodeResource(getResources(), R.drawable.hsb_circle_hard);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        mWidth = getMeasuredWidth();
        mShader = new LinearGradient(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight(), doughnutColors, null, Shader.TileMode.CLAMP);
        mCirclePaint.setShader(mShader);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
        mCurrRingPoint.set(mWidth / 2, mWidth / 2);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mBitmapBg != null)
            mBitmapBg = zoomImg(mBitmapBg, w, 0);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMode == RGB_MODE) {
            canvas.drawBitmap(mBitmapBg, 0, 0, mBitmapPaint);
        } else {
            canvas.drawCircle(mWidth / 2, mWidth / 2, mWidth / 2, mCirclePaint);
        }

        //画指示圆
        if (mMode == RGB_MODE || mMode == WC_MODE)
            canvas.drawCircle(mCurrRingPoint.x, mCurrRingPoint.y, mCurrRingRadius, mCurrRingPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
            case MotionEvent.ACTION_UP:// 抬起
            case MotionEvent.ACTION_MOVE: // 移动
                length = getLength(event.getX(), event.getY(), getWidth() / 2, getHeight() / 2);
                if (length <= mWidth / 2 - mCurrRingRadius) {
                    mCurrRingPoint.set((int) event.getX(), (int) event.getY());
                } else {
                    mCurrRingPoint = getBorderPoint(new Point(getWidth() / 2, getHeight() / 2), new Point(
                            (int) event.getX(), (int) event.getY()), (mWidth / 2 - mCurrRingRadius - 5));
                }

                double cX = getWidth() / 2,
                        cY = getWidth() / 2,
                        pX = mCurrRingPoint.x,
                        pY = mCurrRingPoint.y;

                angel = MathUtil.Angel(pX - cX, pY - cY) + 90; // [-180, 180] => [0, 360]
                if (angel < 0)
                    angel += 360.0;

                double deltaX = Math.abs(cX - pX), deltaY = (cY - pY);
                radio = Math.pow(deltaX * deltaX + deltaY * deltaY, 0.5) / getWidth() / 2 * 2.0;
                if (radio <= 0) radio = 0;
                if (radio >= 1.0) radio = 1.0;

                if (mMode == RGB_MODE) {
                    final double hue = angel / 360.0,
                            sat = radio,
                            brt = linePercent;
                    mHSB[0] = (float) hue;
                    mHSB[1] = (float) sat;
                    mHSB[2] = (float) brt;
                    if (listener != null)
                        listener.onColorChange(mHSB, event.getAction() == MotionEvent.ACTION_UP);
                } else if (mMode == WC_MODE) {
                    int warmVal = (int) ((pY - mCurrRingRadius) / (getHeight() - 2 * mCurrRingRadius) * 100);
                    if (listener != null)
                        listener.onWarmChange(warmVal, event.getAction() == MotionEvent.ACTION_UP);
                }
                break;
            default:
                break;
        }
        postInvalidate();
        return true;
    }

    public void setMode(int mode) {
        mMode = mode;
        if (mode == WARM_MODE || mode == COLD_MODE) {
            mCirclePaint.setShader(null);
            if (mode == WARM_MODE) {
                mCirclePaint.setColor(Color.parseColor("#ffffb61a"));
            } else {
                mCirclePaint.setColor(Color.parseColor("#ffffffff"));
            }
        } else if (mode == WC_MODE) {
            mCirclePaint.setShader(mShader);
        }
        postInvalidate();
    }

    private Bitmap zoomImg(Bitmap bm, int newWidth, int newHeight) {
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

    private int getLength(float x1, float y1, float x2, float y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    private Point getBorderPoint(Point a, Point b, int cutRadius) {
        float radian = getRadian(a, b);
        return new Point(a.x + (int) (cutRadius * Math.cos(radian)), a.x
                + (int) (cutRadius * Math.sin(radian)));
    }

    private float getRadian(Point a, Point b) {
        float lenA = b.x - a.x;
        float lenB = b.y - a.y;
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        float ang = (float) Math.acos(lenA / lenC);
        ang = ang * (b.y < a.y ? -1 : 1);
        return ang;
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.listener = listener;
    }

    public interface OnColorChangedListener {
        void onColorChange(float[] hsb, boolean reqUpdate);

        void onWarmChange(int warm, boolean reqUpdate);
    }
}
