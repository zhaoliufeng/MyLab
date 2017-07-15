package me.zhaoliufeng.mylab;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import me.zhaoliufeng.customviews.SeekBar;

public class SeekBarActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seekbar);
        SeekBar seekBar = (SeekBar)findViewById(R.id.seekbar);
        seekBar.setPosition(50);
    }
}
