package me.zhaoliufeng.customviews.CoutomClickView;

import android.content.Context;
import android.content.ReceiverCallNotAllowedException;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import me.zhaoliufeng.toolslib.ToastUtils;

/**
 * Created by We-Smart on 2017/7/21.
 */

public class ClickViewV2 extends RelativeLayout {

    private static final int ON_DOWN = 0;

    private static final int ON_PRESS = 10;

    private int clickCount = 0;
    private boolean msgSended = false;
    private boolean longPressExecute = false;

    public ClickViewV2(Context context) {
        super(context);
    }

    public ClickViewV2(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()){
            case MotionEvent.ACTION_DOWN:
                longPressExecute = false;
                handler.sendEmptyMessage(ON_DOWN);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                if (!longPressExecute)
                    clickCount++;
                break;
            case MotionEvent.ACTION_CANCEL:
                Log.i("CLICK", "取消触摸");
                break;
        }
        return true;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case ON_DOWN:
                    if (!msgSended){
                        //开始发送600ms延时的手势监听
                        handler.sendEmptyMessageDelayed(ON_PRESS, 600);
                        msgSended = true;
                    }
                    break;
                case ON_PRESS:
                    //执行手势
                    switch (clickCount){
                        case 0:
                            //长按
                            Log.i("CLICK", "长按");
                            ToastUtils.showToast(getContext(), "长按");
                            longPressExecute = true;
                            if (mOnPressEventListener != null)
                                mOnPressEventListener.onLongPress();
                            break;
                        case 1:
                            //单击
                            Log.i("CLICK", "单击");
                            ToastUtils.showToast(getContext(), "单击");
                            if (mOnPressEventListener != null)
                                mOnPressEventListener.onSingeClick();
                            break;
                        case 2:
                            //双击
                            Log.i("CLICK", "双击");
                            ToastUtils.showToast(getContext(), "双击");
                            if (mOnPressEventListener != null)
                                mOnPressEventListener.onDoubleClick();
                            break;
                    }
                    clickCount = 0;
                    msgSended = false;
                    break;
            }
        }
    };
    private OnPressEventListener mOnPressEventListener;

    public void setOnPressEventListener(OnPressEventListener onPressEventListener) {
        this.mOnPressEventListener = onPressEventListener;
    }

    public interface OnPressEventListener {
        void onSingeClick();

        void onDoubleClick();

        void onLongPress();
    }
}
