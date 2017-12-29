package me.zhaoliufeng.mylab.ColorPicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.zhaoliufeng.customviews.ViewColorPicker.RingColorPicker;
import me.zhaoliufeng.customviews.ViewColorPicker.SceneColorPicker;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

public class RingColorPickerActivity extends AppCompatActivity {

    private SceneColorPicker colorPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_color_picker);
        colorPicker = (SceneColorPicker) findViewById(R.id.colorPicker);
        colorPicker.setOnColorChangedListener(new SceneColorPicker.OnColorChangedListener() {
            @Override
            public void onColorChange(float[] hsb, boolean reqUpdate) {
                Log.i("color", hsb[0] + " " + hsb[1] + " " + hsb[2] + " " + reqUpdate);
            }

            @Override
            public void onWarmChange(int warm, boolean reqUpdate) {
                Log.i("color", warm + " " + reqUpdate);
            }
        });
//        ringColorPicker = (RingColorPicker) findViewById(R.id.colorPicker);
//        ringColorPicker.setValChangeListener(new RingColorPicker.OnValChangeListener() {
//
//            @Override
//            public void warmChange(int warmVal, boolean isUp) {
//                Log.i("WARM", warmVal + "");
//            }
//
//            @Override
//            public void colorChange(float[] hsb, boolean isUp) {
//                Log.i("WARM", "Switch " + hsb[0] + "   " + hsb[1] );
//            }
//
//            @Override
//            public void switchChange(boolean isOpen) {
//                Log.i("WARM", "Switch " + isOpen);
//            }
//        });
    }

    public void onClick(View view) {
       colorPicker.setMode(SceneColorPicker.WC_MODE);
    }

    public void colorOnClick(View view) {
        colorPicker.setMode(SceneColorPicker.RGB_MODE);
    }

    public void changeOnClick(View view) {
        colorPicker.setMode(SceneColorPicker.WARM_MODE);
    }

    public static String getAlarmShow(int hours, int minutes) {
        String data = "";
        if (hours >= 0 && hours < 12) {
            data = alarmShow(hours + 1) + ":" + alarmShow(minutes) + " AM";
        } else {
            data = alarmShow(hours - 11) + ":" + alarmShow(minutes) + "PM";
        }
        return data;
    }

    public static String alarmShow(int time) {
        if (time < 10) {
            return "0" + time;
        } else {
            return Integer.toString(time);
        }
    }
}
