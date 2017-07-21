package me.zhaoliufeng.mylab.CustomButton;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.zhaoliufeng.customviews.Button.IOSSwitchView;
import me.zhaoliufeng.customviews.Button.SwitchButton;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

public class SwitchButtonActivity extends AppCompatActivity {

    IOSSwitchView iosSwitchView;
    SwitchButton switchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_switch_button);
        iosSwitchView = (IOSSwitchView) findViewById(R.id.ios_btn);

        switchButton = (SwitchButton) findViewById(R.id.round_btn);
        switchButton.setSelectChangeListener(new SwitchButton.SelectChangeListener() {
            @Override
            public void onChange(boolean isOpen) {
                ToastUtils.showToast(getBaseContext(), isOpen ? "开" : "关");
            }
        });

        iosSwitchView.setState(true);
        iosSwitchView.setOnStateChangedListener(new IOSSwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn() {
                iosSwitchView.toggleSwitch(true);
                ToastUtils.showToast(getBaseContext(), "开");
            }

            @Override
            public void toggleToOff() {
                iosSwitchView.toggleSwitch(false);
                ToastUtils.showToast(getBaseContext(), "关");
            }
        });
    }

    public void toggleOnClick(View view) {
        iosSwitchView.toggleSwitch(iosSwitchView.getState() == 1);
        switchButton.setChecked();
    }

    public void stateOnClick(View view) {
        iosSwitchView.setState(iosSwitchView.getState() == 1);
    }
}
