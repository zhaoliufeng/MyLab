package me.zhaoliufeng.mylab.ColorPicker;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import me.zhaoliufeng.customviews.ViewColorPicker.RingColorPicker;
import me.zhaoliufeng.mylab.R;

public class RingColorPickerActivity extends AppCompatActivity {

    private RingColorPicker ringColorPicker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ring_color_picker);
        ringColorPicker = (RingColorPicker) findViewById(R.id.colorPicker);
       // ringColorPicker.changeMode();
    }

    public void onClick(View view) {
        ringColorPicker.changeMode(RingColorPicker.MODE.ONE_CHANNEL_MODE);
    }
}
