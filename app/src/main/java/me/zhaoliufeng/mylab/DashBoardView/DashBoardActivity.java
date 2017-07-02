package me.zhaoliufeng.mylab.DashBoardView;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.LinearInterpolator;

import butterknife.BindView;
import butterknife.ButterKnife;
import me.zhaoliufeng.customviews.DashBoardView.DashboardView;
import me.zhaoliufeng.mylab.R;

public class DashBoardActivity extends AppCompatActivity {

    @BindView(R.id.db_view)
    DashboardView dbView;
    @BindView(R.id.db_view_s)
    DashboardView dbViewS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ButterKnife.bind(this);
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    handler.sendEmptyMessageDelayed(0, 1000);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();

    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            ObjectAnimator animator = ObjectAnimator.ofInt(dbView, "mRealTimeValue",
                    dbView.getVelocity(), (int) (Math.random() * 180));
            animator.setDuration(1500).setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    dbView.setVelocity(value);
                }
            });
            animator.start();
            animator = ObjectAnimator.ofInt(dbViewS, "mRealTimeValue",
                    dbViewS.getVelocity(), (int) (Math.random() * 180));
            animator.setDuration(1500).setInterpolator(new LinearInterpolator());
            animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    int value = (int) animation.getAnimatedValue();
                    dbViewS.setVelocity(value);
                }
            });
            animator.start();
        }
    };
}
