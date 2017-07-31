package me.zhaoliufeng.mylab.ColorPicker;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import me.zhaoliufeng.customviews.ViewColorPicker.RingColorPicker;
import me.zhaoliufeng.mylab.R;
import me.zhaoliufeng.toolslib.ToastUtils;

public class RingColorPickerActivity extends AppCompatActivity {

    private RingColorPicker ringColorPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_color_picker);
        ringColorPicker = (RingColorPicker) findViewById(R.id.colorPicker);
        ringColorPicker.setValChangeListener(new RingColorPicker.OnValChangeListener() {

            @Override
            public void warmChange(int warmVal, boolean isUp) {
                Log.i("WARM", warmVal + "");
            }

            @Override
            public void colorChange(float[] hsb, boolean isUp) {
                Log.i("WARM", "Switch " + hsb[0] + "   " + hsb[1] );
            }

            @Override
            public void switchChange(boolean isOpen) {
                Log.i("WARM", "Switch " + isOpen);
            }
        });
    }

    public void onClick(View view) {
        if (ringColorPicker.getMode() == RingColorPicker.MODE.ONE_CHANNEL_MODE){
            ringColorPicker.changeMode(RingColorPicker.MODE.TWO_CHANNEL_MODE);
        }else {
            ringColorPicker.changeMode(RingColorPicker.MODE.ONE_CHANNEL_MODE);
        }
        Log.i("TIME", getAlarmShow(23, 0));
    }

    public void colorOnClick(View view) {
        ringColorPicker.changeMode(RingColorPicker.MODE.FIVE_CHANNEL_MODE);
        Log.i("TIME", getAlarmShow(12, 0));
    }

    public void changeOnClick(View view) {
        ringColorPicker.setOneChannelColor(Color.parseColor("#FC4C2F"));
        Log.i("TIME", getAlarmShow(0, 0));
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
