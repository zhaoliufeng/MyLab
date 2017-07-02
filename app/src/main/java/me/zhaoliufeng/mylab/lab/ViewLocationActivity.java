package me.zhaoliufeng.mylab.lab;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTouch;
import me.zhaoliufeng.mylab.R;

/**
 * 实验
 *  比较系统 getLocationOnScreen 和 getLocationOnWindow 的区别
 * 结论：
 *  getLocationOnScreen 获取视图在屏幕中的位置 对于屏幕的定义及整个手机屏幕 位置坐标的起点在手机屏幕左上方
 *  getLocationOnWindow 获取视图在窗口中的位置 对于窗口的定义及一个Activity或是Dialog
 *  因为Activity所在窗口一般情况下铺满全屏，所以获取到的坐标位置与OnScreen相同
 *  当窗口为一个dialog或是改变窗口大小时，两者所得的值会有变化
 */
public class ViewLocationActivity extends AppCompatActivity implements View.OnTouchListener {

    private static final String TAG = "ViewLocationActivity";

    @BindView(R.id.view1)
    View view1;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_window)
    TextView tvWindow;
    @BindView(R.id.tv_screen)
    TextView tvScreen;

    private final float widthScale = 0.7f;
    private final float heightScale = 0.8f;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_location);
        ButterKnife.bind(this);
        WindowManager m = getWindowManager();
        Display d = m.getDefaultDisplay();  //为获取屏幕宽、高
        WindowManager.LayoutParams p = getWindow().getAttributes();  //获取对话框当前的参数值
        p.height = (int) (d.getHeight() * heightScale);   //高度设置为屏幕的1.0
        p.width = (int) (d.getWidth() * widthScale);    //宽度设置为屏幕的0.8
        p.alpha = 1.0f;      //设置本身透明度
        p.dimAmount = 0.0f;      //设置黑暗度
        getWindow().setAttributes(p);
    }

    @OnTouch({R.id.view1, R.id.view2})
    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:

                break;
            case MotionEvent.ACTION_MOVE:
                Log.e(TAG, getWindow().getWindowManager().getDefaultDisplay().getWidth() + "");
                if (event.getRawX()*widthScale > (getWindow().getWindowManager().getDefaultDisplay().getWidth()*widthScale - +v.getWidth())) {
                    break;
                }
                Log.e(TAG, event.getRawX() + "");
                v.setX(event.getRawX()*widthScale);
                int[] loc = new int[2];
                v.getLocationInWindow(loc);
                tvWindow.setText("Window X : " + loc[0] + " Y : " + loc[1]);
                v.getLocationOnScreen(loc);
                tvScreen.setText("Screen X : " + loc[0] + " Y : " + loc[1]);
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }
}
