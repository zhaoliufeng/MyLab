package me.zhaoliufeng.customviews;

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
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import me.zhaoliufeng.toolslib.MathUtil;
import me.zhaoliufeng.toolslib.ValueWrappers;


/**
 * Created by zxd on 2016/09/28.
 */
public class ColorPickView extends View {
    private Context context;
    private int bigCircle; // 外圈半径
    private int rudeRadius = 18; // 可移动小球的半径
    private int centerColor; // 可移动小球的颜色
    private int rudeStrokeWidth = 5;
    private Bitmap bitmapBack; // 背景图片
    private Paint mPaint; // 背景画笔
    private Paint mCenterPaint; // 可移动小球画笔
    private Point centerPoint;// 中心位置
    private Point mRockPosition;// 小球当前位置
    private OnColorChangedListener listener; // 小球移动的监听
    private int length; // 小球到中心位置的距离
    private double angel = 0.0 /* 0 - 360.0 */,
            linePercent = 1.0 /* 0 - 1.0 */,
            radio = 1.0 /* 0 - 1.0 */;
    private float[] mHSB = new float[3];
    private int RGB_MODE = 0;
    private int WC_MODE = 1;
    private int WARM_MODE = 2;
    private int COLD_MODE = 3;
    private int mMode = WC_MODE;
    private Shader mShader;
    //渐变色圆
    private int[] doughnutColors = {0xfffffefc, 0xffffdc92, 0xffffb61a};

    public ColorPickView(Context context) {
        super(context);
        init();
    }

    public ColorPickView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public void setOnColorChangedListener(OnColorChangedListener listener) {
        this.listener = listener;
    }

    private void init() {
        // 获取自定义组件的属性
        centerColor = Color.WHITE;
        // 初始化背景画笔和可移动小球的画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.parseColor("#ffffb61a"));

        mCenterPaint = new Paint();
        mCenterPaint.setColor(centerColor);
        mCenterPaint.setStrokeWidth(rudeStrokeWidth);
        mCenterPaint.setStyle(Paint.Style.STROKE);

        mRockPosition = new Point();
        centerPoint = new Point();
        bitmapBack = BitmapFactory.decodeResource(getResources(), R.drawable.hsb_circle_hard);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        mShader = new LinearGradient(getMeasuredWidth() / 2, 0, getMeasuredWidth() / 2, getMeasuredHeight(), doughnutColors, null, Shader.TileMode.CLAMP);

        centerPoint.set(getMeasuredWidth()/2, getMeasuredHeight()/2);
        mRockPosition.set(centerPoint.x, centerPoint.y);
        // 视图大小设置为直径
        setMeasuredDimension(getMeasuredWidth(), getMeasuredHeight());
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        bigCircle = w / 2;
        // 将背景图片大小设置为属性设置的直径
        bitmapBack = zoomImg(bitmapBack, w, 0);
    }

    public void changMode(int mode) {
        mMode = mode;
        if (mMode == RGB_MODE) {
            mCenterPaint.setColor(0xFFFFFFFF);
        } else if (mMode == WC_MODE) {
            mPaint.setShader(mShader);
        } else if (mMode == WARM_MODE) {
            mPaint.setShader(null);
            mPaint.setColor(Color.parseColor("#ffffb61a"));
        } else {
            mPaint.setShader(null);
            mPaint.setColor(Color.parseColor("#ffffffff"));
        }

        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mMode == RGB_MODE) {
            // 画背景图片
            canvas.drawBitmap(bitmapBack, 0, 0, mPaint);
        } else if (mMode == WC_MODE) {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        } else {
            canvas.drawCircle(getWidth() / 2, getHeight() / 2, getWidth() / 2, mPaint);
        }

