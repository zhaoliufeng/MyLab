package me.zhaoliufeng.mylab.DrawerPlusLayout;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import me.zhaoliufeng.customviews.DrawerPlusLayout.DrawerPlusLayout;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

public class DrawerPlusActivity extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.drawer_plus_layout)
    DrawerPlusLayout drawerPlusLayout;
    @BindView(R.id.tv1)
    TextView tv1;
    @BindView(R.id.tv2)
    TextView tv2;
    @BindView(R.id.tv3)
    TextView tv3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drawer_plus);
        ButterKnife.bind(this);
        //为侧拉菜单添加事件监听
        drawerPlusLayout.setDrawerStatusListener(new DrawerPlusLayout.DrawerStatusListener() {
            @Override
            public void onDrawerStatusChange(boolean open) {
                if (open)
                    ToastUtils.showToast(getBaseContext(), "菜单打开");
                else
                    ToastUtils.showToast(getBaseContext(), "菜单关闭");
            }
        });
    }

    public void drawerBtnOnClick(View view) {
        ToastUtils.showToast(getBaseContext(), "菜单组件点击");
    }

    @OnClick({R.id.tv1, R.id.tv2, R.id.tv3})
    @Override
    public void onClick(View v) {
        TextView tv = (TextView)v;
        ToastUtils.showToast(getBaseContext(), tv.getText().toString());
    }
}
