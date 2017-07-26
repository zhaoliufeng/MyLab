package me.zhaoliufeng.customviews.Shader;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ComposeShader;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RadialGradient;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by We-Smart on 2017/7/26.
 */

public class ColorShader extends View {


    int[] colors =  new int[] {
            0xFFFF0000, 0xFFFF7F00, 0xFFFFFF00, 0xFF7FFF00,
            0xFF00FF00, 0xFF00FF7F, 0xFF00FFFF, 0xFF007FFF,
            0xFF0000FF, 0xFF7F00FF, 0xFFFF00FF, 0xFFFF007F,
            0xFFFF0000}; //色环颜色值

    private Shader sweepShader; //扫描渲染
    private Shader radialShader;    //环形渲染
    private Paint mPaint;   //画笔
    private Paint mCpaint; //中心圆
    public ColorShader(Context context) {
        super(context);
    }

    public ColorShader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);

        mCpaint = new Paint();
        mCpaint.setAntiAlias(true);
        mCpaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));

        setLayerType(View.LAYER_TYPE_SOFTWARE, null);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        sweepShader = new SweepGradient(getWidth()/2, getHeight()/2, colors, null);
        radialShader = new RadialGradient(getWidth()/2, getHeight()/2, getHeight()/2, new int[]{Color.WHITE, Color.parseColor("#00ffffff")}, null, Shader.TileMode.CLAMP);
        ComposeShader shader = new ComposeShader(sweepShader, radialShader, PorterDuff.Mode.LIGHTEN);
        Matrix matrix = new Matrix();
        matrix.setRotate(-90, getMeasuredWidth()/2, getMeasuredHeight()/2);
        shader.setLocalMatrix(matrix);
        mPaint.setShader(shader);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.drawCircle(getWidth()/2, getHeight()/2, getHeight()/2, mPaint);

        canvas.drawCircle(getWidth()/2, getHeight()/2, getHeight()/4, mCpaint);
    }
}
