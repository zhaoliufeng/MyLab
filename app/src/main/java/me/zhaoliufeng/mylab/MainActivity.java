package me.zhaoliufeng.mylab;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import me.zhaoliufeng.mylab.BootPage.BootPageActivity;
import me.zhaoliufeng.mylab.Chart.ChartActivity;
import me.zhaoliufeng.mylab.ColorPicker.ColorPickerActivity;
import me.zhaoliufeng.mylab.ColorPicker.RingColorPickerActivity;
import me.zhaoliufeng.mylab.CustomButton.SwitchButtonActivity;
import me.zhaoliufeng.mylab.CustomSeekBar.SeekBarActivity;
import me.zhaoliufeng.mylab.DashBoardView.DashBoardActivity;
import me.zhaoliufeng.mylab.DrawerPlusLayout.DrawerPlusActivity;
import me.zhaoliufeng.mylab.ItemMask.ItemMaskActivity;
import me.zhaoliufeng.mylab.NumTextView.NumTextViewActivity;
import me.zhaoliufeng.mylab.PayPwView.PayPwActivity;
import me.zhaoliufeng.mylab.lab.LabActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**数字增长动画控件*/
    public void numTextViewOnClick(View view) {
        startActivity(new Intent(this, NumTextViewActivity.class));
    }

    public void itemMaskOnClick(View view) {
        startActivity(new Intent(this, ItemMaskActivity.class));
    }

    public void labOnClick(View view) {
        startActivity(new Intent(this, LabActivity.class));
    }

    public void drawerPlusOnClick(View view) {
        startActivity(new Intent(this, DrawerPlusActivity.class));
    }

    public void bootPageOnClick(View view) {
        startActivity(new Intent(this, BootPageActivity.class));
    }

    public void payPwOnClick(View view) {
        startActivity(new Intent(this, PayPwActivity.class));
    }

    public void chartOnClick(View view) {
        startActivity(new Intent(this, ChartActivity.class));
    }

    public void boardOnClick(View view) {
        startActivity(new Intent(this, DashBoardActivity.class));
    }

    public void switchOnClick(View view) {
        startActivity(new Intent(this, SwitchButtonActivity.class));
    }

    public void colorOnClick(View view) {
        startActivity(new Intent(this, ColorPickerActivity.class));
    }

    public void seekOnClick(View view) {
        startActivity(new Intent(this, SeekBarActivity.class));
    }

    public void ringOnClick(View view) {
        startActivity(new Intent(this, RingColorPickerActivity.class));
    }

    public void clickOnClick(View view) {
        startActivity(new Intent(this, MulitClickActivity.class));
    }
}
