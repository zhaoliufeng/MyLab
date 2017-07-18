package me.zhaoliufeng.mylab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.zhaoliufeng.customviews.SwitchButton;
import me.zhaoliufeng.toolslib.ToastUtils;

public class SwitchButtonActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_button);
        SwitchButton switchButton = (SwitchButton) findViewById(R.id.sbtn);
        switchButton.setSelectChangeListener(new SwitchButton.SelectChangeListener() {
            @Override
            public void onChange(boolean isOpen) {
                ToastUtils.showToast(getBaseContext(), isOpen ? "开" : "关");
            }
        });
    }
}