        if (mMode == RGB_MODE || mMode == WC_MODE) {
            // 画中心小球
            canvas.drawCircle(mRockPosition.x, mRockPosition.y, rudeRadius,
                    mCenterPaint);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        final ValueWrappers.Bool reqUpdate = new ValueWrappers.Bool(false);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN: // 按下
            case MotionEvent.ACTION_UP:// 抬起
                reqUpdate.value = true;
            case MotionEvent.ACTION_MOVE: // 移动
                length = getLength(event.getX(), event.getY(), centerPoint.x,
                        centerPoint.y);
                if (length <= bigCircle - rudeRadius) {
                    mRockPosition.set((int) event.getX(), (int) event.getY());
                } else {
                    mRockPosition = getBorderPoint(centerPoint, new Point(
                            (int) event.getX(), (int) event.getY()), (bigCircle - rudeRadius - 5));
                }

                double cX = centerPoint.x,
                        cY = centerPoint.y,
                        pX = event.getX(),
                        pY = event.getY();

                angel = MathUtil.Angel(pX - cX, pY - cY) + 90; // [-180, 180] => [0, 360]
                if (angel < 0)
                    angel += 360.0;

                double deltaX = Math.abs(cX - pX), deltaY = (cY - pY);
                radio = Math.pow(deltaX * deltaX + deltaY * deltaY, 0.5) / bigCircle * 2.0;
                if (radio <= 0) radio = 0;
                if (radio >= 1.0) radio = 1.0;

                if (mMode == RGB_MODE) {
                    final double hue = angel / 360.0,
                            sat = radio,
                            brt = linePercent;
                    mHSB[0] = (float) hue;
                    mHSB[1] = (float) sat;
                    mHSB[2] = (float) brt;

                    listener.onColorChange(mHSB, reqUpdate.value);
                } else if (mMode == WC_MODE) {
                    int warmVal = (int) ((cY - rudeRadius) / (getHeight() - 2 * rudeRadius) * 100);
                    listener.onWarmChange(warmVal, reqUpdate.value);
                }
                break;
            default:
                break;
        }
        invalidate(); // 更新画布
        return true;
    }


    /**
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    public static int getLength(float x1, float y1, float x2, float y2) {
        return (int) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * @param a
     * @param b
     * @param cutRadius
     * @return
     */
    public static Point getBorderPoint(Point a, Point b, int cutRadius) {
        float radian = getRadian(a, b);
        return new Point(a.x + (int) (cutRadius * Math.cos(radian)), a.x
                + (int) (cutRadius * Math.sin(radian)));
    }

    /**
     * @param a
     * @param b
     * @return
     */
    public static float getRadian(Point a, Point b) {
        float lenA = b.x - a.x;
        float lenB = b.y - a.y;
        float lenC = (float) Math.sqrt(lenA * lenA + lenB * lenB);
        float ang = (float) Math.acos(lenA / lenC);
        ang = ang * (b.y < a.y ? -1 : 1);
        return ang;
    }

    public static Bitmap zoomImg(Bitmap bm, int newWidth , int newHeight){
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

    public void setPoint(float[] hsb) {
        Log.i("color", "h == " + hsb[0] + ",,s==" + hsb[1] + ",,b==" + hsb[2]);
        float angle = hsb[0];
        float currLength = hsb[1] * bigCircle;
        float y = 0;
        float x = 0;
        if (0 <= angle && angle <= 90) {
            y = (float) (centerPoint.y - Math.cos(angle * Math.PI / 180) * currLength);
            x = (float) (Math.sin(angle * Math.PI / 180) * currLength + centerPoint.x);
        } else if (angle > 90 && angle <= 180) {
            x = (float) (Math.cos((angle - 90) * Math.PI / 180) * currLength + centerPoint.x);
            y = (float) (Math.sin((angle - 90) * Math.PI / 180) * currLength + centerPoint.y);
        } else if (angle > 180 && angle <= 270) {
            x = (float) (-Math.sin((angle - 180) * Math.PI / 180) * currLength + centerPoint.x);
            y = (float) (Math.cos((angle - 180) * Math.PI / 180) * currLength + centerPoint.y);
        } else {
            y = (float) (-Math.sin((angle - 270) * Math.PI / 180) * currLength + centerPoint.y);
            x = (float) (-Math.cos((angle - 270) * Math.PI / 180) * currLength + centerPoint.x);
        }

        length = getLength(x, y, centerPoint.x,
                centerPoint.y);
        if (length <= bigCircle - rudeRadius) {
            mRockPosition.set((int) x, (int) y);
        } else {
            mRockPosition = getBorderPoint(centerPoint, new Point(
                    (int) x, (int) y), bigCircle - rudeRadius - 5);
        }

        angel = MathUtil.Angel(x - centerPoint.x, y - centerPoint.y) + 90; // [-180, 180] => [0, 360]
        if (angel < 0)
            angel += 360.0;

        double deltaX = Math.abs(centerPoint.x - x), deltaY = (centerPoint.y - y);
        radio = Math.pow(deltaX * deltaX + deltaY * deltaY, 0.5) / bigCircle * 2.0;
        if (radio <= 0) radio = 0;
        if (radio >= 1.0) radio = 1.0;

        final double hue = angel / 360.0,
                sat = radio,
                brt = linePercent;
//		Util.UIColor c = new Util.UIColor(hue, sat, brt);
        mHSB[0] = (float) hue;
        mHSB[1] = (float) sat;
        mHSB[2] = (float) brt;

//		listener.onColorChange(mHSB, true);
        invalidate();

    }

    // 颜色发生变化的回调接口
    public interface OnColorChangedListener {
        void onColorChange(float[] hsb, boolean reqUpdate);

        void onWarmChange(int warm, boolean reqUpdate);
    }
}
