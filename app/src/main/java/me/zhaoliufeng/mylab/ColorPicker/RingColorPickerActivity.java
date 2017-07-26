package me.zhaoliufeng.mylab.ColorPicker;

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
            public void colorChange(int colorVal, boolean isUp) {

            }
        });
    }

    public void onClick(View view) {
        if (ringColorPicker.getMode() == RingColorPicker.MODE.ONE_CHANNEL_MODE){
            ringColorPicker.changeMode(RingColorPicker.MODE.TWO_CHANNEL_MODE);
        }else {
            ringColorPicker.changeMode(RingColorPicker.MODE.ONE_CHANNEL_MODE);
        }
    }

    public void colorOnClick(View view) {
        ringColorPicker.changeMode(RingColorPicker.MODE.FIVE_CHANNEL_MODE);

    }
}
