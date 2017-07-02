package me.zhaoliufeng.mylab.lab;

import android.app.Activity;
import android.os.Bundle;

import me.zhaoliufeng.mylab.R;

/**
 * 实验
 * 测试自定义控件的接口函数调用顺序
 */

public class ViewDrawApiActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_draw_api);
    }
}
