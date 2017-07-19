package me.zhaoliufeng.customviews;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.RelativeLayout;

import me.zhaoliufeng.toolslib.ToastUtils;

/**
 * Created by We-Smart on 2017/7/19.
 */

public class ClickView extends RelativeLayout {

    private static final int CLEAR_TIME = 3;
    private long pressTime;
    private Point currPoint;
    private static final int DOUBLE_CLICK = 0;
    private static final int LONG_PRESS = 1;
    private static final int SINGLE_CLICK = 2;

    private static final int CANCEL_DOUBLE_CLICK = 10;
    private static final int CANCEL_LONG_PRESS = 11;
    private static final int CANCEL_SINGLE_CLICK = 12;

    private static final int DO_DOUBLE_CLICK = 20;
    private static final int DO_LONG_PRESS = 21;
    private static final int DO_SINGLE_CLICK = 22;

    private boolean isDown = false;

    public ClickView(Context context) {
        super(context);
    }

    public ClickView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isDown = true;
                handler.sendEmptyMessage(LONG_PRESS);

                Message msg = new Message();
                msg.what = DOUBLE_CLICK;
                Bundle bundle = new Bundle();
                bundle.putLong("PressTime", System.currentTimeMillis());
                msg.setData(bundle);
                handler.sendMessage(msg);

                Message sMsg = new Message();
                sMsg.what = SINGLE_CLICK;
                handler.sendMessage(sMsg);
                break;
            case MotionEvent.ACTION_MOVE:

                break;
            case MotionEvent.ACTION_UP:
                handler.sendEmptyMessage(CANCEL_LONG_PRESS);

                isDown = false;
                break;
        }
        return true;
    }

    private long time;
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LONG_PRESS:
                    handler.sendEmptyMessageDelayed(DO_LONG_PRESS, 1000);
                    break;
                case CANCEL_LONG_PRESS:
                    handler.removeMessages(DO_LONG_PRESS);
                    break;
                case DO_LONG_PRESS:
                    ToastUtils.showToast(getContext(), "长按");
                    break;
                case DOUBLE_CLICK:
                    long currTime = msg.getData().getLong("PressTime");
                    if (time == 0){
                        time = currTime;
                        handler.sendEmptyMessageDelayed(CLEAR_TIME, 300);
                    }
                    else if (currTime - time < 300){
                        handler.sendEmptyMessage(DO_DOUBLE_CLICK);
                    }else {
                        time = 0;
                    }
                    break;
                case DO_DOUBLE_CLICK:
                    time = 0;
                    handler.sendEmptyMessage(CANCEL_SINGLE_CLICK);
                    ToastUtils.showToast(getContext(), "双击");
                    break;
                case SINGLE_CLICK:
                    handler.sendEmptyMessageDelayed(DO_SINGLE_CLICK, 300);
                    break;
                case CANCEL_SINGLE_CLICK:
                    Log.i("TIME", "CANCEL_SINGLE_CLICK");
                    handler.removeMessages(DO_SINGLE_CLICK);
                    break;
                case DO_SINGLE_CLICK:
                    if (!isDown)
                        ToastUtils.showToast(getContext(), "单击");
                    break;
                case CLEAR_TIME:
                    time = 0;
                    break;
            }
        }
    };
}
    //    @Override
//    public boolean onTouchEvent(MotionEvent event) {
//        switch (event.getAction()){
//            case MotionEvent.ACTION_DOWN:
//                currPoint.set((int) event.getX(),(int) event.getY());
//                if (pressTime == 0){
//                    pressTime = System.currentTimeMillis();
//                    handler.sendEmptyMessageDelayed(0, 300);
//                    Message longPressMsg = new Message();
//                    longPressMsg.what = LONG_PRESS;
//                    longPressMsg.arg1 = START_LONG_PRESS;
//                    //handler.sendMessageDelayed(longPressMsg, 600);
//                    Log.i("CLICK", pressTime + "");
//                }
//                else if (System.currentTimeMillis() - pressTime < 300){
//                    ToastUtils.showToast(getContext(), "双击");
//                    Log.i("CLICK", pressTime + "");
//                    pressTime = 0;
//                }else {
//                    pressTime = 0;
//                }
//                break;
//            case MotionEvent.ACTION_MOVE:
//
//                break;
//            case MotionEvent.ACTION_UP:
//                Message longPressMsg = new Message();
//                longPressMsg.what = LONG_PRESS;
//                longPressMsg.arg1 = CANCEL_LONG_PRESS;
//                //handler.sendMessage(longPressMsg);
//                break;
//        }
//        return true;
//    }
//
//    Handler handler = new Handler(){
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what){
//                case DOUBLE_CLICK:
//                    if (pressTime != 0){
//                        ToastUtils.showToast(getContext(), "单击");
//                        pressTime = 0;
//                    }
//                    break;
//                case LONG_PRESS:
//                    if (msg.arg1 == START_LONG_PRESS){
//                        handler.sendEmptyMessageDelayed(DO_LONG_PRESS, 600);
//                        pressTime = 0;
//                    }else {
//                        handler.removeMessages(DO_LONG_PRESS);
//                    }
//                    break;
//                case DO_LONG_PRESS:
//                    ToastUtils.showToast(getContext(), "长按");
//                    break;
//            }
//        }
//    };

//}
