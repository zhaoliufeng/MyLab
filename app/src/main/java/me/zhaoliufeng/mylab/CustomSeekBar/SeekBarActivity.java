package me.zhaoliufeng.mylab.CustomSeekBar;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.zhaoliufeng.customviews.CustomSeekBar.LightSeekBar;
import me.zhaoliufeng.customviews.CustomSeekBar.SeekBar;
import me.zhaoliufeng.mylab.R;

public class SeekBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setPosition(50);

        LightSeekBar lightSeekBar = (LightSeekBar) findViewById(R.id.lseekbar);
        lightSeekBar.setPosition(50);
    }
}
